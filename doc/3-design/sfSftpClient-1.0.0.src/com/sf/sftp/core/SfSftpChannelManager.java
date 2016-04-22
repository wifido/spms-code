/*    */ package com.sf.sftp.core;
/*    */ 
/*    */ import com.jcraft.jsch.Channel;
/*    */ import com.jcraft.jsch.ChannelSftp;
/*    */ import com.jcraft.jsch.JSch;
/*    */ import com.jcraft.jsch.JSchException;
/*    */ import com.jcraft.jsch.Session;
/*    */ import com.sf.sftp.core.exception.SfSftpServerHostConnectionException;
/*    */ import com.sf.sftp.core.infoBean.SftpServerInfoBean;
/*    */ import com.sf.sftp.core.util.SecretUtils;
/*    */ import com.sun.crypto.provider.SunJCE;
/*    */ import java.security.Security;
/*    */ import java.util.Properties;
/*    */ import org.apache.log4j.Logger;
/*    */ 
/*    */ public class SfSftpChannelManager
/*    */ {
/* 19 */   private static Logger log = Logger.getLogger(SfSftpChannelManager.class);
/*    */   public static final String CHANNEL_TYPE_SFTP = "sftp";
/*    */   public static final int DEFAULT_PORT = 22;
/*    */ 
/*    */   public static SfSftpChannel createChannel(SftpServerInfoBean sftpServerInfo, int timeout)
/*    */     throws SfSftpServerHostConnectionException
/*    */   {
/* 33 */     Security.addProvider(new SunJCE());
/*    */ 
/* 36 */     int ftpPort = 22;
/* 37 */     if ((sftpServerInfo.getPort() != null) && (!"".equals(sftpServerInfo.getPort()))) {
/* 38 */       ftpPort = Integer.valueOf(sftpServerInfo.getPort()).intValue();
/*    */     }
/*    */ 
/* 41 */     if (log.isInfoEnabled()) {
/* 42 */       log.info("start to request sftpServer information...");
/* 43 */       log.info("SFTP Server IP: " + sftpServerInfo.getHostIp() + ", Port: " + ftpPort);
/*    */     }
/*    */ 
/* 46 */     SfSftpChannel tc = null;
/*    */     try
/*    */     {
/* 49 */       Session session = new JSch().getSession(sftpServerInfo.getUserName(), sftpServerInfo.getHostIp(), ftpPort);
/*    */ 
/* 51 */       if (sftpServerInfo.getPassword() != null) {
/* 52 */         if (sftpServerInfo.isEncryptFlg())
/* 53 */           session.setPassword(new SecretUtils().getDesString(sftpServerInfo.getPassword()));
/*    */         else {
/* 55 */           session.setPassword(sftpServerInfo.getPassword());
/*    */         }
/*    */       }
/*    */ 
/* 59 */       Properties config = new Properties();
/* 60 */       config.put("StrictHostKeyChecking", "no");
/* 61 */       session.setConfig(config);
/* 62 */       session.setTimeout(timeout);
/* 63 */       session.connect();
/*    */ 
/* 65 */       Channel channel = session.openChannel("sftp");
/* 66 */       channel.connect();
/*    */ 
/* 68 */       tc = new SfSftpChannel();
/* 69 */       tc.setChannel((ChannelSftp)channel);
/* 70 */       tc.setSession(session);
/*    */     } catch (JSchException e) {
/* 72 */       throw new SfSftpServerHostConnectionException(e.getCause());
/*    */     }
/*    */ 
/* 75 */     return tc;
/*    */   }
/*    */ }

/* Location:           D:\build\code\web_project\group_project\WebContent\WEB-INF\lib\sfSftpClient-1.0.0.jar
 * Qualified Name:     com.sf.sftp.core.SfSftpChannelManager
 * JD-Core Version:    0.6.2
 */