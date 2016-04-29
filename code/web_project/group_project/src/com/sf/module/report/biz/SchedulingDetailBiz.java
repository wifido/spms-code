package com.sf.module.report.biz;

import static com.sf.module.common.domain.Constants.TOTAL_SIZE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.util.StringUtil;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.operation.util.CommonUtil;
import com.sf.module.report.dao.SchedulingDetailDao;

public class SchedulingDetailBiz extends BaseBiz {
	private static final String SHEDULE_CODE = "SHEDULE_CODE";
	private static final String STRING_BLANK = "";
	private static final String STRING_COMMA = ",";
	private static final int NUM_DEPARTMENT_CODE = 20;
	private static final String FILENAME_EXPORT_SCHEDULING_DETAIL = "排班明细报表";
	private static final String FILENAME_EXPORT_USER_PERMISON = "权限钥匙报表";
	private static final String ACCREDIT_ENABLE_TM = "ACCREDIT_ENABLE_TM";
	private static final String ACCREDIT_DEPT_CODE = "ACCREDIT_DEPT_CODE";
	private static final String ACCREDIT_ROLE_NAME = "ACCREDIT_ROLE_NAME";
	private static final String USER_DEPT_CODE = "USER_DEPT_CODE";
	private static final String USER_ROLE_NAME = "USER_ROLE_NAME";
	private static final String STATUS = "STATUS";
	private static final String EMP_EMAIL = "EMP_EMAIL";
	private static final String EMP_DUTY_NAME = "EMP_DUTY_NAME";
	private static final String AREA_CODE = "AREA_CODE";
	private static final String TEMPLATE_USER_PERMISSION_REPORT = "权限钥匙报表模板.xls";
	private static final String TEMPLATE_SCHEDULING_DETAIL = "排班报表模板.xls";
	private static final String TOTAL_SIZE = "totalSize";
	private static final String ROOT = "root";
	private static final String LIMIT = "limit";
	private static final String START = "start";
	private static final String TIMELENGTH = "TIMELENGTH";
	private static final String DIFFICULTY_MODIFY_VALUE = "DIFFICULTY_MODIFY_VALUE";
	private static final String PROCESS_CODE = "PROCESS_CODE";
	private static final String DIMISSION_DT = "DIMISSION_DT";
	private static final String WORK_TYPE = "WORK_TYPE";
	private static final String EMP_NAME = "EMP_NAME";
	private static final String EMP_CODE = "EMP_CODE";
	private static final String GROUP_CODE = "GROUP_CODE";
	private static final String DEPT_CODE = "DEPT_CODE";
	private static final String SHEDULE_DT = "SHEDULE_DT";
	private static final String ATTENDANCE_HOURS = "ATTENDANCE_HOURS";
	private static final String OVERTIME_HOURS = "OVERTIME_HOURS";
	private static final String WORK_TIME = "WORK_TIME";
	private static final String CLASS_CODE = "CLASS_CODE";
	private static final String LEAVE_TYPE = "LEAVE_TYPE";
	private SchedulingDetailDao schedulingDetailDao;
	private HashMap<String, Object> dataMap;
	private final int MAX_PAGE_SIZE = 60000;

	public HashMap<String, Object> queryDetailReport(
			HashMap<String, String> queryCriteria) {
		dataMap = new HashMap<String, Object>();
		setQueryParameters(queryCriteria);
		int totalSize = schedulingDetailDao
				.queryDetailReportCount(queryCriteria);
		List resultList = schedulingDetailDao.queryDetailReport(queryCriteria);
		dataMap.put(ROOT, resultList);
		dataMap.put(TOTAL_SIZE, totalSize);
		return dataMap;
	}

	public String getExcel(HashMap<String, String> queryCriteria) {
		int totalSize = schedulingDetailDao
				.queryDetailReportCount(queryCriteria);
		if (totalSize < 1) {
			throw new BizException("导出失败！没有符合条件的数据！");
		}

		int sheetCount = getSheetCount(totalSize);

		HSSFWorkbook workbook = getTemplateWrokBook(TEMPLATE_SCHEDULING_DETAIL);
		HSSFCellStyle cellStyle = setCellStyle(workbook);
		HSSFSheet[] sheetArray = new HSSFSheet[sheetCount];
		for (int i = 0; i < sheetArray.length; i++) {
			if (i == 0) {
				sheetArray[i] = workbook.getSheetAt(0);
			} else {
				sheetArray[i] = workbook.cloneSheet(0);// clone sheet
			}
		}

		for (int i = 0; i < sheetCount; i++) {
			HSSFSheet sheet = sheetArray[i];
			queryCriteria.put(START, (i * MAX_PAGE_SIZE) + STRING_BLANK);
			queryCriteria.put(LIMIT, ((i + 1) * MAX_PAGE_SIZE) + STRING_BLANK);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> list = (List<Map<String, Object>>) schedulingDetailDao
					.queryDetailReport(queryCriteria);
			createSheetRows(cellStyle, sheet, list);
		}

		CommonUtil
				.setDownloadFileName(getDowloadFileName(FILENAME_EXPORT_SCHEDULING_DETAIL));
		writeToWorkBook(workbook);
		return CommonUtil.getReturnPageFileName();
	}

	private HSSFWorkbook getTemplateWrokBook(String templateFileName) {
		HSSFWorkbook workbook = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(
					CommonUtil.getReportTemplatePath(templateFileName));
			workbook = new HSSFWorkbook(fis);
			return workbook;
		} catch (IOException e) {
			throw new BizException("导出失败！读取 排班报表模板 文件出错！ ");
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					throw new BizException("导出失败！读取 排班报表模板 文件出错！ ");
				}
			}
		}
	}

	private void writeToWorkBook(HSSFWorkbook workbook) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(
					CommonUtil.getSaveFilePath(SchedulingDetailBiz.class)));
			workbook.write(fos);
		} catch (Exception e) {
			throw new BizException("导出失败！写入到excle文件出错");
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					throw new BizException("导出失败！写入到excle文件出错");
				}
			}
		}
	}

	private void createSheetRows(HSSFCellStyle cellStyle, HSSFSheet sheet,
			List<Map<String, Object>> list) {
		int rowNum = 1; // 操作行
		for (Map<String, Object> schedulingDetail : list) {
			HSSFRow row = sheet.createRow(++rowNum);
			// 日期
			createCell(cellStyle, schedulingDetail.get(SHEDULE_DT), 0, row);
			// 中转场代码
			createCell(cellStyle, schedulingDetail.get(DEPT_CODE), 1, row);
			// 小组代码
			createCell(cellStyle, schedulingDetail.get(GROUP_CODE), 2, row);
			// 工号
			createCell(cellStyle, schedulingDetail.get(EMP_CODE), 3, row);
			// 姓名
			createCell(cellStyle, schedulingDetail.get(EMP_NAME), 4, row);
			//班别代码
			createCell(cellStyle, schedulingDetail.get(CLASS_CODE), 5, row);
			// 工序代码
			createCell(cellStyle, schedulingDetail.get(PROCESS_CODE), 6, row);
			// 工序含金量
			createCell(cellStyle,schedulingDetail.get(DIFFICULTY_MODIFY_VALUE), 7, row);
			// 排班时长
			createCell(cellStyle, schedulingDetail.get(TIMELENGTH), 8, row);
			// 考勤小时数
			createCell(cellStyle, schedulingDetail.get(ATTENDANCE_HOURS), 9, row);
			// 加班时长
			createCell(cellStyle, schedulingDetail.get(OVERTIME_HOURS), 10, row);
			// 考勤系数
			createCell(cellStyle, schedulingDetail.get(WORK_TIME), 11, row);
			//请假类型
			createCell(cellStyle, schedulingDetail.get(LEAVE_TYPE), 12, row);

		}
	}

	private void createCell(HSSFCellStyle cellStyle, Object cellValue,
			int column, HSSFRow row) {
		HSSFCell cell = row.createCell(column);
		cell.setCellValue(cellValue == null ? STRING_BLANK : cellValue.toString());
		cell.setCellStyle(cellStyle);
	}

	private HSSFCellStyle setCellStyle(HSSFWorkbook workbook) {
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		return cellStyle;
	}

	private String getDowloadFileName(String fileName) {
		return fileName
				+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
				+ ".xls";
	}

	private int getSheetCount(int totalSize) {
		if (totalSize <= MAX_PAGE_SIZE) {
			return 1;
		}
		return mod(totalSize) ? getPageCount(totalSize)
				: (getPageCount(totalSize) + 1);
	}

	private boolean mod(int totalSize) {
		return totalSize % MAX_PAGE_SIZE == 0;
	}

	private int getPageCount(int totalSize) {
		return totalSize / MAX_PAGE_SIZE;
	}

	private void setQueryParameters(HashMap<String, String> queryCriteria) {
		Integer limit = extractCriteriaAsIntValue(queryCriteria.get(START))
				+ extractCriteriaAsIntValue(queryCriteria.get(LIMIT));
		queryCriteria.put(LIMIT, limit + STRING_BLANK);
	}

	public void setSchedulingDetailDao(SchedulingDetailDao schedulingDetailDao) {
		this.schedulingDetailDao = schedulingDetailDao;
	}

	private int extractCriteriaAsIntValue(String criteriaName) {
		return Integer.parseInt(criteriaName);
	}

	public String exportUserPermission(HashMap<String, String> queryCriteria) {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>) schedulingDetailDao
				.queryUserPermissionReport(queryCriteria);
		if (list.isEmpty()) {
			throw new BizException("导出失败！没有符合条件的数据！");
		}
		HSSFWorkbook workbook = getTemplateWrokBook(TEMPLATE_USER_PERMISSION_REPORT);
		HSSFCellStyle cellStyle = setCellStyle(workbook);
		HSSFSheet sheet = workbook.getSheetAt(0);
		writeQueryDataToTemplate(cellStyle, sheet, list);
		CommonUtil
				.setDownloadFileName(getDowloadFileName(FILENAME_EXPORT_USER_PERMISON));
		writeToWorkBook(workbook);
		return CommonUtil.getReturnPageFileName();
	}

	private void writeQueryDataToTemplate(HSSFCellStyle cellStyle,
			HSSFSheet sheet, List<Map<String, Object>> list) {
		int rowNum = 1;
		for (Map<String, Object> userPermission : list) {
			HSSFRow row = sheet.createRow(rowNum++);
			int cloumn = 0;
			createCell(cellStyle, userPermission.get(AREA_CODE), cloumn++, row);
			createCell(cellStyle, userPermission.get(DEPT_CODE), cloumn++, row);
			createCell(cellStyle, userPermission.get(EMP_CODE), cloumn++, row);
			createCell(cellStyle, userPermission.get(EMP_NAME), cloumn++, row);
			createCell(cellStyle, userPermission.get(EMP_DUTY_NAME), cloumn++,
					row);
			createCell(cellStyle, userPermission.get(EMP_EMAIL), cloumn++, row);
			createCell(cellStyle, userPermission.get(STATUS), cloumn++, row);
			createCell(cellStyle, userPermission.get(USER_ROLE_NAME), cloumn++,
					row);
			createCell(
					cellStyle,
					handleContainManyDepartmentCase(clobToString((Clob) userPermission
							.get(USER_DEPT_CODE))), cloumn++, row);
			createCell(cellStyle, userPermission.get(ACCREDIT_ROLE_NAME),
					cloumn++, row);
			createCell(
					cellStyle,
					handleContainManyDepartmentCase(clobToString((Clob) userPermission
							.get(ACCREDIT_DEPT_CODE))), cloumn++, row);
			createCell(cellStyle, userPermission.get(ACCREDIT_ENABLE_TM),
					cloumn++, row);
		}
	}

	private String handleContainManyDepartmentCase(String userDeptCode) {
		if (StringUtil.isBlank(userDeptCode)) {
			return STRING_BLANK;
		}
		String[] deptCodes = userDeptCode.split(STRING_COMMA);
		if (departmentCodeLowThanTwenty(deptCodes)) {
			return userDeptCode;
		}
		Set<String> areaCodeList = new HashSet<String>();
		Set<String> hqCodeList = new HashSet<String>();
		for (String deptCode : deptCodes) {
			String areaCode = DepartmentCacheBiz.getDepartmentByCode(deptCode)
					.getAreaDeptCode();
			String hqCode = DepartmentCacheBiz.getDepartmentByCode(deptCode)
					.getHqDeptCode();
			areaCodeList.add(areaCode);
			hqCodeList.add(hqCode);
		}
		if (areaCodeLowthanTwenty(areaCodeList)) {
			Object[] areacodeArray = areaCodeList.toArray();
			Arrays.sort(areacodeArray);
			return Arrays.toString(areacodeArray);
		}
		Object[] hqcodeArray = hqCodeList.toArray();
		Arrays.sort(hqcodeArray);
		return Arrays.toString(hqcodeArray);
	}

	private boolean areaCodeLowthanTwenty(Set<String> areaCodeList) {
		return areaCodeList.size() < NUM_DEPARTMENT_CODE;
	}

	private boolean departmentCodeLowThanTwenty(String[] deptCodes) {
		return deptCodes.length < NUM_DEPARTMENT_CODE;
	}
	
	public Map queryPermissions(HashMap<String, String> paramsMap,String userId){
		String deptId = paramsMap.get("DEPARTMENT_CODE").toString().toUpperCase();
		int totalSize = schedulingDetailDao.queryDeptPermissions(deptId,userId);
		Map result = new HashMap();		
		result.put(TOTAL_SIZE, totalSize);
		
		return result;
	}

	private String clobToString(Clob clob) {
		if (clob == null)
			return null;
		Reader is = null;
		BufferedReader br = null;
		try {
			is = clob.getCharacterStream();// 得到流
			br = new BufferedReader(is);
			String line = br.readLine();
			StringBuilder stringBuilder = new StringBuilder();
			while (line != null) {
				stringBuilder.append(line);
				line = br.readLine();
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			log.info("clob to String error!" + e);
			throw new BizException("clob to String error!");
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.info("clob to String error!" + e);
					throw new BizException("clob to String error!");
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					log.info("clob to String error!" + e);
					throw new BizException("clob to String error!");
				}
			}
		}
	}
}
