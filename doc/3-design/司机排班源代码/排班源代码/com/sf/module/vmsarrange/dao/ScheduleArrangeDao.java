/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-6     方芳                        创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.quartz.Calendar;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sf.framework.base.IPage;
import com.sf.module.vmsarrange.domain.ScheduleArrange;
import com.sf.module.vmsarrange.util.ArrFileUtil;

/**
 * 配班管理dao实现类
 * 
 * @author 方芳 (350614) 2014-6-6
 */
public class ScheduleArrangeDao extends ArrBaseEntityDao<ScheduleArrange> implements
	IScheduleArrangeDao {
	//查询配班分页数据
	public IPage<ScheduleArrange> listPage(Long deptId, String arrangeNo,
			Integer valid,Integer isUsed, int pageSize, int pageIndex, Long userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduleArrange.class);
		if(null == deptId){
			deptId = -1L;
		}
		//添加权限控制
		ArrDeptTreeDaoSql.addDeptIdCriteria(dc, "{alias}.DEPT_ID",deptId, userId);
		if(null != valid){
			dc.add(Restrictions.eq("valid", valid));
		}
		if(!ArrFileUtil.isEmpty(arrangeNo)){
			dc.add(Restrictions.like("arrangeNo", arrangeNo,MatchMode.ANYWHERE));
		}
		if(null != isUsed){
			dc.add(Restrictions.eq("isUsed", isUsed));
		}
		dc.addOrder(Order.desc("arrangeType"));
		dc.addOrder(Order.desc("id"));
		return super.findPageBy(dc, pageSize, pageIndex);
	}
	//查询配班分页数据-班次下拉框
	public IPage<ScheduleArrange> listArrangePage(Long deptId,Integer arrangeType,String arrangeNo,int pageSize, int pageIndex, Long userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduleArrange.class);
		if(null == deptId){
			deptId = -1L;
		}
		//添加权限控制
		ArrDeptTreeDaoSql.addDeptIdCriteria(dc, "{alias}.DEPT_ID",deptId, userId);
		if(null != arrangeType){
			if(arrangeType.compareTo(1)==0 || arrangeType.compareTo(2)==0){
				dc.add(Restrictions.eq("arrangeType",2));
			}
		}
		if(!ArrFileUtil.isEmpty(arrangeNo)){
			dc.add(Restrictions.like("arrangeNo", arrangeNo,MatchMode.ANYWHERE));
		}
		dc.add(Restrictions.eq("valid", 1));
		dc.addOrder(Order.desc("arrangeType"));
		dc.addOrder(Order.desc("id"));
		return super.findPageBy(dc, pageSize, pageIndex);
	}
	//查找指定班次代码
	public int listArrange(String arrangeNo){
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduleArrange.class);
		dc.add(Restrictions.eq("arrangeNo", arrangeNo));
		return super.countBy(dc);
	}
	//获取报表数据总数
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Long listReportCount(Long deptId, String arrangeNo, Integer valid,Integer isUsed, 
			Long userId) {
		if(null == deptId){
			deptId = -1L;
		}
		String sqlBuffer = " select count(*) " +
						   " from tm_arr_schedule_arrange a " +
						   " left join tm_arr_schedule_info_arrange ia on ia.arrange_id = a.arrange_id " +
						   " where 1 = 1 ";
		//查询有权限的子网点
		if(userId == 1){
			sqlBuffer += " and a.dept_id in (select dept_id " +
						 " from tm_department " +
						 " where delete_flg = 0 " +
						 " start with dept_id = " + deptId +
						 " connect by prior dept_code = parent_dept_code) ";
		}else{
			sqlBuffer += " and a.dept_id in (select a.dept_id " +
						 " from ts_user_dept a, " +
						 "      (select t.dept_id " +
						 "          from tm_department t " +
						 "         where t.delete_flg = 0 " +
						 "         start with t.dept_id = " + deptId +
						 "        connect by prior t.dept_code = t.parent_dept_code) b " +
						 " where a.user_id = " + userId +
						 "   and b.dept_id = a.dept_id) ";
		}
		if(null != valid){
			sqlBuffer += " and a.valid = "+valid;
		}
		if(!ArrFileUtil.isEmpty(arrangeNo)){
			sqlBuffer += " and a.arrange_no like '%"+arrangeNo+"%' ";
		}
		if(null != isUsed){
			sqlBuffer += " and a.is_used = "+isUsed;
		}
		final String sql = sqlBuffer;
		return (Long) super.getHibernateTemplate()
		.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException {
				org.hibernate.Query query = session.createSQLQuery(sql);
				Object object = query.uniqueResult();
				if (object != null) {
					return ((BigDecimal)object).longValue();
				}
				return null;
			}
		});
	}
	//获取报表数据
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> listReport(Long deptId, String arrangeNo, Integer valid,Integer isUsed, 
			Long userId) {
		String sqlBuffer = " select a.arrange_id,a.arrange_no, " +
							" decode(a.valid,1,'有效','无效') valid, " +
							" a.arrange_type, " +
							" a.start_tm, " +
							" a.end_tm, " +
							" i.start_tm info_start_tm, " +
				            " i.end_tm info_end_tm, " +
				            " sd.dept_code||'/'||sd.dept_name start_dept, " +
				            " ed.dept_code||'/'||ed.dept_name end_dept, " +
				            " decode(i.valid,1,'有效','无效') info_valid, " +
				            " dd.dept_code||'/'||dd.dept_name dept, " +
				            " v.vehicle_code " +
							" from tm_arr_schedule_arrange a " +
							" left join tm_arr_schedule_info_arrange ia on ia.arrange_id = a.arrange_id " +
				            " left join tm_arr_schedule_info i on i.info_id = ia.info_id " +
				            " left join tm_department sd on sd.dept_id = i.start_dept_id " +
				            " left join tm_department ed on ed.dept_id = i.end_dept_id " +
				            " left join tm_department dd on dd.dept_id = i.dept_id " +
				            " left join vm_arr_vehicle v on v.vehicle_id = i.vehicle_id " +
							" where 1=1 ";
		//查询有权限的子网点
		if(userId == 1){
			sqlBuffer += " and a.dept_id in (select dept_id " +
						 " from tm_department " +
						 " where delete_flg = 0 " +
						 " start with dept_id = " + deptId +
						 " connect by prior dept_code = parent_dept_code) ";
		}else{
			sqlBuffer += " and a.dept_id in (select a.dept_id " +
						 " from ts_user_dept a, " +
						 "      (select t.dept_id " +
						 "          from tm_department t " +
						 "         where t.delete_flg = 0 " +
						 "         start with t.dept_id = " + deptId +
						 "        connect by prior t.dept_code = t.parent_dept_code) b " +
						 " where a.user_id = " + userId +
						 "   and b.dept_id = a.dept_id) ";
		}
		if(null != valid){
			sqlBuffer += " and a.valid = "+valid;
		}
		if(!ArrFileUtil.isEmpty(arrangeNo)){
			sqlBuffer += " and a.arrange_no like '%"+arrangeNo+"%' ";
		}
		if(null != isUsed){
			sqlBuffer += " and a.is_used = "+isUsed;
		}
		sqlBuffer += " order by a.arrange_type desc,a.arrange_id desc ";
		final String sql = sqlBuffer;
		return (List<java.util.Map>) super.getHibernateTemplate()
		.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				org.hibernate.Query query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	//查找机动班重复数据的条数
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Integer listRepeat(String startTm, String endTm, Long deptId,Long id) {
		String sqlBak = " select count(1) from tm_arr_schedule_arrange "
					  + " where start_tm = '"+startTm+"' and end_tm = '"+endTm+"' "
					  + " and  dept_id = "+deptId;
		//修改时不与自己比较
		if(null != id){
			sqlBak += " and arrange_id <> "+id;
		}
		final String sql = sqlBak;
		return (Integer) super.getHibernateTemplate()
		.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException {
				org.hibernate.Query query = session.createSQLQuery(sql);
				Object object = query.uniqueResult();
				if (object != null) {
					return ((BigDecimal)object).intValue();
				}
				return null;
			}
		});
	}
	/**
	 * 根据配班班次代码获取配班信息
	 * @param arrangeNo 配班班次代码集合
	 * @return
	 */
	public List<ScheduleArrange> findArrByArrangeNoList(List<String> arrangeNo,Long deptId,Long userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduleArrange.class);
		//添加权限控制
		ArrDeptTreeDaoSql.addDeptIdCriteria(dc, "{alias}.DEPT_ID",deptId, userId);
		dc.add(Restrictions.in("arrangeNo", arrangeNo));
		return super.findBy(dc);
	}
	/**
	 * 根据配班班次代码获取配班信息
	 * @param arrangeNo 配班班次代码
	 * @return
	 */
	public ScheduleArrange findArrByArrangeNo(String arrangeNo,Long deptId,Long userId) {
		ScheduleArrange arrange = null;
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduleArrange.class);
		//添加权限控制
		ArrDeptTreeDaoSql.addDeptIdCriteria(dc, "{alias}.DEPT_ID",deptId, userId);
		dc.add(Restrictions.eq("arrangeNo", arrangeNo));
		List<ScheduleArrange> list = super.findBy(dc);
		if(null != list && list.size()>0){
			arrange =  list.get(0);
		}
		return arrange;
	}
	public ScheduleArrange findArrByArr(String arrangeNo) {
		ScheduleArrange arrange = null;
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduleArrange.class);
		dc.add(Restrictions.eq("arrangeNo", arrangeNo));
		List<ScheduleArrange> list = super.findBy(dc);
		if(null != list && list.size()>0){
			arrange =  list.get(0);
		}
		return arrange;
	}
	//置为无效-下个月预排班没有使用则可以置为无效
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int updateInvalid(final Long arrangeId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-01");
		final String startMonth = sdf.format(DateUtils.addMonths((new Date()), 1));
		final String endMonth = sdf.format(DateUtils.addMonths((new Date()), 2));
		final String sql = " update tm_arr_schedule_arrange arrange set arrange.valid = 0 " + 
						   " where arrange.arrange_id = :arrangeId and not exists " +
						   " (select id from tt_arr_transfer_class_draft " +
						   " where arrange_id = arrange.arrange_id " +
                           " and day_dt >= to_date(:startMonth,'yyyy-mm-dd') " +
                           " and day_dt < to_date(:endMonth,'yyyy-mm-dd')) ";
		return super.getHibernateTemplate().execute(new HibernateCallback() {
		public Object doInHibernate(Session session)
		throws HibernateException, SQLException {
			Query query = session.createSQLQuery(sql);
			query.setLong("arrangeId", arrangeId);
			query.setString("startMonth", startMonth);
			query.setString("endMonth", endMonth);
			return query.executeUpdate();
			}
		});
	}
	/**
	 * 判断是否被有效的班次使用
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String isExistByInfo(final Long infoId) {
		final String sql = " select arrange_id,arrange_no from tm_arr_schedule_arrange " +
				" where arrange_id in (" +
				" select arrange_id from tm_arr_schedule_info_arrange where info_id = :infoId " +
				" ) and valid = 1 ";
		List<Map> list = super.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				query.setLong("infoId", infoId);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		if(null == list || list.isEmpty() || null == list.get(0) || null == list.get(0).get("ARRANGE_ID")){
			return null;
		}
		return (null == list.get(0).get("ARRANGE_NO")?"班次代码为空":list.get(0).get("ARRANGE_NO").toString());
	}
	/**
	 * 判断是否被其他班次使用-不与自己比较
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String isExistByInfoId(final Long infoId,final Long arrangeId) {
		final String sql = " select arrange_id,arrange_no from tm_arr_schedule_arrange " +
				" where arrange_id in (" +
				" select arrange_id from tm_arr_schedule_info_arrange where info_id = :infoId " +
				" ) and arrange_id <> :arrangeId";
		List<Map> list = super.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				query.setLong("infoId", infoId);
				query.setLong("arrangeId", arrangeId);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		if(null == list || list.isEmpty() || null == list.get(0) || null == list.get(0).get("ARRANGE_ID")){
			return null;
		}
		return (null == list.get(0).get("ARRANGE_NO")?"班次代码为空":list.get(0).get("ARRANGE_NO").toString());
	}
}
