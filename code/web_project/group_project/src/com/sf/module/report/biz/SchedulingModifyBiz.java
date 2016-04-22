package com.sf.module.report.biz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.sf.framework.base.IPage;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.dao.OssDepartmentDao;
import com.sf.module.common.domain.OssDepartment;
import com.sf.module.common.util.Clock;
import com.sf.module.operation.dao.SchedulingDao;
import com.sf.module.operation.domain.Scheduling;
import com.sf.module.operation.util.SchedulingModifyReportTemplate;
import com.sf.module.report.dao.SchedulingModifyDao;
import com.sf.module.report.domain.SchedulingModify;

public class SchedulingModifyBiz extends BaseBiz {

	private SchedulingModifyDao schedulingModifyDao;
	private OssDepartmentDao ossDepartmentDao;
	private SchedulingDao schedulingDao;

	private static final String reportName = "历史排班修改记录";

	public void addFormScheduling(final Scheduling scheduling) {
		if (!this.schedulingDao.checkChangSchedule(scheduling.getId(), scheduling.getScheduleCode())) {
			return;
		}
		int modifype = caluateModifyType(scheduling);
		SchedulingModify schedulingModify = new SchedulingModify();
		schedulingModify.setEmpCode(scheduling.getEmpCode());
		schedulingModify.setScheduleDt(scheduling.getSheduleDt());
		schedulingModify.setScheduleCode(scheduling.getScheduleCode());
		schedulingModify.setModifiedTm(scheduling.getModifiedTm());
		schedulingModify.setModifiedEmpCode(scheduling.getModifiedEmpCode());
		schedulingModify.setModifyType(modifype);
		schedulingModify.setDeptId(scheduling.getDeptId());
		schedulingModify.setYearMonth(extractMonth(scheduling.getSheduleDt()));
		OssDepartment department = this.ossDepartmentDao.getById(scheduling.getDeptId());
		schedulingModify.setDeptCode(department.getDeptCode());
		schedulingModify.setAreaCode(department.getAreaCode());
		this.schedulingModifyDao.save(schedulingModify);
	}
	
	public IPage<SchedulingModify> query(String deptCode, String month, String empCode, int start, int limit) {
		return schedulingModifyDao.queryByPage(deptCode, getCurrentUser().getId(), month, empCode, start, limit);
	}

	public String exprot(String deptCode, String month, String empCode) {
		List<SchedulingModify> list = schedulingModifyDao.query(deptCode, getCurrentUser().getId(), month, empCode);
		String targetFilePath = writeToExcel(deptCode, month, list);
		return targetFilePath;
	}

	private static String extractMonth(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(date);
	}

	private int caluateModifyType(Scheduling scheduling) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String sheduleDt = sdf.format(scheduling.getSheduleDt());
		String modifiedTm = sdf.format(scheduling.getModifiedTm());
		int modifype = Clock.range(sheduleDt, modifiedTm) > 3 ? 1 : 0;
		return modifype;
	}

	private String writeToExcel(String deptCode, String month, List<SchedulingModify> list) {
		if (list.size() == 0) {
			throw new BizException("导出失败！没有符合条件的数据！");
		}
		SchedulingModifyReportTemplate createTemplate = SchedulingModifyReportTemplate.createTemplate(deptCode, month);
		createTemplate.writeAsObject(list, reportName);
		String targetFilePath = createTemplate.getTargetFilePath();
		return targetFilePath;
	}

	public void setSchedulingModifyDao(SchedulingModifyDao schedulingModifyDao) {
		this.schedulingModifyDao = schedulingModifyDao;
	}

	public OssDepartmentDao getOssDepartmentDao() {
		return ossDepartmentDao;
	}

	public void setOssDepartmentDao(OssDepartmentDao ossDepartmentDao) {
		this.ossDepartmentDao = ossDepartmentDao;
	}

	public void setSchedulingDao(SchedulingDao schedulingDao) {
		this.schedulingDao = schedulingDao;
	}

}
