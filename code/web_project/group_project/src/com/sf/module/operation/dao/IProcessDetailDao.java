/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-08     houjingyu       创建
 **********************************************/

package com.sf.module.operation.dao;

import java.util.Date;

import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.operation.domain.ProcessDetail;

/**
 *
 * 工序每日明细的Dao接口
 * @author houjingyu  2014-07-08
 *
 */
public interface IProcessDetailDao extends IEntityDao<ProcessDetail> {
	public ProcessDetail findByCondition(final Long deptid,final Date dt,final String empcode);
}