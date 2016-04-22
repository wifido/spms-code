package com.sf.module.warehouse.biz;

import static com.sf.module.common.domain.Constants.LIMIT;
import static com.sf.module.common.domain.Constants.START;
import static java.lang.String.valueOf;

import java.util.HashMap;
import java.util.List;

import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.dao.IOssDepartmentDao;
import com.sf.module.common.domain.OssDepartment;
import com.sf.module.common.util.DepartmentHelper;
import com.sf.module.warehouse.dao.WarehouseEmployeeDao;

public class WarehouseEmployeeBiz extends BaseBiz {
	private WarehouseEmployeeDao warehouseEmployeeDao;
	private IOssDepartmentDao ossDepartmentDao;
	HashMap<String, Object> resultMap;
	private static final String DEPT_CODE = "dept_id";

	public void setWarehouseEmployeeDao(
			WarehouseEmployeeDao warehouseEmployeeDao) {
		this.warehouseEmployeeDao = warehouseEmployeeDao;
	}

	public void setOssDepartmentDao(IOssDepartmentDao ossDepartmentDao) {
		this.ossDepartmentDao = ossDepartmentDao;
	}

	public HashMap<String, Object> queryStaffInformation(
			HashMap<String, String> queryParameter) {
		resultMap = new HashMap<String, Object>();

        queryParameter.put(LIMIT, valueOf(Integer.parseInt(queryParameter.get(START)) + Integer.parseInt(queryParameter.get(LIMIT))));

        checkOfNetworks(queryParameter);
        
        int totalSize = warehouseEmployeeDao
				.queryStaffInformationCount(queryParameter);
		List resultList = warehouseEmployeeDao
				.queryStaffInformation(queryParameter);
		resultMap.put("totalSize", totalSize);
		resultMap.put("root", resultList);
		return resultMap;
	}

	private void checkOfNetworks(HashMap<String, String> queryParameter) {
        OssDepartment department = ossDepartmentDao.getDepartmentByDeptCode(queryParameter.get(DEPT_CODE).replace("'", ""));
        DepartmentHelper.isCheckOfNetwork(department, queryParameter);
	}

    public boolean updateWarehouseEmployee(HashMap<String, String> queryCriteria) {
		// set employee code parameter that of to update the
		// warehouseClass employee code
		queryCriteria.put("MODIFIED_EMP_CODE", this.getCurrentUser()
				.getEmployee().getCode());
		return warehouseEmployeeDao.updateWarehouseEmployee(queryCriteria);
	}

	public List queryEmployeeWorkTypeByPostType(String postType) {
		return warehouseEmployeeDao.queryEmployeeWorkTypeByPostType(postType);
	}

    public boolean validDynamicDepartmentCode(String dynamicDepartmentCode) {
        return warehouseEmployeeDao.validDynamicDepartmentCode(dynamicDepartmentCode);
    }
}
