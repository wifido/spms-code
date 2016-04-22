package com.sf.module.driver.domain;

public class WeeklyExportModel {
	private int order;
	private String areaCode;
	private String departmentCode;
	private String yearWeek;
	private String employeeName;
	private String employeeCode;
	private String confirmStatus;
	private String confirmTime;
	
	public WeeklyExportModel(String areaCode, String departmentCode, String yearWeek, String employeeName,
			String employeeCode, int status, String confirmTime, int order) {
		this.areaCode = areaCode;
		this.departmentCode = departmentCode;
		this.yearWeek = yearWeek;
		this.employeeName = employeeName;
		this.employeeCode = employeeCode;
		if (status == 0) {
			this.confirmStatus = "未确认";
		} else {
			this.confirmStatus = "已确认";
		}
		this.confirmTime = confirmTime;
		this.order = order;
	}
	
	public String getYearWeek() {
		return yearWeek;
	}

	public void setYearWeek(String yearWeek) {
		this.yearWeek = yearWeek;
	}


	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getConfirmStatus() {
		return confirmStatus;
	}
	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}
	public String getConfirmTime() {
		return confirmTime;
	}
	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}
}
