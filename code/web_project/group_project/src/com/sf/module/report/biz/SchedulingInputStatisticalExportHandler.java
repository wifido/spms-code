package com.sf.module.report.biz;

import com.sf.module.common.util.Template;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

public class SchedulingInputStatisticalExportHandler extends Template{
    private static final String SCHEDULED_INPUT_STATISTICAL_NAME = "排班录入统计";
    private static final int COLUMN_LENGTH = 15;
    private static final String PERCENT_SYMBOL = "%";
    private static final String KEY_YEAR_MONTH = "YEAR_MONTH";
    private static final String KEY_AREA_CODE = "AREA_CODE";
    private static final String KEY_DEPARTMENT_CODE = "DEPARTMENT_CODE";
    private static final String KEY_DIURNAL_COUNT = "DIURNAL_COUNT";
    private static final String KEY_NON_DIURNAL_COUNT = "NON_DIURNAL_COUNT";
    private static final String KEY_OUTER_EMPLOYEE = "OUTER_EMPLOYEE";
    private static final String KEY_GROUP_COUNT = "GROUP_COUNT";
    private static final String KEY_GROUPING_COUNT = "GROUPING_COUNT";
    private static final String KEY_CLASS_COUNT = "CLASS_COUNT";
    private static final String KEY_CONFIRM_PROCESS_COUNT = "CONFIRM_PROCESS_COUNT";
    private static final String KEY_SCH_DIURNAL_COUNT = "SCH_DIURNAL_COUNT";
    private static final String KEY_SCH_NON_DIURNAL_COUNT = "SCH_NON_DIURNAL_COUNT";
    private static final String KEY_SCHED_CONFIRM_OUTER_EMP_COUNT = "SCHED_CONFIRM_OUTER_EMP_COUNT";
    private static final String KEY_PRO_CONFIRM_INNER_EMP_COUNT = "PRO_CONFIRM_INNER_EMP_COUNT";
    private static final String KEY_PRO_CONFIRM_OUTER_EMP_COUNT = "PRO_CONFIRM_OUTER_EMP_COUNT";
    private static final String KEY_SCHEDULING_COMPLETE_RATE = "SCHEDULING_COMPLETE_RATE";

    @Override
    protected void createHeader(HSSFRow row, Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        for (ScheduledInputStatisticalColumn warningColumn : ScheduledInputStatisticalColumn.values()) {
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
        for (ScheduledInputStatisticalColumn statisticalColumn : ScheduledInputStatisticalColumn.values()) {
            if (statisticalColumn.ordinal() == column)
                return statisticalColumn.getValue(result);
        }
        return "";
    }

    @Override
    protected String getSheetName() {
        return SCHEDULED_INPUT_STATISTICAL_NAME;
    }

    public static enum ScheduledInputStatisticalColumn{
        YEAR_MONTH("月份") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_YEAR_MONTH));
            }
        },
        AREA_CODE("地区代码") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_AREA_CODE));
            }
        },
        DEPARTMENT_CODE("网点代码") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_DEPARTMENT_CODE));
            }
        },
        DIURNAL_COUNT("全日制用工(在职人数)") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_DIURNAL_COUNT));
            }
        },
        NON_DIURNAL_COUNT("非全日制用工(在职人数)") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_NON_DIURNAL_COUNT));
            }
        },
        OUTER_EMPLOYEE("外包员工") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_OUTER_EMPLOYEE));
            }
        },
        GROUP_COUNT("小组数量") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_GROUP_COUNT));
            }
        },
        GROUPING_EMPLOYEE_COUNT("已分组人数") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_GROUPING_COUNT));
            }
        },
        CLASS_COUNT("班别数") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_CLASS_COUNT));
            }
        },
        CONFIRMING_PROCESS("已确认工序") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_CONFIRM_PROCESS_COUNT));
            }
        },
        SCH_DIURNAL_COUNT("排班确认(全日制用工)") {
            @Override
            public String getValue(Map<String, Object> result) {
                return whileEmptyReturnZero(String.valueOf(result.get(KEY_SCH_DIURNAL_COUNT)));
            }
        },
        SCH_NON_DIURNAL_COUNT("排班确认(非全日制用工)") {
            @Override
            public String getValue(Map<String, Object> result) {
                return whileEmptyReturnZero(String.valueOf(result.get(KEY_SCH_NON_DIURNAL_COUNT)));
            }
        },
        SCHEDULED_CONFIRMING_OUTER("排班确认（外包）") {
            @Override
            public String getValue(Map<String, Object> result) {
                return whileEmptyReturnZero(String.valueOf(result.get(KEY_SCHED_CONFIRM_OUTER_EMP_COUNT)));
            }
        },
        PROCESS_SCHEDULED_INNER("工序安排（内部）") {
            @Override
            public String getValue(Map<String, Object> result) {
                return whileEmptyReturnZero(String.valueOf(result.get(KEY_PRO_CONFIRM_INNER_EMP_COUNT)));
            }
        },
        PROCESS_SCHEDULED_OUTER("工序安排（外包）") {
            @Override
            public String getValue(Map<String, Object> result) {
                return whileEmptyReturnZero(String.valueOf(result.get(KEY_PRO_CONFIRM_OUTER_EMP_COUNT)));
            }
        },
        SCHEDULED_COMPLETION_RATE("排班完成率") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_SCHEDULING_COMPLETE_RATE)) + PERCENT_SYMBOL;
            }
        };

        public final String title;

        ScheduledInputStatisticalColumn(String title) {
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
