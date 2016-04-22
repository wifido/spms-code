/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-20     zhaochangjin       创建
 **********************************************/

package com.sf.module.operation.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sf.framework.server.base.biz.IBiz;

/**
 * 
 * 外包人员实体的业务接口
 * 
 * @author zhaochangjin 2014-06-20
 * 
 */
public interface IOutEmployeeBiz extends IBiz {

	public Map findDriversForPage(String empcode, Long deptId, String empname,
			Long groupid, int pageSize, int pageIndex);

	@SuppressWarnings("rawtypes")
	public HashMap queryOutEmployee(HashMap paramsMap);

	@SuppressWarnings("rawtypes")
	public HashMap getInsertEmpCode();
	
	@SuppressWarnings("rawtypes")
	public List  getGoupByDeptId(HashMap paramsMap);
	
	@SuppressWarnings("rawtypes")
	public boolean saveEmployee(HashMap paramsMap);
	
	@SuppressWarnings("rawtypes")
	public boolean updateEmployee(HashMap paramsMap);
	
	@SuppressWarnings("rawtypes")
	public boolean deleteEmployee(HashMap paramsMap);
	
	@SuppressWarnings("rawtypes")
	public HashMap exportEmployee(HashMap paramsMap);

	@SuppressWarnings("rawtypes")
	public HashMap importEmployee(HashMap paramsMap);
	
	@SuppressWarnings("rawtypes")
	public HashMap queryHrEmp(HashMap paramsMap);
	
	@SuppressWarnings("rawtypes")
	public boolean updateSynHrEmp(HashMap paramsMap);

	public boolean synEmployeeEmail();

	public Map  isSchedulMgtbyEmpCode(String[] empCodes);
	
	@SuppressWarnings("rawtypes")
	public void batchUpdateEmployeeGroup(HashMap paramsMap);
	
	@SuppressWarnings("rawtypes")
	public HashMap groupImportEmployee(HashMap paramsMap);

    HashMap importEmployeeAttribute(HashMap<String, String> queryMap);

	List queryGroupWithEmptyGroup(HashMap map);
}