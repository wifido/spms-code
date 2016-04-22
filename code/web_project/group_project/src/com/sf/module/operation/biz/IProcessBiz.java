/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-30     632898 李鹏       创建
 **********************************************/

package com.sf.module.operation.biz;

import java.util.HashMap;

import com.sf.framework.server.base.biz.IBiz;

/**
 *
 * 工序管理的业务接口
 * @author 632898 李鹏  2014-06-30
 *
 */
public interface IProcessBiz extends IBiz {
	
	public HashMap queryProcess(HashMap paramsMap);
	
	public boolean  saveProcess(HashMap paramsMap);
	
	public boolean  updateProcess(HashMap paramsMap);
	
	public HashMap exportProcess(HashMap paramsMap);
	
	public HashMap importProcess(HashMap paramsMap);
	
	public HashMap queryUserProcess(HashMap paramsMap);
	
	public HashMap confirmProcess(HashMap paramsMap);
	
	public HashMap isOnlyProcess(HashMap paramsMap);
	
	public HashMap pushMsg();
	/**
	 * 查询总部同步的工序信息 
	 * @author 664525 莫航
	 * @date 2014-07-23
	 * @return dataMap
	 */
	public HashMap tbQueryProcess(HashMap paramsMap);

	public HashMap findByDeptId(Long deptid);
}