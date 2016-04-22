/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2013-4-15        谢年兵                   创建
 **********************************************/
package com.sf.module.common.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sf.framework.server.core.cache.BaseDataCacheDataProvider;
import com.sf.module.common.dao.ISysConfigDao;
import com.sf.module.common.domain.SysConfig;

/**
 * 系统属性配置 缓存
 * @author 杜志星 (380173) 2015-5-4
 */
public class SpmsSysConfigCacheProvider extends BaseDataCacheDataProvider {
	
	private static Logger log = LoggerFactory.getLogger(SpmsSysConfigCacheProvider.class);
	
	private ISysConfigDao sysConfigDao;
	
	public Object getData(Object key) {
		log.info("start load SpmsSysConfigCacheProvider...");
		//缓存系统属性配置
		List<SysConfig> cs = sysConfigDao.findAll();
		
		Map<String,String> cacheObject = new HashMap<String,String>();
		for(SysConfig v:cs){
			cacheObject.put(v.getKeyName(), v.getKeyValue());
		}
		log.info("caching cofig count : " + cacheObject.size());
		log.info("load system config cache success!");
		return cacheObject;
	}

	public void setSysConfigDao(ISysConfigDao sysConfigDao) {
		this.sysConfigDao = sysConfigDao;
	}

}
