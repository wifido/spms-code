package com.sf.module.warehouse.biz;

import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import com.sf.module.common.util.AbstractExcelExportHandler;

public class NoSchedulingEmployeeExportHandler extends AbstractExcelExportHandler {
	private String schedulingMonth;
	private String department;

    public int[] getStartRowNumber() {
    	 return new int[]{4};
    }

    public int[] getHiddenRowNumber() {
	    return new int[]{2};
    }

    public String getDownloadName() {
	    return "仓管未排班人员";
    }

    public String getTemplateFileName() {
	    return "仓管导出未排班人员模板.xls";
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

	public void handleSpecialRow(HSSFSheet sheet, List<Map<String, Object>> list) {
		HSSFRow row = sheet.getRow(1);
		row.getCell(1).setCellValue(schedulingMonth);
		row.getCell(3).setCellValue(department);
    }
	
}
