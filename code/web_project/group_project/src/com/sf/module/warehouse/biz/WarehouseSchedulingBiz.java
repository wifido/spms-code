package com.sf.module.warehouse.biz;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.sf.module.common.domain.Constants.*;
import static com.sf.module.common.util.StringUtil.*;
import static java.lang.String.valueOf;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import javax.annotation.Resource;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.dao.IOssDepartmentDao;
import com.sf.module.common.domain.OssDepartment;
import com.sf.module.common.util.BeanUtil;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.DepartmentHelper;
import com.sf.module.dispatch.domain.SchedulingForDispatch;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.warehouse.dao.WarehouseClassDao;
import com.sf.module.warehouse.dao.WarehouseSchedulingDao;
import com.sf.module.warehouse.dao.WarehouseSchedulingModifyLogDao;
import com.sf.module.warehouse.domain.WarehouseSchedulingModifyLog;

public class WarehouseSchedulingBiz extends BaseBiz {
	private static final String SCHEDULING_CODE = "SCHEDULING_CODE";
	private static final String DAY_OF_MONTH = "DAY_OF_MONTH";
	private static final String START1_TIME = "START1_TIME";
	private static final String END1_TIME = "END1_TIME";
	private static final String SPECIFY_DAY = "2015-03-04 ";
	private static final String DOWNLOAD_PATH = "downloadPath";
	private static final String TYPE_EMPLOYEE_WARHOUSE = "3";
	private static final String SUCCESS = "success";
	private static final String MONTH_ID = "monthId";
	private static final String MONTH2 = "month";
	private static final String DELE_DAY_OF_MONTH = "deleDayOfMonth";
	private static final String MSG = "msg";
	private static final String SCHEDULING_DAYS = "schedulingDays";
	public static final String DEPT_CODE2 = "deptCode";
	private static final String DEPT_CODE = "DEPT_CODE";
	private static final String DEPT_ID = "DEPT_ID";
	private static final String EMP_CODE = "empCode";
	public static final String START_TIME = START1_TIME;
	public static final String END_TIME = END1_TIME;
	public static final String IN_SCHEDULE_CODE = "in_scheduleCode";
	public static final String SCHEDULING_CROSS_TIME_ERROR = "班别时间段出现交叉,请重新设置班别！";
	public static final String SCHEDULING_CODES = "schedulingCodes";
	private static final String IS_CROSS_DAY = "IS_CROSS_DAY";
	private static final int CROSS_DAY = 1;
	private static final int ONE_DAY = 2400;
	private WarehouseSchedulingDao warehouseSchedulingDao;
	private WarehouseClassDao warehouseClassDao;
	private IOssDepartmentDao ossDepartmentDao;
	@Resource
	private WarehouseSchedulingModifyLogDao warehouseSchedulingModifyLogDao;

	public void setOssDepartmentDao(IOssDepartmentDao ossDepartmentDao) {
		this.ossDepartmentDao = ossDepartmentDao;
	}

	public WarehouseClassDao getWarehouseClassDao() {
		return warehouseClassDao;
	}

	public void setWarehouseClassDao(WarehouseClassDao warehouseClassDao) {
		this.warehouseClassDao = warehouseClassDao;
	}

	public void setWarehouseSchedulingDao(WarehouseSchedulingDao warehouseSchedulingDao) {
		this.warehouseSchedulingDao = warehouseSchedulingDao;
	}

	public HashMap<String, Object> querySchedulingInfomation(HashMap<String, String> queryParameter) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();

		queryParameter.put(LIMIT, valueOf(Integer.parseInt(queryParameter.get(START)) + Integer.parseInt(queryParameter.get(LIMIT))));

		checkOfNetworks(queryParameter);

		int totalSize = warehouseSchedulingDao.querySchedulingInformationCount(queryParameter);
		List list = warehouseSchedulingDao.querySchedulingInformation(queryParameter);

		dataMap.put(TOTAL_SIZE, totalSize);
		dataMap.put(ROOT, list);
		return dataMap;
	}

	private void checkOfNetworks(HashMap<String, String> queryParameter) {
		OssDepartment department = ossDepartmentDao.getDepartmentByDeptCode(queryParameter.get("in_departmentCode"));
		DepartmentHelper.isCheckOfNetwork(department, queryParameter);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getEmployeeByEmpCode(HashMap<String, String> queryParameter) {
		Map<String, Object> emp = warehouseSchedulingDao.getEmployeeByEmpCode(queryParameter.get(EMP_CODE));
		putDeptCodeInfo(emp);
		return emp;
	}

	private void putDeptCodeInfo(Map<String, Object> emp) {
		if (null != emp && null != emp.get(DEPT_ID)) {
			Long deptId = Long.parseLong(emp.get(DEPT_ID).toString());
			emp.put(DEPT_CODE, DepartmentCacheBiz.getDepartmentByID(deptId).getDeptCode());
		}
	}

	public Map<String, Object> validSchedulingData(HashMap<String, String> queryParameter) {
		String empCode = queryParameter.get(EMP_CODE);
		String deptCode = queryParameter.get(DEPT_CODE2);
		String schedulingDays = queryParameter.get(SCHEDULING_DAYS);
		String[] dayArray = schedulingDays.split(COMMA);
		StringBuffer sb = new StringBuffer();
		Map<String, Object> result = new HashMap<String, Object>();
		for (String day : dayArray) {
			Map<String, Object> scheduling = warehouseSchedulingDao.checkIsCheduling(empCode, day.replace(BEAMS, ""), deptCode);
			if (scheduling != null) {
				sb.append(day.split(BEAMS)[2]);
				sb.append(COMMA);
			}
		}
		if (!"".equals(sb.toString())) {
			result.put(MSG, sb.toString());
		}
		return result;
	}

	public void updateSchedulingData(HashMap<String, String> queryParameter) {
		String schedulingDays = queryParameter.get(SCHEDULING_DAYS);
		schedulingDays = schedulingDays.replace(BEAMS, "");
		String schedulingCodes = queryParameter.get(SCHEDULING_CODES);
		String deptCode = queryParameter.get(DEPT_CODE2);
		String empCode = queryParameter.get(EMP_CODE);
		String month = queryParameter.get(MONTH2);
		if (!validCrossTime(schedulingCodes, deptCode)) {
			throw new BizException(SCHEDULING_CROSS_TIME_ERROR);
		}
		validCrossTimeForUpdateScheduling(schedulingDays.split(COMMA), schedulingCodes, empCode, deptCode, month);
		WarehouseSchedulingModifyLog moidfyLog = buildMoidfyLog(schedulingDays, deptCode, empCode, month);
		warehouseSchedulingDao.modifyWarehouseScheduling(fillVO(queryParameter), moidfyLog);
	}

	private String getSchedulingDaysThatBeforeModifyTime(String schedulingDays) {
		StringBuilder stringBuilder = new StringBuilder();
		DateTimeFormatter formatter = DateTimeFormat.forPattern(DateFormatType.yyyyMMdd.format);
		for (String day : schedulingDays.split(",")) {
			if (DateTime.parse(day, formatter).isBefore(new DateTime(new Date()).minusDays(1))) {
				stringBuilder.append(day);
				stringBuilder.append(",");
			}
		}
		String days = stringBuilder.toString();
		if (isNotBlank(days)) {
			return days.substring(0, days.length() - 1);
		}
		return null;
	}

	private WarehouseSchedulingModifyLog buildMoidfyLog(String schedulingDays, String deptCode, String empCode, String month) {
		String schedulingDaysThatBeforeModifyTime = getSchedulingDaysThatBeforeModifyTime(schedulingDays);
		int modifyAcount = 0;
		if (isNotBlank(schedulingDaysThatBeforeModifyTime)) {
			modifyAcount = warehouseSchedulingDao.getAcountOfScheduling(empCode, schedulingDaysThatBeforeModifyTime.split(","));
		}
		Map<String, Object> employee = warehouseSchedulingDao.getEmployeeByEmpCode(empCode);
		WarehouseSchedulingModifyLog log = new WarehouseSchedulingModifyLog();
		log.setEmployeeCode(empCode);
		log.setYearMonth(month);
		log.setModifiedEmpCode(getCurrentUser().getEmployee().getCode());
		log.setModifiedDate(new Date());
		log.setModifiedCount(Long.valueOf(modifyAcount));
		log.setDepartmentID(((BigDecimal) employee.get("DEPT_ID")).longValue());
		return log;
	}

	public List<SchedulingForDispatch> fillVO(HashMap<String, String> queryParameter) {
		String createdEmploeeCode = this.getCurrentUser().getEmployee().getCode();
		String empCode = queryParameter.get(EMP_CODE);
		String deptCode = queryParameter.get(DEPT_CODE2);
		String month = queryParameter.get(MONTH2);
		String schedulingDays = queryParameter.get(SCHEDULING_DAYS);
		String schedulingCodes = queryParameter.get(SCHEDULING_CODES);
		String[] dayArray = schedulingDays.split(COMMA);
		List<SchedulingForDispatch> insertList = new ArrayList<SchedulingForDispatch>();

		queryParameter.put(DELE_DAY_OF_MONTH, schedulingDays.replace(BEAMS, ""));
		Date now = new Date();
		for (String day : dayArray) {
			String[] codeArray = schedulingCodes.split(COMMA);
			for (String code : codeArray) {
				SchedulingForDispatch scheduling = new SchedulingForDispatch();
				scheduling.setDayOfMonth(day.replace(BEAMS, ""));
				scheduling.setMonthId(month.replace(BEAMS, ""));
				scheduling.setDepartmentCode(deptCode);
				scheduling.setEmployeeCode(empCode);
				scheduling.setSchedulingCode(code);
				scheduling.setCreatedEmployeeCode(createdEmploeeCode);
				scheduling.setCreateTime(now);
				scheduling.setModifiedEmployeeCode(createdEmploeeCode);
				scheduling.setModifiedTime(now);
				scheduling.setEmpPostType(TYPE_EMPLOYEE_WARHOUSE);
				insertList.add(scheduling);
			}
		}
		return insertList;
	}

	public Map<String, Object> queryNotSchedulingStaff(HashMap<String, String> queryParameter) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();

		queryParameter.put(LIMIT, valueOf(Integer.parseInt(queryParameter.get(START)) + Integer.parseInt(queryParameter.get(LIMIT))));

		int totalSize = warehouseSchedulingDao.queryNotSchedulingStaffTotal(queryParameter);
		List list = warehouseSchedulingDao.queryNotSchedulingStaff(queryParameter);

		dataMap.put(TOTAL_SIZE, totalSize);
		dataMap.put(ROOT, list);
		return dataMap;
	}

	public void validAcrossTime(String[] dayArray, String schedulingCodes, String departmentCode) {
		List<Map<String, Object>> list = queryClassDetailInfo(schedulingCodes, departmentCode);
		for (int i = 0; i < dayArray.length; i++) {
			if (schedulingCodes.equals("休")) {
				continue;
			}
			if (i == dayArray.length - 1) {
				continue;
			}
			if (isContinueDay(dayArray, i)) {
				continue;
			}
			try {
				if (existCrossTime(list)) {
					throw new BizException("上班时间存在冲突！");
				}
				return;
			} catch (ParseException e) {
				throw new BizException("开始结束时间格式错误！");
			}
		}
	}

	public void validCrossTimeForUpdateScheduling(
	        String[] arrayOfModifiedDays,
	        String modifiedSchedulingCodes,
	        String empCode,
	        String departmentCode,
	        String month) {

		List<Map<String, Object>> schedulingsOfEmployeeSpeicMonth = warehouseSchedulingDao.querySchedulingBySpecifiedParameters(
		        empCode,
		        month.replace(BEAMS, ""),
		        departmentCode);

		Map<String, String> schedulingCodesOfEveryDay = newLinkedHashMap();

		for (Map<String, Object> scheduling : schedulingsOfEmployeeSpeicMonth) {
			String dayOfMonth = (String) scheduling.get(DAY_OF_MONTH);
			String code = schedulingCodesOfEveryDay.get(dayOfMonth);
			if (isBlank(code)) {
				schedulingCodesOfEveryDay.put(dayOfMonth, (String) scheduling.get(SCHEDULING_CODE));
				continue;
			}
			code = String.format("%s,%s", code, (String) scheduling.get(SCHEDULING_CODE));
			schedulingCodesOfEveryDay.put(dayOfMonth, code);
		}

		replaceModifiedSchedulingCode(arrayOfModifiedDays, modifiedSchedulingCodes, schedulingCodesOfEveryDay);

		Map.Entry<String, String> prevEntry = null;

		for (Map.Entry<String, String> entry : schedulingCodesOfEveryDay.entrySet()) {
			if (prevEntry == null) {
				prevEntry = entry;
				continue;
			}
			if (REST_MARK.equals(prevEntry.getValue()) || REST_MARK.equals(entry.getValue())) {
				prevEntry = entry;
				continue;
			}
			List<Map<String, Object>> currentClassCodes = queryClassDetailInfo(prevEntry.getValue(), departmentCode);
			List<Map<String, Object>> nextClassCodes = queryClassDetailInfo(entry.getValue(), departmentCode);
			try {
				if (getMaxEndTime(currentClassCodes).after(getMinStartTime(nextClassCodes))) {
					throw new BizException("上班时间存在交叉！");
				}
			} catch (ParseException e) {
				throw new BizException("开始结束时间格式错误！");
			}
			prevEntry = entry;
		}

	}

	private void replaceModifiedSchedulingCode(String[] arrayOfModifiedDays, String schedulingCodes, Map<String, String> schedulingsOfEmployeeSpeicMonth) {

		for (int i = 0; i < arrayOfModifiedDays.length; i++) {
			String day = arrayOfModifiedDays[i].replace("-", "");
			for (Map.Entry<String, String> entry : schedulingsOfEmployeeSpeicMonth.entrySet()) {
				if (day.equals(entry.getKey())) {
					entry.setValue(schedulingCodes);
					break;
				}
			}
		}
	}

	private List<Map<String, Object>> queryClassDetailInfo(String schedulingCodes, String departmentCode) {
		HashMap<String, String> query = new HashMap<String, String>();
		query.put(DEPT_CODE2, departmentCode);
		query.put(IN_SCHEDULE_CODE, schedulingCodes);
		List<Map<String, Object>> list = warehouseClassDao.queryClassTimeByScheduleCode(query);
		return list;
	}

	private boolean isContinueDay(String[] dayArray, int i) {
		return Integer.parseInt(dayArray[i + 1].replace("-", "")) - Integer.parseInt(dayArray[i].replace("-", "")) > 1;
	}

	public static Date getMaxEndTime(List<Map<String, Object>> list) throws ParseException {
		List<Date> endTimes = newArrayList();
		for (Map<String, Object> map : list) {
			String endTime = (String) map.get(END1_TIME);
			Date endDate = DateUtil.parseDate(SPECIFY_DAY + endTime, DateFormatType.yyyy_MM_dd_HHmm);
			if (map.get(IS_CROSS_DAY).equals("1")) {
				endDate = new DateTime(endDate).plusDays(1).toDate();
			}
			endTimes.add(endDate);
		}
		Collections.sort(endTimes);
		Collections.reverse(endTimes);
		return endTimes.get(0);
	}

	public static Date getMinStartTime(List<Map<String, Object>> list) throws ParseException {
		String day = SPECIFY_DAY;
		List<Date> startTimes = newArrayList();
		for (Map<String, Object> map : list) {
			String startTime = (String) map.get(START1_TIME);
			Date startDate = DateUtil.parseDate(SPECIFY_DAY + startTime, DateFormatType.yyyy_MM_dd_HHmm);
			startDate = new DateTime(startDate).plusDays(1).toDate();
			startTimes.add(startDate);
		}
		Collections.sort(startTimes);
		return startTimes.get(0);
	}

	private boolean existCrossTime(List<Map<String, Object>> list) throws ParseException {
		return getMaxEndTime(list).after(getMinStartTime(list)) ? true : false;
	}

	public void addScheduling(HashMap<String, String> queryParameter) {
		String deptCode = queryParameter.get(DEPT_CODE2);
		String schedulingDays = queryParameter.get(SCHEDULING_DAYS);
		String schedulingCodes = queryParameter.get(SCHEDULING_CODES);

		if (!validCrossTime(schedulingCodes, deptCode)) {
			throw new BizException(SCHEDULING_CROSS_TIME_ERROR);
		}

		validAcrossTime(schedulingDays.split(COMMA), schedulingCodes, deptCode);

		warehouseSchedulingDao.updateWarehouseScheduling(fillVO(queryParameter));
	}

	public boolean validCrossTime(String schedulingCodes, String deptCode) {
		HashMap<String, String> qureyClassParameter = new HashMap<String, String>();
		qureyClassParameter.put(IN_SCHEDULE_CODE, schedulingCodes);
		qureyClassParameter.put(DEPT_CODE2, deptCode);
		List list = warehouseClassDao.queryClassTimeByScheduleCode(qureyClassParameter);
		return validTime(list);
	}

	public static boolean validTime(List list) {
		Map warehouseClassDataMap;
		for (int i = 0; i < list.size(); i++) {
			warehouseClassDataMap = (HashMap) list.get(i);
			Integer currentBeginTime = formatConversion(warehouseClassDataMap, START_TIME);
			Integer currentEndTime = formatConversion(warehouseClassDataMap, END_TIME);
			currentEndTime = crossDay(warehouseClassDataMap, currentEndTime);
			for (int j = 0; j < list.size(); j++) {
				warehouseClassDataMap = (HashMap) list.get(j);
				Integer contrastBeginTime = formatConversion(warehouseClassDataMap, START_TIME);
				Integer contrastEndTime = formatConversion(warehouseClassDataMap, END_TIME);
				contrastEndTime = crossDay(warehouseClassDataMap, contrastEndTime);
				if (i != j) {
					if (currentBeginTime < contrastEndTime && currentEndTime > contrastBeginTime) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private static Integer crossDay(Map warehouseClassDataMap, Integer endTime) {
		if (CROSS_DAY == Integer.parseInt(warehouseClassDataMap.get(IS_CROSS_DAY).toString())) {
			endTime = endTime + ONE_DAY;
		}
		return endTime;
	}

	private static int formatConversion(Map warehouseClassDataMap, String key) {
		return Integer.parseInt(warehouseClassDataMap.get(key).toString().replace(":", ""));
	}

	private boolean contianOneDay(int day, String[] dayArray) {
		String str = day < 10 ? "0" + day : day + "";
		for (String d : dayArray) {
			if (d.split(BEAMS)[2].equals(str)) {
				return true;
			}
		}
		return false;
	}

	public static int getMonthLastDay(int year, int month) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	public void deleteScheduling(HashMap<String, String> queryParameter) {
		warehouseSchedulingDao.deleteScheduling(queryParameter);
	}

	public Map<String, Object> exportScheduling(HashMap<String, String> queryParameter) {
		checkOfNetworks(queryParameter);
		List list = warehouseSchedulingDao.querySchedulingInformation(queryParameter);
		if (list.isEmpty()) {
			throw new BizException("no data!");
		}
		try {
			list = BeanUtil.transListBeanToMap(list);
			WarehouseSchdulingExportHandler handler = new WarehouseSchdulingExportHandler();
			handler.handle(list);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put(SUCCESS, true);
			result.put(DOWNLOAD_PATH, handler.getDownloadPath());
			return result;
		} catch (Exception e) {
			log.debug("export error!" + e);
			throw new BizException("exception:" + e);
		}
	}

	public boolean queryIsBeOverdue(HashMap<String, String> paramsMap) {
		String monthId = paramsMap.get("MONTH_ID").toString();
		if (DateUtil.validConfirmDate(monthId)) {
			return DateUtil.isBeOverdue(monthId);
		}
		return true;
	}

	public Map<String, Object> exportNoSchedulingEmployee(HashMap<String, String> queryParameter) {
		// List list =
		// warehouseSchedulingDao.queryNotSchedulingStaff(queryParameter);
		List list = warehouseSchedulingDao.queryNotSchedulingEmployee(queryParameter);
		if (list.isEmpty()) {
			throw new BizException("no data!");
		}
		try {
			NoSchedulingEmployeeExportHandler handler = new NoSchedulingEmployeeExportHandler();
			handler.setSchedulingMonth(queryParameter.get(MONTH_ID));
			handler.setDepartment(queryParameter.get("departmentCode"));
			handler.handle(list);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put(SUCCESS, true);
			result.put(DOWNLOAD_PATH, handler.getDownloadPath());
			return result;
		} catch (Exception e) {
			log.debug("export error!" + e);
			throw new BizException("exception:" + e);
		}
	}
}
