package com.sf.module.domain;

import java.util.Date;

public class ApplyRecord {

	private Long applyId;
	private String applyEmployeeCode;
	private String departmentCode;
	private String dayOfMonth;
	private Long applyType;
	private String applyInfo;
	private String oldConfigCode;
	private String newConfigCode;
	private Long status;
	private String approver;
	private String approverInfo;
	private Date apporveTime;

	public Long getApplyId() {
		return applyId;
	}

	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}

	public String getApplyEmployeeCode() {
		return applyEmployeeCode;
	}

	public void setApplyEmployeeCode(String applyEmployeeCode) {
		this.applyEmployeeCode = applyEmployeeCode;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public Long getApplyType() {
		return applyType;
	}

	public void setApplyType(Long applyType) {
		this.applyType = applyType;
	}

	public String getApplyInfo() {
		return applyInfo;
	}

	public void setApplyInfo(String applyInfo) {
		this.applyInfo = applyInfo;
	}

	public String getOldConfigCode() {
		return oldConfigCode;
	}

	public void setOldConfigCode(String oldConfigCode) {
		this.oldConfigCode = oldConfigCode;
	}

	public String getNewConfigCode() {
		return newConfigCode;
	}

	public void setNewConfigCode(String newConfigCode) {
		this.newConfigCode = newConfigCode;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getApproverInfo() {
		return approverInfo;
	}

	public void setApproverInfo(String approverInfo) {
		this.approverInfo = approverInfo;
	}

	public Date getApporveTime() {
		return apporveTime;
	}

	public void setApporveTime(Date apporveTime) {
		this.apporveTime = apporveTime;
	}

}
