/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-6     方芳                        创建
 **********************************************/
package com.sf.module.vmsarrange.biz;

import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.vmsarrange.dao.IArrSingleStateJdbcDao;

/**
 * 
 * 单用户业务实现类
 * 
 */

public class ArrSingleSateBiz extends BaseBiz implements IArrSingleSateBiz {
	private IArrSingleStateJdbcDao arrSingleStateJdbcDao;
	
	public void setArrSingleStateJdbcDao(
			IArrSingleStateJdbcDao arrSingleStateJdbcDao) {
		this.arrSingleStateJdbcDao = arrSingleStateJdbcDao;
	}
	// 更新状态为开始执行
	public int updateStart(Long id) {
		return arrSingleStateJdbcDao.updateStart(id);
	}
}
