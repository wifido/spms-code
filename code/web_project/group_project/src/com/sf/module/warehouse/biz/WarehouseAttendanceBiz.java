package com.sf.module.warehouse.biz;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.sf.module.common.domain.Constants.*;
import static com.sf.module.common.util.StringUtil.getMonthLeftPaddingWithZero;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.util.BeanUtil;
import com.sf.module.warehouse.dao.WarehouseAttendanceDao;
import com.sf.module.warehouse.domain.WarehouseAttendance;

@Component
public class WarehouseAttendanceBiz extends BaseBiz {
	private static final String EMP_DUTY_NAME = "EMP_DUTY_NAME";
	private static final String DEPARTMENT_CODE = "DEPARTMENT_CODE";
	private static final String MONTH_ID = "MONTH_ID";
	private static final String PERSON_TYPE = "PERSON_TYPE";
	private static final String EMP_NAME = "EMP_NAME";
	private static final String DEPT_CODE = "DEPT_CODE";
	private static final String AREA_CODE = "AREA_CODE";
	private static final String EMP_CODE = "EMP_CODE";
	private static final String WORK_TIME = "WORK_TIME";
	private static final String WORK_DATE = "WORK_DATE";
	@Resource
	private WarehouseAttendanceDao warehouseAttendanceDao;

	public Map<String, Object> queryAttendance(HashMap<String, String> parameters) {
		String limit = String.valueOf((Integer.parseInt(parameters.get(START)) + Integer.parseInt(parameters.get(LIMIT))));
		parameters.put(LIMIT, limit);
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(TOTAL_SIZE, warehouseAttendanceDao.queryAttendanceCount(parameters));
		List<Map<String, Object>> attendanceList = (List<Map<String, Object>>) warehouseAttendanceDao.queryAttendanceList(parameters);
		List<WarehouseAttendance> warehouseAttenDanceList = buildWarhouseAttendance(attendanceList);
		countWorkTime(warehouseAttenDanceList);
		resultMap.put(ROOT, warehouseAttenDanceList);
		return resultMap;
	}

	private List<WarehouseAttendance> buildWarhouseAttendance(List<Map<String, Object>> attendanceList) {
		Map<String, WarehouseAttendance> attendanceMap = newHashMap();
		for (Map<String, Object> attendance : attendanceList) {
			Date attendanceDate = (Date) attendance.get(WORK_DATE);
			DateTime time = new DateTime(attendanceDate);
			String month = String.format("%s-%s", time.getYear(), getMonthLeftPaddingWithZero(time.getMonthOfYear()));
			int day = time.getDayOfMonth();
			String deptCode = (String) attendance.get(DEPT_CODE);
			BigDecimal workLength = (BigDecimal) attendance.get(WORK_TIME);
			String workTime = "0";
			if (workLength != null) {
				workTime = String.valueOf(workLength.intValue());
			}
			String empCode = (String) attendance.get(EMP_CODE);
			String key = String.format("%s-%s-%s", month, empCode, deptCode);
			if (attendanceMap.containsKey(key)) {
				WarehouseAttendance warehouseAttendance = attendanceMap.get(key);
				warehouseAttendance.setMonth(month);
				warehouseAttendance.setWorkTimeWithSpecifyDay(day, workTime);
				continue;
			}
			WarehouseAttendance warehouseAttendance = new WarehouseAttendance();
			warehouseAttendance.setMonth(month);
			warehouseAttendance.setAreaCode((String) attendance.get(AREA_CODE));
			warehouseAttendance.setDeptCode((String) attendance.get(DEPT_CODE));
			warehouseAttendance.setEmpCode(empCode);
			warehouseAttendance.setEmpName((String) attendance.get(EMP_NAME));
			warehouseAttendance.setPersonType((String) attendance.get(PERSON_TYPE));
			warehouseAttendance.setDutyName((String) attendance.get(EMP_DUTY_NAME));
			warehouseAttendance.setWorkTimeWithSpecifyDay(day, workTime);
			attendanceMap.put(key, warehouseAttendance);
		}
		return newArrayList(attendanceMap.values());
	}

	private void countWorkTime(List<WarehouseAttendance> list) {
		for (WarehouseAttendance warehouseAttendance : list) {
			warehouseAttendance.countTotalWorkTime();
		}
	}

	public Map<String, Object> exportAttendance(HashMap<String, String> queryParameter) {
		List list = buildWarhouseAttendance(warehouseAttendanceDao.queryAttendanceList(queryParameter));
		if (list.isEmpty()) {
			throw new BizException("没有数据！");
		}
		try {
			countWorkTime(list);
			WarehouseAttendanceExportHandler handler = new WarehouseAttendanceExportHandler();
			handler.setSchedulingMonth(queryParameter.get(MONTH_ID));
			handler.setDepartment(queryParameter.get(DEPARTMENT_CODE));
			handler.setQueryParameter(queryParameter);
			handler.handle(BeanUtil.transListBeanToMap(list));
			Map<String, Object> result = new HashMap<String, Object>();
			result.put(KEY_SUCCESS, true);
			result.put(DOWNLOAD_PATH, handler.getDownloadPath());
			return result;
		} catch (Exception e) {
			throw new BizException(e.getMessage());
		}
	}

}
