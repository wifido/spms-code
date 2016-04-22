package com.sf.module.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;

import com.sf.framework.server.base.dao.BaseDao;

public class ErrorSchedulingDataDao extends BaseDao{
	
	@Transactional
	public int queryErrorSchedulingCount(HashMap<String, String> queryParameter) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(getQueryCountSql(queryParameter));
		setQueryParameterValue(queryParameter, query);
		return Integer.parseInt(query.list().get(0).toString());
	}
	
	@Transactional
	public List queryErrorSchedulingList(HashMap<String, String> queryParameter) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session
		        .createSQLQuery(getQueryPagingListSql(queryParameter))
		        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		setQueryParameterValue(queryParameter, query);
		return query.list();
	}
	
	@Transactional
	public List queryExportErrorSchedulingList(HashMap<String, String> queryParameter) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session
		        .createSQLQuery(getQueryListSql(queryParameter).toString())
		        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		setQueryParameterValue(queryParameter, query);
		return query.list();
	}
	
	private String getQueryCountSql(HashMap<String, String> queryParameter) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(*)");
		sql.append(" from ti_sap_synchronous_error t, tm_oss_employee e, tm_department d");
		sql.append(" where t.emp_code = e.emp_code");
		sql.append(" and e.dept_id = d.dept_id");
		sql.append(" and t.begin_date between :start_time and :end_time ");
		getParameter(queryParameter, sql);
		return sql.toString();
	}
	
	private String getQueryPagingListSql(HashMap<String, String> queryParameter) {
		StringBuilder sql = getQueryListSql(queryParameter);
		sql.append(" where t.rn <= :limit");
		sql.append(" and t.rn > :start");
		return sql.toString();
	}
	
	private StringBuilder getQueryListSql(HashMap<String, String> queryParameter) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from (select t.*, rownum rn from (select d.dept_code,");
		sql.append(" t.emp_code,");
		sql.append(" e.emp_name,");
		sql.append(" t.begin_date,");
		sql.append(" t.begin_tm,");
		sql.append(" t.end_tm,");
		sql.append(" t.tmr_day_flag,");
		sql.append(" t.off_duty_flag,");
		sql.append(" t.error_info,");
		sql.append(" to_char(t.lastupdate, 'yyyy-mm-dd HH24:mi:ss') lastupdate");
		sql.append(" from ti_sap_synchronous_error t, tm_oss_employee e, tm_department d");
		sql.append(" where t.emp_code = e.emp_code");
		sql.append(" and e.dept_id = d.dept_id");
		sql.append(" and t.begin_date between :start_time and :end_time");
		getParameter(queryParameter, sql);
		sql.append(" order by t.begin_date desc ");
		sql.append(" ) t ) t");
		return sql;
	}
	
	private void getParameter(HashMap<String, String> queryParameter, StringBuilder sql) {
		for (Map.Entry<String, String> entry : queryParameter.entrySet()) {
			String hqlFiled = ":" + entry.getKey();
			if (exist("emp_code", entry)) {
				sql.append("   and e.emp_code = ").append(hqlFiled);
			}
			if (exist("emp_name", entry)) {
				sql.append("   and e.emp_name = ").append(hqlFiled);
			}
			if (exist("emp_post_type", entry)) {
				sql.append("   and e.emp_post_type = ").append(hqlFiled);
			}
			if (exist("error_time", entry)) {
				sql.append("   and to_char(t.lastupdate,'YYYY-MM-DD') = ").append(hqlFiled);
			}
			if (exist("dept_code", entry)) {
				sql.append(" and d.dept_code in ( ");
				sql.append(" SELECT dept_code");
				sql.append(" FROM TM_DEPARTMENT");
				sql.append(" WHERE DELETE_FLG = 0");
				sql.append(" START WITH dept_code = ").append(hqlFiled);
				sql.append(" CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE )");
			}
			if (exist("error_info", entry)) {
				sql.append(" and t.error_info like ").append(hqlFiled);
			}
		}
	}
	
	private void setQueryParameterValue(HashMap<String, String> queryParameter, Query query) {
		String[] parameters = query.getNamedParameters();
		for (String param : parameters) {
			query.setParameter(param, queryParameter.get(param));
		}
	}
	
	private boolean exist(String paramCode, Map.Entry<String, String> entry) {
		return paramCode.equals(entry.getKey()) && entry.getValue() != null
				&& !"".equals(entry.getValue());
	}
}
