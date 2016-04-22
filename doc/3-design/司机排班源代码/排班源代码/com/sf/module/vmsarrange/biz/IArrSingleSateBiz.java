/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-6     方芳                         创建
 **********************************************/
package com.sf.module.vmsarrange.biz;

import com.sf.framework.server.base.biz.IBiz;

/**
 *
 * 单用户操作业务接口类
 *
 */
public interface IArrSingleSateBiz extends IBiz {
	/**
	 * 更新状态为开始执行
	 * @return
	 */
	public int updateStart(Long id);
}
