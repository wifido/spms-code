package com.sf.module.report.action;

import java.util.HashMap;

import com.sf.framework.core.exception.BizException;
import com.sf.module.common.action.SpmsBaseAction;
import com.sf.module.common.util.ServletActionHelper;
import com.sf.module.report.biz.SchedulingDetailBiz;

public class SchedulingDetailAction extends SpmsBaseAction {
	private static final long serialVersionUID = 1L;
	private SchedulingDetailBiz schedulingDetailBiz;
	private HashMap<String, Object> dataMap;
	private boolean success;
	private String fileName;
	private String msg;

	public String queryDetailReport() {
		try {
			dataMap = (HashMap) schedulingDetailBiz
					.queryDetailReport(getHttpRequestParameter());
			success = true;
			dataMap.put("success", true);
		} catch (Exception e) {
			log.error("error", e);
			dataMap.put("success", false);
			dataMap.put("msg", e.getMessage());
		}
		return SUCCESS;
	}

	public String exportReport() {
		try {
			fileName = schedulingDetailBiz.getExcel(getHttpRequestParameter());
			success = true;
		} catch (BizException e) {
			success = false;
			msg = e.getMessageKey();
		}
		return SUCCESS;
	}

	public String exportUserPermissionReport() {
		try {
			fileName = schedulingDetailBiz
					.exportUserPermission(getHttpRequestParameter());
			success = true;
		} catch (BizException e) {
			success = false;
			msg = e.getMessageKey();
		}
		return SUCCESS;
	}

	public HashMap<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(HashMap<String, Object> dataMap) {
		this.dataMap = dataMap;
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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public SchedulingDetailBiz getSchedulingDetailBiz() {
		return schedulingDetailBiz;
	}

	public HashMap<String, String> getHttpRequestParameter() {
		return ServletActionHelper.getHttpRequestParameter();
	}

	public void setSchedulingDetailBiz(SchedulingDetailBiz schedulingDetailBiz) {
		this.schedulingDetailBiz = schedulingDetailBiz;
	}
}
