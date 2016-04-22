package com.sf.module.report.biz;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.report.dao.SchedulingInputStatisticalDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.domain.Constants.TOTAL_SIZE;

@Service
public class SchedulingInputStatisticalBiz extends BaseBiz{
	@Resource
	private SchedulingInputStatisticalDao schedulingInputStatisticalDao;

    public HashMap<String, Object> query(String departmentCode, String yearMonth, String start, String limit) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        int totalSize = schedulingInputStatisticalDao.queryTotalSize(departmentCode, yearMonth);
        List list = schedulingInputStatisticalDao.query(departmentCode, yearMonth, start, limit);

        resultMap.put(TOTAL_SIZE, totalSize);
        resultMap.put(ROOT, list);
        return resultMap;
    }

    public String export(String departmentCode, String yearMonth) {
        List exportData = schedulingInputStatisticalDao.querySchedulingInput(departmentCode, yearMonth);

        if (exportData.size() == 0) {
            throw new BizException("导出失败！没有符合条件的数据！");
        }

        SchedulingInputStatisticalExportHandler statisticalExportHandler = new SchedulingInputStatisticalExportHandler();

        statisticalExportHandler.write(exportData, "排班录入统计报表.xls");
        return statisticalExportHandler.getTargetFilePath();
    }
}
