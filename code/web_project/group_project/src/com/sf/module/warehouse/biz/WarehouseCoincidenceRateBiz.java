package com.sf.module.warehouse.biz;

import static com.sf.module.common.domain.Constants.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.warehouse.dao.WarehouseCoincidenceRateDao;

@Service
public class WarehouseCoincidenceRateBiz extends BaseBiz {

	private static final String SCHED_AGREE_RATE = "SCHED_AGREE_RATE";
	@Resource
	private WarehouseCoincidenceRateDao coincidenceReateDao;

	public HashMap<String, Object> query(String departmentCode, String yearMonth, int start, int limit) {
		return this.coincidenceReateDao.query(departmentCode, yearMonth, start, limit);
	}

	public HashMap<String, Object> exportCoincidenceRate(String departmentCode, String yearMonth, int start, int limit) {
		List<Map<String, Object>> list = (List<Map<String, Object>>) coincidenceReateDao.query(departmentCode, yearMonth, start, limit).get(ROOT);
		if (list.isEmpty()) {
			throw new BizException("没有数据！");
		}
		for (Map<String, Object> map : list) {
			BigDecimal concidenceRate = (BigDecimal) map.get(SCHED_AGREE_RATE);
			if (concidenceRate == null) {
				continue;
			}
			map.put(SCHED_AGREE_RATE, concidenceRate + "%");
		}
		try {
			WarehouseCoincidenceRateExportHandler handler = new WarehouseCoincidenceRateExportHandler();
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
