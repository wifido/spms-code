package com.sf.module.driver.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.sf.module.common.util.Template;
import com.sf.module.driver.domain.WarningModel;

public class DriverWarningContinuousExport extends Template {
    private static final int COLUMN_LENGTH = 10;

    @Override
    protected void createHeader(HSSFRow row, Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        for (WarningContinuousColumn warningContinuousColumn : WarningContinuousColumn.values()) {
            HSSFCell hssfCell = warningContinuousColumn.cellCreate(row);
            hssfCell.setCellStyle(cellStyle);
        }
    }

    @Override
    protected int headerRowCount() {
        return 1;
    }

    @Override
    protected void createSingleCell(HSSFRow row, Object result, CellStyle cellStyle) {
        for (int column = 0; column <= COLUMN_LENGTH; column++) {
            row.createCell(column).setCellValue(getColumnValue((WarningModel.MultipleWarning) result, column));
        }
    }

    @Override
    protected String getSheetName() {
        return "连续出勤超6天明细";
    }

    private String getColumnValue(WarningModel.MultipleWarning multipleWarning, int column) {
        for (WarningContinuousColumn warningColumn : WarningContinuousColumn.values()) {
            if (warningColumn.ordinal() == column)
                return warningColumn.getValue(multipleWarning);
        }
        return "";
    }

    public static enum WarningContinuousColumn {
        NUMBER("序号") {
            @Override
            public String getValue(WarningModel.MultipleWarning multipleWarning) {
                return String.valueOf(multipleWarning.getSerial());
            }
        }, AREA_CODE("地区") {
            @Override
            public String getValue(WarningModel.MultipleWarning multipleWarning) {
                return multipleWarning.getAreaCode() + "/" + multipleWarning.getAreaName();
            }
        }, DEPARTMENT_CODE("网点") {
            @Override
            public String getValue(WarningModel.MultipleWarning multipleWarning) {
                return multipleWarning.getDepartmentCode() + "/" + multipleWarning.getDepartmentName();
            }
        }, EMPLOYEE_CODE("工号") {
            @Override
            public String getValue(WarningModel.MultipleWarning multipleWarning) {
                return multipleWarning.getEmployeeCode();
            }
        }, EMPLOYEE_NAME("驾驶员") {
            @Override
            public String getValue(WarningModel.MultipleWarning multipleWarning) {
                return multipleWarning.getEmployeeName();
            }
        }, ONE_CONTINUOUS_WORKING_MAX_DAYS("1月连续出勤最大天数") {
            @Override
            public String getValue(WarningModel.MultipleWarning multipleWarning) {
                return String.valueOf(multipleWarning.firstMaxContinuousWorkingDay);
            }
        }, TWO_CONTINUOUS_WORKING_MAX_DAYS("2月连续出勤最大天数") {
            @Override
            public String getValue(WarningModel.MultipleWarning multipleWarning) {
                return String.valueOf(multipleWarning.secondMaxContinuousWorkingDay);
            }
        }, THREE_CONTINUOUS_WORKING_MAX_DAYS("3月连续出勤最大天数") {
            @Override
            public String getValue(WarningModel.MultipleWarning multipleWarning) {
                return String.valueOf(multipleWarning.thirdMaxContinuousWorkingDay);
            }
        }, IS_CONTINUOUS_TWO_MONTH_WORKING_SIX_DAYS("是否连续2个月出勤超6天") {
            @Override
            public String getValue(WarningModel.MultipleWarning multipleWarning) {
                return multipleWarning.twoMonthContinuous ? "是" : "否";
            }
        }, IS_CONTINUOUS_THREE_MONTH_WORKING_SIX_DAYS("是否连续3个月出勤超6天") {
            @Override
            public String getValue(WarningModel.MultipleWarning multipleWarning) {
                return multipleWarning.threeMonthContinuous ? "是" : "否";
            }
        };

        public final String title;

        WarningContinuousColumn(String title) {
            this.title = title;
        }

        public abstract String getValue(WarningModel.MultipleWarning multipleWarning);

        private HSSFCell cellCreate(HSSFRow row) {
            HSSFCell cell = row.createCell(ordinal());
            cell.setCellValue(title);
            return cell;
        }
    }
}