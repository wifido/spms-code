/*    */ package com.sf.sftp.client.infoBean;
/*    */ 
/*    */ public class UploadStrategyInfoBean
/*    */ {
/*  6 */   private boolean blZipCompress = false;
/*    */ 
/*  9 */   private boolean blMd5 = false;
/*    */ 
/* 12 */   private boolean resume = false;
/*    */   private String strRemotePath;
/*    */   private String strRemoteFileName;
/*    */   private String strMd5Code;
/*    */ 
/*    */   public boolean isBlZipCompress()
/*    */   {
/* 24 */     return this.blZipCompress;
/*    */   }
/*    */ 
/*    */   public void setBlZipCompress(boolean blZipCompress) {
/* 28 */     this.blZipCompress = blZipCompress;
/*    */   }
/*    */ 
/*    */   public boolean isBlMd5() {
/* 32 */     return this.blMd5;
/*    */   }
/*    */ 
/*    */   public void setBlMd5(boolean blMd5) {
/* 36 */     this.blMd5 = blMd5;
/*    */   }
/*    */ 
/*    */   public boolean isResume() {
/* 40 */     return this.resume;
/*    */   }
/*    */ 
/*    */   public void setResume(boolean resume) {
/* 44 */     this.resume = resume;
/*    */   }
/*    */ 
/*    */   public String getStrRemotePath() {
/* 48 */     return this.strRemotePath;
/*    */   }
/*    */ 
/*    */   public void setStrRemotePath(String strRemotePath) {
/* 52 */     this.strRemotePath = strRemotePath;
/*    */   }
/*    */ 
/*    */   public String getStrMd5Code() {
/* 56 */     return this.strMd5Code;
/*    */   }
/*    */ 
/*    */   public void setStrMd5Code(String strMd5Code) {
/* 60 */     this.strMd5Code = strMd5Code;
/*    */   }
/*    */ 
/*    */   public String getStrRemoteFileName() {
/* 64 */     return this.strRemoteFileName;
/*    */   }
/*    */ 
/*    */   public void setStrRemoteFileName(String strRemoteFileName) {
/* 68 */     this.strRemoteFileName = strRemoteFileName;
/*    */   }
/*    */ }

/* Location:           D:\build\code\web_project\group_project\WebContent\WEB-INF\lib\sfSftpClient-1.0.0.jar
 * Qualified Name:     com.sf.sftp.client.infoBean.UploadStrategyInfoBean
 * JD-Core Version:    0.6.2
 */