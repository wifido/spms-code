package com.sf.module.vmsarrange.log.dao;


/**
 * <pre>
 * *********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * Description: LogDAO接口类
 * HISTORY
 * *********************************************
 *  ID   DATE           PERSON          REASON
 *  1    2014-6-10	          方芳           创建 
 * *********************************************
 * </pre>
 */
public interface ILogDao {

	/**
	 * 保存日志
	 */
	public void log(final String sql) throws Exception;

}
