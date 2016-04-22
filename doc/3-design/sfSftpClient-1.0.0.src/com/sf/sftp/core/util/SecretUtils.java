/*     */ package com.sf.sftp.core.util;
/*     */ 
/*     */ import java.security.Key;
/*     */ import java.security.SecureRandom;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.KeyGenerator;
/*     */ import sun.misc.BASE64Decoder;
/*     */ import sun.misc.BASE64Encoder;
/*     */ 
/*     */ public class SecretUtils
/*     */ {
/*     */   private Key key;
/*     */ 
/*     */   public SecretUtils(String strKey)
/*     */   {
/*  30 */     getKey(strKey);
/*     */   }
/*     */ 
/*     */   public SecretUtils() {
/*  34 */     getKey("sftpServer");
/*     */   }
/*     */ 
/*     */   public void getKey(String strKey)
/*     */   {
/*     */     try
/*     */     {
/*  44 */       KeyGenerator generator = KeyGenerator.getInstance("DES");
/*  45 */       generator.init(new SecureRandom(strKey.getBytes()));
/*  46 */       this.key = generator.generateKey();
/*     */     } catch (Exception e) {
/*  48 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getEncString(String strMing)
/*     */   {
/*  59 */     byte[] byteMi = null;
/*  60 */     byte[] byteMing = null;
/*  61 */     String strMi = "";
/*  62 */     BASE64Encoder base64en = new BASE64Encoder();
/*     */     try {
/*  64 */       byteMing = strMing.getBytes("UTF8");
/*  65 */       byteMi = getEncCode(byteMing);
/*  66 */       strMi = base64en.encode(byteMi);
/*     */     } catch (Exception e) {
/*  68 */       e.printStackTrace();
/*     */     } finally {
/*  70 */       base64en = null;
/*  71 */       byteMing = null;
/*  72 */       byteMi = null;
/*     */     }
/*  74 */     return strMi;
/*     */   }
/*     */ 
/*     */   public String getDesString(String strMi)
/*     */   {
/*  84 */     BASE64Decoder base64De = new BASE64Decoder();
/*  85 */     byte[] byteMing = null;
/*  86 */     byte[] byteMi = null;
/*  87 */     String strMing = "";
/*     */     try {
/*  89 */       byteMi = base64De.decodeBuffer(strMi);
/*  90 */       byteMing = getDesCode(byteMi);
/*  91 */       strMing = new String(byteMing, "UTF8");
/*     */     } catch (Exception e) {
/*  93 */       e.printStackTrace();
/*     */     } finally {
/*  95 */       base64De = null;
/*  96 */       byteMing = null;
/*  97 */       byteMi = null;
/*     */     }
/*  99 */     return strMing;
/*     */   }
/*     */ 
/*     */   private byte[] getEncCode(byte[] byteS)
/*     */   {
/* 109 */     byte[] byteFina = null;
/*     */     try
/*     */     {
/* 112 */       cipher = Cipher.getInstance("DES");
/* 113 */       cipher.init(1, this.key);
/* 114 */       byteFina = cipher.doFinal(byteS);
/*     */     } catch (Exception e) {
/* 116 */       e.printStackTrace();
/*     */     } finally {
/* 118 */       Cipher cipher = null;
/*     */     }
/* 120 */     return byteFina;
/*     */   }
/*     */ 
/*     */   private byte[] getDesCode(byte[] byteD)
/*     */   {
/* 131 */     byte[] byteFina = null;
/*     */     try {
/* 133 */       cipher = Cipher.getInstance("DES");
/* 134 */       cipher.init(2, this.key);
/* 135 */       byteFina = cipher.doFinal(byteD);
/*     */     } catch (Exception e) {
/* 137 */       e.printStackTrace();
/*     */     } finally {
/* 139 */       Cipher cipher = null;
/*     */     }
/* 141 */     return byteFina;
/*     */   }
/*     */ }

/* Location:           D:\build\code\web_project\group_project\WebContent\WEB-INF\lib\sfSftpClient-1.0.0.jar
 * Qualified Name:     com.sf.sftp.core.util.SecretUtils
 * JD-Core Version:    0.6.2
 */