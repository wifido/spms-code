package com.sf.module.dispatch.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sf.module.common.biz.ISendMailBiz;
import com.sf.module.dispatch.biz.MaintenanceMailBiz;
import com.sf.module.dispatch.dao.MaintenanceMailDao;
import com.sf.module.dispatch.dao.SchedulingForDispatchDao;
import com.sf.module.dispatch.domain.MaintenanceMail;

@Component
public class SendMailJob {
	@Autowired
	private ISendMailBiz sendMailBiz;
	
	@Autowired
	private MaintenanceMailBiz maintenanceMailBiz;
	
	@Autowired
	private MaintenanceMailDao maintenanceMailDao;
	
	@Autowired
	private SchedulingForDispatchDao schedulingForDispatchDao;
	// cron = 秒 分 时 日 月 周 年
	@Scheduled(cron = "0 30 10 * * ?")
	public void clearNokeKey() throws Exception {
		maintenanceMailDao.clearLockMailPlan();
	}
	// cron = 秒 分 时 日 月 周 年
	@Scheduled(cron = "0 0 11 * * ?")
	public void sendMail() throws Exception {
		if(maintenanceMailDao.lockMailPlan() > 0) {
			Map<String, String> map = buildQueryparam();
			String yaersMonth = getCurrentYM();
			HashMap<String, Object> resultMap = maintenanceMailBiz.queryMaintenanceMailByDeptCode(map);
			sendMailByDeptCode(yaersMonth, resultMap);
		}
	}

	private void sendMailByDeptCode(String yaersMonth, HashMap<String, Object> resultMap) {
		List<MaintenanceMail> list = (List<MaintenanceMail>) resultMap.get("root");
		for (MaintenanceMail mail : list) {
			String[] mails = mail.getEmailAccount().split(",");
			for (int i = 0; i < mails.length; i++) {
				HashMap<String, String> queryCriteria = new HashMap<String, String>();
				queryCriteria.put("MONTH_ID", yaersMonth);
				queryCriteria.put("DEPARTMENT_CODE", mail.getDepartmentCode());
				if (schedulingForDispatchDao.getExportExcelCount(queryCriteria) > 0) {
					sendMailBiz.sendMail(mails[i],
							createContent(mail.getDepartmentCode()) , null, null, "一线排班系统未排班邮件提醒");
				}
			}
		}
	}

	private String getCurrentYM() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
		return sf.format(new Date());
	}

	private Map<String, String> buildQueryparam() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("DEPARTMENT_CODE", "001");
		map.put("limit", "1000000000");
		map.put("start", "0");
		return map;
	}
	
	private String createContent(String departmentCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><head></head><body>");
		sb.append("<label style='width:2em;'></label>" + departmentCode + "网点的维护人您好，"+ departmentCode+" 网点存在未排班人员，请及时维护排班！</p>");
		sb.append("请进入排班系统进行维护: http://spms.sf-express.com/spms ");
		sb.append("<div style='color:red;font-size:12px;'>[系统发送邮件,请勿回复]</div></body></html>");
		return sb.toString();
	}
}
