package com.sf.module.driver.biz;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.sf.module.common.domain.Constants.DOWNLOAD_PATH;
import static com.sf.module.common.domain.Constants.EXPORT_MAX;
import static com.sf.module.common.domain.Constants.IMPORT_RESULT;
import static com.sf.module.common.domain.Constants.KEY_CONFIGURE_CODE;
import static com.sf.module.common.domain.Constants.KEY_DEPARTMENT_CODE;
import static com.sf.module.common.domain.Constants.KEY_EMPLOYEE_CODE;
import static com.sf.module.common.domain.Constants.KEY_SUCCESS;
import static com.sf.module.common.domain.Constants.KEY_YEAR_MONTH;
import static com.sf.module.common.domain.Constants.KEY_YEAR_WEEK;
import static com.sf.module.common.domain.Constants.LIMIT;
import static com.sf.module.common.domain.Constants.PAGING_QUERY_STATE;
import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.domain.Constants.SCHEDULING_DAYS;
import static com.sf.module.common.domain.Constants.SCHEDULING_TYPE;
import static com.sf.module.common.domain.Constants.START;
import static com.sf.module.common.domain.Constants.TOTAL_SIZE;
import static com.sf.module.common.util.DateFormatType.yyyyMMdd_HHmmss;
import static com.sf.module.operation.util.CommonUtil.getLastDayOfMonth;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.sf.framework.core.domain.IUser;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.domain.Constants;
import com.sf.module.common.util.BeanUtil;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.StringUtil;
import com.sf.module.driver.dao.DriverSchedulingDao;
import com.sf.module.driver.dao.DriverWeeklyExportHandler;
import com.sf.module.driver.dao.LineConfigureDao;
import com.sf.module.driver.domain.DriveLine;
import com.sf.module.driver.domain.DriverScheduling;
import com.sf.module.driver.domain.WeeklyExportModel;
import com.sf.module.driverui.dao.ApplyDao;
import com.sf.module.driverui.domain.ApplyRecord;
import com.sf.module.esbinterface.fileutil.ZipUtil;
import com.sf.module.operation.dao.OutEmployeeDao;
import com.sf.module.operation.util.CommonUtil;
import com.sf.module.pushserver.dao.MessageDao;

@Component
public class DriverSchedulingBiz extends BaseBiz {
	private static Log logger = LogFactory.getLog(DriverSchedulingBiz.class);
	private static final String CONFIRM_DATE = "CONFIRM_DATE";
	private static final String CONFIRM_STATUS = "CONFIRM_STATUS";
	@Resource
	private DriverSchedulingDao driverSchedulingDao;
	@Resource
	private LineConfigureDao lineConfigureDao;
	@Resource
	private OutEmployeeDao outEmployeeDao;
	@Resource
	private ApplyDao applyDao;
	@Autowired
	private MessageDao messageDao;

	private static final String YEAR_MONTH = "YEAR_MONTH";
	private static final String YEAR_WEEK = "YEAR_WEEK";
	private static final String DEPT_CODE = "DEPT_CODE";
	private static final String EMP_CODE = "EMP_CODE";
	private static final String DAY_OF_MONTH = "DAY_OF_MONTH";
	private static final String AREA_CODE = "AREA_CODE";
	private static final String DEPT_DESC = "DEPT_DESC";
	private static final String EMP_NAME = "EMP_NAME";
	private static final String CREATE_TIME = "CREATE_TIME";
	private static final String CREATED_EMPLOYEE_CODE = "CREATED_EMPLOYEE_CODE";
	private static final String MODIFIED_TIME = "MODIFIED_TIME";
	private static final String MODIFIED_EMPLOYEE_CODE = "MODIFIED_EMPLOYEE_CODE";
	private static final String CONFIGURE_CODE = "CONFIGURE_CODE";
	private static final String DEPARTMENT_AREA_DESC = "DEPARTMENT_AREA_DESC";
	private static final String DEPARTMENT_ID = "DEPT_ID";
	private static final String WORK_TYPE = "WORK_TYPE";
	private static final String WORK_TYPE_NORMAL = "正常";
	private static final String CONFIGURETYPE = "CONFIGURETYPE";
	private static final String MANEUVER = "机动";

	private static final String KEY_SCHEDULING_TYPE1 = "SCHEDULING_TYPE";

	public HashMap<String, Object> importScheduling(File uploadFile, Map<String, String> params) {
		DriverSchedulingImportHandler handler = new DriverSchedulingImportHandler(uploadFile, this);
		try {
			handler.handle();
			HashMap<String, Object> result = newHashMap();
			result.put(IMPORT_RESULT, handler.result);
			return result;
		} catch (Exception e) {
			log.debug("export error!" + e);
			throw new BizException(e.getMessage());
		}
	}

	public IUser getUser() {
		return this.getCurrentUser();
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> queryDriverScheduling(HashMap<String, String> queryParameter) {
		queryParameter.put(PAGING_QUERY_STATE, "true");
		queryParameter.put(LIMIT, valueOf(parseInt(queryParameter.get(START)) + parseInt(queryParameter.get(LIMIT))));
		queryParameter.put(SCHEDULING_TYPE, queryParameter.get(SCHEDULING_TYPE));
		int totalSize = driverSchedulingDao.queryDriverSchedulingCount(queryParameter);

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(TOTAL_SIZE, totalSize);
		resultMap.put(ROOT, convertDriverScheduling(driverSchedulingDao.queryDriverScheduling(queryParameter)));

		return resultMap;
	}

	public Map<String, Object> queryDriverScheduledByWeek(HashMap<String, String> queryParameter) {
		queryParameter.put(LIMIT, valueOf(parseInt(queryParameter.get(START)) + parseInt(queryParameter.get(LIMIT))));
		int totalSize = driverSchedulingDao.queryDriverScheduledByWeekSize(queryParameter);
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(TOTAL_SIZE, totalSize);
		resultMap.put(ROOT, convertDriverSchedulingByWeek(driverSchedulingDao.queryDriverScheduledByWeek(queryParameter)));
		return resultMap;
	}

	private List<DriverScheduling> convertDriverScheduling(List<Map<String, Object>> resultList) {
		Map<String, DriverScheduling> map = new HashMap<String, DriverScheduling>();
		DriverScheduling driverScheduling = null;

		for (Map<String, Object> driverSchedulingMap : resultList) {
			String yearMonth = (String) driverSchedulingMap.get(YEAR_MONTH);
			String departmentCode = (String) driverSchedulingMap.get(DEPT_CODE);
			String employeeCode = (String) driverSchedulingMap.get(EMP_CODE);
			String dayOfMonth = (String) driverSchedulingMap.get(DAY_OF_MONTH);
			String configureCode = (String) driverSchedulingMap.get(CONFIGURE_CODE);

			double attendanceDuration = ((BigDecimal) driverSchedulingMap.get("ATTENDANCE_DURATION")).doubleValue();
			double driveDuration = ((BigDecimal) driverSchedulingMap.get("DRIVE_DURATION")).doubleValue();

			String key = employeeCode + "_" + departmentCode + "_" + yearMonth;
			configureCode = setConfigureCode(driverSchedulingMap, configureCode);

			driverScheduling = map.get(key);

			if (driverScheduling == null) {
				driverScheduling = buildDriverSchedulingObject(driverSchedulingMap, map.size() + 1);
				driverScheduling.setConfigureCodeWithSpecifyDay(Integer.parseInt(dayOfMonth.substring(6)), configureCode);

				driverScheduling.setSchedulingAttendanceDuration(attendanceDuration);
				driverScheduling.setDrivingTime(driveDuration);

				map.put(key, driverScheduling);
				continue;
			}

			driverScheduling.setConfigureCodeWithSpecifyDay(Integer.parseInt(dayOfMonth.substring(6)), configureCode);

			attendanceDuration += driverScheduling.getSchedulingAttendanceDuration();
			driverScheduling.setSchedulingAttendanceDuration(attendanceDuration);

			driveDuration += driverScheduling.getDrivingTime();
			driverScheduling.setDrivingTime(driveDuration);
		}

		List<DriverScheduling> driverSchedulings = updateAllTotalRestDays(convertMapToList(map));

		if (driverScheduling != null ) {
			// 最大连续出勤天数
			updateCalculateContinuousAttendanceDay(driverSchedulings);
		}

		return driverSchedulings;
	}

	private void updateCalculateContinuousAttendanceDay(List<DriverScheduling> driverSchedulings) {
		for (DriverScheduling scheduling : driverSchedulings) {
			scheduling.updateCalculateContinuousAttendanceDay();
		}
	}

	private String setConfigureCode(Map<String, Object> driverSchedulingMap, String configureCode) {
		String configureType = driverSchedulingMap.get(CONFIGURETYPE) == null ? "" : driverSchedulingMap.get(CONFIGURETYPE).toString();


		if (driverSchedulingMap.get("APPLY_TYPE") != null && "1".equals(driverSchedulingMap.get("APPLY_TYPE").toString()))
			return "请假";

		if ("0".equals(configureType))
			return configureCode = MANEUVER + configureCode;
		return configureCode;
	}

	private List<DriverScheduling> convertMapToList(Map<String, DriverScheduling> map) {
		List<DriverScheduling> driverSchedulingList = new ArrayList<DriverScheduling>();
		for (Map.Entry<String, DriverScheduling> driverSchedulingEntry : map.entrySet()) {
			driverSchedulingList.add(driverSchedulingEntry.getValue());
		}
		return driverSchedulingList;
	}

	private List<DriverScheduling> convertDriverSchedulingByWeek(List<Map<String, Object>> resultList) {
		Map<String, DriverScheduling> map = new HashMap<String, DriverScheduling>();
		DriverScheduling driverScheduling;
		for (Map<String, Object> driverSchedulingMap : resultList) {
			String yearWeek = (String) driverSchedulingMap.get(YEAR_WEEK);
			String departmentCode = (String) driverSchedulingMap.get(DEPT_CODE);
			String employeeCode = (String) driverSchedulingMap.get(EMP_CODE);
			String dayOfMonth = (String) driverSchedulingMap.get(DAY_OF_MONTH);
			String configureCode = (String) driverSchedulingMap.get(CONFIGURE_CODE);
			String key = employeeCode + "_" + departmentCode + "_" + yearWeek;
			configureCode = setConfigureCode(driverSchedulingMap, configureCode);
			driverScheduling = map.get(key);
			if (driverScheduling == null) {
				driverScheduling = buildDriverSchedulingObject(driverSchedulingMap, map.size() + 1);
				driverScheduling.setConfigureCodeWithSpecifyDayOfWeek(dayOfMonth, configureCode);
				map.put(key, driverScheduling);
				continue;
			}

			driverScheduling.setConfigureCodeWithSpecifyDayOfWeek(dayOfMonth, configureCode);
		}

		return updateAllTotalRestDays(convertMapToList(map));
	}

	private List<DriverScheduling> updateAllTotalRestDays(List<DriverScheduling> driverSchedulingList) {
		for (DriverScheduling scheduling : driverSchedulingList) {
			scheduling.setSchedulingAttendanceDuration(getKeepTwoDecimalByTotalCount(scheduling.getSchedulingAttendanceDuration()));
			scheduling.setDrivingTime(getKeepTwoDecimalByTotalCount(scheduling.getDrivingTime()));

			scheduling.updateTotalRestDays();
		}

		Collections.sort(driverSchedulingList, new Comparator<DriverScheduling>() {
			public int compare(DriverScheduling o1, DriverScheduling o2) {
				return o1.getSequence() - o2.getSequence() > 0 ? 1 : -1;
			}
		});
		return driverSchedulingList;
	}

	public DriverScheduling buildDriverSchedulingObject(Map<String, Object> driverSchedulingMap, int sequence) {
		String yearMonth = (String) driverSchedulingMap.get(YEAR_MONTH);
		String yearWeek = (String) driverSchedulingMap.get(YEAR_WEEK);
		String departmentCode = (String) driverSchedulingMap.get(DEPT_CODE);
		String employeeCode = (String) driverSchedulingMap.get(EMP_CODE);
		String areaCode = (String) driverSchedulingMap.get(AREA_CODE);
		String departmentDescribe = (String) driverSchedulingMap.get(DEPT_DESC);
		String employeeName = (String) driverSchedulingMap.get(EMP_NAME);
		String createTimeStr = (String) (driverSchedulingMap.get(CREATE_TIME));
		String createdEmployeeCode = (String) driverSchedulingMap.get(CREATED_EMPLOYEE_CODE);
		String modifiedTimeStr = (String) (driverSchedulingMap.get(MODIFIED_TIME));
		String modifiedEmployeeCode = (String) driverSchedulingMap.get(MODIFIED_EMPLOYEE_CODE);
		String configureCode = (String) driverSchedulingMap.get(CONFIGURE_CODE);
		String departmentAreaDesc = (String) driverSchedulingMap.get(DEPARTMENT_AREA_DESC);
		String departmentId = (String) driverSchedulingMap.get(DEPARTMENT_ID);
		String workType = (String) driverSchedulingMap.get(WORK_TYPE);
		Object confirmStatus = driverSchedulingMap.get(CONFIRM_STATUS);
		String confirmDate = (String) driverSchedulingMap.get(CONFIRM_DATE);
		String configureType = driverSchedulingMap.get("CONFIGURETYPE") == null ? "" : driverSchedulingMap.get("CONFIGURETYPE").toString();
		DriverScheduling driverScheduling = new DriverScheduling();
		driverScheduling.setConfirmDateAsString(confirmDate);
		if (confirmStatus == null) {
			driverScheduling.setConfirmStatus(0L);
		} else {
			driverScheduling.setConfirmStatus(Long.parseLong(String.valueOf(confirmStatus)));
		}
		if (driverScheduling.getConfirmStatus() == 0L) {
			driverScheduling.setConfirmDateAsString(null);
		}
		driverScheduling.setAreaCode(areaCode);
		driverScheduling.setDepartmentCode(departmentCode);
		driverScheduling.setEmployeeCode(employeeCode);
		driverScheduling.setEmployeeName(employeeName);
		driverScheduling.setDeptDesc(departmentDescribe);
		driverScheduling.setCreator(createdEmployeeCode);
		driverScheduling.setCreatedTimeStr(createTimeStr);
		driverScheduling.setModifier(modifiedEmployeeCode);
		driverScheduling.setModifiedTimeStr(modifiedTimeStr);
		driverScheduling.setConfigureCode(configureCode);
		driverScheduling.setYearMonth(yearMonth);
		driverScheduling.setDepartmentAreaDesc(departmentAreaDesc);
		driverScheduling.setSequence(sequence);
		driverScheduling.setDepartmentId(departmentId);
		driverScheduling.setWorkType(workType);
		driverScheduling.setYearWeek(yearWeek);
		driverScheduling.setConfigureType(configureType);
		return driverScheduling;
	}

	public DriverSchedulingDao getDriverSchedulingDao() {
		return driverSchedulingDao;
	}

	public LineConfigureDao getLineConfigureDao() {
		return lineConfigureDao;
	}

	public OutEmployeeDao getEmployeeDao() {
		return outEmployeeDao;
	}

	public HashMap<String, Object> exportDriverScheduling(HashMap<String, String> queryParameter) {
		try {
			// 排班数据
			List<Map<String, Object>> scheduling = BeanUtil.transListBeanToMap(driverSchedulingDao.querySchedulingByQueryParameter(queryParameter));
			// 导出
			DriverSchedulingExportHandler handler = new DriverSchedulingExportHandler();
			handler.setQueryDepartmentCode(queryParameter.get(KEY_DEPARTMENT_CODE));
			handler.setSchedulingMonth(queryParameter.get(KEY_YEAR_MONTH));
			handler.handle(scheduling);

			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put(KEY_SUCCESS, true);
			resultMap.put(DOWNLOAD_PATH, handler.getDownloadPath());

			return resultMap;
		} catch (Exception e) {
			log.error("export error!" + e);
			throw new BizException("exception:" + e);
		}
	}

	private double getKeepTwoDecimalByTotalCount(double totalCount) {
		try {
			return Double.parseDouble(new DecimalFormat(".##").format(totalCount / getLastDayOfMonth(new Date())));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public Map<String, Object> queryNoSchedulingEmployees(Map<String, String> queryParameter) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(TOTAL_SIZE, driverSchedulingDao.queryNoSchedulingEmployeesCount(queryParameter).size());
		resultMap.put(ROOT, driverSchedulingDao.queryNoSchedulingEmployees(queryParameter));
		return resultMap;
	}
	
	// 校验转网转岗
	public String validSchedulingTimeReturnErrorMessage(List<String> updateDays, String employeeCode, String departmentCode) {
		Map<String, Object> map = driverSchedulingDao.queryEmployeeInformation(employeeCode, departmentCode);
		
		for (String day : updateDays) {
			// 当排班日期小于入职日期
			if (Integer.parseInt(day) < Integer.parseInt(map.get("SF_DATE").toString())) {
				return "员工" + employeeCode + "的排班日期不能小于入职日期" + map.get("SF_DATE") + ",请通过“新增”或“修改”功能录入入职日期后的排班数据";
			}

			// 当排班日期大于离职日期时
			if (Integer.parseInt(day) >= Integer.parseInt(map.get("DIMISSION_DT").toString()) && Integer.parseInt(map.get("DIMISSION_DT").toString()) != 0) {
				return "员工" + employeeCode + "的排班日期不能大于离职日期" + map.get("DIMISSION_DT") + ",请通过“新增”或“修改”功能录入离职日期前的排班数据";
			}
			
			// 当排班日期大于转网日期时
			if (Integer.parseInt(day) < Integer.parseInt(map.get("DATE_FROM").toString())) {
				return "员工" + employeeCode + "的排班日期不能小于转网日期" + map.get("DATE_FROM") + ",请通过“新增”或“修改”功能录入转网日期后的排班数据";
			}

			// 当排班日期大于转岗日期时
			if (Integer.parseInt(day) < Integer.parseInt(map.get("TRANSFER_DATE").toString())) {
				return "员工" + employeeCode + "的排班日期不能小于转岗日期" + map.get("TRANSFER_DATE") + ",请通过“新增”或“修改”功能录入转岗日期后的排班数据";
			}
		}
		
		return "";
	}
	
	// 校验转网转岗
		public String validWhetherCanBeModified(List<String> updateDays, String employeeCode, String departmentCode) {
			Map<String, Object> map = driverSchedulingDao.queryEmployeeConvertDate(employeeCode);
			boolean isValid = true;
			
			if (null == map.get("LAST_ZNO") ) {
				isValid = false;
			}
			
			for (String day : updateDays) {
				// 当排班日期小于入职日期
				if (Integer.parseInt(day) < Integer.parseInt(map.get("SF_DATE").toString())) {
					return "员工" + employeeCode + "的排班日期不能小于入职日期" + map.get("SF_DATE") + ",请通过“新增”或“修改”功能录入入职日期后的排班数据";
				}

				// 当排班日期大于离职日期时
				if (Integer.parseInt(day) >= Integer.parseInt(map.get("DIMISSION_DT").toString()) && Integer.parseInt(map.get("DIMISSION_DT").toString()) != 0) {
					return "员工" + employeeCode + "的排班日期不能大于离职日期" + map.get("DIMISSION_DT") + ",请通过“新增”或“修改”功能录入离职日期前的排班数据";
				}
				
				// 当排班日期大于转岗日期时
				if (Integer.parseInt(map.get("TRANSFER_DATE").toString()) != 0) {
					if (map.get("EMP_POST_TYPE").toString().equals("5")) {
						if (Integer.parseInt(day) < Integer.parseInt(map.get("TRANSFER_DATE").toString())) {
							return "员工" + employeeCode + "的排班日期不能小于转岗日期" + map.get("TRANSFER_DATE") + ",请通过“新增”或“修改”功能录入转岗日期后的排班数据";
						}
					} else {
						if (Integer.parseInt(day) >= Integer.parseInt(map.get("TRANSFER_DATE").toString())) {
							return "员工" + employeeCode + "的排班日期不能大于转岗日期" + map.get("TRANSFER_DATE") + ",请通过“新增”或“修改”功能录入转岗日期后的排班数据";
						}
					}
				}
				
				if (isValid) {
					if (map.get("DEPT_CODE").toString().equals(departmentCode)) {
						if (Integer.parseInt(day) < Integer.parseInt(map.get("DATE_FROM").toString())) {
							return "员工" + employeeCode + "的排班日期不能小于转网日期" + map.get("DATE_FROM") + ",请通过“新增”或“修改”功能录入转网日期后的排班数据";
						}
					} else {
						Map<String, Object> resultMap = driverSchedulingDao.queryWhetherCanBeModified(day,employeeCode,departmentCode);
						if (Integer.parseInt(resultMap.get("COUNT").toString()) == 0){
							return "员工" + employeeCode + "在"+ departmentCode +"网点的"+ day +"号的排班日期不在有效期内";
						}
					}
				} else {
					if (Integer.parseInt(day) < Integer.parseInt(map.get("DATE_FROM").toString())) {
						return "员工" + employeeCode + "的排班日期不能小于转网日期" + map.get("DATE_FROM") + ",请通过“新增”或“修改”功能录入转网日期后的排班数据";
					}
				}
				 
				
				
			}
			
			return "";
		}
	
	private DriverScheduling getCurrentSchedulingByParameter(String employeeCode, String departmentCode, String yearWeek) {
		return buildQuerySchedulingByYearWeek(departmentCode, employeeCode, yearWeek);
	}
	
	private DriverScheduling buildSchedulingObject(String employeeCode,
			String departmentCode, String configureCode, String yearWeek,
			String day) {
		DriverScheduling scheduling = new DriverScheduling();
		scheduling.setEmployeeCode(employeeCode);
		scheduling.setDepartmentCode(departmentCode);
		scheduling.setYearMonth(getMonthOfYear(day));
		scheduling.setYearWeek(yearWeek);
		scheduling.setConfigureCode(configureCode);
		scheduling.setCode(configureCode);
		scheduling.setDayOfMonth(day);
		scheduling.setCreatedTime(new Date());
		scheduling.setCreator(getCurrentUser().getUsername());
		scheduling.setModifiedTime(new Date());
		scheduling.setModifier(getCurrentUser().getUsername());
		scheduling.setSchedulingType(1L);
		scheduling.setSyncState(0L);
		scheduling.setWorkType(WORK_TYPE_NORMAL);
		scheduling.setConfirmStatus(0L);
		
		return scheduling;
	}

	@Transactional
	public Map<String, Object> updateDriverScheduling(HashMap<String, String> queryParameter) throws IllegalAccessException, InvocationTargetException {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		List<String> updateDays = newArrayList(queryParameter.get(SCHEDULING_DAYS).replace("-", "").split(","));
		String employeeCode = queryParameter.get(KEY_EMPLOYEE_CODE);
		String departmentCode = queryParameter.get(KEY_DEPARTMENT_CODE);
		String configureCode = queryParameter.get(KEY_CONFIGURE_CODE);
		String yearWeek = queryParameter.get(KEY_YEAR_WEEK);
		
		DriverScheduling driverScheduling = getCurrentSchedulingByParameter(employeeCode, departmentCode, yearWeek);
		
		List<DriverScheduling> schedulings = driverSchedulingDao.findBy(driverScheduling);
		
		List<DriverScheduling> addedSchedulings = newArrayList();
		
		String errorMessage = validWhetherCanBeModified(updateDays, employeeCode, departmentCode);
		resultMap.put("errorMessage", errorMessage);
		
		if (StringUtil.isNotBlank(errorMessage)) {
			return resultMap;
		}
		
		for (String day : updateDays) {
			if (Integer.parseInt(day) < getMondayByYearWeek(yearWeek) || Integer.parseInt(day) >= getSundayByYearWeek(yearWeek)) {
				continue;
			}

			boolean flag = true;
			for (DriverScheduling scheduling : schedulings) {
				scheduling.setModifiedTime(new Date());
				scheduling.setModifier(getCurrentUser().getUsername());
				scheduling.setConfirmStatus(0L);
				scheduling.setConfirmDate(null);
				if (!scheduling.getDayOfMonth().equals(day)) {
					continue;
				}
				if (afterCurrentWeek(scheduling.getDayOfMonth()) || 1 == scheduling.getSchedulingType()) {
					scheduling.setConfigureCode(configureCode);
					scheduling.setSyncState(0L);
				}
				flag = false;
			}
			if (!flag)
				continue;
			
			addedSchedulings.add(buildSchedulingObject(employeeCode,
					departmentCode, configureCode, yearWeek, day));
		}
		
		driverSchedulingDao.deleteAfterSwitchingDepartmentScheduling(employeeCode, departmentCode, updateDays);
		driverSchedulingDao.updateBatch(schedulings);
	
		if (schedulings.size() == 0) {
			// 发送短信提醒
			messageDao.save(addedSchedulings.get(0).buildPushMessage(
					false));
		} else {
			// 发送短信提醒
			messageDao.save(schedulings.get(0).buildPushMessage(
					false));
		}
		
		if (!addedSchedulings.isEmpty()) {
			driverSchedulingDao.saveBatch(addedSchedulings);
		}
		
		return resultMap;
	}

	private int getSundayByYearWeek(String yearWeek) {
		Calendar cal = createCalendarByYearWeek(yearWeek);
		cal.add(Calendar.DAY_OF_WEEK, 7);
		return Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(cal.getTime()));
	}

	private int getMondayByYearWeek(String yearWeek) {
		Calendar cal = createCalendarByYearWeek(yearWeek);
		return Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(cal.getTime()));
	}

	private Calendar createCalendarByYearWeek(String yearWeek) {
		Calendar cal = Calendar.getInstance(Locale.FRENCH);
		cal.clear();
		cal.set(Calendar.YEAR, Integer.parseInt(yearWeek.split("-")[0]));
		cal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(yearWeek.split("-")[1]));
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal;
	}

	private DriverScheduling buildQuerySchedulingByYearWeek(String departmentCode, String employeeCode, String yearWeek) {
		DriverScheduling driverScheduling = new DriverScheduling();
		driverScheduling.setDepartmentCode(departmentCode);
		driverScheduling.setEmployeeCode(employeeCode);
		driverScheduling.setYearWeek(yearWeek);
		return driverScheduling;
	}

	private static boolean afterCurrentWeek(String day) {
		DateTime date;
		try {
			date = new DateTime(DateUtil.parseDate(day, DateFormatType.yyyyMMdd));
		} catch (ParseException e) {
			throw new BizException("日期解析错误！");
		}
		DateTime now = new DateTime();
		DateTime sunday = now.withDayOfWeek(7).withHourOfDay(23).withMinuteOfHour(59);
		return date.isAfter(sunday);
	}
	
	// 删除转网后的数据
	public void deleteAfterSwitchingDepartmentScheduling(String employeeCode, String departmentCode, List<String> schedulingDays) {
		String dayOfMonth = getEmployeeSwitchingDepartmentTime(employeeCode, departmentCode);
		if (Integer.parseInt(dayOfMonth) > 0) 
			driverSchedulingDao.deleteAfterSwitchingDepartmentScheduling(employeeCode, departmentCode, schedulingDays);
	}
	
	// 获取员工转网时间
	private String getEmployeeSwitchingDepartmentTime(String employeeCode, String departmentCode) {
		Map<String, Object> map = driverSchedulingDao.queryEmployeeInformation(employeeCode, departmentCode);
		
		return String.valueOf((BigDecimal) map.get("DATE_FROM"));
	}

	@Transactional
	public Map<String, Object> addScheduling(Map<String, String> queryParameter) throws IllegalAccessException, InvocationTargetException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<String> schedulingDays = newArrayList(queryParameter.get(SCHEDULING_DAYS).split(","));
		String[] employeeCodes = queryParameter.get(KEY_EMPLOYEE_CODE).split(",");
		String configureCode = queryParameter.get(KEY_CONFIGURE_CODE);
		String yearWeek = queryParameter.get(KEY_YEAR_WEEK);
		
		for (String employeeCodeAndDepartment : employeeCodes) {
			String employeeCode = employeeCodeAndDepartment.split("/")[0];
			String departmentCode = employeeCodeAndDepartment.split("/")[1];
			
			String errorMessage = validSchedulingTimeReturnErrorMessage(schedulingDays, employeeCode, departmentCode);
			if (StringUtil.isNotBlank(errorMessage)) {
				resultMap.put("errorMessage", errorMessage);
				break;
			}
			
			// 删除转网后的数据
			deleteAfterSwitchingDepartmentScheduling(employeeCode, departmentCode, schedulingDays);
		}

		if (resultMap.get("errorMessage") == null) {
			for (String employeeCodeAndDepartment : employeeCodes) {
				List<DriverScheduling> driverSchedulingList = newArrayList();
				String departmentCode = employeeCodeAndDepartment.split("/")[1];
				String employeeCode = employeeCodeAndDepartment.split("/")[0];
				
				for (String day : schedulingDays) {
					if (Integer.parseInt(day) < getMondayByYearWeek(yearWeek)
							|| Integer.parseInt(day) >= getSundayByYearWeek(yearWeek)) {
						continue;
					}
					driverSchedulingList.add(buildSchedulingObject(employeeCode,
							departmentCode, configureCode, yearWeek, day));
				}
				driverSchedulingDao.saveBatch(driverSchedulingList);
				messageDao.save(driverSchedulingList.get(0).buildPushMessage(true));
			}
		}
		
		return resultMap;
	}
	
	private String getMonthOfYear(String day) {
		DateTime date;
		String yearMonth;
		try {
			date = new DateTime(DateUtil.parseDate(day, DateFormatType.yyyyMMdd));
			
			yearMonth = date.getYear() + "-" + StringUtil.getMonthLeftPaddingWithZero(date
							.getMonthOfYear());
			
			if (yearMonth.indexOf("-") == -1) {
				throw new BizException("年月格式错误！" + yearMonth);
			}
		} catch (ParseException e) {
			throw new BizException("日期解析错误！");
		}
		return yearMonth;
	}

	@Transactional
	public void deleteScheduling(Map<String, String> queryParameter) {
		String[] employeeCodes = queryParameter.get(KEY_EMPLOYEE_CODE).split(",");
		String month = queryParameter.get(KEY_YEAR_MONTH);
		String yearWeek = queryParameter.get(YEAR_WEEK);
		for (String employeeCode : employeeCodes) {
			DriverScheduling scheduling = new DriverScheduling();
			scheduling.setEmployeeCode(employeeCode);
			if (StringUtil.isNotBlank(month)) {
				scheduling.setYearMonth(month);
			}
			if (StringUtil.isNotBlank(yearWeek)) {
				scheduling.setYearWeek(yearWeek);
			}
			driverSchedulingDao.removeBatch(driverSchedulingDao.findBy(scheduling));
		}
	}

	public Map<String, Object> exportNotSchedulingEmployee(HashMap<String, String> httpRequestParameter) {
		httpRequestParameter.put(Constants.START, "0");
		httpRequestParameter.put(Constants.LIMIT, "10000");

		List<Map<String, Object>> list = (List<Map<String, Object>>) driverSchedulingDao.queryNoSchedulingEmployees(httpRequestParameter);
		if (list.size() == 0) {
			throw new BizException("没有未排班数据！");
		}

		buildExportData(list);

		DriverNotScheduledEmployeeExportHandler exportHandler = new DriverNotScheduledEmployeeExportHandler();
		exportHandler.setYearOfWeek(httpRequestParameter.get("year_week"));
		exportHandler.setRemoveHiddenRow(false);

		try {
			exportHandler.handle(list);

			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put(KEY_SUCCESS, true);
			resultMap.put(DOWNLOAD_PATH, exportHandler.getDownloadPath());

			return resultMap;
		} catch (Exception e) {
			throw new BizException("数据解析发生异常！");
		}
	}

	public void buildExportData(List<Map<String, Object>> list) {
		for (Map<String, Object> obj : list) {
			obj.put("areaCode", obj.get(AREA_CODE));
			obj.put("departmentCode", obj.get(DEPT_CODE));
			obj.put("employeeName", obj.get(EMP_NAME));
			obj.put("employeeCode", obj.get(EMP_CODE));
			obj.put("workType", "正常");
		}
	}

	public List<DriverScheduling> querySchedulingByEmployeeCode(String employeeCode, String week) {

		List<DriverScheduling> schedulings = (List<DriverScheduling>) driverSchedulingDao.findBy(buildCriteriaForQueryDriverScheduling(employeeCode,
				week));
		if (schedulings.isEmpty())
			return schedulings;

		for (DriverScheduling scheduling : schedulings) {
			String configureCode = scheduling.getConfigureCode();
			setApplyInfo(employeeCode, scheduling);
			if ("休".equals(configureCode)) {
				continue;
			}
			setLineInfo(scheduling);
		}
		return schedulings;
	}

	private void setLineInfo(DriverScheduling scheduling) {
		List<DriveLine> lines = lineConfigureDao.queryLineByConfigCode(scheduling.getDepartmentOfConfigureCode(),
				scheduling.getCodeOfConfigureCode(), scheduling.getYearMonth());
		scheduling.setLines(lines);
	}

	private void setApplyInfo(String employeeCode, DriverScheduling scheduling) {
		DetachedCriteria dc = buildCriteriaForQueryDriverLine(employeeCode, scheduling);
		List<ApplyRecord> applyRecords = (List<ApplyRecord>) applyDao.findBy(dc);
		if (applyRecords.isEmpty()) {
			return;
		}
		scheduling.setApplyType(applyRecords.get(0).getApplyType());
		scheduling.setStatus(applyRecords.get(0).getStatus());
	}

	private DetachedCriteria buildCriteriaForQueryDriverLine(String employeeCode, DriverScheduling scheduling) {
		DetachedCriteria dc;
		dc = DetachedCriteria.forClass(ApplyRecord.class);
		dc.add(Restrictions.eq("applyEmployeeCode", employeeCode));
		dc.add(Restrictions.eq("dayOfMonth", scheduling.getDayOfMonth()));
		dc.addOrder(Order.desc("applyId"));
		return dc;
	}

	private DetachedCriteria buildCriteriaForQueryDriverScheduling(String employeeCode, String week) {
		DetachedCriteria dc = DetachedCriteria.forClass(DriverScheduling.class);
		dc.add(Restrictions.eq("employeeCode", employeeCode));
		dc.add(Restrictions.eq("yearWeek", week));
		dc.add(Restrictions.eq("schedulingType", 1L));
		dc.addOrder(Order.asc("dayOfMonth"));
		return dc;
	}

	@Transactional
	public void confirmScheduling(String empCode, String yearWeek) {
		List<DriverScheduling> schedulings = querySchedulingByEmployeeCode(empCode, yearWeek);
		for (DriverScheduling driverScheduling : schedulings) {
			driverScheduling.setConfirmStatus(1L);
			driverScheduling.setConfirmDate(new Date());
		}
		driverSchedulingDao.updateBatch(schedulings);

		driverSchedulingDao.insertSchedulingConfirmRecord(empCode, yearWeek);
	}

	@Transactional
	public void handImportSuccess(List<DriverScheduling> schedulings) {
		DriverScheduling deleteScheduling = new DriverScheduling();
		deleteScheduling.setYearWeek(schedulings.get(0).getYearWeek());
		deleteScheduling.setEmployeeCode(schedulings.get(0).getEmployeeCode());

		driverSchedulingDao.removeBatch(driverSchedulingDao.findBy(deleteScheduling));
		driverSchedulingDao.saveBatch(schedulings);
		messageDao.save(schedulings.get(0).buildPushMessage(true));
	}

	// 查询按周导出的数据
	private List<WeeklyExportModel> weeklyExport(String departmentCode, String weekOfYear, String confirmStatus, String employeeCode, int start,
			int limit) {
		return buildWeeklyExportModel(driverSchedulingDao
				.queryWeeklyExportData(departmentCode, weekOfYear, confirmStatus, employeeCode, start, limit));
	}

	// 转换Map To List集合
	private List<WeeklyExportModel> buildWeeklyExportModel(List<Map<String, Object>> data) {
		List<WeeklyExportModel> list = new ArrayList<WeeklyExportModel>();

		for (Map<String, Object> obj : data) {
			String areaCode = (String) obj.get("AREA_CODE");
			String departmentCode = (String) obj.get("DEPT_CODE");
			String yearWeek = (String) obj.get("YEAR_WEEK");
			String employeeName = (String) obj.get("EMP_NAME");
			String employeeCode = (String) obj.get("EMP_CODE");
			int status = ((BigDecimal) obj.get("CONFIRM_STATUS")).intValue();
			String configureTime = obj.get("CONFIRM_DATE") == null ? "" : (String) obj.get("CONFIRM_DATE");

			WeeklyExportModel model = new WeeklyExportModel(areaCode, departmentCode, yearWeek, employeeName, employeeCode, status, configureTime,
					list.size() + 1);

			list.add(model);

		}

		return list;
	}

	// 查询当月按周导出数据总数
	public int queryWeeklyExportTotalSize(String departmentCode, String weekOfYear, String confirmStatus, String employeeCode) {
		return driverSchedulingDao.queryWeeklyExportDataCount(departmentCode, weekOfYear, confirmStatus, employeeCode).size();
	}

	public String exportWeekReport(int totalSize, Map<String, String> queryParams) {
		// 获取时间，用于创建存取EXCEL 路径
		String dateTime = DateUtil.formatDate(new Date(), yyyyMMdd_HHmmss);
		CommonUtil.yyyyMMddHHmm = dateTime;
		// 写入excele，返回文件路径数组
		List<String> files = this.writeToExcele(totalSize, queryParams);
		// 打压返回压缩文件路径
		return compressFile(files, dateTime);
	}

	private List<String> writeToExcele(int totalSize, Map<String, String> queryParams) {
		// 获取导出次数、每次导出6万条数据
		int pages = (int) Math.ceil(totalSize / (float) EXPORT_MAX);
		// 存储导出文件路径
		List<String> files = new ArrayList<String>();

		// 循环导出
		for (int i = 0; i < pages; i++) {
			DriverWeeklyExportHandler handler = new DriverWeeklyExportHandler();

			// 通过查询条件，获取导出数据
			List<WeeklyExportModel> list = weeklyExport(extractDeaprtmentCode(queryParams), extractWeekInYear(queryParams),
					extractConfirmStatus(queryParams), extractEmployeeCode(queryParams), i * EXPORT_MAX, (i * EXPORT_MAX) + EXPORT_MAX);
			// 调用导出方法、转入导出数据集合、导出Excel名称
			handler.writeAsObjectToExcel(list, "(" + (i + 1) + ")");

			// 存储保存路径
			files.add(handler.getTargetFilePath());
		}

		return files;
	}

	// 压缩Excel文件
	private String compressFile(List<String> files, String dateTime) {
		if (files.size() > 0) {
			try {
				logger.info("生成的EXCEL文件路径" + files);
				return ZipUtil.doZipExcel(files.get(0), dateTime, "WeekExport");
			} catch (IOException e) {
				logger.error("压缩EXCEL文件出错:" + e);
				e.printStackTrace();
			}
		}
		return "";
	}

	// 获取按周导出总数据量
	public int countAllByWeek(Map<String, String> queryParams) {
		return queryWeeklyExportTotalSize(extractDeaprtmentCode(queryParams), extractWeekInYear(queryParams), extractConfirmStatus(queryParams),
				extractEmployeeCode(queryParams));
	}

	// 从http request的参数集里取网点数据;
	private String extractDeaprtmentCode(Map<String, String> queryParams) {
		return extractValueByKey(queryParams, "departmentCode");
	}

	// 从http request的参数集里取年周数数据;
	private String extractWeekInYear(Map<String, String> queryParams) {
		return extractValueByKey(queryParams, "year_week");
	}

	// 从http request的参数集里取确认状态数据;
	private String extractConfirmStatus(Map<String, String> queryParams) {
		return extractValueByKey(queryParams, "confirmStatus");
	}

	// 从http request的参数集里取员工工号数据;
	private String extractEmployeeCode(Map<String, String> queryParams) {
		return extractValueByKey(queryParams, "employeeCode");
	}

	// 通过Key获取值
	private String extractValueByKey(Map<String, String> queryParams, String key) {
		return queryParams.get(key);
	}
}
