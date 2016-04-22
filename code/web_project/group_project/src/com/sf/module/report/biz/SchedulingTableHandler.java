package com.sf.module.report.biz;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.sf.module.common.util.Template;

public class SchedulingTableHandler extends Template{

    private static final String SCHEDULED_INPUT_STATISTICAL_NAME = "排班表导出";
    private static final int COLUMN_LENGTH = 13;
    private static final String KEY_DEPT_CODE = "DEPT_CODE";
    private static final String KEY_MONTH_ID = "MONTH_ID";
	private static final String KEY_AREA_CODE = "AREA_CODE";
	private static final String KEY_EMP_CODE = "EMP_CODE";
	private static final String KEY_EMP_NAME = "EMP_NAME";
	private static final String KEY_PERSK_TXT = "PERSK_TXT";
	private static final String KEY_SF_DATE = "SF_DATE";
	private static final String KEY_EMP_STATUS = "EMP_STATUS";
	private static final String KEY_SHEDULE_NUM = "SHEDULE_NUM";
	private static final String KEY_GROUP_NUM = "GROUP_NUM";
	private static final String KEY_PROCESS_NUM = "PROCESS_NUM";
	private static final String KEY_LENGTH_TIME_OF_DAY = "LENGTH_TIME_OF_DAY";
	private static final String KEY_REST_DAYS = "REST_DAYS";
	private static final String KEY_TOTAL_ATTENDANCE = "TOTAL_ATTENDANCE";
    
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
    	MONTH_ID("月份") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_MONTH_ID));
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
        PERSK_TXT("人员类型") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_PERSK_TXT)).equals("null") ? "" : String.valueOf(result.get(KEY_PERSK_TXT));
            }
        },
        SF_DATE("入职时间") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_SF_DATE)).equals("null") ? "" : String.valueOf(result.get(KEY_SF_DATE));
            }
        },
        EMP_STATUS("在职状态") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_EMP_STATUS)).equals("1")? "在职" : "离职";
            }
        },
        SHEDULE_NUM("班别数量") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_SHEDULE_NUM));
            }
        },
        GROUP_NUM("小组数量") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_GROUP_NUM));
            }
        },
        PROCESS_NUM("工序数量") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_PROCESS_NUM));
            }
        },
        LENGTH_TIME_OF_DAY("日均时长") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_LENGTH_TIME_OF_DAY));
            }
        },
        REST_DAYS("休息天数") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_REST_DAYS));
            }
        },
        TOTAL_ATTENDANCE("出勤天数") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_TOTAL_ATTENDANCE));
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
