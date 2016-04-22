package com.sf.module.warehouse.domain;

import static com.sf.module.common.util.StringUtil.isBlank;
import java.util.Date;
import com.sf.framework.base.domain.BaseEntity;
import com.sf.module.common.util.StringUtil;

public class WarehouseAttendance extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String scheduleId;
	private String empCode;
	private String empName;
	private String areaCode;
	private String deptCode;
	private String groupCode;
	private Date workDate;
	private String workTime;
	private String jobSeqCode;
	private String jobSeq;
	private String positionType;
	private String personType;
	private String dutyName;
	private String creatEmpCode;
	private Date creatTime;
	private String modifyEmpCode;
	private Date modifyTime;
	private String month;

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

	private String totalWorkTime;

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
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

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public String getJobSeqCode() {
		return jobSeqCode;
	}

	public void setJobSeqCode(String jobSeqCode) {
		this.jobSeqCode = jobSeqCode;
	}

	public String getJobSeq() {
		return jobSeq;
	}

	public void setJobSeq(String jobSeq) {
		this.jobSeq = jobSeq;
	}

	public String getPositionType() {
		return positionType;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}

	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public String getCreatEmpCode() {
		return creatEmpCode;
	}

	public void setCreatEmpCode(String creatEmpCode) {
		this.creatEmpCode = creatEmpCode;
	}

	public String getModifyEmpCode() {
		return modifyEmpCode;
	}

	public void setModifyEmpCode(String modifyEmpCode) {
		this.modifyEmpCode = modifyEmpCode;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
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

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getTotalWorkTime() {
		return totalWorkTime;
	}

	public void setTotalWorkTime(String totalWorkTime) {
		this.totalWorkTime = totalWorkTime;
	}

	public String getDutyName() {
    	return dutyName;
    }

	public void setDutyName(String dutyName) {
    	this.dutyName = dutyName;
    }


	public static enum Day {
		firstDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setFirstDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.firstDay;
			}
		},
		secondDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setSecondDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.secondDay;
			}
		},
		thirdDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setThirdDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.thirdDay;
			}
		},
		fourthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setFourthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.fourthDay;
			}
		},
		fifthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setFifthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.fifthDay;
			}
		},
		sixthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setSixthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.sixthDay;
			}
		},
		seventhDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setSeventhDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.seventhDay;
			}
		},
		eighthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setEighthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.eighthDay;
			}
		},
		ninthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setNinthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.ninthDay;
			}
		},
		tenthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setTenthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.tenthDay;
			}
		},
		eleventhDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setEleventhDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.eleventhDay;
			}
		},
		twelfthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setTwelfthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.twelfthDay;
			}
		},
		thirteenthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setThirteenthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.thirteenthDay;
			}
		},
		fourteenthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setFourteenthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.fourteenthDay;
			}
		},
		fifteenthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setFifteenthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.fifteenthDay;
			}
		},
		sixteenthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setSixteenthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.sixteenthDay;
			}
		},
		seventeenthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setSeventeenthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.seventeenthDay;
			}
		},
		eighteenthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setEighteenthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.eighteenthDay;
			}
		},
		nineteenthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setNineteenthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.nineteenthDay;
			}
		},
		twentiethDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setTwentiethDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.twentiethDay;
			}
		},
		twentyFirstDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setTwentyFirstDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.twentyFirstDay;
			}
		},
		twentySecondDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setTwentySecondDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.twentySecondDay;
			}
		},
		twentyThirdDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setTwentyThirdDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.twentyThirdDay;
			}
		},
		twentyFourthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setTwentyFourthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.twentyFourthDay;
			}
		},
		twentyFifthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setTwentyFifthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.twentyFifthDay;
			}
		},
		twentySixthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setTwentySixthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.twentySixthDay;
			}
		},
		twentySeventhDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setTwentySeventhDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.twentySeventhDay;
			}
		},
		twentyEighthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setTwentyEighthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.twentyEighthDay;
			}
		},
		twentyNinthDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setTwentyNinthDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.twentyNinthDay;
			}
		},
		thirtiethDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setThirtiethDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.thirtiethDay;
			}
		},
		thirtyFirstDay {
			@Override
			public void setWorkTime(WarehouseAttendance attendance, String workTime) {
				attendance.setThirtyFirstDay(workTime);
			}

			@Override
			public String getWorkTime(WarehouseAttendance attendance) {
				return attendance.thirtyFirstDay;
			}
		};

		public abstract void setWorkTime(WarehouseAttendance attendance, String configureCode);

		public abstract String getWorkTime(WarehouseAttendance attendance);
	}

	public void setWorkTimeWithSpecifyDay(int day, String workTime) {
		if (StringUtil.isBlank(workTime))
			return;
		for (Day day1 : Day.values()) {
			if (day == day1.ordinal() + 1) {
				day1.setWorkTime(this, workTime);
				break;
			}
		}
	}

	public void countTotalWorkTime() {
		Day[] values = Day.values();
		double totalWorkTime = 0;
		for (Day day : Day.values()) {
			String workTime = day.getWorkTime(this);
			if (isBlank(workTime))
				continue;
			totalWorkTime = totalWorkTime + Double.parseDouble(workTime);
		}
		this.totalWorkTime = String.valueOf(totalWorkTime);
	}
}
