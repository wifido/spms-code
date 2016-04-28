package com.sf.module.common.action;

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sf.framework.core.domain.IDepartment;
import com.sf.framework.core.domain.IUser;
import com.sf.framework.server.base.action.BaseGridAction;
import com.sf.module.authorization.action.DepartmentTree;
import com.sf.module.authorization.action.DepartmentTreeWithoutCheckbox;
import com.sf.module.common.domain.OperationDepartment;
import com.sf.module.common.util.StringUtil;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.frameworkimpl.biz.ISecurityBiz;
import com.sf.module.operation.biz.IOrgBiz;
import com.sf.module.organization.domain.Department;

/**
 * <pre>
 * *********************************************
 * Copyright sf-express.
 * All rights reserved.
 * Description: 获取用户网点的action
 * HISTORY
 * *********************************************
 *  ID   DATE           PERSON          REASON
 *  1    2010-3-19	    钟志君           创建
 * *********************************************
 * </pre>
 */
@SuppressWarnings("serial")
public class OrgAction extends BaseGridAction<IDepartment> {
	private static final String DEPARTMENT_LIST = "departmentList";
	private static final String TOTAL_SIZE = "totalSize";
	private String fieldDeptCode;
	private IOrgBiz groupOrgBiz;
	private String deptCode;
	private String typeLevel;
	private String isCheckBox; // 是否为多选网点 1 为多选网点 其他为单选节点
	private String dataLevel; // 数据级别 true、为中转场,其他为 点部数据
	private List<DepartmentTreeWithoutCheckbox> deptList;
	private List<OperationDepartment> operationDept;
	private List<DepartmentTree> departmentTreeWithCheckboxList;
	private List treeList;
	private int start;
	private int limit;
	private Map<String, Object> result;
	private ISecurityBiz securityBiz;
	private String deptId;
	
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public ISecurityBiz getSecurityBiz() {
		return securityBiz;
	}

	public void setSecurityBiz(ISecurityBiz securityBiz) {
		this.securityBiz = securityBiz;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String listAllDepartment() {
		List<Department> allDepartmentList = getDepartments();

		result = new HashMap<String, Object>();
		result.put(TOTAL_SIZE, allDepartmentList.size());
		if (allDepartmentList.isEmpty()) {
			result.put(DEPARTMENT_LIST, allDepartmentList);
			return SUCCESS;
		}

		List<Department> paginDepartmentList = newArrayList();
		int end = getPageEnd(allDepartmentList);
		for (int i = start; i < end; i++) {
			paginDepartmentList.add(allDepartmentList.get(i));
		}

		result.put(DEPARTMENT_LIST, paginDepartmentList);
		return SUCCESS;
	}

	private int getPageEnd(List<Department> allDepartmentList) {
		int end = start + limit;
		if (end > allDepartmentList.size()) {
			end = allDepartmentList.size();
		}
		return end;
	}

	private List<Department> getDepartments() {
		List<Department> allDepartmentList = DepartmentCacheBiz.getAllDepartments();
		if (StringUtil.isBlank(deptCode))
			return allDepartmentList;
		List<Department> filterDepartmentList = newArrayList();
		for (Department department : allDepartmentList) {
			if (!department.getDeleteFlg()) {
				if (department.getDeptCode().contains(deptCode)) {
					filterDepartmentList.add(department);
				}
			}
		}
		return filterDepartmentList;
	}

	public String deptTree() {
		operationDept = new ArrayList<OperationDepartment>();
		IUser user = super.getCurrentUser();
		IDepartment parentDept = getCurrentDepartment();
		List<Department> depts = groupOrgBiz.getUserDeptsOrig(user, parentDept);
		for (Department dept : depts) {
			if (!dept.getDeleteFlg()) {
				operationDept.add(toOperationDepartment(dept));
			}
		}
		return SUCCESS;
	}

	public String getSfDeptTree() {
		List<Department> depts = null;
		depts = groupOrgBiz.getUserOffice(getCurrentUser(), getCurrentDepartment());
		treeList = newArrayList();
		for (Department department : depts) {
			if (!department.getDeleteFlg()) {
				treeList.add(toDeptTreeWithoutCheckbox(department));
			}
		}
		return SUCCESS;
	}

	public String getAllTransferDepartment() {
		departmentTreeWithCheckboxList = newArrayList();
		List<Department> departmentList = groupOrgBiz.getUserOffice(getCurrentUser(), getCurrentDepartment());
		for (Department department : departmentList) {
			if (!department.getDeleteFlg()) {
				departmentTreeWithCheckboxList.add(convertDepartmentAsTree(department));
			}
		}
		return SUCCESS;
	}
	
	public String queryDeptCode() {
		// [path,deptCode,deptName,typeLevel]
		String[] dept = queryDeptCode(fieldDeptCode,
				securityBiz, super.getCurrentUser());
		if (dept != null /*
						 * &&
						 * securityBiz.isAuthorizedDepartment(super.getCurrentUser
						 * (), dept[1])
						 */) {
			path = dept[0];
			deptCode = dept[1];
			deptName = dept[2];
			typeLevel = dept[3];
			deptId = dept[4];
		}
		return SUCCESS;
	}
	
	private String[] queryDeptCode(String fieldDeptCode, ISecurityBiz securityBiz, IUser iuser) {
		if (fieldDeptCode == null || "".equals(fieldDeptCode.trim())) {
            return null;
        }
        fieldDeptCode = fieldDeptCode.trim().toUpperCase();
        List<Department> depts = DepartmentCacheBiz.getAllDepartments();
        String[] r = null;
        if (depts != null && depts.size() > 0) {
            StringBuilder path =  new StringBuilder("/0");
            r = new String[5];
            Iterator<Department> iterator = depts.iterator();
            Department d = null;
            for (; iterator.hasNext(); ) {
                d = iterator.next();
                if (d != null) {
                    if (d.getDeptCode() != null && !"".equals(d.getDeptCode().trim())) {
                        if (d.getDeptCode().trim().toUpperCase().equals(fieldDeptCode)) {
                            getPath(d, path, securityBiz, iuser);
                            break;
                        }
                    }
                    if (d.getDeptName() != null && !"".equals(d.getDeptName().trim())) {
                        if (d.getDeptName().trim().equals(fieldDeptCode)) {
                            getPath(d, path, securityBiz, iuser);
                            break;
                        }
                    }
                }
            }
            if (d != null) {
                r[0] = path.toString();
                //toI18nList4Dept
                r[1] = d.getDeptCode();
                r[2] = d.getDeptName();
                r[3] = d.getTypeLevel() + "";
                r[4] = d.getId() + "";
            }
        }
        return r;
	}
	
	private static void getPath(Department d,StringBuilder path, ISecurityBiz securityBiz, IUser iuser) {
        if (d != null && securityBiz.isAuthorizedDepartment(iuser, d)) {
            getPath(d.getParent(), path, securityBiz, iuser);
            path.append("/").append(d.getId());
        }
    }

	private Department getCurrentDepartment() {
		return DepartmentCacheBiz.getDepartmentByID(getId());
	}

	/**
	 * @param to
	 *            set fieldDeptCode the fieldDeptCode
	 * @author 文俊 (337291)
	 * @date May 24, 2013
	 */
	public void setFieldDeptCode(String fieldDeptCode) {
		this.fieldDeptCode = fieldDeptCode;
	}

	private String path;

	/**
	 * @return the path
	 * @author 文俊 (337291)
	 * @date May 24, 2013
	 */
	public String getPath() {
		return path;
	}

	private String deptName;

	/**
	 * @return the deptName
	 * @author 文俊 (337291)
	 * @date May 24, 2013
	 */
	public String getDeptName() {
		return deptName;
	}

	/**
	 * @return the deptCode
	 * @author 文俊 (337291)
	 * @date May 24, 2013
	 */
	public String getDeptCode() {
		return deptCode;
	}

	/**
	 * @return the typeLevel
	 * @author 文俊 (337291)
	 * @date May 28, 2013
	 */
	public String getTypeLevel() {
		return typeLevel;
	}

	public String getIsCheckBox() {
		return isCheckBox;
	}

	public void setIsCheckBox(String isCheckBox) {
		this.isCheckBox = isCheckBox;
	}

	public String getDataLevel() {
		return dataLevel;
	}

	public void setDataLevel(String dataLevel) {
		this.dataLevel = dataLevel;
	}

	private DepartmentTreeWithoutCheckbox toDeptTreeWithoutCheckbox(Department dept) {
		DepartmentTreeWithoutCheckbox deptTree = new DepartmentTreeWithoutCheckbox();
		deptTree.setCode(dept.getCode());
		deptTree.setDeptCode(dept.getDeptCode());
		deptTree.setDeptName(dept.getDeptCode() + "/" + dept.getDeptName());
		deptTree.setId(dept.getId());
		deptTree.setParentCode(dept.getParentCode());
		deptTree.setLeaf(!DepartmentCacheBiz.getDepartmentTreeNode(dept.getId()).hasChildren());
		return deptTree;
	}
	
	private OperationDepartment toOperationDepartment(Department dept) {
		OperationDepartment deptTree = new OperationDepartment();
		deptTree.setCode(dept.getCode());
		deptTree.setDeptCode(dept.getDeptCode());
		deptTree.setDeptName(dept.getDeptCode() + "/" + dept.getDeptName());
		deptTree.setId(dept.getId());
		deptTree.setParentCode(dept.getParentCode());
		deptTree.setTypeCode(dept.getTypeCode());
		deptTree.setLeaf(!DepartmentCacheBiz.getDepartmentTreeNode(dept.getId()).hasChildren());
		return deptTree;
	}

	private DepartmentTree convertDepartmentAsTree(Department department) {
		DepartmentTree deptTree = new DepartmentTree();
		deptTree.setCode(department.getCode());
		deptTree.setDeptCode(department.getDeptCode());
		deptTree.setDeptName(department.getDeptCode() + "/" + department.getDeptName());
		deptTree.setId(department.getId());
		deptTree.setParentCode(department.getParentCode());
		deptTree.setLeaf(!DepartmentCacheBiz.getDepartmentTreeNode(department.getId()).hasChildren());
		deptTree.setChecked(false);
		return deptTree;
	}

	public List<DepartmentTreeWithoutCheckbox> getDeptList() {
		return deptList;
	}

	public void setGroupOrgBiz(IOrgBiz groupOrgBiz) {
		this.groupOrgBiz = groupOrgBiz;
	}

	public boolean isSuccess() {
		return true;
	}

	public List<DepartmentTree> getDepartmentTreeWithCheckboxList() {
		return departmentTreeWithCheckboxList;
	}

	public List getTreeList() {
		return treeList;
	}

	public List<OperationDepartment> getOperationDept() {
		return operationDept;
	}
}
