package com.sf.module.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;

import com.sf.framework.server.base.dao.BaseDao;

public class SyncSchedulingDataDao extends BaseDao{
	
	@Transactional
	public int querySyncSchedulingCount(HashMap<String, String> queryParameter) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(getQueryCountSql(queryParameter));
		setQueryParameterValue(queryParameter, query);
		return Integer.parseInt(query.list().get(0).toString());
	}
	
	@Transactional
	public List querySyncSchedulingList(HashMap<String, String> queryParameter) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session
		        .createSQLQuery(getQueryPagingForListSql(queryParameter))
		        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		setQueryParameterValue(queryParameter, query);
		return query.list();
	}
	
	@Transactional
	public List queryExportSyncSchedulingList(HashMap<String, String> queryParameter) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session
		        .createSQLQuery(getQueryListSql(queryParameter).toString())
		        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		setQueryParameterValue(queryParameter, query);
		return query.list();
	}
	
	private String getQueryCountSql(HashMap<String, String> queryParameter) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(*)" );
		sql.append(" from tt_sap_synchronous ts, tm_oss_employee e , TM_DEPARTMENT d" );
		sql.append(" where ts.emp_code = e.emp_code" );
		sql.append("  and e.dept_id = d.dept_id " );
		getParameter(queryParameter, sql);
		return sql.toString();
	}
	
	private String getQueryPagingForListSql(HashMap<String, String> queryParameter){
		StringBuilder sql = getQueryListSql(queryParameter);
		sql.append("  where t.rn > :start   and t.rn <= :limit ");
		return sql.toString();
	}
	
	private StringBuilder getQueryListSql(HashMap<String, String> queryParameter) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from ");
		sql.append(" (select ts.emp_code," );
		sql.append("        ts.begin_date," );
		sql.append("        ts.begin_tm," );
		sql.append("        ts.end_tm," );
		sql.append("        ts.tmr_day_flag," );
		sql.append("        ts.off_duty_flag," );
		sql.append("        ts.state_flg,");
		sql.append("  		to_char(ts.create_tm,'yyyy-mm-dd HH24:mi:ss') create_tm,");
		sql.append("        ts.error_info," );
		sql.append("        to_char(ts.sync_tm,'yyyy-mm-dd HH24:mi:ss') sync_tm,");
		sql.append("        to_char(e.sf_date,'yyyy-mm-dd HH24:mi:ss') sf_date,");
		sql.append("        to_char(e.dimission_dt,'yyyy-mm-dd HH24:mi:ss') dimission_dt,");
		sql.append("        to_char(e.transfer_date,'yyyy-mm-dd HH24:mi:ss') transfer_date,");
		sql.append("        to_char(e.date_from,'yyyy-mm-dd HH24:mi:ss') date_from,");
		sql.append("        ts.emp_post_type," );
		sql.append("        rownum rn" );
		sql.append("   from tt_sap_synchronous ts, tm_oss_employee e, TM_DEPARTMENT d" );
		sql.append("  where ts.emp_code = e.emp_code" );
		sql.append("  and e.dept_id = d.dept_id " );
		getParameter(queryParameter, sql);
		sql.append("  order by ts.emp_code,ts.begin_date desc");
		sql.append(" ) t");
		return sql;
	}
	
	private void getParameter(HashMap<String, String> queryParameter, StringBuilder sql) {
		for (Map.Entry<String, String> entry : queryParameter.entrySet()) {
			String hqlFiled = ":" + entry.getKey();
			if (exist("emp_code", entry)) {
				sql.append("   and ts.emp_code = ").append(hqlFiled);
			}
			if (exist("start_time", entry)) {
				sql.append("   and ts.begin_date >= ").append(hqlFiled);
			}
			if (exist("end_time", entry)) {
				sql.append("   and ts.begin_date <=  ").append(hqlFiled);
			}
			if (exist("emp_post_type", entry)) {
				sql.append("   and ts.emp_post_type =  ").append(hqlFiled);
			}
			if (exist("dept_code", entry)) {
				sql.append(" and d.dept_code in ( ");
				sql.append(" SELECT dept_code");
				sql.append(" FROM TM_DEPARTMENT");
				sql.append(" WHERE DELETE_FLG = 0");
				sql.append(" START WITH dept_code = ").append(hqlFiled);
				sql.append(" CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE )");
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
