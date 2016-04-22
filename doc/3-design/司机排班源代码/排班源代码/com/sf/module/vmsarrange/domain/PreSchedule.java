/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-5-28     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.sf.framework.base.domain.BaseEntity;

/**
 *
 * PreSchedule实体类
 *
 */
public class PreSchedule extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//班次类型，1是正常，2是顶班，3是机动
	private Integer classType;
	private ArrDepartment department;
	//驾驶员id
	private ArrDriver driver;
	//年月份
	private String yearMonth;
	//计划休息天数
	private Integer planDay;
	//实际休息天数
	private Integer realDay;
	//排班吻合率
	private String rate;
	//预排班草稿状态
	private Integer draftFlag;
	//出勤时长
	private String attendanceTm;
	//驾驶时长
	private String drivingTm;
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
	//修改时间
	private Date modifiedTm;
	//修改人
	private String modifiedEmpCode;
	//实际排班关系表
	private Set<TransferClassesRL> transferClassesRLs = new HashSet<TransferClassesRL>(0);
	//地区名
	private String areaDeptName;
	//地区网点代码
	private String areaDeptCode;
	
	public String getAreaDeptName() {
		return areaDeptName;
	}
	public void setAreaDeptName(String areaDeptName) {
		this.areaDeptName = areaDeptName;
	}
	public String getAreaDeptCode() {
		return areaDeptCode;
	}
	public void setAreaDeptCode(String areaDeptCode) {
		this.areaDeptCode = areaDeptCode;
	}
	public Set<TransferClassesRL> getTransferClassesRLs() {
		return transferClassesRLs;
	}
	public void setTransferClassesRLs(Set<TransferClassesRL> transferClassesRLs) {
		this.transferClassesRLs = transferClassesRLs;
	}
	public Integer getClassType() {
		return classType;
	}
	public void setClassType(Integer classType) {
		this.classType = classType;
	}
	public ArrDepartment getDepartment() {
		return department;
	}
	public void setDepartment(ArrDepartment department) {
		this.department = department;
	}
	public ArrDriver getDriver() {
		return driver;
	}
	public void setDriver(ArrDriver driver) {
		this.driver = driver;
	}
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public Integer getPlanDay() {
		return planDay;
	}
	public void setPlanDay(Integer planDay) {
		this.planDay = planDay;
	}
	public Integer getRealDay() {
		return realDay;
	}
	public void setRealDay(Integer realDay) {
		this.realDay = realDay;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public Integer getDraftFlag() {
		return draftFlag;
	}
	public void setDraftFlag(Integer draftFlag) {
		this.draftFlag = draftFlag;
	}
	public String getAttendanceTm() {
		return attendanceTm;
	}
	public void setAttendanceTm(String attendanceTm) {
		this.attendanceTm = attendanceTm;
	}
	public String getDrivingTm() {
		return drivingTm;
	}
	public void setDrivingTm(String drivingTm) {
		this.drivingTm = drivingTm;
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
	public Date getModifiedTm() {
		return modifiedTm;
	}
	public void setModifiedTm(Date modifiedTm) {
		this.modifiedTm = modifiedTm;
	}
	public String getModifiedEmpCode() {
		return modifiedEmpCode;
	}
	public void setModifiedEmpCode(String modifiedEmpCode) {
		this.modifiedEmpCode = modifiedEmpCode;
	}
	
	
}
