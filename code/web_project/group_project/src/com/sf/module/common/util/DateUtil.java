package com.sf.module.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.sf.module.operation.util.CommonUtil;

public class DateUtil {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat();
	private static final DateFormatType DEFAULT_DATE_FORAMT_TYPE = DateFormatType.FULL_TIME;

	public static String formatDate(Date date, DateFormatType format) {
		applyPatter(format);
		return DATE_FORMAT.format(date);
	}

	public static String formatDate(Date date) {
		return formatDate(date, null);
	}

	public static Date parseDate(String date, DateFormatType format) throws ParseException {
		applyPatter(format);
		return DATE_FORMAT.parse(date);
	}

	public static Date parseDate(String date) throws ParseException {
		return parseDate(date, null);
	}

	public static int getMonthSpace(Date date1, Date date2) throws ParseException {
		int result = 0;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		int year1 = c1.get(Calendar.YEAR);
		int year2 = c2.get(Calendar.YEAR);
		result = c1.get(Calendar.MONTH) - c2.get(Calendar.MONDAY);
		return Math.abs(result + (year1 - year2) * 12);
	}

	public static int getDaysOfMonth(int year, int month) {
		int days = 0;
		if (month != 2) {
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				days = 31;
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				days = 30;
			}
		} else {
			if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
				days = 29;
			else
				days = 28;
		}
		return days;
	}

	private static void applyPatter(DateFormatType format) {
		if (format == null) {
			DATE_FORMAT.applyPattern(DEFAULT_DATE_FORAMT_TYPE.format);
		} else {
			DATE_FORMAT.applyPattern(format.format);
		}
	}

	public static Date timeCalculate(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}

	public static int getYearOfDate(Date date) {
		return getDateField(date, Calendar.YEAR);
	}

	public static int getMonthOfDate(Date date) {
		return getDateField(date, Calendar.MONTH) + 1;
	}

	public static int getDateField(Date date, int field) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(field);
	}

	public static boolean isBeOverdue(String yarsAndMonth) {
		if (yarsAndMonth.equals(CommonUtil.getLastMonthYm(new Date()))) {
			Calendar c = Calendar.getInstance();
			Date now = c.getTime();
			c.set(Calendar.DAY_OF_MONTH, 4);
			c.set(Calendar.HOUR_OF_DAY, 20);
			return now.after(c.getTime());
		}
		return false;
	}

	public static boolean validConfirmDate(String ym) {
		return ym.equals(CommonUtil.getNextMonthYm(new Date())) || ym.equals(CommonUtil.getYm(new Date())) || ym.equals(CommonUtil.getLastMonthYm(new Date()));
	}
}
