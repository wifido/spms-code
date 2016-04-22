package com.sf.module.driver.biz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import com.sf.module.common.util.AbstractExcelExportHandler;

public class DriverSchedulingExportHandler extends AbstractExcelExportHandler {
	private static String DRIVER_SCHEDULING_EXPORT_NAME = "实际排班表";
	private static String DRIVER_SCHEDULING_EXPORT_TEMPLATE_NAME = "实际排班表.xls";
	private static String queryDepartmentCode;
	private String schedulingMonth;

	public String getDownloadName() {
		return DRIVER_SCHEDULING_EXPORT_NAME;
	}

	public String getTemplateFileName() {
		return DRIVER_SCHEDULING_EXPORT_TEMPLATE_NAME;
	}

	public String getSchedulingMonth() {
    	return schedulingMonth;
    }

	public void setSchedulingMonth(String schedulingMonth) {
    	this.schedulingMonth = schedulingMonth;
    }

	public void handleSpecialRow(HSSFSheet sheet, List<Map<String, Object>> list) {
        HSSFRow row = sheet.getRow(0);
        row.getCell(1).setCellValue(getQueryDepartmentCode());
        row.getCell(3).setCellValue(schedulingMonth);
        row.getCell(5).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
	}
	
	public static String getQueryDepartmentCode() {
		return queryDepartmentCode;
	}

    public static void setQueryDepartmentCode(String queryDepartmentCode) {
        DriverSchedulingExportHandler.queryDepartmentCode = queryDepartmentCode;
    }

    public int[] getStartRowNumber() {
        return new int[]{3, 3};
    }

    public int[] getHiddenRowNumber() {
        return new int[]{2, 2};
    }
}
