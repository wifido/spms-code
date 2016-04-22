/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-6      350614        创建
 **********************************************/
package com.sf.module.vmsarrange.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sf.framework.base.domain.BaseEntity;
import com.sf.module.vmsarrange.log.cfg.Logcolumn;
import com.sf.module.vmsarrange.log.cfg.Logoperemp;
import com.sf.module.vmsarrange.log.cfg.Logopertm;
import com.sf.module.vmsarrange.log.cfg.Logtable;

/**
 *
 * 配班管理实体类
 *
 */
@Logtable(entitycode = "TM_ARR_SCHEDULE_ARRANGE", entityname = "配班管理", uniquedesc = { "arrangeNo" })
public class ScheduleArrange extends BaseEntity {
	/**
	 * @author 方芳 (350614)
	 * @date 2014-6-6 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//班次所属网点
	private ArrDepartment dept;
	//最早出车网点
	private ArrDepartment startDept;
	//最晚收车网点
	private ArrDepartment endDept;
	//班次编号
	private String arrangeNo;
	//1有效 0无效
	@Logcolumn(describe = "是否有效(1:是 0:否)", receive = "U")
	private Integer valid;
	@Logcolumn(describe = "上班时间", receive = "U")
	private String startTm;
	@Logcolumn(describe = "下班时间", receive = "U")
	private String endTm;
	private String createdEmpCode;
	private Date createdTm;
	@Logoperemp(describe = "操作人", receive = "U")
	private String modifiedEmpCode;
	@Logopertm(describe = "操作时间", receive = "U")
	private Date modifiedTm;
	//1.机动班 2.非机动班
	private Integer arrangeType;
	//0.未使用 1.已使用
	private Integer isUsed;
	//配班关系表
	private Set<ScheduleInfoArrange> scheduleArrangeInfos = new HashSet<ScheduleInfoArrange>(0);
	private List<ScheduleInfo> infoArrangeList = new ArrayList<ScheduleInfo>(0);
	//地区名称-无映射
	private String areaName;
	//excel序号列-无映射
	private String lineNo;
	@Logcolumn(describe = "修改班次信息", receive = "U")
	private String modifiedInfo;
	//版本锁
	private Integer version;
	
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getModifiedInfo() {
		return modifiedInfo;
	}
	public void setModifiedInfo(String modifiedInfo) {
		this.modifiedInfo = modifiedInfo;
	}
	public String getLineNo() {
		return lineNo;
	}
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Integer getArrangeType() {
		return arrangeType;
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
	/**
	 * 班次类型
	 * @param arrangeType 1.机动班 2.非机动班
	 */
	public void setArrangeType(Integer arrangeType) {
		this.arrangeType = arrangeType;
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
	public String getArrangeNo() {
		return arrangeNo;
	}
	public void setArrangeNo(String arrangeNo) {
		this.arrangeNo = arrangeNo;
	}
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
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
	public Set<ScheduleInfoArrange> getScheduleArrangeInfos() {
		return scheduleArrangeInfos;
	}
	public void setScheduleArrangeInfos(
			Set<ScheduleInfoArrange> scheduleArrangeInfos) {
		this.scheduleArrangeInfos = scheduleArrangeInfos;
	}
	public List<ScheduleInfo> getInfoArrangeList() {
		return infoArrangeList;
	}
	public void setInfoArrangeList(List<ScheduleInfo> infoArrangeList) {
		this.infoArrangeList = infoArrangeList;
	}
}
