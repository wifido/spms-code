/*    */ package com.sf.sftp.core.exception;
/*    */ 
/*    */ public class SfSftGetSftpServerInformationException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 2304740553377829938L;
/* 10 */   protected Throwable cause = null;
/*    */ 
/*    */   public SfSftGetSftpServerInformationException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public SfSftGetSftpServerInformationException(String s) {
/* 17 */     super(s);
/*    */   }
/*    */ 
/*    */   public SfSftGetSftpServerInformationException(Throwable e) {
/* 21 */     super(e);
/*    */   }
/*    */ 
/*    */   public SfSftGetSftpServerInformationException(String s, Throwable e) {
/* 25 */     super(s);
/* 26 */     this.cause = e;
/*    */   }
/*    */ }

/* Location:           D:\build\code\web_project\group_project\WebContent\WEB-INF\lib\sfSftpClient-1.0.0.jar
 * Qualified Name:     com.sf.sftp.core.exception.SfSftGetSftpServerInformationException
 * JD-Core Version:    0.6.2
 */