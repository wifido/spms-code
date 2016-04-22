package com.sf.module.warehouse.biz;

import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import com.sf.module.common.util.AbstractExcelExportHandler;

public class WarehouseSchdulingExportHandler extends AbstractExcelExportHandler {
	private static final String MONTH_ID = "monthId";
	private static final String DEPARTMENT_CODE = "departmentCode";
	private static final String NAME_WAREHOUSE_SCHEDULING_EXPORT = "仓管排班数据导出";
	private static final String NAME_WAREHOUSE_SCHEDULING_EXPORT_TEMPLATE = "仓管排班数据导出模板.xls";

	public int[] getStartRowNumber() {
		return new int[]{4};
	}

	public int[] getHiddenRowNumber() {
		return new int[]{2};
	}

	public String getDownloadName() {
		return NAME_WAREHOUSE_SCHEDULING_EXPORT;
	}

	public String getTemplateFileName() {
		return NAME_WAREHOUSE_SCHEDULING_EXPORT_TEMPLATE;
	}

	public void handleSpecialRow(HSSFSheet sheet, List<Map<String, Object>> list) {
		HSSFRow row = sheet.getRow(1);
		Map<String, Object> entity = list.get(0);
		row.getCell(3).setCellValue(String.valueOf(entity.get(DEPARTMENT_CODE)));
		row.getCell(1).setCellValue(String.valueOf(entity.get(MONTH_ID)));
	}
}
