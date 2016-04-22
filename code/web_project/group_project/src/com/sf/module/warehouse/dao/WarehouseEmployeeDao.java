package com.sf.module.warehouse.dao;

import static com.sf.module.warehouse.dao.WarehouseEmployeeHql.ADD_EMPLOYEE_DYNAMIC_DEPARTMENT_SQL;
import static com.sf.module.warehouse.dao.WarehouseEmployeeHql.DELETE_EMPLOYEE_DYNAMIC_DEPARTMENT_SQL;
import static com.sf.module.warehouse.dao.WarehouseEmployeeHql.QUERY_EMPLOYEE_WORK_TYPE_BY_POST_TYPE;
import static com.sf.module.warehouse.dao.WarehouseEmployeeHql.QUERY_STAFF_INFORMATION;
import static com.sf.module.warehouse.dao.WarehouseEmployeeHql.UPDATE_EMPLOYEE_SQL;
import static com.sf.module.warehouse.dao.WarehouseEmployeeHql.validDynamicDepartmentCodeSql;
import static com.sf.module.common.domain.Constants.IS_CHECK_OF_NETWORK;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;

import com.sf.module.common.util.QueryHelper;
import com.sf.module.dispatch.domain.SchedulingRepository;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.operation.util.CommonUtil;

public class WarehouseEmployeeDao extends QueryHelper {
	private final static String EMP_CODE = "EMP_CODE";
	private final static String DEPT_ID = "DEPT_ID";
	private final static String WORK_TYPE = "WORK_TYPE";
	private final static String IS_HAVE_COMMISSION = "IS_HAVE_COMMISSION";
	private final static String DYNAMIC_DEPT_CODE = "DYNAMIC_DEPT_CODE";
	private static final String COLUMN_WORKING_STATE = "WORKING_STATE";
	private static final String COLUMN_EMP_DUTY_NAME = "EMP_DUTY_NAME";
	private static final String COLUMN_EMP_NAME = "EMP_NAME";

	@Transactional
	public int queryStaffInformationCount(HashMap<String, String> queryParameter) {
		String sql = WarehouseEmployeeHql.QUERY_STAFF_INFORMATION_COUNT + builderPersonnelQueryConditions(queryParameter);
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		setQueryParameterValue(queryParameter, query);
		List<Map<String, Object>> searchSchedulingCountPage = query.list();
		return ((BigDecimal) searchSchedulingCountPage.get(0).get(SchedulingRepository.COL_TOTAL_SIZE)).intValue();
	}

	private String builderPersonnelQueryConditions(HashMap<String, String> queryParameter) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> key : queryParameter.entrySet()) {
			String parameter = ":" + key.getKey();
			if (existAndNotEmpty(EMP_CODE, key))
				sb.append(" and emp.emp_code = ").append(parameter);

			if (existAndNotEmpty(DEPT_ID, key)){
				if (queryParameter.get(IS_CHECK_OF_NETWORK) != null) {
					sb.append(" and dept.dept_code in ( SELECT dept_code  FROM TM_DEPARTMENT WHERE DELETE_FLG = 0  ").append("  START WITH dept_code = ")
					.append(key.getValue()).append(" CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE)");
				} else {
					sb.append(" and dept.dept_code in ( SELECT dept_code  FROM TM_DEPARTMENT WHERE DELETE_FLG = 0  ").append("  and  dept_code = ")
					.append(key.getValue()).append(")");
				}
			}
			if (existAndNotEmpty(WORK_TYPE, key))
				sb.append(" and emp.work_type = ").append(parameter);

			if (existAndNotEmpty(IS_HAVE_COMMISSION, key))
				sb.append(" and emp.is_have_commission = ").append(parameter);

			if (existAndNotEmpty(COLUMN_EMP_NAME, key))
				sb.append(" and emp.emp_name = ").append(parameter);

			if (existAndNotEmpty(COLUMN_EMP_DUTY_NAME, key))
				sb.append(" and emp.emp_duty_name = ").append(parameter);

			if (existAndNotEmpty(COLUMN_WORKING_STATE, key)) {
				if (key.getValue().equals("1"))
					sb.append(" and (emp.dimission_dt is null or emp.dimission_dt > sysdate) ");

				if (key.getValue().equals("2"))
					sb.append(" and emp.dimission_dt < sysdate ");
			}
		}
		return sb.toString();
	}

	private boolean existAndNotEmpty(String columnName, Map.Entry<String, String> obj) {
		return obj.getKey().equalsIgnoreCase(columnName) && obj.getValue() != null && !obj.getValue().equals("");
	}

	@Transactional
	public List queryStaffInformation(HashMap<String, String> queryParameter) {
		String sql = "select * from (" + QUERY_STAFF_INFORMATION + builderPersonnelQueryConditions(queryParameter) + " ) where rn > :start and rn <= :limit";
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		setQueryParameterValue(queryParameter, query);
		return query.list();
	}

	@Transactional
	public boolean updateWarehouseEmployee(HashMap<String, String> queryCriteria) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(UPDATE_EMPLOYEE_SQL);
		setQueryParameterValue(queryCriteria, query);
		query.executeUpdate();
		query = session.createSQLQuery(DELETE_EMPLOYEE_DYNAMIC_DEPARTMENT_SQL);
		setQueryParameterValue(queryCriteria, query);
		query.executeUpdate();
		for (Map.Entry<String, String> entry : queryCriteria.entrySet()) {
			if (containDynamicDepartment(entry)) {
				query = session.createSQLQuery(ADD_EMPLOYEE_DYNAMIC_DEPARTMENT_SQL);
				query.setParameter(EMP_CODE, queryCriteria.get(EMP_CODE));
				query.setParameter(DEPT_ID, getDepartmentID(entry));
				query.executeUpdate();
			}
		}
		return true;
	}

	private Long getDepartmentID(Map.Entry<String, String> entry) {
		return DepartmentCacheBiz.getDepartmentByCode(entry.getValue().split("/")[0]).getId();
	}

	private boolean containDynamicDepartment(Map.Entry<String, String> entry) {
		return entry.getKey().startsWith(DYNAMIC_DEPT_CODE) && !CommonUtil.isEmpty(entry.getValue());
	}

	@Transactional
	public List queryEmployeeWorkTypeByPostType(String postType) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(QUERY_EMPLOYEE_WORK_TYPE_BY_POST_TYPE);
		query.setParameter(0, postType);
		return query.list();
	}

	@Transactional(readOnly = true)
	public boolean validDynamicDepartmentCode(String dynamicDepartmentCode) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(validDynamicDepartmentCodeSql);
		query.setParameter(0, dynamicDepartmentCode);
		return query.list().size() > 0;
	}
}
