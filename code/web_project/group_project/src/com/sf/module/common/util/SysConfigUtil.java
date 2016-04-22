/***********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * 
 * HISTORY
 ********************************************************
 *  ID    DATE              PERSON             	REASON
 *  1     2014-4-24         杜志星 (380173)      创建 
 ********************************************************/
package com.sf.module.common.util;

import java.util.Map;

import com.sf.framework.server.core.integration.SpringBeanLoader;
import com.sf.module.common.biz.ISysConfigBiz;
import com.sf.module.common.domain.SysConfig;

/**
 * 系统参数配置公用类 
 * @author 杜志星 (380173) 2015-5-4 
 */
public class SysConfigUtil {
	
	/**
	 * 通过键值对获取系统配置表里的Value值
	 * @date 2015-5-4 
	 * @param name 
	 * @return
	 */
	public static String getKeyByName(String name){
		
		ISysConfigBiz sysConfigBiz = getSysConfigBiz();
		SysConfig sysConfig = sysConfigBiz.searchByKeyName(name);
		return sysConfig.getKeyValue();
	}
	
	/**
	 * 获取缓存系统属性配置
	 * @date 2015-5-4 
	 * @param key
	 * @return
	 */
	public static String getCacheValue(String key) {
		
		ISysConfigBiz sysConfigBiz = getSysConfigBiz();
		Map<String, String> map = sysConfigBiz.createMap();
		return map.get(key);
	}
	
	public static ISysConfigBiz getSysConfigBiz() {
		return (ISysConfigBiz) SpringBeanLoader.getBean("sysConfigBiz");
	}
	
	/**
	 * demo
	 * @author 杜志星 (380173)
	 * @date 2015-5-4 
	 * @param args
	 */
/*	public static void main(String[] args) {
		
		//取缓存数据
		String typeLevel = getCacheValue("TYPE_LEVEL");
		//实时取配置数据
		String typeLevel_ = getKeyByName("TYPE_LEVEL");
		System.out.println("缓存："+typeLevel+"；非缓存："+typeLevel_);
	}*/
}
