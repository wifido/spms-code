package com.sf.module.vmsarrange.log.dao;

import com.sf.framework.server.base.dao.BaseJdbcDao;

/**
 * <pre>
 * *********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * Description: LogDAO实现类
 * HISTORY
 * *********************************************
 *  ID   DATE           PERSON          REASON
 *  1    2014-9-1	          方芳           创建 
 * *********************************************
 * </pre>
 */
public class LogDao extends BaseJdbcDao implements ILogDao {

	/**
	 * 保存日志
	 */
	public void log(final String sql) throws Exception {
		logger.info(sql);
		this.getHibernateTemplateWrapper().execute(sql);
	}
}
