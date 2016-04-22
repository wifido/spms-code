package com.sf.module.warehouse.biz;

import static com.sf.module.common.util.StringUtil.isNotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import com.sf.module.common.util.AbstractExcelExportHandler;

public class WarehouseAttendanceExportHandler extends AbstractExcelExportHandler {
	private static final String DEPT_CODE = "deptCode";
	private static final String EMP_NAME = "EMP_NAME";
	private static final String EMP_CODE = "EMP_CODE";
	private String schedulingMonth;
	private String department;
	private HashMap<String, String> queryParameter;

	public int[] getStartRowNumber() {
		return new int[] { 3 };
	}

	public int[] getHiddenRowNumber() {
		return new int[] { 1 };
	}

	public String getDownloadName() {
		return "仓管考勤汇总报表";
	}

	public String getTemplateFileName() {
		return "仓管考勤汇总报表导出模板.xls";
	}

	public String getSchedulingMonth() {
		return schedulingMonth;
	}

	public void setSchedulingMonth(String schedulingMonth) {
		this.schedulingMonth = schedulingMonth;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public HashMap<String, String> getQueryParameter() {
		return queryParameter;
	}

	public void setQueryParameter(HashMap<String, String> queryParameter) {
		this.queryParameter = queryParameter;
	}

	public void handleSpecialRow(HSSFSheet sheet, List<Map<String, Object>> list) {
		if (list.isEmpty())
			return;
		String employeeName = (String) queryParameter.get(EMP_NAME);
		String employeeCode = (String) queryParameter.get(EMP_CODE);
		if (isNotBlank(employeeName) || isNotBlank(employeeCode)) {
			department = (String) list.get(0).get(DEPT_CODE);
		}
		HSSFRow row = sheet.getRow(0);
		row.getCell(1).setCellValue(schedulingMonth);
		row.getCell(3).setCellValue(department);
	}
}
