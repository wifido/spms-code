/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-19     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.vmsarrange.domain.PreScheduleDraft;
import com.sf.module.vmsarrange.util.ArrFileUtil;

/**
 *
 * PreScheduleDraftDao处理类
 *
 */
public class PreScheduleDraftDao extends ArrBaseEntityDao<PreScheduleDraft> implements
		IPreScheduleDraftDao {

	/**
	 * 根据驾驶员ID获取预排班草稿表中的已经安排排班的驾驶员
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Integer findDriverById(String yearMonth,Long id) {
		StringBuilder sqlBuffer = new StringBuilder("SELECT COUNT(*) FROM TT_ARR_SCHEDULE_DRAFT SD WHERE 1=1 ");
		//年月
		if( StringUtils.isNotEmpty(yearMonth)){
			sqlBuffer.append(" AND SD.YEAR_MONTH='"+yearMonth+"' ");
		}
		//班次类型
		if(id !=0 ){
			sqlBuffer.append(" AND SD.DRIVER_ID="+id);
		}
		final String sql = sqlBuffer.toString();
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
	//查询分页数据
	@SuppressWarnings("rawtypes")
	public IPage findByPage(String deptCode, String empCode, Integer classType,
			String yearMonth, int pageSize, int pageIndex, Long userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(PreScheduleDraft.class);
		//添加权限控制
		ArrDeptTreeDaoSql.addDeptIdCriteriaByDeptCode(dc, "{alias}.DEPT_ID",deptCode, userId);
		DetachedCriteria rdc = dc.createCriteria("driver");
		if(!ArrFileUtil.isEmpty(empCode)){
			rdc.add(Restrictions.like("empCode", empCode,MatchMode.ANYWHERE));
		}
		//班次类型
		if(null != classType){
			dc.add(Restrictions.eq("classType", classType));
		}
		//年月
		if(!ArrFileUtil.isEmpty(yearMonth)){
			dc.add(Restrictions.eq("yearMonth", yearMonth));
		}
		dc.addOrder(Order.desc("id"));
		return super.findPageBy(dc, pageSize, pageIndex);
	}
	/**
	 * 获取报表数据--预排班
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> listPreReport(String deptCode, String empCode,
			String yearMonth, Long userId,Integer classType) {
		String sqlBuffer = " select d.area_code, " +
						   " d.dept_code, " +
						   " dr.driver_name, " +
						   " dr.emp_code, " +
						   " decode(ar.class_type, 1, '正常', 2, '顶班', '机动') class_type, " +
						   " ar.one, " +
						   " ar.two, " +
						   " ar.three, " +
						   " ar.four, " +
						   " ar.five, " +
						   " ar.six, " +
						   " ar.seven, " +
						   " ar.eight, " +
						   " ar.nine, " +
						   " ar.ten, " +
						   " ar.eleven, " +
						   " ar.twelve, " +
						   " ar.thirteen, " +
						   " ar.fourteen, " +
						   " ar.fifteen, " +
						   " ar.sixteen, " +
						   " ar.seventeen, " +
						   " ar.eighteen, " +
						   " ar.nineteen, " +
						   " ar.twenty, " +
						   " ar.twenty_one, " +
						   " ar.twenty_two, " +
						   " ar.twenty_three, " +
						   " ar.twenty_four, " +
						   " ar.twenty_five, " +
						   " ar.twenty_six, " +
						   " ar.twenty_seven, " +
						   " ar.twenty_eight, " +
						   " ar.twenty_nine, " +
						   " ar.thirty, " +
						   " ar.thirty_one, " +
						   " ar.plan_day, " +
						   " ar.attendance_tm, " +
						   " ar.driving_tm " +
						   " from tt_arr_schedule_draft ar " +
						   " inner join vm_arr_driver dr on dr.driver_id = ar.driver_id " +
						   " left join tm_department d on d.dept_id = dr.dept_id " +
						   " where 1 = 1 ";
		//查询有权限的子网点
		if(userId == 1){
			sqlBuffer += " and ar.dept_id in (select dept_id " +
				 " from tm_department " +
				 " where delete_flg = 0 " +
				 " start with dept_code = '" + deptCode + "' " +
				 " connect by prior dept_code = parent_dept_code) ";
		}else{
			sqlBuffer += " and ar.dept_id in (select a.dept_id " +
				 " from ts_user_dept a, " +
				 "      (select t.dept_id " +
				 "          from tm_department t " +
				 "         where t.delete_flg = 0 " +
				 "         start with t.dept_code = '" + deptCode + "' " + 
				 "        connect by prior t.dept_code = t.parent_dept_code) b " +
				 " where a.user_id = " + userId +
				 "   and b.dept_id = a.dept_id) ";
		}
		//月份必填：月份为空时，导出空数据
		if(null == yearMonth){
			yearMonth = "";
		}
		sqlBuffer += " and ar.year_month = '"+yearMonth+"' ";
		//工号
		if(!ArrFileUtil.isEmpty(empCode)){
			sqlBuffer += " and dr.emp_code like '%"+empCode+"%' ";
		}
		//班次类型
		if(null != classType){
			sqlBuffer += " and ar.class_type = "+classType+" ";
		}
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
	/**
	 * 获取报表数据总数--预排班
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Long listPreReportCount(String deptCode, String empCode,
			String yearMonth, Long userId,Integer classType) {
		String sqlBuffer = " select count(ar.schedule_df_id) " +
		   " from tt_arr_schedule_draft ar " +
		   " inner join vm_arr_driver dr on dr.driver_id = ar.driver_id " +
		   " where 1 = 1 ";
		//查询有权限的子网点
		if(userId == 1){
		sqlBuffer += " and ar.dept_id in (select dept_id " +
		" from tm_department " +
		" where delete_flg = 0 " +
		" start with dept_code = '" + deptCode + "' " +
		" connect by prior dept_code = parent_dept_code) ";
		}else{
		sqlBuffer += " and ar.dept_id in (select a.dept_id " +
		" from ts_user_dept a, " +
		"      (select t.dept_id " +
		"          from tm_department t " +
		"         where t.delete_flg = 0 " +
		"         start with t.dept_code = '" + deptCode + "' " + 
		"        connect by prior t.dept_code = t.parent_dept_code) b " +
		" where a.user_id = " + userId +
		"   and b.dept_id = a.dept_id) ";
		}
		//月份必填：月份为空时，导出空数据
		if(null == yearMonth){
			yearMonth = "";
		}
		sqlBuffer += " and ar.year_month = '"+yearMonth+"' ";
		//工号
		if(!ArrFileUtil.isEmpty(empCode)){
			sqlBuffer += " and dr.emp_code like '%"+empCode+"%' ";
		}
		//班次类型
		if(null != classType){
			sqlBuffer += " and ar.class_type = "+classType+" ";
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
	/**
	 * 获取报表数据--实际排班
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> listRealReport(String deptCode, String empCode,
			String yearMonth, Long userId,Integer classType) {
		String sqlBuffer = " select d.area_code, " +
						   " d.dept_code, " +
						   " dr.driver_name, " +
						   " dr.emp_code, " +
						   " decode(ar.class_type, 1, '正常', 2, '顶班', '机动') class_type, " +
						   " ar.one, " +
						   " ar.two, " +
						   " ar.three, " +
						   " ar.four, " +
						   " ar.five, " +
						   " ar.six, " +
						   " ar.seven, " +
						   " ar.eight, " +
						   " ar.nine, " +
						   " ar.ten, " +
						   " ar.eleven, " +
						   " ar.twelve, " +
						   " ar.thirteen, " +
						   " ar.fourteen, " +
						   " ar.fifteen, " +
						   " ar.sixteen, " +
						   " ar.seventeen, " +
						   " ar.eighteen, " +
						   " ar.nineteen, " +
						   " ar.twenty, " +
						   " ar.twenty_one, " +
						   " ar.twenty_two, " +
						   " ar.twenty_three, " +
						   " ar.twenty_four, " +
						   " ar.twenty_five, " +
						   " ar.twenty_six, " +
						   " ar.twenty_seven, " +
						   " ar.twenty_eight, " +
						   " ar.twenty_nine, " +
						   " ar.thirty, " +
						   " ar.thirty_one, " +
						   " ar.attendance_tm, " +
						   " ar.driving_tm, " +
						   " ar.real_day, " +
						   " ar.rate " +
						   " from tt_arr_schedule ar " +
						   " inner join vm_arr_driver dr on dr.driver_id = ar.driver_id " +
						   " left join tm_department d on d.dept_id = dr.dept_id " +
						   " where 1 = 1 ";
		//查询有权限的子网点
		if(userId == 1){
			sqlBuffer += " and ar.dept_id in (select dept_id " +
						" from tm_department " +
						" where delete_flg = 0 " +
						" start with dept_code = '" + deptCode + "' " +
						" connect by prior dept_code = parent_dept_code) ";
		}else{
			sqlBuffer += " and ar.dept_id in (select a.dept_id " +
						" from ts_user_dept a, " +
						"      (select t.dept_id " +
						"          from tm_department t " +
						"         where t.delete_flg = 0 " +
						"         start with t.dept_code = '" + deptCode + "' " + 
						"        connect by prior t.dept_code = t.parent_dept_code) b " +
						" where a.user_id = " + userId +
						"   and b.dept_id = a.dept_id) ";
		}
		//月份必填：月份为空时，导出空数据
		if(null == yearMonth){
			yearMonth = "";
		}
		sqlBuffer += " and ar.year_month = '"+yearMonth+"' ";
		//工号
		if(!ArrFileUtil.isEmpty(empCode)){
			sqlBuffer += " and dr.emp_code like '%"+empCode+"%' ";
		}
		//班次类型
		if(null != classType){
			sqlBuffer += " and ar.class_type = "+classType+" ";
		}
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
	/**
	 * 获取报表数据总数--实际排班
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Long listRealReportCount(String deptCode, String empCode,
			String yearMonth, Long userId,Integer classType) {
		String sqlBuffer = " select count(ar.schedule_id) " +
		   " from tt_arr_schedule ar " +
		   " inner join vm_arr_driver dr on dr.driver_id = ar.driver_id " +
		   " where 1 = 1 ";
		//查询有权限的子网点
		if(userId == 1){
		sqlBuffer += " and ar.dept_id in (select dept_id " +
		" from tm_department " +
		" where delete_flg = 0 " +
		" start with dept_code = '" + deptCode + "' " +
		" connect by prior dept_code = parent_dept_code) ";
		}else{
		sqlBuffer += " and ar.dept_id in (select a.dept_id " +
		" from ts_user_dept a, " +
		"      (select t.dept_id " +
		"          from tm_department t " +
		"         where t.delete_flg = 0 " +
		"         start with t.dept_code = '" + deptCode + "' " + 
		"        connect by prior t.dept_code = t.parent_dept_code) b " +
		" where a.user_id = " + userId +
		"   and b.dept_id = a.dept_id) ";
		}
		//月份必填：月份为空时，导出空数据
		if(null == yearMonth){
			yearMonth = "";
		}
		sqlBuffer += " and ar.year_month = '"+yearMonth+"' ";
		//工号
		if(!ArrFileUtil.isEmpty(empCode)){
			sqlBuffer += " and dr.emp_code like '%"+empCode+"%' ";
		}
		//班次类型
		if(null != classType){
			sqlBuffer += " and ar.class_type = "+classType+" ";
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
	/**
	 * 获取报表数据--配班信息
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> listArrReport(String deptCode, String empCode,
			String yearMonth, Long userId,Integer classType) {
		String conditionSql = " select ar.schedule_df_id " +
		   " from tt_arr_schedule_draft ar " +
		   " inner join vm_arr_driver dr on dr.driver_id = ar.driver_id " +
		   " where 1 = 1 ";
		//查询有权限的子网点
		if(userId == 1){
			conditionSql += " and ar.dept_id in (select dept_id " +
							" from tm_department " +
							" where delete_flg = 0 " +
							" start with dept_code = '" + deptCode + "' " +
							" connect by prior dept_code = parent_dept_code) ";
		}else{
			conditionSql += " and ar.dept_id in (select a.dept_id " +
							" from ts_user_dept a, " +
							"      (select t.dept_id " +
							"          from tm_department t " +
							"         where t.delete_flg = 0 " +
							"         start with t.dept_code = '" + deptCode + "' " + 
							"        connect by prior t.dept_code = t.parent_dept_code) b " +
							" where a.user_id = " + userId +
							"   and b.dept_id = a.dept_id) ";
		}
		//月份必填：月份为空时，导出空数据
		if(null == yearMonth){
			yearMonth = "";
		}
		conditionSql += " and ar.year_month = '"+yearMonth+"' ";
		//工号
		if(!ArrFileUtil.isEmpty(empCode)){
			conditionSql += " and dr.emp_code like '%"+empCode+"%' ";
		}
		//班次类型
		if(null != classType){
			conditionSql += " and ar.class_type = "+classType+" ";
		}
		final String sql = " with t as( " 
							+ conditionSql +
						   " ), r as( " +
						   " select distinct arrange_id " +
						   " from tt_arr_transfer_class_draft c " +
						   " inner join t on t.schedule_df_id = c.schedule_df_id " +
						   " where arrange_id is not null " +
						   " )select " + 
						   " a.arrange_id, " +
						   " a.arrange_no, " + 
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
						   " inner join r on r.arrange_id = a.arrange_id " +
						   " left join tm_arr_schedule_info_arrange ia on ia.arrange_id = a.arrange_id " +
						   " left join tm_arr_schedule_info i on i.info_id = ia.info_id " +
						   " left join tm_department sd on sd.dept_id = i.start_dept_id " +  
						   " left join tm_department ed on ed.dept_id = i.end_dept_id " +  
						   " left join tm_department dd on dd.dept_id = i.dept_id " +  
						   " left join vm_arr_vehicle v on v.vehicle_id = i.vehicle_id " +  
						   " order by a.arrange_id desc";
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
	/**
	 * 获取报表数据总数--配班信息
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Long listArrReportCount(String deptCode, String empCode,
			String yearMonth, Long userId,Integer classType) {
		String conditionSql = " select ar.schedule_df_id " +
		   " from tt_arr_schedule_draft ar " +
		   " inner join vm_arr_driver dr on dr.driver_id = ar.driver_id " +
		   " where 1 = 1 ";
		//查询有权限的子网点
		if(userId == 1){
			conditionSql += " and ar.dept_id in (select dept_id " +
							" from tm_department " +
							" where delete_flg = 0 " +
							" start with dept_code = '" + deptCode + "' " +
							" connect by prior dept_code = parent_dept_code) ";
		}else{
			conditionSql += " and ar.dept_id in (select a.dept_id " +
							" from ts_user_dept a, " +
							"      (select t.dept_id " +
							"          from tm_department t " +
							"         where t.delete_flg = 0 " +
							"         start with t.dept_code = '" + deptCode + "' " + 
							"        connect by prior t.dept_code = t.parent_dept_code) b " +
							" where a.user_id = " + userId +
							"   and b.dept_id = a.dept_id) ";
		}
		//月份必填：月份为空时，导出空数据
		if(null == yearMonth){
			yearMonth = "";
		}
		conditionSql += " and ar.year_month = '"+yearMonth+"' ";
		//工号
		if(!ArrFileUtil.isEmpty(empCode)){
			conditionSql += " and dr.emp_code like '%"+empCode+"%' ";
		}
		//班次类型
		if(null != classType){
			conditionSql += " and ar.class_type = "+classType+" ";
		}
		final String sql = " with t as( " 
							+ conditionSql +
						   " ), r as( " +
						   " select distinct arrange_id " +
						   " from tt_arr_transfer_class_draft c " +
						   " inner join t on t.schedule_df_id = c.schedule_df_id " +
						   " where arrange_id is not null " +
						   " )select count(a.arrange_id) " + 
						   " from tm_arr_schedule_arrange a " +
						   " inner join r on r.arrange_id = a.arrange_id ";
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean isDeptCodeForUser(final String deptCode,final Long userId) {
		final String sql = "SELECT COUNT(UD.DEPT_ID) RESULTNUM  FROM TS_USER_DEPT UD WHERE UD.USER_ID=? " +
		" AND UD.DEPT_ID=(SELECT TD.DEPT_ID FROM TM_DEPARTMENT TD WHERE TD.DEPT_CODE=?)";
		if(1L == userId){
			return true;
		}
		final List params = new ArrayList();
		final StringBuilder sqlBuffer = new StringBuilder(sql);
		//用户ID
		if( userId !=0){
			params.add(userId);
		}
		//年月
		if( StringUtils.isNotEmpty(deptCode.trim())){
			params.add(deptCode.trim());
		}
		List list = this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sqlBuffer.toString());
				for (int i = 0; i < params.size(); i++) {
					Object param = params.get(i);
					if (param instanceof String) {
						query.setString(i, (String) param);
					} else if (param instanceof Long) {
						query.setLong(i, (Long) param);
					} else if (param instanceof Integer) {
						query.setInteger(i, (Integer) param);
					}
				}
				return query.list();
			}
		});
		//如果查询到对应的网点，表示有权限导入该网点的数据
		if(null != list && list.size()>0){
			int num = Integer.valueOf((list.get(0).toString()));
			return num>0 ? true : false;
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean isDriverForDept(final Long driverDeptId, final String deptCode) {
		final String sql = "SELECT COUNT(1) FROM (SELECT D.DEPT_ID FROM TM_DEPARTMENT D WHERE D.DELETE_FLG = 0 " +
				"START WITH D.DEPT_CODE=? CONNECT BY PRIOR D.DEPT_CODE=D.PARENT_DEPT_CODE " +
				" ) AA WHERE AA.DEPT_ID=?";
		final List params = new ArrayList();
		final StringBuilder sqlBuffer = new StringBuilder(sql);
		//年月
		if( StringUtils.isNotEmpty(deptCode.trim())){
			params.add(deptCode.trim());
		}
		//班次类型
		if( driverDeptId !=0){
			params.add(driverDeptId);
		}
		List list = this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sqlBuffer.toString());
				for (int i = 0; i < params.size(); i++) {
					Object param = params.get(i);
					if (param instanceof String) {
						query.setString(i, (String) param);
					} else if (param instanceof Long) {
						query.setLong(i, (Long) param);
					} else if (param instanceof Integer) {
						query.setInteger(i, (Integer) param);
					}
				}
			return query.list();
			}
		});
		//如果查询到对应的网点，表示有权限导入该网点的数据
		if(null != list && list.size()>0){
			int num = Integer.valueOf((list.get(0).toString()));
			return num>0 ? true : false;
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Integer listRepeat(final String empCode, final String yearMonth) {
		final String sql = "SELECT SD.DRIVER_ID FROM TT_ARR_SCHEDULE_DRAFT SD, VM_ARR_DRIVER VD " +
				"WHERE SD.DRIVER_ID=VD.DRIVER_ID ";
		final List params = new ArrayList();
		final StringBuilder sqlBuffer = new StringBuilder(sql);
		//驾驶员
		if( StringUtils.isNotEmpty(empCode.trim())){
			sqlBuffer.append(" AND VD.EMP_CODE=?");
			params.add(empCode.trim());
		}
		//年月
		if( StringUtils.isNotEmpty(yearMonth.trim())){
			sqlBuffer.append(" AND SD.YEAR_MONTH=?");
			params.add(yearMonth.trim());
		}
			List list = this.getHibernateTemplate().execute(new HibernateCallback(){
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					Query query = session.createSQLQuery(sqlBuffer.toString());
					for (int i = 0; i < params.size(); i++) {
						Object param = params.get(i);
						if (param instanceof String) {
							query.setString(i, (String) param);
						} else if (param instanceof Long) {
							query.setLong(i, (Long) param);
						} else if (param instanceof Integer) {
							query.setInteger(i, (Integer) param);
						}
					}
				return query.list();
				}
			});
			return list.size();
	}
	//根据月份加载数据
	public List<PreScheduleDraft> listAllByMonth(String yearMonth) {
		DetachedCriteria dc = DetachedCriteria.forClass(PreScheduleDraft.class);
		//年月
		dc.add(Restrictions.eq("yearMonth", yearMonth));
		return super.findBy(dc);
	}
	/**
	 * 判断班次是否被下一个预排班使用
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String isExistByArrange(final Long arrangeId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-01");
		final String startMonth = sdf.format(DateUtils.addMonths((new Date()), 1));
		final String endMonth = sdf.format(DateUtils.addMonths((new Date()), 2));
		final String sql = " select max(driver_id) driver_id,to_char(wm_concat(emp_code)) emp_code " +
						   " from tm_vms_driver where driver_id in( " +
						   " select driver_id from tt_arr_schedule_draft where schedule_df_id in( " +
						   " select distinct schedule_df_id from tt_arr_transfer_class_draft " + 
						   " where arrange_id = :arrangeId and day_dt >= to_date(:startMonth,'yyyy-mm-dd') " +
						   " and day_dt < to_date(:endMonth,'yyyy-mm-dd') " +
						   " ) and rownum < 11 ) ";
		List<java.util.Map> list = (List<java.util.Map>) super.getHibernateTemplate()
			.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
				throws HibernateException, SQLException {
					org.hibernate.Query query = session.createSQLQuery(sql);
					query.setLong("arrangeId", arrangeId);
					query.setString("startMonth", startMonth);
					query.setString("endMonth", endMonth);
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					return query.list();
			}
		});
		if(null == list || list.isEmpty() || null == list.get(0) || null == list.get(0).get("DRIVER_ID")){
			return null;
		}
		return (list.get(0).get("EMP_CODE")==null?"(驾驶员工号为空)":list.get(0).get("EMP_CODE").toString());
	}
	//根据驾驶员获取记录
	public PreScheduleDraft listByDriver(String yearMonth,Long driverId) {
		DetachedCriteria dc = DetachedCriteria.forClass(PreScheduleDraft.class);
		dc.add(Restrictions.eq("yearMonth", yearMonth));
		dc.createAlias("driver", "d").add(Restrictions.eq("d.id", driverId));
		List<PreScheduleDraft> list = super.findBy(dc);
		if(null == list || list.isEmpty()){
			return null;
		}
		return list.get(0);
	}
}
