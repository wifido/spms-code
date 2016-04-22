package com.sf.module.report.action;

import java.util.HashMap;
import java.util.Map;

import com.sf.framework.base.IPage;
import com.sf.module.common.action.SpmsBaseAction;
import com.sf.module.report.biz.SchedulingModifyBiz;
import com.sf.module.report.domain.SchedulingModify;

public class SchedulingModifyAction extends SpmsBaseAction {

	private static final long serialVersionUID = 1L;

	private SchedulingModifyBiz schedulingModifyBiz;
	private Map<String, Object> resultMap = new HashMap<String, Object>();
	private String fileName;

	private String departmentCode;
	private String yearMonth;
	private String empCode;

	private int start;
	private int limit;

	public String query() {
		IPage<SchedulingModify> result = schedulingModifyBiz.query(departmentCode, yearMonth, empCode, start, limit);
		resultMap.put("root", result.getData());
		resultMap.put("totalSize", result.getTotalSize());
		return SUCCESS;
	}

	public String export() {
		fileName = schedulingModifyBiz.exprot(departmentCode, yearMonth, empCode);
		this.resultMap.put("fileName", fileName);
		this.resultMap.put("success", true);

		return SUCCESS;
	}

	public void setSchedulingModifyBiz(SchedulingModifyBiz schedulingModifyBiz) {
		this.schedulingModifyBiz = schedulingModifyBiz;
	}

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
