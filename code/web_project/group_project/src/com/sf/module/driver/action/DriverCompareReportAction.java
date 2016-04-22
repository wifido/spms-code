package com.sf.module.driver.action;

import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.common.domain.ExportResult;
import com.sf.module.driver.biz.DriverCompareReportBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class DriverCompareReportAction extends BaseAction {
	@Autowired
	private DriverCompareReportBiz driverCompareReportBiz;

	private Integer[] errorType;
	private String startTime;
	private String endTime;
	private String departmentCode;
	private ExportResult exprotResult;

	public String exportReport() {
		exprotResult = driverCompareReportBiz.exportToFile(departmentCode, errorType, startTime, endTime);
		return SUCCESS;
	}

	public Integer[] getErrorType() {
		return errorType;
	}

	public void setErrorType(Integer[] errorType) {
		this.errorType = errorType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public ExportResult getExprotResult() {
		return exprotResult;
	}

	public void setExprotResult(ExportResult exprotResult) {
		this.exprotResult = exprotResult;
	}

}
