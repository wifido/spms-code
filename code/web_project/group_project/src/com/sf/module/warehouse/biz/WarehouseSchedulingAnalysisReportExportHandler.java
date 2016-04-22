package com.sf.module.warehouse.biz;

import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import com.sf.module.common.util.AbstractExcelExportHandler;

public class WarehouseSchedulingAnalysisReportExportHandler extends AbstractExcelExportHandler  {

    public int[] getStartRowNumber() {
    	 return new int[]{2};
    }

    public int[] getHiddenRowNumber() {
    	 return new int[]{0};
    }

    public String getDownloadName() {
	    return "分点部排班分析报表";
    }

    public String getTemplateFileName() {
	    return "分点部排班分析报表模板.xls";
    }

    public void handleSpecialRow(HSSFSheet sheet, List<Map<String, Object>> list) {
	    
    }

}
