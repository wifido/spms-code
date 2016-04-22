package com.sf.module.warehouse.biz;

import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import com.sf.module.common.util.AbstractExcelExportHandler;

public class WarehouseCoincidenceRateExportHandler extends AbstractExcelExportHandler  {

    public int[] getStartRowNumber() {
    	 return new int[]{3};
    }

    public int[] getHiddenRowNumber() {
    	 return new int[]{1};
    }

    public String getDownloadName() {
	    return "仓管排班吻合率报表";
    }

    public String getTemplateFileName() {
	    return "仓管排班吻合率导出模板.xls";
    }

    public void handleSpecialRow(HSSFSheet sheet, List<Map<String, Object>> list) {
	    
    }

}
