package com.sf.module.warehouse.domain;

import java.util.Date;
import com.sf.framework.base.domain.BaseEntity;

public class WarehouseSchedulingModifyLog extends BaseEntity {
    private static final long serialVersionUID = 1L;
	private Long logID;
	private String yearMonth;
	private Long departmentID;
	private Long modifiedCount;
	private String employeeCode;
	private Date modifiedDate;
	private String modifiedEmpCode;
	public Long getLogID() {
    	return logID;
    }
	public void setLogID(Long logID) {
    	this.logID = logID;
    }
	public String getYearMonth() {
    	return yearMonth;
    }
	public void setYearMonth(String yearMonth) {
    	this.yearMonth = yearMonth;
    }
	public Long getDepartmentID() {
    	return departmentID;
    }
	public void setDepartmentID(Long departmentID) {
    	this.departmentID = departmentID;
    }
	public String getEmployeeCode() {
    	return employeeCode;
    }
	public void setEmployeeCode(String employeeCode) {
    	this.employeeCode = employeeCode;
    }
	public Date getModifiedDate() {
    	return modifiedDate;
    }
	public void setModifiedDate(Date modifiedDate) {
    	this.modifiedDate = modifiedDate;
    }
	public String getModifiedEmpCode() {
    	return modifiedEmpCode;
    }
	public void setModifiedEmpCode(String modifiedEmpCode) {
    	this.modifiedEmpCode = modifiedEmpCode;
    }
	public Long getModifiedCount() {
    	return modifiedCount;
    }
	public void setModifiedCount(Long modifiedCount) {
    	this.modifiedCount = modifiedCount;
    }
	
}