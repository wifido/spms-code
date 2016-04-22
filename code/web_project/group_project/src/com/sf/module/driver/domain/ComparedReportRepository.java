package com.sf.module.driver.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.sf.module.driver.domain.ComparedReportRepository.buildReporter;
import static java.lang.String.format;

public class ComparedReportRepository {
    public static final String EMPLOYEE_CODE = "EMPLOYEE_CODE";
    public static final String DEPARTMENT_NAME = "DEPARTMENT_NAME";
    public static final String DEPARTMENT_CODE = "DEPARTMENT_CODE";
    public static final String EMPLOYEE_NAME = "EMPLOYEE_NAME";
    public static final String ROW_NUMBER = "ROWNUM";
    public static final String AREA_NAME = "AREA_NAME";
    public static final String AREA_CODE = "AREA_CODE";

    public static final String COL_ERROR_TYPE = "ERROR_TYPE";
    public static final String COL_DEPT_CODE = "DEPT_CODE";
    public static final String COL_USER_ID = "USER_ID";
    
    public static List<ComparedReporter> converDBListToReporterList(List data,int daysCount){
    	List<ComparedReporter> ReporterList = newArrayList();
    	for (Object dataItem : data) {
			Map<String, Object> value = (Map<String, Object>) dataItem;
			ComparedReporter comparedReporter = buildReporter(value, daysCount);
			ReporterList.add(comparedReporter);
		}
    	return ReporterList;
    }

    public static ComparedReporter buildReporter(Map<String, Object> values, int daysCount) {
        int rowNumber = ((BigDecimal) values.get(ROW_NUMBER)).intValue();
        String employeeCode = (String) values.get(EMPLOYEE_CODE);
        String departmentName = (String) values.get(DEPARTMENT_NAME);
        String departmentCode = (String) values.get(DEPARTMENT_CODE);
        String employeeName = (String) values.get(EMPLOYEE_NAME);
        String areaCode = (String) values.get(AREA_CODE);
        String areaName = (String) values.get(AREA_NAME);
        
        ComparedReporter comparedReporter = new ComparedReporter();

		for (int index = 1; index <= daysCount; index++) {
			String daySerial = format("DAY%d", index);
			String str = (String) values.get(daySerial);
			Integer value = str == null ? null : Integer.parseInt(str);
			comparedReporter.putResult(daySerial, value);
		}

        comparedReporter.setEmployeeCode(employeeCode);
        comparedReporter.setEmployeeName(employeeName);
        comparedReporter.setDepartmentCode(departmentCode);
        comparedReporter.setDepartmentName(departmentName);
        comparedReporter.setRowNumber(rowNumber);
        comparedReporter.setAreaCode(areaCode);
        comparedReporter.setAreaName(areaName);
        
        return comparedReporter;
    }

    public static class ComparedReporter {
        private static final String[] COMPARE_VALUE_TYPES = {"", "排班未出勤", "出勤未排班", "出勤线路少于排班线路", "出勤线路超出排班线路"};

        private String employeeCode;
        private String employeeName;
        private String departmentCode;
        private String departmentName;
        private String areaCode;
        private String areaName;

        private Map<String, Object> results = newHashMap();
        private int rowNumber;

        public void putResult(String daySerial, Integer result) {
            results.put(daySerial, result == null ? 0 : result);
        }

        public String getAreaCode() {
            return areaCode;
        }

        public String getDepartmentCode() {
            return departmentCode;
        }

        public String getValueWithSpecifyDay(String targetDay) {
            Integer value = (Integer) this.results.get(targetDay);
            return COMPARE_VALUE_TYPES[value];
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public void setDepartmentCode(String departmentCode) {
            this.departmentCode = departmentCode;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
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

        public int getRowNumber() {
            return rowNumber;
        }

        public void setRowNumber(int rowNumber) {
            this.rowNumber = rowNumber;
        }

		public String getAreaName() {
			return areaName;
		}

		public void setAreaName(String areaName) {
			this.areaName = areaName;
		}
        
    }
}