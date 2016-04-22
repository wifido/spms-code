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

import com.sf.framework.base.domain.BaseEntity;

/**
 *
 * 配班关系表
 *
 */
public class ScheduleInfoArrange extends BaseEntity {
	/**
	 * @author 方芳 (350614)
	 * @date 2014-6-6 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long arrangeId;
	private ScheduleInfo scheduleInfo;
	private Long infoIdBak;
	
	public Long getInfoIdBak() {
		return infoIdBak;
	}
	public void setInfoIdBak(Long infoIdBak) {
		this.infoIdBak = infoIdBak;
	}
	public Long getArrangeId() {
		return arrangeId;
	}
	public void setArrangeId(Long arrangeId) {
		this.arrangeId = arrangeId;
	}
	public ScheduleInfo getScheduleInfo() {
		return scheduleInfo;
	}
	public void setScheduleInfo(ScheduleInfo scheduleInfo) {
		this.scheduleInfo = scheduleInfo;
	}
}
