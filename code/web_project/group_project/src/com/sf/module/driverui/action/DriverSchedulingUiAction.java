package com.sf.module.driverui.action;

import static com.google.common.collect.Maps.newHashMap;
import static com.sf.module.common.domain.Constants.LIMIT;
import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.domain.Constants.START;
import static com.sf.module.common.util.StringUtil.getMonthLeftPaddingWithZero;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.ServletActionHelper;
import com.sf.module.driver.biz.DriverSchedulingBiz;
import com.sf.module.driver.biz.LineConfigureBiz;
import com.sf.module.driver.domain.DriverScheduling;
import com.sf.module.driverui.biz.ApplyBiz;
import com.sf.module.driverui.domain.ApplyRecord;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.operation.dao.OutEmployeeDao;
import com.sf.module.operation.domain.OutEmployee;

@Scope("prototype")
@Controller
public class DriverSchedulingUiAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	@Resource
	private OutEmployeeDao outEmployeeDao;
	@Resource
	private DriverSchedulingBiz driverSchedulingBiz;
	@Resource
	private ApplyBiz applyBiz;
	@Resource
	private LineConfigureBiz lineConfigureBiz;

	private Map<String, Object> resultMap;
	private ApplyRecord applyRecord;
	private OutEmployee employee;
	private String employeeCode;
	private String yearWeek;
	private boolean success = true;
	private static final String  DRIVERS_LEADER = "司机组长";
	private static final String TOINDEXPAGE = "toIndexPage";
	private static final String TOLEADERPAGE = "toLeaderPage";

	public String toIndexPage() {
		return setEmployeeInfoToContextAndForward();
	}

	private String setEmployeeInfoToContextAndForward() {
		DateTime now = new DateTime();
		yearWeek = String.format("%s-%s", now.getYear(), getMonthLeftPaddingWithZero(now.getWeekOfWeekyear()));
		employee = getEmployeeInfo();
		if (employee.getEmpDutyName().indexOf(DRIVERS_LEADER) != -1){
			resultMap = applyBiz.queryPendingCount(employee.getEmpCode());
			return TOLEADERPAGE;
		}
		return TOINDEXPAGE;
	}
	
	public String toDriverLeaderPage(){
		resultMap = applyBiz.queryPendingCount(employeeCode);
		employee = getEmployeeInfo();
		return TOLEADERPAGE;
	}
	
	public String toPageIndexApp() {
		DateTime now = new DateTime();
		yearWeek = String.format("%s-%s", now.getYear(),
				getMonthLeftPaddingWithZero(now.getWeekOfWeekyear()));
		employee = getEmployeeInfo();
		return SUCCESS;
	}
	
	public String queryPendingCount() {
		resultMap = applyBiz.queryPendingCount(employeeCode);
		return SUCCESS;
	}

	private OutEmployee getEmployeeInfo() {
		DetachedCriteria dc = DetachedCriteria.forClass(OutEmployee.class);
		dc.add(Restrictions.eq("empCode", employeeCode));
		List<OutEmployee> employees = outEmployeeDao.findBy(dc);
		return employees.get(0);
	}

	public String myScheduling() {
		List<DriverScheduling> schedulings = driverSchedulingBiz.querySchedulingByEmployeeCode(
				employeeCode, yearWeek);
		resultMap = newHashMap();
		resultMap.put(ROOT, schedulings);
		return SUCCESS;
	}

	public String confirmScheduling() {
		driverSchedulingBiz.confirmScheduling(employeeCode, yearWeek);
		resultMap = newHashMap();
		resultMap.put(ROOT, SUCCESS);
		return SUCCESS;
	}
	
	public String toMyLeave() {
		employee = getEmployeeInfo();
		return "toMyLeave";
	}
	
	public String queryLeave() {
		resultMap = newHashMap();
		resultMap = applyBiz.queryLeave(employeeCode, ServletActionContext
				.getRequest().getParameter(START), ServletActionContext
				.getRequest().getParameter(LIMIT));
		return SUCCESS;
	}

	public String toLeave() {
		setApplyInfoCoding(false);
		applyBiz.applyLeave(applyRecord);
		return SUCCESS;
	}
	
	public String toMyExchangeScheduling() {
		employee = getEmployeeInfo();
		return SUCCESS;
	}
	
	public String myExchangScheduling() {
		HashMap<String, String> params = ServletActionHelper.getHttpRequestParameter();
		params.put("employeeCode", employeeCode);
		resultMap = applyBiz.queryMyExchangeScheduling(params);
		return SUCCESS;
	}
	
	public String exchangeScheduling() {
		setApplyRecordProperty();
		applyBiz.applyLeave(applyRecord);
		return SUCCESS;
	}

	private void setApplyRecordProperty() {
		employeeCode = applyRecord.getApplyEmployeeCode();
		employee = getEmployeeInfo();
		applyRecord.setApplyEmployeeCode(employee.getEmpCode());
		applyRecord.setDepartmentCode(DepartmentCacheBiz.getDepartmentByID(employee.getDeptId()).getDeptCode());
		applyRecord.setApplyType(2L);
		applyRecord.setStatus(1L);
		setApplyInfoCoding(true);
	}

	private void setApplyInfoCoding(boolean isExchangeScheduling) {
		try {
			String enCodeApplyInfo = java.net.URLEncoder.encode(applyRecord.getApplyInfo(), "ISO-8859-1");
			String deCodeApplyInfo = java.net.URLDecoder.decode(enCodeApplyInfo,"UTF-8");
			String enCodeOldConfigCode = java.net.URLEncoder.encode(applyRecord.getOldConfigCode(), "ISO-8859-1");
			String deCodeOldConfigCode = java.net.URLDecoder.decode(enCodeOldConfigCode,"UTF-8");
			if (isExchangeScheduling) {
				String enCodeNewConfigCode = java.net.URLEncoder.encode(applyRecord.getNewConfigCode(), "ISO-8859-1");
				String deCodeNewConfigCode = java.net.URLDecoder.decode(enCodeNewConfigCode,"UTF-8");
				applyRecord.setNewConfigCode(deCodeNewConfigCode);
			}
			applyRecord.setApplyInfo(deCodeApplyInfo);
			applyRecord.setOldConfigCode(deCodeOldConfigCode);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String queryLineConfigueForExchangeScheduling() {
		HashMap<String, String> params = ServletActionHelper.getHttpRequestParameter();
		employee = getEmployeeInfo();
		params.put("DEPT_CODE", DepartmentCacheBiz.getDepartmentByID(employee.getDeptId()).getAreaDeptCode());
		params.put("MONTH", getMonth(params));
		params.put("VALID_STATUS", String.valueOf(1));
		resultMap = lineConfigureBiz.queryLineConfigures(params);
		return SUCCESS;
	}

	private String getMonth(HashMap<String, String> params) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(DateFormatType.yyyyMMdd.format);
		String dayOfMonth = params.get("dayOfMonth");
		DateTime day = DateTime.parse(dayOfMonth, formatter);
		String month = String.format("%s-%s", day.getYear(), getMonthLeftPaddingWithZero(day.getMonthOfYear()));
		return month;
	}
	
	public String queryTheApprover() {
		HashMap<String, String> params = ServletActionHelper.getHttpRequestParameter();
		resultMap = applyBiz.queryTheApprover(params);
		return SUCCESS;
	}

	public String getYearWeek() {
		return yearWeek;
	}

	public void setYearWeek(String yearWeek) {
		this.yearWeek = yearWeek;
	}

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}

	public OutEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(OutEmployee employee) {
		this.employee = employee;
	}

	public ApplyRecord getApplyRecord() {
		return applyRecord;
	}

	public void setApplyRecord(ApplyRecord applyRecord) {
		this.applyRecord = applyRecord;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
}
