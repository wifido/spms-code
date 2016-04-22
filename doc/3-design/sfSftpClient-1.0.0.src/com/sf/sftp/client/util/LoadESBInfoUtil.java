/*    */ package com.sf.sftp.client.util;
/*    */ 
/*    */ import com.sf.sftp.client.impl.SftpClientImpl;
/*    */ import com.sf.sftp.client.infoBean.ESBInfoProperties;
/*    */ import com.sf.sftp.core.exception.SfSftpLoadESBInfoPropertiesException;
/*    */ import java.io.IOException;
/*    */ import java.util.Properties;
/*    */ import org.apache.log4j.Logger;
/*    */ 
/*    */ public class LoadESBInfoUtil
/*    */ {
/* 15 */   private static Logger log = Logger.getLogger(SftpClientImpl.class);
/*    */ 
/* 18 */   private static String resourceFile = "/ESBInfo.properties";
/*    */   public static final String ESB_INFO_PROP_IP = "ip";
/*    */   public static final String ESB_INFO_PROP_REQ_SFTP_INFO_PORT = "reqSftpInfoPort";
/*    */   public static final String ESB_INFO_PROP_NOTIFY_PORT = "notifyPort";
/*    */   public static final String ESB_INFO_PROP_MESSAGE = "message";
/* 28 */   private static ESBInfoProperties esbInfoProperties = null;
/*    */ 
/*    */   public static ESBInfoProperties getESBInfoProperties()
/*    */   {
/* 36 */     refreshESBInfoProperties();
/*    */ 
/* 38 */     if (esbInfoProperties == null)
/*    */     {
/* 40 */       if (log.isInfoEnabled()) {
/* 41 */         log.info("Start to Load ESB Information Configuration.....");
/*    */       }
/*    */ 
/* 44 */       Properties props = new Properties();
/*    */       try
/*    */       {
/* 47 */         props.load(LoadESBInfoUtil.class.getResourceAsStream(resourceFile));
/*    */       } catch (IOException e) {
/* 49 */         throw new SfSftpLoadESBInfoPropertiesException(e.getCause());
/*    */       }
/*    */ 
/* 52 */       esbInfoProperties = new ESBInfoProperties();
/* 53 */       esbInfoProperties.setIp(props.get("ip").toString());
/* 54 */       esbInfoProperties.setReqSftpInfoPort(props.get("reqSftpInfoPort").toString());
/* 55 */       esbInfoProperties.setNotifyPort(props.get("notifyPort").toString());
/* 56 */       esbInfoProperties.setMessage(props.get("message").toString());
/*    */     }
/*    */ 
/* 59 */     return esbInfoProperties;
/*    */   }
/*    */ 
/*    */   public static void refreshESBInfoProperties()
/*    */   {
/* 66 */     esbInfoProperties = null;
/*    */   }
/*    */ 
/*    */   public static void setResourceFile(String resourceFile) {
/* 70 */     resourceFile = resourceFile;
/* 71 */     refreshESBInfoProperties();
/*    */   }
/*    */ }

/* Location:           D:\build\code\web_project\group_project\WebContent\WEB-INF\lib\sfSftpClient-1.0.0.jar
 * Qualified Name:     com.sf.sftp.client.util.LoadESBInfoUtil
 * JD-Core Version:    0.6.2
 */