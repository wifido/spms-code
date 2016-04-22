package com.sf.module.driver.domain;

import static com.google.common.collect.Maps.newHashMap;
import static com.sf.module.common.domain.Constants.REST_MARK;
import static com.sf.module.common.util.StringUtil.*;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.joda.time.DateTime;
import com.sf.module.common.domain.ImportModel;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.StringUtil;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.organization.domain.Department;

public class DriverSchedulingImportModel extends ImportModel {
	private String employeeCode;
	private String departmentCode;
	private String areaCode;
	private String month;// 导入日期 字符串格式2014-12-01
	private Date monthAsDate;// 导入日期准成日期格式
	private String day01;
	private String day02;
	private String day03;
	private String day04;
	private String day05;
	private String day06;
	private String day07;
	private String day08;
	private String day09;
	private String day10;
	private String day11;
	private String day12;
	private String day13;
	private String day14;
	private String day15;
	private String day16;
	private String day17;
	private String day18;
	private String day19;
	private String day20;
	private String day21;
	private String day22;
	private String day23;
	private String day24;
	private String day25;
	private String day26;
	private String day27;
	private String day28;
	private String day29;
	private String day30;
	private String day31;
	private String workType;

	private String monday;
	private String tuesday;
	private String wednesday;
	private String thursday;
	private String friday;
	private String saturday;
	private String sunday;

	public final Map<String, Set<String>> configureCodeOfDays = newHashMap();

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
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

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) throws ParseException {
		if (null != month) {
			monthAsDate = DateUtil.parseDate(month, DateFormatType.yyyy_MM_dd);
		}
		this.month = month;
	}

	public Date getMonthAsDate() {
		return monthAsDate;
	}

	public void setMonthAsDate(Date monthAsDate) {
		this.monthAsDate = monthAsDate;
	}

	public String getDay01() {
		return day01;
	}

	public void setDay01(String day01) {
		this.day01 = day01;
	}

	public String getDay02() {
		return day02;
	}

	public void setDay02(String day02) {
		this.day02 = day02;
	}

	public String getDay03() {
		return day03;
	}

	public void setDay03(String day03) {
		this.day03 = day03;
	}

	public String getDay04() {
		return day04;
	}

	public void setDay04(String day04) {
		this.day04 = day04;
	}

	public String getDay05() {
		return day05;
	}

	public void setDay05(String day05) {
		this.day05 = day05;
	}

	public String getDay06() {
		return day06;
	}

	public void setDay06(String day06) {
		this.day06 = day06;
	}

	public String getDay07() {
		return day07;
	}

	public void setDay07(String day07) {
		this.day07 = day07;
	}

	public String getDay08() {
		return day08;
	}

	public void setDay08(String day08) {
		this.day08 = day08;
	}

	public String getDay09() {
		return day09;
	}

	public void setDay09(String day09) {
		this.day09 = day09;
	}

	public String getDay10() {
		return day10;
	}

	public void setDay10(String day10) {
		this.day10 = day10;
	}

	public String getDay11() {
		return day11;
	}

	public void setDay11(String day11) {
		this.day11 = day11;
	}

	public String getDay12() {
		return day12;
	}

	public void setDay12(String day12) {
		this.day12 = day12;
	}

	public String getDay13() {
		return day13;
	}

	public void setDay13(String day13) {
		this.day13 = day13;
	}

	public String getDay14() {
		return day14;
	}

	public void setDay14(String day14) {
		this.day14 = day14;
	}

	public String getDay15() {
		return day15;
	}

	public void setDay15(String day15) {
		this.day15 = day15;
	}

	public String getDay16() {
		return day16;
	}

	public void setDay16(String day16) {
		this.day16 = day16;
	}

	public String getDay17() {
		return day17;
	}

	public void setDay17(String day17) {
		this.day17 = day17;
	}

	public String getDay18() {
		return day18;
	}

	public void setDay18(String day18) {
		this.day18 = day18;
	}

	public String getDay19() {
		return day19;
	}

	public void setDay19(String day19) {
		this.day19 = day19;
	}

	public String getDay20() {
		return day20;
	}

	public void setDay20(String day20) {
		this.day20 = day20;
	}

	public String getDay21() {
		return day21;
	}

	public void setDay21(String day21) {
		this.day21 = day21;
	}

	public String getDay22() {
		return day22;
	}

	public void setDay22(String day22) {
		this.day22 = day22;
	}

	public String getDay23() {
		return day23;
	}

	public void setDay23(String day23) {
		this.day23 = day23;
	}

	public String getDay24() {
		return day24;
	}

	public void setDay24(String day24) {
		this.day24 = day24;
	}

	public String getDay25() {
		return day25;
	}

	public void setDay25(String day25) {
		this.day25 = day25;
	}

	public String getDay26() {
		return day26;
	}

	public void setDay26(String day26) {
		this.day26 = day26;
	}

	public String getDay27() {
		return day27;
	}

	public void setDay27(String day27) {
		this.day27 = day27;
	}

	public String getDay28() {
		return day28;
	}

	public void setDay28(String day28) {
		this.day28 = day28;
	}

	public String getDay29() {
		return day29;
	}

	public void setDay29(String day29) {
		this.day29 = day29;
	}

	public String getDay30() {
		return day30;
	}

	public void setDay30(String day30) {
		this.day30 = day30;
	}

	public String getDay31() {
		return day31;
	}

	public void setDay31(String day31) {
		this.day31 = day31;
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

	public void validImportDataWhileFailureSetErrorMsg() {
		validEmployeeNotEmpty();

		validConfigureCode();
	}

	private void validEmployeeNotEmpty() {
		if (isBlank(employeeCode)) {
			appendErrorMsg("驾驶员工号不能为空!");
		}
	}

	private void validConfigureCode() {
		for (Weeks weeks : Weeks.values()) {
			weeks.validFailure(this);
		}
	}

	public void setConfigureCodeOfDays() {
		DateTime importDate = new DateTime(monthAsDate);
		for (Weeks weeks : Weeks.values()) {
			String configureCode = weeks.getValue(this);
			DateTime dateTime = new DateTime()
			        .withYear(importDate.getYear())
			        .withWeekOfWeekyear(importDate.getWeekOfWeekyear())
			        .withDayOfWeek(weeks.ordinal() + 1);
			String yearMonth = DateUtil.formatDate(dateTime.toDate(), DateFormatType.yyyy_MM);
			if (configureCodeOfDays.containsKey(yearMonth)) {
				configureCodeOfDays.get(yearMonth).add(configureCode);
				continue;
			}
			Set<String> lineConfigureCodes = new HashSet<String>();
			lineConfigureCodes.add(configureCode);
			configureCodeOfDays.put(yearMonth, lineConfigureCodes);
		}
	}

	public static enum Weeks {
		monday {
			@Override
			public String getValue(DriverSchedulingImportModel driverSchedulingImportModel) {
				return driverSchedulingImportModel.getMonday();
			}

			@Override
			public boolean validFailure(DriverSchedulingImportModel driverSchedulingImportModel) {
				return setErrorMsg(driverSchedulingImportModel, driverSchedulingImportModel.getMonday());
			}
		},
		tuesday {
			@Override
			public String getValue(DriverSchedulingImportModel driverSchedulingImportModel) {
				return driverSchedulingImportModel.getTuesday();
			}

			@Override
			public boolean validFailure(DriverSchedulingImportModel driverSchedulingImportModel) {
				return setErrorMsg(driverSchedulingImportModel, driverSchedulingImportModel.getTuesday());
			}
		},
		wednesday {
			@Override
			public String getValue(DriverSchedulingImportModel driverSchedulingImportModel) {
				return driverSchedulingImportModel.getWednesday();
			}

			@Override
			public boolean validFailure(DriverSchedulingImportModel driverSchedulingImportModel) {
				return setErrorMsg(driverSchedulingImportModel, driverSchedulingImportModel.getWednesday());
			}
		},
		thursday {
			@Override
			public String getValue(DriverSchedulingImportModel driverSchedulingImportModel) {
				return driverSchedulingImportModel.getThursday();
			}

			@Override
			public boolean validFailure(DriverSchedulingImportModel driverSchedulingImportModel) {
				return setErrorMsg(driverSchedulingImportModel, driverSchedulingImportModel.getThursday());
			}
		},
		friday {
			@Override
			public String getValue(DriverSchedulingImportModel driverSchedulingImportModel) {
				return driverSchedulingImportModel.getFriday();
			}

			@Override
			public boolean validFailure(DriverSchedulingImportModel driverSchedulingImportModel) {
				return setErrorMsg(driverSchedulingImportModel, driverSchedulingImportModel.getFriday());
			}
		},
		saturday {
			@Override
			public String getValue(DriverSchedulingImportModel driverSchedulingImportModel) {
				return driverSchedulingImportModel.getSaturday();
			}

			@Override
			public boolean validFailure(DriverSchedulingImportModel driverSchedulingImportModel) {
				return setErrorMsg(driverSchedulingImportModel, driverSchedulingImportModel.getSaturday());
			}
		},
		sunday {
			@Override
			public String getValue(DriverSchedulingImportModel driverSchedulingImportModel) {
				return driverSchedulingImportModel.getSunday();
			}

			@Override
			public boolean validFailure(DriverSchedulingImportModel driverSchedulingImportModel) {
				return setErrorMsg(driverSchedulingImportModel, driverSchedulingImportModel.getSunday());
			}
		};

		public abstract String getValue(DriverSchedulingImportModel driverSchedulingImportModel);

		public abstract boolean validFailure(DriverSchedulingImportModel driverSchedulingImportModel);

		public boolean setErrorMsg(DriverSchedulingImportModel driverSchedulingImportModel, String configureCode) {
			if (StringUtil.isBlank(configureCode)) {
				driverSchedulingImportModel.appendErrorMsg("配班代码不能为空！");
				return true;
			}
			if (REST_MARK.equals(configureCode)) {
				return true;
			}

			if (!configureCode.matches("\\w{1,7}-\\d{1,8}")) {
				driverSchedulingImportModel.appendErrorMsg("配班代码格式不正确！正确格式如：755W-00001");
				return true;
			}

			String departmentCode = configureCode.split("-")[0];
			Department department = DepartmentCacheBiz.getDepartmentByCode(departmentCode);
			if (null == department) {
				driverSchedulingImportModel.appendErrorMsg("配班代码的网点不存在！");
				return true;
			}
			if (isNotBlank(driverSchedulingImportModel.areaCode) && !department.getAreaDeptCode().equals(driverSchedulingImportModel.areaCode)) {
				driverSchedulingImportModel.appendErrorMsg(configureCode + " 不是本区部的配班代码！");
				return true;
			}

			return false;
		}
	}
}
