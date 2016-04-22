package com.sf.module.common.domain;

import com.sf.framework.base.domain.BaseEntity;

/**
 * 网点实体类
 * @author 173279
 *
 */
public class OssDepartment extends BaseEntity  {
	private static final long serialVersionUID = 3074491628139216555L;
	private Long id;// 网点ID
	private String deptCode;// 部门代码
	private String deptName;// 部门名称
	private String parentDeptCode;// 父网点代码
	private String areaCode;// 区部代码
	private Integer deleteFlg;// 是否删除
	private Integer typeLevel;
	private String divisionCode;
	
	public String getDivisionCode() {
		return divisionCode;
	}

	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}

	public Integer getTypeLevel() {
		return typeLevel;
	}

	public void setTypeLevel(Integer typeLevel) {
		this.typeLevel = typeLevel;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
