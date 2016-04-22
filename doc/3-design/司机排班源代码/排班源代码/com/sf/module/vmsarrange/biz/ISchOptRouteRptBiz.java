/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-16     方芳                         创建
 **********************************************/
package com.sf.module.vmsarrange.biz;

import java.util.Date;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.biz.IBiz;
import com.sf.module.vmsarrange.domain.SchOptRouteRpt;

/**
 *
 * 路径优化分析报表接口类
 *
 */
public interface ISchOptRouteRptBiz extends IBiz {
	/**
	 * 查询分页数据
	 * @param deptCode
	 * @param yearMonth
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	public IPage<SchOptRouteRpt> listPage(Long deptId,Date yearMonth,int pageSize,int pageIndex);
	/**
	 * 生成报表
	 * @param deptId
	 * @param yearMonth
	 * @return
	 */
	public String listReport(Long deptId, Date yearMonth);
}
