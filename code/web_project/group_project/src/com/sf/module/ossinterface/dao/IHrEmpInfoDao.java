/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-05-28     jun.wen       创建
 **********************************************/

package com.sf.module.ossinterface.dao;

import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.ossinterface.domain.HrEmpInfo;

/**
 *
 * HR系统接口获取(人员表)的Dao接口
 * @author jun.wen  2014-05-28
 *
 */
public interface IHrEmpInfoDao extends IEntityDao<HrEmpInfo> {
	/**
	 * <pre>
	 * 调用初始化emp信息存储过程
	 * </pre>
	 * @author 文俊 (337291)
	 * @date Jun 24, 2014 
	 * @param journalId
	 */
	public void stpInitTmEmployee(String journalId);
}