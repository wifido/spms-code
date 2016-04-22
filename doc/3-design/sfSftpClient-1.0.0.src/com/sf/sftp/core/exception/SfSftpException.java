/*    */ package com.sf.sftp.core.exception;
/*    */ 
/*    */ public class SfSftpException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 2304740553377829938L;
/*  7 */   protected Throwable cause = null;
/*    */ 
/*    */   public SfSftpException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public SfSftpException(String s) {
/* 14 */     super(s);
/*    */   }
/*    */ 
/*    */   public SfSftpException(Throwable e) {
/* 18 */     super(e);
/*    */   }
/*    */ 
/*    */   public SfSftpException(String s, Throwable e) {
/* 22 */     super(s);
/* 23 */     this.cause = e;
/*    */   }
/*    */ 
/*    */   public Throwable getCause() {
/* 27 */     return this.cause;
/*    */   }
/*    */ }

/* Location:           D:\build\code\web_project\group_project\WebContent\WEB-INF\lib\sfSftpClient-1.0.0.jar
 * Qualified Name:     com.sf.sftp.core.exception.SfSftpException
 * JD-Core Version:    0.6.2
 */