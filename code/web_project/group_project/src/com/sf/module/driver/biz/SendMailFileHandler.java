package com.sf.module.driver.biz;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.sf.framework.core.Toolkit;
import com.sf.module.common.util.StringUtil;
import com.sf.module.common.util.Template;
import com.sf.module.common.util.TemplateHelper;
import com.sf.module.driver.domain.DriveLine;
import com.sf.module.operation.util.CommonUtil;

public class SendMailFileHandler extends Template{

    private static final String SCHEDULED_INPUT_STATISTICAL_NAME = "异常预警数据";
    private static final int COLUMN_LENGTH = 8;
    private static final String KEY_DEPARTMENT_CODE = "DEPARTMENT_CODE";
    private static final String KEY_EMP_SCHE_COUNT = "EMP_SCHE_COUNT";
    private static final String KEY_NON_ATTENDANCE_SCHE = "NON_ATTENDANCE_SCHE";
    private static final String KEY_NON_SCHE_ATTENDANCE = "NON_SCHE_ATTENDANCE";
    private static final String KEY_ATTENDANCE_LESS_SCHE = "ATTENDANCE_LESS_SCHE";
    private static final String KEY_ATTENDANCE_MORE_SCHE = "ATTENDANCE_MORE_SCHE";
    private static final String KEY_AREA_NAME = "AREA_NAME";
    private static final String KEY_DEPT_NAME = "DEPT_NAME";
    

    @Override
    protected void createHeader(HSSFRow row, Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        for (SendMailFileColumn warningColumn : SendMailFileColumn.values()) {
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
        for (SendMailFileColumn statisticalColumn : SendMailFileColumn.values()) {
            if (statisticalColumn.ordinal() == column)
                return statisticalColumn.getValue(result);
        }
        return "";
    }

    @Override
    protected String getSheetName() {
        return SCHEDULED_INPUT_STATISTICAL_NAME;
    }
    
    
    public void write(List<Map<String, Object>> results, String fileName) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle cellStyle = workbook.createCellStyle();

        HSSFSheet sheet = workbook.createSheet(getSheetName());
        HSSFRow row = sheet.createRow(0);
        createHeader(row, workbook);

        for (int i = 0; i < results.size(); i++) {
            row = sheet.createRow(i + headerRowCount());
            Map<String, Object> result = results.get(i);
            createSingleCell(row, result, cellStyle);
        }
        writeToFile(workbook, fileName);
    }
    
	private void writeToFile(HSSFWorkbook workbook, String lineInformationExport) {
		try {
			this.targetFilePath = CommonUtil.getGeneralReportSaveDir(
					DriveLine.class.getSimpleName(), "SendDriverMail")
					+ File.separator
					+ Toolkit.getUuidRandomizer().generate()
					+ CommonUtil.FILE_NAME_SEPARATOR
					+ TemplateHelper.templateName(lineInformationExport);
			File file = new File(targetFilePath);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			workbook.write(fileOutputStream);
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static enum SendMailFileColumn {
		AREA_NAME("所属地区") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_AREA_NAME));
			}
		},
		DEPARTMENT_CODE("网点代码") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_DEPARTMENT_CODE));
			}
		},
		DEPT_NAME("网点名称") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_DEPT_NAME));
			}
		},
		EMP_SCHE_COUNT("排班人数") {
			@Override
			public String getValue(Map<String, Object> result) {
				if (StringUtil.isBlank(String.valueOf(result.get(KEY_EMP_SCHE_COUNT)))
						|| "null".equals(String.valueOf(result.get(KEY_EMP_SCHE_COUNT)))) {
					return "0";
				}
				return String.valueOf(result.get(KEY_EMP_SCHE_COUNT));
			}
		},
		NON_ATTENDANCE_SCHE("排班未出勤") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_NON_ATTENDANCE_SCHE));
			}
		},
		NON_SCHE_ATTENDANCE("出勤未排班") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_NON_SCHE_ATTENDANCE));
			}
		},
		ATTENDANCE_LESS_SCHE("出勤小于排班") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_ATTENDANCE_LESS_SCHE));
			}
		},
		ATTENDANCE_MORE_SCHE("出勤大于排班") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_ATTENDANCE_MORE_SCHE));
			}
		};

        public final String title;

        SendMailFileColumn(String title) {
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
