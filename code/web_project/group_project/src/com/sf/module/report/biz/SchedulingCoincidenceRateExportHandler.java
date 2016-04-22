package com.sf.module.report.biz;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.sf.module.common.util.Template;

public class SchedulingCoincidenceRateExportHandler extends Template{

    private static final String SCHEDULED_INPUT_STATISTICAL_NAME = "吻合率统计";
    private static final int COLUMN_LENGTH = 8;
    private static final String KEY_DEPT_CODE = "DEPT_CODE";
    private static final String KEY_COINCIDENCE_RATE = "COINCIDENCE_RATE";
    private static final String KEY_COINCIDENCERATE_COUNT = "COINCIDENCERATE_COUNT";
    private static final String KEY_YEAR_MONTH = "YEAR_MONTH";
    private static final String KEY_DEPT_COUNT = "DEPT_COUNT";
    

    @Override
    protected void createHeader(HSSFRow row, Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        for (TrainingExportColumn warningColumn : TrainingExportColumn.values()) {
            HSSFCell hssfCell = warningColumn.cellCreate(row);
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
            row.createCell(column).setCellValue(getColumnValue((Map<String, Object>) result, column));
        }
    }

    private String getColumnValue(Map<String, Object> result, int column) {
        for (TrainingExportColumn statisticalColumn : TrainingExportColumn.values()) {
            if (statisticalColumn.ordinal() == column)
                return statisticalColumn.getValue(result);
        }
        return "";
    }

    @Override
    protected String getSheetName() {
        return SCHEDULED_INPUT_STATISTICAL_NAME;
    }

    public static enum TrainingExportColumn{
    	YEAR_MONTH("年月") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_YEAR_MONTH));
            }
        },
        DEPT_CODE("网点代码") {
             @Override
             public String getValue(Map<String, Object> result) {
                 return String.valueOf(result.get(KEY_DEPT_CODE));
             }
        },
        COINCIDENCE_RATE("吻合率") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_COINCIDENCE_RATE)) + "%";
            }
        },
        COINCIDENCERATE_COUNT("吻合数") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_COINCIDENCERATE_COUNT));
            }
        },
        DEPT_COUNT("总量") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_DEPT_COUNT));
            }
        };

        public final String title;

        TrainingExportColumn(String title) {
            this.title = title;
        }
        
		private static String whileEmptyReturnZero(String value) {
			return value.equals("null") ? "0" : value;
		}

        public abstract String getValue(Map<String, Object> result);

        public HSSFCell cellCreate(HSSFRow row) {
            HSSFCell cell = row.createCell((short) this.ordinal());
            cell.setCellValue(title);
            return cell;
        }
    }
}
