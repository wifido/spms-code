package com.sf.module.esbinterface.fileutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;

public class FileUtil {
    private static Logger log = LoggerFactory.getLogger(FileUtil.class);

    public static void fileChannelCopy(File sourceFile, File toFile) {
        if (!sourceFile.isFile() || !sourceFile.exists()) {
            log.error("sourceFile is not a file or no source file exist");
            throw new RuntimeException("sourceFile is not a file or no source file exist");
        }
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        FileChannel fileInChannel = null;
        FileChannel fileOutChannel = null;

        try {
            fileInputStream = new FileInputStream(sourceFile);
            fileOutputStream = new FileOutputStream(toFile);

            fileInChannel = fileInputStream.getChannel();
            fileOutChannel = fileOutputStream.getChannel();
            fileInChannel.transferTo(0, fileInChannel.size(), fileOutChannel);
            sourceFile.delete();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            closeStreamQuietly(fileInputStream);
            closeChannelQuietly(fileInChannel);
            closeChannelQuietly(fileOutChannel);
            closeOutStreamQuietly(fileOutputStream);
        }
    }

    public static void closeStreamQuietly(InputStream is) {
        if (is == null) return;
        try {
            is.close();
        } catch (IOException ignored) {
        }
    }

    public static void closeOutStreamQuietly(FileOutputStream fileOutputStream) {
        if (fileOutputStream == null) return;
        try {
            fileOutputStream.close();
        } catch (IOException ignored) {
        }
    }

    public static void closeChannelQuietly(FileChannel fileChannel) {
        if (fileChannel != null) {
            try {
                fileChannel.close();
            } catch (IOException ignored) {
            }
        }
    }

    public static boolean deleteDirectoryFiles(String dir) {
        if (!dir.endsWith(File.separator)) dir = dir + File.separator;
        File dirFile = new File(dir);
        if (!dirFile.exists() || !dirFile.isDirectory()) return false;
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                flag = deleteDirectoryFiles(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }

        if (!flag) {
            log.error("delete dir fail ");
            return false;
        }

        return true;
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            file.delete();
            log.info("delete file:" + filePath + " success!");
            return true;
        } else {
            log.info("delete file:" + filePath + " failure!");
            return false;
        }
    }

}
