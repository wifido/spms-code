/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2010-10-11      谢年兵        创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.util.List;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.vmsarrange.domain.ArrDepartment;

/**
 * 简单部门Dao接口
 * @author 600675 2014-05-27
 */
@SuppressWarnings("rawtypes")
public interface IArrDepartmentDao extends IEntityDao<ArrDepartment> {
			
	/**
	 * 获取所有的区部(月度消耗生成数据专用)
	 * @return
	 */
	List<ArrDepartment> getAllAreaDepts();
	
	/**
	 * 获取用户拥有权限的区部(月度消耗查询数据专用)
	 * @param userId
	 * @return
	 */
	List<ArrDepartment> getAreaByUserId(Long userId);
	
	/**
	 * 获取所有区部及区部以上的部门
	 * @return
	 */
	List<ArrDepartment> getAllAreaAndUpwardDept();
	
	/**
	 * 根据用户权限获取区部及区部以上的部门
	 * @param userId
	 * @return
	 */
	List<ArrDepartment> getAllAreaAndUpwardDeptByUserId(Long userId);
	
	/**
	 * 获取行政的岗位信息(所有岗位)
	 * @return
	 */
	List listPoliticalPostInfoForCombox();
	
	/**
	 * @author 350613
	 * 根据用户权限获取所有经营本部
	 * @return
	 */
	List<ArrDepartment> getAllODept(Long userId);
	/**
	 * 查找全网所有部门-无权限控制
	 * @return
	 */
	IPage<ArrDepartment> listAllDepts(String deptCode,int pageSize,int pageIndex);
	/**
	 * 查找有权限的部门
	 * @return
	 */
	IPage<ArrDepartment> listAllUserDepts(String deptCode,Long userId,int pageSize,int pageIndex);
	/**
	 * 校验用户是否有指定部门的权限
	 * @param userId
	 * @param deptId
	 * @return
	 */
	ArrDepartment listDeptByUserAndDept(Long deptId,Long userId);
	/**
	 * 查找有权限的所有部门
	 */
	public List<ArrDepartment> listAllDeptByUser(Long userId);
}
