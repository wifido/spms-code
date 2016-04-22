/***********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * 
 * HISTORY
 ********************************************************
 *  ID    DATE               PERSON             REASON
 *  1     Jun 19, 2014           文俊 (337291)       创建 
 ********************************************************/
package com.sf.module.ossinterface.ws;

import java.io.File;
import java.util.List;

import com.sf.module.ossinterface.util.ESBBigFileUtil;
import com.sf.sftp.client.ISftpClient;
import com.sf.sftp.client.SftpClientManager;
import com.sf.sftp.client.infoBean.DownloadStrategyInfoBean;
import com.sf.sftp.client.infoBean.UploadStrategyInfoBean;

/**
 * 
 * @author 文俊 (337291) Jun 19, 2014
 */

public class SftipUtil {

    private static class SingletonHolder {
        // 单例变量
        static {
            SftpClientManager.setEsbConfigResourceFile(ESBBigFileUtil.Config.ESB_CONFIG_RESOURCE_FILE);
        }
        private static ISftpClient sftpClient = SftpClientManager.getSftpClient();
    }
    
    
    public static void main(String[] args) {
     // 通过报文还原上传策略
        UploadStrategyInfoBean uploadStrategyInfo = new UploadStrategyInfoBean();
//        uploadStrategyInfo.setBlMd5(blMd5);
//        uploadStrategyInfo.setStrMd5Code(md5Code);
        uploadStrategyInfo.setStrRemotePath("20140619/HRS/HRS_EMP_INIT");
        uploadStrategyInfo.setStrRemoteFileName("1403147194104_HRS_EMP_INIT.zip");
        uploadStrategyInfo.setBlZipCompress(true);

        // 配置下载策略
        DownloadStrategyInfoBean downloadStrategyInfo = new DownloadStrategyInfoBean();
        downloadStrategyInfo.setUploadStrategyInfo(uploadStrategyInfo);
        downloadStrategyInfo.setDownloadPath("D:/tmp");
        System.out.println(downLoad(downloadStrategyInfo));
    }

    public static List<File> downLoad(DownloadStrategyInfoBean downloadStrategyInfo) {
        return SingletonHolder.sftpClient.downLoad(downloadStrategyInfo);
    }
}
