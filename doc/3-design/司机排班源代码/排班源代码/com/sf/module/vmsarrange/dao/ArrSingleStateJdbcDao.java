package com.sf.module.vmsarrange.dao;

import com.sf.framework.server.base.dao.BaseJdbcDao;

/**
 * <pre>
 * *********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * Description: 系统单用户操作状态 Dao接口实现
 * HISTORY
 * *********************************************
 *  ID   DATE           PERSON          REASON
 *  1    2014-06-03	           方芳                          	 创建 
 * *********************************************
 * </pre>
 */

public class ArrSingleStateJdbcDao extends BaseJdbcDao implements IArrSingleStateJdbcDao {
	private static String UPDATE_END_SQL = "update tm_arr_single_state set state = 0,end_tm = sysdate where type_id = :typeId and state = 1";
	private static String UPDATE_START_SQL = "update tm_arr_single_state set state = 1,start_tm = sysdate where type_id = :typeId and state = 0";
	/**
	 * 置状态为正在执行:需要回滚(事务控制)
	 * typeId:1.班次信息新增
	 * typeId:2.机动班新增/修改
	 * typeId:3.调班
	 */
	public int updateStart(Long typeId) {
		Object[] params = new Object[] {typeId};
		return this.getHibernateTemplateWrapper().update(
				UPDATE_START_SQL,params);
	}
	/**
	 * 置状态为空闲：不回滚
	 * typeId:1.班次信息新增
	 */
	public int updateEnd(Long typeId) {
		Object[] params = new Object[] {typeId};
		return this.getHibernateTemplateWrapper().update(
				UPDATE_END_SQL,params);
	}
}
