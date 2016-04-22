package com.sf.module.driver.util;

import com.sf.module.common.util.Template;
import com.sf.module.driver.domain.ComparedReportRepository.ComparedReporter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class CompareReportTemplate extends Template {
	private static final String REPORT_SHEET_TITLE = "排班异常明细";
	private static final int HEADER_ROW_COUNT = 2;
	private List<ColumnValue> extractRuler = newArrayList();

	private List<String> headerBrief;
	private List<String> headerNames;

	public CompareReportTemplate(List<String> headerBrief, List<String> headerNames, List<String> dateRange) {
		this.headerBrief = headerBrief;
		this.headerNames = headerNames;

		extractRuler.add(new ColumnValue() {
			public String getValue(ComparedReporter comparedReporter) {
				return String.valueOf(comparedReporter.getRowNumber());
			}
		});

		extractRuler.add(new ColumnValue() {
			public String getValue(ComparedReporter comparedReporter) {
				return comparedReporter.getAreaName();
			}
		});

		extractRuler.add(new ColumnValue() {
			public String getValue(ComparedReporter comparedReporter) {
				return comparedReporter.getAreaCode();
			}
		});

		extractRuler.add(new ColumnValue() {
			public String getValue(ComparedReporter comparedReporter) {
				return comparedReporter.getEmployeeName();
			}
		});

		extractRuler.add(new ColumnValue() {
			public String getValue(ComparedReporter comparedReporter) {
				return comparedReporter.getEmployeeCode();
			}
		});

		for (int index = 0; index < dateRange.size(); index++) {
			final int finalIndex = index + 1;
			extractRuler.add(new ColumnValue() {
				public String getValue(ComparedReporter comparedReporter) {
					return comparedReporter.getValueWithSpecifyDay(String.format("DAY%d", finalIndex));
				}
			});
		}
	}

	@Override
	protected void createHeader(HSSFRow row, Workbook workbook) {
		CellStyle cellStyle = this.createGreyStyle(workbook);

		for (int i = 0; i < headerBrief.size(); i++) {
			HSSFCell hssfCell = cellCreate(row, headerBrief.get(i), i);
			if (i % 2 == 0) {
				hssfCell.setCellStyle(cellStyle);
			}
		}

		row = row.getSheet().createRow(row.getRowNum() + 1);
		for (int index = 0; index < headerNames.size(); index++) {
			HSSFCell hssfCell = cellCreate(row, headerNames.get(index), index);
			hssfCell.setCellStyle(cellStyle);
		}
	}

	private CellStyle createGreyStyle(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		return cellStyle;
	}

	private HSSFCell cellCreate(HSSFRow row, String title, int column) {
		HSSFCell cell = row.createCell((short) column);
		cell.setCellValue(title);
		return cell;
	}

	@Override
	protected int headerRowCount() {
		return HEADER_ROW_COUNT;
	}

	@Override
	protected void createSingleCell(HSSFRow row, Object result, CellStyle cellStyle) {
		ComparedReporter cellValue = (ComparedReporter) result;

		for (int column = 0; column < headerNames.size(); column++) {
			HSSFCell cell = row.createCell(column);

			ColumnValue columnValue = this.extractRuler.get(column);
			cell.setCellValue(columnValue.getValue(cellValue));

			cell.setCellStyle(cellStyle);
		}
	}

	private interface ColumnValue {
		String getValue(ComparedReporter comparedReporter);
	}

	@Override
	protected String getSheetName() {
		return REPORT_SHEET_TITLE;
	}
}