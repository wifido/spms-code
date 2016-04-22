/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-25     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.dto;

/**
 *
 * ParamsDto处理类
 *
 */
public class ParamsDto {
	//被调班人的记录ID
	private Long scheduleId;
	//被调班人的日期
	private String dayDt;
	//需调班人的记录ID
	private Long scheduleIdOld;
	//需调班人的日期
	private String dayDtOld;
	//请假类型
	private String vacationComboValue;
	//调班类型标示
	private String flag;
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getDayDt() {
		return dayDt;
	}
	public void setDayDt(String dayDt) {
		this.dayDt = dayDt;
	}
	public String getVacationComboValue() {
		return vacationComboValue;
	}
	public void setVacationComboValue(String vacationComboValue) {
		this.vacationComboValue = vacationComboValue;
	}
	public Long getScheduleIdOld() {
		return scheduleIdOld;
	}
	public void setScheduleIdOld(Long scheduleIdOld) {
		this.scheduleIdOld = scheduleIdOld;
	}
	public String getDayDtOld() {
		return dayDtOld;
	}
	public void setDayDtOld(String dayDtOld) {
		this.dayDtOld = dayDtOld;
	}
}
