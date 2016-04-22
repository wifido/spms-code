/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-5-29     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.domain;

import java.util.Date;

import com.sf.framework.base.domain.BaseEntity;

/**
 *
 * Driver实体类
 *
 */
public class ArrDriver extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	//网点
	private ArrDepartment department;
	//驾驶员名字
	private String driverName;
	//驾驶员工号
	private String empCode;
	//准驾车型
	private String quasiDrivingType;
	//创建时间
	private Date createdTm;
	//创建人
	private String createdEmpCode;
	//修改时间
	private Date modifiedTm;
	//修改人
	private String modifiedEmpCode;
	//是否高管
	private Integer managerFlg;
	//是否有效
	private Integer valid;
	//工号+名称
	public String getCodeName(){
		return (null==empCode?"":empCode)+"/"+(null==driverName?"":driverName);
	}
	public Integer getManagerFlg() {
		return managerFlg;
	}
	public void setManagerFlg(Integer managerFlg) {
		this.managerFlg = managerFlg;
	}
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	public String getQuasiDrivingType() {
		return quasiDrivingType;
	}
	public void setQuasiDrivingType(String quasiDrivingType) {
		this.quasiDrivingType = quasiDrivingType;
	}
	public ArrDepartment getDepartment() {
		return department;
	}
	public void setDepartment(ArrDepartment department) {
		this.department = department;
	}
	public Date getCreatedTm() {
		return createdTm;
	}
	public void setCreatedTm(Date createdTm) {
		this.createdTm = createdTm;
	}
	public String getCreatedEmpCode() {
		return createdEmpCode;
	}
	public void setCreatedEmpCode(String createdEmpCode) {
		this.createdEmpCode = createdEmpCode;
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
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	
}
