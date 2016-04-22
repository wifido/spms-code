/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-24     380173       创建
 **********************************************/

package com.sf.module.common.biz;

import java.util.Map;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.biz.IBiz;
import com.sf.module.common.domain.SysConfig;

/**
 *
 * 系统配置表(序列：SEQ_BASE)的业务接口
 * @author 380173  2015-05-04
 *
 */
public interface ISysConfigBiz extends IBiz {

	/**
	 * 系统配置参数:查询
	 * @author 杜志星 (380173)
	 * @date 2015-05-04
	 * @param keyName key名称
	 * @return
	 */
	SysConfig searchByKeyName(String keyName);
	
	/**
	 * 系统配置参数:缓存
	 * @author 杜志星 (380173)
	 * @date 2015-05-04
	 * @return
	 */
	Map<String, String> createMap();
	
	/**
	 * 系统配置表:分页查询
	 * @author 杜志星 (380173)
	 * @date 2015-05-04
	 * @param model
	 * @param pageSize 每页条数
	 * @param pageIndex 页数
	 * @param userId
	 * @return
	 */
	IPage<SysConfig> pageView(SysConfig model, int pageSize, int pageIndex);
	
	/**
	 * 新增编辑
	 * @author 杜志星 (380173)
	 * @date 2015-05-04
	 * @param model
	 */
	void saveOrUpdate(SysConfig model);
	
	/**
	 * 删除
	 * @author 杜志星 (380173)
	 * @date 2014-8-27 
	 * @param ids
	 */
	void remove(long[] ids);
	
}