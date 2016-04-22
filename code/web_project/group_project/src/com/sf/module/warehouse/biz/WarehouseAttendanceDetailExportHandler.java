package com.sf.module.warehouse.biz;

import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import com.sf.module.common.util.AbstractExcelExportHandler;

public class WarehouseAttendanceDetailExportHandler extends AbstractExcelExportHandler {
    public int[] getStartRowNumber() {
    	 return new int[]{2};
    }

    public int[] getHiddenRowNumber() {
	    return new int[]{1};
    }

    public String getDownloadName() {
	    return "考勤明细报表";
    }

    public String getTemplateFileName() {
	    return "考勤明细报表.xls";
    }

	public void handleSpecialRow(HSSFSheet sheet, List<Map<String, Object>> list) {
	}
}
