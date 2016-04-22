/***********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * 
 * HISTORY
 ********************************************************
 *  ID    DATE               PERSON             REASON
 *  1     Jun 21, 2014           文俊 (337291)       创建 
 ********************************************************/
package com.sf.module.ossinterface.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 
 * @author 文俊 (337291) May 30, 2014 
 */

public abstract class ESBBigFileUtil {
    
    private ESBBigFileUtil(){}
    

    /**
     * <pre>
     * 流水号生产
     * </pre>
     * @author 文俊 (337291)
     * @date May 29, 2014 
     * @param systemId
     * @param dataType
     * @return
     */
    public static String generateJournalId(String systemId, String dataType) {
        return generateId(systemId, dataType).toString();
    }
    
    /**
     * <pre>
     * 流水号生产
     * </pre>
     * @author 文俊 (337291)
     * @date Jun 4, 2014 
     * @param systemId
     * @param dataType
     * @return
     */
    public static StringBuilder generateId(String systemId, String dataType) {
        return new StringBuilder(systemId)
        .append("-")
        .append(getTimeStamp())
        .append("-")
        .append(dataType)
        .append("-")
        .append(new Random().nextInt(1000))
        ;
    }
    
    /**
     * <pre>
     * 获取时间戳
     * </pre>
     * @author 文俊 (337291)
     * @date Jun 4, 2014 
     * @return
     */
    public static String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());
    }
    
    /**
     * <pre>
     * 文件上传远程路径（相对路径）
     * 设置上传路径时请以系统英文名+数据类型来定义
     * </pre>
     * @author 文俊 (337291)
     * @date Jun 4, 2014 
     * @return
     */
    public static String getRemoteFilePath(String dataType) {
        //时间(年月日)
        return new StringBuilder(new SimpleDateFormat("yyyyMMdd").format(new Date()))
        //系统英文名
        .append("/").append(Config.SELF_SYSTEM_ID)
        //数据类型
        .append("/").append(dataType)
        .toString();
    }
    
    /**
     * <pre>
     * 文件上传远程文件名称
     * 设置上传文件时以时间戳+数据类型来定义
     * </pre>
     * @author 文俊 (337291)
     * @date Jun 4, 2014 
     * @param dataType
     * @return
     */
    public static String generateFileName(String dataType) {
        return new StringBuffer(getTimeStamp()).append("_").append(dataType).toString();
    }
    
    /**
     * 配置信息
     * 
     * @author 文俊 (337291) Jun 4, 2014
     */
    public  interface Config {
        
        /**
         * 系统标识 ECAS-TCAS
         */
        String SELF_SYSTEM_ID = "SPMS-CORE";
        
        
        
        /**
         * 是否压缩打包
         */
        String IS_ZIP = "true";
        
        
        /**
         * 文件下载尝试次数
         */
        int TRY_TIME = 10;
        
        /**
         * 文件重新下载尝试的间隔时长
         */
        long INTERVAL_MILLIS = 10000;
        
        /**
         * ESB配置信息资源文件位置
         */
        String ESB_CONFIG_RESOURCE_FILE = "/com/sf/module/ossinterface/META-INF/config/ESBInfo.properties";
        
    }
    
}

