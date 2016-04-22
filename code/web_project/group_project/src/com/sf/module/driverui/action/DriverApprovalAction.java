package com.sf.module.driverui.action;

import static com.google.common.collect.Maps.newHashMap;
import static com.sf.module.common.util.ServletActionHelper.getHttpRequestParameter;
import static com.sf.module.common.util.StringUtil.getMonthLeftPaddingWithZero;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.driverui.biz.ApplyBiz;
import com.sf.module.operation.dao.OutEmployeeDao;
import com.sf.module.operation.domain.OutEmployee;

@Scope("prototype")
@Controller
public class DriverApprovalAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private OutEmployee employee;
	@Resource
	private ApplyBiz applyBiz;
	@Resource
	private OutEmployeeDao outEmployeeDao;
	private Map<String, Object> resultMap = newHashMap();
	private String msg;
	private boolean success = true;
	private String yearWeek;
	private String employeeCode;
	
	public String queryPending() {
		resultMap = applyBiz.queryApprovalList(getHttpRequestParameter());
		return SUCCESS;
	}

	public String approval() {
		msg = applyBiz.updateApply(getHttpRequestParameter());
		return SUCCESS;
	}
	
	public String revokeLeave(){
		applyBiz.revokeLeaveApply(getHttpRequestParameter());
		return SUCCESS;
	}
	
	public String revokeExchangeScheduling(){
		resultMap.put("configureInfor", applyBiz.revokeExchangeScheduling(getHttpRequestParameter()));
		return SUCCESS;
	}

	public String transferForward() {
		return SUCCESS;
	}

	public String leaveForward() {
		return SUCCESS;
	}

	public String toLeaderMyScheduledPage() {
		DateTime now = new DateTime();
		yearWeek = String.format("%s-%s", now.getYear(),
				getMonthLeftPaddingWithZero(now.getWeekOfWeekyear()));
		employee = getEmployeeInfo();
		return SUCCESS;
	}

	private OutEmployee getEmployeeInfo() {
		DetachedCriteria dc = DetachedCriteria.forClass(OutEmployee.class);
		dc.add(Restrictions.eq("empCode", employeeCode));
		List<OutEmployee> employees = outEmployeeDao.findBy(dc);
		return employees.get(0);
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public OutEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(OutEmployee employee) {
		this.employee = employee;
	}

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}
	
	public String getYearWeek() {
		return yearWeek;
	}

	public void setYearWeek(String yearWeek) {
		this.yearWeek = yearWeek;
	}

}
