package com.sf.module.report.biz;

import java.util.HashMap;
import java.util.List;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.report.dao.WatchkeeperCountDao;



public class WatchkeeperCountBiz extends BaseBiz{

	private WatchkeeperCountDao watchkeeperCountDao;
	
	public HashMap<String, Object> query(String departmentCode, String dayOfMonth,  int start, int limit) {
		return this.watchkeeperCountDao.query(departmentCode, dayOfMonth, start, limit);
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String export(String departmentCode, String dayOfMonth) {
		
		List list = watchkeeperCountDao.export(departmentCode, dayOfMonth);
		if (list.size() == 0) {
			throw new BizException("该网点暂无值班人员统计表信息！");
		}
		WatchkeeperCountHandler watchkeeperCountHandler = new WatchkeeperCountHandler();
		watchkeeperCountHandler.write(list, "值班人员统计表信息导出");
	    return watchkeeperCountHandler.getTargetFilePath();
	}

	public WatchkeeperCountDao getWatchkeeperCountDao() {
		return watchkeeperCountDao;
	}

	public void setWatchkeeperCountDao(WatchkeeperCountDao watchkeeperCountDao) {
		this.watchkeeperCountDao = watchkeeperCountDao;
	}

}
