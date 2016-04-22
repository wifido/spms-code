package com.sf.module.operation.domain;

import java.util.Date;

public class GroupBaseInfoError {

	private static final long serialVersionUID = 3074491628139216555L;
	/*
	 * 组别代码
	 */
	private String groupCode;
	/*
	 * 组别名称
	 */
	private String groupName;
	/*
	 * 备注
	 */
	private String remark;
	/*
	 * 网点代码
	 */
	private Long deptId;
	/*
	 * 失效日期
	 */
	private String disableDt;
	/*
	 * 创建人工号
	 */
	private String createEmpCode;
	/*
	 * 修改人工号
	 */
	private String modifiedEmpCode;
	/*
	 * 创建时间
	 */
	private Date createTm;
	/*
	 * 修改时间
	 */
	private Date modifiedTm;
	/**
	 * 网点名称
	 */
	private String deptName;

	/**
	 * 地区代码
	 */
	private String areaCode;

	/**
	 * 查询状态
	 * 
	 * @return
	 */
	private int status;
	
	/**
	 * 错误信息
	 */
	private String errMsg;
	
	/**
	 * 网点代码
	 */
	private String deptCode;
	
	/*
	 * 失效日期
	 */
	private String enableDt;
	

	public String getEnableDt() {
		return enableDt;
	}

	public void setEnableDt(String enableDt) {
		this.enableDt = enableDt;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String  getDisableDt() {
		return disableDt;
	}

	public void setDisableDt(String disableDt) {
		this.disableDt = disableDt;
	}

	public String getCreateEmpCode() {
		return createEmpCode;
	}

	public void setCreateEmpCode(String createEmpCode) {
		this.createEmpCode = createEmpCode;
	}

	public String getModifiedEmpCode() {
		return modifiedEmpCode;
	}

	public void setModifiedEmpCode(String modifiedEmpCode) {
		this.modifiedEmpCode = modifiedEmpCode;
	}

	public Date getCreateTm() {
		return createTm;
	}

	public void setCreateTm(Date createTm) {
		this.createTm = createTm;
	}

	public Date getModifiedTm() {
		return modifiedTm;
	}

	public void setModifiedTm(Date modifiedTm) {
		this.modifiedTm = modifiedTm;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	
	

}
