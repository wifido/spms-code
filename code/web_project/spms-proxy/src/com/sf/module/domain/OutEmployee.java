/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-20     zhaochangjin       创建
 **********************************************/

package com.sf.module.domain;
import java.util.Date;

/**
 * 
 * 外包人员实体
 * 
 * @author zhaochangjin 2014-06-20
 * 
 */
public class OutEmployee {

	private static final long serialVersionUID = 1L;

	/**
	 * 工号（系统自动生成，从100000000开始)
	 */
	private String empCode;

	/**
	 * 姓名
	 */
	private String empName;

	/**
	 * 状态1：在职，0：离职
	 */
	/*update by 632898 2014-07-10  在职字段已删除
	 * private Integer empStatus;*/

	/**
	 * 职位
	 */
	private String empDutyName;

	/**
	 * 网点ID
	 */
	private Long deptId;
	private String areaCode;
	private String deptName;
	/**
	 * 小组ID
	 */
	private Long groupId;

	/**
	 * 创建时间
	 */
	private String createTm;

	/**
	 * 修改时间
	 */
	private String modifiedTm;

	/**
	 * 创建人工号
	 */
	private String createEmpCode;

	/**
	 * 修改人工号
	 */
	private String modifiedEmpCode;
	
	/**
	 * 用工类型
	 */
	private Integer  workType;

	/**
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 离职日期
	 */
	private String dimissionDt;
	
	/**
	 * 所在小组失效日期
	 */
	private String disableDt;
	
	private String empPostType;
	
	private String transformDepartmentDate;

	private String transformPostTypeDate;
	
	private String sfDate;
	
	

	public String getTransformDepartmentDate() {
		return transformDepartmentDate;
	}

	public void setTransformDepartmentDate(String transformDepartmentDate) {
		this.transformDepartmentDate = transformDepartmentDate;
	}

	public String getTransformPostTypeDate() {
		return transformPostTypeDate;
	}

	public void setTransformPostTypeDate(String transformPostTypeDate) {
		this.transformPostTypeDate = transformPostTypeDate;
	}

	public String getSfDate() {
		return sfDate;
	}

	public void setSfDate(String sfDate) {
		this.sfDate = sfDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setCreateTm(String createTm) {
		this.createTm = createTm;
	}

	public void setModifiedTm(String modifiedTm) {
		this.modifiedTm = modifiedTm;
	}

	public void setDimissionDt(String dimissionDt) {
		this.dimissionDt = dimissionDt;
	}

	public void setDisableDt(String disableDt) {
		this.disableDt = disableDt;
	}

	public String getEmpPostType() {
		return empPostType;
	}

	public void setEmpPostType(String empPostType) {
		this.empPostType = empPostType;
	}

	/**
	 * 获取工号（系统自动生成，从100000000开始)
	 */
	public String getEmpCode() {
		return this.empCode;
	}

	/**
	 * 设置工号（系统自动生成，从100000000开始)
	 */
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	/**
	 * 获取姓名
	 */
	public String getEmpName() {
		return this.empName;
	}

	/**
	 * 设置姓名
	 */
	public void setEmpName(String empName) {
		this.empName = empName;
	}

	/**
	 * 获取状态1：在职，0：离职
	 */
/*	public Integer getEmpStatus() {
		return this.empStatus;
	}*/

	/**
	 * 设置状态1：在职，0：离职
	 */
/*	public void setEmpStatus(Integer empStatus) {
		this.empStatus = empStatus;
	}*/

	/**
	 * 获取职位
	 */
	public String getEmpDutyName() {
		return this.empDutyName;
	}

	/**
	 * 设置职位
	 */
	public void setEmpDutyName(String empDutyName) {
		this.empDutyName = empDutyName;
	}

	/**
	 * 获取网点ID
	 */
	public Long getDeptId() {
		return this.deptId;
	}

	/**
	 * 设置网点ID
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	/**
	 * 获取小组ID
	 */
	public Long getGroupId() {
		return this.groupId;
	}

	/**
	 * 设置小组ID
	 */
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}


	/**
	 * 获取创建人工号
	 */
	public String getCreateEmpCode() {
		return this.createEmpCode;
	}

	/**
	 * 设置创建人工号
	 */
	public void setCreateEmpCode(String createEmpCode) {
		this.createEmpCode = createEmpCode;
	}

	/**
	 * 获取修改人工号
	 */
	public String getModifiedEmpCode() {
		return this.modifiedEmpCode;
	}

	/**
	 * 设置修改人工号
	 */
	public void setModifiedEmpCode(String modifiedEmpCode) {
		this.modifiedEmpCode = modifiedEmpCode;
	}
	
	/**
	 * 获取用工类型
	 */
	public Integer getWorkType() {
		return workType;
	}

	/**
	 * 设置用工类型
	 */
	public void setWorkType(Integer workType) {
		this.workType = workType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
}