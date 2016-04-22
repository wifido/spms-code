/*    */ package com.sf.sftp.client.util;
/*    */ 
/*    */ import com.sf.sftp.client.infoBean.UploadStrategyInfoBean;
/*    */ 
/*    */ public class NotifyMessageUtil
/*    */ {
/*    */   public static final String ITEM_SEPARATOR = ",";
/*    */   public static final String EQUAL = "=";
/*    */   public static final String KEY_ZIP = "blZipCompress";
/*    */   public static final String KEY_MD5 = "blMd5";
/*    */   public static final String KEY_MD5_CODE = "strMd5Code";
/*    */   public static final String KEY_PATH = "strRemotePath";
/*    */   public static final String KEY_FILE_NAME = "strRemoteFileName";
/*    */ 
/*    */   public static String packNotifyMessage(UploadStrategyInfoBean uploadStrategyInfo)
/*    */   {
/* 25 */     if (uploadStrategyInfo == null) {
/* 26 */       return null;
/*    */     }
/*    */ 
/* 30 */     StringBuilder notifyMessage = new StringBuilder();
/* 31 */     notifyMessage.append("blZipCompress=" + uploadStrategyInfo.isBlZipCompress() + ",");
/* 32 */     notifyMessage.append("blMd5=" + uploadStrategyInfo.isBlMd5() + ",");
/*    */ 
/* 35 */     if ((uploadStrategyInfo.getStrMd5Code() != null) && (!"".equals(uploadStrategyInfo.getStrMd5Code()))) {
/* 36 */       notifyMessage.append("strMd5Code=" + uploadStrategyInfo.getStrMd5Code() + ",");
/*    */     }
/*    */ 
/* 40 */     if ((uploadStrategyInfo.getStrRemotePath() != null) && (!"".equals(uploadStrategyInfo.getStrRemotePath()))) {
/* 41 */       notifyMessage.append("strRemotePath=" + uploadStrategyInfo.getStrRemotePath() + ",");
/*    */     }
/* 43 */     notifyMessage.append("strRemoteFileName=" + uploadStrategyInfo.getStrRemoteFileName());
/*    */ 
/* 45 */     return new String(notifyMessage);
/*    */   }
/*    */ 
/*    */   public static UploadStrategyInfoBean unPackNotifyMessage(String notifyMessage)
/*    */   {
/* 53 */     if (notifyMessage == null) {
/* 54 */       return null;
/*    */     }
/*    */ 
/* 57 */     UploadStrategyInfoBean uploadStrategyInfo = new UploadStrategyInfoBean();
/* 58 */     String[] strItemArr = notifyMessage.split(",");
/*    */ 
/* 60 */     for (String strItem : strItemArr) {
/* 61 */       int index = strItem.indexOf("=");
/*    */ 
/* 63 */       if (0 < index) {
/* 64 */         String key = strItem.substring(0, index);
/* 65 */         String value = strItem.substring(index + 1);
/* 66 */         if ("blZipCompress".equals(key))
/* 67 */           uploadStrategyInfo.setBlZipCompress(Boolean.valueOf(value).booleanValue());
/* 68 */         else if ("blMd5".equals(key))
/* 69 */           uploadStrategyInfo.setBlMd5(Boolean.valueOf(value).booleanValue());
/* 70 */         else if ("strMd5Code".equals(key))
/* 71 */           uploadStrategyInfo.setStrMd5Code(value);
/* 72 */         else if ("strRemotePath".equals(key))
/* 73 */           uploadStrategyInfo.setStrRemotePath(value);
/* 74 */         else if ("strRemoteFileName".equals(key)) {
/* 75 */           uploadStrategyInfo.setStrRemoteFileName(value);
/*    */         }
/*    */       }
/*    */     }
/*    */ 
/* 80 */     return uploadStrategyInfo;
/*    */   }
/*    */ }

/* Location:           D:\build\code\web_project\group_project\WebContent\WEB-INF\lib\sfSftpClient-1.0.0.jar
 * Qualified Name:     com.sf.sftp.client.util.NotifyMessageUtil
 * JD-Core Version:    0.6.2
 */