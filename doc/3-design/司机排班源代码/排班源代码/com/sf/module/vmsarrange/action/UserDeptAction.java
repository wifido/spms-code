/**
 * <pre>
 * *********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * Description: 获取用户网点的action
 * HISTORY
 * *********************************************
 *  ID   DATE           PERSON          REASON
 *  1    2014-05-27	    600675                          创建 
 * *********************************************
 * </pre>
 */

package com.sf.module.vmsarrange.action;

import java.util.ArrayList;
import java.util.List;

import com.sf.framework.core.domain.IDepartment;
import com.sf.framework.core.domain.IUser;
import com.sf.framework.server.base.action.BaseGridAction;
import com.sf.module.cmscommon.biz.DepartmentCacheBiz;
import com.sf.module.cmscommon.biz.ISecurityBiz;
import com.sf.module.department.domain.Department;
import com.sf.module.vmsarrange.dao.ArrDepartmentDao;
import com.sf.module.vmsarrange.domain.DepartmentTree;

@SuppressWarnings("serial")
public class UserDeptAction extends BaseGridAction<IDepartment> {

	private ISecurityBiz securityBiz;
	// 用户网点列表
	private List<DepartmentTree> deptList;

	public String deptTree() {
		deptList = new ArrayList<DepartmentTree>();
		IUser user = super.getCurrentUser();
		IDepartment parentDept = DepartmentCacheBiz.getDepartmentByID(getId());
		List<Department> depts = securityBiz.getUserAuthorizedSubDepts(user, parentDept);
		for (Department dept : depts) {
			deptList.add(toDeptTreeWithoutCheckbox(dept));
		}

		return SUCCESS;
	}

	private DepartmentTree toDeptTreeWithoutCheckbox(Department dept) {
		DepartmentTree deptTree = new DepartmentTree();
		deptTree.setCode(dept.getCode());
		deptTree.setDeptCode(dept.getDeptCode());
		deptTree.setDeptName(dept.getDeptCode() + "/" + dept.getDeptName());
		deptTree.setId(dept.getId());
		deptTree.setParentCode(dept.getParentCode());
		deptTree.setLeaf(!DepartmentCacheBiz.getDepartmentTreeNode(dept.getId()).hasChildren());
		//对”北京电商“进行特殊处理,因为其类型为虚拟网点,但却与区部为同一级别
		if (ArrDepartmentDao.isSpecialAreaCode(dept.getDeptCode())) {
			deptTree.setTypeLevel(Long.valueOf(2));
		} else {
			deptTree.setTypeLevel(dept.getTypeLevel());
		}
		return deptTree;
	}

	public List<DepartmentTree> getDeptList() {
		return deptList;
	}

	public void setSecurityBiz(ISecurityBiz securityBiz) {
		this.securityBiz = securityBiz;
	}

	public boolean isSuccess() {
		return true;
	}
}
