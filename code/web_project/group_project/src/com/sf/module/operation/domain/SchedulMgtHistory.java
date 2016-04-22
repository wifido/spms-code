package com.sf.module.operation.domain;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import com.sf.framework.base.domain.BaseEntity;
import com.sf.framework.core.exception.BizException;
import com.sf.module.common.util.DateFormatType;

public class SchedulMgtHistory extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private String ym;

	private Long deptId;

	private String deptCode;

	private String areaCode;

	private String empCode;

	private String empName;

	private int workType;

	private String workTypeStr;

	private String dimissionDt;

	private String errorDesc;

	private String day1;

	private String day2;

	private String day3;

	private String day4;

	private String day5;

	private String day6;

	private String day7;

	private String day8;

	private String day9;

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

	private Date createTm;

	private Date modifiedTm;

	private String createEmpCode;

	private String modifiedEmpCode;

	private Integer version;

	private Integer coincideuNum = 0;// 跟上个版本排版吻合数量

	private Integer effectiveNum = 0;// 有效的排班天数

	public Integer getCoincideuNum() {
		return coincideuNum;
	}

	public void setCoincideuNum(Integer coincideuNum) {
		this.coincideuNum = coincideuNum;
	}

	public Integer getEffectiveNum() {
		return effectiveNum;
	}

	public void setEffectiveNum(Integer effectiveNum) {
		this.effectiveNum = effectiveNum;
	}

	public String getYm() {
		return ym;
	}

	public void setYm(String ym) {
		this.ym = ym;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public int getWorkType() {
		return workType;
	}

	public void setWorkType(int workType) {
		this.workType = workType;
	}

	public String getWorkTypeStr() {
		return workTypeStr;
	}

	public void setWorkTypeStr(String workTypeStr) {
		this.workTypeStr = workTypeStr;
	}

	public String getDimissionDt() {
		return dimissionDt;
	}

	public void setDimissionDt(String dimissionDt) {
		this.dimissionDt = dimissionDt;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public String getDay1() {
		return day1;
	}

	public void setDay1(String day1) {
		this.day1 = day1;
	}

	public String getDay2() {
		return day2;
	}

	public void setDay2(String day2) {
		this.day2 = day2;
	}

	public String getDay3() {
		return day3;
	}

	public void setDay3(String day3) {
		this.day3 = day3;
	}

	public String getDay4() {
		return day4;
	}

	public void setDay4(String day4) {
		this.day4 = day4;
	}

	public String getDay5() {
		return day5;
	}

	public void setDay5(String day5) {
		this.day5 = day5;
	}

	public String getDay6() {
		return day6;
	}

	public void setDay6(String day6) {
		this.day6 = day6;
	}

	public String getDay7() {
		return day7;
	}

	public void setDay7(String day7) {
		this.day7 = day7;
	}

	public String getDay8() {
		return day8;
	}

	public void setDay8(String day8) {
		this.day8 = day8;
	}

	public String getDay9() {
		return day9;
	}

	public void setDay9(String day9) {
		this.day9 = day9;
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

	public Date getCreateTm() {
		return createTm;
	}

	public void setCreateTm(Date createTm) {
		this.createTm = createTm;
	}

	public Date getModifiedTm() {
		return modifiedTm;
	}

	public void setModifiedTm(Date modifiedTm) {
		this.modifiedTm = modifiedTm;
	}

	public String getCreateEmpCode() {
		return createEmpCode;
	}

	public void setCreateEmpCode(String createEmpCode) {
		this.createEmpCode = createEmpCode;
	}

	public String getModifiedEmpCode() {
		return modifiedEmpCode;
	}

	public void setModifiedEmpCode(String modifiedEmpCode) {
		this.modifiedEmpCode = modifiedEmpCode;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	private void countCoincide(SchedulMgtHistory prevScheduling, int startDay, int lastDay) throws IllegalAccessException, InvocationTargetException,
	        NoSuchMethodException {
		for (int i = startDay; i <= lastDay; i++) {
			String propertyName = String.format("day%s", i);
			String oldSchedulingCode = String.valueOf(PropertyUtils.getProperty(prevScheduling, propertyName));
			String newSchedulingCode = String.valueOf(PropertyUtils.getProperty(this, propertyName));
			if (!newSchedulingCode.equals(oldSchedulingCode)) {
				coincideuNum++;
			}
		}
	}

	public void countCoincideNum(OutEmployee employee, SchedulMgtHistory prevScheduling) {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern(DateFormatType.yyyy_MM.format);
			DateTime firstDayOfMonth = DateTime.parse(ym, formatter);
			int minIndexOfSchedulingDay = getMinIndexOfSchedulingDay(firstDayOfMonth, employee);
			int maxIndexOfSchedulingDay = getMaxIndexOfSchedulingDay(firstDayOfMonth, employee);
			setCoincideuNum(0);
			countCoincide(prevScheduling, minIndexOfSchedulingDay, maxIndexOfSchedulingDay);
		} catch (Exception e) {
			throw new BizException(e.getMessage());
		}
	}

	public void countEffectiveNum(OutEmployee employee) {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern(DateFormatType.yyyy_MM.format);
			DateTime firstDayOfMonth = DateTime.parse(ym, formatter);
			DateTime lastDayOfMonth = DateTime.parse(ym, formatter).plusMonths(1).minusDays(1);
			int minIndexOfSchedulingDay = getMinIndexOfSchedulingDay(firstDayOfMonth, employee);
			Date dimissionDt = employee.getDimissionDt();
			setEffectiveNum(0);
			effectiveNum = lastDayOfMonth.getDayOfMonth() + 1 - minIndexOfSchedulingDay;
			if (dimissionDt != null && dimissionDt.before(lastDayOfMonth.toDate())) {
				effectiveNum = new DateTime(dimissionDt).getDayOfMonth() + 1 - minIndexOfSchedulingDay;
			}
		} catch (Exception e) {
			throw new BizException(e.getMessage());
		}
	}

	private int getMinIndexOfSchedulingDay(DateTime firstDayOfThisMonth, OutEmployee employee) {
		Date modifyDate = this.getModifiedTm();
		if (modifyDate == null)
			modifyDate = this.createTm;
		ArrayList<DateTime> dateList = new ArrayList<DateTime>();
		dateList.add(firstDayOfThisMonth);
		Date transformDepartmentDate = employee.getTransformDepartmentDate();
		if (null != transformDepartmentDate && transformDepartmentDate.before(modifyDate)) {
			dateList.add(new DateTime(transformDepartmentDate));
		}
		Date transformPostTypeDate = employee.getTransformPostTypeDate();
		if (null != transformPostTypeDate && transformPostTypeDate.before(modifyDate)) {
			dateList.add(new DateTime(transformPostTypeDate));
		}
		Date sfDate = employee.getSfDate();
		if (null != sfDate && sfDate.before(modifyDate)) {
			dateList.add(new DateTime(sfDate));
		}
		Collections.sort(dateList);
		Collections.reverse(dateList);
		if(dateList.get(0).isBefore(firstDayOfThisMonth)){
			return 0;
		}
		return dateList.get(0).getDayOfMonth();
	}

	private int getMaxIndexOfSchedulingDay(DateTime firstDayOfThisMonth, OutEmployee employee) {
		Date modifyDate = this.getModifiedTm();
		if (modifyDate == null)
			modifyDate = this.createTm;
		if(modifyDate.before(firstDayOfThisMonth.toDate())){
			return 0;
		}
		Date dimissionDt = employee.getDimissionDt();
		int maxDay = new DateTime(modifyDate).getDayOfMonth();
		if (null != dimissionDt && dimissionDt.before(modifyDate)) {
			maxDay = new DateTime(dimissionDt).minusDays(1).getDayOfMonth();
		}
		return maxDay;
	}

}