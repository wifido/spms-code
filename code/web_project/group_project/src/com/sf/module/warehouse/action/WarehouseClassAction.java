package com.sf.module.warehouse.action;

import java.util.HashMap;
import java.util.List;

import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.common.util.ServletActionHelper;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.organization.domain.Department;
import com.sf.module.warehouse.biz.WarehouseClassBiz;

public class WarehouseClassAction extends BaseAction {

	private static final String ROOT = "root";
	private static final String DEPARTMENT_CODE = "departmentCode";
	private static final long serialVersionUID = 1L;
	public HashMap<String, Object> dataMap;
	private WarehouseClassBiz warehouseClassBiz;
	private boolean success;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public HashMap<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(HashMap<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public void setWarehouseClassBiz(WarehouseClassBiz warehouseClassBiz) {
		this.warehouseClassBiz = warehouseClassBiz;
	}

	public String query() {
		dataMap = new HashMap<String, Object>();
		dataMap = warehouseClassBiz.queryWarehouseClass(getRequestParameters());
		success = true;
		return SUCCESS;
	}

	public String add() {
		warehouseClassBiz.addWarehouseClass(getRequestParameters());
		success = true;
		return SUCCESS;
	}

	private HashMap<String, String> getRequestParameters() {
		return ServletActionHelper.getHttpRequestParameter();
	}

	public String update() {
		warehouseClassBiz.updateWarehouseClass(getRequestParameters());
		success = true;
		return SUCCESS;
	}

	public String delete() {
		dataMap = new HashMap<String, Object>();
		String msg = warehouseClassBiz.delete(getRequestParameters());
		dataMap.put("msg", msg);
		success = true;
		return SUCCESS;
	}

	public String queryClassByDeptId() {
		String departmentCode = getRequestParameters().get(DEPARTMENT_CODE);
		dataMap = new HashMap<String, Object>();
		Department department = DepartmentCacheBiz.getDepartmentByCode(departmentCode);
		if(null!=department){
			dataMap.put(ROOT, warehouseClassBiz.getDeptClass(department.getId()));
		}
		success = true;
		return SUCCESS;
	}
}
