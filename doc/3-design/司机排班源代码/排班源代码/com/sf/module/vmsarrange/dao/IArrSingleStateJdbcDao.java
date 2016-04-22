package com.sf.module.vmsarrange.dao;

import com.sf.framework.server.base.dao.IJdbcDao;

/**
 * <pre>
 * *********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * Description: 系统单用户操作状态jdbc Dao接口
 * HISTORY
 * *********************************************
 *  ID   DATE           PERSON          REASON
 *  1    2014-06-03	          方芳                                 创建 
 * *********************************************
 * </pre>
 */
public interface IArrSingleStateJdbcDao extends IJdbcDao {
	/**
	 * 置状态为正在执行:需要回滚(事务控制)
	 * @param typeId:1.班次信息新增
	 * @return
	 */
	public int updateStart(Long typeId);
	
	/**
	 * 置状态为空闲：不回滚
	 * @param typeId:1.班次信息新增
	 * @return
	 */
	public int updateEnd(Long typeId);
}
