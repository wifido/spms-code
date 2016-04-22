/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-25     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.util.List;
import java.util.Map;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.vmsarrange.domain.ScheduleMonthRpt;

/**
 *
 * IScheduleMonthRptDao处理类
 *
 */
public interface IScheduleMonthRptDao extends IEntityDao<ScheduleMonthRpt>{

	/**
	 * 分页才查询历史实际排班
	 * @param deptCode
	 * @param empCode
	 * @param classType
	 * @param yearMonth
	 * @param start
	 * @param limit
	 * @param userId
	 * @return
	 */
	IPage findByPage(String deptCode, String empCode, Integer classType,
			String yearMonth, int pageSize, int pageIndex, Long userId);

	/**
	 * 获取实际排班记录总数
	 * @param deptCode
	 * @param empCode
	 * @param yearMonth
	 * @param userId
	 * @return
	 */
	Long listRealMonthReportCount(String deptCode, String empCode,
			String yearMonth, Long userId,Integer classType);

	/**
	 * 获取实际排班记录
	 * @param deptCode
	 * @param empCode
	 * @param yearMonth
	 * @param userId
	 * @return
	 */
	List<Map> listRealMonthReport(String deptCode, String empCode,
			String yearMonth, Long userId,Integer classType);

}
