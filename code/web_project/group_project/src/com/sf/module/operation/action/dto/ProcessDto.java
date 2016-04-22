package com.sf.module.operation.action.dto;


public class ProcessDto {
	/**
	 * 网点ID
	 */
	private Long deptId;
	private Long deptName;
	/**
	 * 工序ID
	 */
	private String processCode;
	
	/**
	 * 排班日期
	 */
	private String processDts;
	private String empCodes;
	/**
	 * 被排班人工号
	 */
	private String empCode;
	private String empName;
	private String ym;
	private Integer status;

	private Long teamId;
	public Long getDeptId() {
		return deptId;
	}


	public String getEmpCodes() {
		return empCodes;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
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

	

	public Long getDeptName() {
		return deptName;
	}

	public void setDeptName(Long deptName) {
		this.deptName = deptName;
	}

	public String getProcessCode() {
		return processCode;
	}


	public String getProcessDts() {
		return processDts;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}


	public void setProcessDts(String processDts) {
		this.processDts = processDts;
	}

}
