package com.sf.module.dispatch.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.dispatch.dao.MonitorReportDao;
import com.sf.module.dispatch.domain.SchedulingForDispatch;
import com.sf.module.operation.util.CommonUtil;

@Component
public class MonitorReportBiz extends BaseBiz {
	@Resource
	private MonitorReportDao monitorReportDao;
	
	public HashMap<String, Object> queryMonitorReport(HashMap<String, String> queryParameter) {
		return monitorReportDao.queryMonitorReport(queryParameter);
	}
	
	public String exportMonitorReport(HashMap<String, String> httpRequestParameter) {
		ArrayList<HashMap<String, Object>> list = monitorReportDao.queryExportMonitorReport(httpRequestParameter);
		if (list.size() == 0) {
			throw new BizException("该网点暂无排班监控数据！");
		}
		HSSFWorkbook workbook = null;
		String fileName = String.format("一线排班监控表_%s.xls", new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(CommonUtil.getReportTemplatePath("一线排班监控表模板.xls"));
			workbook = new HSSFWorkbook(fis);
			HSSFSheet sh = workbook.getSheetAt(0);
			writeExcel(workbook, sh, list);
			CommonUtil.setDownloadFileName(fileName);
			fos = new FileOutputStream(new File(CommonUtil.getSaveFilePath(SchedulingForDispatch.class)));
			workbook.write(fos);
		} catch (Exception e) {
			log.error("throw Exception:", e);
			throw new BizException("导出失败!");
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (Exception e) {
				log.error("throw Exception:", e);
			}
			try {
				if (fos != null)
					fos.close();
			} catch (Exception e) {
				log.error("throw Exception:", e);
			}
		}
		return CommonUtil.getReturnPageFileName();
	}
	
	
	private void writeExcel(HSSFWorkbook workbook, HSSFSheet sheet,
			ArrayList<HashMap<String, Object>> list) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		HSSFCellStyle cellStyle = getGeneralStyle(workbook);
		int rowNum = 2;
		if (list != null && list.size() > 0 && sheet != null) {
			for(HashMap<String, Object> map : list) {
				int column = 0;
				rowNum = createHSSFRow(sheet, cellStyle, rowNum, map, column);
			}
		}
	}

	private int createHSSFRow(HSSFSheet sheet, HSSFCellStyle cellStyle, int rowNum,
			HashMap<String, Object> map, int column) {
		HSSFRow row = sheet.createRow(++rowNum);
		createHSSFCell(cellStyle, map, column, row);
		return rowNum;
	}

	private void createHSSFCell(HSSFCellStyle cellStyle, HashMap<String, Object> map, int column,
			HSSFRow row) {
		HSSFCell cell1 = row.createCell(column++);
		cell1.setCellValue(map.get("DAY_OF_MONTH").toString());
		cell1.setCellStyle(cellStyle);
		
		HSSFCell cell2 = row.createCell(column++);
		cell2.setCellValue(map.get("HQ_CODE") == null ? "" : map.get("HQ_CODE").toString());
		cell2.setCellStyle(cellStyle);
		
		HSSFCell cell3 = row.createCell(column++);
		cell3.setCellValue(map.get("AREA_CODE") == null ? "" : map.get("AREA_CODE").toString());
		cell3.setCellStyle(cellStyle);
		
		HSSFCell cell4 = row.createCell(column++);
		cell4.setCellValue(map.get("DEPT_CODE").toString());
		cell4.setCellStyle(cellStyle);
		
		HSSFCell cell5 = row.createCell(column++);
		cell5.setCellValue(((BigDecimal)map.get("FULLTIME_EMP_NUM")).intValue());
		cell5.setCellStyle(cellStyle);
		
		HSSFCell cell6 = row.createCell(column++);
		cell6.setCellValue(((BigDecimal)map.get("NOT_FULLTIME_EMP_NUM")).intValue());
		cell6.setCellStyle(cellStyle);
		
		HSSFCell cell7 = row.createCell(column++);
		cell7.setCellValue(((BigDecimal)map.get("FULLTIME_SCHEDULING_NUM")).intValue());
		cell7.setCellStyle(cellStyle);
		
		HSSFCell cell8 = row.createCell(column++);
		cell8.setCellValue(((BigDecimal)map.get("FULLTIME_REST_NUM")).intValue());
		cell8.setCellStyle(cellStyle);
		
		HSSFCell cell9 = row.createCell(column++);
		cell9.setCellValue(((BigDecimal)map.get("NOT_FULLTIME_SCHEDULING_NUM")).intValue());
		cell9.setCellStyle(cellStyle);
		
		HSSFCell cell10 = row.createCell(column++);
		cell10.setCellValue(((BigDecimal)map.get("NOT_FULLTIME_REST_NUM")).intValue());
		cell10.setCellStyle(cellStyle);
		
		HSSFCell cell11 = row.createCell(column++);
		cell11.setCellValue(((BigDecimal)map.get("FULLTIME_NOT_SCHEDULING")).intValue());
		cell11.setCellStyle(cellStyle);
		
		HSSFCell cell12 = row.createCell(column++);
		cell12.setCellValue(((BigDecimal)map.get("NOT_FULLTIME_NOT_SCHEDULING")).intValue());
		cell12.setCellStyle(cellStyle);
		
		HSSFCell cell13 = row.createCell(column++);
		cell13.setCellValue(map.get("FULLTIME_SCHEDULING_RATE").toString());
		cell13.setCellStyle(cellStyle);
		
		HSSFCell cell14 = row.createCell(column++);
		cell14.setCellValue(map.get("NOT_FULLTIME_SCHEDULING_RATE").toString());
		cell14.setCellStyle(cellStyle);
		
		HSSFCell cell15 = row.createCell(column++);
		cell15.setCellValue(map.get("FULLTIME_PLANNING_WORK_RATE").toString());
		cell15.setCellStyle(cellStyle);
		
		HSSFCell cell16 = row.createCell(column++);
		cell16.setCellValue(map.get("NO_FULLTIME_PLAN_WORK_RATE").toString());
		cell16.setCellStyle(cellStyle);
	}
	
	private HSSFCellStyle getGeneralStyle(HSSFWorkbook workbook) {
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		return cellStyle;
	}


}
