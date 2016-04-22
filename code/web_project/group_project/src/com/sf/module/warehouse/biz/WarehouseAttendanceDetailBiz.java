package com.sf.module.warehouse.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.warehouse.dao.WarehouseAttendanceDetailDao;

@Component
public class WarehouseAttendanceDetailBiz extends BaseBiz {

	@Resource
	private WarehouseAttendanceDetailDao attendanceDetailDao;
	private static final String SUCCESS = "success";
	private static final String DOWNLOAD_PATH = "downloadPath";

	public Map<String, Object> query(
			HashMap<String, String> httpRequestParameter) {
		int totalSize = attendanceDetailDao
				.queryTotalSize(httpRequestParameter);

		List list = attendanceDetailDao.query(httpRequestParameter);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("totalSize", totalSize);
		resultMap.put("root", list);
		return resultMap;
	}

	public Map<String, Object> exportWarehouseAttendanceDetail(HashMap<String, String> requestParameter) {
		List list = attendanceDetailDao.query(requestParameter);
		
		if (list.isEmpty()) {
			throw new BizException("没有可导出的数据！");
		}
		
		try {
			WarehouseAttendanceDetailExportHandler handler = new WarehouseAttendanceDetailExportHandler();
			handler.handle(list);
			
			Map<String, Object> result = new HashMap<String, Object>();
			result.put(SUCCESS, true);
			result.put(DOWNLOAD_PATH, handler.getDownloadPath());
			return result;
		} catch (Exception e) {
			log.debug("export error!" + e);
			throw new BizException("exception:" + e); 
		}
	}
}
