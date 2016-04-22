package com.sf.module.vmsarrange.domain;

import com.sf.framework.base.domain.BaseEntity;

/**
 * <pre>
 * *********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * Description: 车辆管理系统的部门对象
 * HISTORY
 * *********************************************
 *  ID   DATE           PERSON          REASON
 *  1    2014-05-27	    600675                          创建 
 * *********************************************
 * </pre>
 */
@SuppressWarnings("serial")
public class ArrDepartment extends BaseEntity {
	private String deptCode;// 部门代码
	private String deptName;// 部门名称
	private Long typeLevel;// 部门类型(0:总部,1:经营本部 ,2:区部,3:分部,4:点部)
	private String parentDeptCode;// 父网点代码
	private String areaCode;// 区部代码
	private Integer deleteFlg;// 是否删除
	private String typeCode;//机构类型
	
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Long getTypeLevel() {
		return typeLevel;
	}

	public void setTypeLevel(Long typeLevel) {
		this.typeLevel = typeLevel;
	}

	public String getParentDeptCode() {
		return parentDeptCode;
	}

	public void setParentDeptCode(String parentDeptCode) {
		this.parentDeptCode = parentDeptCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Integer getDeleteFlg() {
		return deleteFlg;
	}

	public void setDeleteFlg(Integer deleteFlg) {
		this.deleteFlg = deleteFlg;
	}
}
