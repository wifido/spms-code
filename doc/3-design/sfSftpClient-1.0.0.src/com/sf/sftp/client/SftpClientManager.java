/*    */ package com.sf.sftp.client;
/*    */ 
/*    */ import com.sf.sftp.client.impl.SftpClientImpl;
/*    */ import com.sf.sftp.client.util.LoadESBInfoUtil;
/*    */ 
/*    */ public class SftpClientManager
/*    */ {
/*  8 */   private static ISftpClient sftpClient = null;
/*    */ 
/*    */   public static ISftpClient getSftpClient() {
/* 11 */     if (sftpClient == null) {
/* 12 */       sftpClient = new SftpClientImpl();
/*    */     }
/*    */ 
/* 15 */     return sftpClient;
/*    */   }
/*    */ 
/*    */   public static void setEsbConfigResourceFile(String resourceFile)
/*    */   {
/* 22 */     LoadESBInfoUtil.setResourceFile(resourceFile);
/*    */   }
/*    */ }

/* Location:           D:\build\code\web_project\group_project\WebContent\WEB-INF\lib\sfSftpClient-1.0.0.jar
 * Qualified Name:     com.sf.sftp.client.SftpClientManager
 * JD-Core Version:    0.6.2
 */