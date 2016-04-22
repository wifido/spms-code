/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-16     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.domain;

import java.util.Date;

import com.sf.framework.base.domain.BaseEntity;

/**
 *
 * VehicleCertificates处理类
 *
 */
public class VehicleCertificates extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	//车辆类型
	private String vehicleType;
	//持证要求
	private String holderCertsNeed;
	//是否有效
	private Integer isValid;
	// 创建人
	private String createdEmpCode;
	// 创建时间
	private Date createdTm;
	// 修改人
	private String modifiedEmpCode;
	// 修改时间
	private Date modifiedTm;
	
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getHolderCertsNeed() {
		return holderCertsNeed;
	}
	public void setHolderCertsNeed(String holderCertsNeed) {
		this.holderCertsNeed = holderCertsNeed;
	}
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	public String getCreatedEmpCode() {
		return createdEmpCode;
	}
	public void setCreatedEmpCode(String createdEmpCode) {
		this.createdEmpCode = createdEmpCode;
	}
	public Date getCreatedTm() {
		return createdTm;
	}
	public void setCreatedTm(Date createdTm) {
		this.createdTm = createdTm;
	}
	public String getModifiedEmpCode() {
		return modifiedEmpCode;
	}
	public void setModifiedEmpCode(String modifiedEmpCode) {
		this.modifiedEmpCode = modifiedEmpCode;
	}
	public Date getModifiedTm() {
		return modifiedTm;
	}
	public void setModifiedTm(Date modifiedTm) {
		this.modifiedTm = modifiedTm;
	}
	
}
