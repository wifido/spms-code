package com.sf.module.dispatch.action;

import java.util.HashMap;
import static com.sf.module.common.util.ServletActionHelper.getHttpRequestParameter;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.dispatch.biz.MonitorReportBiz;

@Scope("prototype")
@Component("MonitorReport")
public class MonitorReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory
            .getLog(MonitorReportAction.class);
	private HashMap<String, Object> dataMap;
	private Boolean success = true;
	private String fileName;
	private String msg;
	@Resource
	private MonitorReportBiz monitorReportBiz; 
	
	public String query() {
		dataMap = monitorReportBiz.queryMonitorReport(getHttpRequestParameter());
		return SUCCESS;
	}
	
	public String export() {
		try {
			fileName =  monitorReportBiz.exportMonitorReport(getHttpRequestParameter());
		} catch (Exception e) {
			success = false;
			msg = e.getMessage();
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
	

}
