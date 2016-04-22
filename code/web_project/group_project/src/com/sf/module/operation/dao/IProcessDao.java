/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-30     632898 李鹏       创建
 **********************************************/

package com.sf.module.operation.dao;

import java.util.HashMap;
import java.util.List;

import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.operation.domain.Process;

/**
 *
 * 工序管理的Dao接口
 * @author 632898 李鹏  2014-06-30
 *
 */
public interface IProcessDao extends IEntityDao<Process> {
	   
	   public HashMap queryProcess(HashMap paramsMap);
		/**
		 * 查询总部同步的工序信息 
		 * @author 664525 莫航
		 * @date 2014-07-23
		 * @return dataMap
		 */
		public HashMap tbQueryProcess(HashMap paramsMap);
		public Process findBy(Long deptid,String processCode);
		public List<Process> findByDeptId(Long deptid);
		public List<Process> pushMsg(long 	deptId);
		public boolean isUsedProcess(Long deptId,String processCode);
		public List<Process>  loadProcessByCode(Long deptId,String processCode);
}