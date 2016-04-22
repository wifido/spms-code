package com.sf.module.driver.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import com.sf.module.common.util.Template;
import com.sf.module.driver.domain.WarningModel;
public class DriverWarningExporter extends Template {
    private static final int COLUMN_LENGTH = 8;

    @Override
    public void createHeader(HSSFRow row, Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        for (WarningColumn warningColumn : WarningColumn.values()) {
            HSSFCell hssfCell = warningColumn.cellCreate(row);
            hssfCell.setCellStyle(cellStyle);
        }
    }

    @Override
    protected int headerRowCount() {
        return 1;
    }

    @Override
    protected void createSingleCell(HSSFRow row, Object original, CellStyle cellStyle) {
        for (int column = 0; column <= COLUMN_LENGTH; column++) {
            row.createCell(column).setCellValue(getColumnValue((WarningModel) original, column));
        }
    }

    private String getColumnValue(WarningModel original, int column) {
        for (WarningColumn warningColumn : WarningColumn.values()) {
            if (warningColumn.ordinal() == column)
                return warningColumn.getValue(original);
        }
        return "";
    }

    @Override
    protected String getSheetName() {
        return "预警明细";
    }

    public static enum WarningColumn {
        SERIAL_NUMBER("序号") {
            @Override
            public String getValue(WarningModel singleWarning) {
                return String.valueOf(singleWarning.getSerial());
            }
        },
        AREA("地区") {
            @Override
            public String getValue(WarningModel singleWarning) {
                return singleWarning.getAreaCode() + "/" + singleWarning.getAreaName();
            }
        },
        DEPARTMENT("网点") {
            @Override
            public String getValue(WarningModel singleWarning) {
                return singleWarning.getDepartmentCode() + "/" + singleWarning.getDepartmentName();
            }
        },
        WARNING_DAY("预警日期") {
            @Override
            public String getValue(WarningModel singleWarning) {
                return String.valueOf(singleWarning.getDriveDay());
            }
        },
        USER_CODE("工号") {
            @Override
            public String getValue(WarningModel SingleWarning) {
                return SingleWarning.getEmployeeCode();
            }
        },
        DRIVER_NAME("驾驶员") {
            @Override
            public String getValue(WarningModel singleWarning) {
                return singleWarning.getEmployeeName();
            }
        },
        MAX_CONTINUOUS_WORKING_COUNT("最大连续出勤天数") {
            @Override
            public String getValue(WarningModel singleWarning) {
                return String.valueOf(singleWarning.getWarningDay());
            }
        };

        public final String title;

        WarningColumn(String title) {
            this.title = title;
        }

        public abstract String getValue(WarningModel SingleWarning);

        private HSSFCell cellCreate(HSSFRow row) {
            HSSFCell cell = row.createCell((short) this.ordinal());
            cell.setCellValue(title);
            return cell;
        }
    }
}