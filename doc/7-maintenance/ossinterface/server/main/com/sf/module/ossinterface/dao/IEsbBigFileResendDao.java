/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-21     wen.jun       创建
 **********************************************/

package com.sf.module.ossinterface.dao;

import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.ossinterface.domain.EsbBigFileResend;

/**
 *
 * ESB数据重发请求(BigFileResendData)参数配置表的Dao接口
 * @author wen.jun  2014-06-21
 *
 */
public interface IEsbBigFileResendDao extends IEntityDao<EsbBigFileResend> {
	
	/**
	 * <pre>
	 * 更加createdTm获取间隔最早失败的任务
	 * </pre>
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014 
	 * @return
	 */
	public EsbBigFileResend getByCreatedTmOrder() ;
}