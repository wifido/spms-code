package com.sf.module.common.job;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sf.module.common.biz.ISendMailBiz;
import com.sf.module.common.biz.ISysConfigBiz;
import com.sf.module.common.biz.MailEarlyWarningMonitoringBiz;
import com.sf.module.common.domain.SysConfig;
import com.sf.module.driver.job.SendDriverMailJob;
import com.sf.module.esbinterface.util.VMUtils;

@Component
public class MailEarlyWarningMonitoring {
	private static Log logger = LogFactory.getLog(MailEarlyWarningMonitoring.class);
	@Resource
	private MailEarlyWarningMonitoringBiz mailEarlyWarningMonitoringBiz;
	@Autowired
	private ISendMailBiz sendMailBiz;
	@Autowired
	private ISysConfigBiz sysConfigBiz;
	private final String KEY_NAME = "MONITOR_EMAIL";
	
	private String KEY_HOST_NAME = "SEND_DRIVER_MAIL_HOT";

	@Scheduled(cron = "0 20 3 * * ?")
	public void monitorSPMSTwoCDPByOperation() {
		logger.info(" MailEarlyWarningMonitoring.monitorSPMSTwoCDPByOperation() start ");
		SysConfig hotNameConfig = sysConfigBiz.searchByKeyName(KEY_HOST_NAME);
		if (!VMUtils.getCurrentMachineName().equals(hotNameConfig.getKeyValue()))
			return;
		int existingData =  mailEarlyWarningMonitoringBiz.queryExistingData();
		logger.info(" existingData ="  + existingData);
		if (mailEarlyWarningMonitoringBiz.queryExistingData() == 0) {
			SysConfig sysConfig = sysConfigBiz.searchByKeyName(KEY_NAME);
			String[] emails = sysConfig.getKeyValue().split(",");
			for (String email : emails) {
				sendMailBiz.sendMail(email, createContent("SPMS排班系统与CDP系统接口预警，处理班别数据失败！请及时处理"),
						null, null, "SPMS排班系统与CDP系统接口预警");
			}
		}
		
		logger.info(" MailEarlyWarningMonitoring.monitorSPMSTwoCDPByOperation() end ");
	}
	@Scheduled(cron = "0 0 2 * * ?")
	//@Scheduled(cron = "0 0/10 * * * ?")
	public void monitorExceptionLog() {
		logger.info(" MailEarlyWarningMonitoring.monitorExceptionLog() start ");
		SysConfig hotNameConfig = sysConfigBiz.searchByKeyName(KEY_HOST_NAME);
		if (!VMUtils.getCurrentMachineName().equals(hotNameConfig.getKeyValue()))
			return;
		int existingData =  mailEarlyWarningMonitoringBiz.queryExceptionLog();
		logger.info(" existingData ="  + existingData);
		if (mailEarlyWarningMonitoringBiz.queryExceptionLog() != 0) {
			SysConfig sysConfig = sysConfigBiz.searchByKeyName(KEY_NAME);
			String[] emails = sysConfig.getKeyValue().split(",");
			for (String email : emails) {
				sendMailBiz.sendMail(email, createContent("SPMS排班系统存储过程执行出现异常！请及时处理"), null, null,
						"SPMS排班系统存储过程定时任务监控");
			}
		}
		logger.info(" MailEarlyWarningMonitoring.monitorExceptionLog() end ");
	}

	private String createContent(String content) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><head></head><body>");
		sb.append("<label style='width:2em;'></label>" + content + "</p>");
		sb.append("<div style='color:red;font-size:12px;'>[系统发送邮件,请勿回复]</div></body></html>");
		return sb.toString();
	}

}
