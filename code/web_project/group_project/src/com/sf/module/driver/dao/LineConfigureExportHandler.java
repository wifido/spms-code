package com.sf.module.driver.dao;

import static com.google.common.collect.Maps.*;
import static com.sf.module.common.util.TemplateHelper.createCell;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.util.CellRangeAddress;
import com.sf.framework.core.exception.BizException;
import com.sf.module.common.util.AbstractExcelExportHandler;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.TemplateHelper;

public class LineConfigureExportHandler extends AbstractExcelExportHandler {

	private static final String END_TIME = "END_TIME";
	private static final String START_TIME = "START_TIME";
	private static final String CONFIGURE_CODE = "CONFIGURE_CODE";
	private static final String TEMPLATE_NAME = "配班导出模板.xls";
	private static final String DOWNLOAD_NAME = "配班导出数据";
	private static final int NUMBER_CONFIGURE_COLDE_CELL = 2;
	private static final int NUMBER_CONFIGURE_ID_CELL = 0;
	private static final int NUMBER_MONTH_CELL = 1;
	private static final int NUMBER_WORK_TIME_CELL = 11;

	public int[] getStartRowNumber() {
		return new int[] { 2 };
	}

	public int[] getHiddenRowNumber() {
		return new int[] { 0 };
	}

	public String getDownloadName() {
		return DOWNLOAD_NAME;
	}

	public String getTemplateFileName() {
		return TEMPLATE_NAME;
	}

	public void handleSpecialRow(HSSFSheet sheet, List<Map<String, Object>> lineCofnigures) {
		Map<String, CellRangeAddress> mergedCellMap = getCellRangeAddress(sheet, countWorktime(lineCofnigures));
		mergeCell(sheet, mergedCellMap);
	}

	private Map<String, WorkTimeCounter> countWorktime(List<Map<String, Object>> lineCofnigures) {
		Map<String, WorkTimeCounter> workTimeCounters = newHashMap();
		for (Map<String, Object> lineConfigure : lineCofnigures) {
			String configureCode = (String) lineConfigure.get(CONFIGURE_CODE);
			String startTime = (String) lineConfigure.get(START_TIME);
			String endTime = (String) lineConfigure.get(END_TIME);
			WorkTimeCounter counter;
			if (workTimeCounters.containsKey(configureCode)) {
				counter = workTimeCounters.get(configureCode);
				counter.setCrossDays(counter.getEndTime(), startTime);
				counter.setCrossDays(startTime, endTime);
				counter.setEndTime(endTime);
				continue;
			}
			counter = new WorkTimeCounter();
			counter.setStartTime(startTime);
			counter.setEndTime(endTime);
			counter.setCrossDays(startTime, endTime);
			workTimeCounters.put(configureCode, counter);
		}
		return workTimeCounters;
	}

	private void mergeCell(HSSFSheet sheet, Map<String, CellRangeAddress> mergedCellMap) {
		for (Map.Entry<String, CellRangeAddress> entry : mergedCellMap.entrySet()) {
			CellRangeAddress cellRangeAddress = entry.getValue();
			sheet.addMergedRegion(cellRangeAddress);
			mergedSpecifyColumn(sheet, cellRangeAddress, NUMBER_CONFIGURE_ID_CELL);
			mergedSpecifyColumn(sheet, cellRangeAddress, NUMBER_MONTH_CELL);
			mergedSpecifyColumn(sheet, cellRangeAddress, NUMBER_WORK_TIME_CELL);
		}
	}

	private void mergedSpecifyColumn(HSSFSheet sheet, CellRangeAddress cellRangeAddress, int indexOfCell) {
		CellRangeAddress range = cellRangeAddress.copy();
		range.setFirstColumn(indexOfCell);
		range.setLastColumn(indexOfCell);
		sheet.addMergedRegion(range);
	}

	private Map<String, CellRangeAddress> getCellRangeAddress(HSSFSheet sheet, Map<String, WorkTimeCounter> workTimeCounters) {
		Map<String, CellRangeAddress> cellRangeAddressMap = newLinkedHashMap();
		int sequence = 1;
		for (int i = getStartRowNumber()[0]; i <= sheet.getLastRowNum(); i++) {
			HSSFRow row = sheet.getRow(i);
			HSSFCell cell = row.getCell(NUMBER_CONFIGURE_COLDE_CELL);
			String configureCode = TemplateHelper.getCellValueAsString(cell);
			CellRangeAddress cellRangeAddress;
			if (cellRangeAddressMap.containsKey(configureCode)) {
				cellRangeAddress = cellRangeAddressMap.get(configureCode);
				cellRangeAddress.setLastRow(cellRangeAddress.getLastRow() + 1);
				continue;
			}
			setValueToSpecifyColumn(row, NUMBER_CONFIGURE_ID_CELL, String.valueOf(sequence));
			setValueToSpecifyColumn(row, NUMBER_WORK_TIME_CELL, String.format("%.1f", workTimeCounters.get(configureCode).getWorkTime()));
			cellRangeAddress = new CellRangeAddress(i, i, NUMBER_CONFIGURE_COLDE_CELL, NUMBER_CONFIGURE_COLDE_CELL);
			cellRangeAddressMap.put(configureCode, cellRangeAddress);
			sequence++;
		}
		return cellRangeAddressMap;
	}

	private void setValueToSpecifyColumn(HSSFRow row, int indexOfCell, String cellVaule) {
		HSSFCell cell = row.getCell(indexOfCell);
		if (cell == null) {
			createCell(row, indexOfCell, cellVaule);
			return;
		}
		cell.setCellValue(cellVaule);
	}
}

class WorkTimeCounter {
	private static final String DEFAULT_DAY = "2014-12-01 ";
	private String startTime;
	private String endTime;
	private int crossDays;

	public int getCrossDays() {
		return crossDays;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public double getWorkTime() {
		Date startDate = parseDate(startTime);
		Date endDate = parseDate(endTime);
		double between = endDate.getTime() - startDate.getTime();
		if (between == 0) {
			return 24;
		}
		return between / (1000 * 60 * 60) + crossDays * 24;
	}

	public void setCrossDays(String startTime, String endTime) {
		if (parseDate(startTime).after(parseDate(endTime))) {
			crossDays++;
		}
	}

	private Date parseDate(String time) {
		try {
			return DateUtil.parseDate(DEFAULT_DAY + time, DateFormatType.yyyy_MM_dd_HHmm);
		} catch (ParseException e) {
			throw new BizException("parse date error!");
		}
	}
}