package com.sf.module.report.biz;

import java.util.HashMap;
import java.util.List;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.report.dao.SchedulingTableDao;

public class SchedulingTableBiz extends BaseBiz {
	
	private SchedulingTableDao schedulingTableDao;
	
	public HashMap<String, Object> query(String departmentCode, String yearMonth,  int start, int limit) {
		return this.schedulingTableDao.query(departmentCode, yearMonth, start, limit);
	}
	
	public String export(String departmentCode, String yearMonth) {
		List list = schedulingTableDao.export(departmentCode, yearMonth);
		if (list.size() == 0) {
			throw new BizException("该网点暂无排班信息！");
		}
		SchedulingTableHandler schedulingTableHandler = new SchedulingTableHandler();
		schedulingTableHandler.write(list, "排班表信息导出");
	    return schedulingTableHandler.getTargetFilePath();
	}

	public SchedulingTableDao getSchedulingTableDao() {
		return schedulingTableDao;
	}

	public void setSchedulingTableDao(SchedulingTableDao schedulingTableDao) {
		this.schedulingTableDao = schedulingTableDao;
	}
	
}
