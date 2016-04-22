package com.sf.module.report.action;

import static com.sf.module.common.util.ServletActionHelper.getHttpRequestParameter;

import java.util.HashMap;

import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.common.util.ServletActionHelper;
import com.sf.module.report.biz.SchedulingCoincidenceRateBiz;

public class SchedulingCoincidenceRateAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private SchedulingCoincidenceRateBiz schedulingCoincidenceRateBiz;

	private HashMap<String, Object> resultMap = new HashMap<String, Object>();
	
	private String fileName;
	
	private boolean success;
	
	public String query() {
		resultMap = schedulingCoincidenceRateBiz.query(getQueryValueByKey("departmentCode"), 
				getQueryValueByKey("yearMonth"),
				getQueryValueByKey("positionType"),
				ServletActionHelper.getStart(getHttpRequestParameter()),
				ServletActionHelper.getLimit(getHttpRequestParameter()));
		success = true;
		return SUCCESS;
	}
	
	public String export() {
		fileName = schedulingCoincidenceRateBiz.export(getQueryValueByKey("departmentCode"), 
				getQueryValueByKey("yearMonth"),
				getQueryValueByKey("positionType"));
		success = true;
		return SUCCESS;
	}
	
	private String getQueryValueByKey(String key) {
		return getHttpRequestParameter().get(key);
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public HashMap<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(HashMap<String, Object> resultMap) {
		this.resultMap = resultMap;
	}

	public SchedulingCoincidenceRateBiz getSchedulingCoincidenceRateBiz() {
		return schedulingCoincidenceRateBiz;
	}

	public void setSchedulingCoincidenceRateBiz(SchedulingCoincidenceRateBiz schedulingCoincidenceRateBiz) {
		this.schedulingCoincidenceRateBiz = schedulingCoincidenceRateBiz;
	}
	
}
