/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-25     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.domain;

import java.util.Date;

import com.sf.framework.base.domain.BaseEntity;

/**
 *
 * ScheduleMonthRpt处理类
 *
 */
public class ScheduleMonthRpt extends BaseEntity {
	/**
	 * @author 方芳 (350614)
	 * @date 2014-7-3 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//班次类型，1是正常，2是顶班，3是机动
	private String classType;
	private String deptCode;
	private ArrDepartment department;
	//地区名
	private String areaDeptName;
	//地区网点代码
	private String areaDeptCode;
	//年月份
	private String yearMonth;
	//平均工作时长
	private Integer avgWorkingTm;
	//平均驾驶时长
	private Integer avgDrivingTm;
	//计划休息天数
	private Integer planDay;
	//实际休息天数-对应字段：REAL_REST_TM
	private Integer realDay;
	//排班吻合率
	private String rate;
	//驾驶员
	private ArrDriver driver;
	//日期
	private String one;
	private String two;
	private String three;
	private String four;
	private String five;
	private String six;
	private String seven;
	private String eight;
	private String nine;
	private String ten;
	private String eleven;
	private String twelve;
	private String thirteen;
	private String fourteen;
	private String fifteen;
	private String sixteen;
	private String seventeen;
	private String eighteen;
	private String nineteen;
	private String twenty;
	private String twentyOne;
	private String twentyTwo;
	private String twentyThree;
	private String twentyFour;
	private String twentyFive;
	private String twentySix;
	private String twentySeven;
	private String twentyEight;
	private String twentyNine;
	private String thirty;
	private String thirtyOne;
	//创建时间
	private Date createdTm;
	//创建人
	private String createdEmpCode;
	
	public ArrDriver getDriver() {
		return driver;
	}

	public void setDriver(ArrDriver driver) {
		this.driver = driver;
	}

	public String getAreaDeptCode() {
		return areaDeptCode;
	}

	public void setAreaDeptCode(String areaDeptCode) {
		this.areaDeptCode = areaDeptCode;
	}

	public Integer getRealDay() {
		return realDay;
	}

	public void setRealDay(Integer realDay) {
		this.realDay = realDay;
	}

	public ArrDepartment getDepartment() {
		return department;
	}

	public void setDepartment(ArrDepartment department) {
		this.department = department;
	}
	
	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public Integer getPlanDay() {
		return planDay;
	}

	public void setPlanDay(Integer planDay) {
		this.planDay = planDay;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getAreaDeptName() {
		return areaDeptName;
	}
	public void setAreaDeptName(String areaDeptName) {
		this.areaDeptName = areaDeptName;
	}
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public Integer getAvgWorkingTm() {
		return avgWorkingTm;
	}
	public void setAvgWorkingTm(Integer avgWorkingTm) {
		this.avgWorkingTm = avgWorkingTm;
	}
	public Integer getAvgDrivingTm() {
		return avgDrivingTm;
	}
	public void setAvgDrivingTm(Integer avgDrivingTm) {
		this.avgDrivingTm = avgDrivingTm;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getOne() {
		return one;
	}
	public void setOne(String one) {
		this.one = one;
	}
	public String getTwo() {
		return two;
	}
	public void setTwo(String two) {
		this.two = two;
	}
	public String getThree() {
		return three;
	}
	public void setThree(String three) {
		this.three = three;
	}
	public String getFour() {
		return four;
	}
	public void setFour(String four) {
		this.four = four;
	}
	public String getFive() {
		return five;
	}
	public void setFive(String five) {
		this.five = five;
	}
	public String getSix() {
		return six;
	}
	public void setSix(String six) {
		this.six = six;
	}
	public String getSeven() {
		return seven;
	}
	public void setSeven(String seven) {
		this.seven = seven;
	}
	public String getEight() {
		return eight;
	}
	public void setEight(String eight) {
		this.eight = eight;
	}
	public String getNine() {
		return nine;
	}
	public void setNine(String nine) {
		this.nine = nine;
	}
	public String getTen() {
		return ten;
	}
	public void setTen(String ten) {
		this.ten = ten;
	}
	public String getEleven() {
		return eleven;
	}
	public void setEleven(String eleven) {
		this.eleven = eleven;
	}
	public String getTwelve() {
		return twelve;
	}
	public void setTwelve(String twelve) {
		this.twelve = twelve;
	}
	public String getThirteen() {
		return thirteen;
	}
	public void setThirteen(String thirteen) {
		this.thirteen = thirteen;
	}
	public String getFourteen() {
		return fourteen;
	}
	public void setFourteen(String fourteen) {
		this.fourteen = fourteen;
	}
	public String getFifteen() {
		return fifteen;
	}
	public void setFifteen(String fifteen) {
		this.fifteen = fifteen;
	}
	public String getSixteen() {
		return sixteen;
	}
	public void setSixteen(String sixteen) {
		this.sixteen = sixteen;
	}
	public String getSeventeen() {
		return seventeen;
	}
	public void setSeventeen(String seventeen) {
		this.seventeen = seventeen;
	}
	public String getEighteen() {
		return eighteen;
	}
	public void setEighteen(String eighteen) {
		this.eighteen = eighteen;
	}
	public String getNineteen() {
		return nineteen;
	}
	public void setNineteen(String nineteen) {
		this.nineteen = nineteen;
	}
	public String getTwenty() {
		return twenty;
	}
	public void setTwenty(String twenty) {
		this.twenty = twenty;
	}
	
	public String getTwentyOne() {
		return twentyOne;
	}

	public void setTwentyOne(String twentyOne) {
		this.twentyOne = twentyOne;
	}

	public String getTwentyTwo() {
		return twentyTwo;
	}

	public void setTwentyTwo(String twentyTwo) {
		this.twentyTwo = twentyTwo;
	}

	public String getTwentyThree() {
		return twentyThree;
	}

	public void setTwentyThree(String twentyThree) {
		this.twentyThree = twentyThree;
	}

	public String getTwentyFour() {
		return twentyFour;
	}

	public void setTwentyFour(String twentyFour) {
		this.twentyFour = twentyFour;
	}

	public String getTwentyFive() {
		return twentyFive;
	}

	public void setTwentyFive(String twentyFive) {
		this.twentyFive = twentyFive;
	}

	public String getTwentySix() {
		return twentySix;
	}

	public void setTwentySix(String twentySix) {
		this.twentySix = twentySix;
	}

	public String getTwentySeven() {
		return twentySeven;
	}

	public void setTwentySeven(String twentySeven) {
		this.twentySeven = twentySeven;
	}

	public String getTwentyEight() {
		return twentyEight;
	}

	public void setTwentyEight(String twentyEight) {
		this.twentyEight = twentyEight;
	}

	public String getTwentyNine() {
		return twentyNine;
	}

	public void setTwentyNine(String twentyNine) {
		this.twentyNine = twentyNine;
	}

	public String getThirty() {
		return thirty;
	}

	public void setThirty(String thirty) {
		this.thirty = thirty;
	}

	public String getThirtyOne() {
		return thirtyOne;
	}

	public void setThirtyOne(String thirtyOne) {
		this.thirtyOne = thirtyOne;
	}

	public Date getCreatedTm() {
		return createdTm;
	}
	public void setCreatedTm(Date createdTm) {
		this.createdTm = createdTm;
	}
	public String getCreatedEmpCode() {
		return createdEmpCode;
	}
	public void setCreatedEmpCode(String createdEmpCode) {
		this.createdEmpCode = createdEmpCode;
	}
}
