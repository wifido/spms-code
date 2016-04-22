/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-30     632898 李鹏       创建
 **********************************************/

package com.sf.module.operation.domain;

import com.sf.framework.base.domain.BaseEntity;
import java.util.Date;

/**
 *
 * 工序管理
 * @author 632898 李鹏  2014-06-30
 *
 */
public class Process extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 网点ID；导入时值为总部ID
	 */
	private Long deptId;

	/**
	 * 工序代码
	 */
	private String processCode;

	/**
	 * 工序名称
	 */
	private String processName;
	
	private String nameCode;

	/**
	 * 工序区域
	 */
	private String processArea;

	/**
	 * 工序使用工具
	 */
	private String processTool;

	/**
	 * 判断需求值
	 */
	private Double estimateValue;

	/**
	 * 强度需求值
	 */
	private Double intensityValue;

	/**
	 * 技能需求值
	 */
	private Double skillValue;

	/**
	 * 难度系数值
	 */
	private Double difficultyValue;

	/**
	 * 难度修正值（中转场才有）
	 */
	private Double difficultyModifyValue;
	/**
	 * 创建人工号
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createTm;

	/**
	 * 修改时间
	 */
	private Date modifiedTm;

	/**
	 * 修改人工号
	 */
	private String modifiedEmpCode;

	/**
	 * 创建人工号
	 */
	private String createEmpCode;

	/**
	 * 获取网点ID；导入时值为总部ID
	 */
	public Long getDeptId() {
		return this.deptId;
	}

	/**
	 * 设置网点ID；导入时值为总部ID
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	/**
	 * 获取工序代码
	 */
	public String getProcessCode() {
		return this.processCode;
	}

	/**
	 * 设置工序代码
	 */
	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	/**
	 * 获取工序名称
	 */
	public String getProcessName() {
		return this.processName;
	}

	/**
	 * 设置工序名称
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}

	/**
	 * 获取工序区域
	 */
	public String getProcessArea() {
		return this.processArea;
	}

	/**
	 * 设置工序区域
	 */
	public void setProcessArea(String processArea) {
		this.processArea = processArea;
	}

	/**
	 * 获取工序使用工具
	 */
	public String getProcessTool() {
		return this.processTool;
	}

	/**
	 * 设置工序使用工具
	 */
	public void setProcessTool(String processTool) {
		this.processTool = processTool;
	}

	/**
	 * 获取判断需求值
	 */
	public Double getEstimateValue() {
		return this.estimateValue;
	}

	/**
	 * 设置判断需求值
	 */
	public void setEstimateValue(Double estimateValue) {
		this.estimateValue = estimateValue;
	}

	/**
	 * 获取强度需求值
	 */
	public Double getIntensityValue() {
		return this.intensityValue;
	}

	/**
	 * 设置强度需求值
	 */
	public void setIntensityValue(Double intensityValue) {
		this.intensityValue = intensityValue;
	}

	/**
	 * 获取技能需求值
	 */
	public Double getSkillValue() {
		return this.skillValue;
	}

	/**
	 * 设置技能需求值
	 */
	public void setSkillValue(Double skillValue) {
		this.skillValue = skillValue;
	}

	/**
	 * 获取难度系数值
	 */
	public Double getDifficultyValue() {
		return this.difficultyValue;
	}

	/**
	 * 设置难度系数值
	 */
	public void setDifficultyValue(Double difficultyValue) {
		this.difficultyValue = difficultyValue;
	}

	/**
	 * 获取难度修正值（中转场才有）
	 */
	public Double getDifficultyModifyValue() {
		return this.difficultyModifyValue;
	}

	/**
	 * 设置难度修正值（中转场才有）
	 */
	public void setDifficultyModifyValue(Double difficultyModifyValue) {
		this.difficultyModifyValue = difficultyModifyValue;
	}

	/**
	 * 获取创建时间
	 */
	public Date getCreateTm() {
		return this.createTm;
	}

	/**
	 * 设置创建时间
	 */
	public void setCreateTm(Date createTm) {
		this.createTm = createTm;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getNameCode() {
		return this.processCode+"-"+this.processName;
	}
	
}