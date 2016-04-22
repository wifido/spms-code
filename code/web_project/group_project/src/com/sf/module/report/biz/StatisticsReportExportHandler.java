package com.sf.module.report.biz;

import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import com.sf.module.common.util.Template;

public class StatisticsReportExportHandler extends Template {

	private static final String SCHEDULED_INPUT_STATISTICAL_NAME = "场地排班统计表";
	private static final int COLUMN_LENGTH = 27;
	private static final String KEY_DEPT_CODE = "DEPT_CODE";
	private static final String KEY_DAY_OF_MONTH = "DAY_OF_MONTH";
	private static final String KEY_AREA_CODE = "AREA_CODE";
	private static final String KEY_TOTAL_EMP_NUM = "TOTAL_EMP_NUM";
	private static final String KEY_FULLTIME_EMP_NUM = "FULLTIME_EMP_NUM";
	private static final String KEY_NOT_FULLTIME_EMP_NUM = "NOT_FULLTIME_EMP_NUM";
	private static final String KEY_OUT_EMP_NUM = "OUT_EMP_NUM";
	private static final String KEY_CLASS_NUM = "CLASS_NUM";
	private static final String KEY_GROUP_NUM = "GROUP_NUM";
	private static final String KEY_TOTAL_ATTENDANCE_NUM = "TOTAL_ATTENDANCE_NUM";
	private static final String KEY_FULLTIME_ATTENDANCE_NUM = "FULLTIME_ATTENDANCE_NUM";
	private static final String KEY_NOT_FULLTIME_ATTENDANCE_NUM = "NOT_FULLTIME_ATTENDANCE_NUM";
	private static final String KEY_OUT_ATTENDANCE_NUM = "OUT_ATTENDANCE_NUM";
	private static final String KEY_TOTAL_REST_NUM = "TOTAL_REST_NUM";
	private static final String KEY_FULLTIME_REST_NUM = "FULLTIME_REST_NUM";
	private static final String KEY_NOT_FULLTIME_REST_NUM = "NOT_FULLTIME_REST_NUM";
	private static final String KEY_OUT_REST_NUM = "OUT_REST_NUM";
	private static final String KEY_TOTAL_WORKTIME = "TOTAL_WORKTIME";
	private static final String KEY_TOTAL_ATTENDANCE_PERCENT = "TOTAL_ATTENDANCE_PERCENT";
	private static final String KEY_FULLTIME_EMP_PERCENT = "FULLTIME_EMP_PERCENT";
	private static final String KEY_NOT_FULLTIME_EMP_PERCENT = "NOT_FULLTIME_EMP_PERCENT";
	private static final String KEY_OUT_EMP_PERCENT = "OUT_EMP_PERCENT";
	private static final String KEY_TOTAL_SCHEDULING_NUM = "TOTAL_SCHEDULING_NUM";
	private static final String KEY_FULLTIME_SCHEDULING_NUM = "FULLTIME_SCHEDULING_NUM";
	private static final String KEY_NOT_FULLTIME_SCHEDULING_NUM = "NOT_FULLTIME_SCHEDULING_NUM";
	private static final String KEY_OUT_SCHEDULING_NUM = "OUT_SCHEDULING_NUM";
	private static final String KEY_SCHEDULING_RATIO = "SCHEDULING_RATIO";
//	private static final String KEY_COM_FULL_ATTENDANCE = "COM_FULL_ATTENDANCE";
//	private static final String KEY_COM_NOT_FULL_ATTENDANCE = "COM_NOT_FULL_ATTENDANCE";
//	private static final String KEY_COM_OUT_ATTENDANCE = "COM_OUT_ATTENDANCE";
	private static final String KEY_ZERO = "0";

	@Override
	protected void createHeader(HSSFRow row, Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		for (StatisticsExportColumn warningColumn : StatisticsExportColumn.values()) {
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
		for (int column = 0; column < COLUMN_LENGTH; column++) {
			row.createCell(column).setCellValue(getColumnValue((Map<String, Object>) result, column));
		}
	}

	private String getColumnValue(Map<String, Object> result, int column) {
		for (StatisticsExportColumn statisticalColumn : StatisticsExportColumn.values()) {
			if (statisticalColumn.ordinal() == column) {
				String value = statisticalColumn.getValue(result);
				return value.equals("null") ? "" : value;
			}
		}
		return "";
	}

	@Override
	protected String getSheetName() {
		return SCHEDULED_INPUT_STATISTICAL_NAME;
	}

	public static enum StatisticsExportColumn {
		DAY_OF_MONTH("日期") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_DAY_OF_MONTH));
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
		TOTAL_EMP_NUM("在职总人数") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_TOTAL_EMP_NUM));
			}
		},
		FULLTIME_EMP_NUM("全日制在职") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_FULLTIME_EMP_NUM));
			}
		},
		NOT_FULLTIME_EMP_NUM("非全日制在职") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_NOT_FULLTIME_EMP_NUM));
			}
		},
		OUT_EMP_NUM("外包在职") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_OUT_EMP_NUM));
			}
		},
		TOTAL_SCHEDULING_NUM("总排班数") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_TOTAL_SCHEDULING_NUM));
			}
		},
		FULLTIME_SCHEDULING_NUM("全日制排班数") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_FULLTIME_SCHEDULING_NUM));
			}
		},
		NOT_FULLTIME_SCHEDULING_NUM("非全日制排班数") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_NOT_FULLTIME_SCHEDULING_NUM));
			}
		},
		OUT_SCHEDULING_NUM("外包排班数") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_OUT_SCHEDULING_NUM));
			}
		},
		TOTAL_REST_NUM("排休总数") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_TOTAL_REST_NUM));
			}
		},
		FULLTIME_REST_NUM("全日制排休") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_FULLTIME_REST_NUM));
			}
		},
		NOT_FULLTIME_REST_NUM("非全日制排休") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_NOT_FULLTIME_REST_NUM));
			}
		},
		OUT_REST_NUM("外包排休") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_OUT_REST_NUM));
			}
		},
		TOTAL_ATTENDANCE_NUM("出勤总数") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_TOTAL_ATTENDANCE_NUM));
			}			
		},		
		FULLTIME_ATTENDANCE_NUM("全日制出勤") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_FULLTIME_ATTENDANCE_NUM));
			}
		},
		NOT_FULLTIME_ATTENDANCE_NUM("非全日制出勤") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_NOT_FULLTIME_ATTENDANCE_NUM));
			}
		},
		OUT_ATTENDANCE_NUM("外包出勤") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_OUT_ATTENDANCE_NUM));
			}
		},
		TOTAL_WORKTIME("考勤时长") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_TOTAL_WORKTIME)).equals("null") ? KEY_ZERO : String.valueOf(result.get(KEY_TOTAL_WORKTIME));
			}
		},
		/*COM_FULL_ATTENDANCE("全日制考勤匹配度") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_COM_FULL_ATTENDANCE));
			}
		},
		COM_NOT_FULL_ATTENDANCE("非全日制考勤匹配度") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_COM_NOT_FULL_ATTENDANCE));
			}
		},
		COM_OUT_ATTENDANCE("外包考勤匹配度") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_COM_OUT_ATTENDANCE));
			}
		},*/
		CLASS_NUM("班次数") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_CLASS_NUM));
			}
		},
		GROUP_NUM("小组数") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_GROUP_NUM));
			}
		},
		TOTAL_ATTENDANCE_PERCENT("出勤总占比") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_TOTAL_ATTENDANCE_PERCENT));
			}
		},
		FULLTIME_EMP_PERCENT("全日制出勤占比") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_FULLTIME_EMP_PERCENT));
			}
		},
		NOT_FULLTIME_EMP_PERCENT("非全日制出勤占比") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_NOT_FULLTIME_EMP_PERCENT));
			}
		},
		OUT_EMP_PERCENT("外包出勤占比") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_OUT_EMP_PERCENT));
			}
		},
		SCHEDULING_RATIO("排班占比") {
			@Override
			public String getValue(Map<String, Object> result) {
				return String.valueOf(result.get(KEY_SCHEDULING_RATIO));
			}
		};

		public final String title;

		StatisticsExportColumn(String title) {
			this.title = title;
		}

		private static String whileEmptyReturnZero(String value) {
			return value.equals("null") ? KEY_ZERO : value;
		}

		public abstract String getValue(Map<String, Object> result);

		public HSSFCell cellCreate(HSSFRow row) {
			HSSFCell cell = row.createCell((short) this.ordinal());
			cell.setCellValue(title);
			return cell;
		}
	}
}
