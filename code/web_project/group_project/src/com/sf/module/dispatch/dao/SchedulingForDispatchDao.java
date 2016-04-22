package com.sf.module.dispatch.dao;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.sf.module.common.domain.Constants.KEY_EMP_CODES;
import static com.sf.module.dispatch.domain.SchedulingRepository.AREA_CODE;
import static com.sf.module.dispatch.domain.SchedulingRepository.CANCEL_FLAG;
import static com.sf.module.dispatch.domain.SchedulingRepository.COL_BEGIN_TIME;
import static com.sf.module.dispatch.domain.SchedulingRepository.COL_CREATED_EMPLOYEE_CODE;
import static com.sf.module.dispatch.domain.SchedulingRepository.COL_DAY_OF_MONTH;
import static com.sf.module.dispatch.domain.SchedulingRepository.COL_DEPARTMENT_CODE;
import static com.sf.module.dispatch.domain.SchedulingRepository.COL_DEPARTMENT_NAME;
import static com.sf.module.dispatch.domain.SchedulingRepository.COL_EMPLOYEE_CODE;
import static com.sf.module.dispatch.domain.SchedulingRepository.COL_EMP_NAME;
import static com.sf.module.dispatch.domain.SchedulingRepository.COL_END_TIME;
import static com.sf.module.dispatch.domain.SchedulingRepository.COL_ID;
import static com.sf.module.dispatch.domain.SchedulingRepository.COL_MODIFIED_EMPLOYEE_CODE;
import static com.sf.module.dispatch.domain.SchedulingRepository.COL_MONTH_ID;
import static com.sf.module.dispatch.domain.SchedulingRepository.COL_PERSON_TYPE;
import static com.sf.module.dispatch.domain.SchedulingRepository.COL_WORK_TYPE;
import static com.sf.module.dispatch.domain.SchedulingRepository.DATA_SOURCE;
import static com.sf.module.dispatch.domain.SchedulingRepository.DATA_SOURCE_SAP;
import static com.sf.module.dispatch.domain.SchedulingRepository.DATA_SOURCE_SPMS;
import static com.sf.module.dispatch.domain.SchedulingRepository.DIMISSION_DT;
import static com.sf.module.dispatch.domain.SchedulingRepository.DIVISION_CODE;
import static com.sf.module.dispatch.domain.SchedulingRepository.MODIFIED_TIME;
import static org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.transaction.annotation.Transactional;

import com.sf.framework.core.exception.BizException;
import com.sf.module.common.domain.Constants;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.StringUtil;
import com.sf.module.dispatch.domain.SchedulingForDispatch;
import com.sf.module.dispatch.domain.SchedulingRepository;

public class SchedulingForDispatchDao extends AppBaseDao<SchedulingForDispatch> {
	
	private static final String  STRING_DYNAMIC_PARAMETER = "${parameter}";
	
	private static final String SQL_FOR_QUERY_RECURSIVE_DEPARTMENT = "" 
            + "                     (SELECT dept_code \n "
            + "                          FROM TM_DEPARTMENT \n "
            + "                         WHERE DELETE_FLG = 0 \n "
            + "                         START WITH dept_code = :DEPARTMENT_CODE \n "
            + "                        CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE) \n ";
	
	private static String searchResultCount = " select count(*) totalSize\n" 
			+ "   from (select sc.employee_code \n"
			+ "  from tt_schedule_daily sc, tm_oss_employee emp, tm_department dept \n" 
			+ "  where sc.employee_code = emp.emp_code \n"
			+ "  and emp.dept_id = dept.dept_id \n"
			+ "  and sc.department_code = dept.dept_code \n" 
			+ "	 and sc.emp_post_type = emp.emp_post_type \n" 
			+ "  and sc.emp_post_type = '2' \n"
			+ "  and emp.emp_post_type = '2' \n"
			+ "  and sc.month_id = :MONTH_ID  \n" 
            + "	 and dept.dept_code in \n"
            +	 SQL_FOR_QUERY_RECURSIVE_DEPARTMENT
            + 	 STRING_DYNAMIC_PARAMETER
			+ "  \n group by sc.employee_code, sc.month_id) t";

	private static String querySchedulingList = "select sc.id, sc.month_id as month_id,\n" 
			+ "       sc.employee_code, \n"
			+ "       emp.emp_name, emp.dimission_dt, emp.cancel_flag, emp.data_source, \n" 
			+ "       sc.department_code, \n" 
			+ "		  dept.dept_name, \n"
			+ "		  dept.area_code, \n"
			+ "		  dept.division_code, \n"
			+ "		  emp.persg work_type, \n"
			+ "		  emp.persk_txt person_type, \n"
			+ "       sc.created_employee_code, \n"
			+ "       sc.modified_employee_code, \n"
			+ "       substr(sc.day_of_month, 7) day_of_month, \n"
			+ "       substr(sc.begin_time,0,4) begin_time, \n" 
			+ "       substr(sc.end_time,0,4) end_time, \n"
			+ "       to_char(sc.modified_time,'yyyy-mm-dd HH24:mi:ss') modified_time \n"
			+ "  from tt_schedule_daily sc, tm_oss_employee emp, tm_department dept \n" 
			+ " where sc.employee_code = emp.emp_code \n"
			+ "   and emp.dept_id = dept.dept_id \n"
			+ "   and sc.department_code = dept.dept_code \n" 
			+ "	  and sc.emp_post_type = emp.emp_post_type \n" 
			+ "   and emp.emp_post_type = '2' \n"
			+ "   and sc.emp_post_type = '2' \n"
			+ "   and employee_code in \n" 
			+ "       (select employee_code \n" 
			+ "          from (select t.employee_code ,rownum rn \n"
			+ "                  from (select sc.employee_code \n"
			+ "                           from tt_schedule_daily sc , tm_oss_employee emp, tm_department dept \n"
			+ "              where sc.employee_code = emp.emp_code \n" 
			+ "   			and emp.dept_id = dept.dept_id \n"
			+ "             and sc.department_code = dept.dept_code \n"
			+ "	  			and sc.emp_post_type = emp.emp_post_type \n" 
			+ "  			and emp.emp_post_type = '2' \n"
			+ "             and sc.month_id = :MONTH_ID \n"
			+ "	 			and dept.dept_code in \n"
			+	 			SQL_FOR_QUERY_RECURSIVE_DEPARTMENT
	        + 				STRING_DYNAMIC_PARAMETER
			+ "             \n  group by sc.employee_code, sc.month_id) t)\n" 
			+ "         where rn > :start \n" 
			+ "           and rn <= :limit ) \n"
			+ "			and sc.month_id = :MONTH_ID "
			+ "	 		and dept.dept_code in \n"
			+	 		SQL_FOR_QUERY_RECURSIVE_DEPARTMENT
	        + 			STRING_DYNAMIC_PARAMETER
			+ "			order by  sc.modified_time desc";

	private static String queryOneDayScheduling = "select id, \n"
			+ "   department_code,\n" 
			+ "        employee_code,\n"
			+ "        substr(sc.day_of_month, 7) day_of_month,\n" 
			+ "        substr(sc.month_id, 5) month_id,\n"
			+ "        substr(sc.month_id, 0, 4) years,\n" 
			+ " 		created_employee_code,\n" 
			+ "  		create_time,\n"
			+ "        substr(sc.begin_time,0,4) begin_time,\n" 
			+ "        substr(sc.end_time,0,4) end_time,\n" 
			+ "		work_type,\n" 
			+ "        emp_name,\n"
			+ "        dept_name,\n" 
			+ "        0 isEdit, \n" 
			+ "		decode(sc.CROSS_DAY_TYPE,null,'0','1') across_id, \n"
			+ "		decode(sc.CROSS_DAY_TYPE,null,'否','是') across_name,\n"
			+ " 	emp.persg \n" 
			+ "   from tt_schedule_daily sc, tm_oss_employee emp, tm_department dept\n"
			+ "  where sc.employee_code = emp.emp_code\n"
			+ "    and  emp.dept_id = dept.dept_id \n"
			+ "    and sc.department_code = dept.dept_code\n" 
			+ "    and sc.employee_code = ? \n"
			+ "    and sc.month_id = ? \n" 
			+ "    and sc.emp_post_type = '2' \n" 
			+ "    and sc.department_code = ? \n";

	private static String deleteScheduling = "delete from tt_schedule_daily t \n" 
			+ "  where t.employee_code = ? \n" 
			+ "  and t.month_id = ? \n"
			+ "  and t.emp_post_type = '2' "
			+ "  and t.day_of_month = ? ";

	public final static String SQL_UPDATE_SCHEDULING_MODIFY_INFO = " update tt_schedule_daily t set t.modified_time = sysdate ," +
			" t.modified_employee_code = ? " +
			" where t.employee_code= ? " +
			" and t.month_id = ? " +
			" and t.emp_post_type = '2' ";
	
	@Transactional
	public int queryTotalSize(HashMap<String, String> queryCriteria) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		String sql = searchResultCount.replace(STRING_DYNAMIC_PARAMETER, builderQueryCondition(queryCriteria));
		Query queryCount = session.createSQLQuery(sql).setResultTransformer(ALIAS_TO_ENTITY_MAP);
		setQueryParameterValue(queryCriteria, queryCount);
		List<Map<String, Object>> searchSchedulingCountPage = queryCount.list();
		return ((BigDecimal) searchSchedulingCountPage.get(0).get(SchedulingRepository.COL_TOTAL_SIZE)).intValue();
	}

	private String builderQueryCondition(HashMap<String, String> queryCriteria) {
		StringBuilder stringBuilder = new StringBuilder();
		if (StringUtil.isNotBlank(queryCriteria.get("EMPLOYEE_CODE"))) {
			stringBuilder.append(" and emp.emp_code = :EMPLOYEE_CODE ");
		}
		if (StringUtil.isNotBlank(queryCriteria.get("EMP_NAME"))) {
			stringBuilder.append(" and emp.emp_name = :EMP_NAME ");
		}
		if (StringUtil.isNotBlank(queryCriteria.get("DATA_SOURCE"))) {
			stringBuilder.append(" and sc.data_source = :DATA_SOURCE ");
		}
		if (StringUtil.isNotBlank(queryCriteria.get(WORK_TYPE))) {
			// 当等于4时，查询全日制
			if (queryCriteria.get(WORK_TYPE).equals("4")) {
				stringBuilder.append(" and emp.persg = 'A' ");
			} else {
				stringBuilder.append(" and emp.persg = 'C' ");
			}
		}
		return stringBuilder.toString();
	}

	@Transactional
	public List querySchedulingResultList(HashMap<String, String> queryCriteria,String joinString) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		String sql = querySchedulingList.replace(STRING_DYNAMIC_PARAMETER, builderQueryCondition(queryCriteria));
		Query searchList = session.createSQLQuery(sql).setResultTransformer(ALIAS_TO_ENTITY_MAP);
		setQueryParameterValue(queryCriteria, searchList);
		List<Map<String, Object>> schedulingList = searchList.list();
		return  constructSchedulingList(constructScheduling(joinString, schedulingList));
	}

	private List<SchedulingForDispatch> constructSchedulingList(Map<String, SchedulingForDispatch> schedulingResultMap) {
	    List<SchedulingForDispatch> resultList = new ArrayList<SchedulingForDispatch>();
		for (Map.Entry<String, SchedulingForDispatch> temp : schedulingResultMap.entrySet()) {
			resultList.add(temp.getValue());
		}
	    return resultList;
    }

	private Map<String, SchedulingForDispatch> constructScheduling(String joinString, List<Map<String, Object>> schedulingList) {
	    Map<String, SchedulingForDispatch> schedulingResultMap = newLinkedHashMap();
		for (Map<String, Object> map : schedulingList) {
			String mothId = (String) map.get(COL_MONTH_ID);
			String employeeCode = (String) map.get(COL_EMPLOYEE_CODE);
			String dayOfMonth = map.get(COL_DAY_OF_MONTH) + "";
			String key = mothId + "," + employeeCode;
			String workTime = map.get(COL_BEGIN_TIME) + ":" + map.get(COL_END_TIME);
			if (schedulingResultMap.get(key) != null) {
				SchedulingForDispatch scheduling = schedulingResultMap.get(key);
				scheduling.updateWorkTimeWithExpectDay(dayOfMonth, workTime,joinString);
				continue;
			} 
			SchedulingForDispatch scheduling = new SchedulingForDispatch();
			scheduling.setId(Long.parseLong(map.get(COL_ID) + ""));
			scheduling.setMonthId((String) map.get(COL_MONTH_ID));
			scheduling.setEmployeeCode((String) map.get(COL_EMPLOYEE_CODE));
			scheduling.setEmployeeName((String) map.get(COL_EMP_NAME));
			scheduling.setWorkType(map.get(COL_WORK_TYPE) + "");
			scheduling.setDepartmentCode((String) map.get(COL_DEPARTMENT_CODE));
			scheduling.setCreatedEmployeeCode((String) map.get(COL_CREATED_EMPLOYEE_CODE));
			scheduling.setModifiedEmployeeCode((String) map.get(COL_MODIFIED_EMPLOYEE_CODE));
			scheduling.setDeptName((String) map.get(COL_DEPARTMENT_NAME));
			scheduling.setAreaCode((String) map.get(AREA_CODE));
			scheduling.setPersonType((String) map.get(COL_PERSON_TYPE));
			scheduling.setDivisionCode((String) map.get(DIVISION_CODE));
			scheduling.updateWorkTimeWithExpectDay(dayOfMonth, workTime,joinString);
			
			if(DATA_SOURCE_SPMS.equals((String) map.get(DATA_SOURCE)) ){
				scheduling.setDataSource("SPMS");
			}else if(DATA_SOURCE_SAP.equals((String) map.get(DATA_SOURCE))){
				scheduling.setDataSource("SAP");
			}else{
				scheduling.setDataSource("PMP");
			}
			
			if(null == (String) map.get(CANCEL_FLAG)){
				scheduling.setCancelFlag("在职");
			}else{
				scheduling.setCancelFlag("离职");
			}
			
			if(null == (Date) map.get(DIMISSION_DT)){
				scheduling.setDimissionTime(null);
			}else{
				Date getLeaveDate = (Date) map.get(DIMISSION_DT);
				SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
				String leaveDate = sdf.format(getLeaveDate); 
				scheduling.setDimissionTime(leaveDate);
			}
			
			scheduling.setScheduledStatus("是");
			if (null == map.get(MODIFIED_TIME)) {
				scheduling.setModifiedTime(null);
			}else {
				try {
					scheduling.setModifiedTime(DateUtil.parseDate(
							map.get(MODIFIED_TIME).toString(), DateFormatType.FULL_TIME));
				} catch (ParseException e) {
					throw new BizException("修改时间格式错误");
				}
			}
			schedulingResultMap.put(key, scheduling);
		}
	    return schedulingResultMap;
    }
	
	@Transactional
	public void importScheduling(List<SchedulingForDispatch> schedulingForDispatch) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		for (SchedulingForDispatch scheduling : schedulingForDispatch) {
			Query query = session.createSQLQuery(deleteScheduling);
			query.setParameter(0, scheduling.getEmployeeCode());
			query.setParameter(1, scheduling.getMonthId());
			query.setParameter(2, scheduling.getDayOfMonth());
			query.executeUpdate();
			session.save(scheduling);
		}
	}

	@Transactional
	public List queryOneDayScheduling(HashMap<String, String> queryCriteria) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		List<Map<String, Object>> schedulingList;
		String sql = queryOneDayScheduling;
		if (queryCriteria.get(COL_DAY_OF_MONTH) != null) {
			sql += "  and sc.day_of_month = ? ";
		}
		Query query = session.createSQLQuery(sql).setResultTransformer(ALIAS_TO_ENTITY_MAP);
		query.setParameter(0, queryCriteria.get(COL_EMPLOYEE_CODE));
		query.setParameter(1, queryCriteria.get(COL_MONTH_ID));
		query.setParameter(2, queryCriteria.get(COL_DEPARTMENT_CODE));
		if (queryCriteria.get(COL_DAY_OF_MONTH) != null) {
			query.setParameter(3, queryCriteria.get(COL_DAY_OF_MONTH));
		}
		schedulingList = query.list();
		return schedulingList;
	}

	@Transactional
	public void saveOrDeleScheduling(List<SchedulingForDispatch> list, SchedulingForDispatch deleteObj, String modifiedEmployeeCode) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		
//		Query updateQuery = session.createSQLQuery(SQL_UPDATE_SCHEDULING_MODIFY_INFO);
//		updateQuery.setParameter(0, modifiedEmployeeCode);
//		updateQuery.setParameter(1, deleteObj.getEmployeeCode());
//		updateQuery.setParameter(2, deleteObj.getMonthId());
//		updateQuery.executeUpdate();
		
		List<SchedulingForDispatch> daySchedueList = getDayScheduling(deleteObj);
		Query query = session.createSQLQuery(deleteScheduling);
		query.setParameter(0, deleteObj.getEmployeeCode());
		query.setParameter(1, deleteObj.getMonthId());
		query.setParameter(2, deleteObj.getDayOfMonth());
		query.executeUpdate();
		saveBatch(list);
		if (list.isEmpty() && daySchedueList != null) {
			SchedulingForDispatch dispatch = daySchedueList.get(0);
			dispatch.setBeginTime("");
			dispatch.setEndTime("");
			save(dispatch);
		}
	}

	@Transactional
	public List<SchedulingForDispatch> getDayScheduling(SchedulingForDispatch deleteObj) {
		String querySql = "select ID, " + "DEPARTMENT_CODE, " + "BEGIN_TIME, " + "END_TIME, " + "DAY_OF_MONTH, " + "MONTH_ID, " + "EMPLOYEE_CODE, "
				+ "CREATED_EMPLOYEE_CODE, " + "MODIFIED_EMPLOYEE_CODE, " + "CREATE_TIME, " + "MODIFIED_TIME, " + "EMP_POST_TYPE, " + "CROSS_DAY_TYPE, "
				+ "SYNCHRO_STATUS, " + "SCHEDULING_CODE  " + "from " + " tt_schedule_daily sd " + " where sd.employee_code = ?" + " and sd.month_id = ? "
				+ " and sd.day_of_month = ?  and sd.emp_post_type = '2' ";
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(querySql).addEntity(SchedulingForDispatch.class);
		query.setParameter(0, deleteObj.getEmployeeCode());
		query.setParameter(1, deleteObj.getMonthId());
		query.setParameter(2, deleteObj.getDayOfMonth());
		return query.list();
	}

	/**
	 * @param queryCriteria
	 * @return
	 */
	@Transactional
	public int getExportExcelCount(HashMap<String, String> queryCriteria) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		String sql =  " SELECT COUNT(*) TOTALSIZE " 
				+ "   FROM TM_OSS_EMPLOYEE T, TM_DEPARTMENT D " 
				+ "  WHERE T.DEPT_ID = D.DEPT_ID " 
				+ "    AND NOT EXISTS " 
				+ "  (SELECT 1 " 
				+ "           FROM TT_SCHEDULE_DAILY TT " 
				+ "          WHERE TT.MONTH_ID = :MONTH_ID " 
				+ "            AND TT.EMP_POST_TYPE = '2' " 
				+ "            AND T.EMP_CODE = TT.EMPLOYEE_CODE " 
				+ "            AND TT.DEPARTMENT_CODE = D.DEPT_CODE) " 
				+ "    AND (TO_CHAR(T.DIMISSION_DT, 'YYYYMMDD') > :MONTH_ID || '01' OR " 
				+ "        T.DIMISSION_DT IS NULL) " 
				+ "    AND (TO_CHAR(T.SF_DATE, 'YYYYMM') <= :MONTH_ID OR T.SF_DATE IS NULL) " 
				+ "    AND (TO_CHAR(T.TRANSFER_DATE, 'YYYYMM') <= :MONTH_ID OR " 
				+ "        T.TRANSFER_DATE IS NULL) " 
				+ "    AND (TO_CHAR(T.DATE_FROM, 'YYYYMM') <= :MONTH_ID OR T.DATE_FROM IS NULL) " 
				+ "    AND T.DEPT_ID IN " 
				+ "        (SELECT DEPT_ID " 
				+ "           FROM TM_DEPARTMENT " 
				+ "          START WITH DEPT_CODE IN (:DEPARTMENT_CODE) " 
				+ "         CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE) " 
				+ "    AND T.EMP_POST_TYPE = '2' " ;
		Query query = session.createSQLQuery(sql).setResultTransformer(ALIAS_TO_ENTITY_MAP);
		setQueryParameterValue(queryCriteria, query);
		List<Map<String, Object>> searchSchedulingCountPage = query.list();
		return ((BigDecimal) searchSchedulingCountPage.get(0).get(SchedulingRepository.COL_TOTAL_SIZE)).intValue();
	}
	
	@Transactional
	public List<SchedulingForDispatch> getExportExcelList(HashMap<String, String> queryCriteria) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		String sql =" SELECT D.AREA_CODE, " 
				+ "        T.PERSK_TXT PERSON_TYPE, " 
				+ "        D.DEPT_CODE, " 
				+ "        T.EMP_CODE, " 
				+ "        T.EMP_NAME, " 
				+ "        DECODE_EMP_WORKTYPE(T.WORK_TYPE) WORK_TYPE " 
				+ "   FROM TM_OSS_EMPLOYEE T, TM_DEPARTMENT D " 
				+ "  WHERE T.DEPT_ID = D.DEPT_ID " 
				+ "    AND T.DEPT_ID IN " 
				+ "        (SELECT DEPT_ID " 
				+ "           FROM TM_DEPARTMENT " 
				+ "          START WITH DEPT_CODE IN (:DEPARTMENT_CODE) " 
				+ "         CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE) " 
				+ "    AND T.EMP_POST_TYPE = '2' " 
				+ "    AND NOT EXISTS " 
				+ "  (SELECT 1 " 
				+ "           FROM TT_SCHEDULE_DAILY TT " 
				+ "          WHERE TT.MONTH_ID = :MONTH_ID " 
				+ "            AND TT.EMP_POST_TYPE = '2' " 
				+ "            AND T.EMP_CODE = TT.EMPLOYEE_CODE " 
				+ "            AND TT.DEPARTMENT_CODE = D.DEPT_CODE) " 
				+ "    AND (TO_CHAR(T.DIMISSION_DT, 'YYYYMMDD') > :MONTH_ID || '01' OR " 
				+ "        T.DIMISSION_DT IS NULL) " 
				+ "    AND (TO_CHAR(T.SF_DATE, 'YYYYMM') <= :MONTH_ID OR T.SF_DATE IS NULL) " 
				+ "    AND (TO_CHAR(T.TRANSFER_DATE, 'YYYYMM') <= :MONTH_ID OR " 
				+ "        T.TRANSFER_DATE IS NULL) " 
				+ "    AND (TO_CHAR(T.DATE_FROM, 'YYYYMM') <= :MONTH_ID OR T.DATE_FROM IS NULL) " 
				+ "  ORDER BY AREA_CODE, DEPT_CODE " ;

		Query searchList = session.createSQLQuery(sql).setResultTransformer(ALIAS_TO_ENTITY_MAP);
		setQueryParameterValue(queryCriteria, searchList);
		searchList.setFirstResult(Integer.parseInt(queryCriteria.get(Constants.START)));
		searchList.setMaxResults(Integer.parseInt(queryCriteria.get(Constants.LIMIT)));
		return bulidSchedulingList(searchList.list());
	}

	private List<SchedulingForDispatch> bulidSchedulingList(List<Map<String, Object>> schedulingList) {
	    List<SchedulingForDispatch> schedulingResultList = new ArrayList<SchedulingForDispatch>();
		for (Map<String, Object> map : schedulingList) {
			SchedulingForDispatch scheduling = new SchedulingForDispatch();
			scheduling.setWorkType(map.get("WORK_TYPE") == null ? "" : map.get("WORK_TYPE").toString());
			scheduling.setDepartmentCode(map.get("DEPT_CODE").toString());
			scheduling.setAreaCode(map.get("AREA_CODE").toString());
			scheduling.setUsername(map.get("EMP_NAME").toString());
			scheduling.setEmployeeCode(map.get("EMP_CODE").toString());
			scheduling.setEmployeeName(map.get("EMP_NAME").toString());
			scheduling.setPersonType(map.get("PERSON_TYPE") == null ? "" : map.get("PERSON_TYPE").toString());
			scheduling.setScheduledStatus("否");
			schedulingResultList.add(scheduling);
		}
	    return schedulingResultList;
    }

	@Transactional
	public boolean existEmployyeCode(String searchSql) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(searchSql);
		return query.list().size() > 0;
	}

	@Transactional
	public boolean generalValidationMethod(String querySql) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(querySql);
		return query.list().size() > 0;
	}
	
	@Transactional
	public boolean deleteScheulDaily(String[] employeeCodes, int monthId) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(" delete tt_schedule_daily t where t.EMPLOYEE_CODE in (:empCodes) and t.month_id = '" +  monthId +	"' and t.emp_post_type = '2'");
		query.setParameterList(KEY_EMP_CODES, employeeCodes);
		return query.executeUpdate() > 0;
	}
	
	@Transactional
	public boolean queryEmployeeWorkType(String employessCode,String departmentCode) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(" select persg from tm_oss_employee t , " 
	    +		"tm_department d where t.dept_id = d.dept_id  and d.dept_code = '" 
		+ departmentCode  +
	    "' and t.emp_code = '"
		+ employessCode +
	    "' and t.persg = 'A' ");
		return query.list().size() > 0;
	}
	
	@Transactional
	public int queryDeptPermissions(String deptId,String userId) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		String sql = "select count(*) totalSize from TS_USER_DEPT t where t.user_id = '"+userId+"' and t.dept_id = (select t.dept_id from tm_department t where t.dept_code='"+deptId+"')";
		Query queryCount = session.createSQLQuery(sql);
		List<Map<String, Object>> searchSchedulingCountPage = queryCount.list();
		return  ((BigDecimal) searchSchedulingCountPage.get(0)).intValue();
	}
}