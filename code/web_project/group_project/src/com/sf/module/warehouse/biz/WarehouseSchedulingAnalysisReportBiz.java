package com.sf.module.warehouse.biz;

import static com.sf.module.common.domain.Constants.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.warehouse.dao.WarehouseSchedulingAnalysisReportDao;

@Service
public class WarehouseSchedulingAnalysisReportBiz extends BaseBiz {

	@Resource
	private WarehouseSchedulingAnalysisReportDao warehouseSchedulingAnalysisReportDao;

	public HashMap<String, Object> query(String departmentCode, String yearMonth, int start, int limit) {
		return this.warehouseSchedulingAnalysisReportDao.query(departmentCode, yearMonth, start, limit);
	}
	
	public HashMap<String, Object> export(String departmentCode, String yearMonth, int start, int limit) {
		List<Map<String, Object>> list = (List<Map<String, Object>>) warehouseSchedulingAnalysisReportDao.query(departmentCode, yearMonth, start, limit).get(ROOT);
		if (list.isEmpty()) {
			throw new BizException("没有数据！");
		}
		try {
			WarehouseSchedulingAnalysisReportExportHandler handler = new WarehouseSchedulingAnalysisReportExportHandler();
			handler.handle(list);
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put(KEY_SUCCESS, true);
			result.put(DOWNLOAD_PATH, handler.getDownloadPath());
			return result;
		} catch (Exception e) {
			throw new BizException(e.getMessage());
		}
	}
}
