package com.sf.module.driver.job;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sf.module.authorization.domain.User;
import com.sf.module.common.biz.ISendMailBiz;
import com.sf.module.common.biz.ISysConfigBiz;
import com.sf.module.common.domain.SysConfig;
import com.sf.module.common.util.StringUtil;
import com.sf.module.driver.biz.SendDriverMailBiz;
import com.sf.module.esbinterface.util.VMUtils;
import com.sf.module.organization.domain.Employee;

@Component
public class SendDriverMailJob {
	private static Log logger = LogFactory.getLog(SendDriverMailJob.class);
	@Autowired
	private ISendMailBiz sendMailBiz;

	@Autowired
	private ISysConfigBiz sysConfigBiz;

	@Resource
	private SendDriverMailBiz sendDriverMailBiz;

	private String KEY_NAME = "DRIVER_MAIL_ROLE_NAME";

	private String KEY_HOST_NAME = "SEND_DRIVER_MAIL_HOT";

	// cron = 秒 分 时 日 月 周 年
	// @Scheduled(cron = "0 39 15 ? * 1")
	//@Scheduled(cron = "0 0/1 * * * ?")
	public void sendMail() throws Exception {
		logger.info("driver mail start ");
		SysConfig sysConfig = sysConfigBiz.searchByKeyName(KEY_NAME);
		logger.info("driver mail Role name configuration value = " + sysConfig.getKeyValue());

		SysConfig hotNameConfig = sysConfigBiz.searchByKeyName(KEY_HOST_NAME);
		logger.info("driver mail Host name value = " + hotNameConfig.getKeyValue());
		if (!VMUtils.getCurrentMachineName().equals(hotNameConfig.getKeyValue()))
			return;
		logger.info(" Current Machine Name = " + VMUtils.getCurrentMachineName() + " = " + hotNameConfig.getKeyValue());
		String[] roleNames = sysConfig.getKeyValue().split(",");
		Set<Long> userIds = new HashSet<Long>();
		for (String roleName : roleNames) {
			Long roleId = sendDriverMailBiz.findRoleIdByName(roleName);
			userIds.addAll(sendDriverMailBiz.findUsersHasRole(roleId));
		}
		logger.info(" send mail count = " + userIds.size());
		for (Long userId : userIds) {
			if (StringUtil.isBlank(sendDriverMailBiz.findUser(userId)))
				continue;
			Employee employee = sendDriverMailBiz.getEmployeeByCode(sendDriverMailBiz.findUser(userId));
			if (StringUtil.isBlank(employee.getEmpEmail()))
				continue;
			String fileName = sendDriverMailBiz.generateAccessories(userId);
			if (StringUtil.isBlank(fileName))
				continue;
			File file = new File(fileName);
			sendMailBiz.sendMail(employee.getEmpEmail(), createContent(), file, "上周异常统计报表.xls",
					"SPMS排班系统上周排班异常提醒邮件");

		}
		logger.info("driver mail end ");
	}

	private String createContent() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><head></head><body>");
		sb.append("<label style='width:2em;'></label>附件是SPMS排班系统上周排班异常数据，请尽快核实</p>");
		sb.append("<div style='color:red;font-size:12px;'>[系统发送邮件,请勿回复]</div></body></html>");
		return sb.toString();
	}

}
