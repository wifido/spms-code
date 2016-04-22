package com.sf.module.warehouse.action;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.common.util.ServletActionHelper;
import com.sf.module.warehouse.biz.WarehouseSchedulingBiz;
import com.sf.module.warehouse.biz.WarehouseSchedulingImportBiz;

public class WarehouseSchedulingAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private File uploadFile;
	public Map<String, Object> dataMap;
	private WarehouseSchedulingBiz warehouseSchedulingBiz;
	private WarehouseSchedulingImportBiz warehouseSchedulingImportBiz;
	private boolean success = true;

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public void setWarehouseSchedulingBiz(WarehouseSchedulingBiz warehouseSchedulingBiz) {
		this.warehouseSchedulingBiz = warehouseSchedulingBiz;
	}

	public void setWarehouseSchedulingImportBiz(WarehouseSchedulingImportBiz warehouseSchedulingImportBiz) {
		this.warehouseSchedulingImportBiz = warehouseSchedulingImportBiz;
	}

	private static HashMap<String, String> getRequestParameters() {
		return ServletActionHelper.getHttpRequestParameter();
	}

	public String importRec() {
		dataMap = warehouseSchedulingImportBiz.importExcel(uploadFile, getRequestParameters());
		return SUCCESS;
	}

	public String query() {
		dataMap = warehouseSchedulingBiz.querySchedulingInfomation(getRequestParameters());
		return SUCCESS;
	}

	public String queryEmployeeInfo() {
		dataMap = warehouseSchedulingBiz.getEmployeeByEmpCode(getRequestParameters());
		return SUCCESS;
	}

	public String valid() {
		dataMap = warehouseSchedulingBiz.validSchedulingData(getRequestParameters());
		return SUCCESS;
	}

	public String update() {
		warehouseSchedulingBiz.updateSchedulingData(getRequestParameters());
		return SUCCESS;
	}

	public String queryNotSchedulingStaff() {
		dataMap = warehouseSchedulingBiz.queryNotSchedulingStaff(getRequestParameters());
		return SUCCESS;
	}

	public String addScheduling() {
		warehouseSchedulingBiz.addScheduling(getRequestParameters());
		return SUCCESS;
	}

	public String delete() {
		warehouseSchedulingBiz.deleteScheduling(getRequestParameters());
		return SUCCESS;
	}

	public String export() {
		dataMap = warehouseSchedulingBiz.exportScheduling(getRequestParameters());
		return SUCCESS;
	}

	public String queryIsBeOverdue() {
		success = warehouseSchedulingBiz.queryIsBeOverdue(getRequestParameters());
		return SUCCESS;
	}

	public String exportNoSchedulingEmployee() {
		dataMap = warehouseSchedulingBiz.exportNoSchedulingEmployee(getRequestParameters());
		return SUCCESS;
	}
}
