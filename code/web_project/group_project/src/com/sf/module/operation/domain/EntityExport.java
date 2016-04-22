package com.sf.module.operation.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import com.sf.module.common.util.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.StringUtils;
import com.sf.module.dispatch.domain.DutyType;
import com.sf.module.operation.util.CommonUtil;

public class EntityExport {

	/**
	 * 获取隐藏的列名
	 * 
	 * @param workbook
	 * @param columnMap
	 *            存储列头的map
	 * @param sheetIndex
	 *            列头所在sheet页
	 * @param hiddenRowIndex
	 *            列头所在的行数
	 * @return columnMap
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap getColumnMap(HSSFWorkbook workbook,
			HashMap columnMap, int sheetIndex, int hiddenRowIndex) {
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		Row row = sheet.getRow(hiddenRowIndex);
		for (Iterator it = row.cellIterator(); it.hasNext();) {
			HSSFCell cell = (HSSFCell) it.next();
			if (cell.getStringCellValue() == null
					|| cell.getStringCellValue().length() <= 0)
				continue;
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			columnMap.put(cell.getStringCellValue(), cell.getColumnIndex());
		}
		return columnMap;
	}

	/**
	 * 把数据写入Excel
	 * 
	 * @param workbook
	 *            需要写入的workbook
	 * @param sheetIndex
	 *            开始写入的sheet页
	 * @param rowIndex
	 *            开始写入的行数
	 * @param columnMap
	 *            表头列数据
	 * @param data
	 *            需要插入的数据
	 * @param isArrange
	 *            是否需要整理数据 无处理为false
	 * @return
	 */
	public static HSSFWorkbook writeWorkBook(HSSFWorkbook workbook,
			int sheetIndex, int rowIndex, HashMap columnMap, List data,
			boolean isArrange) {
		HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
		// 取表格样式
		HSSFCellStyle cellStyle = workbook.createCellStyle(); 
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  		
		HSSFRow firstRow = sheet.getRow(2);
		
//		HSSFCellStyle style = firstRow.getCell(0).getCellStyle();
		
		firstRow.getCell(0).setCellStyle(cellStyle);
		// 遍历写入数据
		int count = 0;
		for (int i = 0; i < data.size(); i++) {
			HashMap dataMap = (HashMap) data.get(i);
			if (isArrange) {
				arrangeData(dataMap);
			}
			count++;
			if (count > 60000) {
				sheetIndex = sheetIndex + 1;
				sheet = workbook.createSheet("汇总" + sheetIndex);
				rowIndex = 0;
				count = 1;
			}
			Iterator<String> iter = columnMap.keySet().iterator();
			int index = rowIndex;
			HSSFRow row = sheet.createRow(index);

			while (iter.hasNext()) {
				String key = iter.next();
				int cellIndex = Integer.parseInt(columnMap.get(key).toString());
				String dataValue = "";
				if (dataMap.get(key) != null
						&& dataMap.get(key).toString().length() > 0) {
					dataValue = dataMap.get(key).toString();
				}
				if (key.equals("NUMBER")) {
					dataValue = String.valueOf(i + 1);
				}
				if (key.equals("EMP_STATUS")) {
					if (dataValue.equals("1")) {
						dataValue = "在职";
					} else {
						dataValue = "离职";
					}
				}
				if (key.equals("WORK_TYPE")) {
					if (Integer.parseInt(dataValue) == 0 || Integer.parseInt(dataValue) == 6 || Integer.parseInt(dataValue) == 8 || Integer.parseInt(dataValue) == 9) {
						dataValue = getDutyTypeName(Integer.parseInt(dataValue));
					} else {
						dataValue = dataMap.get("PERSK_TXT") == null ? "" : dataMap.get("PERSK_TXT").toString();
					}
				}
				//if (!dataValue.isEmpty()) {
				HSSFCell cell = row.createCell(cellIndex);
				cell.setCellValue(dataValue);
				cell.setCellStyle(cellStyle);
			//}
				if(key.equals("ERROR_MSG")){
					HSSFCellStyle style = workbook.createCellStyle();
					cell.setCellValue(dataValue);
					cell.setCellStyle(style);
				}
				
				
				
			}
			rowIndex++;
		}
		return workbook;
	}

	public static String getDutyTypeName(int workType) {
		for (DutyType dutyType : DutyType.values()) {
			if (workType == dutyType.ordinal()) {
				return dutyType.name;
			}
		}
		return "";
	}

	/**
	 * 时间数据加工
	 */
	public static void arrangeData(HashMap map) {
		double totalTime = 0;
		if (map.get("START1_TIME") != null && map.get("END1_TIME") != null) {
			totalTime = totalTime
					+ arrangeTime(map.get("START1_TIME").toString(),
							map.get("END1_TIME").toString());
		}
		if (map.get("START2_TIME") != null && map.get("END2_TIME") != null) {
			totalTime = totalTime
					+ arrangeTime(map.get("START2_TIME").toString(),
							map.get("END2_TIME").toString());
		}
		if (map.get("START3_TIME") != null && map.get("END3_TIME") != null) {
			totalTime = totalTime
					+ arrangeTime(map.get("START3_TIME").toString(),
							map.get("END3_TIME").toString());
		}

		map.put("COUNT_TIME", totalTime);
	}

	/**
	 * 计算时间差
	 * 
	 * @param time1
	 * @param time2
	 * @return hours
	 */
	public static double arrangeTime(String time1, String time2) {
		DateFormat df = new SimpleDateFormat("HH:mm");
		Date dTime1 = null, dTime2 = null;
		try {
			dTime1 = df.parse(time1);
			dTime2 = df.parse(time2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(dTime1);
		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(dTime2);
		cal2.set(Calendar.SECOND, 0);
		cal2.set(Calendar.MILLISECOND, 0);
		double min = (cal2.getTimeInMillis() - cal1.getTimeInMillis())
				/ (60 * 1000);
		double hours = min / 60;
		// 隔日时间差
		if (hours <= 0) {
			hours = hours + 24;
		}
		return hours;
	}

	/**
	 * 导入错误返回Excel整理
	 * 
	 * @param workbook
	 *            用户上传的workbook
	 * @param errorList
	 *            校验之后的错误数据收集
	 * @param sheetIndex
	 *            需要整理的sheet页
	 * @param rowIndex
	 *            开始写入的行数
	 * @param columnMap
	 *            表头列数据
	 * @param downloadFileName
	 *            下载文件名
	 * @param templateFileName
	 *            下载文件名
	 * @throws Exception
	 * @author 632898 李鹏
	 * @date 2014-06-24
	 */
	public static String errorWorkBook(HSSFWorkbook workbook, HashMap errorMap,
			int sheetIndex, int rowIndex, HashMap columnMap,
			String downloadFileName, String templateFileName, int errorIndex)
			throws Exception {
		HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
		HSSFRow row1 = sheet.getRow(1);
		row1.getCell(errorIndex).setCellValue("校验未通过原因");
		int cellIndex = Integer.parseInt(columnMap.get("ERROR_MSG").toString());
		List list = new ArrayList();
		// 清空sheet页
		for (int i = rowIndex; i <= sheet.getLastRowNum(); i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			String rowNum = String.valueOf(row.getRowNum());
			if (!StringUtils.isEmpty(errorMap.get(rowNum))) {
				HSSFCell cell = row.createCell(cellIndex);
				cell.setCellValue(errorMap.get(rowNum).toString());
				list.add(row);
			}
		}
		// 把错误的数据写入sheet
		FileInputStream template = new FileInputStream(
				CommonUtil.getReportTemplatePath(templateFileName));
		HSSFWorkbook wb = new HSSFWorkbook(template);
		Sheet errorSheet = wb.getSheetAt(0);
		// 取表格样式
		HSSFRow firstRow = (HSSFRow) errorSheet.getRow(1);
		HSSFCellStyle style = firstRow.getCell(0).getCellStyle();
		HSSFCell errorCell = firstRow.createCell(cellIndex);
		errorCell.setCellStyle(style);
		errorCell.setCellValue("校验未通过原因");

		for (int j = 0; j < list.size(); j++) {
			HSSFRow row = (HSSFRow) errorSheet.createRow(j + rowIndex);
			HSSFRow dataRow = (HSSFRow) list.get(j);
			Iterator iter = dataRow.iterator();
			while (iter.hasNext()) {
				HSSFCell dataCell = (HSSFCell) iter.next();
				HSSFCell cell = row.createCell(dataCell.getCellNum());
				cell.setCellStyle(style);
				try {
					cell.setCellValue(dataCell.getStringCellValue());
				} catch (Exception e) {

				}
			}
		}
		// 把错误信息写入下载文件中
		CommonUtil.setDownloadFileName(downloadFileName);
		String exportFileName = CommonUtil
				.getSaveFilePath(SchedulingBase.class);
		File file = new File(exportFileName);
		if (!file.exists())
			file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
		wb.write(fos);
		fos.close();
		// 返回下载文件路径
		return CommonUtil.getReturnPageFileName();
	}

}
