package com.sf.sftp.client;

import com.sf.sftp.client.infoBean.DownloadStrategyInfoBean;
import com.sf.sftp.client.infoBean.UploadStrategyInfoBean;
import java.io.File;
import java.util.List;
import java.util.Vector;

public abstract interface ISftpClient
{
  public abstract boolean upload(File paramFile, UploadStrategyInfoBean paramUploadStrategyInfoBean);

  public abstract boolean upload(List<File> paramList, UploadStrategyInfoBean paramUploadStrategyInfoBean);

  public abstract List<File> downLoad(DownloadStrategyInfoBean paramDownloadStrategyInfoBean);

  public abstract boolean delete(String paramString);

  public abstract boolean mkdir(String paramString);

  public abstract boolean rmdir(String paramString);

  public abstract Vector<?> lsDir(String paramString);

  public abstract boolean rename(String paramString1, String paramString2);

  public abstract String sendNotifyMessage(UploadStrategyInfoBean paramUploadStrategyInfoBean);
}

/* Location:           D:\build\code\web_project\group_project\WebContent\WEB-INF\lib\sfSftpClient-1.0.0.jar
 * Qualified Name:     com.sf.sftp.client.ISftpClient
 * JD-Core Version:    0.6.2
 */