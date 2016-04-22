package com.sf.module.dispatch.biz;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.sf.module.common.util.StringUtil;
import com.sf.module.common.util.Template;

public class MonitorReportExportHandler extends Template{

    private static final String SCHEDULED_INPUT_STATISTICAL_NAME = "排班监控";
    private static final int COLUMN_LENGTH = 16;
    private static final String KEY_DAY_OF_MONTH = "DAY_OF_MONTH";
    private static final String KEY_HQ_CODE = "HQ_CODE";
    private static final String KEY_AREA_CODE = "AREA_CODE";
    private static final String KEY_DEPT_CODE = "DEPT_CODE";
    private static final String KEY_FULLTIME_EMP_NUM = "FULLTIME_EMP_NUM";
    private static final String KEY_NOT_FULLTIME_EMP_NUM = "NOT_FULLTIME_EMP_NUM";
    private static final String KEY_FULLTIME_SCHEDULING_NUM = "FULLTIME_SCHEDULING_NUM";
    private static final String KEY_FULLTIME_REST_NUM = "FULLTIME_REST_NUM";
    private static final String KEY_NOT_FULLTIME_SCHEDULING_NUM = "NOT_FULLTIME_SCHEDULING_NUM";
    private static final String KEY_NOT_FULLTIME_REST_NUM = "NOT_FULLTIME_REST_NUM";
    private static final String KEY_FULLTIME_NOT_SCHEDULING = "FULLTIME_NOT_SCHEDULING";
    private static final String KEY_NOT_FULLTIME_NOT_SCHEDULING = "NOT_FULLTIME_NOT_SCHEDULING";
    private static final String KEY_FULLTIME_SCHEDULING_RATE = "FULLTIME_SCHEDULING_RATE";
    private static final String KEY_NOT_FULLTIME_SCHEDULING_RATE = "NOT_FULLTIME_SCHEDULING_RATE";
    private static final String KEY_FULLTIME_PLANNING_WORK_RATE = "FULLTIME_PLANNING_WORK_RATE";
    private static final String KEY_NO_FULLTIME_PLAN_WORK_RATE = "NO_FULLTIME_PLAN_WORK_RATE";
    

    @Override
    protected void createHeader(HSSFRow row, Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        for (MonitorReportExportColumn warningColumn : MonitorReportExportColumn.values()) {
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
        for (MonitorReportExportColumn statisticalColumn : MonitorReportExportColumn.values()) {
            if (statisticalColumn.ordinal() == column)
                return statisticalColumn.getValue(result);
        }
        return "";
    }

    @Override
    protected String getSheetName() {
        return SCHEDULED_INPUT_STATISTICAL_NAME;
    }

    public static enum MonitorReportExportColumn{
    	DAY_OF_MONTH("日期") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_DAY_OF_MONTH));
            }
        },
        HQ_CODE("经营本部") {
             @Override
             public String getValue(Map<String, Object> result) {
            	 if(StringUtil.isBlank(String.valueOf(result.get(KEY_HQ_CODE))) || "null".equals(String.valueOf(result.get(KEY_HQ_CODE)))){
             		return "";
             	}
             	return String.valueOf(result.get(KEY_HQ_CODE));
             }
        },
        AREA_CODE("地区代码") {
            @Override
            public String getValue(Map<String, Object> result) {
            	if(StringUtil.isBlank(String.valueOf(result.get(KEY_AREA_CODE))) || "null".equals(String.valueOf(result.get(KEY_AREA_CODE)))){
            		return "";
            	}
            	return String.valueOf(result.get(KEY_AREA_CODE));
            }
        },
        DEPT_CODE("网点代码") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_DEPT_CODE));
            }
        },
        FULLTIME_EMP_NUM("全日制需排班数") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_FULLTIME_EMP_NUM));
            }
        },
        NOT_FULLTIME_EMP_NUM("非全日制需排班数") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_NOT_FULLTIME_EMP_NUM));
            }
        },
        FULLTIME_SCHEDULING_NUM("全日制上班数") {
            @Override
            public String getValue(Map<String, Object> result) {
           
            	return String.valueOf(result.get(KEY_FULLTIME_SCHEDULING_NUM));
            }
        },
        FULLTIME_REST_NUM("全日制休息数") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_FULLTIME_REST_NUM));
            }
        },
        NOT_FULLTIME_SCHEDULING_NUM("非全日制上班数") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_NOT_FULLTIME_SCHEDULING_NUM));
            }
        },
        NOT_FULLTIME_REST_NUM("非全日制休息数") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_NOT_FULLTIME_REST_NUM));
            }
        },
        FULLTIME_NOT_SCHEDULING("全日制未排班数") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_FULLTIME_NOT_SCHEDULING));
            }
        },
        NOT_FULLTIME_NOT_SCHEDULING("非全日制未排班数") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_NOT_FULLTIME_NOT_SCHEDULING));
            }
        },
        FULLTIME_SCHEDULING_RATE("全日制排班及时率") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_FULLTIME_SCHEDULING_RATE));
            }
        },
        NOT_FULLTIME_SCHEDULING_RATE("非全日制排班及时率") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_NOT_FULLTIME_SCHEDULING_RATE));
            }
        },
        FULLTIME_PLANNING_WORK_RATE("全日制规划上班率") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_FULLTIME_PLANNING_WORK_RATE));
            }
        },
        NO_FULLTIME_PLAN_WORK_RATE("非全日制规划上班率") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_NO_FULLTIME_PLAN_WORK_RATE));
            }
        };

        public final String title;

        MonitorReportExportColumn(String title) {
            this.title = title;
        }

        public abstract String getValue(Map<String, Object> result);

        public HSSFCell cellCreate(HSSFRow row) {
            HSSFCell cell = row.createCell((short) this.ordinal());
            cell.setCellValue(title);
            return cell;
        }
    }
}
