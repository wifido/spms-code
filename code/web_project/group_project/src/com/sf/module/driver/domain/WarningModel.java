package com.sf.module.driver.domain;

import java.util.HashMap;

public class WarningModel {
	public static final int MAX_CONTINUOUS_DAY = 7;
	private int serial;
	private String employeeCode;
    private String employeeName;
    private String departmentCode;
    private String departmentName;
    private String areaCode;
    private String areaName;
    private String warningDay;
    private String driveDay;

	public WarningModel(String employeeCode, String employeeName,
			String departmentCode, String departmentName, String areaCode, String areaName, String warningDay,
			String driveDay, int serial) {
		this.serial = serial;
		this.employeeCode = employeeCode;
		this.employeeName = employeeName;
		this.departmentCode = departmentCode;
		this.departmentName = departmentName;
		this.areaCode = areaCode;
		this.areaName = areaName;
		this.warningDay = warningDay;
		this.driveDay = driveDay;
	}

	public WarningModel(String employeeCode, String employeeName,
			String departmentCode, String departmentName, String areaCode,
			String areaName) {
		this.employeeCode = employeeCode;
		this.employeeName = employeeName;
		this.departmentCode = departmentCode;
		this.departmentName = departmentName;
		this.areaCode = areaCode;
		this.areaName = areaName;
	}
	
	public static class MultipleWarning extends WarningModel {
        public int firstMaxContinuousWorkingDay;
        public int secondMaxContinuousWorkingDay;
        public int thirdMaxContinuousWorkingDay;
        public boolean threeMonthContinuous;
        public boolean twoMonthContinuous;
        public HashMap<String, Integer> warningMonths = new HashMap<String, Integer>();

		public MultipleWarning(String employeeCode, String employeeName,
				String departmentCode, String departmentName, String areaCode,
				String areaName) {
			super(employeeCode, employeeName, departmentCode, departmentName,
					areaCode, areaName);
		}
        
        public HashMap<String, Integer> getWarningMonths() {
			return warningMonths;
		}

		public void setWarningMonths(HashMap<String, Integer> warningMonths) {
			this.warningMonths = warningMonths;
		}

		public void setFirstMaxContinuousWorkingDay(int firstMaxContinuousWorkingDay) {
			this.firstMaxContinuousWorkingDay = firstMaxContinuousWorkingDay;
		}

		public void setSecondMaxContinuousWorkingDay(int secondMaxContinuousWorkingDay) {
			this.secondMaxContinuousWorkingDay = secondMaxContinuousWorkingDay;
		}

		public void setThirdMaxContinuousWorkingDay(int thirdMaxContinuousWorkingDay) {
			this.thirdMaxContinuousWorkingDay = thirdMaxContinuousWorkingDay;
		}

		public void setThreeMonthContinuous() {
			this.threeMonthContinuous = firstMaxContinuousWorkingDay >= MAX_CONTINUOUS_DAY && secondMaxContinuousWorkingDay >= MAX_CONTINUOUS_DAY && thirdMaxContinuousWorkingDay >= MAX_CONTINUOUS_DAY;;
		}

		public void setTwoMonthContinuous() {
			this.twoMonthContinuous = (firstMaxContinuousWorkingDay >= MAX_CONTINUOUS_DAY && secondMaxContinuousWorkingDay >= MAX_CONTINUOUS_DAY) || (secondMaxContinuousWorkingDay >= MAX_CONTINUOUS_DAY && thirdMaxContinuousWorkingDay >= MAX_CONTINUOUS_DAY);;
		}

		public int getFirstMaxContinuousWorkingDay() {
            return firstMaxContinuousWorkingDay;
        }

        public int getSecondMaxContinuousWorkingDay() {
            return secondMaxContinuousWorkingDay;
        }

        public int getThirdMaxContinuousWorkingDay() {
            return thirdMaxContinuousWorkingDay;
        }

        public boolean isThreeMonthContinuous() {
            return threeMonthContinuous;
        }

        public boolean isTwoMonthContinuous() {
            return twoMonthContinuous;
        }
    }
	
    public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}
	
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	
	public String getEmployeeName() {
		return employeeName;
	}
	
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	
	public String getDepartmentCode() {
		return departmentCode;
	}
	
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	
	public String getAreaCode() {
		return areaCode;
	}
	
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	public String getWarningDay() {
		return warningDay;
	}
	
	public void setWarningDay(String warningDay) {
		this.warningDay = warningDay;
	}
	
	public String getDriveDay() {
		return driveDay;
	}
	
	public void setDriveDay(String driveDay) {
		this.driveDay = driveDay;
	}

	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}
}
