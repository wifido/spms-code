package com.sf.module.esbinterface.fileutil;

import com.sf.module.esbinterface.util.Constants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.UUID;

public class ZipUtil {
	private static Log logger = LogFactory
            .getLog(ZipUtil.class);
    public static File doZip(String zipDirectory) throws IOException {
        ZipOutputStream zipOut;
        File zipDir = new File(zipDirectory);
        String UPLOAD_FILE_NAME = Constants.FILE_PREFIX + UUID.randomUUID().toString() + ".zip";

        try {
            String parent = new File(zipDirectory).getParentFile().getPath();

            zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(parent + File.separator + UPLOAD_FILE_NAME)));
            handleDir(zipDir, zipOut);
            zipOut.close();

            FileUtil.deleteDirectoryFiles(zipDirectory);

            File zipFile = new File(zipDir, UPLOAD_FILE_NAME);
            FileUtil.fileChannelCopy(new File(parent + File.separator + UPLOAD_FILE_NAME), zipFile);

            return zipFile;
        } catch (IOException ioe) {
            throw ioe;
        }
    }
    
    // 压缩Excel 文件
    public static String doZipExcel(String zipDirectory, String dateTime, String templetName) throws IOException {
        ZipOutputStream zipOut = null;
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        
        logger.info("压缩的文件全路径:" + zipDirectory);
        // 压缩的文件夹路径
        File zipDir = new File(new File(zipDirectory).getParentFile().getPath());
        
        // 设置导出压缩文件名称
        String fileName = zipDir.getPath().substring(zipDir.getPath().lastIndexOf(File.separator) + 1);
        String UPLOAD_FILE_NAME =  fileName + templetName + ".zip";
        
        logger.info("生成的压缩文件名称:" + UPLOAD_FILE_NAME);

        try {
            String parent = new File(zipDir.getPath()).getParentFile().getPath();
            fileOutputStream = new FileOutputStream(parent + File.separator + UPLOAD_FILE_NAME);
            
            logger.info("复制文件夹的路径/文件路径:" + zipDir.getPath() + File.separator + UPLOAD_FILE_NAME);
            
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			zipOut = new ZipOutputStream(bufferedOutputStream);
            handleDir(zipDir, zipOut);
            zipOut.close();

            logger.info("下载压缩文件路径" + parent + File.separator + UPLOAD_FILE_NAME);
            return parent + File.separator + UPLOAD_FILE_NAME;
        } catch (IOException ioe) {
        	logger.error("压缩的文件全路径:" + zipDirectory);
            throw ioe;
        } finally {
        	if(zipOut != null){  
        		zipOut.close();  
            }
        	
        	if(bufferedOutputStream != null){  
        		bufferedOutputStream.close();  
            }
        	
        	if(fileOutputStream != null){  
        		fileOutputStream.close();  
            }
        }
    }

    private static void handleDir(File dir, ZipOutputStream zipOut) throws IOException {
        int readedBytes;
        byte[] buf = new byte[1024];
        FileInputStream fileIn = null;
        File[] files;

        files = dir.listFiles();

        if (files.length == 0) {//如果目录为空,则单独创建之.
            zipOut.putNextEntry(new ZipEntry(dir.toString() + "/"));
            zipOut.closeEntry();
        } else {//如果目录不为空,则分别处理目录和文件.
            for (File fileName : files) {
                if (fileName.isDirectory()) {
                    handleDir(fileName, zipOut);
                } else {
                    fileIn = new FileInputStream(fileName);
                    zipOut.putNextEntry(new ZipEntry(fileName.getName().toString()));

                    while ((readedBytes = fileIn.read(buf)) > 0) {
                        zipOut.write(buf, 0, readedBytes);
                    }

                    zipOut.closeEntry();
                }
            }
        }
        
        if (fileIn != null) {
        	fileIn.close();
        }
    }

    public static void main(String[] args) {
        try {
            ZipUtil.doZip("/Users/staff/Developer/dev-spms-core/trunk/code/web_project/spms/out/artifacts/group_project_war_exploded/WEB-INF/classes/sendFile");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
