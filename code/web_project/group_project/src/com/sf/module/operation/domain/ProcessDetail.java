/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-08     houjingyu       创建
 **********************************************/

package com.sf.module.operation.domain;

import com.sf.framework.base.domain.BaseEntity;
import java.util.Date;

/**
 *
 * 工序每日明细
 * @author houjingyu  2014-07-08
 *
 */
public class ProcessDetail extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 工序代码
	 */
	private String processCode;

	/**
	 * 网点ID
	 */
	private Long deptId;

	/**
	 * 指定工序日期
	 */
	private Date processDt;

	/**
	 * 被安排工序员工工号
	 */
	private String empCode;

	/**
	 * 创建日期
	 */
	private Date createTm;

	/**
	 * 修改日期
	 */
	private Date modifiedTm;

	/**
	 * 创建人工号
	 */
	private String createEmpCode;

	/**
	 * 修改人工号
	 */
	private String modifiedEmpCode;

	/**
	 * 月度主键ID
	 */
	private Long processMonId;

	/**
	 * 获取工序ID
	 */
	public String getProcessCode() {
		return this.processCode;
	}

	/**
	 * 设置工序ID
	 */
	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	/**
	 * 获取网点ID
	 */
	public Long getDeptId() {
		return this.deptId;
	}

	/**
	 * 设置网点ID
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	/**
	 * 获取指定工序日期
	 */
	public Date getProcessDt() {
		return this.processDt;
	}

	/**
	 * 设置指定工序日期
	 */
	public void setProcessDt(Date processDt) {
		this.processDt = processDt;
	}

	/**
	 * 获取被安排工序员工工号
	 */
	public String getEmpCode() {
		return this.empCode;
	}

	/**
	 * 设置被安排工序员工工号
	 */
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	/**
	 * 获取创建日期
	 */
	public Date getCreateTm() {
		return this.createTm;
	}

	/**
	 * 设置创建日期
	 */
	public void setCreateTm(Date createTm) {
		this.createTm = createTm;
	}

	/**
	 * 获取修改日期
	 */
	public Date getModifiedTm() {
		return this.modifiedTm;
	}

	/**
	 * 设置修改日期
	 */
	public void setModifiedTm(Date modifiedTm) {
		this.modifiedTm = modifiedTm;
	}

	/**
	 * 获取创建人工号
	 */
	public String getCreateEmpCode() {
		return this.createEmpCode;
	}

	/**
	 * 设置创建人工号
	 */
	public void setCreateEmpCode(String createEmpCode) {
		this.createEmpCode = createEmpCode;
	}

	/**
	 * 获取修改人工号
	 */
	public String getModifiedEmpCode() {
		return this.modifiedEmpCode;
	}

	/**
	 * 设置修改人工号
	 */
	public void setModifiedEmpCode(String modifiedEmpCode) {
		this.modifiedEmpCode = modifiedEmpCode;
	}

	/**
	 * 获取月度主键ID
	 */
	public Long getProcessMonId() {
		return this.processMonId;
	}

	/**
	 * 设置月度主键ID
	 */
	public void setProcessMonId(Long processMonId) {
		this.processMonId = processMonId;
	}
}