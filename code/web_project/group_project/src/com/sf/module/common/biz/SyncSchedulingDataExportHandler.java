package com.sf.module.common.biz;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.sf.module.common.util.StringUtil;
import com.sf.module.common.util.Template;

public class SyncSchedulingDataExportHandler extends Template{

    private static final String SCHEDULED_INPUT_STATISTICAL_NAME = "SPMS推送给SAP的排班数据";
    private static final int COLUMN_LENGTH = 15;
    private static final String KEY_EMP_CODE = "EMP_CODE";
    private static final String KEY_BEGIN_DATE = "BEGIN_DATE";
    private static final String KEY_BEGIN_TM = "BEGIN_TM";
    private static final String KEY_END_TM = "END_TM";
    private static final String KEY_TMR_DAY_FLAG = "TMR_DAY_FLAG";
    private static final String KEY_OFF_DUTY_FLAG = "OFF_DUTY_FLAG";
    private static final String KEY_STATE_FLG = "STATE_FLG";
    private static final String KEY_CREATE_TM = "CREATE_TM";
    private static final String KEY_ERROR_INFO = "ERROR_INFO";
    private static final String KEY_SYNC_TM = "SYNC_TM";
    private static final String KEY_SF_DATE = "SF_DATE";
    private static final String KEY_DIMISSION_DT = "DIMISSION_DT";
    private static final String KEY_TRANSFER_DATE = "TRANSFER_DATE";
    private static final String KEY_DATE_FROM = "DATE_FROM";
    private static final String KEY_EMP_POST_TYPE = "EMP_POST_TYPE";
    
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
        EMP_CODE("员工工号") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_EMP_CODE));
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
            	if(StringUtil.isBlank(String.valueOf(result.get(KEY_BEGIN_TM))) || "null".equals(String.valueOf(result.get(KEY_BEGIN_TM)))){
            		return "";
            	}
                return String.valueOf(result.get(KEY_BEGIN_TM));
            }
        },
        END_TM("结束时间") {
            @Override
            public String getValue(Map<String, Object> result) {
            	if(StringUtil.isBlank(String.valueOf(result.get(KEY_END_TM))) || "null".equals(String.valueOf(result.get(KEY_END_TM)))){
            		return "";
            	}
                return String.valueOf(result.get(KEY_END_TM));
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
        OFF_DUTY_FLAG("OFF为休息") {
            @Override
            public String getValue(Map<String, Object> result) {
            	if(StringUtil.isBlank(String.valueOf(result.get(KEY_OFF_DUTY_FLAG))) || "null".equals(String.valueOf(result.get(KEY_OFF_DUTY_FLAG)))){
            		return "";
            	}
                return String.valueOf(result.get(KEY_OFF_DUTY_FLAG));
            }
        },
        STATE_FLG("同步状态") {
            @Override
            public String getValue(Map<String, Object> result) {
            	if (String.valueOf(result.get(KEY_STATE_FLG)).equals("0"))
            		return "未推送";
            	
            	if (String.valueOf(result.get(KEY_STATE_FLG)).equals("1"))
            		return "正在推送";
            	
            	if (String.valueOf(result.get(KEY_STATE_FLG)).equals("2"))
            		return "推送成功";
            	
            	if (String.valueOf(result.get(KEY_STATE_FLG)).equals("3"))
            		return "推送失败";
            	return "";
            }
        },
        EMP_POST_TYPE("岗位") {
            @Override
            public String getValue(Map<String, Object> result) {
            	if (String.valueOf(result.get(KEY_EMP_POST_TYPE)).equals("1"))
            		return "运作";
            	
            	if (String.valueOf(result.get(KEY_EMP_POST_TYPE)).equals("2"))
            		return "一线";
            	
            	if (String.valueOf(result.get(KEY_EMP_POST_TYPE)).equals("3"))
            		return "仓管";
            	return "";
            }
        },
        CREATE_TM("插入接口表时间") {
            @Override
            public String getValue(Map<String, Object> result) {
                return String.valueOf(result.get(KEY_CREATE_TM));
            }
        },
        ERROR_INFO("失败原因") {
            @Override
            public String getValue(Map<String, Object> result) {
            	return String.valueOf(result.get(KEY_ERROR_INFO));
            }
        },
        SYNC_TM("同步时间") {
            @Override
            public String getValue(Map<String, Object> result) {
            	if(StringUtil.isBlank(String.valueOf(result.get(KEY_SYNC_TM))) || "null".equals(String.valueOf(result.get(KEY_SYNC_TM)))){
            		return "";
            	}
                return String.valueOf(result.get(KEY_SYNC_TM));
            }
        },
        SF_DATE("入职时间") {
            @Override
            public String getValue(Map<String, Object> result) {
            	if(StringUtil.isBlank(String.valueOf(result.get(KEY_SF_DATE))) || "null".equals(String.valueOf(result.get(KEY_SF_DATE)))){
            		return "";
            	}
                return String.valueOf(result.get(KEY_SF_DATE));
            }
        },
        DIMISSION_DT("离职时间") {
            @Override
            public String getValue(Map<String, Object> result) {
            	if(StringUtil.isBlank(String.valueOf(result.get(KEY_DIMISSION_DT))) || "null".equals(String.valueOf(result.get(KEY_DIMISSION_DT)))){
            		return "";
            	}
                return String.valueOf(result.get(KEY_DIMISSION_DT));
            }
        },
        TRANSFER_DATE("转岗时间") {
            @Override
            public String getValue(Map<String, Object> result) {
            	if(StringUtil.isBlank(String.valueOf(result.get(KEY_TRANSFER_DATE))) || "null".equals(String.valueOf(result.get(KEY_TRANSFER_DATE)))){
            		return "";
            	}
                return String.valueOf(result.get(KEY_TRANSFER_DATE));
            }
        },
        DATE_FROM("转网时间") {
            @Override
            public String getValue(Map<String, Object> result) {
            	if(StringUtil.isBlank(String.valueOf(result.get(KEY_DATE_FROM))) || "null".equals(String.valueOf(result.get(KEY_DATE_FROM)))){
            		return "";
            	}
                return String.valueOf(result.get(KEY_DATE_FROM));
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
