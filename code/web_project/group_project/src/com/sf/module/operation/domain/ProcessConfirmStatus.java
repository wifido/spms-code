/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-02     houjingyu       创建
 **********************************************/

package com.sf.module.operation.domain;

import com.sf.framework.base.domain.BaseEntity;

/**
 *
 * 排班提交确认状态
 * @author houjingyu  2014-07-02
 *
 */
public class ProcessConfirmStatus extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 网点
	 */
	private Long deptId;

	/**
	 * 月份
	 */
	private String ym;

	/**
	 * 提交确认标志(1-已确认 null-未确认)
	 */
	private Integer commitStatus;

	/**
	 * 获取网点
	 */
	public Long getDeptId() {
		return this.deptId;
	}

	/**
	 * 设置网点
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	/**
	 * 获取月份
	 */
	public String getYm() {
		return this.ym;
	}

	/**
	 * 设置月份
	 */
	public void setYm(String ym) {
		this.ym = ym;
	}

	/**
	 * 获取提交确认标志(1-已确认 null-未确认)
	 */
	public Integer getCommitStatus() {
		return this.commitStatus;
	}

	/**
	 * 设置提交确认标志(1-已确认 null-未确认)
	 */
	public void setCommitStatus(Integer commitStatus) {
		this.commitStatus = commitStatus;
	}
}