package com.sf.module.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.sf.module.common.domain.Constants.IMPORT_SUCCESSFUL;
import static com.sf.module.common.util.Clock.now;
import static java.lang.String.valueOf;

public class StringUtil {
	
	public static final String OPERATION_DEPTCODE_FILTER = "OPERATION_DEPTCODE_FILTER"; 
	public static final String ZB_DEPTCODE_FILTER = "ZB_DEPTCODE_FILTER"; 
	public static final String JYBB_DEPTCODE_FILTER = "JYBB_DEPTCODE_FILTER"; 
	public static final String QB_DEPTCODE_FILTER = "QB_DEPTCODE_FILTER"; 
	public static final String FB_DEPTCODE_FILTER = "FB_DEPTCODE_FILTER"; 
	public static final String SPMS2SAP_SCHEDULE = "SPMS2SAP_SCHEDULE"; 
	public static final String SPMS2SAP_SCHEDULE_DESC = "记录手工推送排班数据给SAP的推送时间"; 
	
    private static final String YEAR_FORMAT_AS_YYYY = "yyyy";

    public static boolean isBlank(String target) {
        return target == null || "".equals(target);
    }

    public static boolean isNotBlank(String target) {
        return !isBlank(target);
    }

    public static String getThisYearAsYYYY() {
        return new SimpleDateFormat(YEAR_FORMAT_AS_YYYY).format(now());
    }

    public static String getThisYearWithSpecifyMonth(int month) {
        String thisYear = getThisYearAsYYYY();
        return thisYear + getMonthLeftPaddingWithZero(month);
    }

    public static String getMonthLeftPaddingWithZero(int month) {
        return leftPadding("0", valueOf(month), 2);
    }

    public static String leftPadding(String targetReplaceChar, String sourceChar, int maxLength) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0; index < maxLength - sourceChar.length(); index++) {
            stringBuilder.append(targetReplaceChar);
        }

        stringBuilder.append(sourceChar);
        return stringBuilder.toString();
    }

    public static String strReplace(String remark) {
        remark = remark.replace("'", "");
        remark = remark.replace("/", "");
        remark = remark.replace("<", "");
        remark = remark.replace(">", "");
        remark = remark.replace("%", "");
        remark = remark.replace("\"", "");
        return remark;
    }

    public static String extractHandlerResult(int successCount, int errorCount) {
        return String.format(IMPORT_SUCCESSFUL, successCount, errorCount);
    }

    public static String removeColon(String time) {
        return time.replace(":", "");
    }

    public static String removeQuotes(String value) {
        return value.replace("'", "");
    }

    public static String removeHorizontalLine(String value) {
        return value.replace("-", "");
    }

    public static String previousMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.MONTH, -1);

        String currentMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        return calendar.get(Calendar.YEAR) + StringUtil.leftPadding("0", currentMonth, 2);
    }
}
