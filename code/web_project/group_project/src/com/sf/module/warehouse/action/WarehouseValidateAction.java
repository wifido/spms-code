package com.sf.module.warehouse.action;

import java.util.HashMap;

import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.common.util.ServletActionHelper;
import com.sf.module.warehouse.biz.WarehouseValidateBiz;

public class WarehouseValidateAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private HashMap<String, Object> dataMap;
	private WarehouseValidateBiz warehouseValidateBiz;
	
	public String validateAuthority() {
		dataMap = warehouseValidateBiz.validateAuthority(ServletActionHelper.getHttpRequestParameter());
		return SUCCESS;
	}
	
	public HashMap<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(HashMap<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public WarehouseValidateBiz getWarehouseValidateBiz() {
		return warehouseValidateBiz;
	}
	
	public void setWarehouseValidateBiz(WarehouseValidateBiz warehouseValidateBiz) {
		this.warehouseValidateBiz = warehouseValidateBiz;
	}
	
	
}
