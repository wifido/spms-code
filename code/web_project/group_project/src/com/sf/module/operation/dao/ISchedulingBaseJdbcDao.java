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

import java.util.List;

import com.sf.framework.server.base.dao.IJdbcDao;
import com.sf.module.operation.domain.SchedulingBase;

/**
 * 
 * 班次基础信息管理的JdbcDao接口
 * 
 * @author 632898 李鹏 2014-06-18
 * 
 */
public interface ISchedulingBaseJdbcDao extends IJdbcDao {
	public List<SchedulingBase> getScheBaseList(Long deptid,String ym);
}