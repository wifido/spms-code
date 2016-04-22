/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-05-30     方芳                        创建
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
import org.hibernate.type.StandardBasicTypes;
import org.quartz.Calendar;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sf.framework.base.IPage;
import com.sf.module.vmsarrange.domain.ScheduleInfo;
import com.sf.module.vmsarrange.util.ArrFileUtil;

/**
 * 班次信息dao实现类
 * 
 * @author 方芳 (350614) 2014-5-30
 */
public class ScheduleInfoDao extends ArrBaseEntityDao<ScheduleInfo> implements
	IScheduleInfoDao {
	//查找重复数据的条数
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Integer listRepeat(final String startTm,final String endTm,final Long startDeptId,
			final Long endDeptId,final Long id) {
		String sqlbak = null;
		//修改时不与自己比较
		if(null != id){
			sqlbak = " select count(1) from tm_arr_schedule_info "
				+ " where start_tm = :startTm and end_tm = :endTm and  start_dept_id = :startDeptId and end_dept_id = :endDeptId "
				+ " and valid=1 " 
				+ " and info_id <> :id ";
		}else{
			sqlbak = " select count(1) from tm_arr_schedule_info "
				+ " where start_tm = :startTm and end_tm = :endTm and  start_dept_id = :startDeptId and end_dept_id = :endDeptId " 
				+ " and valid=1 ";
		}
		final String sql = sqlbak;
		return (Integer) super.getHibernateTemplate()
		.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException {
				org.hibernate.Query query = session.createSQLQuery(sql);
				query.setParameter("startTm", (null == startTm?"":startTm));
				query.setParameter("endTm", (null == endTm?"":endTm));
				query.setParameter("startDeptId", (null == startDeptId?-1:startDeptId));
				query.setParameter("endDeptId", (null == endDeptId?-1:endDeptId));
				if(null != id){
					query.setParameter("id", id);
				}
				Object object = query.uniqueResult();
				if (object != null) {
					return ((BigDecimal)object).intValue();
				}
				return null;
			}
		});
	}
	//查找指定车牌号的所有班次信息
	public List<ScheduleInfo> listByCode(String vehicleCode) {
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduleInfo.class);
		dc.createAlias("vehicle", "v").add(Restrictions.eq("v.vehicleCode", (null == vehicleCode?"":vehicleCode)));
		dc.add(Restrictions.eq("valid", 1));
		dc.addOrder(Order.asc("startTm"));
		return super.findBy(dc);
	}
	//查找分页数据
	public IPage<ScheduleInfo> listPage(Long deptId, Integer dataSource,
			String vehicleCode, Integer valid,Integer isUsed,int pageSize,int pageIndex,Long userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduleInfo.class);
		if(null == deptId){
			deptId = -1L;
		}
		//添加权限控制
		ArrDeptTreeDaoSql.addDeptIdCriteria(dc, "{alias}.DEPT_ID",deptId, userId);
		if(null != dataSource){
			dc.add(Restrictions.eq("dataSource", dataSource));
		}
		if(null != valid){
			dc.add(Restrictions.eq("valid", valid));
		}
		if(!ArrFileUtil.isEmpty(vehicleCode)){
			dc.createAlias("vehicle", "v").add(Restrictions.like("v.vehicleCode", vehicleCode,MatchMode.ANYWHERE));
		}
		if(null != isUsed){
			if(1 == isUsed){
				dc.add(Restrictions.eq("isUsed", 1));
			}else{
				dc.add(Restrictions.in("isUsed", new Integer[]{0,2}));
			}
		}
		//dc.addOrder(Order.asc("vehicle"));
		//dc.addOrder(Order.asc("startTm"));
		dc.addOrder(Order.desc("id"));
		return super.findPageBy(dc, pageSize, pageIndex);
	}
	//批量删除
	public void deleteByIds(List<Long> ids) {
		Map<String, Object> args = new HashMap<String, Object>(1);
		args.put("id", ids);
		super.execute(
				"DELETE FROM com.sf.module.vmsarrange.domain.ScheduleInfo WHERE id IN (:id)",
				args);
	}
	//查找报表数据
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> listReport(Long deptId,Integer dataSource,
			String vehicleCode,Integer valid,Integer isUsed,int startIdx,int endIdx,Long userId) {
		if(null == deptId){
			deptId = -1L;
		}
		String orderSql = " order by info.vehicle_id,info.start_tm,info.info_id ";
		String sqlbak = " select dept.area_code, " +
         				" decode(info.data_source, 1, '手工录入', '路径优化') data_source, " +
         				" info.line_optimize_no, " +
         				" info.model_base, " +
         				" info.start_tm, " +
         				" info.end_tm, " +
         				" sdept.dept_code start_dept_code, " +
         				" sdept.dept_name start_dept_name, " +
         				" edept.dept_code end_dept_code, " +
         				" edept.dept_name end_dept_name, " +
         				" dept.dept_code, " +
         				" dept.dept_name, " +
         				" v.vehicle_code, " +
         				" rank() over("+orderSql+") row_num " +
         				" from tm_arr_schedule_info info " +
         				" left join vm_arr_vehicle v on v.vehicle_id = info.vehicle_id " +
         				" left join tm_department dept on dept.dept_id = info.dept_id " +
         				" left join tm_department sdept on sdept.dept_id = info.start_dept_id " +
         				" left join tm_department edept on edept.dept_id = info.end_dept_id " +
         				" where 1 = 1 ";
		if(userId.compareTo(1L) == 0){
			sqlbak += " and info.dept_id in (select dept_id " +
			"  from tm_department " +
			" where delete_flg = 0 " +
			" start with dept_id = " + deptId +
			" connect by prior dept_code = parent_dept_code) ";
		}else{
			sqlbak += " and info.dept_id in (select a.dept_id " +
			"  from ts_user_dept a, " +
			"       (select t.dept_id " +
			"          from tm_department t " +
			"         where t.delete_flg = 0 " +
			"         start with t.dept_id = " + deptId +
			"        connect by prior t.dept_code = t.parent_dept_code) b " +
			" where a.user_id = " + userId +
			"   and b.dept_id = a.dept_id) ";
		}
		if(null != dataSource){
			sqlbak += " and info.data_source = " + dataSource;
		}
		if(null != valid){
			sqlbak += " and info.valid = " + valid;
		}
		if(!ArrFileUtil.isEmpty(vehicleCode)){
			sqlbak += " and v.vehicle_code like '%" + vehicleCode+"%' ";
		}
		if(null != isUsed){
			if(1 == isUsed){
				sqlbak += " and info.is_used = 1 ";
			}else{
				sqlbak += " and info.is_used in(0,2) ";
			}
		}
		sqlbak += " and rownum <= " + endIdx;
		sqlbak += orderSql;
		sqlbak = " select * from ( " + sqlbak + " ) where row_num >= " + startIdx;
		final String sql = sqlbak;
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
	//查找报表总数
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Long listReportCount(Long deptId,Integer dataSource,
			String vehicleCode,Integer valid,Integer isUsed,Long userId) {
		if(null == deptId){
			deptId = -1L;
		}
		String sqlbak = " select count(info.info_id) " +
         				" from tm_arr_schedule_info info " +
         				" left join vm_arr_vehicle v on v.vehicle_id = info.vehicle_id " +
         				" left join tm_department dept on dept.dept_id = info.dept_id " +
         				" left join tm_department sdept on sdept.dept_id = info.start_dept_id " +
         				" left join tm_department edept on edept.dept_id = info.end_dept_id " +
         				" where 1 = 1 ";
		if(userId.compareTo(1L) == 0){
			sqlbak += " and info.dept_id in (select dept_id " +
			"  from tm_department " +
			" where delete_flg = 0 " +
			" start with dept_id = " + deptId +
			" connect by prior dept_code = parent_dept_code) ";
		}else{
			sqlbak += " and info.dept_id in (select a.dept_id " +
			"  from ts_user_dept a, " +
			"       (select t.dept_id " +
			"          from tm_department t " +
			"         where t.delete_flg = 0 " +
			"         start with t.dept_id = " + deptId +
			"        connect by prior t.dept_code = t.parent_dept_code) b " +
			" where a.user_id = " + userId +
			"   and b.dept_id = a.dept_id) ";
		}
		if(null != dataSource){
			sqlbak += " and info.data_source = " + dataSource;
		}
		if(null != valid){
			sqlbak += " and info.valid = " + valid;
		}
		if(!ArrFileUtil.isEmpty(vehicleCode)){
			sqlbak += " and v.vehicle_code like '%" + vehicleCode+"%' ";
		}
		if(null != isUsed){
			if(1 == isUsed){
				sqlbak += " and info.is_used = 1 ";
			}else{
				sqlbak += " and info.is_used in(0,2) ";
			}
		}
		final String sql = sqlbak;
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
	//为配班加载班次信息
	public IPage<ScheduleInfo> listPageForArrange(Long deptId,Long recordId,String startTm, int pageSize,
			int pageIndex, Long userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduleInfo.class);
		//添加权限控制
		ArrDeptTreeDaoSql.addDeptIdCriteria(dc, "{alias}.DEPT_ID",deptId, userId);
		//只加载有效的记录
		dc.add(Restrictions.eq("valid", 1));
		if(null != recordId){
			//修改时加载自己已经选过的记录
			dc.add(Restrictions.or(Restrictions.in("isUsed", new Integer[]{0,2}), 
					Restrictions.sqlRestriction("exists (select id from tm_arr_schedule_info_arrange " +
							" where arrange_id = ? and info_id = {alias}.info_id)", recordId, StandardBasicTypes.LONG)));
		}else{
			//新增时加载未被使用记录
			dc.add(Restrictions.in("isUsed", new Integer[]{0,2}));
		}
		if(!ArrFileUtil.isEmpty(startTm)){
			dc.add(Restrictions.like("startTm", startTm, MatchMode.START));
		}
		dc.addOrder(Order.asc("startTm"));
		dc.addOrder(Order.desc("id"));
		return super.findPageBy(dc, pageSize, pageIndex);
	}
	//查找班次明细信息
	public ScheduleInfo listInfo(String startTm, String endTm,
			String startDeptCode, String endDeptCode, String vehicleCode,Long userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduleInfo.class);
		//添加权限控制
		ArrDeptTreeDaoSql.addDeptIdCriteria(dc, "{alias}.DEPT_ID",1L, userId);
		dc.add(Restrictions.eq("startTm", startTm));
		dc.add(Restrictions.eq("endTm", endTm));
		dc.createAlias("vehicle", "v").add(Restrictions.eq("v.vehicleCode", vehicleCode));
		dc.createAlias("startDept", "sd").add(Restrictions.eq("sd.deptCode", startDeptCode));
		dc.createAlias("endDept", "ed").add(Restrictions.eq("ed.deptCode", endDeptCode));
		List<ScheduleInfo> l = super.findBy(dc);
		if(null == l || l.isEmpty()){
			return null;
		}
		return l.get(0);
	}
	//查找班次明细信息-通过部门id
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> listInfoById(String startTm, String endTm,
			Long startDeptId, Long endDeptId, String vehicleCode,Long userId) {
		String sqlbak = "select info_id,valid,is_used from tm_arr_schedule_info info " +
				" inner join vm_arr_vehicle v on v.vehicle_id = info.vehicle_id " +
				" where 1=1 ";
		sqlbak += " and info.start_tm = '"+startTm+"' ";
		sqlbak += " and info.end_tm = '"+endTm+"' ";
		sqlbak += " and v.vehicle_code = '"+vehicleCode+"' ";
		sqlbak += " and info.start_dept_id = '"+startDeptId+"' ";
		sqlbak += " and info.end_dept_id = '"+endDeptId+"' ";
		//添加权限控制
		if(userId.compareTo(1L) == 0){
			sqlbak += " and info.dept_id in (select dept_id " +
			"  from tm_department " +
			" where delete_flg = 0 " +
			" start with dept_id = 1 " +
			" connect by prior dept_code = parent_dept_code) ";
		}else{
			sqlbak += " and info.dept_id in (select a.dept_id " +
			"  from ts_user_dept a, " +
			"       (select t.dept_id " +
			"          from tm_department t " +
			"         where t.delete_flg = 0 " +
			"         start with t.dept_id = 1 " +
			"        connect by prior t.dept_code = t.parent_dept_code) b " +
			" where a.user_id = " + userId +
			"   and b.dept_id = a.dept_id) ";
		}
		final String sql = sqlbak;
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
	//查找手工录入线路
	public ScheduleInfo listInfoByStartEnd(String startTm, String endTm,
			Long startDeptId, Long endDeptId,Long userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduleInfo.class);
		//添加权限控制
		ArrDeptTreeDaoSql.addDeptIdCriteria(dc, "{alias}.DEPT_ID",1L, userId);
		dc.add(Restrictions.eq("startTm", startTm));
		dc.add(Restrictions.eq("endTm", endTm));
		dc.createAlias("startDept", "sd").add(Restrictions.eq("sd.id", startDeptId));
		dc.createAlias("endDept", "ed").add(Restrictions.eq("ed.id", endDeptId));
		List<ScheduleInfo> l = super.findBy(dc);
		if(null == l || l.isEmpty()){
			return null;
		}
		return l.get(0);
	}
	//查找手工录入线路
	public ScheduleInfo listInfoByStartEndDept(String startTm, String endTm,
			Long startDeptId, Long endDeptId,Long deptId,Long userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduleInfo.class);
		//添加权限控制
		ArrDeptTreeDaoSql.addDeptIdCriteria(dc, "{alias}.DEPT_ID",deptId, userId);
		dc.add(Restrictions.eq("startTm", startTm));
		dc.add(Restrictions.eq("endTm", endTm));
		dc.createAlias("startDept", "sd").add(Restrictions.eq("sd.id", startDeptId));
		dc.createAlias("endDept", "ed").add(Restrictions.eq("ed.id", endDeptId));
		List<ScheduleInfo> l = super.findBy(dc);
		if(null == l || l.isEmpty()){
			return null;
		}
		return l.get(0);
	}
}
