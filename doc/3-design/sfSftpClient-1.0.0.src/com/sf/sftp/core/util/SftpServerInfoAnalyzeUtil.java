/*    */ package com.sf.sftp.core.util;
/*    */ 
/*    */ import com.sf.sftp.core.infoBean.SftpServerInfoBean;
/*    */ 
/*    */ public class SftpServerInfoAnalyzeUtil
/*    */ {
/*    */   public static final String ITEM_SEPARATOR = "|";
/*    */   public static final String ITEM_SEPARATOR_REGULAR = "\\|";
/*    */   public static final String EQUAL = "=";
/*    */   public static final String KEY_IP = "ip";
/*    */   public static final String KEY_PORT = "port";
/*    */   public static final String KEY_USER_NAME = "username";
/*    */   public static final String KEY_PASSWORD = "passwd";
/*    */   public static final String KEY_PATH = "path";
/*    */   public static final String KEY_PASSWORD_ENCRYPT = "encryptFlg";
/*    */   public static final String KEY_DAY_DIRECTORY_ENABLE = "dayDirectoryEnable";
/*    */ 
/*    */   public static SftpServerInfoBean analyzeSftpServerInfo(String strSftpServerInfo)
/*    */   {
/* 28 */     if (strSftpServerInfo == null) {
/* 29 */       return null;
/*    */     }
/*    */ 
/* 32 */     SftpServerInfoBean sftpServerInfo = new SftpServerInfoBean();
/* 33 */     String[] strItemArr = strSftpServerInfo.split("\\|");
/*    */ 
/* 35 */     for (String strItem : strItemArr) {
/* 36 */       int index = strItem.indexOf("=");
/*    */ 
/* 38 */       if (0 < index) {
/* 39 */         String key = strItem.substring(0, index);
/* 40 */         String value = strItem.substring(index + 1);
/*    */ 
/* 42 */         if ("username".equals(key)) {
/* 43 */           sftpServerInfo.setUserName(value);
/*    */         }
/* 45 */         else if ("passwd".equals(key)) {
/* 46 */           sftpServerInfo.setPassword(value);
/*    */         }
/* 48 */         else if ("path".equals(key)) {
/* 49 */           sftpServerInfo.setPath(value);
/*    */         }
/* 51 */         else if ("ip".equals(key)) {
/* 52 */           sftpServerInfo.setHostIp(value);
/*    */         }
/* 54 */         else if ("port".equals(key)) {
/* 55 */           sftpServerInfo.setPort(value);
/*    */         }
/* 57 */         else if ("encryptFlg".equals(key))
/* 58 */           sftpServerInfo.setEncryptFlg(Boolean.valueOf(value).booleanValue());
/* 59 */         else if ("dayDirectoryEnable".equals(key)) {
/* 60 */           sftpServerInfo.setDayDirectoryEnable(Boolean.valueOf(value).booleanValue());
/*    */         }
/*    */       }
/*    */     }
/*    */ 
/* 65 */     return sftpServerInfo;
/*    */   }
/*    */ }

/* Location:           D:\build\code\web_project\group_project\WebContent\WEB-INF\lib\sfSftpClient-1.0.0.jar
 * Qualified Name:     com.sf.sftp.core.util.SftpServerInfoAnalyzeUtil
 * JD-Core Version:    0.6.2
 */