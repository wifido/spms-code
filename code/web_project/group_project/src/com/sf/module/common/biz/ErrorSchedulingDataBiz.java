package com.sf.module.common.biz;

import static com.sf.module.common.domain.Constants.LIMIT;
import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.domain.Constants.START;
import static com.sf.module.common.domain.Constants.TOTAL_SIZE;

import java.util.HashMap;
import java.util.List;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.dao.ErrorSchedulingDataDao;
import com.sf.module.training.biz.TrainingExportHandler;

public class ErrorSchedulingDataBiz extends BaseBiz{
	private ErrorSchedulingDataDao errorSchedulingDataDao;
	
	public HashMap<String, Object> query(HashMap<String, String> httpRequestParameter) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(TOTAL_SIZE, errorSchedulingDataDao.queryErrorSchedulingCount(httpRequestParameter));
		String limit = String.valueOf((Integer.parseInt(httpRequestParameter.get(START)) + Integer.parseInt(httpRequestParameter.get(LIMIT))));
		httpRequestParameter.put(LIMIT, limit);
		resultMap.put(ROOT, errorSchedulingDataDao.queryErrorSchedulingList(httpRequestParameter));
		return resultMap;
	}
	
	public String exporErrorSchedulingData(HashMap<String, String> httpRequestParameter) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		List list = errorSchedulingDataDao.queryExportErrorSchedulingList(httpRequestParameter);
		if (list.size() == 0) {
			throw new BizException("该网点暂无错误排班数据！");
		}
		ErrorSchedulingDataExportHandler errorSchedulingDataExportHandler = new ErrorSchedulingDataExportHandler();
		errorSchedulingDataExportHandler.write(list, "SAP返回错误排班数据导出");
	    return errorSchedulingDataExportHandler.getTargetFilePath();
	}

	public void setErrorSchedulingDataDao(ErrorSchedulingDataDao errorSchedulingDataDao) {
		this.errorSchedulingDataDao = errorSchedulingDataDao;
	}
	
	public ErrorSchedulingDataDao getErrorSchedulingDataDao() {
		return errorSchedulingDataDao;
	}
}
