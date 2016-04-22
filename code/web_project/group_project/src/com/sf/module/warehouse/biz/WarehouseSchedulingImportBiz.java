package com.sf.module.warehouse.biz;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.sf.module.common.domain.Constants.REST_MARK;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import com.google.common.base.Joiner;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.TemplateHelper;
import com.sf.module.dispatch.domain.SchedulingForDispatch;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.operation.domain.SchedulMgt;
import com.sf.module.operation.util.CommonUtil;
import com.sf.module.organization.domain.Department;
import com.sf.module.warehouse.dao.WarehouseClassDao;
import com.sf.module.warehouse.dao.WarehouseSchedulingDao;

public class WarehouseSchedulingImportBiz extends BaseBiz {
	private static final String DISABLE_DT = "DISABLE_DT";
	private static final String ENABLE_DT = "ENABLE_DT";
	private static final String LEAVE_DATE = "DIMISSION_DT";
	private static final String ENTRY_DATE = "SF_DATE";
	private static final String TRANSFER_DEPARTMENT_DATE = "DATE_FROM";
	private static final String TRANSFER_POST_DATE = "TRANSFER_DATE";
	private static final String SCHEDULE_CODE = "SCHEDULE_CODE";
	private static final String STRING_ZERO = "0";
	private static final String STRING_REST = "休";
	public static final String CURRENT_USER = "current_user";
	public static final String SCHEDULING_DEPT_CODE = "dept_code";
	public static final String SCHEDULING_DEPT_Id = "dept_id";
	public static final String SCHEDULING_MONTH = "scheduling_month";
	public static final String SCHEDULING_DAYS_OF_MONTH = "scheduling_days_of_month";
	public static final String SCHEDULING_TOTAL_ROWS = "total_rows";
	public static final int SCHEDULING_ERROR_COLUMN_NUM = 36;
	public static final int SCHEDULING_FIRST_DAY_COLUMN_NUM = 5;
	public static final String SCHEDULING_CLASS_CODE_SEPARATOR = "/";
	public static final String WAREHOUSE_EMPLOYEE_TYPE = "3";

	private WarehouseSchedulingDao warehouseSchedulingDao;
	private WarehouseClassDao warehouseClassDao;

	public WarehouseSchedulingDao getWarehouseSchedulingDao() {
		return warehouseSchedulingDao;
	}

	public void setWarehouseSchedulingDao(WarehouseSchedulingDao warehouseSchedulingDao) {
		this.warehouseSchedulingDao = warehouseSchedulingDao;
	}

	public WarehouseClassDao getWarehouseClassDao() {
		return warehouseClassDao;
	}

	public void setWarehouseClassDao(WarehouseClassDao warehouseClassDao) {
		this.warehouseClassDao = warehouseClassDao;
	}

	public Map<String, Object> importExcel(File uploadFile, HashMap<String, String> paramsMap) {
		log.info("import warehouse sheduling data begin!------");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.putAll(paramsMap);
		log.info("get import excle workBook!-----");
		HSSFWorkbook workBook = getHSSFWorkbook(uploadFile);
		HSSFSheet sheet = workBook.getSheetAt(0);
		log.info("valid import max rows!-----");
		validMaxRowNum(sheet, dataMap);
		log.info("valid month!----");
		validMonth(sheet, dataMap);
		log.info("valid department!-----");
		validDepartment(sheet, dataMap);
		log.info("create error cell!-----");
		createErrorCell(sheet.getRow(2), "失败原因");
		log.info("hand each row begin!-----");
		handEachRow(sheet, dataMap);
		log.info("hand each row end!");
		if (dataMap.containsKey("sucess")) {
			log.info("delete empty rows before write to excel!");
			TemplateHelper.removeEmptyRow(sheet);
			log.info("have error row ,write error info to excle!");
			writeErrorMsgToExcle(dataMap, workBook);
		}
		return dataMap;
	}

	private void writeErrorMsgToExcle(Map<String, Object> resultMap, HSSFWorkbook workBook) {
		CommonUtil.setDownloadFileName(generateFailExcelFileName());
		FileOutputStream fos = null;
		try {
			String errorExcleFileName = CommonUtil.getSaveFilePath(SchedulMgt.class);
			File outFile = new File(errorExcleFileName);
			if (!outFile.exists())
				outFile.createNewFile();
			fos = new FileOutputStream(errorExcleFileName);
			workBook.write(fos);
			resultMap.put("errorFileName", CommonUtil.getReturnPageFileName());
		} catch (Exception e) {
			log.error("have a exception when create error info to excle! exception:" + e);
			throw new BizException("保存路径不存在！");
		}
	}

	private String generateFailExcelFileName() {
		String time = DateUtil.formatDate(new Date(), DateFormatType.yyyyMMdd_HHmmss);
		String fileName = "仓管排班数据_" + time + ".xls";
		log.info("generateFailExcelFileName :" + fileName);
		return fileName;
	}

	private boolean existErrorMsg(HSSFRow row, Set<String> errorMsg, Map<String, Object> dataMap) {
		if (!errorMsg.isEmpty()) {
			setErrorMsgToRowCell(row, dataMap, errorMsg);
			return true;
		}
		return false;
	}

	private void setErrorMsgToRowCell(HSSFRow row, Map<String, Object> dataMap, Set<String> errorMsg) {
		dataMap.put("sucess", false);
		createErrorCell(row, getErrorDesc(errorMsg));
	}

	private void validSchedulingDate(List<SchedulingForDispatch> list, Map<String, Object> dataMap) {
		String schedulingMonth = (String) dataMap.get(SCHEDULING_MONTH);
		Date firstDayOfSchedulingMonth = null;
		Date endDayOfSchedulingMonth = null;
		try {
			firstDayOfSchedulingMonth = DateUtil.parseDate(schedulingMonth, DateFormatType.yyyyMM);
			endDayOfSchedulingMonth = new DateTime(firstDayOfSchedulingMonth).plusMonths(1).toDate();
		} catch (ParseException e) {
			throw new BizException("解析日期错误！");
		}
		Map<String, Object> employee = (Map<String, Object>) dataMap.get(list.get(0).getEmployeeCode());
		Date maxDayOfSchedulingDate = getMaxDayOfScheduling(endDayOfSchedulingMonth, employee);
		Date minDayOfSchedulingDate = getMinDayOfScheduling(firstDayOfSchedulingMonth, employee);
		if (firstDayOfSchedulingMonth == minDayOfSchedulingDate && maxDayOfSchedulingDate == endDayOfSchedulingMonth) {
			return;
		}
		List<SchedulingForDispatch> removeSchedulings = filterScheduling(list, minDayOfSchedulingDate, maxDayOfSchedulingDate);
		removeScheduling(list, removeSchedulings);
	}

	private void removeScheduling(List<SchedulingForDispatch> list, List<SchedulingForDispatch> removeSchedulings) {
		for (SchedulingForDispatch scheduling : removeSchedulings) {
			list.remove(scheduling);
		}
	}

	private List<SchedulingForDispatch> filterScheduling(List<SchedulingForDispatch> list, Date minDayOfSchedulingDate, Date maxDayOfSchedulingDate) {
		List<SchedulingForDispatch> removeSchedulings = newArrayList();
		for (SchedulingForDispatch scheduling : list) {
			try {
				Date dayOfMonth = DateUtil.parseDate(scheduling.getDayOfMonth(), DateFormatType.yyyyMMdd);
				if (dayOfMonth.before(minDayOfSchedulingDate) || dayOfMonth.after(maxDayOfSchedulingDate)) {
					removeSchedulings.add(scheduling);
				}
			} catch (ParseException e) {
				throw new BizException("解析日期错误！");
			}
		}
		return removeSchedulings;
	}

	private Date getMinDayOfScheduling(Date firstDayOfMonth, Map<String, Object> employee) {
		Date transferPostDate = (Date) employee.get(TRANSFER_POST_DATE);
		Date transferDepartmentDate = (Date) employee.get(TRANSFER_DEPARTMENT_DATE);
		Date entryDate = (Date) employee.get(ENTRY_DATE);
		List<Date> dates = newArrayList(firstDayOfMonth);
		if (null != transferPostDate) {
			dates.add(transferPostDate);
		}
		if (null != transferDepartmentDate) {
			dates.add(transferDepartmentDate);
		}
		if (null != entryDate) {
			dates.add(entryDate);
		}
		Collections.sort(dates);
		Collections.reverse(dates);
		return dates.get(0);
	}

	private Date getMaxDayOfScheduling(Date endDayOfMonth, Map<String, Object> employee) {
		Date maxDay = endDayOfMonth;
		Date leaveDate = (Date) employee.get(LEAVE_DATE);
		if (null != leaveDate && leaveDate.before(endDayOfMonth)) {
			maxDay = new DateTime(leaveDate).minusDays(1).toDate();
		}
		return maxDay;
	}

	private void handEachRow(HSSFSheet sheet, Map<String, Object> dataMap) {
		int rows = (Integer) dataMap.get(SCHEDULING_TOTAL_ROWS);
		log.info("toal rows:--------" + rows);
		List<SchedulingForDispatch> insertList = new ArrayList<SchedulingForDispatch>();
		log.info("get dept calss begin!--------");
		List<Map<String, Object>> deptClassDetailInfos = warehouseClassDao.queryClassDetailByDeptId((Long) dataMap.get(SCHEDULING_DEPT_Id));
		Set<String> schedulingEmployees = new HashSet<String>();
		int failNum = 0;
		for (int rowBum = 3; rowBum <= rows; rowBum++) {
			log.info("hand row beign! rownum:-----" + rowBum);
			HSSFRow row = sheet.getRow(rowBum);
			if (row == null) {
				log.info("this row is empty!");
				continue;
			}
			SchedulingForDispatch scheduling = new SchedulingForDispatch();
			Set<String> errorMsg = new HashSet<String>();
			checkEmpCode(row, scheduling, errorMsg, schedulingEmployees, dataMap);

			if (existErrorMsg(row, errorMsg, dataMap)) {
				failNum++;
				continue;
			}

			List<SchedulingForDispatch> tempList = new ArrayList<SchedulingForDispatch>();
			handEveryDayScheduling(dataMap, row, scheduling, errorMsg, deptClassDetailInfos, tempList);

			if (existErrorMsg(row, errorMsg, dataMap)) {
				failNum++;
				continue;
			}

			validCrossTime(tempList, deptClassDetailInfos, errorMsg);

			if (existErrorMsg(row, errorMsg, dataMap)) {
				failNum++;
				continue;
			}

			sheet.removeRow(row);

			validSchedulingDate(tempList, dataMap);

			insertList.addAll(tempList);
		}
		if (!insertList.isEmpty()) {
			log.info("save data to db begin:--------");
			saveDataToDb(insertList);
			log.info("save data to db end:--------");
		}
		schedulingEmployees.clear();
		dataMap.put("tips", "成功导入：" + (rows - failNum - 2) + "条   失败导入: " + failNum + "条!");
	}

	private boolean cotainClassCode(List<Map<String, Object>> classes, String classCode) {
		for (Map<String, Object> classInfo : classes) {
			if (classCode.equals((String) classInfo.get(SCHEDULE_CODE))) {
				return true;
			}
		}
		return false;
	}

	private Map<String, Object> getClassDetailInfo(List<Map<String, Object>> classes, String classCode) {
		for (Map<String, Object> classInfo : classes) {
			if (classCode.equals((String) classInfo.get(SCHEDULE_CODE))) {
				return classInfo;
			}
		}
		return null;
	}

	private void handEveryDayScheduling(
	        Map<String, Object> dataMap,
	        HSSFRow row,
	        SchedulingForDispatch scheduling,
	        Set<String> errorMsg,
	        List<Map<String, Object>> deptClass,
	        List<SchedulingForDispatch> tempList) {

		int daysOfMonth = (Integer) dataMap.get(SCHEDULING_DAYS_OF_MONTH);
		for (int i = SCHEDULING_FIRST_DAY_COLUMN_NUM; i < SCHEDULING_FIRST_DAY_COLUMN_NUM + daysOfMonth; i++) {
			HSSFCell cell = row.getCell(i);
			String schedulingCodes = getCellStrValue(cell);
			if (CommonUtil.isEmpty(schedulingCodes)) {
				errorMsg.add("排班内容不能为空!\n");
				continue;
			}
			String[] codeArray = schedulingCodes.split(SCHEDULING_CLASS_CODE_SEPARATOR);
			if (codeArray.length > 3) {
				errorMsg.add("排班代码不能超过3个!\n");
				continue;
			}

			if (validClassIsRepeat(codeArray)) {
				errorMsg.add("同一天班别代码不能重复!\n");
				continue;
			}

			List<String> codeArrayList = Arrays.asList(codeArray);
			if (codeArray.length > 1 && codeArrayList.contains("休")) {
				errorMsg.add("排班不能同时出现班别代码和休!\n");
				continue;
			}

			for (String code : codeArray) {
				if (CommonUtil.isEmpty(code))
					continue;
				String month = dataMap.get(SCHEDULING_MONTH).toString();
				String dayOfMonth = "";
				int day = (i - SCHEDULING_FIRST_DAY_COLUMN_NUM) + 1;
				if (day < 10) {
					dayOfMonth = month + STRING_ZERO + day;
				} else {
					dayOfMonth = month + day;
				}
				if (STRING_REST.equals(code)) {
					tempList.add(generateEntity(scheduling, code, month, dayOfMonth));
					continue;
				}
				if (!cotainClassCode(deptClass, code)) {
					errorMsg.add("班别代码" + code + " 不存在或已失效!\n");
					continue;
				}
				validExpiredDateOfSchedulingCode(deptClass, code, errorMsg, dayOfMonth);
				
				validTakeEffectTimeOfSchedulingCode(deptClass, code, errorMsg, dayOfMonth);
				if (codeArray.length == 1) {
					tempList.add(generateEntity(scheduling, code, month, dayOfMonth));
					continue;
				}
				String deptCode = (String) dataMap.get(SCHEDULING_DEPT_CODE);
				HashMap<String, String> qureyClassParameter = new HashMap<String, String>();
				qureyClassParameter.put(WarehouseSchedulingBiz.IN_SCHEDULE_CODE, Joiner.on(",").join(codeArray));
				qureyClassParameter.put(WarehouseSchedulingBiz.DEPT_CODE2, deptCode);
				List list = warehouseClassDao.queryClassTimeByScheduleCode(qureyClassParameter);
				if (!WarehouseSchedulingBiz.validTime(list)) {
					errorMsg.add("班别时间段出现交叉,请重新设置班别！\n");
					continue;
				}
				tempList.add(generateEntity(scheduling, code, month, dayOfMonth));
			}
		}
	}

	private void validExpiredDateOfSchedulingCode(List<Map<String, Object>> deptClass, String code, Set<String> errorMsg, String dayOfMonth) {
		Map<String, Object> classDetailInfo = getClassDetailInfo(deptClass, code);
		Date classExpiredTime = (Date) classDetailInfo.get(DISABLE_DT);
		DateTimeFormatter format = DateTimeFormat.forPattern(DateFormatType.yyyyMMdd.format);
		if (null == classExpiredTime)
			return;
		DateTime schedulingDate = DateTime.parse(dayOfMonth, format);
		DateTime classExpiredDate = new DateTime(classExpiredTime);
		if (classExpiredDate.isBefore(schedulingDate)) {
			errorMsg.add(String.format("排班日期冲突！班别代码 %s 将在 %s 失效！", code, classExpiredDate.toString(DateFormatType.yyyyMMdd.format)));
		}
	}
	
	private void validTakeEffectTimeOfSchedulingCode(List<Map<String, Object>> deptClass, String code, Set<String> errorMsg, String dayOfMonth) {
		Map<String, Object> classDetailInfo = getClassDetailInfo(deptClass, code);
		Date enalbeTime = (Date) classDetailInfo.get(ENABLE_DT);
		DateTimeFormatter format = DateTimeFormat.forPattern(DateFormatType.yyyyMMdd.format);
		if (null == enalbeTime)
			return;
		DateTime schedulingDate = DateTime.parse(dayOfMonth, format);
		DateTime classEnableTime = new DateTime(enalbeTime);
		if (schedulingDate.isBefore(classEnableTime)) {
			errorMsg.add(String.format("排班日期冲突！班别代码 %s 将在 %s 生效！", code, classEnableTime.toString(DateFormatType.yyyyMMdd.format)));
		}
	}

	private void validCrossTime(List<SchedulingForDispatch> schedulingList, List<Map<String, Object>> departmentClassDetailInfos, Set<String> errorMsg) {
		Map<String, List<Map<String, Object>>> schedulingCodesOfEveryDay = newLinkedHashMap();

		for (SchedulingForDispatch scheduling : schedulingList) {
			String dayOfMonth = scheduling.getDayOfMonth();
			List<Map<String, Object>> classInfos = schedulingCodesOfEveryDay.get(dayOfMonth);
			if (null == classInfos) {
				classInfos = newArrayList();
				classInfos.add(getClassInfo(departmentClassDetailInfos, scheduling.getSchedulingCode()));
				schedulingCodesOfEveryDay.put(dayOfMonth, classInfos);
				continue;
			}
			classInfos.add(getClassInfo(departmentClassDetailInfos, scheduling.getSchedulingCode()));
		}

		Map.Entry<String, List<Map<String, Object>>> prevEntry = null;

		for (Map.Entry<String, List<Map<String, Object>>> entry : schedulingCodesOfEveryDay.entrySet()) {
			if (prevEntry == null) {
				prevEntry = entry;
				continue;
			}
			List<Map<String, Object>> currentDayClassInfo = entry.getValue();
			List<Map<String, Object>> prevDayClassInfo = prevEntry.getValue();
			if (null == currentDayClassInfo.get(0) || REST_MARK.equals(currentDayClassInfo.get(0).get(SCHEDULE_CODE))) {
				prevEntry = entry;
				continue;
			}
			if (null == prevDayClassInfo.get(0) || REST_MARK.equals(prevDayClassInfo.get(0).get(SCHEDULE_CODE))) {
				prevEntry = entry;
				continue;
			}
			try {
				if (WarehouseSchedulingBiz.getMaxEndTime(prevDayClassInfo).after(WarehouseSchedulingBiz.getMinStartTime(currentDayClassInfo))) {
					errorMsg.add(String.format("%s的下班时间跟%s的上班时间存在冲突", prevEntry.getKey(), entry.getKey()));
					return;
				}
			} catch (ParseException e) {
				errorMsg.add("开始结束时间格式错误");
				return;
			}
			prevEntry = entry;
		}

	}

	private Map<String, Object> getClassInfo(List<Map<String, Object>> deptClass, String classCode) {
		for (Map<String, Object> map : deptClass) {
			if (classCode.equals(map.get(SCHEDULE_CODE))) {
				return map;
			}
		}
		return null;
	}

	private boolean validClassIsRepeat(String[] codeArray) {
		Set set = new HashSet();
		List list = Arrays.asList(codeArray);
		set.addAll(list);
		return set.size() != codeArray.length;
	}

	private SchedulingForDispatch generateEntity(SchedulingForDispatch scheduling, String code, String month, String dayOfMonth) {
		SchedulingForDispatch insertScheduling = new SchedulingForDispatch();
		CommonUtil.copyProperties(insertScheduling, scheduling);
		insertScheduling.setSchedulingCode(code);
		insertScheduling.setMonthId(month);
		insertScheduling.setDayOfMonth(dayOfMonth);
		insertScheduling.setEmpPostType("3");
		insertScheduling.setCreatedEmployeeCode(getCurrentUser().getEmployee().getCode());
		insertScheduling.setCreateTime(new Date());
		insertScheduling.setModifiedEmployeeCode(getCurrentUser().getEmployee().getCode());
		insertScheduling.setModifiedTime(new Date());
		return insertScheduling;
	}

	public boolean saveDataToDb(List<SchedulingForDispatch> schedulingList) {
		return warehouseSchedulingDao.updateWarehouseScheduling(schedulingList);
	}

	private String getErrorDesc(Set<String> errorMsg) {
		StringBuffer sb = new StringBuffer();
		for (String errorDesc : errorMsg) {
			sb.append(errorDesc);
			sb.append("\n");
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private List getDeptClass(Map<String, Object> dataMap) {
		Long deptId = (Long) dataMap.get(SCHEDULING_DEPT_Id);
		return warehouseClassDao.queryClassDetailByDeptId(deptId);
	}

	public static boolean targetTimeBeforeCurrentTime(Date targetTime, Date currentTime) {
		return targetTime.before(currentTime);
	}

	private void checkEmpCode(HSSFRow row, SchedulingForDispatch scheduling, Set<String> errorMsg, Set<String> schedulingEmployees, Map<String, Object> dataMap) {
		HSSFCell cell = row.getCell(1);
		String empCode = getCellStrValue(cell);
		if (StringUtils.isEmpty(empCode)) {
			errorMsg.add("员工工号不能为空!\n");
			return;
		}
		if (schedulingEmployees.contains(empCode)) {
			errorMsg.add("此员工存在多条记录!\n");
			return;
		}
		Long deptId = (Long) dataMap.get(SCHEDULING_DEPT_Id);
		String deptCode = (String) dataMap.get(SCHEDULING_DEPT_CODE);
		Map<String, Object> emp = warehouseSchedulingDao.getEmployeeByEmpCode(empCode);
		if (null == emp) {
			errorMsg.add("员工工号不存在!\n");
			return;
		}
		String emp_post_type = (String) emp.get("EMP_POST_TYPE");
		if (!WAREHOUSE_EMPLOYEE_TYPE.equals(emp_post_type)) {
			errorMsg.add("此员工不是仓管人员!\n");
			return;
		}
		Date dimission_dt = (Date) emp.get(LEAVE_DATE);
		if (null != dimission_dt && dimission_dt.before(new Date())) {
			errorMsg.add("此员工已经离职!\n");
			return;
		}
		Long emp_dept_id = Long.parseLong(emp.get("DEPT_ID").toString());
		if (!deptId.equals(emp_dept_id)) {
			@SuppressWarnings("rawtypes")
			List dynamicDpetIds = warehouseSchedulingDao.queryEmployeeDynamicDept(empCode);
			if (dynamicDpetIds.isEmpty()) {
				errorMsg.add("此员工不属于网点 '" + deptCode + "' 且机动网点也没有关联此网点!\n");
				return;
			}
		}

		String yearMonth = dataMap.get(SCHEDULING_MONTH).toString();
		try {
			if (targetTimeBeforeCurrentTime(DateUtil.parseDate(yearMonth, DateFormatType.yyyyMM), new Date())) {
				HashMap<String, String> queryParameter = new HashMap<String, String>();
				queryParameter.put("monthId", yearMonth);
				queryParameter.put("empCode", empCode);

				if (warehouseSchedulingDao.existScheduling(queryParameter)) {
					errorMsg.add("该员工" + yearMonth + "月份已有排班，如需修改请在界面操作");
					return;
				}
			}
		} catch (ParseException e) {
			throw new BizException("导入时间转换出错");
		}

		schedulingEmployees.add(empCode);
		scheduling.setEmployeeCode(empCode);
		scheduling.setDepartmentCode(deptCode);
		scheduling.setDepartmentId(deptId);
		scheduling.setEmpPostType(WAREHOUSE_EMPLOYEE_TYPE);
		dataMap.put(empCode, emp);
	}

	private void validMaxRowNum(HSSFSheet sheet, Map<String, Object> dataMap) {
		int rowCount = sheet.getLastRowNum();
		if (rowCount > 1000) {
			throw new BizException("一次最多只能导入 1000 条记录！");
		}
		dataMap.put(SCHEDULING_TOTAL_ROWS, rowCount);
	}

	private void createErrorCell(HSSFRow row, String cellValue) {
		HSSFCell errorCell = row.createCell(SCHEDULING_ERROR_COLUMN_NUM);
		errorCell.setCellValue(cellValue);
		HSSFCell preCell = row.getCell(SCHEDULING_ERROR_COLUMN_NUM - 4);
		if (preCell != null) {
			errorCell.setCellStyle(preCell.getCellStyle());
		}
	}

	private void validMonth(HSSFSheet sheet, Map<String, Object> dataMap) {
		HSSFRow row = sheet.getRow(1);
		if (null == row) {
			throw new BizException("导入的模板有问题！请检查！");
		}
		HSSFCell cell1 = row.getCell(1);
		if (null == cell1) {
			throw new BizException("导入的模板有问题！请检查！");
		}
		String month = getCellStrValue(cell1);
		if (StringUtils.isBlank(month)) {
			throw new BizException("排班月份不能为空！");
		}
		try {
			Date date = DateUtil.parseDate(month, DateFormatType.yyyy_MM);
			if (DateUtil.getMonthSpace(date, new Date()) > 1) {
				throw new BizException("排班月份有误！排班月份只能是当前月、上月或下月！");
			}

			if (DateUtil.isBeOverdue(month)) {
				throw new BizException("已逾期，不能导入该月排班！");
			}

			String year = month.split("-")[0];
			String m = month.split("-")[1];
			if (m.length() < 2) {
				m = STRING_ZERO + m;
			}
			int days = DateUtil.getDaysOfMonth(Integer.parseInt(year), Integer.parseInt(m));
			dataMap.put(SCHEDULING_DAYS_OF_MONTH, days);
			dataMap.put(SCHEDULING_MONTH, year + m);
		} catch (ParseException e) {
			throw new BizException("排班月份格式不正确，正确格式 例如:2014-07！");
		}
	}

	private void validDepartment(HSSFSheet sheet, Map<String, Object> dataMap) {
		HSSFRow row = sheet.getRow(1);
		HSSFCell cell = row.getCell(3);
		String deptCode = getCellStrValue(cell);
		if (StringUtils.isBlank(deptCode)) {
			throw new BizException("网点代码不能为空!");
		}
		Department dept = DepartmentCacheBiz.getDepartmentByCode(deptCode);
		if (dept == null) {
			throw new BizException("网点代码不存在!");
		}
		dataMap.put(SCHEDULING_DEPT_CODE, deptCode);
		dataMap.put(SCHEDULING_DEPT_Id, dept.getId());
	}

	private HSSFWorkbook getHSSFWorkbook(File uploadFile) {
		FileInputStream fin = null;
		HSSFWorkbook readWorkBook = null;
		try {
			fin = new FileInputStream(uploadFile);
			readWorkBook = new HSSFWorkbook(fin);
		} catch (Exception e1) {
			log.error("error", e1);
			throw new BizException("读文件出错！");
		} finally {
			if (fin != null) {
				try {
					fin.close();
				} catch (Exception e) {
					log.error("error", e);
				}
			}

		}
		return readWorkBook;
	}

	private String getCellStrValue(HSSFCell cellobj) {
		if (cellobj == null)
			return null;
		cellobj.setCellType(HSSFCell.CELL_TYPE_STRING);
		return cellobj.toString().trim();
	}

}
