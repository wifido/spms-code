package com.sf.module.common.biz;

import java.util.List;

import com.sf.framework.core.domain.IDepartment;
import com.sf.framework.core.domain.IUser;
import com.sf.framework.server.base.biz.IBiz;
import com.sf.module.organization.domain.Department;

public interface IOrgBiz extends IBiz {
	public List<Department> getUserDeptsOrig(IUser user, IDepartment parentDept);
}
