/*     */ package com.sf.sftp.core;
/*     */ 
/*     */ import com.sf.sftp.core.exception.SfSftGetSftpServerInformationException;
/*     */ import com.sf.sftp.core.exception.SfSftpESBHostConnectionException;
/*     */ import com.sf.sftp.core.infoBean.SftpServerInfoBean;
/*     */ import com.sf.sftp.core.util.SftpServerInfoAnalyzeUtil;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ public class GetSftpServerInfoService
/*     */ {
/*  27 */   private static final Log log = LogFactory.getLog(GetSftpServerInfoService.class);
/*     */ 
/*     */   public static SftpServerInfoBean getSftpServerInfoByTcp(String strEsbHostIp, String strPort, String reqContext)
/*     */   {
/*  40 */     if (log.isInfoEnabled()) {
/*  41 */       log.info("start to request sftpServer information...");
/*  42 */       log.info("Esb host IP: " + strEsbHostIp + ", Port: " + strPort);
/*     */     }
/*     */ 
/*  45 */     SftpServerInfoBean sftpServerInfo = null;
/*  46 */     Socket s = null;
/*  47 */     OutputStream os = null;
/*  48 */     DataOutputStream dos = null;
/*  49 */     InputStream is = null;
/*     */     try {
/*  51 */       s = new Socket(strEsbHostIp, Integer.parseInt(strPort));
/*  52 */       os = s.getOutputStream();
/*  53 */       dos = new DataOutputStream(os);
/*  54 */       dos.write(reqContext.getBytes("utf-8"));
/*  55 */       dos.flush();
/*  56 */       s.shutdownOutput();
/*     */ 
/*  58 */       is = s.getInputStream();
/*  59 */       byte[] buf = new byte[1024];
/*  60 */       int len = is.read(buf);
/*  61 */       String strSftpServerInfo = new String(buf, 0, len);
/*     */ 
/*  64 */       sftpServerInfo = SftpServerInfoAnalyzeUtil.analyzeSftpServerInfo(strSftpServerInfo);
/*     */ 
/*  66 */       is.close();
/*  67 */       dos.close();
/*  68 */       s.close();
/*     */     }
/*     */     catch (UnknownHostException e)
/*     */     {
/*  72 */       throw new SfSftpESBHostConnectionException(e.getCause());
/*     */     } catch (IOException e) {
/*  74 */       throw new SfSftpESBHostConnectionException(e.getCause());
/*     */     } finally {
/*     */       try {
/*  77 */         if (os != null) {
/*  78 */           os.close();
/*     */         }
/*     */ 
/*  81 */         if (is != null) {
/*  82 */           is.close();
/*     */         }
/*     */ 
/*  85 */         if (s != null)
/*  86 */           s.close();
/*     */       }
/*     */       catch (IOException e) {
/*  89 */         if (log.isInfoEnabled()) {
/*  90 */           log.error(e);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  96 */     if ((sftpServerInfo == null) || (sftpServerInfo.getHostIp() == null)) {
/*  97 */       throw new SfSftGetSftpServerInformationException();
/*     */     }
/*     */ 
/* 100 */     return sftpServerInfo;
/*     */   }
/*     */ }

/* Location:           D:\build\code\web_project\group_project\WebContent\WEB-INF\lib\sfSftpClient-1.0.0.jar
 * Qualified Name:     com.sf.sftp.core.GetSftpServerInfoService
 * JD-Core Version:    0.6.2
 */