package com.sf.module.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;

public class TemplateHelper {
	public static String templateName(String moduleName, String dateFormat) {
		return moduleName + new SimpleDateFormat(dateFormat).format(new Date()) + ".xls";
	}

	public static String templateName(String moduleName) {
		return moduleName + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".xls";
	}

	public static HSSFWorkbook readExcel(File uploadFile) throws IOException {
		FileInputStream fin = null;
		HSSFWorkbook readWorkBook = null;
		try {
			fin = new FileInputStream(uploadFile);
			readWorkBook = new HSSFWorkbook(fin);
		} finally {
			if (fin != null) {
				fin.close();
			}
		}
		return readWorkBook;
	}

	public static void createCell(HSSFRow row, int cellNumber, String cellValue) {
		createCell(row, cellNumber, cellValue, null);
	}

	public static void createCell(HSSFRow row, int cellNumber, String cellValue, HSSFCellStyle cellStyle) {
		final HSSFCell errorCell = row.createCell(cellNumber);
		errorCell.setCellValue(cellValue);
		if (null != cellStyle) {
			errorCell.setCellStyle(cellStyle);
		}
	}

	public static String getCellValueAsString(HSSFCell cell) {
		if (null == cell) {
			return "";
		}
		cell.setCellType(Cell.CELL_TYPE_STRING);
		return cell.toString().trim();
	}

	public static void wirteToExcel(HSSFWorkbook workBook, String savePath) throws IOException {
		FileOutputStream fos = null;
		try {
			final File outFile = new File(savePath);
			if (!outFile.exists())
				outFile.createNewFile();
			fos = new FileOutputStream(savePath);
			workBook.write(fos);
		} finally {
			if (null != fos) {
				fos.close();
			}
		}
	}

	public static void removeEmptyRow(HSSFSheet sheet) {
		int i = sheet.getLastRowNum();
		HSSFRow tempRow;
		while (i > 0) {
			i--;
			tempRow = sheet.getRow(i);
			if (tempRow == null) {
				sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
			}
		}
	}
}
