package com.sf.module.dispatch.biz;

import static com.google.common.collect.Lists.newArrayList;
import static com.sf.framework.util.StringUtils.isNotEmpty;
import static com.sf.module.common.domain.Constants.*;
import static com.sf.module.common.util.IOUtil.close;
import static org.apache.commons.lang.StringUtils.isBlank;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.json.JSONArray;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.transaction.annotation.Transactional;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.framework.server.core.context.UserContext;
import com.sf.module.common.util.BeanUtil;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.StringUtil;
import com.sf.module.common.util.Template;
import com.sf.module.dispatch.dao.SchedulingForDispatchDao;
import com.sf.module.dispatch.domain.DutyType;
import com.sf.module.dispatch.domain.SchedulingForDispatch;
import com.sf.module.dispatch.domain.SchedulingWrangler;
import com.sf.module.dispatch.domain.SchedulingWrangler.FailedReasonType;
import com.sf.module.operation.util.CommonUtil;

public class SchedulingForDispatchBiz extends BaseBiz {
	private static final String ANY_VALID_DAY = "-01";
	private static final Pattern PATTERN_FOR_VALID_YEAR_AND_MONTH = Pattern.compile("[0-9]{4}-[0-9]{2}");
	private SchedulingForDispatchDao schedulingForDispatchDao;
	private final int maxPageSize = 60000;
	private final String SLASH = "/";
	private final String RECEIVE_SEND = "2";

	@SuppressWarnings("rawtypes")
	public Map queryAll(HashMap<String, String> queryCriteria) {
		constructCriteria(queryCriteria);

		int totalSize = schedulingForDispatchDao.queryTotalSize(queryCriteria);
		List list = schedulingForDispatchDao.querySchedulingResultList(queryCriteria, null);

		Map result = new HashMap();
		result.put(TOTAL_SIZE, totalSize);
		result.put(ROOT, list);
		return result;
	}

	private void constructCriteria(HashMap<String, String> queryCriteria) {
		int totalPerPage = extractCriteriaAsIntValue(queryCriteria, LIMIT);
		int serialOfPage = extractCriteriaAsIntValue(queryCriteria, START);

		queryCriteria.put(LIMIT, String.valueOf(serialOfPage + totalPerPage));
	}

	private int extractCriteriaAsIntValue(HashMap<String, String> queryCriteria, String criteriaName) {
		return Integer.parseInt(queryCriteria.get(criteriaName));
	}

	public void setSchedulingForDispatchDao(SchedulingForDispatchDao schedulingForDispatchDao) {
		this.schedulingForDispatchDao = schedulingForDispatchDao;
	}

	public List queryOneDayScheduling(HashMap<String, String> queryCriteria) {
		return schedulingForDispatchDao.queryOneDayScheduling(queryCriteria);
	}

	public void saveOrDeleScheduling(List<SchedulingForDispatch> list, SchedulingForDispatch deleteObj) {
		schedulingForDispatchDao.saveOrDeleScheduling(list, deleteObj, this.getCurrentUser().getEmployee().getCode());
	}

	public int returnMaximumLine(HSSFSheet sheet) {
		int totalRow = sheet.getLastRowNum();
		return totalRow;
	}

	public void isBeyondMaximumLine(int totalRow) {
		if (totalRow > 1000) {
			throw new BizException("不能大于1000行");
		}
	}

	public List<SchedulingWrangler> getAllImportExcelData(int totalRow, HSSFSheet sheet, int daysCountOfMonth, String yearAndMonth) {
		List<SchedulingWrangler> schedulingForDispatches = newArrayList();
		for (int rowIndex = 3; rowIndex <= totalRow; rowIndex++) {
			if(sheet.getRow(rowIndex) == null) {
				continue;
			}
			if (Template.isEmptyRow(sheet.getRow(rowIndex), 5)) {
				continue;
			}
			schedulingForDispatches.add(constructBaseScheduling(sheet.getRow(rowIndex), daysCountOfMonth, yearAndMonth));
		}
		if (schedulingForDispatches.size() == 0) {
			throw new BizException("请填写排班数据！");
		}
		return schedulingForDispatches;
	}

	public HashMap<String, Object> importScheduling(File importExcel, String departmentCodes) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HSSFWorkbook workbook = createWorkbook(importExcel);
		HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFCell cellMonth = sheet.getRow(0).getCell(7);

		String yearAndMonth = getYearAndMonth(cellMonth);
		int daysCountOfMonth = CommonUtil.getLastDayOfMonth(CommonUtil.getYmd(cellMonth + ANY_VALID_DAY));
		int totalRow = returnMaximumLine(sheet);
		isBeyondMaximumLine(totalRow);

		List<SchedulingWrangler> schedulingForDispatches = getAllImportExcelData(totalRow, sheet, daysCountOfMonth, yearAndMonth);
		int successNumber = 0;
		String errorMessage = "";
		List<SchedulingForDispatch> errorSchedulingForDispatchList = newArrayList();
		for (SchedulingWrangler schedulingWrangler : schedulingForDispatches) {

			String validaEmployeeCode = schedulingWrangler.getDaysScheduling().get(0).getEmployeeCode();
			String deptCode = schedulingWrangler.getDaysScheduling().get(0).getDepartmentCode();
			// String validaMonthId =
			// schedulingWrangler.getDaysScheduling().get(0).getMonthId().replace("-",
			// "");

			schedulingWrangler.existEmployeeCode(validaEmployeeCode, deptCode, schedulingForDispatchDao);
			// schedulingWrangler.existAlreadyScheduling(validaEmployeeCode,
			// validaMonthId, deptCode, schedulingForDispatchDao);

			schedulingWrangler.setErrorMessageByDeptCode(deptCode, departmentCodes);

			errorMessage = schedulingWrangler.getFailedErrorMessage();
			if (isNotEmpty(errorMessage)) {
				notVerifiedData(getCellValueAsString(cellMonth), errorMessage, errorSchedulingForDispatchList, schedulingWrangler);
			} else {
				successNumber++;
				schedulingForDispatchDao.importScheduling(schedulingWrangler.getDaysScheduling());
			}
		}
		if (errorSchedulingForDispatchList.size() > 0) {
			errorCreatingExcel(resultMap, yearAndMonth, errorMessage, errorSchedulingForDispatchList);
		}
		String msg = getPromptInformation(totalRow, successNumber, errorSchedulingForDispatchList.size());
		resultMap.put("msg", msg);
		return resultMap;
	}

	private void errorCreatingExcel(
	        HashMap<String, Object> resultMap,
	        String yearAndMonth,
	        String failedReason,
	        List<SchedulingForDispatch> errorSchedulingForDispatchList) {
		FileInputStream fileInputStreamForError = null;
		// 生成导入错误的Excel
		try {
			fileInputStreamForError = new FileInputStream(CommonUtil.getReportTemplatePath("一线排班错误数据.xls"));
			HSSFWorkbook errorWorkBook = new HSSFWorkbook(fileInputStreamForError);
			String errorExcelPath = writeErrExcel(errorWorkBook, errorSchedulingForDispatchList, yearAndMonth, failedReason);
			resultMap.put("errorExcelPath", errorExcelPath);
		} catch (Exception e) {
			log.error("error:", e);
			throw new BizException("导出文件出错！");
		} finally {
			close(fileInputStreamForError);
		}
	}

	private String getPromptInformation(int totalRow, int passNum, int failure) {
		String msg;
		if (totalRow - (passNum + 2) > 0) {
			msg = String.format("导入成功%s条导入失败%s条", passNum, failure);
		} else {
			msg = "导入数据成功";
		}
		return msg;
	}

	private void notVerifiedData(
	        String yearAndMonth,
	        String failedReason,
	        List<SchedulingForDispatch> errorSchedulingForDispatchList,
	        SchedulingWrangler schedulingWrangler) {
		SchedulingForDispatch errorSchedulingForDispatch = new SchedulingForDispatch();
		errorSchedulingForDispatch.setErrorMsg(failedReason);
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		int dayNum = 1;
		for (int i = 0; i < schedulingWrangler.getDaysScheduling().size(); i++) {
			SchedulingForDispatch schedulingForDispatch = schedulingWrangler.getDaysScheduling().get(i);

			if (i == 0) {
				errorSchedulingForDispatch.setMonthId(yearAndMonth);
				errorSchedulingForDispatch.setAreaCode(schedulingForDispatch.getAreaCode());
				errorSchedulingForDispatch.setDepartmentCode(schedulingForDispatch.getDepartmentCode());
				errorSchedulingForDispatch.setEmployeeCode(schedulingForDispatch.getEmployeeCode());
				errorSchedulingForDispatch.setUsername(schedulingForDispatch.getUsername());
				errorSchedulingForDispatch.setWorkType(schedulingForDispatch.getWorkType());
			}
			if (isNotEmpty(schedulingForDispatch.getBeginTimes()) && isNotEmpty(schedulingForDispatch.getEndTimes())) {
				map.put(dayNum++, schedulingForDispatch.getBeginTimes() + "-" + schedulingForDispatch.getEndTimes());
			}
			errorSchedulingForDispatch.setMap(map);
		}
		errorSchedulingForDispatchList.add(errorSchedulingForDispatch);
	}

	private boolean isInvalidYearAndMonth(String yearAndMonth) {
		Matcher matcher = PATTERN_FOR_VALID_YEAR_AND_MONTH.matcher(yearAndMonth);
		return !matcher.matches();
	}

	private String getYearAndMonth(HSSFCell cellMonth) {
		String yearAndMonth = getCellValueAsString(cellMonth);
		if (isBlank(yearAndMonth)) {
			throw new BizException("年月不能为空！");
		}

		if (isInvalidYearAndMonth(yearAndMonth)) {
			throw new BizException("年月格式不正确，正确格式 例如:2014-07！");
		}

		if (DateUtil.validConfirmDate(yearAndMonth)) {
			if (DateUtil.isBeOverdue(yearAndMonth))
				throw new BizException("已逾期，不能导入该月排班！");
		} else {
			throw new BizException("最多支持上个月、当前月以及下个月的排班导入！");
		}

		return yearAndMonth.replace("-", "");
	}

	private HSSFWorkbook createWorkbook(File importExcel) {
		HSSFWorkbook workbook;
		try {
			FileInputStream fileInputStream = new FileInputStream(importExcel);
			workbook = new HSSFWorkbook(new POIFSFileSystem(fileInputStream));
			close(fileInputStream);
		} catch (Exception e) {
			log.error("error", e);
			throw new BizException("读文件出错！");
		}
		return workbook;
	}

	public boolean objContainsEmpty(String[] str) {
		for (int i = 0; i < str.length; i++) {
			if (str[i].equals("") && str.length > 1) {
				return true;
			}
		}
		return false;
	}

	private SchedulingWrangler constructBaseScheduling(HSSFRow row, int daysCountOfMonth, String yearAndMonth) {
		String areaCode = getCellValueAsString(row.getCell(0));
		String departmentCode = getCellValueAsString(row.getCell(1));
		String userCode = getCellValueAsString(row.getCell(2));
		String username = getCellValueAsString(row.getCell(3));
		String workType = getCellValueAsString(row.getCell(4));
		//boolean isHourlyWorker = schedulingForDispatchDao.queryEmployeeWorkType(userCode, departmentCode);

		List<SchedulingForDispatch> daysScheduling = newArrayList();

		int dayOfMonth = 0;

		for (int index = 5; index <= (daysCountOfMonth * 2) + 5 - 2; index += 2) {
			dayOfMonth++;
			String beginTime = getCellValueAsString(row.getCell(index));
			String endTime = getCellValueAsString(row.getCell(index + 1));
			if (StringUtil.isBlank(beginTime) && StringUtil.isBlank(endTime))
				continue;

			if (beginTime.contains(SLASH) && endTime.contains(SLASH)) {
				String[] beginTimes = beginTime.split(SLASH);
				String[] endTimes = endTime.split(SLASH);
				if (beginTimes.length != endTimes.length || (objContainsEmpty(beginTimes) || objContainsEmpty(endTimes))) {
					saveTheSschedule(
					        yearAndMonth,
					        areaCode,
					        departmentCode,
					        userCode,
					        username,
					        workType,
					        daysScheduling,
					        dayOfMonth,
					        beginTime,
					        endTime,
					        true);
				} else {
					for (int timeIndex = 0; timeIndex < beginTimes.length; timeIndex++) {

						SchedulingForDispatch schedulingForDispatch = dataAssembly(
						        yearAndMonth,
						        areaCode,
						        departmentCode,
						        userCode,
						        username,
						        workType,
						        dayOfMonth,
						        beginTime,
						        endTime);
						schedulingForDispatch.setBeginTime(beginTimes[timeIndex] + "00");
						schedulingForDispatch.setEndTime(endTimes[timeIndex] + "00");
						if (timeIndex == 0) {
							schedulingForDispatch.setBeginTimes(beginTime);
							schedulingForDispatch.setEndTimes(endTime);
						}
						daysScheduling.add(schedulingForDispatch);
					}
				}

			} else {
				saveTheSschedule(
				        yearAndMonth,
				        areaCode,
				        departmentCode,
				        userCode,
				        username,
				        workType,
				        daysScheduling,
				        dayOfMonth,
				        beginTime,
				        endTime,
				        false);
			}

		}
		return new SchedulingWrangler(daysScheduling);

	}

	private void saveTheSschedule(
	        String yearAndMonth,
	        String areaCode,
	        String departmentCode,
	        String userCode,
	        String username,
	        String workType,
	        List<SchedulingForDispatch> daysScheduling,
	        int dayOfMonth,
	        String beginTime,
	        String endTime,
	        boolean checkFailed) {
		SchedulingForDispatch schedulingForDispatch = dataAssembly(
		        yearAndMonth,
		        areaCode,
		        departmentCode,
		        userCode,
		        username,
		        workType,
		        dayOfMonth,
		        beginTime,
		        endTime);

		if (schedulingForDispatch.isHugh() || schedulingForDispatch.isSlash()) {
			schedulingForDispatch.setBeginTime("");
			schedulingForDispatch.setEndTime("");
		} else {
			schedulingForDispatch.setEndTime(endTime + "00");
			schedulingForDispatch.setBeginTime(beginTime + "00");
		}
		schedulingForDispatch.setCheckFailed(checkFailed);
		schedulingForDispatch.setBeginTimes(beginTime);
		schedulingForDispatch.setEndTimes(endTime);
		daysScheduling.add(schedulingForDispatch);
	}

	private SchedulingForDispatch dataAssembly(
	        String yearAndMonth,
	        String areaCode,
	        String departmentCode,
	        String userCode,
	        String username,
	        String workType,
	        int dayOfMonth,
	        String beginTime,
	        String endTime) {

		SchedulingForDispatch schedulingForDispatch = new SchedulingForDispatch();
		schedulingForDispatch.setEmpPostType(RECEIVE_SEND);
		schedulingForDispatch.setAreaCode(areaCode);
		schedulingForDispatch.setDepartmentCode(departmentCode);
		schedulingForDispatch.setEmployeeCode(userCode);
		schedulingForDispatch.setWorkType(workType);
		schedulingForDispatch.setUsername(username);
		schedulingForDispatch.setDayOfMonth(yearAndMonth + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth));
		schedulingForDispatch.setEndTime(endTime);
		schedulingForDispatch.setBeginTime(beginTime);

		schedulingForDispatch.setMonthId(yearAndMonth);
		schedulingForDispatch.setCreatedEmployeeCode(getEmployeeCode());
		schedulingForDispatch.setModifiedEmployeeCode(getEmployeeCode());
		Date now = new Date();
		schedulingForDispatch.setCreateTime(now);
		schedulingForDispatch.setModifiedTime(now);
		return schedulingForDispatch;
	}

	private String getEmployeeCode() {
		return getCurrentUser().getEmployee().getCode();
	}

	private DutyType getDutyType(String workType) {
		for (DutyType dutyType : DutyType.values()) {
			if (dutyType.name.equals(workType)) {
				return dutyType;
			}
		}

		return DutyType.UNKNOWN;
	}

	private String writeErrExcel(HSSFWorkbook errorWorkBook, List<SchedulingForDispatch> noPassList, String ym, String failedReason) {
		HSSFCellStyle cellStyle = errorWorkBook.createCellStyle();
		HSSFDataFormat format = errorWorkBook.createDataFormat();
		cellStyle.setDataFormat(format.getFormat("@"));
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		String fileName = String.format("导入错误数据%s.xls", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		HSSFSheet sheet = errorWorkBook.getSheetAt(0);
		FileOutputStream errorFos = null;
		if (noPassList != null && noPassList.size() > 0) {
			HSSFRow row0 = sheet.getRow(0);
			row0.getCell(7).setCellValue(ym);

			int rowNum = 2; // 操作行
			try {
				for (SchedulingForDispatch itemInPassList : noPassList) {
					int column = 0;
					HSSFRow row = sheet.createRow(++rowNum);
					// 地区
					HSSFCell cell1 = row.createCell(column++);
					cell1.setCellValue(itemInPassList.getAreaCode() == null ? "" : itemInPassList.getAreaCode());
					cell1.setCellStyle(cellStyle);
					// 网点
					HSSFCell cell2 = row.createCell(column++);
					cell2.setCellValue(itemInPassList.getDepartmentCode() == null ? "" : itemInPassList.getDepartmentCode());
					cell2.setCellStyle(cellStyle);
					// 工号
					HSSFCell cell3 = row.createCell(column++);
					cell3.setCellValue(itemInPassList.getEmployeeCode() == null ? "" : itemInPassList.getEmployeeCode());
					cell3.setCellStyle(cellStyle);
					// 姓名
					HSSFCell cell4 = row.createCell(column++);
					cell4.setCellValue(itemInPassList.getUsername() == null ? "" : itemInPassList.getUsername());
					cell4.setCellStyle(cellStyle);
					HSSFCell cell5 = row.createCell(column++);
					cell5.setCellValue(itemInPassList.getWorkType());
					cell5.setCellStyle(cellStyle);

					for (int index = column; index <= 35; index++) {
						HashMap map = itemInPassList.getMap();
						String daysValue = map.get(index - 4) == null ? "" : map.get(index - 4).toString();
						if (daysValue.indexOf("-") != -1) {
							String[] daysValues = daysValue.split("-");
							for (int daysIndex = 0; daysIndex < daysValues.length; daysIndex++) {
								HSSFCell cells = row.createCell(column++);
								cells.setCellValue(daysValues[daysIndex]);
								cells.setCellStyle(cellStyle);
							}

						} else {
							HSSFCell cell = row.createCell(column++);
							cell.setCellValue("休");
							cell.setCellStyle(cellStyle);

							HSSFCell cells = row.createCell(column++);
							cells.setCellValue("休");
							cells.setCellStyle(cellStyle);
						}
					}
					HSSFCell cell36 = row.createCell(column++);
					cell36.setCellValue(itemInPassList.getErrorMsg() == null ? "" : itemInPassList.getErrorMsg());
					cell36.setCellStyle(cellStyle);
				}
				CommonUtil.setDownloadFileName(fileName);
				errorFos = new FileOutputStream(new File(CommonUtil.getSaveFilePath(SchedulingForDispatch.class)));
				errorWorkBook.write(errorFos);
			} catch (Exception e) {
				log.error("error:", e);
				throw new BizException("操作失败！");
			} finally {
				if (errorFos != null) {
					try {
						errorFos.close();
					} catch (Exception e1) {
						log.error("err:", e1);
					}
				}
			}
		}
		return CommonUtil.getReturnPageFileName();
	}

	private String getCellValueAsString(HSSFCell cell) {
		if (null == cell) {
			return "";
		}
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		return cell.toString().trim();
	}

	public String getExportExcel(HashMap<String, String> queryCriteria) {
		int sheetCt = 0;
		String monthId = queryCriteria.get("MONTH_ID");
		String yearAndMonth = getYearAndMonth(monthId);
		int personnelCount = schedulingForDispatchDao.getExportExcelCount(queryCriteria);
		
		if(isHeadquarters(queryCriteria)){
			throw new BizException("总部数据量太大,不支持导出！");
		}

		if (personnelCount == 0) {
			throw new BizException(String.format("该网点%s在%s月没有未排班人员信息！", queryCriteria.get("DEPARTMENT_CODE"), yearAndMonth));
		}
		
		if (personnelCount <= maxPageSize) {
			sheetCt = 1;
		} else {
			if (personnelCount % maxPageSize == 0) {
				sheetCt = personnelCount / maxPageSize;
			} else {
				sheetCt = personnelCount / maxPageSize + 1;
			}
		}

		HSSFWorkbook workbook = null;
		String fileName = String.format("一线排班导出_%s.xls", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		FileInputStream fis = null;
		FileOutputStream fos = null;

		try {
			fis = new FileInputStream(CommonUtil.getReportTemplatePath("一线排班模板.xls"));
			workbook = new HSSFWorkbook(fis);
			HSSFCellStyle cellStyle = workbook.createCellStyle();

			HSSFDataFormat format = workbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("@"));
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			HSSFSheet[] sheetArray = new HSSFSheet[sheetCt];
			HSSFSheet sh = workbook.getSheetAt(0);
			HSSFRow r = sh.getRow(0);
			r.getCell(3).setCellValue("");
			r.getCell(7).setCellValue(yearAndMonth);
			sheetArray[0] = sh;

			for (int k = 1; k < sheetArray.length; k++) {
				sheetArray[k] = workbook.cloneSheet(0);
			}

			for (int i = 0; i < sheetCt; i++) {
				HSSFSheet sheet = sheetArray[i];
				int rowNum = 2; // 操作行
				queryCriteria.put(START, String.valueOf(i * maxPageSize));
				queryCriteria.put(LIMIT, String.valueOf((i + 1) * maxPageSize));
				List<SchedulingForDispatch> resultList = schedulingForDispatchDao.getExportExcelList(queryCriteria);
				for (SchedulingForDispatch mgt: resultList) {
					int column = 0;
					HSSFRow row = sheet.createRow(++rowNum);

					// 地区
					HSSFCell cell1 = row.createCell(column++);
					cell1.setCellValue(mgt.getAreaCode() == null ? "" : mgt.getAreaCode());
					cell1.setCellStyle(cellStyle);

					// 网点
					HSSFCell cell2 = row.createCell(column++);
					cell2.setCellValue(mgt.getDepartmentCode() == null ? "" : mgt.getDepartmentCode());
					cell2.setCellStyle(cellStyle);

					// 工号
					HSSFCell cell3 = row.createCell(column++);
					cell3.setCellValue(mgt.getEmployeeCode() == null ? "" : mgt.getEmployeeCode());
					cell3.setCellStyle(cellStyle);

					// 姓名
					HSSFCell cell4 = row.createCell(column++);
					cell4.setCellValue(mgt.getUsername() == null ? "" : mgt.getUsername());
					cell4.setCellStyle(cellStyle);

					HSSFCell cell5 = row.createCell(column++);
					cell5.setCellValue(mgt.getWorkType() == null ? "" : mgt.getWorkType());
					cell5.setCellStyle(cellStyle);
					for (int index = column; index <= 35; index++) {
						HSSFCell cell = row.createCell(column++);
						cell.setCellValue("休");
						cell.setCellStyle(cellStyle);

						HSSFCell cells = row.createCell(column++);
						cells.setCellValue("休");
						cells.setCellStyle(cellStyle);
					}
				}
				
				
			}
			CommonUtil.setDownloadFileName(fileName);
			fos = new FileOutputStream(new File(CommonUtil.getSaveFilePath(SchedulingForDispatch.class)));
			workbook.write(fos);
		} catch (Exception e) {
			log.error("throw Exception:", e);
			throw new BizException("导出失败!");
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (Exception e) {
				log.error("throw Exception:", e);
			}
			try {
				if (fos != null)
					fos.close();
			} catch (Exception e) {
				log.error("throw Exception:", e);
			}
		}
		return CommonUtil.getReturnPageFileName();
	}

	private boolean isHeadquarters(HashMap<String, String> queryCriteria) {
	    return "001".equals(queryCriteria.get("DEPARTMENT_CODE"));
    }

	private String getYearAndMonth(String monthId) {
		String yesr = monthId.substring(0, 4);
		String month = monthId.substring(4, 6);
		String yearAndMonth = yesr + "-" + month;
		return yearAndMonth;
	}

	public String getDutyTypeName(int workType) {
		for (DutyType dutyType : DutyType.values()) {
			if (workType == dutyType.ordinal()) {
				return dutyType.name;
			}
		}
		return "";
	}

	private int promptScheduling(String failedReason) {
		for (FailedReasonType failedReasonType : SchedulingWrangler.FailedReasonType.values()) {
			if (failedReasonType.reason.equals(failedReason)) {
				return failedReasonType.ordinal();
			}
		}
		return 0;
	}

	public void beConfirmedscheduling(List<SchedulingForDispatch> beConfirmedSchedulingList, SchedulingWrangler schedulingWrangler) {
		Map<String, SchedulingForDispatch> schedulingResultMap = new HashMap<String, SchedulingForDispatch>();
		for (SchedulingForDispatch schedulingForDispatch : schedulingWrangler.getDaysScheduling()) {
			String mothId = schedulingForDispatch.getMonthId();
			String employeeCode = schedulingForDispatch.getEmployeeCode();
			String dayOfMonth = getDayOfMonth(schedulingForDispatch.getDayOfMonth());
			String key = mothId + "," + employeeCode;
			String workType = schedulingForDispatch.getWorkType();
			String workTime = "null";
			if (isNotEmpty(schedulingForDispatch.getBeginTime()) && isNotEmpty(schedulingForDispatch.getEndTime())) {
				workTime = schedulingForDispatch.getBeginTime().substring(0, 4) + ":" + schedulingForDispatch.getEndTime().substring(0, 4);
			}

			if (schedulingResultMap.get(key) != null) {
				SchedulingForDispatch scheduling = schedulingResultMap.get(key);
				scheduling.updateWorkTimeWithExpectDay(dayOfMonth, workTime, null);
			} else {
				SchedulingForDispatch scheduling = new SchedulingForDispatch();
				scheduling.setMonthId(mothId);
				scheduling.setEmployeeCode(employeeCode);
				scheduling.setEmployeeName(schedulingForDispatch.getUsername());
				scheduling.setWorkType(schedulingForDispatch.getWorkType());
				scheduling.setDepartmentCode(schedulingForDispatch.getDepartmentCode());
				scheduling.setCreatedEmployeeCode(getEmployeeCode());
				scheduling.setModifiedEmployeeCode(getEmployeeCode());
				scheduling.updateWorkTimeWithExpectDay(dayOfMonth, workTime, null);
				scheduling.setWorkType(workType);
				schedulingResultMap.put(key, scheduling);
			}
		}

		for (Map.Entry<String, SchedulingForDispatch> temp : schedulingResultMap.entrySet()) {
			beConfirmedSchedulingList.add(temp.getValue());
		}

	}

	private String getDayOfMonth(String datOfMonth) {
		return datOfMonth.substring(datOfMonth.length() - 2, datOfMonth.length());
	}

	private List<SchedulingForDispatch> jsonToList(String jsonstr) {
		if ("".equals(jsonstr) || 0 == jsonstr.trim().length()) {
			return null;
		}
		JSONArray array = JSONArray.fromObject(jsonstr);
		if (array == null || array.isEmpty()) {
			return null;
		}
		@SuppressWarnings("unchecked")
		List<SchedulingForDispatch> list = (List<SchedulingForDispatch>) JSONArray.toCollection(array, SchedulingForDispatch.class);
		return list;
	}

	private SchedulingForDispatch constructionScheduling(SchedulingForDispatch scheduling, String monthId, int days) {
		SchedulingForDispatch schedulingForDispatch = new SchedulingForDispatch();
		schedulingForDispatch.setEmpPostType(RECEIVE_SEND);
		schedulingForDispatch.setAreaCode(scheduling.getAreaCode());
		schedulingForDispatch.setDepartmentCode(scheduling.getDepartmentCode());
		schedulingForDispatch.setEmployeeCode(scheduling.getEmployeeCode());
		schedulingForDispatch.setDutyType(getDutyType(scheduling.getWorkType()));
		schedulingForDispatch.setUsername(scheduling.getEmployeeName());

		schedulingForDispatch.setDayOfMonth(monthId + (days < 10 ? "0" + days : days));
		schedulingForDispatch.setMonthId(monthId);
		schedulingForDispatch.setCreatedEmployeeCode(getEmployeeCode());
		schedulingForDispatch.setModifiedEmployeeCode(getEmployeeCode());
		Date now = new Date();
		schedulingForDispatch.setCreateTime(now);
		schedulingForDispatch.setModifiedTime(now);
		return schedulingForDispatch;
	}

	private void timeConversion(SchedulingForDispatch schedulingForDispatch, String[] workTime) {
		for (int commutingTime = 0; commutingTime < workTime.length; commutingTime++) {
			schedulingForDispatch.setBeginTime(workTime[commutingTime] + "00");
			commutingTime++;
			schedulingForDispatch.setEndTime(workTime[commutingTime] + "00");
		}
	}

	private void timeConversion(SchedulingForDispatch schedulingForDispatch, String dayOldValue) {
		if (dayOldValue.indexOf(":") != -1) {
			String workTime[] = dayOldValue.split(":");
			timeConversion(schedulingForDispatch, workTime);
		}
	}

	public boolean deleteScheulDaily(HashMap<String, String> paramsMap) {
		String[] empCodes = paramsMap.get(KEY_EMP_CODES).toString().split(COMMA);
		Integer monthId = Integer.parseInt(paramsMap.get("MONTH_ID").toString());
		return schedulingForDispatchDao.deleteScheulDaily(empCodes, monthId);
	}

	public Map queryPermissions(HashMap<String, String> paramsMap,String userId){
		String deptId = paramsMap.get("DEPARTMENT_CODE").toString();
		int totalSize = schedulingForDispatchDao.queryDeptPermissions(deptId,userId);
		Map result = new HashMap();		
		result.put(TOTAL_SIZE, totalSize);
		
		return result;
	}

	public boolean queryIsBeOverdue(HashMap<String, String> paramsMap) {
		String monthId = paramsMap.get("MONTH_ID").toString();
		String yarsAndMonth = monthId.substring(0, 4) + "-" + monthId.substring(4, 6);
		if (DateUtil.validConfirmDate(yarsAndMonth)) {
			return DateUtil.isBeOverdue(yarsAndMonth);
		}
		return true;
	}

	public String exportDispatchSchedulingDetail(HashMap<String, String> queryCriteria) throws Exception {
		DispatchSchedulingExportHandler handler = new DispatchSchedulingExportHandler();

		handler.setSchedulingMonth(queryCriteria.get("MONTH_ID"));
		queryCriteria.put(START, String.valueOf(0));
		queryCriteria.put(LIMIT, String.valueOf(maxPageSize));

		List<SchedulingForDispatch> scheduledList = schedulingForDispatchDao.querySchedulingResultList(queryCriteria, "\r\n");
		scheduledList.addAll(schedulingForDispatchDao.getExportExcelList(queryCriteria));

		if (scheduledList.size() == 0) {
			throw new BizException("没有可导出的排班数据！");
		}

		handler.handle(BeanUtil.transListBeanToMap(scheduledList));
		return handler.getDownloadPath();
	}
}
