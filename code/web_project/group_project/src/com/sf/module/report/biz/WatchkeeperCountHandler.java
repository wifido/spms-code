package com.sf.module.report.biz;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.sf.module.common.util.Template;

public class WatchkeeperCountHandler extends Template{

    private static final String SCHEDULED_INPUT_STATISTICAL_NAME = "值班人员统计表导出";
    private static final int COLUMN_LENGTH = 8;
    private static final String KEY_SHEDULE_DT = "SHEDULE_DT";
    private static final String KEY_AREA_CODE = "AREA_CODE";
    private static final String KEY_DEPT_CODE = "DEPT_CODE";	
	private static final String KEY_EMP_CODE = "EMP_CODE";
	private static final String KEY_EMP_NAME = "EMP_NAME";	
	private static final String KEY_EMP_DUTY_NAME = "EMP_DUTY_NAME";
	private static final String KEY_SHEDULE_TIME = "SHEDULE_TIME";
	private static final String KEY_SCHEDULE_CODE = "SCHEDULE_CODE";
	
    
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

    @SuppressWarnings("unchecked")
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
    	SHEDULE_DT("日期") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_SHEDULE_DT));
			}
		},
		AREA_CODE("地区代码") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_AREA_CODE));
			}
		},
		DEPT_CODE("网点代码") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_DEPT_CODE));
			}
		},
        EMP_CODE("员工工号") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_EMP_CODE));
            }
        },
        EMP_NAME("员工姓名") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_EMP_NAME));
            }
        },
        EMP_DUTY_NAME("职位") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_EMP_DUTY_NAME)).equals("null") ? "" : String.valueOf(result.get(KEY_EMP_DUTY_NAME));
            }
        },
        SHEDULE_TIME("班别时间") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_SHEDULE_TIME)).equals("null") ? "" : String.valueOf(result.get(KEY_SHEDULE_TIME));
            }
        },      
        SCHEDULE_CODE("班别代码") {
            @Override
            public String getValue(Map<String, Object> result) {
            	return String.valueOf(result.get(KEY_SCHEDULE_CODE)).equals("null") ? "" : String.valueOf(result.get(KEY_SCHEDULE_CODE));
            }
        };

        public final String title;

        TrainingExportColumn(String title) {
            this.title = title;
        }
        
		@SuppressWarnings("unused")
		private static String whileEmptyReturnZero(String value) {
			return value.equals("null") ? "0" : value;
		}

        public abstract String getValue(Map<String, Object> result);

        @SuppressWarnings("deprecation")
		public HSSFCell cellCreate(HSSFRow row) {
            HSSFCell cell = row.createCell((short) this.ordinal());
            cell.setCellValue(title);
            return cell;
        }
    }
}
