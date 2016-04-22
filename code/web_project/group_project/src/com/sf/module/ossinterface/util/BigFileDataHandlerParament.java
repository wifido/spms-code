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

import java.io.File;
import java.util.List;

import com.sf.module.ossinterface.domain.EsbBigFileResend;

/**
 * 处理从ESB文件下载的数据关键参数
 * @author 文俊 (337291) Jun 21, 2014 
 */

/**
 * 
 * @author 文俊 (337291) Jun 21, 2014 
 */
public class BigFileDataHandlerParament extends EsbBigFileResend {
 
	/**
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014 
	 */
	public BigFileDataHandlerParament(String journalId) {
		super();
		this.journalId = journalId;
	}
	
	
	private Long logId;
	
	/**
	 * @author 文俊 (337291)
	 * @date Jun 23, 2014 
	 * @return the logId
	 */
	public Long getLogId() {
		return logId;
	}



	/**
	 * @author 文俊 (337291)
	 * @date Jun 23, 2014 
	 * @param to set logId the logId 
	 */
	public void setLogId(Long logId) {
		this.logId = logId;
	}



	/**
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014 
	 */
	public BigFileDataHandlerParament(Long logId) {
		super();
		this.logId = logId;
	}


	/**
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014 
	 * 
	 */
	private static final long serialVersionUID = 545697673229089751L;
	
	/**
	 * 是否下载成功
	 */
	private boolean isSuccess = false;
	
	/**
	 * SFTP下载到的数据文件
	 */
	private List<File> fileList;
	
	
	
	/**
	 * @author 文俊 (337291)
	 * @date Jun 23, 2014 
	 * @param isSuccess
	 * @param systemId
	 * @param dataType
	 * @param selfSystemId
	 * @param timeStamp
	 * @param journalId
	 * @param fileList
	 * @param md5Code
	 * @param remotePath
	 * @param remoteFileName
	 * @param isZip
	 */
	public BigFileDataHandlerParament(boolean isSuccess, String systemId, String dataType,
			String selfSystemId, String timeStamp, String journalId,
			List<File> fileList, String md5Code, String remotePath, String remoteFileName, String isZip) {
		this(journalId);
		this.isSuccess = isSuccess;
		this.systemId = systemId;
		this.dataType = dataType;
		this.selfSystemId = selfSystemId;
		this.timeStamp = timeStamp;
		this.fileList = fileList;
		this.md5Code = md5Code;
		this.remotePath = remotePath;
		this.remoteFileName = remoteFileName;
		this.isZip = isZip;
	}


 
	/**
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014 
	 * @return the fileList
	 */
	public List<File> getFileList() {
		return fileList;
	}
	/**
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014 
	 * @param to set fileList the fileList 
	 */
	public void setFileList(List<File> fileList) {
		this.fileList = fileList;
	}

	@Override
	public String toString() {
		return "BigFileDataHandlerParament {systemId=" + systemId
				+ ", dataType=" + dataType + ", selfSystemId=" + selfSystemId
				+ ", timeStamp=" + timeStamp + ", journalId=" + journalId
				+ ", fileList=" + fileList + ", isSuccess=" + isSuccess + "}";
	}



	/**
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014 
	 * @return the isSuccess
	 */
	public boolean getIsSuccess() {
		return isSuccess;
	}



	/**
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014 
	 * @param to set isSuccess the isSuccess 
	 */
	public void setIsSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	
}
