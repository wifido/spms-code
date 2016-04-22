package com.sf.module.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public enum DateFormatType {
	FULL_TIME("yyyy-MM-dd HH:mm:ss"),
	yyyy_MM_dd("yyyy-MM-dd"), 
	yyyy_MM_dd_with_slash("yyyy/MM/dd"), 
	yyyy_MM_dd_HHmm("yyyy-MM-dd HH:mm"), 
	yyyy_MM("yyyy-MM"), 
	yyyyMMdd_HHmmss("yyyyMMdd_HHmmss"), 
	DDHH("ddHH"),
    yyyyMMddHHmm("yyyyMMddHHmm"),
    yyyyMMdd("yyyyMMdd"),
    yyyyMM("yyyyMM");

    public final String format;

    DateFormatType(String format) {
        this.format = format;
    }

    public String formatDateAsString(Date date) {
        return new SimpleDateFormat(format).format(date);
    }
}
