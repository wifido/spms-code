package com.sf.module.driver.domain;

import java.util.Date;
import com.sf.framework.base.domain.BaseEntity;

public class Vehicle extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long vehicleId; // 车辆编号
	private String vehicleCode; // 车牌号
	private Long deptId; // 网点
	private String brandModel; // 车辆品牌
	private Long driverId; // 司机工号
	private Long wheelbase; // 车辆轴距
	private String creator; // 创建人工号
	private Date createdTime; // 创建时间
	private String modifier; // 修改人
	private Date modifiedTime; // 修改时间
	private Date syncTime;// 同步时间

	public Long getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getVehicleCode() {
		return vehicleCode;
	}

	public void setVehicleCode(String vehicleCode) {
		this.vehicleCode = vehicleCode;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getBrandModel() {
		return brandModel;
	}

	public void setBrandModel(String brandModel) {
		this.brandModel = brandModel;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public Long getWheelbase() {
		return wheelbase;
	}

	public void setWheelbase(Long wheelbase) {
		this.wheelbase = wheelbase;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Date getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}

}
