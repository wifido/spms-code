/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-5-30      350614        创建
 **********************************************/
package com.sf.module.vmsarrange.domain;

import com.sf.framework.base.domain.BaseEntity;

/**
 *
 * 车辆表视图实体类
 *
 */
public class ArrVehicle extends BaseEntity {
	/**
	 * @author 方芳 (350614)
	 * @date 2014-5-30 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrDepartment department;
	private String brandModel;
	//车辆类型与持证要求维护的类
	private VehicleCertificates vehicleDrivingType;
	private Long wheelbase;
	private String vehicleCode;
	private Integer vehicleState;
	private Integer usage;
	
	/**
	 * 获取品牌轴距
	 * @return
	 */
	public String getModelBase(){
		if((null == brandModel || "".equals(brandModel)) 
				&& (null == wheelbase || "".equals(wheelbase))){
			return null;
		}
		return (null == brandModel?"":brandModel)+(null == wheelbase?"":wheelbase);
	}
	public Integer getUsage() {
		return usage;
	}
	public void setUsage(Integer usage) {
		this.usage = usage;
	}
	public ArrDepartment getDepartment() {
		return department;
	}
	public void setDepartment(ArrDepartment department) {
		this.department = department;
	}
	public String getBrandModel() {
		return brandModel;
	}
	public void setBrandModel(String brandModel) {
		this.brandModel = brandModel;
	}
	public Long getWheelbase() {
		return wheelbase;
	}
	public void setWheelbase(Long wheelbase) {
		this.wheelbase = wheelbase;
	}
	public String getVehicleCode() {
		return vehicleCode;
	}
	public void setVehicleCode(String vehicleCode) {
		this.vehicleCode = vehicleCode;
	}
	public Integer getVehicleState() {
		return vehicleState;
	}
	public void setVehicleState(Integer vehicleState) {
		this.vehicleState = vehicleState;
	}
	public VehicleCertificates getVehicleDrivingType() {
		return vehicleDrivingType;
	}
	public void setVehicleDrivingType(VehicleCertificates vehicleDrivingType) {
		this.vehicleDrivingType = vehicleDrivingType;
	}
	
}
