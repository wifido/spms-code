/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-16     方芳                         创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.util.Date;
import java.util.List;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.vmsarrange.domain.SchOptRouteRpt;

/**
 * 
 * 路径优化报表dao接口
 * @author 方芳 (350614) 2014-6-16
 */
public interface ISchOptRouteRptDao extends IEntityDao<SchOptRouteRpt> {
	/**
	 * 查询路径优化分析报表分页数据
	 * @param deptCode
	 * @param yearMonth
	 * @param pageSize
	 * @param pageIndex
	 * @param userId
	 * @return
	 */
	public IPage<SchOptRouteRpt> listPage(Long deptId,Date yearMonth,int pageSize,int pageIndex,Long userId);
	
	/**
	 * 查询路径优化分析报表数据
	 * @param deptCode
	 * @param yearMonth
	 * @param userId
	 * @return
	 */
	public List<SchOptRouteRpt> listReport(Long deptId,Date yearMonth,Long userId);
	
	/**
	 * 查询路径优化分析报表总数
	 * @param deptCode
	 * @param yearMonth
	 * @param userId
	 * @return
	 */
	public int listReportCount(Long deptId,Date yearMonth,Long userId);
}
