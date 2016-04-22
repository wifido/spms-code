package com.sf.module.dispatch.biz;

import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import com.sf.module.common.util.AbstractExcelExportHandler;

public class DispatchSchedulingExportHandler extends AbstractExcelExportHandler {
	private String schedulingMonth;

	public String getSchedulingMonth() {
		return schedulingMonth;
	}

	public void setSchedulingMonth(String schedulingMonth) {
		this.schedulingMonth = schedulingMonth;
	}

	public int[] getStartRowNumber() {
		return new int[] { 3 };
	}

	public int[] getHiddenRowNumber() {
		return new int[] { 0 };
	}

	public String getDownloadName() {
		return "一线已排班明细报表";
	}

	public String getTemplateFileName() {
		return "一线已排班明细报表导出模板.xls";
	}

	public void handleSpecialRow(HSSFSheet sheet, List<Map<String, Object>> list) {
		sheet.getRow(1).getCell(6).setCellValue(schedulingMonth);
	}

}
