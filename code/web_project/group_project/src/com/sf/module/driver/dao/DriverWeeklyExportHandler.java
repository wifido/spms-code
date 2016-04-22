package com.sf.module.driver.dao;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import com.sf.module.common.util.Template;
import com.sf.module.driver.domain.WeeklyExportModel;

public class DriverWeeklyExportHandler extends Template{
	@Override
	protected void createHeader(HSSFRow row, Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		
		for (WeeklyExport weeklyExport : WeeklyExport.values()) {
			HSSFCell hssfCell = weeklyExport.cellCreate(row);
            hssfCell.setCellStyle(cellStyle);
		}
	}

	@Override
	protected int headerRowCount() {
		return 1;
	}

	@Override
	protected void createSingleCell(HSSFRow row, Object result, CellStyle cellStyle) {
		for (int column = 0; column <= 7; column++) {
            row.createCell(column).setCellValue(getColumnValue((WeeklyExportModel) result, column));
        }	
	}
	
	private String getColumnValue(WeeklyExportModel original, int column) {
        for (WeeklyExport weeklyExport : WeeklyExport.values()) {
            if (weeklyExport.ordinal() == column)
                return weeklyExport.getValue(original);
        }
        return "";
    }

	@Override
	protected String getSheetName() {
		return "weeklyExport";
	}
	
	public static enum WeeklyExport {
		ORDER("序号") {
			@Override
			public String getValue(WeeklyExportModel model) {
				return String.valueOf(model.getOrder());
			}
		}, AREA_CODE("地区") {
			@Override
			public String getValue(WeeklyExportModel model) {
				return model.getAreaCode();
			}
		}, DEPARTMENT_CODE("网点代码") {
			@Override
			public String getValue(WeeklyExportModel model) {
				return model.getDepartmentCode();
			}
		}, YEAR_WEEK("导出周数") {
			@Override
			public String getValue(WeeklyExportModel model) {
				return model.getYearWeek();
			}
		}, EMPLOYEE_NAME("驾驶员姓名") {
			@Override
			public String getValue(WeeklyExportModel model) {
				return model.getEmployeeName();
			}
		}, EMPLOYEE_CODE("驾驶员工号") {
			@Override
			public String getValue(WeeklyExportModel model) {
				return model.getEmployeeCode();
			}
		}, CONFIRM_STATUS("确认状态") {
			@Override
			public String getValue(WeeklyExportModel model) {
				return model.getConfirmStatus();
			}
		}, CONFIRM_TIME("确认时间") {
			@Override
			public String getValue(WeeklyExportModel model) {
				return model.getConfirmTime();
			}
		};
		public String title;
		
		WeeklyExport(String title) {
			this.title = title;
		}
		
		public abstract String getValue(WeeklyExportModel model);
		
		private HSSFCell cellCreate(HSSFRow row) {
            HSSFCell cell = row.createCell((short) this.ordinal());
            cell.setCellValue(title);
            return cell;
        }
	}
}
