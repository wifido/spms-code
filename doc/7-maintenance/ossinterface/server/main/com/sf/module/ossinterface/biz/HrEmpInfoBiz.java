/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-05-28     jun.wen       创建
 **********************************************/

package com.sf.module.ossinterface.biz;

import java.util.List;

import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.ossinterface.dao.IHrEmpInfoDao;
import com.sf.module.ossinterface.domain.HrEmpInfo;

/**
 *
 * HR系统接口获取(人员表)的业务实现类
 * @author jun.wen  2014-05-28
 *
 */
public class HrEmpInfoBiz extends BaseBiz implements IHrEmpInfoBiz {

	/**
	 * HR系统接口获取(人员表)的Dao接口
	 */
	private IHrEmpInfoDao hrEmpInfoDao;

	/**
	 * 设置HR系统接口获取(人员表)的Dao接口
	 */
	public void setHrEmpInfoDao(IHrEmpInfoDao hrEmpInfoDao) {
		this.hrEmpInfoDao = hrEmpInfoDao;
	}

	/**
	 * <pre>
	 * 保存员工信息1w提交一次
	 * </pre>
	 * @author 文俊 (337291)
	 * @date May 28, 2014 
	 * @param entities
	 */
	public void save(List<HrEmpInfo> entities) {
	    hrEmpInfoDao.saveBatch(entities);
	}
	
	
	/**
	 * <pre>
	 * 调用初始化emp信息存储过程
	 * 事物在存储过程里面控制
	 * </pre>
	 * @author 文俊 (337291)
	 * @date Jun 24, 2014 
	 * @param journalId
	 */
	public void initTmEmployee(String journalId) {
		hrEmpInfoDao.stpInitTmEmployee(journalId);
	}
	
    

}