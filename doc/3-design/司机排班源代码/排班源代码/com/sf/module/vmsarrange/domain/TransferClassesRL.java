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
import com.sf.module.vmsarrange.util.ArrFileUtil;

/**
 *
 * TransferClassesRL实体类
 *
 */
public class TransferClassesRL extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	//实际排班ID
	private Long scheduleId;
	//配班 ID
	private ScheduleArrange arrangeRl;
	//实际排班中对应日期字段值如：ONE，TWO...THIRTY_ONE
	private Date dayDt;
	//配班信息代码
	private String arrangeNo;
	
	private Integer isTransfer;
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
	
	public String getArrangeNo() {
		return arrangeNo;
	}
	public void setArrangeNo(String arrangeNo) {
		this.arrangeNo = arrangeNo;
	}
	public ScheduleArrange getArrangeRl() {
		return arrangeRl;
	}
	public void setArrangeRl(ScheduleArrange arrangeRl) {
		this.arrangeRl = arrangeRl;
	}
	public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
	public Integer getIsTransfer() {
		return isTransfer;
	}
	public void setIsTransfer(Integer isTransfer) {
		this.isTransfer = isTransfer;
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
	public void setSafeRemark(String remark) {
		String remarkBak = null;
		if(!ArrFileUtil.isEmpty(this.remark)){
			remarkBak = this.remark+";"+remark;
		}else{
			remarkBak = remark;
		}
		if(null != remarkBak && remarkBak.getBytes().length>200){
			remarkBak = remarkBak.substring(0,60);
		}
		this.remark = remarkBak;
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
