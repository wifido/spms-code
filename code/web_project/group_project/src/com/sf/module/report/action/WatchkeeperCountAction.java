package com.sf.module.report.action;

import static com.sf.module.common.util.ServletActionHelper.getHttpRequestParameter;

import java.util.HashMap;

import com.sf.module.common.action.SpmsBaseAction;
import com.sf.module.common.util.ServletActionHelper;
import com.sf.module.report.biz.WatchkeeperCountBiz;

public class WatchkeeperCountAction extends SpmsBaseAction{

	private static final long serialVersionUID = 1L;
	
	private WatchkeeperCountBiz watchkeeperCountBiz;
	private HashMap<String, Object> resultMap = new HashMap<String, Object>();
	private boolean success;	
	private String fileName;
	
	
	public String query(){
		resultMap = watchkeeperCountBiz.query(getQueryValueByKey("departmentCode"), getQueryValueByKey("dayOfMonth"), 
				ServletActionHelper.getStart(getHttpRequestParameter()), ServletActionHelper.getLimit(getHttpRequestParameter()));
		success = true;
		return SUCCESS;
	}
	
	public String export(){
		fileName = watchkeeperCountBiz.export(getQueryValueByKey("departmentCode"), getQueryValueByKey("dayOfMonth"));
		success = true;
		return SUCCESS;
	}
	
	private String getQueryValueByKey(String key) {
		return getHttpRequestParameter().get(key);
	}

	public WatchkeeperCountBiz getWatchkeeperCountBiz() {
		return watchkeeperCountBiz;
	}

	public void setWatchkeeperCountBiz(WatchkeeperCountBiz watchkeeperCountBiz) {
		this.watchkeeperCountBiz = watchkeeperCountBiz;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	

}
