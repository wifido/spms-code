package com.sf.module.driver.domain;

import static com.google.common.collect.Lists.newArrayList;
import static com.sf.module.common.domain.Constants.REST_MARK;
import static com.sf.module.common.util.StringUtil.isBlank;
import static com.sf.module.common.util.StringUtil.isNotBlank;
import static java.lang.Integer.parseInt;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.sf.framework.base.domain.BaseEntity;
import com.sf.framework.core.exception.BizException;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.StringUtil;
import com.sf.module.pushserver.domain.Message;

public class DriverScheduling extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String departmentCode;
	private String employeeCode;
	private String dayOfMonth;
	private String yearMonth;
	private String configureCode;
	private String creator;
	private Date createdTime;
	private String modifier;
	private Date modifiedTime;
	private String areaCode;
	private String employeeName;
	private String deptDesc;
	private Long schedulingType;
	private String workType;
	private Long confirmStatus;
	private Date confirmDate;
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

	private String monday;
	private String tuesday;
	private String wednesday;
	private String thursday;
	private String friday;
	private String saturday;
	private String sunday;

	private int totalRestDays;
	private double monthlyAverageDailyTime;
	private double monthlyAverageDrivingTime;
	private double schedulingAttendanceDuration;
	private double schedulingMonthlyAverageDrivingTime;
	private String schedulingMatchesRate;
	private int maxContinuousDay;
	private double drivingTime;
	
	private String departmentAreaDesc;
	private int sequence;
	private String departmentId;
	private String yearWeek;

	private List<DriveLine> lines;
	private Long applyType;
	private Long status;

	private String departmentOfConfigureCode;
	private String codeOfConfigureCode;
	private String configureType;
	private String confirmDateAsString;
	private String createdTimeStr;
	private String modifiedTimeStr;
	private Long syncState;
	private double averageAttendanceTime;
	private int continuousAttendanceDays;
	
	public double getAverageAttendanceTime() {
		return averageAttendanceTime;
	}

	public void setAverageAttendanceTime(double averageAttendanceTime) {
		this.averageAttendanceTime = averageAttendanceTime;
	}

	public int getContinuousAttendanceDays() {
		return continuousAttendanceDays;
	}

	public void setContinuousAttendanceDays(int continuousAttendanceDays) {
		this.continuousAttendanceDays = continuousAttendanceDays;
	}

	public Long getSyncState() {
		return syncState;
	}

	public void setSyncState(Long syncState) {
		this.syncState = syncState;
	}

	public String getCreatedTimeStr() {
		return createdTimeStr;
	}

	public void setCreatedTimeStr(String createdTimeStr) {
		this.createdTimeStr = createdTimeStr;
	}

	public String getModifiedTimeStr() {
		return modifiedTimeStr;
	}

	public void setModifiedTimeStr(String modifiedTimeStr) {
		this.modifiedTimeStr = modifiedTimeStr;
	}

	public String getConfirmDateAsString() {
    	return confirmDateAsString;
    }

	public void setConfirmDateAsString(String confirmDateAsString) {
    	this.confirmDateAsString = confirmDateAsString;
    }

	public String getConfigureType() {
		return configureType;
	}

	public void setConfigureType(String configureType) {
		this.configureType = configureType;
	}

	public Long getApplyType() {
		return applyType;
	}

	public void setApplyType(Long applyType) {
		this.applyType = applyType;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public List<DriveLine> getLines() {
		return lines;
	}

	public void setLines(List<DriveLine> lines) {
		this.lines = lines;
	}

	public static enum SchedulingType {
		ADVANCE_SCHEDULING, SCHEDULING;
	}

	private String getYears() {
		if (isBlank(yearWeek)) {
			return "";
		}
		return yearWeek.substring(0, 4);
	}

	private String getWeeksOfYear() {
		if (isBlank(yearWeek)) {
			return "";
		}
		return yearWeek.substring(5);
	}

	public Message buildPushMessage(boolean isAdd) {
		String string = isAdd ? "生成" : "修改";
		Message message = new Message();
		message.setMsg(String.format("%s,您【%s】年第【%s】周的排班数据已%s，请确认！", employeeCode, getYears(), getWeeksOfYear(), string));
		message.setCreateDate(new Date());
		message.setUserId(employeeCode);
		message.setStatus(0L);
		return message;
	}

	public void setConfigureCodeWithSpecifyDay(int day, String configureCode) {
		if (StringUtil.isBlank(configureCode))
			return;
		for (Day day1 : Day.values()) {
			if (day == day1.ordinal() + 1) {
				day1.setValue(this, configureCode);
				break;
			}
		}
	}

	public void setConfigureCodeWithSpecifyDayOfWeek(String dayOfMonth, String configureCode) {
		if (StringUtil.isBlank(configureCode))
			return;
		for (Week dayOfWeek : Week.values()) {
			Date day;
			try {
				day = DateUtil.parseDate(dayOfMonth, DateFormatType.yyyyMMdd);
				DateTime date = new DateTime(day);
				if (date.getDayOfWeek() == dayOfWeek.ordinal() + 1) {
					dayOfWeek.setValue(this, configureCode);
					break;
				}
			} catch (ParseException e) {
				throw new BizException("日期格式错误！");
			}
		}
	}

	public void updateTotalRestDays() {
		Day[] values = Day.values();
		Iterable<Day> filter = Iterables.filter(Arrays.asList(values), new Predicate<Day>() {
			@Override
			public boolean apply(Day day) {
				String configureCode1 = day.getConfigureCode(DriverScheduling.this);
				return day.isRest(configureCode1);
			}
		});

		this.totalRestDays = newArrayList(filter).size();
	}
	
	public void updateCalculateContinuousAttendanceDay() {
		int maxContinuousDay = 0;
		int continuousDay = 0;
		
		Day[] values = DriverScheduling.Day.values();
		
		for (Day day : values) {
			String configureCode = day.getConfigureCode(this);
			
			if (configureCode == null || configureCode.equals(REST_MARK) || configureCode.equals("请假")) {
				continuousDay = 0;
				continue;
			}
			
			continuousDay++;
			
			if (continuousDay > maxContinuousDay)
				maxContinuousDay = continuousDay;
		}
		
		this.maxContinuousDay = maxContinuousDay;
	}
	
	public void updateSchedulingAttendanceDuration(List<DriveLine> list)
			throws ParseException {
		Date startTime = null;
		Date endTime = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmm");

		String dayOfMonth = "2015-01-01 ";
		
		for (int i = 0; i < list.size(); i++) {
			DriveLine currentLine = list.get(i);
			if (i == 0) {
				startTime = sdf.parse(dayOfMonth + currentLine.getStartTime());
			}
			
			if (list.size() == 1) {
				endTime = getEndTime("0000", list.get(0)
						.getStartTime(), list.get(0).getEndTime(), endTime);
			}
			
			if (i == list.size() - 1)
				continue;
			
			if (parseInt(currentLine.getStartTime()) >= parseInt(currentLine.getEndTime())) {
				endTime = getEndTime(currentLine.getStartTime(), currentLine.getStartTime(), currentLine.getEndTime(), endTime);
			}
			
			DriveLine nextLine = list.get(i + 1);
			endTime = getEndTime(currentLine.getEndTime(), nextLine.getStartTime(), nextLine.getEndTime(), endTime);
		}
		
		this.schedulingAttendanceDuration += ((endTime.getTime() - startTime.getTime())/1000)/60/60.0;
	}
	
	public void updateDrivingTime(List<DriveLine> lines) {
		String dayOfMonth = "2015-01-01 ";
		
		for (DriveLine driveLine : lines) {
			String startTime = driveLine.getStartTime();
			String endTime = driveLine.getEndTime();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HHmm");
			
			try {
				Date startDate = dateFormat.parse(dayOfMonth +startTime);
				Date endDate = getEndTime("0000", startTime, endTime, null);
				
				this.drivingTime += ((endDate.getTime() - startDate.getTime())/1000)/60/60.0;
			} catch (ParseException e) {
				throw new BizException("日期跳格式错误(yyyy-MM-dd HHmm)" + dayOfMonth +startTime);
			}
		}
	}
	
	public void updateSchedulingMonthlyAverageDrivingTime(List<DriveLine> lines) {
		String dayOfMonth = "2015-01-01 ";
		
		for (DriveLine driveLine : lines) {
			String startTime = driveLine.getStartTime();
			String endTime = driveLine.getEndTime();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HHmm");
			
			try {
				Date startDate = dateFormat.parse(dayOfMonth +startTime);
				Date endDate = getEndTime("0000", startTime, endTime, null);
				
				this.schedulingMonthlyAverageDrivingTime += ((endDate.getTime() - startDate.getTime())/1000)/60/60.0;
			} catch (ParseException e) {
				throw new BizException("日期跳格式错误(yyyy-MM-dd HHmm)" + dayOfMonth +startTime);
			}
		}
	}
	
	private static Date getEndTime(String currentEndTime, String nextStartTime, String nextEndTime, Date endTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmm");
		String dayOfMonth = "2015-01-01 ";
		
		if (endTime != null)
			dayOfMonth = new SimpleDateFormat("yyyy-MM-dd ").format(endTime);
		
		if (parseInt(nextStartTime) >= parseInt(currentEndTime)
				&& parseInt(nextStartTime) < parseInt(nextEndTime)) {
			return sdf.parse(dayOfMonth + nextEndTime);
		}
		
		endTime = sdf.parse(dayOfMonth + nextEndTime);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		
		calendar.add(Calendar.DATE, 1);
		
		return sdf.parse(sdf.format(calendar.getTime()));
	}
	
	public void updateDrivingDate(List<DrivingLogData> driveDatas) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		for (DrivingLogData driveData : driveDatas) {
			List<DrivingTime> driveTimes = driveData.getDriveTimes();
			
			if (driveTimes.size() == 0) {
				continue;
			}
			
			driveData.setStartTime(driveTimes.get(0).getStartTime());
			driveData.setEndTime(driveTimes.get(driveTimes.size() - 1)
					.getEndTime());
			
			System.out.println("第一条开始时间:" + driveData.getStartTime() + "----最后一条结束时间:" + driveData.getEndTime());
			
			for (int i = 0; i < driveTimes.size(); i++) {
				Date startTm = format.parse(driveTimes.get(i).getStartTime());
				Date endTm = format.parse(driveTimes.get(i).getEndTime());
				
				driveTimes.get(i).setSingleTotalTime((endTm.getTime() - startTm.getTime())/1000/60/60.0);
				
				System.out.println("时间戳 -----" +driveTimes.get(i).singleTotalTime);
			}
			
			// 更新驾驶时长 
			driveData.updateDriveTime();
			
			// 更新平均出勤时长 
			driveData.updateAverageTime();
			
			System.out.println("驾驶总时长" + driveData.getMonthAverageDriveTime()*60.0);
			
			System.out.println("平均出勤总时长" + driveData.getMonthAverageTime()*60.0);
			
		}
		
		for (DrivingLogData logData : driveDatas) {
			this.monthlyAverageDrivingTime += logData.monthAverageDriveTime;
			this.monthlyAverageDailyTime += logData.monthAverageTime;
		}
	}
	
	public List<DrivingLogData> convertMapToListObject(List<Map<String, Object>> list) {
		HashMap<String, DrivingLogData> objMap = new HashMap<String, DrivingLogData>();

		DrivingLogData driveData;

		for (Map<String, Object> map : list) {
			String employeeCode = (String) map.get("DRIVE_MEMBER");
			String startPlace = (String) map.get("START_PLACE");
			String endPlace = (String) map.get("END_PLACE");
			String startTime = (String) map.get("START_TM");
			String endTime = (String) map.get("END_TM");

			String yearMonthDay = startTime.substring(0, 10);

			Object object = objMap.get(yearMonthDay);

			if (object == null) {
				driveData = new DrivingLogData(employeeCode, startPlace, endPlace, startTime);
				driveData.getDriveTimes().add(new DrivingTime(startTime, endTime, startPlace, endPlace, driveData.getDriveTimes().size()));

				objMap.put(yearMonthDay, driveData);

				continue;
			}

			driveData = (DrivingLogData) object;
			driveData.jointEndPlace(endPlace);
			driveData.getDriveTimes().add(
					new DrivingTime(startTime, endTime, startPlace, endPlace, driveData.getDriveTimes().size()));
		}
		
		return covertMapToList(objMap);
	}
	
	private static List<DrivingLogData> covertMapToList(
			HashMap<String, DrivingLogData> objMap) {
		List<DrivingLogData> datas = new ArrayList<DrivingLogData>();

		for (Map.Entry<String, DrivingLogData> entry : objMap.entrySet()) {
			datas.add(entry.getValue());
		}
		return datas;
	}
	
	public static class DrivingLogData {
		private String employeeCode;
		private String startTime;
		private String endTime;
		private String startPlace;
		private String endPlace;
		private String dayOfMonth;
		private String jointDepartment;
		
		private double monthAverageTime;
		private double monthAverageDriveTime;
		
		private List<DrivingTime> DriveTimes = new ArrayList<DrivingTime>();
		
		public DrivingLogData(String employeeCode, String startPlace, String endPlace, String startTime) {
			this.employeeCode = employeeCode;
			this.startPlace = startPlace;
			this.endPlace = endPlace;
			this.jointDepartment = startPlace + "-" + endPlace;
			this.dayOfMonth = startTime.substring(0, 10).replace("-", "");
		}
		
		public void jointEndPlace(String endPlace) {
			this.jointDepartment += "-" + endPlace;
		}
		
		public void updateDriveTime() {
			for (DrivingTime driveTime : DriveTimes) {
				this.monthAverageDriveTime += driveTime.getSingleTotalTime();
			}
		}
		
		public void updateAverageTime() throws ParseException {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			this.monthAverageTime = (format.parse(endTime).getTime() - format.parse(startTime).getTime())/1000/60/60.0;
		}
		
		public String getEmployeeCode() {
			return employeeCode;
		}
		
		public void setEmployeeCode(String employeeCode) {
			this.employeeCode = employeeCode;
		}
		
		public String getStartTime() {
			return startTime;
		}
		
		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}
		
		public String getEndTime() {
			return endTime;
		}
		
		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}
		
		public String getStartPlace() {
			return startPlace;
		}
		
		public void setStartPlace(String startPlace) {
			this.startPlace = startPlace;
		}
		
		public String getEndPlace() {
			return endPlace;
		}
		
		public void setEndPlace(String endPlace) {
			this.endPlace = endPlace;
		}

		public List<DrivingTime> getDriveTimes() {
			return DriveTimes;
		}

		public void setDriveTimes(List<DrivingTime> driveTimes) {
			DriveTimes = driveTimes;
		}

		public double getMonthAverageTime() {
			return monthAverageTime;
		}

		public void setMonthAverageTime(double monthAverageTime) {
			this.monthAverageTime = monthAverageTime;
		}

		public double getMonthAverageDriveTime() {
			return monthAverageDriveTime;
		}

		public void setMonthAverageDriveTime(double monthAverageDriveTime) {
			this.monthAverageDriveTime = monthAverageDriveTime;
		}

		public String getDayOfMonth() {
			return dayOfMonth;
		}

		public void setDayOfMonth(String dayOfMonth) {
			this.dayOfMonth = dayOfMonth;
		}

		public String getJointDepartment() {
			return jointDepartment;
		}

		public void setJointDepartment(String jointDepartment) {
			this.jointDepartment = jointDepartment;
		}
	}
	
	public static class DrivingTime {
		private String startTime;
		private String endTime;
		private String startPlace;
		private String endPlace;
		private double singleTotalTime;
		private int order;
		
		public DrivingTime(String startTime, String endTime, String startPlace, String endPlace, int order) {
			this.startTime = startTime;
			this.endTime = endTime;
			this.startPlace = startPlace;
			this.endPlace = endPlace;
			this.order = order;
		}
		
		public String getStartTime() {
			return startTime;
		}
		
		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}
		
		public String getEndTime() {
			return endTime;
		}
		
		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public double getSingleTotalTime() {
			return singleTotalTime;
		}

		public void setSingleTotalTime(double singleTotalTime) {
			this.singleTotalTime = singleTotalTime;
		}

		public int getOrder() {
			return order;
		}

		public void setOrder(int order) {
			this.order = order;
		}

		public String getStartPlace() {
			return startPlace;
		}

		public void setStartPlace(String startPlace) {
			this.startPlace = startPlace;
		}

		public String getEndPlace() {
			return endPlace;
		}

		public void setEndPlace(String endPlace) {
			this.endPlace = endPlace;
		}
	}

	public static enum Week {
		monday {
			public void setValue(DriverScheduling driverScheduling, String configureCode) {
				driverScheduling.setMonday(configureCode);
			}
		},
		tuesday {
			public void setValue(DriverScheduling driverScheduling, String configureCode) {
				driverScheduling.setTuesday(configureCode);
			};
		},
		wednesday {
			public void setValue(DriverScheduling driverScheduling, String configureCode) {
				driverScheduling.setWednesday(configureCode);
			}
		},
		thursday {
			public void setValue(DriverScheduling driverScheduling, String configureCode) {
				driverScheduling.setThursday(configureCode);
			}
		},
		friday {
			public void setValue(DriverScheduling driverScheduling, String configureCode) {
				driverScheduling.setFriday(configureCode);
			}
		},
		saturday {
			public void setValue(DriverScheduling driverScheduling, String configureCode) {
				driverScheduling.setSaturday(configureCode);
			}
		},
		sunday {
			public void setValue(DriverScheduling driverScheduling, String configureCode) {
				driverScheduling.setSunday(configureCode);
			}
		};
		public abstract void setValue(DriverScheduling driverScheduling, String configureCode);
	}

	public static enum Day {
		firstDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setFirstDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.firstDay;
			}
		},
		secondDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setSecondDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.secondDay;
			}
		},
		thirdDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setThirdDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.thirdDay;
			}
		},
		fourthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setFourthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.fourthDay;
			}
		},
		fifthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setFifthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.fifthDay;
			}
		},
		sixthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setSixthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.sixthDay;
			}
		},
		seventhDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setSeventhDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.seventhDay;
			}
		},
		eighthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setEighthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.eighthDay;
			}
		},
		ninthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setNinthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.ninthDay;
			}
		},
		tenthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setTenthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.tenthDay;
			}
		},
		eleventhDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setEleventhDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.eleventhDay;
			}
		},
		twelfthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setTwelfthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.twelfthDay;
			}
		},
		thirteenthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setThirteenthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.thirteenthDay;
			}
		},
		fourteenthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setFourteenthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.fourteenthDay;
			}
		},
		fifteenthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setFifteenthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.fifteenthDay;
			}
		},
		sixteenthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setSixteenthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.sixteenthDay;
			}
		},
		seventeenthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setSeventeenthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.seventeenthDay;
			}
		},
		eighteenthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setEighteenthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.eighteenthDay;
			}
		},
		nineteenthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setNineteenthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.nineteenthDay;
			}
		},
		twentiethDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setTwentiethDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.twentiethDay;
			}
		},
		twentyFirstDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setTwentyFirstDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.twentyFirstDay;
			}
		},
		twentySecondDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setTwentySecondDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.twentySecondDay;
			}
		},
		twentyThirdDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setTwentyThirdDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.twentyThirdDay;
			}
		},
		twentyFourthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setTwentyFourthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.twentyFourthDay;
			}
		},
		twentyFifthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setTwentyFifthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.twentyFifthDay;
			}
		},
		twentySixthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setTwentySixthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.twentySixthDay;
			}
		},
		twentySeventhDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setTwentySeventhDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.twentySeventhDay;
			}
		},
		twentyEighthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setTwentyEighthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.twentyEighthDay;
			}
		},
		twentyNinthDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setTwentyNinthDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.twentyNinthDay;
			}
		},
		thirtiethDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setThirtiethDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.thirtiethDay;
			}
		},
		thirtyFirstDay {
			@Override
			public void setValue(DriverScheduling driverScheduling, String scheduling) {
				driverScheduling.setThirtyFirstDay(scheduling);
			}

			@Override
			public String getConfigureCode(DriverScheduling driverScheduling) {
				return driverScheduling.thirtyFirstDay;
			}
		};

		public abstract void setValue(DriverScheduling driverScheduling, String configureCode);

		public abstract String getConfigureCode(DriverScheduling driverScheduling);

		private boolean isRest(String configureCode) {
			return REST_MARK.equals(configureCode);
		}
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getDepartmentAreaDesc() {
		return departmentAreaDesc;
	}

	public void setDepartmentAreaDesc(String departmentAreaDesc) {
		this.departmentAreaDesc = departmentAreaDesc;
	}

	public String getDeptDesc() {
		return deptDesc;
	}

	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
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

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public String getConfigureCode() {
		return configureCode;
	}

	public void setConfigureCode(String configureCode) {
		this.configureCode = configureCode;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public int getTotalRestDays() {
		return totalRestDays;
	}

	public void setTotalRestDays(int totalRestDays) {
		this.totalRestDays = totalRestDays;
	}

	public Long getSchedulingType() {
		return schedulingType;
	}

	public void setSchedulingType(Long schedulingType) {
		this.schedulingType = schedulingType;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getMonday() {
		return monday;
	}

	public void setMonday(String monday) {
		this.monday = monday;
	}

	public String getTuesday() {
		return tuesday;
	}

	public void setTuesday(String tuesday) {
		this.tuesday = tuesday;
	}

	public String getWednesday() {
		return wednesday;
	}

	public void setWednesday(String wednesday) {
		this.wednesday = wednesday;
	}

	public String getThursday() {
		return thursday;
	}

	public void setThursday(String thursday) {
		this.thursday = thursday;
	}

	public String getFriday() {
		return friday;
	}

	public void setFriday(String friday) {
		this.friday = friday;
	}

	public String getSaturday() {
		return saturday;
	}

	public void setSaturday(String saturday) {
		this.saturday = saturday;
	}

	public String getSunday() {
		return sunday;
	}

	public void setSunday(String sunday) {
		this.sunday = sunday;
	}

	public String getYearWeek() {
		return yearWeek;
	}

	public void setYearWeek(String yearWeek) {
		this.yearWeek = yearWeek;
	}

	public Long getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(Long confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getDepartmentOfConfigureCode() {
		if (isNotBlank(configureCode) && configureCode.contains("-")) {
			return configureCode.split("-")[0];
		}
		return departmentOfConfigureCode;
	}

	public void setDepartmentOfConfigureCode(String departmentOfConfigureCode) {
		this.departmentOfConfigureCode = departmentOfConfigureCode;
	}

	public String getCodeOfConfigureCode() {
		if (isNotBlank(configureCode) && configureCode.contains("-")) {
			return configureCode.split("-")[1];
		}
		return codeOfConfigureCode;
	}

	public void setCodeOfConfigureCode(String codeOfConfigureCode) {
		this.codeOfConfigureCode = codeOfConfigureCode;
	}

	public double getMonthlyAverageDailyTime() {
		return monthlyAverageDailyTime;
	}

	public void setMonthlyAverageDailyTime(double monthlyAverageDailyTime) {
		this.monthlyAverageDailyTime = monthlyAverageDailyTime;
	}

	public int getMaxContinuousDay() {
		return maxContinuousDay;
	}

	public void setMaxContinuousDay(int maxContinuousDay) {
		this.maxContinuousDay = maxContinuousDay;
	}

	public double getSchedulingAttendanceDuration() {
		return schedulingAttendanceDuration;
	}

	public void setSchedulingAttendanceDuration(double schedulingAttendanceDuration) {
		this.schedulingAttendanceDuration = schedulingAttendanceDuration;
	}

	public double getDrivingTime() {
		return drivingTime;
	}

	public void setDrivingTime(double drivingTime) {
		this.drivingTime = drivingTime;
	}

	public double getMonthlyAverageDrivingTime() {
		return monthlyAverageDrivingTime;
	}

	public void setMonthlyAverageDrivingTime(double monthlyAverageDrivingTime) {
		this.monthlyAverageDrivingTime = monthlyAverageDrivingTime;
	}

	public double getSchedulingMonthlyAverageDrivingTime() {
		return schedulingMonthlyAverageDrivingTime;
	}

	public void setSchedulingMonthlyAverageDrivingTime(
			double schedulingMonthlyAverageDrivingTime) {
		this.schedulingMonthlyAverageDrivingTime = schedulingMonthlyAverageDrivingTime;
	}

	public String getSchedulingMatchesRate() {
		return schedulingMatchesRate;
	}

	public void setSchedulingMatchesRate(String schedulingMatchesRate) {
		this.schedulingMatchesRate = schedulingMatchesRate;
	}
}
