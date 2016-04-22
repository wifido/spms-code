package com.sf.module.dispatch.domain;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.sf.framework.util.StringUtils.isNotEmpty;
import static com.sf.module.common.util.StringUtil.removeQuotes;
import static com.sf.module.dispatch.domain.SchedulingWrangler.FailedReasonType.EMPLOYEE_CODE_ALREADY_SCHEDULING;
import static com.sf.module.dispatch.domain.SchedulingWrangler.FailedReasonType.EMPLOYEE_CODE_ISVALID;
import static com.sf.module.dispatch.domain.SchedulingWrangler.FailedReasonType.EMPTY_EMPLOYEE_CODE;
import static com.sf.module.dispatch.domain.SchedulingWrangler.FailedReasonType.EMPTY_ZONE_CODE;
import static com.sf.module.dispatch.domain.SchedulingWrangler.FailedReasonType.NOT_ENOUGH_FREE_TIME;
import static com.sf.module.dispatch.domain.SchedulingWrangler.FailedReasonType.SCHEDULING_DUPLICATE_DATA;
import static com.sf.module.dispatch.domain.SchedulingWrangler.FailedReasonType.SCHEDULING_STARTTIME_MUST_LESS_ENDTIME;
import static com.sf.module.dispatch.domain.SchedulingWrangler.FailedReasonType.WRONG_DATE_FORMAT;
import static org.apache.commons.lang.StringUtils.isEmpty;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.sf.module.dispatch.dao.SchedulingForDispatchDao;

public class SchedulingWrangler {
	private HashMap<FailedReasonType, FailedReasonType> failedReason;
	private static final String TIME_PATTERN = "^((0\\d{1}|1\\d{1}|2[0-3])([0-5]\\d{1})|2400)$";
	private static final String SLASH = "/";

	public List<SchedulingForDispatch> getDaysScheduling() {
		return daysScheduling;
	}

	private final List<SchedulingForDispatch> daysScheduling;

	public SchedulingWrangler(List<SchedulingForDispatch> daysScheduling) {
		this.daysScheduling = daysScheduling;
		if (failedReason == null) {
			failedReason = newHashMap();
		}
	}

	public boolean hasEmptyZoneCode() {
		return false;
	}

	public String getFailedErrorMessage() {
		int restCount = 0;
		boolean isFullTime = false;
		boolean checkFialed = false;
		for (SchedulingForDispatch schedulingForDispatch : daysScheduling) {

			if (schedulingForDispatch.isCheckFailed()) {
				putErrorMsg(WRONG_DATE_FORMAT);
				checkFialed = true;
				break;
			}

			if (isEmpty(schedulingForDispatch.getDepartmentCode())) {
				putErrorMsg(EMPTY_ZONE_CODE);
			}

			if (isEmpty(schedulingForDispatch.getEmployeeCode())) {
				putErrorMsg(EMPTY_EMPLOYEE_CODE);
			}

			if (!schedulingForDispatch.getBeginTime().isEmpty() && !schedulingForDispatch.getEndTime().isEmpty()) {
				if(schedulingForDispatch.getBeginTime().length() != 6 ||  schedulingForDispatch.getEndTime().length() != 6){
					putErrorMsg(WRONG_DATE_FORMAT);
				}
				String currentBeginTime = schedulingForDispatch.getBeginTime().substring(0, 4);
				String currentEndTime = schedulingForDispatch.getEndTime().substring(0, 4);
				boolean validBeginTime = currentBeginTime.matches(TIME_PATTERN);
				boolean validEndTime = currentEndTime.matches(TIME_PATTERN);
				if (!validBeginTime || !validEndTime) {
					putErrorMsg(WRONG_DATE_FORMAT);
				}
				if (Integer.parseInt(currentBeginTime) == Integer.parseInt(currentEndTime)) {
					putErrorMsg(SCHEDULING_STARTTIME_MUST_LESS_ENDTIME);
				}
			}
		}

		if (checkFialed) {
			return getErrorMsg();
		}

		for (int i = 0; i < daysScheduling.size(); i++) {
			SchedulingForDispatch currentObj = daysScheduling.get(i);
			if (currentObj.getBeginTime().equals("") || currentObj.getEndTime().equals("") || currentObj.getBeginTime().equals("休")
					|| currentObj.getEndTime().equals("休")) {
				continue;
			}
			String currentBeginTime = currentObj.getBeginTime().substring(0, 4);
			String currentEndTime = currentObj.getEndTime().substring(0, 4);
			String currentDayOfMonth = currentObj.getDayOfMonth();

			for (int j = 0; j < daysScheduling.size(); j++) {
				SchedulingForDispatch contrastObj = daysScheduling.get(j);
				if (contrastObj.getBeginTime().equals("") || contrastObj.getEndTime().equals("") || contrastObj.getBeginTime().equals("休")
						|| contrastObj.getEndTime().equals("休")) {
					continue;
				}
				String contrastBeginTime = contrastObj.getBeginTime().substring(0, 4);
				String contrastEndTime = contrastObj.getEndTime().substring(0, 4);
				String contrastDayOfMonth = contrastObj.getDayOfMonth();

				if (i != j) {
					if (currentDayOfMonth.equals(contrastDayOfMonth)) {
						validCrossTime(currentBeginTime, currentEndTime, contrastBeginTime, contrastEndTime);
					}
				}
			}
		}
		return getErrorMsg();
	}

	private void validCrossTime(String currentBeginTime, String currentEndTime, String contrastBeginTime, String contrastEndTime) {
		Integer currentBeginTimeInt = Integer.parseInt(currentBeginTime);
		Integer contrastBeginTimeInt = Integer.parseInt(contrastBeginTime);
		Integer contrastEndTimeInt = Integer.parseInt(contrastEndTime);
		Integer currentEndTimeInt = Integer.parseInt(currentEndTime); 
		if(contrastBeginTimeInt > contrastEndTimeInt) {
			contrastBeginTimeInt = contrastBeginTimeInt - 2400;
		}
		if(currentBeginTimeInt > currentEndTimeInt) {
			currentBeginTimeInt = currentBeginTimeInt - 2400;
		}
		if (!(currentBeginTimeInt > contrastEndTimeInt
				|| currentEndTimeInt < contrastBeginTimeInt)) {
			putErrorMsg(SCHEDULING_DUPLICATE_DATA);
		}
	}

	private String getErrorMsg() {
		StringBuilder failedReasonString = new StringBuilder();
		for (FailedReasonType failed_reason : failedReason.keySet()) {
			failedReasonString.append(failed_reason.reason);
		}
		return failedReasonString.toString();
	}

	public void putErrorMsg(FailedReasonType reasonType) {
		failedReason.put(reasonType, reasonType);
	}

	public void existEmployeeCode(String employeeCode, String deptCode, SchedulingForDispatchDao schedulingForDispatchDao) {
		String existEmployyeCodeQuerySql = "select e.emp_code " + "from tm_oss_employee e, tm_department dep " + "where e.emp_code = '" + employeeCode + "' "
				+ "and dep.dept_code = '" + deptCode + "'  and dep.dept_id = e.dept_id and e.emp_post_type = 2 ";
		if (!schedulingForDispatchDao.generalValidationMethod(existEmployyeCodeQuerySql.toString())) {
			putErrorMsg(EMPLOYEE_CODE_ISVALID);
		}
	}

    public void setErrorMessageByDeptCode(String deptCode, String departmentCodes) {
        ArrayList<String> departmentList = newArrayList(removeQuotes(departmentCodes).split(","));

        if (!departmentList.contains(deptCode)) {
            putErrorMsg(FailedReasonType.SCHEDULING_DEPARTMENT_CODE_INVALID);
        }
    }

    /**
	 * 3.地区代码不能为空 4.网点代码不能为空 5.工号不能为空 或不存在 6.非小时工每月排休不能少于4天 7.小时工一天不能超过4个小时
	 * 8.小时工不能连续上6天班
	 */
	public static enum FailedReasonType {
		EMPTY_ZONE_CODE("网点不能为空;"),
        EMPTY_EMPLOYEE_CODE("工号不能为空;"),
        NOT_ENOUGH_FREE_TIME("提醒：排休少于4天;"),
        EMPLOYEE_CODE_ISVALID("员工工号不存在，请确认后再导入;"),
        EMPLOYEE_CODE_ALREADY_SCHEDULING("员工当月已经排班，请确认后再导入;"),
        WRONG_DATE_FORMAT("排班导入时间格式有误！请确认后重新导入;"),
        SCHEDULING_DUPLICATE_DATA("排班时间段出现交叉数据;"),
        SCHEDULING_STARTTIME_MUST_LESS_ENDTIME("排班时间不能大于等于24小时;"),
        SCHEDULING_DEPARTMENT_CODE_INVALID("当前网点与数据导入网点不同，不能导入");

		public String reason;

		FailedReasonType(String reason) {
			this.reason = reason;
		}
	}
}
