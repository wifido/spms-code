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
 * TransferClassesDF实体类
 *
 */
public class TransferClassesDF extends BaseEntity {

	private static final long serialVersionUID = 1L;
	//预排班ID
	private Long scheduleDfId;
	//配班ID
	private ScheduleArrange arrangeDf;
	//预排班的日期
	private Date dayDt;
	//配班信息代码
	private String arrangeNo;
	//备注
	private String remark;
	//创建时间
	private Date createdTm;
	//创建人
	private String createdEmpCode;
	//修改时间
	private Date modifiedTm;
	//修改人
	private String modifiedEmpCode;
	
	public Long getScheduleDfId() {
		return scheduleDfId;
	}
	public void setScheduleDfId(Long scheduleDfId) {
		this.scheduleDfId = scheduleDfId;
	}
	public String getArrangeNo() {
		return arrangeNo;
	}
	public void setArrangeNo(String arrangeNo) {
		this.arrangeNo = arrangeNo;
	}
	public ScheduleArrange getArrangeDf() {
		return arrangeDf;
	}
	public void setArrangeDf(ScheduleArrange arrangeDf) {
		this.arrangeDf = arrangeDf;
	}
	
	public Date getDayDt() {
		return dayDt;
	}
	public void setDayDt(Date dayDt) {
		this.dayDt = dayDt;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	
	
}
