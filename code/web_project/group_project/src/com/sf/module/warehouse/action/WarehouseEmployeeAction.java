package com.sf.module.warehouse.action;

import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.common.domain.Constants;
import com.sf.module.common.util.ServletActionHelper;
import com.sf.module.warehouse.biz.WarehouseEmployeeBiz;

import java.util.HashMap;
import java.util.List;

public class WarehouseEmployeeAction extends BaseAction {
	private static final long serialVersionUID = 1L;
    private static final String EMPLOYEE_DEPARTMENT_CODE = "employeeDepartmentCode";
    private static final String DYNAMIC_DEPARTMENT_CODE = "dynamicDepartmentCode";
    private WarehouseEmployeeBiz warehouseEmployeeBiz;
	private HashMap<String, Object> dataMap;
	private boolean success = true;

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

	public void setWarehouseEmployeeBiz(
			WarehouseEmployeeBiz warehouseEmployeeBiz) {
		this.warehouseEmployeeBiz = warehouseEmployeeBiz;
	}

	public String query() {
		dataMap = warehouseEmployeeBiz
				.queryStaffInformation(getRequestParameters());
		success = true;
		return SUCCESS;
	}

	private static HashMap<String, String> getRequestParameters() {
		return ServletActionHelper.getHttpRequestParameter();
	}

	public String update() {
		warehouseEmployeeBiz.updateWarehouseEmployee(getRequestParameters());
		success = true;
		return SUCCESS;
	}

    public String validDynamicDepartmentCode() {
        HashMap<String, String> queryParameter = ServletActionHelper.getHttpRequestParameter();
        String dynamicDepartmentCode = queryParameter.get(DYNAMIC_DEPARTMENT_CODE);

        dataMap = new HashMap<String, Object>();
        if (!warehouseEmployeeBiz.validDynamicDepartmentCode(dynamicDepartmentCode)) {
            dataMap.put(Constants.KEY_SUCCESS, "false");
        }
        return SUCCESS;
    }

	public String queryEmployeeWorkTypeByPostType() {
		dataMap = new HashMap<String, Object>();
		List list = warehouseEmployeeBiz.queryEmployeeWorkTypeByPostType(getRequestParameters()
				.get("postType"));
		dataMap.put("root", list);
		dataMap.put("success", true);
		return SUCCESS;
	}
}
