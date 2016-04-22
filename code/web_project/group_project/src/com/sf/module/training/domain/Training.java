package com.sf.module.training.domain;

import java.util.Date;

import com.sf.framework.base.domain.BaseEntity;

public class Training extends BaseEntity{

	private static final long serialVersionUID = 1L;

	private String departmentCode;
	private String trainingCode;
	private String employeeCode;
	private String yearsMonth;
	private String dayOfMonth;
	private Long postType;
	private String description;
	private String createdEmployeeCode;
	private String modifiedEmployeeCode;
	private Date createTime;
	private Date modifiedTime;
	
	public String getCreatedEmployeeCode() {
		return createdEmployeeCode;
	}
	public void setCreatedEmployeeCode(String createdEmployeeCode) {
		this.createdEmployeeCode = createdEmployeeCode;
	}
	public String getModifiedEmployeeCode() {
		return modifiedEmployeeCode;
	}
	public void setModifiedEmployeeCode(String modifiedEmployeeCode) {
		this.modifiedEmployeeCode = modifiedEmployeeCode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public String getTrainingCode() {
		return trainingCode;
	}
	public void setTrainingCode(String trainingCode) {
		this.trainingCode = trainingCode;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getYearsMonth() {
		return yearsMonth;
	}
	public void setYearsMonth(String yearsMonth) {
		this.yearsMonth = yearsMonth;
	}
	public String getDayOfMonth() {
		return dayOfMonth;
	}
	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
	public Long getPostType() {
		return postType;
	}
	public void setPostType(Long postType) {
		this.postType = postType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
