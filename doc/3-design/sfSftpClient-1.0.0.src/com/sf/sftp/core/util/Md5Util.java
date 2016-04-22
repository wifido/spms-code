/*     */ package com.sf.sftp.core.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.FileChannel.MapMode;
/*     */ import java.security.AccessController;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivilegedAction;
/*     */ import org.apache.log4j.Logger;
/*     */ import sun.misc.Cleaner;
/*     */ 
/*     */ public class Md5Util
/*     */ {
/*  18 */   private static Logger log = Logger.getLogger(Md5Util.class);
/*  19 */   protected static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*  20 */   protected static MessageDigest messagedigest = null;
/*     */ 
/*     */   public static String generateMd5Code(File file)
/*     */   {
/*  35 */     String md5Code = "";
/*  36 */     FileInputStream in = null;
/*  37 */     FileChannel ch = null;
/*     */     try {
/*  39 */       in = new FileInputStream(file);
/*  40 */       ch = in.getChannel();
/*  41 */       MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0L, file.length());
/*  42 */       messagedigest.update(byteBuffer);
/*  43 */       clean(byteBuffer);
/*  44 */       return bufferToHex(messagedigest.digest());
/*     */     } catch (IOException e) {
/*  46 */       log.error(e);
/*     */     } finally {
/*  48 */       if (ch != null) {
/*     */         try {
/*  50 */           ch.close();
/*     */         } catch (IOException e) {
/*  52 */           log.error(e);
/*     */         }
/*     */       }
/*     */ 
/*  56 */       if (in != null) {
/*     */         try {
/*  58 */           in.close();
/*     */         } catch (IOException e) {
/*  60 */           log.error(e);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*  65 */     return md5Code;
/*     */   }
/*     */ 
/*     */   public static void clean(Object buffer)
/*     */   {
/*  71 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/*     */         try {
/*  74 */           Method getCleanerMethod = this.val$buffer.getClass().getMethod("cleaner", new Class[0]);
/*  75 */           getCleanerMethod.setAccessible(true);
/*  76 */           Cleaner cleaner = (Cleaner)getCleanerMethod.invoke(this.val$buffer, new Object[0]);
/*  77 */           cleaner.clean();
/*     */         } catch (Exception e) {
/*  79 */           e.printStackTrace();
/*     */         }
/*     */ 
/*  82 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static boolean checkMd5Code(File file, String strMd5Code)
/*     */   {
/*  92 */     if (strMd5Code == null) {
/*  93 */       return false;
/*     */     }
/*  95 */     return strMd5Code.equals(generateMd5Code(file));
/*     */   }
/*     */ 
/*     */   private static String bufferToHex(byte[] bytes)
/*     */   {
/* 100 */     return bufferToHex(bytes, 0, bytes.length);
/*     */   }
/*     */ 
/*     */   private static String bufferToHex(byte[] bytes, int m, int n) {
/* 104 */     StringBuffer stringbuffer = new StringBuffer(2 * n);
/* 105 */     int k = m + n;
/* 106 */     for (int l = m; l < k; l++) {
/* 107 */       appendHexPair(bytes[l], stringbuffer);
/*     */     }
/* 109 */     return stringbuffer.toString();
/*     */   }
/*     */ 
/*     */   private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
/* 113 */     char c0 = hexDigits[((bt & 0xF0) >> 4)];
/* 114 */     char c1 = hexDigits[(bt & 0xF)];
/* 115 */     stringbuffer.append(c0);
/* 116 */     stringbuffer.append(c1);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  24 */       messagedigest = MessageDigest.getInstance("MD5");
/*     */     } catch (NoSuchAlgorithmException nsaex) {
/*  26 */       log.error("init fail!，MessageDigest unsuport MD5Util!");
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\build\code\web_project\group_project\WebContent\WEB-INF\lib\sfSftpClient-1.0.0.jar
 * Qualified Name:     com.sf.sftp.core.util.Md5Util
 * JD-Core Version:    0.6.2
 */