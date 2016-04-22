package com.sf.module.driver.biz;

import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.joda.time.DateTime;
import com.sf.module.common.util.AbstractExcelExportHandler;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;

public class DriverNotScheduledEmployeeExportHandler extends AbstractExcelExportHandler{
	private final static String DRIVER_NOT_SCHEDULING_EXPORT_NAME = "未排班人员信息";
	private static String DRIVER_NOT_SCHEDULING_EXPORT_TEMPLATE_NAME = "司机排班导入模板.xls";
	private static String yearOfWeek;

	@Override
	public int[] getStartRowNumber() {
		return new int[]{3};
	}

	@Override
	public int[] getHiddenRowNumber() {
		return new int[]{1};
	}

	@Override
	public String getDownloadName() {
		return DRIVER_NOT_SCHEDULING_EXPORT_NAME;
	}

	@Override
	public String getTemplateFileName() {
		return DRIVER_NOT_SCHEDULING_EXPORT_TEMPLATE_NAME;
	}

	@Override
	public void handleSpecialRow(HSSFSheet sheet, List<Map<String, Object>> list) {
		DateTime dateTime = new DateTime()
				.withYear(Integer.parseInt(yearOfWeek.split("-")[0]))
				.withWeekOfWeekyear(Integer.parseInt(yearOfWeek.split("-")[1]))
				.withDayOfWeek(1);
		HSSFRow row = sheet.getRow(0);
		row.getCell(1).setCellValue(
				DateUtil.formatDate(dateTime.toDate(),
						DateFormatType.yyyy_MM_dd));
	}

	public static String getYearOfWeek() {
		return yearOfWeek;
	}

	public static void setYearOfWeek(String yearOfWeek) {
		DriverNotScheduledEmployeeExportHandler.yearOfWeek = yearOfWeek;
	}
	
}
