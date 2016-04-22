package com.sf.module.common.domain;

import com.sf.module.authorization.action.DepartmentTreeWithoutCheckbox;

public class OperationDepartment extends DepartmentTreeWithoutCheckbox {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String typeCode;

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	
}
