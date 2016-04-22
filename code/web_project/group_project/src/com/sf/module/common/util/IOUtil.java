package com.sf.module.common.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtil {
    public static void close(FileInputStream fileInputStream) {
        try {
            if (fileInputStream == null) {
                return;
            }
            fileInputStream.close();
        } catch (Exception e) {
//            log.error("error", e);
        }
    }

    public static void close(InputStream inputStream) {
        try {
            if (inputStream == null) {
                return;
            }
            inputStream.close();
        } catch (Exception e) {
//            log.error("error", e);
        }
    }

    public static void close(OutputStream fileOutputStream) {
        try {
            if (fileOutputStream == null) {
                return;
            }
            fileOutputStream.close();
        } catch (Exception e) {
//            log.error("error", e);
        }

    }
}
