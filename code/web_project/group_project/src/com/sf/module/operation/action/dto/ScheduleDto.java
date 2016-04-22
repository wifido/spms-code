package com.sf.module.operation.action.dto;


public class ScheduleDto {
	/**
	 * 网点ID
	 */
	private Long deptId;
	private Long deptName;
	/**
	 * 班别ID
	 */
	private Long sheduleId;
	private String sheduleCode;
	
	/**
	 * 排班日期
	 */
	private String sheduleDts;
	private String empCodes;
	/**
	 * 被排班人工号
	 */
	private String empCode;
	private String empName;
	private String ym;
	private Integer status;
	private String className;
	private Long teamId;
	

	public Long getDeptId() {
		return deptId;
	}

	public Long getSheduleId() {
		return sheduleId;
	}

	public String getSheduleDts() {
		return sheduleDts;
	}

	public String getEmpCodes() {
		return empCodes;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public void setSheduleId(Long sheduleId) {
		this.sheduleId = sheduleId;
	}

	public void setSheduleDts(String sheduleDts) {
		this.sheduleDts = sheduleDts;
	}

	public void setEmpCodes(String empCodes) {
		this.empCodes = empCodes;
	}

	public String getEmpCode() {
		return empCode;
	}

	public String getEmpName() {
		return empName;
	}

	public String getYm() {
		return ym;
	}

	public Integer getStatus() {
		return status;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public void setYm(String ym) {
		this.ym = ym;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Long getDeptName() {
		return deptName;
	}

	public void setDeptName(Long deptName) {
		this.deptName = deptName;
	}

	public String getSheduleCode() {
		return sheduleCode;
	}

	public void setSheduleCode(String sheduleCode) {
		this.sheduleCode = sheduleCode;
	}

}
