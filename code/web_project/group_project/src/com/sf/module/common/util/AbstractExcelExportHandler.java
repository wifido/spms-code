package com.sf.module.common.util;

import static com.sf.module.common.util.TemplateHelper.createCell;
import static com.sf.module.common.util.TemplateHelper.readExcel;
import static com.sf.module.common.util.TemplateHelper.removeEmptyRow;
import static com.sf.module.common.util.TemplateHelper.templateName;
import static com.sf.module.common.util.TemplateHelper.wirteToExcel;
import static com.sf.module.operation.util.CommonUtil.getReportTemplatePath;
import static com.sf.module.operation.util.CommonUtil.getReturnPageFileName;
import static com.sf.module.operation.util.CommonUtil.setDownloadFileName;
import static com.sf.module.common.util.TemplateHelper.getCellValueAsString;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.sf.module.operation.util.CommonUtil;

public abstract class AbstractExcelExportHandler {
	private Map<String, Integer> headerMap = new HashMap<String, Integer>();
	private boolean removeHiddenRow = true;

	public String getDownloadPath() {
		return getReturnPageFileName();
	}

	public void handle(List<Map<String, Object>>... list) throws Exception {
		File templateFile = getTemplateFile();
		HSSFWorkbook workbook = readExcel(templateFile);
		for (int i = 0; i < list.length; i++) {
			List<Map<String, Object>> datas = list[i];
			HSSFSheet sheet = workbook.getSheetAt(i);
			
			setHiddenHeader(sheet, i);
			
			writeData(datas, sheet, i);
			
			handleSpecialRow(sheet, datas);
			if (removeHiddenRow) {
				sheet.removeRow(sheet.getRow(getHiddenRowNumber()[i]));
				removeEmptyRow(sheet);
			}
		}
		setDownloadFileName(templateName(getDownloadName()));
		wirteToExcel(workbook, CommonUtil.getSaveFilePath(AbstractExcelExportHandler.class));
	}

	private void writeData(List<Map<String, Object>> list, HSSFSheet sheet, int index) {
		int startRowNumber = getStartRowNumber()[index];
		for (int i = 0; i < list.size(); i++) {
			HSSFRow row = sheet.createRow(i + startRowNumber);
			Map<String, Object> entity = list.get(i);
			for (Map.Entry<String, Object> entry : entity.entrySet()) {
				Integer cellIndex = headerMap.get(entry.getKey());
				if (null == cellIndex || null == entry.getValue())
					continue;
                createCell(row, cellIndex, String.valueOf(entry.getValue()));
			}
		}
	}

	private void setHiddenHeader(HSSFSheet sheet, int index) {
		HSSFRow hiddenRow = sheet.getRow(getHiddenRowNumber()[index]);
		for (int i = 0; i < hiddenRow.getLastCellNum(); i++) {
			headerMap.put(getCellValueAsString(hiddenRow.getCell(i)), i);
		}
	}

	private File getTemplateFile() {
        return new File(getReportTemplatePath(getTemplateFileName()));
	}

	public boolean isRemoveHiddenRow() {
		return removeHiddenRow;
	}

	public void setRemoveHiddenRow(boolean removeHiddenRow) {
		this.removeHiddenRow = removeHiddenRow;
	}
	
    public abstract int[] getStartRowNumber();

	public abstract int[] getHiddenRowNumber();

	public abstract String getDownloadName();

	public abstract String getTemplateFileName();

	public abstract void handleSpecialRow(HSSFSheet sheet, List<Map<String, Object>> list);
}