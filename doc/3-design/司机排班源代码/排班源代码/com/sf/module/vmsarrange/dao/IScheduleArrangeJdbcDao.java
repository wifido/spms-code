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

import com.sf.framework.server.base.dao.IJdbcDao;

/**
 * 
 * 配班管理dao接口
 * @author 方芳 (350614) 2014-6-6
 */
public interface IScheduleArrangeJdbcDao extends IJdbcDao {
	public void updateInvalid(final Long[] recordIds,final String empCode);
}
