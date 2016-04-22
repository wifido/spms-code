package com.sf.module.warehouse.biz;

import static com.sf.module.common.domain.Constants.LIMIT;
import static com.sf.module.common.domain.Constants.START;
import static java.lang.String.valueOf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.dao.IOssDepartmentDao;
import com.sf.module.common.domain.OssDepartment;
import com.sf.module.common.util.DepartmentHelper;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.warehouse.dao.WarehouseClassDao;

public class WarehouseClassBiz extends BaseBiz {
	private IOssDepartmentDao ossDepartmentDao;
	
	private WarehouseClassDao warehouseClassDao;
	
	private static final String DEPT_CODE = "DEPT_CODE";

	public void setOssDepartmentDao(IOssDepartmentDao ossDepartmentDao) {
		this.ossDepartmentDao = ossDepartmentDao;
	}

	public void setWarehouseClassDao(WarehouseClassDao warehouseClassDao) {
		this.warehouseClassDao = warehouseClassDao;
	}

	private HashMap<String, Object> resultMap;

	public HashMap<String, Object> queryWarehouseClass(
			HashMap<String, String> queryParameter) {
		resultMap = new HashMap<String, Object>();

        queryParameter.put(LIMIT, valueOf(Integer.parseInt(queryParameter.get(START)) + Integer.parseInt(queryParameter.get(LIMIT))));
        
        checkOfNetworks(queryParameter);
        
        int totalSize = warehouseClassDao
				.queryClassInformationCount(queryParameter);
		List resultList = warehouseClassDao
				.queryClassInformation(queryParameter);

		resultMap.put("totalSize", totalSize);
		resultMap.put("root", resultList);
		return resultMap;
	}

	private void checkOfNetworks(HashMap<String, String> queryParameter) {
        OssDepartment department = ossDepartmentDao.getDepartmentByDeptCode(queryParameter.get(DEPT_CODE).replace("'", ""));
        DepartmentHelper.isCheckOfNetwork(department, queryParameter);
	}
	
	public boolean addWarehouseClass(HashMap<String, String> queryParameter) {

		// set employee code parameter that of to create the warehouseClass
		// employee code
		queryParameter.put("create_emp_code", this.getCurrentUser()
				.getEmployee().getCode());

		// the 'dept_id' parameter stored content is like '755A/福田分部',it is need
		// convert to ID ,like '100045300'
		queryParameter.put("dept_id", getDepartMentId(queryParameter));

		if (!warehouseClassDao.validClassNameExist(queryParameter)) {
			throw new BizException("班别代码已存在！");
		}

		return warehouseClassDao.addWarehouseClass(queryParameter);
	}

	private String getDepartMentId(HashMap<String, String> queryParameter) {
		return DepartmentCacheBiz.getDepartmentByCode(
				queryParameter.get("dept_id").split("/")[0]).getId()
				+ "";
	}

	public boolean updateWarehouseClass(HashMap<String, String> queryParameter) {
		// set employee code parameter that of to update the warehouseClass
		// employee code
		queryParameter.put("modified_emp_code", this.getCurrentUser()
				.getEmployee().getCode());

		return warehouseClassDao.updateWarehouseClass(queryParameter);
	}

	public String delete(HashMap<String, String> queryCriteria) {
		if (warehouseClassDao.queryExistReference(queryCriteria)) {
			return "删除班别存在引用，不能删除！";
		} else {
			if (!warehouseClassDao.delete(queryCriteria)) {
				return "执行失败，删除出错！";
			}
		}
		return "删除成功！";
	}

	@SuppressWarnings("unchecked")
	public List getDeptClass(Long deptId) {
		Map<String, Object> restCode = new HashMap<String, Object>();
		restCode.put("SCHEDULE_CODE", "休");
		List list =warehouseClassDao.queryClassDetailByDeptId(deptId);
		list.add(0,restCode);
		return list;
	}
}
