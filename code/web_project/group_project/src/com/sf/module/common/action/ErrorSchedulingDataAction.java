package com.sf.module.common.action;

import static com.sf.module.common.util.ServletActionHelper.getHttpRequestParameter;

import java.util.Map;

import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.common.biz.ErrorSchedulingDataBiz;

public class ErrorSchedulingDataAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private Map<String, Object> resultMap;

	private ErrorSchedulingDataBiz errorSchedulingDataBiz;

	private String fileName;
	
	private boolean success;

	public String query() {
		resultMap = errorSchedulingDataBiz.query(getHttpRequestParameter());
		success = true;
		return SUCCESS;
	}

	public String export() {
		fileName = errorSchedulingDataBiz.exporErrorSchedulingData(getHttpRequestParameter());
		success = true;
		return SUCCESS;
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

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

	public void setErrorSchedulingDataBiz(ErrorSchedulingDataBiz errorSchedulingDataBiz) {
		this.errorSchedulingDataBiz = errorSchedulingDataBiz;
	}

	public ErrorSchedulingDataBiz getErrorSchedulingDataBiz() {
		return errorSchedulingDataBiz;
	}
}
