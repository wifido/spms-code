package com.sf.module.report.action;

import static com.sf.module.common.util.ServletActionHelper.getHttpRequestParameter;

import java.util.HashMap;

import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.common.util.ServletActionHelper;
import com.sf.module.report.biz.SchedulingTableBiz;

public class SchedulingTableAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private SchedulingTableBiz schedulingTableBiz;

	private HashMap<String, Object> resultMap = new HashMap<String, Object>();

	private boolean success;
	
	private String fileName;

	public String query() {
		resultMap = schedulingTableBiz.query(getQueryValueByKey("departmentCode"), getQueryValueByKey("yearMonth"), 
				ServletActionHelper.getStart(getHttpRequestParameter()), ServletActionHelper.getLimit(getHttpRequestParameter()));
		success = true;
		return SUCCESS;
	}
	
	public String export() {
		fileName = schedulingTableBiz.export(getQueryValueByKey("departmentCode"), 
				getQueryValueByKey("yearMonth"));
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

	public HashMap<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(HashMap<String, Object> resultMap) {
		this.resultMap = resultMap;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public SchedulingTableBiz getSchedulingTableBiz() {
		return schedulingTableBiz;
	}

	public void setSchedulingTableBiz(SchedulingTableBiz schedulingTableBiz) {
		this.schedulingTableBiz = schedulingTableBiz;
	}

}
