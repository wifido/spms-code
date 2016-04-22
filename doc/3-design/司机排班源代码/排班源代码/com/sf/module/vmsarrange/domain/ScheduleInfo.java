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

import java.util.Date;

import com.sf.framework.base.domain.BaseEntity;
import com.sf.module.vmsarrange.log.cfg.Logcolumn;
import com.sf.module.vmsarrange.log.cfg.Logoperemp;
import com.sf.module.vmsarrange.log.cfg.Logopertm;
import com.sf.module.vmsarrange.log.cfg.Logtable;

/**
 *
 * 班次信息实体类
 *
 */
@Logtable(entitycode = "TM_ARR_SCHEDULE_INFO", 
		entityname = "班次信息", 
		uniquedesc = { "startDept","endDept","startTm","endTm" }
)
public class ScheduleInfo extends BaseEntity {
	/**
	 * @author 方芳 (350614)
	 * @date 2014-5-30 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Logcolumn(describe = "归属网点",receive="U",isbean=true,refproperty="deptCode")
	private ArrDepartment dept;
	@Logcolumn(describe = "起始网点",receive="U",isbean=true,refproperty="deptCode")
	private ArrDepartment startDept;
	@Logcolumn(describe = "目的网点",receive="U",isbean=true,refproperty="deptCode")
	private ArrDepartment endDept;
	//1手工录入 2路径优化
	private Integer dataSource;
	private String lineOptimizeNo;
	//1有效 0无效
	@Logcolumn(describe = "是否有效(1:是 0:否)",receive="UD")
	private Integer valid;
	//是否被使用 1是 0否
	private Integer isUsed;
	private String modelBase;
	private String startTm;
	private String endTm;
	@Logcolumn(describe = "车牌号",receive="U",isbean=true,refproperty="vehicleCode")
	private ArrVehicle vehicle;
	private String createdEmpCode;
	private Date createdTm;
	@Logoperemp(describe = "操作人", receive = "UD")
	private String modifiedEmpCode;
	@Logopertm(describe = "操作时间", receive = "UD")
	private Date modifiedTm;
	//地区名称-无映射
	private String areaName;
	//起始网点：code+name-报表导出
	private String startDeptCodeName;
	//目的网点:code+name-报表导出
	private String endDeptCodeName;
	//归属网点:code+name-报表导出
	private String deptCodeName;
	//是否有效-中文描述-报表导出
	private String validTxt;
	//车牌号-报表导出
	private String vehicleCode;
	//版本锁
	private Integer version;
	
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getVehicleCode() {
		return vehicleCode;
	}
	public void setVehicleCode(String vehicleCode) {
		this.vehicleCode = vehicleCode;
	}
	public String getValidTxt() {
		return validTxt;
	}
	public void setValidTxt(String validTxt) {
		this.validTxt = validTxt;
	}
	public String getStartDeptCodeName() {
		return startDeptCodeName;
	}
	public void setStartDeptCodeName(String startDeptCodeName) {
		this.startDeptCodeName = startDeptCodeName;
	}
	public String getEndDeptCodeName() {
		return endDeptCodeName;
	}
	public void setEndDeptCodeName(String endDeptCodeName) {
		this.endDeptCodeName = endDeptCodeName;
	}
	public String getDeptCodeName() {
		return deptCodeName;
	}
	public void setDeptCodeName(String deptCodeName) {
		this.deptCodeName = deptCodeName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Integer getIsUsed() {
		return isUsed;
	}
	/**
	 * 此通过本方法无法修改值-慎用
	 * @param isUsed
	 */
	public void setIsUsed(Integer isUsed) {
		this.isUsed = isUsed;
	}
	public ArrDepartment getDept() {
		return dept;
	}
	public void setDept(ArrDepartment dept) {
		this.dept = dept;
	}
	public ArrDepartment getStartDept() {
		return startDept;
	}
	public void setStartDept(ArrDepartment startDept) {
		this.startDept = startDept;
	}
	public ArrDepartment getEndDept() {
		return endDept;
	}
	public void setEndDept(ArrDepartment endDept) {
		this.endDept = endDept;
	}
	public Integer getDataSource() {
		return dataSource;
	}
	public void setDataSource(Integer dataSource) {
		this.dataSource = dataSource;
	}
	public String getLineOptimizeNo() {
		return lineOptimizeNo;
	}
	public void setLineOptimizeNo(String lineOptimizeNo) {
		this.lineOptimizeNo = lineOptimizeNo;
	}
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	public String getModelBase() {
		return modelBase;
	}
	public void setModelBase(String modelBase) {
		this.modelBase = modelBase;
	}
	public String getStartTm() {
		return startTm;
	}
	public void setStartTm(String startTm) {
		this.startTm = startTm;
	}
	public String getEndTm() {
		return endTm;
	}
	public void setEndTm(String endTm) {
		this.endTm = endTm;
	}
	public ArrVehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(ArrVehicle vehicle) {
		this.vehicle = vehicle;
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
