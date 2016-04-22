package com.sf.module.common.biz;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.sf.module.common.util.StringUtil;
import com.sf.module.common.util.Template;

public class ErrorSchedulingDataExportHandler extends Template{

    private static final String SCHEDULED_INPUT_STATISTICAL_NAME = "SAP返回排班错误数据";
    private static final int COLUMN_LENGTH = 10;
    private static final String KEY_DEPT_CODE = "DEPT_CODE";
    private static final String KEY_EMP_CODE = "EMP_CODE";
    private static final String KEY_EMP_NAME = "EMP_NAME";
    private static final String KEY_BEGIN_DATE = "BEGIN_DATE";
    private static final String KEY_BEGIN_TM = "BEGIN_TM";
    private static final String KEY_END_TM = "END_TM";
    private static final String KEY_TMR_DAY_FLAG = "TMR_DAY_FLAG";
    private static final String KEY_OFF_DUTY_FLAG = "OFF_DUTY_FLAG";
    private static final String KEY_ERROR_INFO = "ERROR_INFO";
    private static final String KEY_LASTUPDATE = "LASTUPDATE";
    
    @Override
    protected void createHeader(HSSFRow row, Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        for (ErrorSchedulingDataExportColumn warningColumn : ErrorSchedulingDataExportColumn.values()) {
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
        for (ErrorSchedulingDataExportColumn statisticalColumn : ErrorSchedulingDataExportColumn.values()) {
            if (statisticalColumn.ordinal() == column)
                return statisticalColumn.getValue(result);
        }
        return "";
    }

    @Override
    protected String getSheetName() {
        return SCHEDULED_INPUT_STATISTICAL_NAME;
    }

    public static enum ErrorSchedulingDataExportColumn{
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
        BEGIN_DATE("排班日期") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_BEGIN_DATE));
            }
        },
        BEGIN_TM("开始时间") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_BEGIN_TM));
            }
        },
        END_TM("结束时间") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_END_TM));
            }
        },
        OFF_DUTY_FLAG("OFF为休息") {
            @Override
            public String getValue(Map<String, Object> result) {
            	if(StringUtil.isBlank(String.valueOf(result.get(KEY_OFF_DUTY_FLAG))) || "null".equals(String.valueOf(result.get(KEY_OFF_DUTY_FLAG)))){
            		return "";
            	}
                return String.valueOf(result.get(KEY_OFF_DUTY_FLAG));
            }
        },
        TMR_DAY_FLAG("X表示前一天") {
            @Override
            public String getValue(Map<String, Object> result) {
            	if(StringUtil.isBlank(String.valueOf(result.get(KEY_TMR_DAY_FLAG))) || "null".equals(String.valueOf(result.get(KEY_TMR_DAY_FLAG)))){
            		return "";
            	}
            	return String.valueOf(result.get(KEY_TMR_DAY_FLAG));
            }
        },
        ERROR_INFO("错误主题") {
            @Override
            public String getValue(Map<String, Object> result) {
            	return String.valueOf(result.get(KEY_ERROR_INFO));
            }
        },
        LASTUPDATE("错误返回时间") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_LASTUPDATE));
            }
        };

        public final String title;

        ErrorSchedulingDataExportColumn(String title) {
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
