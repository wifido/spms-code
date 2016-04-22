package com.sf.module.warehouse.biz;

import java.util.HashMap;

import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.warehouse.dao.WarehouseValidateDao;

public class WarehouseValidateBiz extends BaseBiz {
	private HashMap<String, Object> resultMap;
	private WarehouseValidateDao warehouseValidateDao;
	private String ROOT = "ROOT";
	
	public HashMap<String, Object> validateAuthority(HashMap<String, String> queryParameter) {
		resultMap = new HashMap<String, Object>();
		if (getCurrentUser().getStatus().name().equals(ROOT)) {
			resultMap.put("success",true);
			return resultMap;
		}
		queryParameter.put("user_id", getCurrentUser().getId() + "");
		resultMap.put("success",warehouseValidateDao.validateAuthority(queryParameter) > 0);
		return resultMap;
	}

	public WarehouseValidateDao getWarehouseValidateDao() {
		return warehouseValidateDao;
	}

	public void setWarehouseValidateDao(WarehouseValidateDao warehouseValidateDao) {
		this.warehouseValidateDao = warehouseValidateDao;
	}

}
