package com.sf.module.common.biz;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.dao.MailEarlyWarningMonitoringDao;

@Component
public class MailEarlyWarningMonitoringBiz extends BaseBiz {
	@Resource
	private MailEarlyWarningMonitoringDao mailEarlyWarningMonitoringDao;
	@Transactional
	public int queryExistingData() {
		return mailEarlyWarningMonitoringDao
				.queryExistingData("SELECT COUNT(*) COUNT FROM TM_SPMS2CDP_BY_OPERATION_INFO WHERE ROWNUM = 1");
	}
	@Transactional
	public int queryExceptionLog() {
		return mailEarlyWarningMonitoringDao
				.queryExistingData("SELECT COUNT(*) COUNT  FROM TL_EXCEPTION_LOG T WHERE T.EXCEPTION_TM > SYSDATE - 1   AND T.EXCEPTION_REMK like '%ERROR%'");
	}
}
