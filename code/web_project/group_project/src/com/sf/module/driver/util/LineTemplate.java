package com.sf.module.driver.util;

import static com.sf.module.driver.dao.LineRepository.*;
import static java.lang.Integer.parseInt;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import com.sf.module.common.util.Template;
import com.sf.module.driver.domain.DriveLine.InputType;

public class LineTemplate extends Template {
	private static final String SHEET_NAME = "线路信息";
	private static final String OPTIMIZE_LINE_CODE = "OPTIMIZE_LINE_CODE";

	private int countColumn() {
		return 9;
	}

	@Override
	protected void createSingleCell(HSSFRow row, Object result, CellStyle cellStyle) {
		for (int column = 0; column <= countColumn(); column++) {
			row.createCell(column).setCellValue(getColumnValue((Map<String, Object>) result, column));
		}
	}

	@Override
	protected void createHeader(HSSFRow row, Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		for (LineTable lineTable : LineTable.values()) {
			HSSFCell hssfCell = lineTable.cellCreate(row);
			hssfCell.setCellStyle(cellStyle);
		}
	}

	private static String formatName(Object code, Object name) {
		return code == null && name == null ? "" : code.toString() + "/" + name.toString();
	}

	// convert time like 1200 to 12:00
	private static String formatTime(String time) {
		return String.format("%s:%s", time.substring(0, 2), time.substring(2));
	}

	private static enum LineTable {
		AREA("地区") {
			@Override
			public String getValue(Map<String, Object> hashMap) {
				Object areaCode = hashMap.get(COL_AREA_CODE);
				return areaCode == null ? "" : areaCode.toString();
			}
		},
		INPUT_TYPE("数据源") {
			@Override
			public String getValue(Map<String, Object> hashMap) {
				Object inputType = hashMap.get(COL_INPUT_TYPE);
				return inputType == null ? "" : InputType.getInputTypeDescription(parseInt(String.valueOf(inputType)));
			}
		},
		BELONG_CODE("归属网点") {
			@Override
			public String getValue(Map<String, Object> hashMap) {
				return formatName(hashMap.get(COL_BELONG_ZONE_CODE), hashMap.get(COL_BELONG_DEPARTMENT_NAME));
			}
		},
		START_TIME("出车时间") {
			@Override
			public String getValue(Map<String, Object> hashMap) {
				Object startTime = hashMap.get(COL_START_TIME);
				return startTime == null ? "" : formatTime(startTime.toString());
			}
		},
		END_TIME("收车时间") {
			@Override
			public String getValue(Map<String, Object> hashMap) {
				Object endTime = hashMap.get(COL_END_TIME);
				return endTime == null ? "" : formatTime(endTime.toString());
			}
		},
		ORIGINAL_CODE("始发网点") {
			@Override
			public String getValue(Map<String, Object> hashMap) {
				return formatName(hashMap.get(COL_SOURCE_CODE), hashMap.get(COL_SOURCE_DEPARTMENT_NAME));
			}
		},
		DESTINATION_CODE("目的网点") {
			@Override
			public String getValue(Map<String, Object> hashMap) {
				return formatName(hashMap.get(COL_DESTINATION_CODE), hashMap.get(COL_DESTINATION_NAME));
			}
		},
		VEHICLE_NUMBER("车牌号") {
			@Override
			public String getValue(Map<String, Object> hashMap) {
				Object vehicleNumber = hashMap.get(COL_VEHICLE_NUMBER);
				return vehicleNumber == null ? "" : vehicleNumber.toString();
			}
		},
		VEHICLE_TYPE("车型") {
			@Override
			public String getValue(Map<String, Object> hashMap) {
				Object vehicleType = hashMap.get(COL_VEHICLE_TYPE);
				return vehicleType == null ? "" : vehicleType.toString();
			}
		};

		private final String title;

		private HSSFCell cellCreate(HSSFRow row) {
			HSSFCell cell = row.createCell((short) this.ordinal());
			cell.setCellValue(title);
			return cell;
		}

		public abstract String getValue(Map<String, Object> hashMap);

		LineTable(String title) {
			this.title = title;
		}
	}

	@Override
	protected int headerRowCount() {
		return 1;
	}

	@Override
	public String getSheetName() {
		return SHEET_NAME;
	}

	private String getColumnValue(Map<String, Object> original, int column) {
		for (LineTable lineTable : LineTable.values()) {
			if (lineTable.ordinal() == column)
				return lineTable.getValue(original);
		}
		return "";
	}
}