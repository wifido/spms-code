/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-24     380173       创建
 **********************************************/

package com.sf.module.common.dao;

import java.util.List;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.common.domain.SysConfig;


/**
 *
 * 系统配置表(序列：SEQ_BASE)的Dao接口
 * @author 380173  2015-5-4
 *
 */
public interface ISysConfigDao extends IEntityDao<SysConfig> {

	/**
	 * 系统配置参数:查询
	 * @author 杜志星 (380173)
	 * @date 2015-5-4 
	 * @param keyName key名称
	 * @return
	 */
	List<SysConfig> searchByKeyName(String keyName);
	
	/**
	 * 系统配置表:分页查询
	 * @author 杜志星 (380173)
	 * @date 2015-5-4 
	 * @param model
	 * @param pageSize 每页条数
	 * @param pageIndex 页数
	 * @param userId
	 * @return
	 */
	IPage<SysConfig> pageView(SysConfig model, int pageSize, int pageIndex, Long userId);
}