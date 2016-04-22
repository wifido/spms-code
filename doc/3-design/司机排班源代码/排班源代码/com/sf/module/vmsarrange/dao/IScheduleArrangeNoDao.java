/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-6     方芳                         创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.util.List;

import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.vmsarrange.domain.ScheduleArrangeNo;

/**
 * 
 *班次代码dao接口
 * @author 方芳 (350614) 2014-6-6
 */
public interface IScheduleArrangeNoDao extends IEntityDao<ScheduleArrangeNo> {
	/**
	 * 获取最大的序号
	 * @param deptCode
	 * @param type 1.机动 2.普通
	 * @return
	 */
	public Long listMaxNo(String deptCode,int type);
	/**
	 * 获取指定区部的记录
	 * @param deptCode
	 * @param type
	 * @return
	 */
	public List<ScheduleArrangeNo> listByCodeAndType(String deptCode, int type);
}
