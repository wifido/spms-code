/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-17     houjingyu       创建
 **********************************************/

package com.sf.module.operation.domain;

import java.util.Date;

import com.sf.framework.base.domain.BaseEntity;

/**
 * 
 * 排班
 * 
 * @author houjingyu 2014-06-17
 * 
 */
public class Scheduling extends BaseEntity {

	private static final long serialVersionUID = 1L;
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else {
			if (this.getClass() == obj.getClass()) {
				Scheduling u = (Scheduling) obj;
				if (this.getEmpCode().equals(u.getEmpCode())
						&& this.getDeptId().equals(u.getDeptId())
						&& this.getSheduleDt().equals(u.getSheduleDt())) {
					return true;
				} else {
					return false;
				}

			} else {
				return false;
			}
		}
	}

	/**
	 * 班别ID
	 */
	private Long sheduleId;

	private String scheduleCode;
	/**
	 * 网点ID
	 */
	private Long deptId;
	
	/**
	 * 排班日期
	 */
	private Date sheduleDt;

	private Long sheduleMonId;
	/**
	 * 被排班人工号
	 */
	private String empCode;

	/**
	 * 创建日期
	 */
	private Date createTm;

	/**
	 * 修改日期
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
	
	private Integer synchroStatus;
	
	private int commitStatus;
	
	public Integer getSynchroStatus() {
		return synchroStatus;
	}

	public void setSynchroStatus(Integer synchroStatus) {
		this.synchroStatus = synchroStatus;
	}

	public int getCommitStatus() {
		return commitStatus;
	}

	public void setCommitStatus(int commitStatus) {
		this.commitStatus = commitStatus;
	}

	/**
	 * 获取班别ID
	 */
	public Long getSheduleId() {
		return this.sheduleId;
	}

	/**
	 * 设置班别ID
	 */
	public void setSheduleId(Long sheduleId) {
		this.sheduleId = sheduleId;
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
	 * 获取排班日期
	 */
	public Date getSheduleDt() {
		return this.sheduleDt;
	}

	/**
	 * 设置排班日期
	 */
	public void setSheduleDt(Date sheduleDt) {
		this.sheduleDt = sheduleDt;
	}

	/**
	 * 获取被排班人工号
	 */
	public String getEmpCode() {
		return this.empCode;
	}

	/**
	 * 设置被排班人工号
	 */
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	/**
	 * 获取创建日期
	 */
	public Date getCreateTm() {
		return this.createTm;
	}

	/**
	 * 设置创建日期
	 */
	public void setCreateTm(Date createTm) {
		this.createTm = createTm;
	}

	/**
	 * 获取修改日期
	 */
	public Date getModifiedTm() {
		return this.modifiedTm;
	}

	/**
	 * 设置修改日期
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

	public Long getSheduleMonId() {
		return sheduleMonId;
	}

	public void setSheduleMonId(Long sheduleMonId) {
		this.sheduleMonId = sheduleMonId;
	}

	public String getScheduleCode() {
		return scheduleCode;
	}
	

	public void setScheduleCode(String scheduleCode) {
		this.scheduleCode = scheduleCode;
	}

	
}