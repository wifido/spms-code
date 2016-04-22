/*    */ package com.sf.sftp.core;
/*    */ 
/*    */ import com.sf.sftp.core.exception.SfSftpESBHostConnectionException;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.net.Socket;
/*    */ import java.net.UnknownHostException;
/*    */ 
/*    */ public class SendNotifyMessageService
/*    */ {
/*    */   public static final String ENCODE_UTF8 = "utf-8";
/*    */ 
/*    */   public static String send(String strEsbHostIp, String strPort, String strNotifyMessage)
/*    */     throws SfSftpESBHostConnectionException
/*    */   {
/* 25 */     Socket s = null;
/* 26 */     OutputStream ot = null;
/* 27 */     DataOutputStream os = null;
/*    */     try {
/* 29 */       s = new Socket(strEsbHostIp, Integer.valueOf(strPort).intValue());
/* 30 */       ot = s.getOutputStream();
/* 31 */       os = new DataOutputStream(ot);
/* 32 */       os.write(strNotifyMessage.getBytes("utf-8"));
/* 33 */       os.flush();
/* 34 */       s.shutdownOutput();
/*    */ 
/* 36 */       InputStream is = s.getInputStream();
/* 37 */       byte[] buf = new byte[1024];
/* 38 */       int len = is.read(buf);
/* 39 */       return new String(buf, 0, len);
/*    */     }
/*    */     catch (UnknownHostException e) {
/* 42 */       throw new SfSftpESBHostConnectionException(e.getCause());
/*    */     } catch (IOException e) {
/* 44 */       throw new SfSftpESBHostConnectionException(e.getCause());
/*    */     } finally {
/*    */       try {
/* 47 */         if (os != null) {
/* 48 */           os.close();
/*    */         }
/* 50 */         if (s != null)
/* 51 */           s.close();
/*    */       }
/*    */       catch (IOException e) {
/* 54 */         throw new SfSftpESBHostConnectionException(e.getCause());
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\build\code\web_project\group_project\WebContent\WEB-INF\lib\sfSftpClient-1.0.0.jar
 * Qualified Name:     com.sf.sftp.core.SendNotifyMessageService
 * JD-Core Version:    0.6.2
 */