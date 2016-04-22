/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-20     zhaochangjin       创建
 **********************************************/

package com.sf.module.operation.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.operation.domain.GroupOutEmployee;
import com.sf.module.operation.domain.OperationEmployeeAttribute;
import com.sf.module.operation.domain.OutEmployee;

/**
 * 
 * 外包人员实体的Dao接口
 * 
 * @author zhaochangjin 2014-06-20
 * 
 */
public interface IOutEmployeeDao extends IEntityDao<OutEmployee> {
	public Map findEmpForPage(final String empcode, final Long deptId, final String empname, final Long groupid, final int pageSize, final int pageIndex);

	public HashMap queryOutEmployee(HashMap paramsMap);

	public HashMap getInsertEmpCode();

	public List getGroupByDeptId(HashMap paramsMap);

	public HashMap queryHrEmp(HashMap paramsMap);

	public boolean updateSynHrEmp(HashMap paramsMap);

	public List queryEmployeeToEmail();

	public List querySynEmployee();

	public void batchUpdateEmployeeGroup(String username, Date enableDate, Map<String, Object>[] employees, String groupId, String departmentId);

	public boolean queryEmployeeValidity(String empCode, String deptCode);

	public boolean queryGroupValidity(String groupCode, String departmentCode);

	public void updateEmployeeGroup(String groupCode, String empCode, String deptCode, Date enableTime, String userName);

	public boolean updateEmployeeSiteCode(OperationEmployeeAttribute operationEmployeeAttribute);

	public Long buildOperationEmployeeAttributeId();

	public boolean insertOperationEmployeeAttribute(OperationEmployeeAttribute operationEmployeeAttribute);

	public boolean deleteOperationEmployeeAttributeById(String[] employeeCode);

	public Map<String, Object> queryRecordOfModifyEmployeeGroup(String employeeCode);

	public List getGroupByGroupCode(String groupCode);

	boolean employeeAttributeExistByParameter(GroupOutEmployee groupOutEmployee);

	void updateEmployeeAttributeSiteCodeByParameter(GroupOutEmployee groupOutEmployee);

	public OutEmployee queryEmployee(String empCode, Long deptId);
	
	public HashMap exportOutEmployee(HashMap paramsMap);
}