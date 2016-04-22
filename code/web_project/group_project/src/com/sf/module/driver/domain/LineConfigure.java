package com.sf.module.driver.domain;

import static java.lang.Integer.parseInt;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.sf.framework.base.domain.BaseEntity;
import com.sf.framework.core.exception.BizException;

public class LineConfigure extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String code;
	private Long id;
	private Long departMentId;
	private Long validStatus;
	private Date createTime;
	private String modifier;
	private Date modifiedTime;
	private Date createdTime;
	private String creator;
	private String departmentCode;
	private Long type;
	private String yearMonth;
	private double attendanceDuration;
	private double driveDuration;

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDepartMentId() {
		return departMentId;
	}

	public void setDepartMentId(Long departMentId) {
		this.departMentId = departMentId;
	}

	public Long getValidStatus() {
		return validStatus;
	}

	public void setValidStatus(Long validStatus) {
		this.validStatus = validStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public double getAttendanceDuration() {
		return attendanceDuration;
	}

	public void setAttendanceDuration(double attendanceDuration) {
		this.attendanceDuration = attendanceDuration;
	}

	public double getDriveDuration() {
		return driveDuration;
	}

	public void setDriveDuration(double driveDuration) {
		this.driveDuration = driveDuration;
	}
	
	public static double getTimeDifference(String startTimeStr, String endTimeStr) {
		Date endTime = getWhenAcrossDayPulsOne(startTimeStr, endTimeStr);
		
		String dayOfMonth = "2015-01-01 ";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HHmm");
		
		try {
			return ((endTime.getTime() - dateFormat.parse(dayOfMonth + startTimeStr).getTime()) / 1000) / 60 / 60.0;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public static void main(String[] args) {
		double a = getTimeDifference("1000", "0900");
		
		System.out.println(a);
	}

	public void setLineConfigureInformation(List<DriveLine> driveLines) {
		double attendanceDuration = 0;
		double driveDuration = 0;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HHmm");
		
		String dayOfMonth = "2015-01-01 ";
		Date lastTime = null;
		
		try {
			for (int i = 0; i < driveLines.size(); i++) {
				String startTime = driveLines.get(i).getStartTime();
				String endTime = driveLines.get(i).getEndTime();

				Date startDate = dateFormat.parse(dayOfMonth + startTime);
				Date endDate = getWhenAcrossDayPulsOne(startTime, endTime);

				lastTime = getEndTime(startTime, endTime, lastTime);

				driveDuration += ((endDate.getTime() - startDate.getTime()) / 1000) / 60 / 60.0;

			}
			attendanceDuration = ((lastTime.getTime() - dateFormat.parse(
					dayOfMonth + driveLines.get(0).getStartTime()).getTime()) / 1000) / 60 / 60.0;
		} catch (ParseException e) {
			throw new BizException("日期跳格式错误");
		}

		this.driveDuration = Double.parseDouble(new DecimalFormat(".####").format(driveDuration));
		this.attendanceDuration = Double.parseDouble(new DecimalFormat(".####").format(attendanceDuration));
	}
	
	public static Date getWhenAcrossDayPulsOne(String startTimeStr, String endTimeStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmm");
		String dayOfMonth = "2015-01-01 ";
		
		try {
			if (parseInt(endTimeStr) >= parseInt(startTimeStr)) {
				return sdf.parse(dayOfMonth + endTimeStr);
			}

			Date endTime = sdf.parse(dayOfMonth + endTimeStr);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);

			calendar.add(Calendar.DATE, 1);

			return sdf.parse(sdf.format(calendar.getTime()));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Date getEndTime(String curretnStartTime, String currentEndTime, Date endTime) throws ParseException {
		// 格式化年月日
		SimpleDateFormat formatYearMonthDay = new SimpleDateFormat("yyyy-MM-dd HHmm");
		
		// 默认为1 号，当出现多条数据对比时，根据(endTime)最后结束时间取值
		String dayOfMonth = "2015-01-01 ";
		
		// 当只有一条数据对比时，默认为0000; 否则根据endTime取值(时分)
		String beforeEndTime = "0000";
		
		// 当出现多条数据对比时
		if (endTime != null) {
			dayOfMonth = new SimpleDateFormat("yyyy-MM-dd ").format(endTime);
			beforeEndTime = new SimpleDateFormat("HHmm").format(endTime);
		}
		
		// 当不存在跨天时
		if (parseInt(currentEndTime) >= parseInt(curretnStartTime) &&
				parseInt(beforeEndTime) <= parseInt(curretnStartTime)) {
			return formatYearMonthDay.parse(dayOfMonth + currentEndTime);
		}
		
		// 否则时间加一天
		endTime = formatYearMonthDay.parse(dayOfMonth + currentEndTime);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		
		calendar.add(Calendar.DATE, 1);
		
		return formatYearMonthDay.parse(formatYearMonthDay.format(calendar.getTime()));
	}
}
