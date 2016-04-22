package com.sf.module.report.biz;

import static com.sf.module.common.domain.Constants.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.report.dao.StatisticsReportDao;

@Component
public class StatisticsReportBiz extends BaseBiz {
	@Resource
	private StatisticsReportDao statisticsReportDao;

	public Map<String, Object> queryStatisticsReport(HashMap<String, String> parameters) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String limit = String.valueOf((Integer.parseInt(parameters.get(START)) + Integer.parseInt(parameters.get(LIMIT))));
		parameters.put(LIMIT, limit);
		resultMap.put(TOTAL_SIZE, statisticsReportDao.queryStatisticsReportCount(parameters));
		resultMap.put(ROOT, statisticsReportDao.queryStatisticsReportList(parameters));
		return resultMap;
	}
	
	public String export(HashMap<String,String> parameters) {
		List<Map<String, Object>> exportList = statisticsReportDao.exportReportList(parameters);
		
		if (exportList.size() == 0) {
            throw new BizException("导出失败！没有符合条件的数据！");
        }
		
		StatisticsReportExportHandler exportHandler = new StatisticsReportExportHandler();
		exportHandler.write(exportList, "排班统计报表导出");
		return exportHandler.getTargetFilePath();
	}
}
