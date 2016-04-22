package com.sf.module.esbinterface.util;

import org.joda.time.DateTime;

public interface Constants {

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
	String ESB_CONFIG_RESOURCE_FILE = "/com/sf/module/esbinterface/META-INF/config/ESBInfo.properties";

	int MAX_UPLOAD_NOTIFY_RETRY_TIME = 5;
	int MAX_WAIT_SECOND_TIME = 5;
	String IS_MD5 = "true";
	String SYSTEM_ID = "SPMS-CORE";
	String FILE_PREFIX = SYSTEM_ID + "-";
	String DEFAULT_UPLOAD_FILE_NAME = "SPMS-CORE.zip";
	String DATA_TYPE = "HCM-IN_PTBC";
	String SF_UPLOAD_PATH = "VOL_sftp1/SAP_in/" + new DateTime().toString("yyyyMMdd") + "/" + DATA_TYPE;
}
