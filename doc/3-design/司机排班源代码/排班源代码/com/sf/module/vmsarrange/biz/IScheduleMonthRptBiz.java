/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-26     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.biz;

import java.util.Map;

import com.sf.framework.server.base.biz.IBiz;

/**
 *
 * IScheduleMonthRptBiz处理类
 *
 */
public interface IScheduleMonthRptBiz extends IBiz {

	/**
	 * 根据ID，年月日查看明细
	 * @param id
	 * @param yearMonth
	 * @param dayNum
	 * @return
	 */
	Map findByCondition(Long id, String yearMonth, Integer dayNum);

}
