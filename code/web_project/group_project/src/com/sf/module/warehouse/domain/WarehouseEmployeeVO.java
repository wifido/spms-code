package com.sf.module.warehouse.domain;

import java.util.Date;

import com.sf.framework.base.domain.BaseEntity;

public class WarehouseEmployeeVO extends BaseEntity {
	private Long empId;
	private String empCode;
	private String empName;
	private String empDutyName;
	private String deptId;
	private Date createTm;
	private Date modifiedTm;
	private String createEmpCode;
	private String modifiedEmpCode;
	private String workType;
	private String email;
	private Date dimissionDt;
	private Date SfDate;
	private String isHaveCommission;
	
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmpDutyName() {
		return empDutyName;
	}
	public void setEmpDutyName(String empDutyName) {
		this.empDutyName = empDutyName;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
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
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getDimissionDt() {
		return dimissionDt;
	}
	public void setDimissionDt(Date dimissionDt) {
		this.dimissionDt = dimissionDt;
	}
	public Date getSfDate() {
		return SfDate;
	}
	public void setSfDate(Date sfDate) {
		SfDate = sfDate;
	}
	public String getIsHaveCommission() {
		return isHaveCommission;
	}
	public void setIsHaveCommission(String isHaveCommission) {
		this.isHaveCommission = isHaveCommission;
	}
}
