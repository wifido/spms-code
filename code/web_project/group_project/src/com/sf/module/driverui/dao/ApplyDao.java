package com.sf.module.driverui.dao;

import static com.sf.module.common.domain.Constants.KEY_EMPLOYEE_CODE;
import static com.sf.module.common.domain.Constants.LIMIT;
import static com.sf.module.common.domain.Constants.START;
import static com.sf.module.common.domain.Constants.TOTALSIZE;
import static com.sf.module.driver.dao.LineConfigureRepository.SQL_QUERY_APPROVER_COUNT;
import static org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.common.util.StringUtil;
import com.sf.module.driver.dao.LineConfigureRepository;
import com.sf.module.driverui.domain.ApplyRecord;

@Repository
public class ApplyDao extends BaseEntityDao<ApplyRecord> {
	private static final String INIT_CONDITION = "1=1";

	@Transactional
	public List<ApplyRecord> queryApprovalList(HashMap<String, String> params) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session
				.createSQLQuery(
						setSearchString(params,
								LineConfigureRepository.SQL_QUERY_APPROVAL_LIST_BY_EMP_CODE)
								+ LineConfigureRepository.SQL_QUERY_APPROVAL_LIST_BY_EMP_CODE_ORDER_BY)
				.setResultTransformer(ALIAS_TO_ENTITY_MAP);
		String[] namedParameters = query.getNamedParameters();
		for (String namedParameter : namedParameters) {
			query.setParameter(namedParameter, params.get(namedParameter));
		}
		query.setFirstResult(Integer.parseInt(params.get(START)));
		query.setMaxResults(Integer.parseInt(params.get(LIMIT)));
		return query.list();
	}

	@Transactional
	public List queryLeave(String employeeCode, String start, String limit) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

		Query query = session.createSQLQuery(buildPagingQueryBysql(getQueryHql())).setResultTransformer(ALIAS_TO_ENTITY_MAP);
		query.setParameter(KEY_EMPLOYEE_CODE, employeeCode);
		query.setParameter(START, start);
		query.setParameter(LIMIT, limit);
		
		return query.list();
	}
	
	@Transactional
	public int queryLeaveCount(String employeeCode) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

		Query query = session.createSQLQuery(getQueryHql())
				.setResultTransformer(ALIAS_TO_ENTITY_MAP);
		query.setParameter(KEY_EMPLOYEE_CODE, employeeCode);
		
		return query.list().size();
	}
	
	private String getQueryHql() {
		return "select app.apply_id,emp.emp_name, emp.emp_code,app.approver_info,to_char(app.apporve_time,'YYYY-MM-DD HH24:mi:ss') apporve_Time,"
				+ " dept_app.dept_code,app.day_of_month,app.apply_info,app.status,app.approver from"
				+ " tt_driver_apply app,tm_oss_employee emp,tm_department   dept_app"
				+ " where app.apply_employee_code = emp.emp_code and app.department_code = dept_app.dept_code"
				+ " and app.apply_type = 1 and app.apply_employee_code = :employeeCode order by app.apply_id desc";
	}
	
	private String buildPagingQueryBysql(String sql) {
		return "select * from (select t.*, rownum rn from (" + sql +") t) t where rn > :start and rn <= :limit";
	}

	@Transactional
	public int queryApprovalCount(HashMap<String, String> params) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(
				setSearchString(params,
						LineConfigureRepository.SQL_QUERY_APPROVAL_COUNT_BY_EMP_CODE))
				.setResultTransformer(ALIAS_TO_ENTITY_MAP);
		String[] namedParameters = query.getNamedParameters();
		for (String namedParameter : namedParameters) {
			query.setParameter(namedParameter, params.get(namedParameter));
		}
		List<HashMap<String, Object>> list = query.list();
		return Integer.valueOf(list.get(0).get(TOTALSIZE).toString());
	}

	private String setSearchString(HashMap<String, String> params, String sql) {
		if (StringUtil.isNotBlank(params.get("searchString"))) {
			sql = sql.replace(INIT_CONDITION,
					LineConfigureRepository.SQL_QUERY_APPROVAL_LIST_BY_EMP_CODE_REPLACE);
			params.put("searchString", "%" + params.get("searchString") + "%");
		}
		return sql;
	}
	
	@Transactional
	public int queryTheApproverCount(HashMap<String, String> params) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session.createSQLQuery(
				SQL_QUERY_APPROVER_COUNT
						+ isEmployeeFieldNotEmpty(params.get("searchString")))
				.setResultTransformer(ALIAS_TO_ENTITY_MAP);

		setQueryParament(query, params);
		
		return query.list().size();
	}

	@Transactional
	public List queryTheApproverList(HashMap<String, String> params) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session.createSQLQuery(
				buildPagingQueryParameter(params.get("searchString")))
				.setResultTransformer(ALIAS_TO_ENTITY_MAP);

		setQueryParament(query, params);
		
		return query.list();
	}
	
	private void setQueryParament(Query query, HashMap<String, String> params) {
		for (String field : query.getNamedParameters()) {
			if (field.equals("searchString")) {
				query.setParameter(field, "%" +params.get(field) + "%");
				continue;
			}
			query.setParameter(field, params.get(field));
		}
	}
	
	private String isEmployeeFieldNotEmpty(String searchString) {
		if (StringUtil.isNotBlank(searchString)) {
			return " and (emp.emp_name like :searchString or emp.emp_code like :searchString)";
		}
		return "";
	}
	
	private String buildPagingQueryParameter(String searchString) {
		return "select * from (" + SQL_QUERY_APPROVER_COUNT
				+ isEmployeeFieldNotEmpty(searchString) + ") t where t.rn > :start and t.rn <= :limit";
	}
}
