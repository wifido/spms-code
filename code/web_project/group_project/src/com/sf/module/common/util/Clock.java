package com.sf.module.common.util;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.sf.module.common.domain.Constants.ONE_DAY;
import static com.sf.module.common.util.StringUtil.removeHorizontalLine;

public class Clock {

    public static Date now() {
        return new DateTime().toDate();
    }

    private static DateTime currentDateTime() {
    	return new DateTime();
    }
    
    public static int currentDayOfMonth() {
    	return currentDateTime().getDayOfMonth();
    }
    
    public static int currentMonthOfYear() {
		return currentDateTime().getMonthOfYear();
    }
    
    public static int currentHourOfDay() {
    	return currentDateTime().getHourOfDay();
    }
    
    public static int previousMonth() {
    	return currentDateTime().minusMonths(1).getMonthOfYear();
    }
    
    public static boolean isOverlap(TimeRange sourceRange, TimeRange targetRange) {
        return targetRange.endTime == sourceRange.endTime && targetRange.startTime == sourceRange.startTime;
    }

    public static Date formatWithYyyyMMdd(String dateString) {
        try {
            return new SimpleDateFormat("yyyyMMdd").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    public static int range(String firstDay, String lastDay) {
        Date first = formatWithYyyyMMdd(firstDay);
        Date last = formatWithYyyyMMdd(lastDay);
        return (int) ((last.getTime() - first.getTime()) / ONE_DAY) + 1;
    }

    public static String afterDay(String day, int afterDays) {
        Date date = formatWithYyyyMMdd(day);
        return new SimpleDateFormat("yyyyMMdd").format(new Date(date.getTime() + (afterDays - 1) * ONE_DAY));
    }

    public static List<String> allDateInRange(String startDay, String endDay) {
        String firstDay = removeHorizontalLine(startDay);
        String lastDay = removeHorizontalLine(endDay);
        int range = Clock.range(firstDay, lastDay);

        List<String> extensionHeader = newArrayList();
        try {
            DateTime firstDateTime = new DateTime(new SimpleDateFormat("yyyyMMdd").parse(firstDay).getTime());
            for (int index = 0; index < range; index++) {
                DateTime dateTime = firstDateTime.plusDays(index);
                extensionHeader.add(new SimpleDateFormat("yyyyMMdd").format(dateTime.toDate()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return extensionHeader;
    }

    public static class TimeRange {
        public final int endTime;
        public final int startTime;

        public TimeRange(int startTime, int endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

}
