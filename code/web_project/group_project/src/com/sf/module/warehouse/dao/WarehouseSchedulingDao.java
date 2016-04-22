package com.sf.module.warehouse.dao;

import static com.sf.module.common.domain.Constants.*;
import static com.sf.module.warehouse.dao.WarehouseSchedulingHql.*;
import java.math.BigDecimal;
import java.util.*;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;
import com.sf.framework.core.exception.BizException;
import com.sf.module.common.util.QueryHelper;
import com.sf.module.common.util.StringUtil;
import com.sf.module.dispatch.domain.SchedulingForDispatch;
import com.sf.module.warehouse.domain.WarehouseSchedulingModifyLog;
import com.sf.module.warehouse.domain.WarehouseSchedulingVO;

public class WarehouseSchedulingDao extends QueryHelper {
	private static final String MODIFIED_TIME = "MODIFIED_TIME";
	private static final String MODIFIED_EMPLOYEE_CODE = "MODIFIED_EMPLOYEE_CODE";
	private static final String CONDITION_EMP_NAME = "EMP_NAME = $$";
	private static final String CONDITION_EMP_DUTY_NAME = "EMP_DUTY_NAME = $$";
	private static final String CONDITION_WORK_TYPE = "WORK_TYPE = $$";
	private HashMap<String, String> queryCondition;
	private final static String REPLACE_DYNAMIC_DEPARTMENT_ID_FIELD = "${deptId}";
	private final static String REPLACE_DYNAMIC_PARAMETER = "${dynamicParameters}";
	private final static String REPLACE_DYNAMIC_PARAMETER_DEPT_CODE = "${deptCode}";
	private final static String DEPARTMENT_TYPE = "DEPARTMENT_TYPE";
	private final static String KEY_DEPARTMENT_CODE = "in_departmentCode";

	public WarehouseSchedulingDao() {
		initQueryCondition();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Map<String, Object> getEmployeeByEmpCode(String empCode) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		try {
			Query query = session.createSQLQuery(GET_EMPLOYEE_SQL).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			query.setParameter(0, empCode);
			List result = query.list();
			if (!result.isEmpty()) {
				return (Map<String, Object>) result.get(0);
			}
			return null;
		} catch (HibernateException e) {
			throw new BizException(e.getMessage());
		}
	}

	@Transactional
	public List queryEmployeeDynamicDept(String empCode) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		try {
			Query query = session.createSQLQuery(QUERY_EMPLOYEE_DYNAMIC_DEPT_SQL);
			query.setParameter(0, empCode);
			return query.list();
		} catch (HibernateException e) {
			throw new BizException(e.getMessage());
		}
	}

	@Transactional
	public Map<String, Object> checkIsCheduling(String empCode, String dayOfMonth, String deptCode) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		try {
			Query query = session.createSQLQuery(CHECK_IS_SCHEDULING_SQL).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			query.setParameter(0, empCode);
			query.setParameter(1, deptCode);
			query.setParameter(2, dayOfMonth);
			List result = query.list();
			if (!result.isEmpty()) {
				return (Map<String, Object>) result.get(0);
			}
			return null;
		} catch (HibernateException e) {
			throw new BizException(e.getMessage());
		}
	}

	@Transactional
	public int getAcountOfScheduling(String empCode, String... dayOfMonth) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(SQL_FOR_COUNT_NUMBER_OF_SCHEDULING);
		query.setParameter(0, empCode);
		query.setParameterList("dayOfMonth", dayOfMonth);
		return ((BigDecimal) query.list().get(0)).intValue();
	}

	@Transactional
	public boolean updateWarehouseScheduling(List<SchedulingForDispatch> schedulingList) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(DELETE_EMPLOYEE_SCHEDULING_SQL);
		for (SchedulingForDispatch scheduling : schedulingList) {
			query.setParameter(0, scheduling.getEmployeeCode());
			query.setParameter("day_of_month", scheduling.getDayOfMonth());
			query.executeUpdate();
			session.save(scheduling);
		}
		return true;
	}

	@Transactional
	public void modifyWarehouseScheduling(List<SchedulingForDispatch> schedulingList, WarehouseSchedulingModifyLog warehouseSchedulingModifyLog) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(DELETE_EMPLOYEE_SCHEDULING_SQL);
		for (SchedulingForDispatch scheduling : schedulingList) {
			query.setParameter(0, scheduling.getEmployeeCode());
			query.setParameter("day_of_month", scheduling.getDayOfMonth());
			query.executeUpdate();
			session.save(scheduling);
		}
		if (warehouseSchedulingModifyLog.getModifiedCount() > 0) {
			session.save(warehouseSchedulingModifyLog);
		}
	}

	public int querySchedulingInformationCount(HashMap<String, String> queryParameter) {
		String hql = QUERY_SCHEDULING_INFORMATION_COUNT.replace(
		        REPLACE_DYNAMIC_PARAMETER_DEPT_CODE,
		        StringUtil.isNotBlank(queryParameter.get(IS_CHECK_OF_NETWORK)) ? CONDITION_DEPARTMENT_CODE : CONDITION_THIS_DEPARTMENT_CODE).replace(
		        REPLACE_DYNAMIC_PARAMETER,
		        getQueryConditions(queryParameter, queryCondition));
		List<HashMap<String, Object>> list = getQuery(queryParameter, hql);
		return Integer.valueOf(list.get(0).get(TOTALSIZE).toString());
	}

	public List querySchedulingInformation(HashMap<String, String> queryParameter) {
		String hql = QUERY_SCHEDULING_INFORMATION_SQL.replace(
		        REPLACE_DYNAMIC_PARAMETER_DEPT_CODE,
		        StringUtil.isNotBlank(queryParameter.get(IS_CHECK_OF_NETWORK)) ? CONDITION_DEPARTMENT_CODE : CONDITION_THIS_DEPARTMENT_CODE).replace(
		        REPLACE_DYNAMIC_PARAMETER,
		        getQueryConditions(queryParameter, queryCondition));
		return getQueryResult(getQuery(queryParameter, hql), queryParameter.get(KEY_DEPARTMENT_CODE));
	}

	public List getQueryResult(List<Map<String, Object>> schedulingList, String departmentCode) {
		Map<String, WarehouseSchedulingVO> schedulingResultMap = new HashMap<String, WarehouseSchedulingVO>();

		for (Map<String, Object> map : schedulingList) {

			String mothId = String.valueOf(map.get(COLUMN_MONTH_ID));
			String employeeCode = String.valueOf(map.get(COLUMN_EMPLOYEE_CODE));
			String dayOfMonth = String.valueOf(map.get(COLUMN_DAY_OF_MONTH));
			String key = mothId + "," + employeeCode;
			String schedulingCode = String.valueOf(map.get(COLUMN_SCHEDULING_CODE));
			String deptCode = String.valueOf(map.get(COLUMN_DEPT_CODE));
			String motorDept = String.valueOf(map.get(COLUMN_MOTOR_DEPT));

			if (schedulingResultMap.get(key) != null) {
				WarehouseSchedulingVO scheduling = schedulingResultMap.get(key);
				scheduling.updateWorkTimeWithExpectDay(dayOfMonth.substring(6), schedulingCode, departmentCode, motorDept);
			} else {
				WarehouseSchedulingVO scheduling = new WarehouseSchedulingVO();
				scheduling.setId(Long.parseLong(String.valueOf(map.get(COLUMN_ID))));
				scheduling.setMonthId(String.valueOf(map.get(COLUMN_MONTH_ID)));
				scheduling.setAreaCode(String.valueOf(map.get(COLUMN_AREA_CODE)));
				scheduling.setDepartmentCode(deptCode);
				scheduling.setEmployeeCode(String.valueOf(map.get(COLUMN_EMPLOYEE_CODE)));
				scheduling.setEmployeeName((String) map.get(COLUMN_EMP_NAME));
				scheduling.setWorkType(map.get(COLUMN_WORK_TYPE_NAME) == null ? "" : map.get(COLUMN_WORK_TYPE_NAME).toString());
				scheduling.setModifiedEmployeeCode((String) map.get(MODIFIED_EMPLOYEE_CODE));
				scheduling.setModifiedTime(map.get(MODIFIED_TIME).toString());
				scheduling.updateWorkTimeWithExpectDay(dayOfMonth.substring(6), schedulingCode, departmentCode, motorDept);
				String dutyName = String.valueOf(map.get(COLUMN_EMP_DUTY_NAME));
				scheduling.setEmpDutyName(dutyName);
				schedulingResultMap.put(key, scheduling);
			}
		}

		List<WarehouseSchedulingVO> resultList = new ArrayList<WarehouseSchedulingVO>();
		for (Map.Entry<String, WarehouseSchedulingVO> temp : schedulingResultMap.entrySet()) {
			resultList.add(temp.getValue());
		}
		return resultList;
	}

	public HashMap<String, String> initQueryCondition() {
		queryCondition = new HashMap<String, String>();
		queryCondition.put(COLUMN_EMP_CODE, getAttributeConditions(ALIASE_TM_OSS_EMPLOYEE, CONDITION_EMP_CODE));
		queryCondition.put(COLUMN_EMP_NAME, getAttributeConditions(ALIASE_TM_OSS_EMPLOYEE, WarehouseSchedulingDao.CONDITION_EMP_NAME));
		queryCondition.put(COLUMN_EMP_DUTY_NAME, getAttributeConditions(ALIASE_TM_OSS_EMPLOYEE, WarehouseSchedulingDao.CONDITION_EMP_DUTY_NAME));
		queryCondition.put(COLUMN_WORK_TYPE, getAttributeConditions(ALIASE_TM_OSS_EMPLOYEE, WarehouseSchedulingDao.CONDITION_WORK_TYPE));
		return queryCondition;
	}

	public String getAttributeConditions(String alias, String queryField) {
		return AND + alias + POINT + queryField;
	}

	public int queryNotSchedulingStaffTotal(HashMap<String, String> queryParameter) {
		String hql = QUERY_NOT_SCHEDULING_EMPLOYEE_TOTAL_SQL + getQueryConditions(queryParameter, getSearchEmployeeCondition());

		List<Map<String, Object>> list = getQuery(queryParameter, hql);
		return Integer.parseInt(list.get(0).get(TOTALSIZE).toString());
	}

	public HashMap<String, String> getSearchEmployeeCondition() {
		HashMap<String, String> queryCondition = new HashMap<String, String>();
		queryCondition.put(COLUMN_EMP_CODE, getAttributeConditions(ALIASE_TM_OSS_EMPLOYEE, CONDITION_EMP_CODE));
		return queryCondition;
	}

	public List queryNotSchedulingStaff(HashMap<String, String> queryParameter) {
		String hql = QUERY_NOT_SCHEDULING_EMPLOYEE_SQL.replace("${parameter1}", getQueryConditions(queryParameter, getSearchEmployeeCondition()));
		return getQuery(queryParameter, hql);
	}

	public boolean existScheduling(HashMap<String, String> queryParameter) {
		String hql = QUERY_EXIST_SCHEDULING_SQL;
		return getQuery(queryParameter, hql).size() > 0;
	}

	@Transactional
	public void deleteScheduling(HashMap<String, String> queryParameter) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(SQL_DELETE_SCHEDULING_BY_MONTH);
		setQueryParameterValue(queryParameter, query);
		query.executeUpdate();
	}

	@Transactional
	public List<Map<String, Object>> querySchedulingBySpecifiedParameters(String empCode, String monthId, String departmentCode) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		try {
			Query query = session.createSQLQuery(SQL_QUERY_SCHEDULING_BY_SPECIFIED_PARAMETERS).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			query.setParameter(0, monthId);
			query.setParameter(1, empCode);
			query.setParameter(2, departmentCode);
			List<Map<String, Object>> result = (List<Map<String, Object>>) query.list();
			return result;
		} catch (HibernateException e) {
			throw new BizException(e.getMessage());
		}
	}

	@Transactional
	public List queryNotSchedulingEmployee(HashMap<String, String> queryParameter) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(SQL_QUERY_NO_SCHEDULING_EMPLOYEE_FOR_EXPORT).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		setQueryParameterValue(queryParameter, query);
		return query.list();
	}

}
