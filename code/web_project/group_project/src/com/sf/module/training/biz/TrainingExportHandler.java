package com.sf.module.training.biz;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.sf.module.common.util.StringUtil;
import com.sf.module.common.util.Template;

public class TrainingExportHandler extends Template{

    private static final String SCHEDULED_INPUT_STATISTICAL_NAME = "培训信息";
    private static final int COLUMN_LENGTH = 8;
    private static final String KEY_DEPARTMENT_CODE = "DEPARTMENT_CODE";
    private static final String KEY_TRAINING_CODE = "TRAINING_CODE";
    private static final String KEY_EMPLOYEE_CODE = "EMPLOYEE_CODE";
    private static final String KEY_YEARS_MONTH = "YEARS_MONTH";
    private static final String KEY_DAY_OF_MONTH = "DAY_OF_MONTH";
    private static final String KEY_EMP_DUTY_NAME = "EMP_DUTY_NAME";
    private static final String KEY_EMPLOYEE_NAME = "EMPLOYEE_NAME";
    private static final String KEY_POST_TYPE = "POST_TYPE";
    

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
    	YEARS_MONTH("年月") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_YEARS_MONTH));
            }
        },
    	DEPARTMENT_CODE("网点代码") {
             @Override
             public String getValue(Map<String, Object> result) {
                 return String.valueOf(result.get(KEY_DEPARTMENT_CODE));
             }
        },
        TRAINING_CODE("培训代码") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_TRAINING_CODE));
            }
        },
        DAY_OF_MONTH("培训日期") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_DAY_OF_MONTH));
            }
        },
        EMPLOYEE_CODE("员工代码") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_EMPLOYEE_CODE));
            }
        },
        EMPLOYEE_NAME("员工姓名") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_EMPLOYEE_NAME));
            }
        },
        EMP_DUTY_NAME("职位名称") {
            @Override
            public String getValue(Map<String, Object> result) {
            	if(StringUtil.isBlank(String.valueOf(result.get(KEY_EMP_DUTY_NAME))) || "null".equals(String.valueOf(result.get(KEY_EMP_DUTY_NAME)))){
            		return "";
            	}
            	return String.valueOf(result.get(KEY_EMP_DUTY_NAME));
            }
        },
        POST_TYPE("岗位") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_POST_TYPE)).equals("1")? "运作" : "仓管";
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
