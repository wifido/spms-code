package com.sf.module.common.dao;

import java.util.List;

import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.common.domain.OssDepartment;

public interface IOssDepartmentDao extends IEntityDao<OssDepartment>{
	
	/**
	 * 获取用户所拥有的权限网点
	 * @param userId
	 * @author 173279
	 * @return
	 */
	List<OssDepartment> getDeptByUserId(Long userId,Long deptId);
	
	/**
	 * 获取用户所拥有的权限网点
	 * @param userId
	 * @author 173279
	 * @return
	 */
	List<OssDepartment> getParentDeptCode(String deptCode);
	
	OssDepartment getDepartmentByDeptCode(String deptCode);
	
	public OssDepartment getById(Long deptId);
}
