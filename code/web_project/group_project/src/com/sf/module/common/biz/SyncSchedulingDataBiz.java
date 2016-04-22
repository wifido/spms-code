package com.sf.module.common.biz;

import static com.sf.module.common.domain.Constants.LIMIT;
import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.domain.Constants.START;
import static com.sf.module.common.domain.Constants.TOTAL_SIZE;

import java.util.HashMap;
import java.util.List;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.dao.SyncSchedulingDataDao;

public class SyncSchedulingDataBiz extends BaseBiz{
	private SyncSchedulingDataDao syncSchedulingDataDao;

	public HashMap<String, Object> query(HashMap<String, String> httpRequestParameter) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(TOTAL_SIZE, syncSchedulingDataDao.querySyncSchedulingCount(httpRequestParameter));
		String limit = String.valueOf((Integer.parseInt(httpRequestParameter.get(START)) + Integer.parseInt(httpRequestParameter.get(LIMIT))));
		httpRequestParameter.put(LIMIT, limit);
		resultMap.put(ROOT, syncSchedulingDataDao.querySyncSchedulingList(httpRequestParameter));
		return resultMap;
	}
	
	public String exportSyncSchedulinData(HashMap<String, String> httpRequestParameter) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		List list = syncSchedulingDataDao.queryExportSyncSchedulingList(httpRequestParameter);
		if (list.size() == 0) {
			throw new BizException("无排班数据推送！");
		}
		SyncSchedulingDataExportHandler syncSchedulingDataExportHandler = new SyncSchedulingDataExportHandler();
		syncSchedulingDataExportHandler.write(list, "SPMS推送SAP排班数据据导出");
	    return syncSchedulingDataExportHandler.getTargetFilePath();
	}
	
	public SyncSchedulingDataDao getSyncSchedulingDataDao() {
		return syncSchedulingDataDao;
	}

	public void setSyncSchedulingDataDao(SyncSchedulingDataDao syncSchedulingDataDao) {
		this.syncSchedulingDataDao = syncSchedulingDataDao;
	}
	
	
}
