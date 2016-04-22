package com.sf.module.esbinterface.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ESBBigFileUtil {

    public static String generateJournalId(String systemId, String dataType) {
        return generateId(systemId, dataType).toString();
    }

    public static StringBuilder generateId(String systemId, String dataType) {
        return new StringBuilder(systemId)
                .append("-")
                .append(getTimeStamp())
                .append("-")
                .append(dataType)
                .append("-")
                .append(new Random().nextInt(1000));
    }

    private static String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());
    }
}
