/*     */ package com.sf.sftp.client.impl;
/*     */ 
/*     */ import com.jcraft.jsch.SftpException;
/*     */ import com.sf.sftp.client.ISftpClient;
/*     */ import com.sf.sftp.client.infoBean.DownloadStrategyInfoBean;
/*     */ import com.sf.sftp.client.infoBean.ESBInfoProperties;
/*     */ import com.sf.sftp.client.infoBean.UploadStrategyInfoBean;
/*     */ import com.sf.sftp.client.util.LoadESBInfoUtil;
/*     */ import com.sf.sftp.client.util.NotifyMessageUtil;
/*     */ import com.sf.sftp.core.GetSftpServerInfoService;
/*     */ import com.sf.sftp.core.SendNotifyMessageService;
/*     */ import com.sf.sftp.core.SfSftpChannel;
/*     */ import com.sf.sftp.core.SfSftpChannelManager;
/*     */ import com.sf.sftp.core.infoBean.SftpServerInfoBean;
/*     */ import com.sf.sftp.core.util.Md5Util;
/*     */ import com.sf.sftp.core.util.ZipUtil;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ public class SftpClientImpl
/*     */   implements ISftpClient
/*     */ {
/*  38 */   private static Logger log = Logger.getLogger(SftpClientImpl.class);
/*  39 */   private int TIME_OUT = 50000;
/*  40 */   private final String dateFormat = "yyyyMMdd";
/*     */ 
/*     */   public boolean upload(File file, UploadStrategyInfoBean uploadStrategyInfo)
/*     */   {
/*  47 */     if ((file == null) || (!file.exists())) {
/*  48 */       log.error("文件不存在，上传失败!");
/*  49 */       return false;
/*     */     }
/*     */ 
/*  53 */     if (file.isDirectory()) {
/*  54 */       uploadStrategyInfo.setBlZipCompress(true);
/*     */     }
/*     */ 
/*  58 */     File uploadFile = uploadStrategyProcess(file, uploadStrategyInfo);
/*     */ 
/*  60 */     if (!uploadFile.exists()) {
/*  61 */       log.error("文件不存在，上传失败!");
/*  62 */       return false;
/*     */     }
/*     */ 
/*  65 */     return putFileToServer(uploadFile, uploadStrategyInfo);
/*     */   }
/*     */ 
/*     */   private boolean putFileToServer(File uploadFile, UploadStrategyInfoBean uploadStrategyInfo)
/*     */   {
/*  78 */     ESBInfoProperties esbInfo = LoadESBInfoUtil.getESBInfoProperties();
/*     */ 
/*  81 */     SftpServerInfoBean sftpServerInfo = GetSftpServerInfoService.getSftpServerInfoByTcp(esbInfo.getIp(), esbInfo.getReqSftpInfoPort(), esbInfo.getMessage());
/*     */ 
/*  84 */     if (sftpServerInfo.isDayDirectoryEnable()) {
/*  85 */       uploadStrategyInfo.setStrRemotePath(getNewRemotePath(uploadStrategyInfo.getStrRemotePath()));
/*     */     }
/*     */ 
/*  89 */     SfSftpChannel sfSftpChannel = SfSftpChannelManager.createChannel(sftpServerInfo, this.TIME_OUT);
/*  90 */     boolean result = sfSftpChannel.put(uploadFile, uploadStrategyInfo);
/*  91 */     sfSftpChannel.close();
/*     */ 
/*  94 */     deleteTempFile(uploadFile, uploadStrategyInfo.isBlZipCompress());
/*     */ 
/*  96 */     return result;
/*     */   }
/*     */ 
/*     */   private String getNewRemotePath(String remotePath)
/*     */   {
/* 105 */     SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
/* 106 */     simpleDateFormat.applyPattern("yyyyMMdd");
/*     */ 
/* 108 */     String newRemotePath = simpleDateFormat.format(new Date());
/* 109 */     if (remotePath != null) {
/* 110 */       newRemotePath = newRemotePath + "/" + remotePath;
/*     */     }
/*     */ 
/* 113 */     return newRemotePath;
/*     */   }
/*     */ 
/*     */   private void deleteTempFile(File uploadFile, boolean isZip)
/*     */   {
/* 123 */     if (isZip)
/* 124 */       uploadFile.delete();
/*     */   }
/*     */ 
/*     */   private File uploadStrategyProcess(File file, UploadStrategyInfoBean uploadStrategyInfo)
/*     */   {
/* 137 */     File destFile = file;
/*     */ 
/* 140 */     if (uploadStrategyInfo.isBlZipCompress()) {
/* 141 */       if ((uploadStrategyInfo.getStrRemoteFileName() == null) || ("".equals(uploadStrategyInfo.getStrRemoteFileName())))
/* 142 */         destFile = ZipUtil.zip(file);
/*     */       else {
/* 144 */         destFile = ZipUtil.zip(file, uploadStrategyInfo.getStrRemoteFileName());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 149 */     if (uploadStrategyInfo.isBlMd5()) {
/* 150 */       uploadStrategyInfo.setStrMd5Code(Md5Util.generateMd5Code(destFile));
/*     */     }
/*     */ 
/* 154 */     if (uploadStrategyInfo.getStrRemoteFileName() == null) {
/* 155 */       uploadStrategyInfo.setStrRemoteFileName(destFile.getName());
/*     */     }
/*     */ 
/* 158 */     return destFile;
/*     */   }
/*     */ 
/*     */   public boolean upload(List<File> fileList, UploadStrategyInfoBean uploadStrategyInfo)
/*     */   {
/* 164 */     if ((fileList == null) || (fileList.isEmpty())) {
/* 165 */       log.error("文件不存在，上传失败!");
/* 166 */       return false;
/*     */     }
/*     */ 
/* 170 */     if (fileList.size() == 1) {
/* 171 */       return upload((File)fileList.get(0), uploadStrategyInfo);
/*     */     }
/*     */ 
/* 174 */     File uploadFile = uploadStrategyProcess(fileList, uploadStrategyInfo);
/*     */ 
/* 177 */     if (!uploadFile.exists()) {
/* 178 */       log.error("文件不存在，上传失败!");
/* 179 */       return false;
/*     */     }
/*     */ 
/* 182 */     return putFileToServer(uploadFile, uploadStrategyInfo);
/*     */   }
/*     */ 
/*     */   private File uploadStrategyProcess(List<File> fileList, UploadStrategyInfoBean uploadStrategyInfo)
/*     */   {
/* 196 */     uploadStrategyInfo.setBlZipCompress(true);
/* 197 */     File destFile = ZipUtil.zip(fileList, uploadStrategyInfo.getStrRemoteFileName());
/*     */ 
/* 200 */     if (uploadStrategyInfo.isBlMd5()) {
/* 201 */       uploadStrategyInfo.setStrMd5Code(Md5Util.generateMd5Code(destFile));
/*     */     }
/*     */ 
/* 205 */     if (uploadStrategyInfo.getStrRemoteFileName() == null) {
/* 206 */       uploadStrategyInfo.setStrRemoteFileName(destFile.getName());
/*     */     }
/*     */ 
/* 209 */     return destFile;
/*     */   }
/*     */ 
/*     */   public List<File> downLoad(DownloadStrategyInfoBean downloadStrategyInfo)
/*     */   {
/* 214 */     if ((downloadStrategyInfo == null) || 
/* 215 */       (downloadStrategyInfo.getUploadStrategyInfo() == null)) {
/* 216 */       return null;
/*     */     }
/*     */ 
/* 219 */     List listFile = null;
/* 220 */     InputStream is = null;
/* 221 */     OutputStream os = null;
/* 222 */     File localFile = null;
/*     */     try
/*     */     {
/* 226 */       ESBInfoProperties esbInfo = LoadESBInfoUtil.getESBInfoProperties();
/*     */ 
/* 229 */       SftpServerInfoBean sftpServerInfo = GetSftpServerInfoService.getSftpServerInfoByTcp(esbInfo.getIp(), esbInfo.getReqSftpInfoPort(), esbInfo.getMessage());
/*     */ 
/* 232 */       SfSftpChannel sfSftpChannel = SfSftpChannelManager.createChannel(sftpServerInfo, this.TIME_OUT);
/* 233 */       localFile = sfSftpChannel.get(downloadStrategyInfo);
/* 234 */       sfSftpChannel.close();
/*     */ 
/* 236 */       UploadStrategyInfoBean uploadStrategyInfo = downloadStrategyInfo.getUploadStrategyInfo();
/*     */ 
/* 239 */       if ((uploadStrategyInfo.isBlMd5()) && 
/* 240 */         (!Md5Util.checkMd5Code(localFile, uploadStrategyInfo.getStrMd5Code())))
/*     */       {
/* 242 */         log.error("The file has been changed by others!");
/*     */ 
/* 245 */         if (localFile != null) {
/* 246 */           localFile.delete();
/*     */         }
/*     */ 
/* 249 */         return null;
/*     */       }
/*     */       UploadStrategyInfoBean uploadStrategyInfo;
/*     */       SfSftpChannel sfSftpChannel;
/*     */       SftpServerInfoBean sftpServerInfo;
/*     */       ESBInfoProperties esbInfo;
/* 254 */       if (uploadStrategyInfo.isBlZipCompress()) {
/* 255 */         listFile = ZipUtil.unZip(localFile, downloadStrategyInfo.getDownloadPath());
/*     */ 
/* 258 */         if (localFile != null)
/* 259 */           localFile.delete();
/*     */       }
/*     */       else {
/* 262 */         listFile = new ArrayList();
/* 263 */         listFile.add(localFile);
/*     */       }
/*     */     } catch (ZipException e) {
/* 266 */       log.error(e.getMessage());
/* 267 */       return null;
/*     */     }
/*     */     finally {
/*     */       try {
/* 271 */         if (is != null)
/* 272 */           is.close();
/* 273 */         if (os != null)
/* 274 */           os.close();
/*     */       } catch (IOException e) {
/* 276 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 271 */       if (is != null)
/* 272 */         is.close();
/* 273 */       if (os != null)
/* 274 */         os.close();
/*     */     } catch (IOException e) {
/* 276 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 280 */     return listFile;
/*     */   }
/*     */ 
/*     */   public boolean delete(String remotePath)
/*     */   {
/* 286 */     ESBInfoProperties esbInfo = LoadESBInfoUtil.getESBInfoProperties();
/*     */ 
/* 289 */     SftpServerInfoBean sftpServerInfo = GetSftpServerInfoService.getSftpServerInfoByTcp(esbInfo.getIp(), esbInfo.getReqSftpInfoPort(), esbInfo.getMessage());
/*     */     try
/*     */     {
/* 292 */       SfSftpChannel sfSftpChannel = SfSftpChannelManager.createChannel(sftpServerInfo, this.TIME_OUT);
/* 293 */       sfSftpChannel.delete(remotePath);
/* 294 */       sfSftpChannel.close();
/*     */     } catch (SftpException e) {
/* 296 */       return false;
/*     */     }
/*     */ 
/* 299 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean mkdir(String dir)
/*     */   {
/* 305 */     ESBInfoProperties esbInfo = LoadESBInfoUtil.getESBInfoProperties();
/*     */ 
/* 308 */     SftpServerInfoBean sftpServerInfo = GetSftpServerInfoService.getSftpServerInfoByTcp(esbInfo.getIp(), esbInfo.getReqSftpInfoPort(), esbInfo.getMessage());
/*     */     try
/*     */     {
/* 311 */       SfSftpChannel sfSftpChannel = SfSftpChannelManager.createChannel(sftpServerInfo, this.TIME_OUT);
/* 312 */       sfSftpChannel.mkdir(dir);
/* 313 */       sfSftpChannel.close();
/*     */     } catch (SftpException e) {
/* 315 */       return false;
/*     */     }
/*     */ 
/* 318 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean rmdir(String dir)
/*     */   {
/* 324 */     ESBInfoProperties esbInfo = LoadESBInfoUtil.getESBInfoProperties();
/*     */ 
/* 327 */     SftpServerInfoBean sftpServerInfo = GetSftpServerInfoService.getSftpServerInfoByTcp(esbInfo.getIp(), esbInfo.getReqSftpInfoPort(), esbInfo.getMessage());
/*     */     try
/*     */     {
/* 330 */       SfSftpChannel sfSftpChannel = SfSftpChannelManager.createChannel(sftpServerInfo, this.TIME_OUT);
/* 331 */       sfSftpChannel.rmdir(dir);
/* 332 */       sfSftpChannel.close();
/*     */     } catch (SftpException e) {
/* 334 */       return false;
/*     */     }
/*     */ 
/* 337 */     return true;
/*     */   }
/*     */ 
/*     */   public Vector<?> lsDir(String dir)
/*     */   {
/* 343 */     ESBInfoProperties esbInfo = LoadESBInfoUtil.getESBInfoProperties();
/*     */ 
/* 346 */     SftpServerInfoBean sftpServerInfo = GetSftpServerInfoService.getSftpServerInfoByTcp(esbInfo.getIp(), esbInfo.getReqSftpInfoPort(), esbInfo.getMessage());
/*     */     try
/*     */     {
/* 349 */       SfSftpChannel sfSftpChannel = SfSftpChannelManager.createChannel(sftpServerInfo, this.TIME_OUT);
/* 350 */       Vector dirVector = sfSftpChannel.lsDir(dir);
/* 351 */       sfSftpChannel.close();
/*     */ 
/* 353 */       return dirVector; } catch (SftpException e) {
/*     */     }
/* 355 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean rename(String oldName, String newName)
/*     */   {
/* 362 */     ESBInfoProperties esbInfo = LoadESBInfoUtil.getESBInfoProperties();
/*     */ 
/* 365 */     SftpServerInfoBean sftpServerInfo = GetSftpServerInfoService.getSftpServerInfoByTcp(esbInfo.getIp(), esbInfo.getReqSftpInfoPort(), esbInfo.getMessage());
/*     */     try
/*     */     {
/* 368 */       SfSftpChannel sfSftpChannel = SfSftpChannelManager.createChannel(sftpServerInfo, this.TIME_OUT);
/* 369 */       sfSftpChannel.rename(oldName, newName);
/* 370 */       sfSftpChannel.close();
/*     */     } catch (SftpException e) {
/* 372 */       return false;
/*     */     }
/*     */ 
/* 375 */     return true;
/*     */   }
/*     */ 
/*     */   public String sendNotifyMessage(UploadStrategyInfoBean uploadStrategyInfo)
/*     */   {
/* 387 */     ESBInfoProperties esbInfo = LoadESBInfoUtil.getESBInfoProperties();
/*     */ 
/* 390 */     return SendNotifyMessageService.send(esbInfo.getIp(), esbInfo.getNotifyPort(), NotifyMessageUtil.packNotifyMessage(uploadStrategyInfo));
/*     */   }
/*     */ }

/* Location:           D:\build\code\web_project\group_project\WebContent\WEB-INF\lib\sfSftpClient-1.0.0.jar
 * Qualified Name:     com.sf.sftp.client.impl.SftpClientImpl
 * JD-Core Version:    0.6.2
 */