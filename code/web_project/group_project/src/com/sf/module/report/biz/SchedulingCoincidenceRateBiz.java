package com.sf.module.report.biz;

import static com.sf.module.common.domain.Constants.ROOT;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.organization.domain.Department;
import com.sf.module.report.dao.SchedulingCoincidenceRateDao;
import com.sf.module.training.biz.TrainingExportHandler;

public class SchedulingCoincidenceRateBiz extends BaseBiz {
	
	private SchedulingCoincidenceRateDao schedulingCoincidenceRateDao;
	
	public HashMap<String, Object> query(String departmentCode, String yearMonth, String positionType, int start, int limit) {
		Department department = DepartmentCacheBiz.getDepartmentByCode(departmentCode);
		if(!departmentCode.equals(department.getAreaDeptCode())){
			HashMap<String, Object> result = schedulingCoincidenceRateDao.query(departmentCode, yearMonth, positionType, start, limit);
			return result;
		}
		HashMap<String, Object> result = schedulingCoincidenceRateDao.queryCoincidenceRateForAreaDepartment(departmentCode, yearMonth, positionType, start, limit);
		List<Map<String,Object>> list = (List<Map<String,Object>>)result.get(ROOT);
		Map<String, Object> map = list.get(0);
		map.put("DEPT_ID", department.getId());
		map.put("DEPT_CODE", departmentCode);
		map.put("YEAR_MONTH", yearMonth);
		return result;
	}
	
	public String export(String departmentCode, String yearMonth, String positionType) {
		List list = schedulingCoincidenceRateDao.queryExport(departmentCode, yearMonth, positionType);
		if (list.size() == 0) {
			throw new BizException("该网点暂无排班吻合率信息！");
		}

		HashMap<String, Object> result = schedulingCoincidenceRateDao.queryCoincidenceRateForAreaDepartment(departmentCode, yearMonth, positionType, 0, 1);
		List<Map<String,Object>> temp = (List<Map<String,Object>>)result.get(ROOT);
		Map<String, Object> map = temp.get(0);
		map.put("DEPT_CODE", "总计");
		map.put("YEAR_MONTH", "");
		list.add(map);
		
		SchedulingCoincidenceRateExportHandler schedulingCoincidenceRateExportHandler = new SchedulingCoincidenceRateExportHandler();
		schedulingCoincidenceRateExportHandler.write(list, "排班吻合率导出");
	    return schedulingCoincidenceRateExportHandler.getTargetFilePath();
	}
	
	public SchedulingCoincidenceRateDao getSchedulingCoincidenceRateDao() {
		return schedulingCoincidenceRateDao;
	}

	public void setSchedulingCoincidenceRateDao(SchedulingCoincidenceRateDao schedulingCoincidenceRateDao) {
		this.schedulingCoincidenceRateDao = schedulingCoincidenceRateDao;
	}

	
}
