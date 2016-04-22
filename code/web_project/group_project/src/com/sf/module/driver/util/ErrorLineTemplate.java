package com.sf.module.driver.util;

import static java.util.Arrays.asList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import com.sf.module.common.util.Template;
import com.sf.module.driver.domain.LineImportTable;

public class ErrorLineTemplate extends Template {
	private List<String> headerNames = asList("数据源", "归属网点", "出车时间", "收车时间", "始发网点(代码)", "目的网点(代码)", "指定车牌号", "车型", "错误信息");

	public int countColumn() {
		return headerNames.size();
	}

	@Override
	public void createHeader(HSSFRow row, Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		for (int index = 0; index < countColumn(); index++) {
			HSSFCell hssfCell = cellCreate(row, headerNames.get(index), index);
			hssfCell.setCellStyle(cellStyle);
		}
	}

	private HSSFCell cellCreate(HSSFRow row, String title, int column) {
		HSSFCell cell = row.createCell((short) column);
		cell.setCellValue(title);
		return cell;
	}

	@Override
	protected int headerRowCount() {
		return 1;
	}

	@Override
	protected void createSingleCell(HSSFRow row, Object result, CellStyle cellStyle) {
		for (int column = 0; column <= countColumn(); column++) {
			HSSFCell cell = row.createCell(column);
			cell.setCellValue(getColumnValue(result, column));
			cell.setCellStyle(cellStyle);
		}
	}

	@Override
	protected String getSheetName() {
		return "DriveLine";
	}

	private String getColumnValue(Object result, int column) {
		return ((LineImportTable) result).getValue(column);
	}
}