package com.sf.module.dispatch.domain;

import static com.sf.framework.util.StringUtils.isNotEmpty;
import static com.sf.module.common.domain.Constants.REST_MARK;
import static com.sf.module.common.util.StringUtil.isNotBlank;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;

import com.sf.framework.base.domain.BaseEntity;
import com.sf.module.common.domain.DayInMonth;
import com.sf.module.common.util.StringUtil;

public class SchedulingForDispatch extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private Long schedulingId;
	private String schedulingCode;
	private String dayOfMonth;
	private String departmentCode;
	private Long departmentId;
	private String deptName;
	private String beginTime;
	private String endTime;
	private String monthId;
	private String employeeCode;
	private String employeeName;
	private String createdEmployeeCode;
	private String modifiedEmployeeCode;
	private Date createTime;
	private Date modifiedTime;
	
	private String dimissionTime;
	private String dataSource;
	private String cancelFlag;
	
	private String username;
	private DutyType dutyType;
	private String areaCode;
	private String divisionCode;
	private HashMap map;
	private String errorMsg;
	private String firstDay;
	private String secondDay;
	private String thirdDay;
	private String fourthDay;
	private String fifthDay;
	private String sixthDay;
	private String seventhDay;
	private String eighthDay;
	private String ninthDay;
	private String tenthDay;
	private String eleventhDay;
	private String twelfthDay;
	private String thirteenthDay;
	private String fourteenthDay;
	private String fifteenthDay;
	private String sixteenthDay;
	private String seventeenthDay;
	private String eighteenthDay;
	private String nineteenthDay;
	private String twentiethDay;
	private String twentyFirstDay;
	private String twentySecondDay;
	private String twentyThirdDay;
	private String twentyFourthDay;
	private String twentyFifthDay;
	private String twentySixthDay;
	private String twentySeventhDay;
	private String twentyEighthDay;
	private String twentyNinthDay;
	private String thirtiethDay;
	private String thirtyFirstDay;
	private String startWorkTime;
	private String endWorkTime;
	private String workType;
	private String empPostType;
	private String crossDayType;
	private String beginTimes;
	private String endTimes;
	private boolean checkFailed;
	private boolean hourlyWorker;
	private String personType;
	private int totalRestDays;
	private String scheduledStatus;
	private String totalRestDayString;

	public String getTotalRestDayString() {
		return totalRestDayString;
	}

	public void setTotalRestDayString(String totalRestDayString) {
		this.totalRestDayString = totalRestDayString;
	}

	public String getScheduledStatus() {
		return scheduledStatus;
	}

	public void setScheduledStatus(String scheduledStatus) {
		this.scheduledStatus = scheduledStatus;
	}

	public int getTotalRestDays() {
		return totalRestDays;
	}

	public void setTotalRestDays(int totalRestDays) {
		this.totalRestDays = totalRestDays;
	}

	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public String getDivisionCode() {
		return divisionCode;
	}

	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}

	public boolean isCheckFailed() {
		return checkFailed;
	}

	public void setCheckFailed(boolean checkFailed) {
		this.checkFailed = checkFailed;
	}

	public String getSchedulingCode() {
		return schedulingCode;
	}

	public void setSchedulingCode(String schedulingCode) {
		this.schedulingCode = schedulingCode;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getBeginTimes() {
		return beginTimes;
	}

	public void setBeginTimes(String beginTimes) {
		this.beginTimes = beginTimes;
	}

	public String getEndTimes() {
		return endTimes;
	}

	public void setEndTimes(String endTimes) {
		this.endTimes = endTimes;
	}

	public String getCrossDayType() {
		return crossDayType;
	}

	public void setCrossDayType(String crossDayType) {
		this.crossDayType = crossDayType;
	}

	public String getEmpPostType() {
		return empPostType;
	}

	public void setEmpPostType(String empPostType) {
		this.empPostType = empPostType;
	}

	public boolean isRest() {
		return isNotEmpty(beginTime) && isNotEmpty(endTime);
	}

	public boolean isHugh() {
		return REST_MARK.equals(beginTime) || REST_MARK.equals(endTime);
	}

	public boolean isSlash() {
		return "/".equals(beginTime) || "/".equals(endTime);
	}

	public boolean isNull() {
		return "".equals(beginTime) || "".equals(endTime);
	}

	public void updateWorkTimeWithExpectDay(String day, String workTime, String joinStr) {
		String filedNameForExpectDay = DayInMonth.values()[Integer.parseInt(day)].getName();
		if (workTime.contains(REST_MARK) || workTime.contains("null")) {
			workTime = REST_MARK;
			totalRestDays++;
		}
		if (StringUtil.isBlank(joinStr)) {
			joinStr = "<br>";
		}
		try {
			Field fieldAsExpectDay = getClass().getDeclaredField(filedNameForExpectDay);
			String dayOldValue = (String) fieldAsExpectDay.get(this);
			StringBuilder stringBuilder = new StringBuilder();

			if (isNotBlank(dayOldValue)) {
				stringBuilder.append(dayOldValue);
				stringBuilder.append(joinStr);
			}
			stringBuilder.append(workTime);

			fieldAsExpectDay.set(this, stringBuilder.toString());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		
		totalRestDayString = String.valueOf(totalRestDays);
	}

	public String getFirstDay() {
		return firstDay;
	}

	public void setFirstDay(String firstDay) {
		this.firstDay = firstDay;
	}

	public String getSecondDay() {
		return secondDay;
	}

	public void setSecondDay(String secondDay) {
		this.secondDay = secondDay;
	}

	public String getThirdDay() {
		return thirdDay;
	}

	public void setThirdDay(String thirdDay) {
		this.thirdDay = thirdDay;
	}

	public String getFourthDay() {
		return fourthDay;
	}

	public void setFourthDay(String fourthDay) {
		this.fourthDay = fourthDay;
	}

	public String getFifthDay() {
		return fifthDay;
	}

	public void setFifthDay(String fifthDay) {
		this.fifthDay = fifthDay;
	}

	public String getSixthDay() {
		return sixthDay;
	}

	public void setSixthDay(String sixthDay) {
		this.sixthDay = sixthDay;
	}

	public String getSeventhDay() {
		return seventhDay;
	}

	public void setSeventhDay(String seventhDay) {
		this.seventhDay = seventhDay;
	}

	public String getEighthDay() {
		return eighthDay;
	}

	public void setEighthDay(String eighthDay) {
		this.eighthDay = eighthDay;
	}

	public String getNinthDay() {
		return ninthDay;
	}

	public void setNinthDay(String ninthDay) {
		this.ninthDay = ninthDay;
	}

	public String getTenthDay() {
		return tenthDay;
	}

	public void setTenthDay(String tenthDay) {
		this.tenthDay = tenthDay;
	}

	public String getEleventhDay() {
		return eleventhDay;
	}

	public void setEleventhDay(String eleventhDay) {
		this.eleventhDay = eleventhDay;
	}

	public String getTwelfthDay() {
		return twelfthDay;
	}

	public void setTwelfthDay(String twelfthDay) {
		this.twelfthDay = twelfthDay;
	}

	public String getThirteenthDay() {
		return thirteenthDay;
	}

	public void setThirteenthDay(String thirteenthDay) {
		this.thirteenthDay = thirteenthDay;
	}

	public String getFourteenthDay() {
		return fourteenthDay;
	}

	public void setFourteenthDay(String fourteenthDay) {
		this.fourteenthDay = fourteenthDay;
	}

	public String getFifteenthDay() {
		return fifteenthDay;
	}

	public void setFifteenthDay(String fifteenthDay) {
		this.fifteenthDay = fifteenthDay;
	}

	public String getSixteenthDay() {
		return sixteenthDay;
	}

	public void setSixteenthDay(String sixteenthDay) {
		this.sixteenthDay = sixteenthDay;
	}

	public String getSeventeenthDay() {
		return seventeenthDay;
	}

	public void setSeventeenthDay(String seventeenthDay) {
		this.seventeenthDay = seventeenthDay;
	}

	public String getEighteenthDay() {
		return eighteenthDay;
	}

	public void setEighteenthDay(String eighteenthDay) {
		this.eighteenthDay = eighteenthDay;
	}

	public String getNineteenthDay() {
		return nineteenthDay;
	}

	public void setNineteenthDay(String nineteenthDay) {
		this.nineteenthDay = nineteenthDay;
	}

	public String getTwentiethDay() {
		return twentiethDay;
	}

	public void setTwentiethDay(String twentiethDay) {
		this.twentiethDay = twentiethDay;
	}

	public String getTwentyFirstDay() {
		return twentyFirstDay;
	}

	public void setTwentyFirstDay(String twentyFirstDay) {
		this.twentyFirstDay = twentyFirstDay;
	}

	public String getTwentySecondDay() {
		return twentySecondDay;
	}

	public void setTwentySecondDay(String twentySecondDay) {
		this.twentySecondDay = twentySecondDay;
	}

	public String getTwentyThirdDay() {
		return twentyThirdDay;
	}

	public void setTwentyThirdDay(String twentyThirdDay) {
		this.twentyThirdDay = twentyThirdDay;
	}

	public String getTwentyFourthDay() {
		return twentyFourthDay;
	}

	public void setTwentyFourthDay(String twentyFourthDay) {
		this.twentyFourthDay = twentyFourthDay;
	}

	public String getTwentyFifthDay() {
		return twentyFifthDay;
	}

	public void setTwentyFifthDay(String twentyFifthDay) {
		this.twentyFifthDay = twentyFifthDay;
	}

	public String getTwentySixthDay() {
		return twentySixthDay;
	}

	public void setTwentySixthDay(String twentySixthDay) {
		this.twentySixthDay = twentySixthDay;
	}

	public String getTwentySeventhDay() {
		return twentySeventhDay;
	}

	public void setTwentySeventhDay(String twentySeventhDay) {
		this.twentySeventhDay = twentySeventhDay;
	}

	public String getTwentyEighthDay() {
		return twentyEighthDay;
	}

	public void setTwentyEighthDay(String twentyEighthDay) {
		this.twentyEighthDay = twentyEighthDay;
	}

	public String getTwentyNinthDay() {
		return twentyNinthDay;
	}

	public void setTwentyNinthDay(String twentyNinthDay) {
		this.twentyNinthDay = twentyNinthDay;
	}

	public String getThirtiethDay() {
		return thirtiethDay;
	}

	public void setThirtiethDay(String thirtiethDay) {
		this.thirtiethDay = thirtiethDay;
	}

	public String getThirtyFirstDay() {
		return thirtyFirstDay;
	}

	public void setThirtyFirstDay(String thirtyFirstDay) {
		this.thirtyFirstDay = thirtyFirstDay;
	}

	public String getStartWorkTime() {
		return startWorkTime;
	}

	public void setStartWorkTime(String startWorkTime) {
		this.startWorkTime = startWorkTime;
	}

	public String getEndWorkTime() {
		return endWorkTime;
	}

	public void setEndWorkTime(String endWorkTime) {
		this.endWorkTime = endWorkTime;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public String getMonthId() {
		return monthId;
	}

	public void setMonthId(String monthId) {
		this.monthId = monthId;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getCreatedEmployeeCode() {
		return createdEmployeeCode;
	}

	public void setCreatedEmployeeCode(String createdEmployeeCode) {
		this.createdEmployeeCode = createdEmployeeCode;
	}

	public String getModifiedEmployeeCode() {
		return modifiedEmployeeCode;
	}

	public void setModifiedEmployeeCode(String modifiedEmployeeCode) {
		this.modifiedEmployeeCode = modifiedEmployeeCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Long getSchedulingId() {
		return schedulingId;
	}

	public void setSchedulingId(Long schedulingId) {
		this.schedulingId = schedulingId;
	}

	public boolean isNotFullTime() {
		return this.dutyType == DutyType.HOURLY_WORKER;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public DutyType getDutyType() {
		return dutyType;
	}

	public void setDutyType(DutyType dutyType) {
		this.dutyType = dutyType;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public boolean isHourlyWorker() {
		return hourlyWorker;
	}

	public void setHourlyWorker(boolean hourlyWorker) {
		this.hourlyWorker = hourlyWorker;
	}

	public HashMap getMap() {
		return map;
	}

	public void setMap(HashMap map) {
		this.map = map;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	

	public String getDimissionTime() {
		return dimissionTime;
	}

	public void setDimissionTime(String dimissionTime) {
		this.dimissionTime = dimissionTime;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getCancelFlag() {
		return cancelFlag;
	}

	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}

}