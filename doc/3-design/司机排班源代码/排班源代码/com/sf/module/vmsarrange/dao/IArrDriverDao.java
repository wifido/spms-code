/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-19     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.vmsarrange.domain.ArrDriver;

/**
 *
 * IArrDriverDao处理类
 *
 */
public interface IArrDriverDao extends IEntityDao<ArrDriver> {

	/**
	 * 根据驾驶员工号查出驾驶员信息
	 * @param empCode
	 * @return
	 */
	ArrDriver findDriverByCode(String empCode);
	
	/**
	 * 根据驾驶员工号查出驾驶员信息
	 * @param empCode
	 * @return
	 */
	ArrDriver findDriverByCodeAndId(String empCode,Long deptId,Long userId);
	/**
	 * 查找驾驶员分页数据
	 * @param deptId
	 * @param empCode
	 * @param yearMonth
	 * @param userId
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	public IPage<ArrDriver> listPageBy(Long deptId, String empCode,String yearMonth, Long userId,int pageSize,int pageIndex);
}
