/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-19     houjingyu       创建
 **********************************************/

package com.sf.module.operation.domain;

import java.util.Date;

import com.sf.framework.base.domain.BaseEntity;

/**
 * 
 * 排班管理实体
 * 
 * @author houjingyu 2014-06-19
 * 
 */
public class SchedulMgt extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else {
			if (this.getClass() == obj.getClass()) {
				SchedulMgt u = (SchedulMgt) obj;
				if (this.getEmpCode().equals(u.getEmpCode())
						&& this.getDeptId().equals(u.getDeptId()) && this.getYm().equals(u.getYm())) {
					return true;
				} else {
					return false;
				}

			} else {
				return false;
			}
		}
	}

	/**
	 * 年月
	 */
	private String ym;

	/**
	 * 网点代码
	 */
	private Long deptId;
	private String deptCode;
	private String areaCode;

	/**
	 * 被排班人工号
	 */
	private String empCode;
	/**
	 * 被排班人姓名
	 */
	private String empName;

	/**
	 * 用工类型
	 */
	private int workType;
	private String workTypeStr;
	/**
	 * 离职日期
	 */
	private String dimissionDt;
	/**
	 * 错误描述
	 */
	private String errorDesc;
	/**
	 * 第1天
	 */
	private String day1;

	/**
	 * 第2天
	 */
	private String day2;

	/**
	 * 第3天
	 */
	private String day3;

	/**
	 * 第4天
	 */
	private String day4;

	/**
	 * 第5天
	 */
	private String day5;

	/**
	 * 第6天
	 */
	private String day6;

	/**
	 * 第7天
	 */
	private String day7;

	/**
	 * 第8天
	 */
	private String day8;

	/**
	 * 第9天
	 */
	private String day9;

	/**
	 * 第10天
	 */
	private String day10;

	/**
	 * 第11天
	 */
	private String day11;

	/**
	 * 第12天
	 */
	private String day12;

	/**
	 * 第13天
	 */
	private String day13;

	/**
	 * 第14天
	 */
	private String day14;

	/**
	 * 第15天
	 */
	private String day15;

	/**
	 * 第16天
	 */
	private String day16;

	/**
	 * 第17天
	 */
	private String day17;

	/**
	 * 第18天
	 */
	private String day18;

	/**
	 * 第19天
	 */
	private String day19;

	/**
	 * 第20天
	 */
	private String day20;

	/**
	 * 第21天
	 */
	private String day21;

	/**
	 * 第22天
	 */
	private String day22;

	/**
	 * 第23天
	 */
	private String day23;

	/**
	 * 第24天
	 */
	private String day24;

	/**
	 * 第25天
	 */
	private String day25;

	/**
	 * 第26天
	 */
	private String day26;

	/**
	 * 第27天
	 */
	private String day27;

	/**
	 * 第28天
	 */
	private String day28;

	/**
	 * 第29天
	 */
	private String day29;

	/**
	 * 第30天
	 */
	private String day30;

	/**
	 * 第31天
	 */
	private String day31;

	/**
	 * 创建日期
	 */
	private Date createTm;

	/**
	 * 修改日期
	 */
	private Date modifiedTm;

	/**
	 * 创建人工号
	 */
	private String createEmpCode;

	/**
	 * 修改人工号
	 */
	private String modifiedEmpCode;

	/**
	 * 版本号
	 */
	private Integer version;

	private String betweenDate;

	private Integer commitStatus;

	private Integer synchroStatus;

	private String transferDate;

	private String dateFrom;
	
	private String lastZno;
	
	private String sfDate;
	
	private String empPostType;
	
	private String empDutyName;
	
	public String getEmpDutyName() {
		return empDutyName;
	}

	public void setEmpDutyName(String empDutyName) {
		this.empDutyName = empDutyName;
	}

	public String getSfDate() {
		return sfDate;
	}

	public void setSfDate(String sfDate) {
		this.sfDate = sfDate;
	}

	public String getLastZno() {
		return lastZno;
	}

	public void setLastZno(String lastZno) {
		this.lastZno = lastZno;
	}

	public String getEmpPostType() {
		return empPostType;
	}

	public void setEmpPostType(String empPostType) {
		this.empPostType = empPostType;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}

	public Integer getSynchroStatus() {
		return synchroStatus;
	}

	public void setSynchroStatus(Integer synchroStatus) {
		this.synchroStatus = synchroStatus;
	}

	public Integer getCommitStatus() {
		return commitStatus;
	}

	public void setCommitStatus(Integer commitStatus) {
		this.commitStatus = commitStatus;
	}

	/**
	 * 获取年月
	 */
	public String getYm() {
		return this.ym;
	}

	/**
	 * 设置年月
	 */
	public void setYm(String ym) {
		this.ym = ym;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	/**
	 * 获取被排班人工号
	 */
	public String getEmpCode() {
		return this.empCode;
	}

	/**
	 * 设置被排班人工号
	 */
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	/**
	 * 获取第1天
	 */
	public String getDay1() {
		return this.day1;
	}

	/**
	 * 设置第1天
	 */
	public void setDay1(String day1) {
		this.day1 = day1;
	}

	/**
	 * 获取第2天
	 */
	public String getDay2() {
		return this.day2;
	}

	/**
	 * 设置第2天
	 */
	public void setDay2(String day2) {
		this.day2 = day2;
	}

	/**
	 * 获取第3天
	 */
	public String getDay3() {
		return this.day3;
	}

	/**
	 * 设置第3天
	 */
	public void setDay3(String day3) {
		this.day3 = day3;
	}

	/**
	 * 获取第4天
	 */
	public String getDay4() {
		return this.day4;
	}

	/**
	 * 设置第4天
	 */
	public void setDay4(String day4) {
		this.day4 = day4;
	}

	/**
	 * 获取第5天
	 */
	public String getDay5() {
		return this.day5;
	}

	/**
	 * 设置第5天
	 */
	public void setDay5(String day5) {
		this.day5 = day5;
	}

	/**
	 * 获取第6天
	 */
	public String getDay6() {
		return this.day6;
	}

	/**
	 * 设置第6天
	 */
	public void setDay6(String day6) {
		this.day6 = day6;
	}

	/**
	 * 获取第7天
	 */
	public String getDay7() {
		return this.day7;
	}

	/**
	 * 设置第7天
	 */
	public void setDay7(String day7) {
		this.day7 = day7;
	}

	/**
	 * 获取第8天
	 */
	public String getDay8() {
		return this.day8;
	}

	/**
	 * 设置第8天
	 */
	public void setDay8(String day8) {
		this.day8 = day8;
	}

	/**
	 * 获取第9天
	 */
	public String getDay9() {
		return this.day9;
	}

	/**
	 * 设置第9天
	 */
	public void setDay9(String day9) {
		this.day9 = day9;
	}

	/**
	 * 获取第10天
	 */
	public String getDay10() {
		return this.day10;
	}

	/**
	 * 设置第10天
	 */
	public void setDay10(String day10) {
		this.day10 = day10;
	}

	/**
	 * 获取第11天
	 */
	public String getDay11() {
		return this.day11;
	}

	/**
	 * 设置第11天
	 */
	public void setDay11(String day11) {
		this.day11 = day11;
	}

	/**
	 * 获取第12天
	 */
	public String getDay12() {
		return this.day12;
	}

	/**
	 * 设置第12天
	 */
	public void setDay12(String day12) {
		this.day12 = day12;
	}

	/**
	 * 获取第13天
	 */
	public String getDay13() {
		return this.day13;
	}

	/**
	 * 设置第13天
	 */
	public void setDay13(String day13) {
		this.day13 = day13;
	}

	/**
	 * 获取第14天
	 */
	public String getDay14() {
		return this.day14;
	}

	/**
	 * 设置第14天
	 */
	public void setDay14(String day14) {
		this.day14 = day14;
	}

	/**
	 * 获取第15天
	 */
	public String getDay15() {
		return this.day15;
	}

	/**
	 * 设置第15天
	 */
	public void setDay15(String day15) {
		this.day15 = day15;
	}

	/**
	 * 获取第16天
	 */
	public String getDay16() {
		return this.day16;
	}

	/**
	 * 设置第16天
	 */
	public void setDay16(String day16) {
		this.day16 = day16;
	}

	/**
	 * 获取第17天
	 */
	public String getDay17() {
		return this.day17;
	}

	/**
	 * 设置第17天
	 */
	public void setDay17(String day17) {
		this.day17 = day17;
	}

	/**
	 * 获取第18天
	 */
	public String getDay18() {
		return this.day18;
	}

	/**
	 * 设置第18天
	 */
	public void setDay18(String day18) {
		this.day18 = day18;
	}

	/**
	 * 获取第19天
	 */
	public String getDay19() {
		return this.day19;
	}

	/**
	 * 设置第19天
	 */
	public void setDay19(String day19) {
		this.day19 = day19;
	}

	/**
	 * 获取第20天
	 */
	public String getDay20() {
		return this.day20;
	}

	/**
	 * 设置第20天
	 */
	public void setDay20(String day20) {
		this.day20 = day20;
	}

	/**
	 * 获取第21天
	 */
	public String getDay21() {
		return this.day21;
	}

	/**
	 * 设置第21天
	 */
	public void setDay21(String day21) {
		this.day21 = day21;
	}

	/**
	 * 获取第22天
	 */
	public String getDay22() {
		return this.day22;
	}

	/**
	 * 设置第22天
	 */
	public void setDay22(String day22) {
		this.day22 = day22;
	}

	/**
	 * 获取第23天
	 */
	public String getDay23() {
		return this.day23;
	}

	/**
	 * 设置第23天
	 */
	public void setDay23(String day23) {
		this.day23 = day23;
	}

	/**
	 * 获取第24天
	 */
	public String getDay24() {
		return this.day24;
	}

	/**
	 * 设置第24天
	 */
	public void setDay24(String day24) {
		this.day24 = day24;
	}

	/**
	 * 获取第25天
	 */
	public String getDay25() {
		return this.day25;
	}

	/**
	 * 设置第25天
	 */
	public void setDay25(String day25) {
		this.day25 = day25;
	}

	/**
	 * 获取第26天
	 */
	public String getDay26() {
		return this.day26;
	}

	/**
	 * 设置第26天
	 */
	public void setDay26(String day26) {
		this.day26 = day26;
	}

	/**
	 * 获取第27天
	 */
	public String getDay27() {
		return this.day27;
	}

	/**
	 * 设置第27天
	 */
	public void setDay27(String day27) {
		this.day27 = day27;
	}

	/**
	 * 获取第28天
	 */
	public String getDay28() {
		return this.day28;
	}

	/**
	 * 设置第28天
	 */
	public void setDay28(String day28) {
		this.day28 = day28;
	}

	/**
	 * 获取第29天
	 */
	public String getDay29() {
		return this.day29;
	}

	/**
	 * 设置第29天
	 */
	public void setDay29(String day29) {
		this.day29 = day29;
	}

	/**
	 * 获取第30天
	 */
	public String getDay30() {
		return this.day30;
	}

	/**
	 * 设置第30天
	 */
	public void setDay30(String day30) {
		this.day30 = day30;
	}

	/**
	 * 获取第31天
	 */
	public String getDay31() {
		return this.day31;
	}

	/**
	 * 设置第31天
	 */
	public void setDay31(String day31) {
		this.day31 = day31;
	}

	/**
	 * 获取创建日期
	 */
	public Date getCreateTm() {
		return this.createTm;
	}

	/**
	 * 设置创建日期
	 */
	public void setCreateTm(Date createTm) {
		this.createTm = createTm;
	}

	/**
	 * 获取修改日期
	 */
	public Date getModifiedTm() {
		return this.modifiedTm;
	}

	/**
	 * 设置修改日期
	 */
	public void setModifiedTm(Date modifiedTm) {
		this.modifiedTm = modifiedTm;
	}

	/**
	 * 获取创建人工号
	 */
	public String getCreateEmpCode() {
		return this.createEmpCode;
	}

	/**
	 * 设置创建人工号
	 */
	public void setCreateEmpCode(String createEmpCode) {
		this.createEmpCode = createEmpCode;
	}

	/**
	 * 获取修改人工号
	 */
	public String getModifiedEmpCode() {
		return this.modifiedEmpCode;
	}

	/**
	 * 设置修改人工号
	 */
	public void setModifiedEmpCode(String modifiedEmpCode) {
		this.modifiedEmpCode = modifiedEmpCode;
	}

	public int getWorkType() {
		return workType;
	}

	public void setWorkType(int workType) {
		this.workType = workType;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public String getEmpName() {
		return empName;
	}

	public String getDimissionDt() {
		return dimissionDt;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
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

	public String getWorkTypeStr() {
		return workTypeStr;
	}

	public void setWorkTypeStr(String workTypeStr) {
		this.workTypeStr = workTypeStr;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getBetweenDate() {
		return betweenDate;
	}

	public void setBetweenDate(String betweenDate) {
		this.betweenDate = betweenDate;
	}

}