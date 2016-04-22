package com.sf.module.report.domain;

import java.util.Date;

import com.sf.framework.base.domain.BaseEntity;

public class SchedulingModify extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String empCode;
	private Date scheduleDt;
	private String scheduleCode;
	private Date modifiedTm;
	private String modifiedEmpCode;
	private Integer modifyType;
	private Long deptId;
	private String yearMonth;
	private String deptCode;
	private String areaCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public Date getScheduleDt() {
		return scheduleDt;
	}

	public void setScheduleDt(Date scheduleDt) {
		this.scheduleDt = scheduleDt;
	}

	public String getScheduleCode() {
		return scheduleCode;
	}

	public void setScheduleCode(String scheduleCode) {
		this.scheduleCode = scheduleCode;
	}

	public Date getModifiedTm() {
		return modifiedTm;
	}

	public void setModifiedTm(Date modifiedTm) {
		this.modifiedTm = modifiedTm;
	}

	public String getModifiedEmpCode() {
		return modifiedEmpCode;
	}

	public void setModifiedEmpCode(String modifiedEmpCode) {
		this.modifiedEmpCode = modifiedEmpCode;
	}

	public Integer getModifyType() {
		return modifyType;
	}

	public void setModifyType(Integer modifyType) {
		this.modifyType = modifyType;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
}
