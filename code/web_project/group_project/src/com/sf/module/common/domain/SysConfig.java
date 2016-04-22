/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-24     380173       创建
 **********************************************/

package com.sf.module.common.domain;

import java.util.Date;

import com.sf.framework.base.domain.BaseEntity;

/**
 *
 * 系统配置表(序列：SEQ_BASE)
 * @author 380173  2015-5-4
 *
 */
public class SysConfig extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * key的名称
	 */
	private String keyName;

	/**
	 * key的值
	 */
	private String keyValue;

	/**
	 * key的描述
	 */
	private String keyDesc;

	/**
	 * 创建时间
	 */
	private Date createdTm;

	/**
	 * 修改时间
	 */
	private Date modifiedTm;

	/**
	 * 获取key的名称
	 */
	public String getKeyName() {
		return this.keyName;
	}

	/**
	 * 设置key的名称
	 */
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	/**
	 * 获取key的值
	 */
	public String getKeyValue() {
		return this.keyValue;
	}

	/**
	 * 设置key的值
	 */
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	/**
	 * 获取key的描述
	 */
	public String getKeyDesc() {
		return this.keyDesc;
	}

	/**
	 * 设置key的描述
	 */
	public void setKeyDesc(String keyDesc) {
		this.keyDesc = keyDesc;
	}

	/**
	 * 获取创建时间
	 */
	public Date getCreatedTm() {
		return this.createdTm;
	}

	/**
	 * 设置创建时间
	 */
	public void setCreatedTm(Date createdTm) {
		this.createdTm = createdTm;
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
}