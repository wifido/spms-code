/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-21     wen.jun       创建
 **********************************************/

package com.sf.module.ossinterface.domain;

import com.sf.framework.base.domain.BaseEntity;
import java.util.Date;

/**
 *
 * ESB数据重发请求(BigFileResendData)参数配置表
 * @author wen.jun  2014-06-21
 *
 */
public class EsbBigFileResend extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	/**
	 * -1=开始下载
	 */
	public static Integer START_DOWN = Integer.valueOf(-1);
	
	/**
	 * 0=开始解析
	 */
	public static Integer START_PARSER = Integer.valueOf(0);

	/**
	 * 1=成功
	 */
	public static Integer SUCCESS = Integer.valueOf(1);
	/**
	 * 2=解析失败
	 */
	public static Integer PARSER_FAILURE = Integer.valueOf(2);
	

	/**
	 * 3=下载失败
	 */
	public static Integer DOWNlOAD_FAILURE = Integer.valueOf(3);
	
	/**
	 * 4=已重发
	 */
	public static Integer RESEND = Integer.valueOf(4);
	

	/**
	 * -9=空请求,-1=开始下载,0=开始解析,1=成功,2=解析失败,3=下载失败,4=已重发
	 */
	protected Integer state;

	/**
	 * 源系统ID
	 */
	protected String systemId;

	/**
	 * 数据类型
	 */
	protected String dataType;

	/**
	 * 自己系统ID
	 */
	protected String selfSystemId;

	/**
	 * 获取到的数据时间戳
	 */
	protected String timeStamp;

	/**
	 * 流水号，数据重发
	 */
	protected String journalId;

	/**
	 * MD5值
	 */
	protected String md5Code;

	/**
	 * 远程文件路径
	 */
	protected String remotePath;

	/**
	 * 远程文件名称
	 */
	protected String remoteFileName;

	/**
	 * 是否压缩
	 */
	protected String isZip;

	/**
	 * 修改时间
	 */
	protected Date modifiedTm;

	/**
	 * 获取-9=空请求,-1=开始下载,0=开始解析,1=成功,2=解析失败,3=下载失败,4=已重发
	 */
	public Integer getState() {
		return this.state;
	}

	/**
	 * 设置-9=空请求,-1=开始下载,0=开始解析,1=成功,2=解析失败,3=下载失败,4=已重发
	 */
	public void setState(Integer state) {
		this.state = state;
	}

	/**
	 * 获取源系统ID
	 */
	public String getSystemId() {
		return this.systemId;
	}

	/**
	 * 设置源系统ID
	 */
	public void setSystemId(String systemId) {
		this.systemId = substring(100, systemId);
	}

	/**
	 * 获取数据类型
	 */
	public String getDataType() {
		return this.dataType;
	}

	/**
	 * 设置数据类型
	 */
	public void setDataType(String dataType) {
		this.dataType = substring(100, dataType);
	}

	/**
	 * 获取自己系统ID
	 */
	public String getSelfSystemId() {
		return this.selfSystemId;
	}

	/**
	 * 设置自己系统ID
	 */
	public void setSelfSystemId(String selfSystemId) {
		this.selfSystemId = substring(100, selfSystemId);
	}

	/**
	 * 获取获取到的数据时间戳
	 */
	public String getTimeStamp() {
		return this.timeStamp;
	}

	/**
	 * 设置获取到的数据时间戳
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = substring(100, timeStamp);
	}

	/**
	 * 获取流水号，数据重发
	 */
	public String getJournalId() {
		return this.journalId;
	}

	/**
	 * 设置流水号，数据重发
	 */
	public void setJournalId(String journalId) {
		this.journalId = substring(200, journalId);
	}

	/**
	 * 获取MD5值
	 */
	public String getMd5Code() {
		return this.md5Code;
	}

	/**
	 * 设置MD5值
	 */
	public void setMd5Code(String md5Code) {
		this.md5Code = substring(400, md5Code);
	}

	/**
	 * 获取远程文件路径
	 */
	public String getRemotePath() {
		return this.remotePath;
	}

	/**
	 * 设置远程文件路径
	 */
	public void setRemotePath(String remotePath) {
		this.remotePath = substring(400, remotePath);
	}

	/**
	 * 获取远程文件名称
	 */
	public String getRemoteFileName() {
		return this.remoteFileName;
	}

	/**
	 * 设置远程文件名称
	 */
	public void setRemoteFileName(String remoteFileName) {
		this.remoteFileName = substring(400, remoteFileName);
	}

	/**
	 * 获取是否压缩
	 */
	public String getIsZip() {
		return this.isZip;
	}

	/**
	 * 设置是否压缩
	 */
	public void setIsZip(String isZip) {
		this.isZip = substring(10, isZip);
	}

	/**
	 * 获取修改时间
	 */
	public Date getModifiedTm() {
		return this.modifiedTm;
	}

	/**
	 * 设置修改时间
	 */
	public void setModifiedTm(Date modifiedTm) {
		this.modifiedTm = modifiedTm;
	}
	
	private Date createdTm;

	/**
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014 
	 * @return the createdTm
	 */
	public Date getCreatedTm() {
		return createdTm;
	}

	/**
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014 
	 * @param to set createdTm the createdTm 
	 */
	public void setCreatedTm(Date createdTm) {
		this.createdTm = createdTm;
	}

	private  String substring(int len, String str) {
		if (str != null && str.length() > len) {
			str = str.substring(0, len);
		} 
		return str;
	}
	
}