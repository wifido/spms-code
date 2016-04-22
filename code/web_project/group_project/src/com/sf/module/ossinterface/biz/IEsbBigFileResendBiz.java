/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-21     wen.jun       创建
 **********************************************/

package com.sf.module.ossinterface.biz;

import com.sf.framework.server.base.biz.IBiz;
import com.sf.module.ossinterface.domain.EsbBigFileResend;

/**
 *
 * ESB数据重发请求(BigFileResendData)参数配置表的业务接口
 * @author wen.jun  2014-06-21
 *
 */
public interface IEsbBigFileResendBiz extends IBiz {

	/**
	 * <pre>
	 * ESB数据重发请求(BigFileResendData)参数配置表
	 * </pre>
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014
	 * @param entity
	 * @return
	 */
	Long save(EsbBigFileResend entity);

	/**
	 * <pre>
	 * 更新参数配置表状态
	 * </pre>
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014
	 * @param id
	 * @param state
	 */
	void update(Long id, Integer state);

	/**
	 * <pre>
	 * 更加createdTm获取间隔最早失败的任务
	 * </pre>
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014
	 */
	EsbBigFileResend getByCreatedTmOrder();
	/**
	 * 检查是否需要出具重发
	 * @author 173279
	 * @return
	 */
	int checkDataResend();

	/**
	 * 写异常状态
	 *
	 * @author 069823
	 * @date 2014-7-29 void
	 */
	void writeExecpeiontStatus();
	/**
	 * 记录数据重发完成状态
	 * @author 173279
	 */
	void writeSuccessStatus();
}