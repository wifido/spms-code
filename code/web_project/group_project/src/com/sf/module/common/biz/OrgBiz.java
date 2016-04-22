package com.sf.module.common.biz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.sf.framework.core.domain.IDepartment;
import com.sf.framework.core.domain.IUser;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.authorization.util.TreeabledUtil;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.organization.domain.Department;

public class OrgBiz extends BaseBiz implements IOrgBiz {
	public List<Department> getUserDeptsOrig(IUser user, IDepartment parentDept) {
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
					tn = DepartmentCacheBiz.getDepartmentTreeNode(parentDept
							.getId());
				else if (parentDept.getDeptCode() != null)
					tn = DepartmentCacheBiz.getDepartmentTreeNode(parentDept
							.getDeptCode());
				else {
					throw new IllegalArgumentException(
							"Can not handler this department which both of id and deptcode are null.");
				}

				if (tn == null) {
					return null;
				}
				for (Iterator enumer = tn.children().iterator(); enumer
						.hasNext();) {
					tu = (TreeabledUtil) enumer.next();
					Department department = (Department) tu.getUserObject();
					if (department.getDeptCode().equals("001"))
						depts.add(department);

					if (department.getHqDeptCode() != null
							&& department.getHqDeptCode().equals(
									department.getDeptCode())
							&& department.getDeptName().contains("经营本部"))
						depts.add(department);

					if (department.getAreaDeptCode() != null
							&& department.getAreaDeptCode().equals(
									department.getDeptCode())
							&& department.getDeptName().contains("区部")) {
						depts.add(department);
					}

					if (department.getDeptName().contains("中转场")
							|| department.getDeptName().contains("航空组"))
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
					Department currentOne = DepartmentCacheBiz
							.getDepartmentByID(id);
					if (currentOne == null)
						continue;
					Department parent = currentOne.getParent();

					if ((parent != null)
							&& (deptIdList.contains(parent.getId()))) {
						continue;
					}
					depts.add(currentOne);
				}
			} else {
				TreeabledUtil tn = null;
				if (parentDept.getId() != null)
					tn = DepartmentCacheBiz.getDepartmentTreeNode(parentDept
							.getId());
				else if (parentDept.getDeptCode() != null)
					tn = DepartmentCacheBiz.getDepartmentTreeNode(parentDept
							.getDeptCode());
				else {
					throw new IllegalArgumentException(
							"Can not handler this department which both of id and deptcode are null.");
				}

				if (tn == null) {
					return null;
				}

				Department currentDepartment = (Department) tn.getUserObject();
				if ((currentDepartment == null)
						|| (!(deptIdList.contains(currentDepartment.getId())))) {
					return null;
				}

				for (Iterator enumer = tn.children().iterator(); enumer
						.hasNext();) {
					TreeabledUtil tu1 = (TreeabledUtil) enumer.next();
					Department department = (Department) tu1.getUserObject();
					if ((department == null)
							|| (!(deptIdList.contains(department.getId()))))
						continue;
					// 为经营本部或区部时直接添加
					if (department.getDeptCode().equals("001"))
						depts.add(department);

					if (department.getHqDeptCode() != null
							&& department.getHqDeptCode().equals(
									department.getDeptCode())
							&& department.getDeptName().contains("经营本部"))
						depts.add(department);

					if (department.getAreaDeptCode() != null
							&& department.getAreaDeptCode().equals(
									department.getDeptCode())
							&& department.getDeptName().contains("区部")) {
						depts.add(department);
					}

					if (department.getDeptName().contains("中转场")
							|| department.getDeptName().contains("航空组"))
						depts.add(department);
				}
			}
		}

		if (depts.size() > 0) {
			Collections.sort(depts, new DepartmentComparator());
		}

		return depts;

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
}
