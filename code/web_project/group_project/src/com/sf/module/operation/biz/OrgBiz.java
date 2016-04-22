package com.sf.module.operation.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sf.framework.core.domain.IDepartment;
import com.sf.framework.core.domain.IUser;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.authorization.util.TreeabledUtil;
import com.sf.module.common.biz.ISysConfigBiz;
import com.sf.module.common.dao.IOssDepartmentDao;
import com.sf.module.common.domain.OssDepartment;
import com.sf.module.common.util.StringUtil;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.organization.domain.Department;

public class OrgBiz extends BaseBiz implements IOrgBiz {

	private IOssDepartmentDao ossDepartmentDao;

	private static String[] LETTERS_VALID = { "W", "X", "R" };

	public ISysConfigBiz sysConfigBiz;

	public void setSysConfigBiz(ISysConfigBiz sysConfigBiz) {
		this.sysConfigBiz = sysConfigBiz;
	}

	public IOssDepartmentDao getOssDepartmentDao() {
		return ossDepartmentDao;
	}

	public void setOssDepartmentDao(IOssDepartmentDao ossDepartmentDao) {
		this.ossDepartmentDao = ossDepartmentDao;
	}

	@Override
	public List<Department> getUserOffice(IUser user, IDepartment parentDept) {
		List depts = new ArrayList();
		if (user == null)
			return depts;
		TreeabledUtil tu;
		if (isAdministrator(user)) {
			TreeabledUtil tn;
			if (parentDept == null) {
				tn = DepartmentCacheBiz.getDepartmentTreeRoot();
				depts.add((Department) tn.getUserObject());
			} else {
				tn = null;
				if (parentDept.getId() != null)
					tn = DepartmentCacheBiz.getDepartmentTreeNode(parentDept.getId());
				else if (parentDept.getDeptCode() != null)
					tn = DepartmentCacheBiz.getDepartmentTreeNode(parentDept.getDeptCode());
				else {
					throw new IllegalArgumentException("Can not handler this department which both of id and deptcode are null.");
				}

				if (tn == null) {
					return null;
				}
				for (Iterator enumer = tn.children().iterator(); enumer.hasNext();) {
					tu = (TreeabledUtil) enumer.next();
					Department department = (Department) tu.getUserObject();
					depts.add(department);
				}
			}
		} else {
			List<Long> deptIdList = user.getAuthDepartmentIds();
			if (deptIdList == null) {
				return null;
			}

			if (parentDept == null) {
				for (Long id : deptIdList) {
					Department currentOne = DepartmentCacheBiz.getDepartmentByID(id);
					if (currentOne == null)
						continue;
					Department parent = currentOne.getParent();

					if ((parent != null) && (deptIdList.contains(parent.getId()))) {
						continue;
					}
					depts.add(currentOne);
				}
			} else {
				TreeabledUtil tn = null;
				if (parentDept.getId() != null)
					tn = DepartmentCacheBiz.getDepartmentTreeNode(parentDept.getId());
				else if (parentDept.getDeptCode() != null)
					tn = DepartmentCacheBiz.getDepartmentTreeNode(parentDept.getDeptCode());
				else {
					throw new IllegalArgumentException("Can not handler this department which both of id and deptcode are null.");
				}

				if (tn == null) {
					return null;
				}

				Department currentDepartment = (Department) tn.getUserObject();
				if ((currentDepartment == null) || (!(deptIdList.contains(currentDepartment.getId())))) {
					return null;
				}

				for (Iterator enumer = tn.children().iterator(); enumer.hasNext();) {
					TreeabledUtil tu1 = (TreeabledUtil) enumer.next();
					Department department = (Department) tu1.getUserObject();
					if ((department == null) || (!(deptIdList.contains(department.getId()))))
						continue;
					// 为经营本部或区部时直接添加
					depts.add(department);
				}
			}
		}

		if (depts.size() > 0) {
			Collections.sort(depts, new DepartmentComparator());
		}

		return depts;
	}

	public List<Department> getUserDeptsOrig(IUser user, IDepartment parentDept) {

		Map<String, String> map = getSysConfigCache();
		String[] TYPE_CODE = map.get(StringUtil.OPERATION_DEPTCODE_FILTER).split(",");
		String ZB_TYPE_CODE = map.get(StringUtil.ZB_DEPTCODE_FILTER);
		String JYBB_TYPE_CODE = map.get(StringUtil.JYBB_DEPTCODE_FILTER);
		String[] QB_TYPE_CODE = map.get(StringUtil.QB_DEPTCODE_FILTER).split(",");
		String[] BRANCH_TYPE_CODE =map.get(StringUtil.FB_DEPTCODE_FILTER).split(",");
		
		List depts = new ArrayList();
		if (user == null)
			return depts;
		TreeabledUtil tu;
		if (isAdministrator(user)) {
			TreeabledUtil tn;
			if (parentDept == null) {
				tn = DepartmentCacheBiz.getDepartmentTreeRoot();
				depts.add((Department) tn.getUserObject());
			} else {
				tn = null;
				if (parentDept.getId() != null)
					tn = DepartmentCacheBiz.getDepartmentTreeNode(parentDept.getId());
				else if (parentDept.getDeptCode() != null)
					tn = DepartmentCacheBiz.getDepartmentTreeNode(parentDept.getDeptCode());
				else {
					throw new IllegalArgumentException("Can not handler this department which both of id and deptcode are null.");
				}

				if (tn == null) {
					return null;
				}
				for (Iterator enumer = tn.children().iterator(); enumer.hasNext();) {
					tu = (TreeabledUtil) enumer.next();
					Department department = (Department) tu.getUserObject();
					if (department.getTypeCode().equals(ZB_TYPE_CODE))
						depts.add(department);

					if (department.getHqDeptCode() != null && department.getHqDeptCode().equals(department.getDeptCode())
							&& department.getTypeCode().equals(JYBB_TYPE_CODE))
						depts.add(department);

					if (department.getAreaDeptCode() != null && department.getAreaDeptCode().equals(department.getDeptCode())
							&& Arrays.asList(QB_TYPE_CODE).contains(department.getTypeCode())) {
						depts.add(department);
					}

					if (Arrays.asList(TYPE_CODE).contains(department.getTypeCode())) {
						depts.add(department);
					}

					if (Arrays.asList(BRANCH_TYPE_CODE).contains(department.getTypeCode())) {
						depts.add(department);
					}
				}
			}
		} else {
			List<Long> deptIdList = user.getAuthDepartmentIds();
			if (deptIdList == null) {
				return null;
			}

			if (parentDept == null) {
				for (Long id : deptIdList) {
					Department currentOne = DepartmentCacheBiz.getDepartmentByID(id);
					if (currentOne == null)
						continue;
					Department parent = currentOne.getParent();

					if ((parent != null) && (deptIdList.contains(parent.getId()))) {
						continue;
					}
					depts.add(currentOne);
				}
			} else {
				TreeabledUtil tn = null;
				if (parentDept.getId() != null)
					tn = DepartmentCacheBiz.getDepartmentTreeNode(parentDept.getId());
				else if (parentDept.getDeptCode() != null)
					tn = DepartmentCacheBiz.getDepartmentTreeNode(parentDept.getDeptCode());
				else {
					throw new IllegalArgumentException("Can not handler this department which both of id and deptcode are null.");
				}

				if (tn == null) {
					return null;
				}

				Department currentDepartment = (Department) tn.getUserObject();
				if ((currentDepartment == null) || (!(deptIdList.contains(currentDepartment.getId())))) {
					return null;
				}

				for (Iterator enumer = tn.children().iterator(); enumer.hasNext();) {
					TreeabledUtil tu1 = (TreeabledUtil) enumer.next();
					Department department = (Department) tu1.getUserObject();
					if ((department == null) || (!(deptIdList.contains(department.getId()))))
						continue;
					// 为经营本部或区部时直接添加
					if (department.getTypeCode().equals(ZB_TYPE_CODE))
						depts.add(department);

					if (department.getHqDeptCode() != null && department.getHqDeptCode().equals(department.getDeptCode())
							&& department.getTypeCode().equals(JYBB_TYPE_CODE))
						depts.add(department);

					if (department.getAreaDeptCode() != null && department.getAreaDeptCode().equals(department.getDeptCode())
							&& Arrays.asList(QB_TYPE_CODE).contains(department.getTypeCode())) {
						depts.add(department);
					}

					if (Arrays.asList(TYPE_CODE).contains(department.getTypeCode())) {
						depts.add(department);
					}

					if (Arrays.asList(BRANCH_TYPE_CODE).contains(department.getTypeCode())) {
						depts.add(department);
					}
				}
			}
		}

		if (depts.size() > 0) {
			Collections.sort(depts, new DepartmentComparator());

			for (Iterator<Department> departmentIterator = depts.iterator(); departmentIterator.hasNext();) {
				boolean isInTransition = false;
				Department dept = departmentIterator.next();
				if (Arrays.asList(BRANCH_TYPE_CODE).contains(dept.getTypeCode())) {
					List<OssDepartment> deptList = ossDepartmentDao.getParentDeptCode(dept.getDeptCode());
					for (Iterator<OssDepartment> ossDepartmentIterator = deptList.iterator(); ossDepartmentIterator.hasNext();) {
						OssDepartment ossDepartment = ossDepartmentIterator.next();
						if (isIndexOf(ossDepartment.getDeptCode())) {
							isInTransition = true;
						}
					}
					if (!isInTransition) {
						departmentIterator.remove();
					}
				}

			}
		}

		return depts;

	}

	private boolean isIndexOf(String deptCode) {
		boolean flag = false;
		for (String letterName : LETTERS_VALID) {
			if (deptCode.indexOf(letterName) == 3) {
				flag = true;
			}
		}
		return flag;
	}

	private boolean isAdministrator(IUser user) {
		return ("ADMIN".equals(user.getUsername().toUpperCase()));
	}

	static class DepartmentComparator implements Comparator<Department> {
		public int compare(Department source, Department target) {
			if (source == null)
				return 1;
			if (target == null) {
				return -1;
			}
			String selfDeptCode = source.getDeptCode();
			String targetDeptCode = target.getDeptCode();
			if (selfDeptCode == null)
				return 1;
			if (targetDeptCode == null) {
				return -1;
			}
			return selfDeptCode.compareToIgnoreCase(targetDeptCode);
		}
	}

	private Map<String, String> getSysConfigCache() {
		return sysConfigBiz.createMap();
	}

}
