package com.sf.module.driverui.domain;

import java.util.Date;
import com.sf.framework.base.domain.BaseEntity;
import com.sf.module.pushserver.domain.Message;

public class ApplyRecord extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long applyId;
	private String applyEmployeeCode;
	private String departmentCode;
	private String dayOfMonth;
	private Long applyType;
	private String applyInfo;
	private String oldConfigCode;
	private String newConfigCode;
	private Long status;
	private String approver;
	private String approverInfo;
	private Date apporveTime;

	public Long getApplyId() {
		return applyId;
	}

	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}

	public String getApplyEmployeeCode() {
		return applyEmployeeCode;
	}

	public void setApplyEmployeeCode(String applyEmployeeCode) {
		this.applyEmployeeCode = applyEmployeeCode;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public Long getApplyType() {
		return applyType;
	}

	public void setApplyType(Long applyType) {
		this.applyType = applyType;
	}

	public String getApplyInfo() {
		return applyInfo;
	}

	public void setApplyInfo(String applyInfo) {
		this.applyInfo = applyInfo;
	}

	public String getOldConfigCode() {
		return oldConfigCode;
	}

	public void setOldConfigCode(String oldConfigCode) {
		this.oldConfigCode = oldConfigCode;
	}

	public String getNewConfigCode() {
		return newConfigCode;
	}

	public void setNewConfigCode(String newConfigCode) {
		this.newConfigCode = newConfigCode;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getApproverInfo() {
		return approverInfo;
	}

	public void setApproverInfo(String approverInfo) {
		this.approverInfo = approverInfo;
	}

	public Date getApporveTime() {
		return apporveTime;
	}

	public void setApporveTime(Date apporveTime) {
		this.apporveTime = apporveTime;
	}

	public Message buildAprrovlResultMesseage() {
		Message message = new Message();
		message.setUserId(applyEmployeeCode);
		message.setStatus(0L);
		message.setCreateDate(new Date());
		message.setMsg(String.format(
		        "%s,您提交的 【%s】申请,已【%s】 %s",
		        applyEmployeeCode,
		        getApplyTypeString(applyType),
		        getApprovlResult(status),
		        getConfirmString(applyType, status)));
		return message;
	}

	public Message buildPendingAprrovlMesseage() {
		Message message = new Message();
		message.setUserId(approver);
		message.setStatus(0L);
		message.setCreateDate(new Date());
		message.setMsg(String.format("%s,【%s】提交的一个【 %s】申请,需要您审批！", approver, applyEmployeeCode, getApplyTypeString(applyType)));
		return message;
	}

	private String getApplyTypeString(Long type) {
		if (1L == type) {
			return "请假";
		}
		return "调班";
	}

	private String getConfirmString(Long type, Long status) {
		if (1L != type && 2L == status) {
			return " 请确认 ！";
		}
		return "";
	}

	private String getApprovlResult(Long status) {
		if (2L == status) {
			return "审批通过";
		}
		if (3L == status) {
			return "驳回";
		}
		return "";
	}
}
