/*     */ package com.sf.sftp.core.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.lingala.zip4j.core.ZipFile;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import net.lingala.zip4j.model.FileHeader;
/*     */ import net.lingala.zip4j.model.ZipParameters;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ public class ZipUtil
/*     */ {
/*  17 */   private static Logger log = Logger.getLogger(ZipUtil.class);
/*     */   public static final String EXT_ZIP = ".zip";
/*     */ 
/*     */   public static File zip(File file)
/*     */   {
/*  27 */     return zip(file, file.getName() + ".zip");
/*     */   }
/*     */ 
/*     */   public static File zip(File file, String strDestFileName)
/*     */   {
/*  37 */     List files = new ArrayList();
/*  38 */     files.add(file);
/*     */ 
/*  40 */     if ((strDestFileName == null) || (strDestFileName.trim() == ""))
/*  41 */       strDestFileName = file.getName() + ".zip";
/*  42 */     else if (!strDestFileName.endsWith(".zip")) {
/*  43 */       strDestFileName = strDestFileName + ".zip";
/*     */     }
/*     */ 
/*  46 */     return zip(files, new File(strDestFileName));
/*     */   }
/*     */ 
/*     */   public static File zip(List<File> fileList, String strDestFileName)
/*     */   {
/*  57 */     if ((fileList == null) || (fileList.isEmpty())) {
/*  58 */       return null;
/*     */     }
/*     */ 
/*  61 */     if ((strDestFileName == null) || (strDestFileName.trim() == ""))
/*  62 */       strDestFileName = ((File)fileList.get(0)).getName() + ".zip";
/*  63 */     else if (!strDestFileName.endsWith(".zip")) {
/*  64 */       strDestFileName = strDestFileName + ".zip";
/*     */     }
/*     */ 
/*  67 */     return zip(fileList, new File(strDestFileName));
/*     */   }
/*     */ 
/*     */   private static File zip(List<File> fileList, File destFile)
/*     */   {
/*  72 */     if (destFile.exists()) destFile.delete();
/*     */ 
/*  74 */     ZipParameters parameters = new ZipParameters();
/*  75 */     parameters.setCompressionMethod(8);
/*  76 */     parameters.setCompressionLevel(5);
/*     */ 
/*  78 */     ZipFile zipFile1 = null;
/*     */     try {
/*  80 */       zipFile1 = new ZipFile(destFile);
/*  81 */       for (File f : fileList) {
/*  82 */         if (f.isDirectory())
/*  83 */           zipFile1.addFolder(f, parameters);
/*  84 */         else if (f.isFile())
/*  85 */           zipFile1.addFile(f, parameters);
/*     */       }
/*     */     }
/*     */     catch (ZipException e)
/*     */     {
/*  90 */       log.error(e.getMessage());
/*     */ 
/*  93 */       destFile.delete();
/*     */ 
/*  95 */       return null;
/*     */     }
/*     */ 
/*  98 */     return zipFile1.getFile();
/*     */   }
/*     */ 
/*     */   public static List<File> unZip(File zipFile, String destPath)
/*     */     throws ZipException
/*     */   {
/* 112 */     if ((zipFile == null) || (!zipFile.exists())) {
/* 113 */       return null;
/*     */     }
/*     */ 
/* 117 */     if ((destPath == null) || (destPath == "")) {
/* 118 */       destPath = zipFile.getAbsoluteFile().getParent();
/*     */     }
/*     */ 
/* 121 */     ZipFile zFile = new ZipFile(zipFile);
/* 122 */     zFile.setFileNameCharset("GBK");
/* 123 */     if (!zFile.isValidZipFile()) {
/* 124 */       throw new ZipException("压缩文件不合法,可能被损坏.");
/*     */     }
/*     */ 
/* 127 */     File destDir = new File(destPath);
/* 128 */     if (!destDir.exists()) {
/* 129 */       destDir.mkdirs();
/*     */     }
/* 131 */     zFile.extractAll(destPath);
/*     */ 
/* 133 */     List headerList = zFile.getFileHeaders();
/* 134 */     List extractedFileList = new ArrayList();
/*     */ 
/* 136 */     for (FileHeader fileHeader : headerList) {
/* 137 */       File file = new File(destDir, fileHeader.getFileName());
/* 138 */       if (destDir.equals(file.getParentFile())) {
/* 139 */         extractedFileList.add(file);
/*     */       }
/*     */     }
/*     */ 
/* 143 */     return extractedFileList;
/*     */   }
/*     */ }

/* Location:           D:\build\code\web_project\group_project\WebContent\WEB-INF\lib\sfSftpClient-1.0.0.jar
 * Qualified Name:     com.sf.sftp.core.util.ZipUtil
 * JD-Core Version:    0.6.2
 */