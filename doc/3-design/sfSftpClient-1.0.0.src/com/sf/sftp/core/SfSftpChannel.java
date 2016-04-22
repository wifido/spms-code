/*     */ package com.sf.sftp.core;
/*     */ 
/*     */ import com.jcraft.jsch.ChannelSftp;
/*     */ import com.jcraft.jsch.Session;
/*     */ import com.jcraft.jsch.SftpException;
/*     */ import com.sf.sftp.client.infoBean.DownloadStrategyInfoBean;
/*     */ import com.sf.sftp.client.infoBean.UploadStrategyInfoBean;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Vector;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ public class SfSftpChannel
/*     */ {
/*  22 */   private static Logger log = Logger.getLogger(SfSftpChannel.class);
/*  23 */   private ChannelSftp channelSftp = null;
/*  24 */   private Session session = null;
/*     */   public static final String FILE_SEPERATOR = "/";
/*     */ 
/*     */   public boolean put(File uploadFile, UploadStrategyInfoBean uploadStrategyInfo)
/*     */   {
/*  35 */     FileInputStream fileInputStream = null;
/*     */     try {
/*  37 */       String strRemotePathFull = getRemotePathFull(uploadStrategyInfo);
/*  38 */       fileInputStream = new FileInputStream(uploadFile);
/*     */ 
/*  40 */       createRemotePath(uploadStrategyInfo.getStrRemotePath());
/*  41 */       this.channelSftp.put(fileInputStream, strRemotePathFull, uploadStrategyInfo.isResume() ? 1 : 0);
/*     */     } catch (SftpException e) {
/*  43 */       log.error(e);
/*  44 */       return false;
/*     */     }
/*     */     catch (FileNotFoundException e)
/*     */     {
/*     */       boolean bool;
/*  46 */       log.error(e);
/*  47 */       return false;
/*     */     } finally {
/*  49 */       if (fileInputStream != null) {
/*     */         try {
/*  51 */           fileInputStream.close();
/*     */         } catch (IOException e) {
/*  53 */           log.error(e);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*  58 */     return true;
/*     */   }
/*     */ 
/*     */   private void createRemotePath(String remotePath)
/*     */   {
/*  67 */     if (remotePath != null) {
/*  68 */       String[] remoteFolder = remotePath.split("/");
/*  69 */       String currPath = "";
/*  70 */       for (String folder : remoteFolder) {
/*  71 */         currPath = currPath + folder + "/";
/*  72 */         if ((folder != null) && (!"".equals(folder)))
/*     */           try {
/*  74 */             this.channelSftp.mkdir(currPath);
/*     */           }
/*     */           catch (SftpException e)
/*     */           {
/*     */           }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getRemotePathFull(UploadStrategyInfoBean uploadStrategyInfo)
/*     */     throws SftpException
/*     */   {
/*  92 */     String strRemotePathFull = null;
/*     */ 
/*  94 */     if ((uploadStrategyInfo.getStrRemotePath() == null) || (uploadStrategyInfo.getStrRemotePath() == ""))
/*  95 */       strRemotePathFull = this.channelSftp.getHome() + "/" + uploadStrategyInfo.getStrRemoteFileName();
/*     */     else {
/*  97 */       strRemotePathFull = this.channelSftp.getHome() + "/" + uploadStrategyInfo.getStrRemotePath() + "/" + uploadStrategyInfo.getStrRemoteFileName();
/*     */     }
/*     */ 
/* 100 */     return strRemotePathFull;
/*     */   }
/*     */ 
/*     */   private String getLocalFilePath(DownloadStrategyInfoBean downloadStrategyInfo)
/*     */     throws SftpException
/*     */   {
/* 111 */     String strLocalFilePath = null;
/*     */ 
/* 113 */     if (downloadStrategyInfo.getDownloadPath() == null) {
/* 114 */       strLocalFilePath = downloadStrategyInfo.getUploadStrategyInfo().getStrRemoteFileName();
/*     */     }
/*     */     else {
/* 117 */       File file = new File(downloadStrategyInfo.getDownloadPath());
/* 118 */       if (!file.exists()) {
/* 119 */         file.mkdirs();
/*     */       }
/* 121 */       strLocalFilePath = downloadStrategyInfo.getDownloadPath() + "/" + downloadStrategyInfo.getUploadStrategyInfo().getStrRemoteFileName();
/*     */     }
/*     */ 
/* 124 */     return strLocalFilePath;
/*     */   }
/*     */ 
/*     */   public File get(DownloadStrategyInfoBean downloadStrategyInfo)
/*     */   {
/*     */     String remotePath;
/*     */     try
/*     */     {
/* 138 */       remotePath = getRemotePathFull(downloadStrategyInfo.getUploadStrategyInfo());
/*     */     } catch (SftpException e1) {
/* 140 */       log.error("Download error!");
/*     */ 
/* 142 */       return null;
/*     */     }
/*     */ 
/* 145 */     if (log.isInfoEnabled()) {
/* 146 */       log.info("Start to download File: \t" + remotePath);
/* 151 */     }
/*     */ InputStream is = null;
/* 152 */     OutputStream os = null;
/*     */     File localFile;
/*     */     try {
/* 154 */       is = this.channelSftp.get(remotePath);
/*     */ 
/* 156 */       localFile = new File(getLocalFilePath(downloadStrategyInfo));
/* 157 */       os = new FileOutputStream(localFile);
/* 158 */       byte[] bytes = new byte[1048576];
/* 159 */       len = 0;
/* 160 */       while ((len = is.read(bytes)) >= 0) {
/* 161 */         os.write(bytes, 0, len);
/*     */       }
/*     */     }
/*     */     catch (SftpException e)
/*     */     {
/* 166 */       log.error("Download error!");
/* 167 */       log.error(e);
/*     */ 
/* 169 */       return null;
/*     */     } catch (FileNotFoundException e) {
/* 171 */       log.error("Download error!");
/* 172 */       log.error(e);
/*     */ 
/* 174 */       return null;
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*     */       int len;
/* 176 */       log.error("Download error!");
/* 177 */       log.error(e);
/*     */ 
/* 179 */       return null;
/*     */     } finally {
/* 181 */       if (os != null) {
/*     */         try {
/* 183 */           os.close();
/*     */         } catch (IOException e) {
/* 185 */           log.error(e);
/*     */         }
/*     */       }
/*     */ 
/* 189 */       if (is != null) {
/*     */         try {
/* 191 */           is.close();
/*     */         } catch (IOException e) {
/* 193 */           log.error(e);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 198 */     if (log.isInfoEnabled()) {
/* 199 */       log.info("Download completed!");
/*     */     }
/*     */ 
/* 202 */     return localFile;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 210 */     if (this.channelSftp != null) {
/* 211 */       this.channelSftp.disconnect();
/*     */     }
/*     */ 
/* 214 */     if (this.session != null)
/* 215 */       this.session.disconnect();
/*     */   }
/*     */ 
/*     */   public ChannelSftp getChannel()
/*     */   {
/* 220 */     return this.channelSftp;
/*     */   }
/*     */ 
/*     */   public void setChannel(ChannelSftp channel) {
/* 224 */     this.channelSftp = channel;
/*     */   }
/*     */ 
/*     */   public Session getSession() {
/* 228 */     return this.session;
/*     */   }
/*     */ 
/*     */   public void setSession(Session session) {
/* 232 */     this.session = session;
/*     */   }
/*     */ 
/*     */   public void delete(String remotePath) throws SftpException {
/* 236 */     this.channelSftp.rm(this.channelSftp.getHome() + "/" + remotePath);
/*     */   }
/*     */ 
/*     */   public void mkdir(String dir) throws SftpException {
/* 240 */     this.channelSftp.mkdir(dir);
/*     */   }
/*     */ 
/*     */   public void rmdir(String dir) throws SftpException {
/* 244 */     this.channelSftp.rmdir(dir);
/*     */   }
/*     */ 
/*     */   public Vector<?> lsDir(String dir) throws SftpException {
/* 248 */     return this.channelSftp.ls(dir);
/*     */   }
/*     */ 
/*     */   public void rename(String oldName, String newName) throws SftpException {
/* 252 */     this.channelSftp.rename(oldName, newName);
/*     */   }
/*     */ }

/* Location:           D:\build\code\web_project\group_project\WebContent\WEB-INF\lib\sfSftpClient-1.0.0.jar
 * Qualified Name:     com.sf.sftp.core.SfSftpChannel
 * JD-Core Version:    0.6.2
 */