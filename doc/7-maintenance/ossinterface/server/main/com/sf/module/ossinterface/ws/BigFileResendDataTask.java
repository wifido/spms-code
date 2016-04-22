/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 ********************************************************
 *  ID    DATE               PERSON             REASON
 *  1     2012-7-3           文俊 (337291)       创建
 ********************************************************/
package com.sf.module.ossinterface.ws;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sf.module.ossinterface.biz.IEsbBigFileResendBiz;
import com.sf.module.ossinterface.domain.EsbBigFileResend;

/**
 * springframework.scheduling.quartz定时请求ESB数据重发 每20分钟执行
 *
 * @author 文俊 (337291) 2012-7-3
 */
public class BigFileResendDataTask {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private IEsbBigFileResendBiz esbBigFileResendBiz;

	public void setEsbBigFileResendBiz(IEsbBigFileResendBiz esbBigFileResendBiz) {
		this.esbBigFileResendBiz = esbBigFileResendBiz;
	}

	private EsbBigFileResend order = null;

	/**
	 * 清除excel下载目录
	 */
	public void invokeTarget() {
		if (esbBigFileResendBiz.checkDataResend() > 0) {
			logger.info("Executing operation reqResend");
			order = esbBigFileResendBiz.getByCreatedTmOrder();
			if (order != null) {
				try {
					String resendData = BigFileResendDataPortTypeImpl
							.bigFileResendData(order.getSystemId(),
									order.getDataType(), order.getTimeStamp(),
									order.getSelfSystemId(),
									order.getJournalId());
					//System.out.println(resendData);
					esbBigFileResendBiz.update(order.getId(),
							EsbBigFileResend.RESEND);
					esbBigFileResendBiz.writeSuccessStatus();
					logger.info("Success operation reqResend: ", resendData);
				} catch (Exception e) {
					logger.error("reqResend Failure: ", e);
					e.printStackTrace();
					esbBigFileResendBiz.writeExecpeiontStatus();
				}
			}
		}
	}
}
