package com.sf.module.driver.dao;

import static com.google.common.collect.Maps.newHashMap;
import static com.sf.module.common.domain.Constants.KEY_AREA_CODE;
import static com.sf.module.common.domain.Constants.KEY_AREA_NAME;
import static com.sf.module.common.domain.Constants.KEY_CAPITAL_YEAR_MONTH;
import static com.sf.module.common.domain.Constants.KEY_DEPARTMENT_CODE;
import static com.sf.module.common.domain.Constants.KEY_DEPT_CODE;
import static com.sf.module.common.domain.Constants.KEY_DEPT_NAME;
import static com.sf.module.common.domain.Constants.KEY_DRIVER_IDENTIFY;
import static com.sf.module.common.domain.Constants.KEY_DRIVE_DAY;
import static com.sf.module.common.domain.Constants.KEY_EMP_NAME;
import static com.sf.module.common.domain.Constants.KEY_END_TIME;
import static com.sf.module.common.domain.Constants.KEY_START_TIME;
import static com.sf.module.common.domain.Constants.KEY_WARNNING_DAYS;
import static com.sf.module.common.domain.Constants.LIMIT;
import static com.sf.module.common.domain.Constants.START;
import static com.sf.module.common.domain.Constants.TOTALSIZE;
import static com.sf.module.driver.biz.DriverWarningBiz.getContinuousWarningSecondMonth;
import static org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.sf.module.common.dao.ScheduleBaseDao;
import com.sf.module.driver.domain.DriverWarningModel;
import com.sf.module.driver.domain.WarningModel;
import com.sf.module.driver.domain.WarningModel.MultipleWarning;

@Repository
public class DriverWarningDao extends ScheduleBaseDao<DriverWarningModel> {

    @SuppressWarnings("unchecked")
    
    // 查询单月预警数据
	@Transactional
	public List<WarningModel> querySingleWarning(String departmentCode,
			String startTime, String endTime, int start, int limit) {
    	// 获取查询条件结果集
		Map<String, String> parametersMap = buildQueryCondition(departmentCode,
				startTime, endTime, start, limit);

		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session
				.createSQLQuery(
						appendPagingCondition(DriverWarningRepository.SQL_QUERY_SINGLE_WARNING))
				.setResultTransformer(ALIAS_TO_ENTITY_MAP);

		// 设置查询参数
		setQueryParamete(parametersMap, query);

		return convertWarningModel((List<Map<String, Object>>) query.list());
	}
    
    // 获取导出单月预警数据
    @SuppressWarnings("unchecked")
	@Transactional
	public List<WarningModel> exportSingleWarning(String departmentCode,
			String startTime, String endTime) {
    	// 获取查询条件结果集
		Map<String, String> parametersMap = buildQueryCondition(departmentCode,
				startTime, endTime, 0, 0);

		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session.createSQLQuery(
				DriverWarningRepository.SQL_QUERY_SINGLE_WARNING)
				.setResultTransformer(ALIAS_TO_ENTITY_MAP);

		// 设置查询参数
		setQueryParamete(parametersMap, query);

		// 将查询结果集转换成单月预警对象集合
		return convertWarningModel((List<Map<String, Object>>) query.list());
	}

    // 设置查询条件
	private void setQueryParamete(Map<String, String> parametersMap, Query query) {
		for (String param : query.getNamedParameters()) {
			query.setParameter(param, parametersMap.get(param));
		}
	}
    
	// 构建查询条件
	private Map<String, String> buildQueryCondition(String departmentCode,
			String startTime, String endTime, int start, int limit) {
		Map<String, String> parametersMap = newHashMap();

		parametersMap.put(KEY_DEPARTMENT_CODE, departmentCode);
		parametersMap.put(KEY_START_TIME, startTime);
		parametersMap.put(KEY_END_TIME, endTime);
		parametersMap.put(START, numberConvertToString(start));
		parametersMap.put(LIMIT, numberConvertToString(limit + start));

		return parametersMap;
	}
    
	// 拼接分页查询
    private String appendPagingCondition(String sql) {
    	return sql + " and rn > :start and rn <= :limit";
    }
    
    // 数字转换成字符串
    private String numberConvertToString(int number) {
    	return number + "";
    }
    
    // 将查询出的Map数据转换成预警对象
    private List<WarningModel> convertWarningModel(List<Map<String, Object>> maps) {
    	List<WarningModel> warningModels = new ArrayList<WarningModel>();
    	
    	// 构建单条预警数据、添加到集合中
    	for (Map<String, Object> map : maps) {
    		warningModels.add(buildWarningModel(map, warningModels.size() + 1));
    	}
    	
    	return warningModels;
    }
    
    // 构建预警对象
	private WarningModel buildWarningModel(Map<String, Object> map, int order) {
		return new WarningModel(getValueByMapKey(map, KEY_DRIVER_IDENTIFY),
				getValueByMapKey(map, KEY_EMP_NAME),
				getValueByMapKey(map, KEY_DEPT_CODE),
				getValueByMapKey(map, KEY_DEPT_NAME),
				getValueByMapKey(map, KEY_AREA_CODE),
				getValueByMapKey(map, KEY_AREA_NAME),
				String.valueOf((BigDecimal) map.get(KEY_WARNNING_DAYS)),
				getValueByMapKey(map, KEY_DRIVE_DAY),
				order);
	}
	
	// 通过键获取值
	private String getValueByMapKey(Map<String, Object> map, String key) {
		return (String) map.get(key);
	}
    
	// 将查询出的Map数据转换成季度预警数据集
    private List<MultipleWarning> convertMultipleWarning(List<Map<String, Object>> maps, String startTime, String endTime) {
    	List<MultipleWarning> results = new ArrayList<MultipleWarning>();
    	
    	// 构建季度预警月份
    	HashMap<String, Integer> months = buildMultipleWarningMonths(startTime, endTime);
    	
    	// 临时存贮预警数据
    	HashMap<String, MultipleWarning> warnings = new HashMap<String, MultipleWarning>();
    	
    	// 构建季度预警集合
    	for (Map<String, Object> map : maps) {
    		// 预警天数
			String warningDay = String.valueOf((BigDecimal) map.get(KEY_WARNNING_DAYS));
			
			// 获取预警的月份
			Integer monthCount = months.get(getValueByMapKey(map, KEY_CAPITAL_YEAR_MONTH));
    		
			// 员工、网点 作为KEY
    		String key = getValueByMapKey(map, KEY_DRIVER_IDENTIFY) + getValueByMapKey(map, KEY_DEPT_CODE);
    		
    		// 获取季度预警对象、等于空去创建
			MultipleWarning warning = warnings.get(key);
			if (warning == null) {
				warning = new MultipleWarning(
						getValueByMapKey(map, KEY_DRIVER_IDENTIFY), 
						getValueByMapKey(map, KEY_EMP_NAME), 
						getValueByMapKey(map, KEY_DEPT_CODE),
						getValueByMapKey(map, KEY_DEPT_NAME), 
						getValueByMapKey(map, KEY_AREA_CODE), 
						getValueByMapKey(map, KEY_AREA_NAME));

				warnings.put(key, warning);
			}

			// 设置预警的月份 数量
			if (monthCount == 1) {
				warning.setFirstMaxContinuousWorkingDay(Integer
						.parseInt(warningDay));
			} else if (monthCount == 2) {
				warning.setSecondMaxContinuousWorkingDay(Integer
						.parseInt(warningDay));
			} else if (monthCount == 3) {
				warning.setThirdMaxContinuousWorkingDay(Integer
						.parseInt(warningDay));
			}
    	}
    	
    	// 设置连续两月预警、三月预警 的值
    	for (Entry<String, MultipleWarning> multipleWarning : warnings.entrySet()) {
    		MultipleWarning warning = multipleWarning.getValue();
    		// 设置序列号、连续两月预警、三月预警 的值
    		warning.setSerial(results.size() + 1);
    		warning.setTwoMonthContinuous();
    		warning.setThreeMonthContinuous();
    		
    		results.add(warning);
    	}
    	
    	return results;
    }

    // 构建季度预警月份
	private HashMap<String, Integer> buildMultipleWarningMonths(String startTime,
			String endTime) {
		HashMap<String, Integer> months = new HashMap<String, Integer>();
		// 设置预警第一个月、二月、三月
    	months.put(startTime.substring(0, 6), 1);
    	months.put(getContinuousWarningSecondMonth(startTime), 2);
    	months.put(endTime.substring(0, 6), 3);
		return months;
	}
    
	// 查询单月预警总总数据量
    @Transactional
	public int countSingleWarning(String departmentCode, String startTime,
			String endTime) {
		Map<String, String> parametersMap = newHashMap();
		parametersMap.put(KEY_DEPARTMENT_CODE, departmentCode);
		parametersMap.put(KEY_START_TIME, startTime);
		parametersMap.put(KEY_END_TIME, endTime);
    	
    	Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session.createSQLQuery(DriverWarningRepository.SQL_QUERY_SINGLE_WARNING_TOTAL)
				.setResultTransformer(ALIAS_TO_ENTITY_MAP);
		
		setQueryParamete(parametersMap, query);
		@SuppressWarnings("unchecked")
		HashMap<String, Object> map = (HashMap<String, Object>) query.list().get(0);
		return  Integer.parseInt(String.valueOf((BigDecimal)map.get("TOTALSIZE")));
	}

    // 查询季度预警数据
    @SuppressWarnings("unchecked")
	@Transactional
    public List<MultipleWarning> queryMultipleWarning(String departmentCode, String startTime, String endTime, int start, int limit) {
		// 获取查询条件结果集
		Map<String, String> parametersMap = buildQueryCondition(departmentCode,
				startTime, endTime, start, limit);
    			
    	Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session.createSQLQuery(appendPagingCondition(DriverWarningRepository.SQL_QUERY_MULTIPLE_WARNING))
				.setResultTransformer(ALIAS_TO_ENTITY_MAP);
		
		// 设置查询条件
		setQueryParamete(parametersMap, query);

    	return convertMultipleWarning(query.list(), startTime, endTime);
    }
    
    // 获取季度预警导出数据
    @SuppressWarnings("unchecked")
	@Transactional
    public List<MultipleWarning> getExportMultipleWarning(String departmentCode, String startTime, String endTime) {
		// 获取查询条件结果集
		Map<String, String> parametersMap = buildQueryCondition(departmentCode,
				startTime, endTime, 0, 0);
		
    	Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session.createSQLQuery(DriverWarningRepository.SQL_QUERY_MULTIPLE_WARNING)
				.setResultTransformer(ALIAS_TO_ENTITY_MAP);
		
		// 设置查询条件
		setQueryParamete(parametersMap, query);

    	return convertMultipleWarning(query.list(), startTime, endTime);
    }
    
    // 查询季度预警总数据量
    @SuppressWarnings("unchecked")
	@Transactional
    public int queryCountMultipleWarning(String departmentCode, String startTime, String endTime) {
     // 获取查询条件结果集
     		Map<String, String> parametersMap = buildQueryCondition(departmentCode,
     				startTime, endTime, 0, 0);
		
    	Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session.createSQLQuery(DriverWarningRepository.SQL_QUERY_COUNT_MULTIPLE_WARNING)
				.setResultTransformer(ALIAS_TO_ENTITY_MAP);

		// 设置查询条件
		setQueryParamete(parametersMap, query);

    	return ((BigDecimal)((HashMap<String, Object>)query.list().get(0)).get(TOTALSIZE)).intValue();
    }
    
    private static class DriverWarningRepository {
		private final static String SQL_QUERY_SINGLE_WARNING_TOTAL = "select count(1) totalSize"
				+ " from TT_DRIVER_WARNNING_INFO drive"
				+ " where drive.dept_code in"
				+ " (SELECT DEPT_CODE FROM Tm_Department B WHERE DELETE_FLG = 0 START WITH DEPT_CODE = :departmentCode"
				+ " CONNECT BY PRIOR B.DEPT_CODE = B.PARENT_DEPT_CODE)"
				+ " and drive.drive_day >= to_date(:startTime, 'yyyymmdd')"
				+ " and drive.drive_day <= to_date(:endTime, 'yyyymmdd')"
				+ " order by drive.driver_identify, drive.drive_day asc";
		
		private final static String SQL_QUERY_SINGLE_WARNING = "select t.area_code,t.area_name,t.dept_code,t.dept_name,t.driver_identify,t.emp_name,t.drive_day,t.warnning_days"
				+ " from (select t.*, rownum rn"
				+ " from (select drive.area_code,area.dept_name area_name,drive.dept_code,dept.dept_name dept_name,drive.driver_identify,drive.emp_name"
				+ " ,to_char(drive.drive_day, 'yyyymmdd') drive_day,drive.warnning_days"
				+ " from TT_DRIVER_WARNNING_INFO drive,Tm_Department dept,Tm_Department area"
				+ " where drive.dept_code = dept.dept_code"
				+ " and drive.area_code = area.dept_code"
				+ " and drive.dept_code in"
				+ " (SELECT DEPT_CODE FROM Tm_Department B WHERE DELETE_FLG = 0 START WITH DEPT_CODE = :departmentCode"
				+ " CONNECT BY PRIOR B.DEPT_CODE = B.PARENT_DEPT_CODE)"
				+ " and drive.drive_day >= to_date(:startTime, 'yyyymmdd')"
				+ " and drive.drive_day <= to_date(:endTime, 'yyyymmdd')"
				+ " order by drive.driver_identify, drive.drive_day asc) t) t"
				+ " where 1 = 1";
		
		private final static String SQL_QUERY_MULTIPLE_WARNING = "select t.driver_identify,"
				+ "        t.emp_name,"
				+ "        t.dept_code,"
				+ "        dept.dept_name    dept_name,"
				+ "        t.area_code,"
				+ "        area.dept_name area_name,"
				+ "        year_month,"
				+ "        t.warnning_days"
				+ "   from (select t.*, rownum rn"
				+ "           from (select drive.driver_identify, drive.emp_name,to_char(drive.drive_day,'yyyymm') year_month, drive.dept_code, drive.area_code,max(drive.warnning_days) warnning_days"
				+ "                   from TT_DRIVER_WARNNING_INFO drive"
				+ "                  where drive.dept_code in"
				+ "                        (SELECT DEPT_CODE"
				+ "                           FROM Tm_Department B"
				+ "                          WHERE DELETE_FLG = 0"
				+ "                          START WITH DEPT_CODE = :departmentCode"
				+ "                         CONNECT BY PRIOR B.DEPT_CODE = B.PARENT_DEPT_CODE)"
				+ "							and drive.drive_day >= to_date(:startTime, 'yyyymmdd')"
				+ "							and drive.drive_day <= to_date(:endTime, 'yyyymmdd')"
				+ "                  group by drive.driver_identify,drive.dept_code,drive.area_code,drive.emp_name, to_char(drive.drive_day,'yyyymm')"
				+ "                  order by drive.driver_identify, drive.dept_code) t) t,"
				+ "        Tm_Department dept,"
				+ "        Tm_Department area"
				+ "  where t.dept_code = dept.dept_code"
				+ "    and t.area_code = area.dept_code";
		
		
		private final static String SQL_QUERY_COUNT_MULTIPLE_WARNING = " select count(1) totalSize from (select count(1)"
				+ "           from TT_DRIVER_WARNNING_INFO drive"
				+ "          where drive.dept_code in"
				+ "                (SELECT DEPT_CODE"
				+ "                   FROM Tm_Department B"
				+ "                  WHERE DELETE_FLG = 0"
				+ "                  START WITH DEPT_CODE = :departmentCode"
				+ "                 CONNECT BY PRIOR B.DEPT_CODE = B.PARENT_DEPT_CODE)"
				+ "            and drive.drive_day >= to_date(:startTime, 'yyyymmdd')"
				+ "            and drive.drive_day <= to_date(:endTime, 'yyyymmdd')"
				+ "          group by drive.driver_identify,drive.dept_code,drive.area_code,drive.emp_name,to_char(drive.drive_day, 'yyyymm')"
				+ "          order by drive.driver_identify, drive.dept_code)";
    }
}
