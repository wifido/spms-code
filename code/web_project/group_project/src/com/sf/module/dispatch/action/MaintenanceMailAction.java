package com.sf.module.dispatch.action;

import static com.sf.module.common.util.ServletActionHelper.getHttpRequestParameter;

import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.dispatch.biz.MaintenanceMailBiz;
import com.sf.module.dispatch.domain.MaintenanceMail;

@Scope("prototype")
@Component("MaintenanceMail")
public class MaintenanceMailAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory
            .getLog(MaintenanceMailAction.class);
	@Resource
	private MaintenanceMailBiz maintenanceMailBiz;
	private MaintenanceMail maintenanceMail;
	private HashMap<String, Object> dataMap;
	private Boolean success = true;
	private String msg;
	private long[] ids;
	
	public String query() {
		dataMap  = maintenanceMailBiz.queryMaintenanceMailByDeptCode(getHttpRequestParameter());
		return SUCCESS;
	}
	
	public String saveOrUpdate() {
		try {
			maintenanceMailBiz.saveOrUpdate(maintenanceMail);
		} catch (Exception e) {
			msg = e.getMessage();
		}
		maintenanceMail = null;
		return SUCCESS;
	}
	
	public String validExists() {
		dataMap = new HashMap<String, Object>();
		dataMap.put("success", maintenanceMailBiz.validExists(getHttpRequestParameter()));
		return SUCCESS;
	}
	
	public String remove() {
		try{
			maintenanceMailBiz.remove(getHttpRequestParameter());
		} catch (BizException e) {
			msg = e.getMessage();
		}
		return SUCCESS;
	}
	

	public MaintenanceMail getMaintenanceMail() {
		return maintenanceMail;
	}

	public void setMaintenanceMail(MaintenanceMail maintenanceMail) {
		this.maintenanceMail = maintenanceMail;
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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
