package com.sf.module.operation.domain;


import java.util.Date;

import com.sf.framework.base.domain.BaseEntity;

/**
 * 描述：
 * 
 * <pre>
 * HISTORY
 * ****************************************************************************
 *  ID   DATE           PERSON          REASON
 *  1    2014-7-2       173279        Create
 * ****************************************************************************
 * </pre>
 * @author 173279
 * @since 1.0
 */
public class GroupBaseInfo extends BaseEntity {

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
	private Date disableDt;
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
	 * 生效日期
	 */
	private Date enableDt;
	
	public Date getEnableDt() {
		return enableDt;
	}
	public void setEnableDt(Date enableDt) {
		this.enableDt = enableDt;
	}
	/**
	 * 查询状态
	 * @return
	 */
	private int status;
	
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
	public Date getDisableDt() {
		return disableDt;
	}
	public void setDisableDt(Date disableDt) {
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
	
	
	
	
}
