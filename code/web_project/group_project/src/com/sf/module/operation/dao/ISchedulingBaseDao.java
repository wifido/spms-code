/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-18     632898 李鹏       创建
 **********************************************/

package com.sf.module.operation.dao;

import java.util.HashMap;

import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.operation.domain.SchedulingBase;

/**
 * 
 * 班次基础信息管理的Dao接口
 * 
 * @author 632898 李鹏 2014-06-18
 * 
 */
public interface ISchedulingBaseDao extends IEntityDao<SchedulingBase> {

	public HashMap querySchedule(HashMap map);
	public SchedulingBase getScheeduleBaseByCode(final String code,final Long deptId);
}