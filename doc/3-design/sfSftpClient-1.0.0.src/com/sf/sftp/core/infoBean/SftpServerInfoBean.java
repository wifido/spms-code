/*    */ package com.sf.sftp.core.infoBean;
/*    */ 
/*    */ public class SftpServerInfoBean
/*    */ {
/*    */   private String hostIp;
/*    */   private String port;
/*    */   private String userName;
/*    */   private String password;
/*    */   private String path;
/* 24 */   private boolean encryptFlg = true;
/*    */ 
/* 27 */   private boolean dayDirectoryEnable = true;
/*    */ 
/*    */   public String getHostIp() {
/* 30 */     return this.hostIp;
/*    */   }
/*    */ 
/*    */   public void setHostIp(String hostIp) {
/* 34 */     this.hostIp = hostIp;
/*    */   }
/*    */ 
/*    */   public String getPort() {
/* 38 */     return this.port;
/*    */   }
/*    */ 
/*    */   public void setPort(String port) {
/* 42 */     this.port = port;
/*    */   }
/*    */ 
/*    */   public String getUserName() {
/* 46 */     return this.userName;
/*    */   }
/*    */ 
/*    */   public void setUserName(String userName) {
/* 50 */     this.userName = userName;
/*    */   }
/*    */ 
/*    */   public String getPassword() {
/* 54 */     return this.password;
/*    */   }
/*    */ 
/*    */   public void setPassword(String password) {
/* 58 */     this.password = password;
/*    */   }
/*    */ 
/*    */   public String getPath() {
/* 62 */     return this.path;
/*    */   }
/*    */ 
/*    */   public void setPath(String path) {
/* 66 */     this.path = path;
/*    */   }
/*    */ 
/*    */   public boolean isEncryptFlg() {
/* 70 */     return this.encryptFlg;
/*    */   }
/*    */ 
/*    */   public void setEncryptFlg(boolean encryptFlg) {
/* 74 */     this.encryptFlg = encryptFlg;
/*    */   }
/*    */ 
/*    */   public boolean isDayDirectoryEnable() {
/* 78 */     return this.dayDirectoryEnable;
/*    */   }
/*    */ 
/*    */   public void setDayDirectoryEnable(boolean dayDirectoryEnable) {
/* 82 */     this.dayDirectoryEnable = dayDirectoryEnable;
/*    */   }
/*    */ }

/* Location:           D:\build\code\web_project\group_project\WebContent\WEB-INF\lib\sfSftpClient-1.0.0.jar
 * Qualified Name:     com.sf.sftp.core.infoBean.SftpServerInfoBean
 * JD-Core Version:    0.6.2
 */