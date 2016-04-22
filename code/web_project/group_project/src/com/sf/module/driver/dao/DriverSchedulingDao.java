package com.sf.module.driver.dao;

import static com.sf.module.common.domain.Constants.KEY_DEPARTMENT_CODE;
import static com.sf.module.common.domain.Constants.KEY_EMPLOYEE_CODE;
import static com.sf.module.common.domain.Constants.KEY_YEAR_MONTH;
import static com.sf.module.common.domain.Constants.KEY_YEAR_WEEK;
import static com.sf.module.common.domain.Constants.PAGING_QUERY_STATE;
import static com.sf.module.common.domain.Constants.SCHEDULING_TYPE;
import static com.sf.module.driver.dao.DriverSchedulingRepository.DELETE_SWITCHING_DEPARTMENT_SCHEDULING;
import static com.sf.module.driver.dao.DriverSchedulingRepository.QUERY_DRIVER_SCHEDULED_BY_WEEK;
import static com.sf.module.driver.dao.DriverSchedulingRepository.QUERY_DRIVER_SCHEDULED_BY_WEEK_SIZE;
import static com.sf.module.driver.dao.DriverSchedulingRepository.QUERY_EMPLOYEE_CONVERT_DATE;
import static com.sf.module.driver.dao.DriverSchedulingRepository.QUERY_SWITCHING_TIME;
import static com.sf.module.driver.dao.DriverSchedulingRepository.QUERY_WEEKLY_EXPORT_SQL;
import static com.sf.module.driver.dao.DriverSchedulingRepository.QUERY_WHETHER_CAN_BE_MODIFIED;
import static com.sf.module.driver.dao.DriverSchedulingRepository.SQL_QUERY_DRIVER_SCHEDULING;
import static com.sf.module.driver.dao.DriverSchedulingRepository.SQL_QUERY_DRIVER_SCHEDULING_COUNT;
import static com.sf.module.driver.dao.DriverSchedulingRepository.SQL_QUERY_NO_SCHEDULING_EMPLOYEE;
import static com.sf.module.driver.dao.DriverSchedulingRepository.SQL_QUERY_NO_SCHEDULING_EMPLOYEE_COUNT;
import static com.sf.module.driver.dao.DriverSchedulingRepository.buildDynamicParamForQueryNoSchedulingEmployee;
import static org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sf.module.common.dao.ScheduleBaseDao;
import com.sf.module.common.domain.Constants;
import com.sf.module.common.util.StringUtil;
import com.sf.module.driver.domain.DriverScheduling;

@Repository
public class DriverSchedulingDao extends ScheduleBaseDao<DriverScheduling> {
	public static final String DYNAMIC_PARAMETERS = "${dynamicParameters}";
	private static final String CONFIRM_STATUS_PARAMETER = "${dynamicConfirmStatusCondition}";

	@Transactional(readOnly = true)
	public int queryDriverSchedulingCount(HashMap<String, String> queryParameter) {
		String sql = SQL_QUERY_DRIVER_SCHEDULING_COUNT.replace(DYNAMIC_PARAMETERS, buildQueryCondition(queryParameter));

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql);

		String[] namedParameters = query.getNamedParameters();
		for (String namedParameter : namedParameters) {
			query.setParameter(namedParameter, queryParameter.get(namedParameter));
		}
		int totalSize = Integer.parseInt(query.list().get(0).toString());
		return totalSize;
	}

	private String buildQueryCondition(HashMap<String, String> queryParameter) {
		StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(buildQueryConditionForEmployeeCode(queryParameter.get(KEY_EMPLOYEE_CODE)));

        if (StringUtil.isNotBlank(queryParameter.get(KEY_YEAR_MONTH))) {
            stringBuilder.append(" and t.year_month = '").append(queryParameter.get(KEY_YEAR_MONTH)).append("'");
        }
		return stringBuilder.toString();
	}
	
	private String buildQueryConditionForEmployeeCode(String employeeCode) {
        return StringUtil.isNotBlank(employeeCode) ? " and t.employee_code = '" + employeeCode + "'" : "";
    }

	@Transactional(readOnly = true)
	public List queryDriverScheduling(HashMap<String, String> queryParameter) {
		String sql = buildQuerySql(queryParameter);
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql).setResultTransformer(ALIAS_TO_ENTITY_MAP);

		String[] namedParameters = query.getNamedParameters();
		for (String namedParameter : namedParameters) {
			query.setParameter(namedParameter, queryParameter.get(namedParameter));
		}

		return query.list();
	}

	private String buildQuerySql(HashMap<String, String> queryParameter) {
		String sql = SQL_QUERY_DRIVER_SCHEDULING.replace(DYNAMIC_PARAMETERS, buildQueryCondition(queryParameter));
		if (StringUtil.isBlank(queryParameter.get(PAGING_QUERY_STATE))) {
			return sql;
		}
		return buildPagingQuerySql(sql);
	}

	public String buildPagingQuerySql(String sql) {
		return sql + " and rn > :start and rn <= :limit";
	}

	@Transactional(readOnly = true)
	public List queryNoSchedulingEmployees(Map<String, String> queryParameter) {
		String sql = buildDynamicParamForQueryNoSchedulingEmployee(SQL_QUERY_NO_SCHEDULING_EMPLOYEE, queryParameter);
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql).setResultTransformer(ALIAS_TO_ENTITY_MAP);
		String[] namedParameters = query.getNamedParameters();
		for (String namedParameter : namedParameters) {
			query.setParameter(namedParameter, queryParameter.get(namedParameter));
		}
		query.setFirstResult(Integer.parseInt(queryParameter.get(Constants.START)));
		query.setMaxResults(Integer.parseInt(queryParameter.get(Constants.LIMIT)));
		return query.list();
	}

	@Transactional(readOnly = true)
	public List queryNoSchedulingEmployeesCount(Map<String, String> queryParameter) {
		String sql = buildDynamicParamForQueryNoSchedulingEmployee(SQL_QUERY_NO_SCHEDULING_EMPLOYEE_COUNT, queryParameter);
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql).setResultTransformer(ALIAS_TO_ENTITY_MAP);
		String[] namedParameters = query.getNamedParameters();
		for (String namedParameter : namedParameters) {
			query.setParameter(namedParameter, queryParameter.get(namedParameter));
		}
		return query.list();
	}

    @Transactional(readOnly = true)
    public int queryDriverScheduledByWeekSize(HashMap<String, String> queryParameter) {
    	String sql;
    	sql = QUERY_DRIVER_SCHEDULED_BY_WEEK_SIZE.replace(CONFIRM_STATUS_PARAMETER, bulidQueryConditionByConfirmStatus(queryParameter.get("confirmStatus")));
        sql = sql.replace(DYNAMIC_PARAMETERS, buildQueryCondition(queryParameter));

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query query = session.createSQLQuery(sql);
        query.setParameter(0, queryParameter.get(KEY_DEPARTMENT_CODE));
        query.setParameter(1, queryParameter.get(KEY_YEAR_WEEK));

        return Integer.parseInt(query.list().get(0).toString());
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> queryDriverScheduledByWeek(HashMap<String, String> queryParameter) {
    	String sql;
    	sql = QUERY_DRIVER_SCHEDULED_BY_WEEK.replace(CONFIRM_STATUS_PARAMETER, bulidQueryConditionByConfirmStatus(queryParameter.get("confirmStatus")));
        sql = sql.replace(DYNAMIC_PARAMETERS, buildQueryCondition(queryParameter));

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query query = session.createSQLQuery(sql).setResultTransformer(ALIAS_TO_ENTITY_MAP);
        query.setParameter(0, queryParameter.get(KEY_DEPARTMENT_CODE));
        query.setParameter(1, queryParameter.get(KEY_YEAR_WEEK));
        query.setParameter(2, queryParameter.get(Constants.START));
        query.setParameter(3, queryParameter.get(Constants.LIMIT));

        return query.list();
    }
    
    private String bulidQueryConditionByConfirmStatus(String confirmStatus) {
    	if (StringUtil.isBlank(confirmStatus)) {
    		return "";
    	}
    		
    	if (confirmStatus.equals("0")) {
    		 return " and g.confirm_status = 0";
    	}
    	
    	return " and g.confirm_status = 1";
    }
    
    // 司机排班按周导出符合条件的数据
    @Transactional
    public List<Map<String, Object>> queryWeeklyExportDataCount(String departmentCode, String weekOfYear,
			String confirmStatus, String employeeCode) {
    	Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
    	
		BasicQueryProcessUtil basicQueryProcessUtil = BasicQueryProcessUtil.buildInstance()
				.addNecessaryCondition(DriverSchedulingRepository.COL_DEPARTMENT_CODE, departmentCode)
				.addNecessaryCondition(DriverSchedulingRepository.COL_YEARWEEK, weekOfYear)
				.addOptionalCondition(DriverSchedulingRepository.COL_CONFIRM_STATUS, confirmStatus)
				.addOptionalCondition(DriverSchedulingRepository.COL_EMP_CODE, employeeCode);

		return basicQueryProcessUtil.query(session, QUERY_WEEKLY_EXPORT_SQL);
    }
    
    //分页导出司机排班按数据
    @Transactional
	public List<Map<String, Object>> queryWeeklyExportData(String departmentCode, String weekOfYear, String confirmStatus, String employeeCode,
			int start, int limit) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

		BasicQueryProcessUtil basicQueryProcessUtil = new BasicQueryProcessUtil()
				.addNecessaryCondition(DriverSchedulingRepository.COL_DEPARTMENT_CODE, departmentCode)
				.addNecessaryCondition(DriverSchedulingRepository.COL_YEARWEEK, weekOfYear)
				.addNecessaryCondition(Constants.START, Integer.toString(start)).addNecessaryCondition(Constants.LIMIT, Integer.toString(limit))
				.addOptionalCondition(DriverSchedulingRepository.COL_CONFIRM_STATUS, confirmStatus)
				.addOptionalCondition(DriverSchedulingRepository.COL_EMP_CODE, employeeCode);

		String sql = "select * from (select t.*, rownum rn from (" + QUERY_WEEKLY_EXPORT_SQL + ") t) t where rn > :start and rn <= :limit";
		return basicQueryProcessUtil.query(session, sql);
	}
    
    @Transactional(readOnly = true)
    public Map<String, Object> queryDepartmentCodeByEmployeeCode(String employeeCode) {
    	String sql;
		sql = "   select d.dept_code from tm_oss_employee e, tm_department d                 "
				+ "   where e.dept_id = d.dept_id                "
				+ "   and e.emp_code = ?                ";

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query query = session.createSQLQuery(sql).setResultTransformer(ALIAS_TO_ENTITY_MAP);

        query.setParameter(0, employeeCode);
       
        return (Map<String, Object>)query.list().get(0);
    }
    
    // 查询排班数据 通过网点代码、排班年月、实际排班
	@Transactional
	public List<DriverScheduling> querySchedulingByQueryParameter(
			HashMap<String, String> queryParameter) {
		// 获取Session
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();

		Query query = session.createSQLQuery(
				DriverSchedulingRepository.QUERY_SCHEDULING_PRACTICAL)
				.setResultTransformer(ALIAS_TO_ENTITY_MAP);

		// 设置查询条件的值
		for (String condition : query.getNamedParameters()) {
			query.setParameter(condition, queryParameter.get(condition));
		}

		return convertDriverSchedulingByListMap(query.list());
	}
	
	private List<DriverScheduling> convertDriverSchedulingByListMap(List<Map<String, Object>> list) {
		List<DriverScheduling> driverSchedulings = new ArrayList<DriverScheduling>();
		
		for (Map<String, Object> map : list) {
			String employeeCode = isNull(map.get("EMPLOYEE_CODE"));
			String employeeName = isNull(map.get("EMPLOYEE_NAME"));
			String departmentCode =isNull(map.get("DEPARTMENT_CODE"));
			String departmentName =isNull(map.get("DEPARTMENT_NAME"));
			String areaCode =isNull(map.get("AREA_CODE"));
			String areaName =isNull(map.get("AREA_NAME"));
			String day1 =isNull(map.get("DAY1"));
			String day2 =isNull(map.get("DAY2"));
			String day3 =isNull(map.get("DAY3"));
			String day4 =isNull(map.get("DAY4"));
			String day5=isNull(map.get("DAY5"));
			String day6 =isNull(map.get("DAY6"));
			String day7 =isNull(map.get("DAY7"));
			String day8 =isNull(map.get("DAY8"));
			String day9 =isNull(map.get("DAY9"));
			String day10 =isNull(map.get("DAY10"));
			String day11 =isNull(map.get("DAY11"));
			String day12=isNull(map.get("DAY12"));
			String day13 =isNull(map.get("DAY13"));
			String day14 =isNull(map.get("DAY14"));
			String day15 =isNull(map.get("DAY15"));
			String day16 =isNull(map.get("DAY16"));
			String day17 =isNull(map.get("DAY17"));
			String day18 =isNull(map.get("DAY18"));
			String day19 =isNull(map.get("DAY19"));
			String day20=isNull(map.get("DAY20"));
			String day21 =isNull(map.get("DAY21"));
			String day22 =isNull(map.get("DAY22"));
			String day23 =isNull(map.get("DAY23"));
			String day24 =isNull(map.get("DAY24"));
			String day25 =isNull(map.get("DAY25"));
			String day26=isNull(map.get("DAY26"));
			String day27 =isNull(map.get("DAY27"));
			String day28 =isNull(map.get("DAY28"));
			String day29 =isNull(map.get("DAY29"));
			String day30=isNull(map.get("DAY30"));
			String day31=isNull(map.get("DAY31"));
			int totalRestDays = ((BigDecimal)map.get("TOTAL_REST_COUNT")).intValue();
			int continuousAttendanceDays = ((BigDecimal)map.get("CONTINUOUS_ATTENDANCE_DAYS")).intValue();
			double monthlyAverageDrivingTime =((BigDecimal)map.get("DRIVE_TIME_MONTH_T")).doubleValue();
			double monthlyAverageDailyTime =((BigDecimal)map.get("ATTENDANCE_DURATION")).doubleValue();
			double averageAttendanceTime =((BigDecimal)map.get("AVERAGE_ATTENDANCE_TIME")).doubleValue();
			
			DriverScheduling scheduling = new DriverScheduling();
			scheduling.setSequence(driverSchedulings.size() + 1);
			scheduling.setEmployeeCode(employeeCode);
			scheduling.setEmployeeName(employeeName);
			scheduling.setDepartmentAreaDesc(joinSlash(areaCode, areaName));
			scheduling.setDepartmentCode(joinSlash(departmentCode, departmentName));
			scheduling.setWorkType("正常");
			scheduling.setFirstDay(day1);
			scheduling.setSecondDay(day2);
			scheduling.setThirdDay(day3);
			scheduling.setFourthDay(day4);
			scheduling.setFifthDay(day5);
			scheduling.setSixthDay(day6);
			scheduling.setSeventhDay(day7);
			scheduling.setEighthDay(day8);
			scheduling.setNinthDay(day9);
			scheduling.setTenthDay(day10);
			scheduling.setEleventhDay(day11);
			scheduling.setTwelfthDay(day12);
			scheduling.setThirteenthDay(day13);
			scheduling.setFourteenthDay(day14);
			scheduling.setFifteenthDay(day15);
			scheduling.setSixteenthDay(day16);
			scheduling.setSeventeenthDay(day17);
			scheduling.setEighteenthDay(day18);
			scheduling.setNineteenthDay(day19);
			scheduling.setTwentiethDay(day20);
			scheduling.setTwentyFirstDay(day21);
			scheduling.setTwentySecondDay(day22);
			scheduling.setTwentyThirdDay(day23);
			scheduling.setTwentyFourthDay(day24);
			scheduling.setTwentyFifthDay(day25);
			scheduling.setTwentySixthDay(day26);
			scheduling.setTwentySeventhDay(day27);
			scheduling.setTwentyEighthDay(day28);
			scheduling.setTwentyNinthDay(day29);
			scheduling.setThirtiethDay(day30);
			scheduling.setThirtyFirstDay(day31);
			scheduling.setMonthlyAverageDailyTime(monthlyAverageDailyTime);
			scheduling.setMonthlyAverageDrivingTime(monthlyAverageDrivingTime);
			scheduling.setContinuousAttendanceDays(continuousAttendanceDays);
			scheduling.setTotalRestDays(totalRestDays);
			scheduling.setAverageAttendanceTime(averageAttendanceTime);
			
			driverSchedulings.add(scheduling);
		}
		
		return driverSchedulings;
	}
	
	
	public void insertSchedulingConfirmRecord(String employeeCode, String yearWeek) {
		// 获取Session
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();

		Query query = session.createSQLQuery(
				DriverSchedulingRepository.INSERT_SCHEDULING_CONFIRM)
				.setResultTransformer(ALIAS_TO_ENTITY_MAP);

		// 设置查询条件的值
		query.setParameter(0, employeeCode);
		query.setParameter(1, yearWeek);

		query.executeUpdate();
	}

	private String isNull(Object obj) {
		return (String) (obj == null ? "" : obj);
	}
	
	private String joinSlash(String value1, String value2) {
		return value1 + "/" + value2;
	}

	// 查询转网转岗最大时间
	@Transactional
	public Map<String, Object> queryEmployeeInformation(String employeeCode, String departmentCode) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(QUERY_SWITCHING_TIME).setResultTransformer(
				ALIAS_TO_ENTITY_MAP);

		query.setParameter(0, employeeCode);
		query.setParameter(1, departmentCode);
		
		if (query.list().size() > 0)
			return (Map<String, Object>) query.list().get(0);
		return null;
	}
	
	@Transactional
	public Map<String, Object> queryEmployeeConvertDate(String employeeCode) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(QUERY_EMPLOYEE_CONVERT_DATE).setResultTransformer(
				ALIAS_TO_ENTITY_MAP);
		query.setParameter(0, employeeCode);
		if (query.list().size() > 0)
			return (Map<String, Object>) query.list().get(0);
		return null;
	}
	
	@Transactional
	public Map<String, Object> queryWhetherCanBeModified(String dayOfMonth, String employeeCode, String departmentCode) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(QUERY_WHETHER_CAN_BE_MODIFIED).setResultTransformer(
				ALIAS_TO_ENTITY_MAP);
		query.setParameter(0, dayOfMonth);
		query.setParameter(1, employeeCode);
		query.setParameter(2, departmentCode);
		if (query.list().size() > 0)
			return (Map<String, Object>) query.list().get(0);
		return null;
	}
	
	// 删除转网后的数据
	@Transactional
	public void deleteAfterSwitchingDepartmentScheduling(String employeeCode,
			String departmentCode, List<String> schedulingDays) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(DELETE_SWITCHING_DEPARTMENT_SCHEDULING);

		query.setParameter("employeeCode", employeeCode);
		query.setParameter("departmentCode", departmentCode);
		query.setParameterList("dayOfMonths", schedulingDays);
		
		query.executeUpdate();
	}
}
