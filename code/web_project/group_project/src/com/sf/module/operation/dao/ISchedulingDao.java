/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-17     houjingyu       创建
 **********************************************/

package com.sf.module.operation.dao;

import java.util.Date;

import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.operation.domain.Scheduling;

/**
 *
 * 排班的Dao接口
 * @author houjingyu  2014-06-17
 *
 */
public interface ISchedulingDao extends IEntityDao<Scheduling> {
	public Scheduling findByCondition(final Date dt,final String empcode);
	public boolean findById( String scheduleCode, Long deptid, String ym);
	public boolean checkChangSchedule(Long id, String newScheduleCodd);
}