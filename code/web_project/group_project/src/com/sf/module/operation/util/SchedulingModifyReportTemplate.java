package com.sf.module.operation.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.sf.module.common.util.Template;
import com.sf.module.report.domain.SchedulingModify;

public class SchedulingModifyReportTemplate extends Template {

	private static final String REPORT_SHEET_TITLE = "历史排班修改记录";
	private static final int HEADER_ROW_COUNT = 2;
	private static final String DEPT_CODE_COLUMN = "网点代码：";
	private static final String MONTH_COLUMN = "月份：";
	private static int columnCount = 8;

	private List<ColumnValue> columnList;
	private String deptCode;
	private String month;

	private SchedulingModifyReportTemplate(String deptCode, String month) {
		this.deptCode = deptCode;
		this.month = month;
		columnList=new ArrayList<ColumnValue>();
		columnList.add(ColumnValue.AREA_CODE);
		columnList.add(ColumnValue.DEPT_CODE);
		columnList.add(ColumnValue.EMP_CODE);
		columnList.add(ColumnValue.SCHEDULE_Dt);
		columnList.add(ColumnValue.SCHEDULE_CODE);
		columnList.add(ColumnValue.MODIFY_TIME);
		columnList.add(ColumnValue.MODIFY_EMPCODE);
		columnList.add(ColumnValue.MODIFY_TYPE);
	}

	public static SchedulingModifyReportTemplate createTemplate(String deptCode, String month) {
		return new SchedulingModifyReportTemplate(deptCode, month);
	}

	@Override
	protected void createHeader(HSSFRow row, Workbook workbook) {
		CellStyle cellStyle = createGreyStyle(workbook);
		cellCreate(row, DEPT_CODE_COLUMN, 0).setCellStyle(cellStyle);
		cellCreate(row, deptCode, 1);
		cellCreate(row, MONTH_COLUMN, 2).setCellStyle(cellStyle);
		cellCreate(row, month, 3);
		row = row.getSheet().createRow(row.getRowNum() + 1);
		for (int i = 0; i < columnList.size(); i++) {
			cellCreate(row, columnList.get(i).getTitle(), i);
		}
	}

	private HSSFCell cellCreate(HSSFRow row, String title, int column) {
		HSSFCell cell = row.createCell(column);
		cell.setCellValue(title);
		return cell;
	}

	@Override
	protected int headerRowCount() {
		return HEADER_ROW_COUNT;
	}

	@Override
	protected void createSingleCell(HSSFRow row, Object result, CellStyle cellStyle) {
		SchedulingModify schedulingModify = (SchedulingModify) result;
		for (int i = 0; i < columnCount; i++) {
			HSSFCell cell = row.createCell(i);
			ColumnValue columnValue = columnList.get(i);
			cell.setCellValue(columnValue.getValue(schedulingModify));
		}
	}

	@Override
	protected String getSheetName() {
		return REPORT_SHEET_TITLE;
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

	private enum ColumnValue {
		AREA_CODE("地区代码") {
			@Override
			public String getValue(SchedulingModify schedulingModify) {
				return schedulingModify.getAreaCode();
			}
		},
		DEPT_CODE("网点代码") {
			@Override
			public String getValue(SchedulingModify schedulingModify) {
				return schedulingModify.getDeptCode();
			}
		},
		EMP_CODE("工号") {
			@Override
			public String getValue(SchedulingModify schedulingModify) {
				return schedulingModify.getEmpCode();
			}
		},
		SCHEDULE_Dt("排班日期") {
			@Override
			public String getValue(SchedulingModify schedulingModify) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				return sdf.format(schedulingModify.getScheduleDt());
			}
		},
		SCHEDULE_CODE("班别代码") {
			@Override
			public String getValue(SchedulingModify schedulingModify) {
				return schedulingModify.getScheduleCode();
			}
		},
		MODIFY_TIME("修改时间") {
			@Override
			public String getValue(SchedulingModify schedulingModify) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
				return sdf.format(schedulingModify.getModifiedTm());
			}
		},
		MODIFY_EMPCODE("修改人") {
			@Override
			public String getValue(SchedulingModify schedulingModify) {
				return schedulingModify.getModifiedEmpCode();
			}
		},
		MODIFY_TYPE("是否修改三天前排班") {
			@Override
			public String getValue(SchedulingModify schedulingModify) {
				return schedulingModify.getModifyType()==1?"是":"否";
			}
		};
		private String title;

		private ColumnValue(String title) {
			this.title = title;
		}

		public String getTitle() {
			return title;
		}

		public abstract String getValue(SchedulingModify schedulingModify);
	}
}
