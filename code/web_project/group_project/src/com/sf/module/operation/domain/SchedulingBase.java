/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-18     632898 李鹏       创建
 **********************************************/

package com.sf.module.operation.domain;

import java.util.Date;

import com.sf.framework.base.domain.BaseEntity;

/**
 * 
 * 班次基础信息管理
 * 
 * @author 632898 李鹏 2014-06-18
 * 
 */
public class SchedulingBase extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 班别代码
	 */
	private String scheduleCode;

	/**
	 * 班别名称
	 */
	private String scheduleName;

	/**
	 * 网点ID
	 */
	private Long deptId;

	/**
	 * 开始时间一
	 */
	private String start1Time;

	/**
	 * 结束时间一
	 */
	private String end1Time;

	/**
	 * 开始时间二
	 */
	private String start2Time;

	/**
	 * 结束时间二
	 */
	private String end2Time;

	/**
	 * 开始时间三
	 */
	private String start3Time;

	/**
	 * 结束时间三
	 */
	private String end3Time;

	/**
	 * 生效日期
	 */
	private Date enableDt;

	/**
	 * 失效日期
	 */
	private Date disableDt;

	/**
	 * 创建时间
	 */
	private Date createTm;

	/**
	 * 修改时间
	 */
	private Date modifiedTm;

	/**
	 * 创建人工号
	 */
	private String createEmpCode;

	/**
	 * 修改人工号
	 */
	private String modifiedEmpCode;
	
	/**
	 * 班别类型 1动作，2仓管
	 */
	private String ClassType;
	
	private String ym;
	
	public String getYm() {
		return ym;
	}

	public void setYm(String ym) {
		this.ym = ym;
	}

	public String getClassType() {
		return ClassType;
	}

	public void setClassType(String classType) {
		ClassType = classType;
	}

	/**
	 * 获取班别代码
	 */
	public String getScheduleCode() {
		return this.scheduleCode;
	}

	/**
	 * 设置班别代码
	 */
	public void setScheduleCode(String scheduleCode) {
		this.scheduleCode = scheduleCode;
	}

	/**
	 * 获取班别名称
	 */
	public String getScheduleName() {
		return this.scheduleName;
	}

	/**
	 * 设置班别名称
	 */
	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
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
	 * 获取开始时间一
	 */
	public String getStart1Time() {
		return this.start1Time;
	}

	/**
	 * 设置开始时间一
	 */
	public void setStart1Time(String start1Time) {
		this.start1Time = start1Time;
	}

	/**
	 * 获取结束时间一
	 */
	public String getEnd1Time() {
		return this.end1Time;
	}

	/**
	 * 设置结束时间一
	 */
	public void setEnd1Time(String end1Time) {
		this.end1Time = end1Time;
	}

	/**
	 * 获取开始时间二
	 */
	public String getStart2Time() {
		return this.start2Time;
	}

	/**
	 * 设置开始时间二
	 */
	public void setStart2Time(String start2Time) {
		this.start2Time = start2Time;
	}

	/**
	 * 获取结束时间二
	 */
	public String getEnd2Time() {
		return this.end2Time;
	}

	/**
	 * 设置结束时间二
	 */
	public void setEnd2Time(String end2Time) {
		this.end2Time = end2Time;
	}

	/**
	 * 获取开始时间三
	 */
	public String getStart3Time() {
		return this.start3Time;
	}

	/**
	 * 设置开始时间三
	 */
	public void setStart3Time(String start3Time) {
		this.start3Time = start3Time;
	}

	/**
	 * 获取结束时间三
	 */
	public String getEnd3Time() {
		return this.end3Time;
	}

	/**
	 * 设置结束时间三
	 */
	public void setEnd3Time(String end3Time) {
		this.end3Time = end3Time;
	}

	/**
	 * 获取生效日期
	 */
	public Date getEnableDt() {
		return this.enableDt;
	}

	/**
	 * 设置生效日期
	 */
	public void setEnableDt(Date enableDt) {
		this.enableDt = enableDt;
	}

	/**
	 * 获取失效日期
	 */
	public Date getDisableDt() {
		return this.disableDt;
	}

	/**
	 * 设置失效日期
	 */
	public void setDisableDt(Date disableDt) {
		this.disableDt = disableDt;
	}

	/**
	 * 获取创建时间
	 */
	public Date getCreateTm() {
		return this.createTm;
	}

	/**
	 * 设置创建时间
	 */
	public void setCreateTm(Date createTm) {
		this.createTm = createTm;
	}

	/**
	 * 获取修改时间
	 */
	public Date getModifiedTm() {
		return this.modifiedTm;
	}

	/**
	 * 设置修改时间
	 */
	public void setModifiedTm(Date modifiedTm) {
		this.modifiedTm = modifiedTm;
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
}