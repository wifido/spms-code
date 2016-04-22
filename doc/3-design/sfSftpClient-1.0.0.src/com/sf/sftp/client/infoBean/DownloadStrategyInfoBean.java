/*    */ package com.sf.sftp.client.infoBean;
/*    */ 
/*    */ import com.sf.sftp.client.util.NotifyMessageUtil;
/*    */ 
/*    */ public class DownloadStrategyInfoBean
/*    */ {
/*    */   private UploadStrategyInfoBean uploadStrategyInfo;
/*    */   private String downloadPath;
/*    */ 
/*    */   public UploadStrategyInfoBean getUploadStrategyInfo()
/*    */   {
/* 14 */     return this.uploadStrategyInfo;
/*    */   }
/*    */ 
/*    */   public void setUploadStrategyInfo(UploadStrategyInfoBean uploadStrategyInfo) {
/* 18 */     this.uploadStrategyInfo = uploadStrategyInfo;
/*    */   }
/*    */ 
/*    */   public void setUploadStrategyInfo(String notifyMessage)
/*    */   {
/* 26 */     this.uploadStrategyInfo = NotifyMessageUtil.unPackNotifyMessage(notifyMessage);
/*    */   }
/*    */ 
/*    */   public String getDownloadPath() {
/* 30 */     return this.downloadPath;
/*    */   }
/*    */ 
/*    */   public void setDownloadPath(String downloadPath) {
/* 34 */     this.downloadPath = downloadPath;
/*    */   }
/*    */ }

/* Location:           D:\build\code\web_project\group_project\WebContent\WEB-INF\lib\sfSftpClient-1.0.0.jar
 * Qualified Name:     com.sf.sftp.client.infoBean.DownloadStrategyInfoBean
 * JD-Core Version:    0.6.2
 */