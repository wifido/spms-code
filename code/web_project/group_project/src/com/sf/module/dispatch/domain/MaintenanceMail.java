package com.sf.module.dispatch.domain;

import com.sf.framework.base.domain.BaseEntity;

public class MaintenanceMail  extends BaseEntity{

	private static final long serialVersionUID = 1L;
	
	private String emailAccount;
	
	private String departmentCode;
	
	private String divisionCode;
	
	private String areaCode;
	
	public String getDivisionCode() {
		return divisionCode;
	}

	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getEmailAccount() {
		return emailAccount;
	}

	public void setEmailAccount(String emailAccount) {
		this.emailAccount = emailAccount;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

}
