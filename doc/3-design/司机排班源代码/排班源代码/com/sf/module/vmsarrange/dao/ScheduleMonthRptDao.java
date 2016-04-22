/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-25     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.vmsarrange.domain.ScheduleMonthRpt;
import com.sf.module.vmsarrange.util.ArrFileUtil;

/**
 *
 * ScheduleMonthRptDao处理类
 *
 */
public class ScheduleMonthRptDao extends ArrBaseEntityDao<ScheduleMonthRpt> implements
		IScheduleMonthRptDao {

	public IPage findByPage(String deptCode, String empCode, Integer classType,
			String yearMonth, int pageSize, int pageIndex, Long userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduleMonthRpt.class);
		//添加权限控制
		ArrDeptTreeDaoSql.addDeptIdCriteriaByDeptCode(dc, "{alias}.DEPT_ID",deptCode, userId);
		dc.createAlias("driver", "d");
		if(!ArrFileUtil.isEmpty(empCode)){
			dc.add(Restrictions.like("d.empCode", empCode,MatchMode.ANYWHERE));
		}
		//班次类型
		String classTypeText = null;
		if(null != classType){
			switch(classType.intValue()){
			case 1:classTypeText="正常";break;
			case 2:classTypeText="顶班";break;
			case 3:classTypeText="机动";break;
			}
		}
		if(!ArrFileUtil.isEmpty(classTypeText)){
			dc.add(Restrictions.eq("classType", classTypeText));
		}
		//年月
		if(!ArrFileUtil.isEmpty(yearMonth)){
			dc.add(Restrictions.eq("yearMonth", yearMonth));
		}
		dc.addOrder(Order.desc("id"));
		
		
		return super.findPageBy(dc, pageSize, pageIndex);
	}

	/**
	 * 获取报表数据--实际排班
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> listRealMonthReport(String deptCode, String empCode,
			String yearMonth, Long userId,Integer classType) {
		String sqlBuffer = " select d.area_code, " +
						   " d.dept_code, " +
						   " dr.driver_name, " +
						   " dr.emp_code, " +
						   " ar.class_type , " +
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
						   " ar.real_working_tm attendance_tm, " +
						   " ar.avg_driving_tm driving_tm, " +
						   " ar.real_driving_tm real_driving_tm, " +
						   " ar.real_rest_tm real_day, " +
						   " ar.real_rate rate " +
						   " from tr_arr_sch_month_report ar " +
						   " left join vm_arr_driver dr on dr.driver_id = ar.driver_id " +
						   " left join tm_department d on d.dept_id = ar.dept_id " +
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
		sqlBuffer += " and ar.month = '"+yearMonth+"' ";
		if(!ArrFileUtil.isEmpty(empCode)){
			sqlBuffer += " and dr.emp_code like '%"+empCode+"%' ";
		}
		//班次类型
		String classTypeText = null;
		if(null != classType){
			switch(classType.intValue()){
			case 1:classTypeText="正常";break;
			case 2:classTypeText="顶班";break;
			case 3:classTypeText="机动";break;
			}
		}
		if(null != classTypeText){
			sqlBuffer += " and ar.class_type = '"+classTypeText+"' ";
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
	public Long listRealMonthReportCount(String deptCode, String empCode,
			String yearMonth, Long userId,Integer classType) {
		String sqlBuffer = " select count(ar.rpt_id) " +
		   " from tr_arr_sch_month_report ar " +
		   " left join vm_arr_driver dr on dr.driver_id = ar.driver_id " +
		   " left join tm_department d on d.dept_id = ar.dept_id " +
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
		sqlBuffer += " and ar.month = '"+yearMonth+"' ";
		if(!ArrFileUtil.isEmpty(empCode)){
			sqlBuffer += " and dr.emp_code like '%"+empCode+"%' ";
		}
		//班次类型
		String classTypeText = null;
		if(null != classType){
			switch(classType.intValue()){
			case 1:classTypeText="正常";break;
			case 2:classTypeText="顶班";break;
			case 3:classTypeText="机动";break;
			}
		}
		if(null != classTypeText){
			sqlBuffer += " and ar.class_type = '"+classTypeText+"' ";
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
}
