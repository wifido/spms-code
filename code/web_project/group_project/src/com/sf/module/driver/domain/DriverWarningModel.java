package com.sf.module.driver.domain;


import com.sf.framework.base.domain.BaseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.sf.module.common.util.Clock.afterDay;
import static com.sf.module.common.util.Clock.range;

public class DriverWarningModel extends BaseEntity {
    private static final int MAX_CONTINUOUS_DAY = 7;
    private List<String> allDays = newArrayList();
    private String firstDay;
    private String lastDay;
    private String employeeCode;
    private String employeeName;
    private String departmentCode;
    private String departmentName;
    private String areaCode;
    private String areaName;

    public DriverWarningModel(String firstDay, String lastDay,
                              String employeeCode, String employeeName,
                              String departmentCode, String departmentName,
                              String areaCode, String areaName) {
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
        this.areaCode = areaCode;
        this.areaName = areaName;
    }

    public void putDay(String date) {
        allDays.add(date);
    }

    public List<SingleWarning> singleRangeWarning(int nextIndex) {
        List<DriveStatus> driveStatuses = fillUpRestDayToInsteadOfNotFoundDays();
        List<SingleWarning> singleWarnings = calculateWarning(driveStatuses);
        injectSerial(singleWarnings, nextIndex);
        return singleWarnings;
    }

    private void injectSerial(List<SingleWarning> singleWarnings, int nextIndex) {
    	for (int index = 0; index < singleWarnings.size(); index++) {
            SingleWarning singleWarning = singleWarnings.get(index);
            singleWarning.setSerial(index + nextIndex + 1);
        }
    }

    private List<SingleWarning> calculateWarning(List<DriveStatus> driveStatuses) {
        List<SingleWarning> driverWarningList = newArrayList();

        int maxContinuousWorkingDay = 0;
        for (int index = 0; index < driveStatuses.size(); index++) {
            DriveStatus currentStatus = driveStatuses.get(index);

            boolean isLastOne = index == driveStatuses.size() - 1;
            boolean isNextAdjustToRest = isLastOne || !driveStatuses.get(index + 1).isWorking;

            maxContinuousWorkingDay++;
            if (!currentStatus.isWorking) {
                maxContinuousWorkingDay = 0;
            }

            if (maxContinuousWorkingDay >= MAX_CONTINUOUS_DAY) {
            	if (!isNextAdjustToRest) {
					continue;
				}
                SingleWarning driverWarning = new SingleWarning(currentStatus.date, isNextAdjustToRest,
                        maxContinuousWorkingDay,
                        employeeCode, employeeName, departmentCode, departmentName, areaCode, areaName);
                driverWarningList.add(driverWarning);
            }
        }

        return driverWarningList;
    }

    private List<DriveStatus> fillUpRestDayToInsteadOfNotFoundDays() {
        List<DriveStatus> driveStatusArrayList = newArrayList();
        for (int index = 0; index < range(firstDay, lastDay); index++) {
            String date = afterDay(firstDay, index + 1);

            boolean contains = allDays.contains(date);
            DriveStatus driveStatus = new DriveStatus(date, contains);

            driveStatusArrayList.add(driveStatus);
        }

        return driveStatusArrayList;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public MultipleWarning multipleWarning() {
        if (this.allDays.isEmpty()) {
            return MultipleWarning.EMPTY;
        }

        HashMap<String, List<DriveStatus>> stringListHashMap = splitByMonthBoundary(fillUpRestDayToInsteadOfNotFoundDays());

        int firstMaxContinuous = calculateMaxContinuousWorking(stringListHashMap.get("1"));
        int secondMaxContinuous = calculateMaxContinuousWorking(stringListHashMap.get("2"));
        int thirdMaxContinuous = calculateMaxContinuousWorking(stringListHashMap.get("3"));

        boolean doNotWarn = firstMaxContinuous == 0 && secondMaxContinuous == 0 && thirdMaxContinuous == 0;

        return doNotWarn
                ? MultipleWarning.EMPTY
                : new MultipleWarning(firstMaxContinuous, secondMaxContinuous, thirdMaxContinuous,
                employeeCode, employeeName, departmentCode, departmentName, areaCode, areaName);
    }

    private int calculateMaxContinuousWorking(List<DriveStatus> strings) {
        List<Integer> continuousList = getContinuousWorkingDays(strings);

        return filterLargestContinuous(continuousList);
    }

    private int filterLargestContinuous(List<Integer> continuousList) {
        int maxInList = 0;
        for (Integer integer : continuousList) {
            if (maxInList <= integer) {
                maxInList = integer;
            }
        }

        return maxInList >= MAX_CONTINUOUS_DAY ? maxInList : 0;
    }

    private List<Integer> getContinuousWorkingDays(List<DriveStatus> strings) {
        List<Integer> continuousList = newArrayList();
        int maxContinuous = 0;
        
		for (int i = 0; i < strings.size(); i++) {
			DriveStatus driveStatus = strings.get(i);

			if (driveStatus.isWorking) {
				maxContinuous++;
			}

			if (!driveStatus.isWorking && maxContinuous > 0
					|| strings.size() == maxContinuous || driveStatus.isWorking && i == strings.size() - 1) {
				continuousList.add(maxContinuous);
				maxContinuous = 0;
			}
		}
        return continuousList;
    }

    private HashMap<String, List<DriveStatus>> splitByMonthBoundary(List<DriveStatus> driveStatuses) {
        HashMap<String, List<DriveStatus>> eachMonth = new HashMap<String, List<DriveStatus>>();
        eachMonth.put("1", new ArrayList<DriveStatus>());
        eachMonth.put("2", new ArrayList<DriveStatus>());
        eachMonth.put("3", new ArrayList<DriveStatus>());

        int currentMonth = 1;
        for (int index = 0; index < driveStatuses.size(); index++) {
            DriveStatus currentStatus = driveStatuses.get(index);
            boolean isLastStatus = index == driveStatuses.size() - 1;
            eachMonth.get(String.valueOf(currentMonth)).add(driveStatuses.get(index));

            if (isLastStatus) {
                break;
            }

            if (isNotSameMonth(currentStatus, driveStatuses.get(index + 1))) {
                currentMonth++;
            }
        }

        return eachMonth;
    }

    private boolean isNotSameMonth(DriveStatus driveStatus, DriveStatus nextStatus) {
        String currentMonth = driveStatus.date.substring(0, 6);
        String nextMonth = nextStatus.date.substring(0, 6);
        return !currentMonth.equals(nextMonth);
    }

    public static abstract class Warning {
        protected String employeeCode;
        protected String employeeName;
        protected String departmentName;
        protected String departmentCode;
        protected String areaCode;
        protected String areaName;
        private int serial;

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
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

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public String getEmployeeName() {
            return employeeName;
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
        }

        public String getEmployeeCode() {
            return employeeCode;
        }

        public void setEmployeeCode(String employeeCode) {
            this.employeeCode = employeeCode;
        }

        public Warning(String employeeCode, String employeeName,
                       String departmentCode, String departmentName,
                       String areaCode, String areaName) {
            this.employeeCode = employeeCode;
            this.employeeName = employeeName;
            this.departmentName = departmentName;
            this.departmentCode = departmentCode;
            this.areaCode = areaCode;
            this.areaName = areaName;
        }

        public int getSerial() {
            return serial;
        }

        public void setSerial(int serial) {
            this.serial = serial;
        }
    }

    public static class SingleWarning extends Warning {
        public final String warningDay;
        public final boolean hasWarningNextDay;
        public final int maxContinuousWorkingDay;

        public SingleWarning(String warningDay, boolean hasWarningNextDay,
                             int maxContinuousWorkingDay,
                             String employeeCode, String employeeName,
                             String departmentCode, String departmentName,
                             String areaCode, String areaName) {
            super(employeeCode, employeeName, departmentCode, departmentName, areaCode, areaName);
            this.warningDay = warningDay;
            this.hasWarningNextDay = hasWarningNextDay;
            this.maxContinuousWorkingDay = maxContinuousWorkingDay;
        }

        public String getWarningDay() {
            return warningDay;
        }

        public int getMaxContinuousWorkingDay() {
            return maxContinuousWorkingDay;
        }

        public boolean isHasWarningNextDay() {
            return hasWarningNextDay;
        }
    }

    public static class MultipleWarning extends Warning {
        public final static MultipleWarning EMPTY = new MultipleWarning();
        public final int firstMaxContinuousWorkingDay;
        public final int secondMaxContinuousWorkingDay;
        public final int thirdMaxContinuousWorkingDay;
        public final boolean threeMonthContinuous;
        public final boolean twoMonthContinuous;

        public MultipleWarning(int firstMaxContinuousWorkingDay, int secondMaxContinuousWorkingDay, int thirdMaxContinuousWorkingDay,
                               String employeeCode, String employeeName,
                               String departmentCode, String departmentName,
                               String areaCode, String areaName) {
            super(employeeCode, employeeName, departmentCode, departmentName, areaCode, areaName);
            this.firstMaxContinuousWorkingDay = firstMaxContinuousWorkingDay;
            this.secondMaxContinuousWorkingDay = secondMaxContinuousWorkingDay;
            this.thirdMaxContinuousWorkingDay = thirdMaxContinuousWorkingDay;
            this.threeMonthContinuous = firstMaxContinuousWorkingDay >= MAX_CONTINUOUS_DAY && secondMaxContinuousWorkingDay >= MAX_CONTINUOUS_DAY && thirdMaxContinuousWorkingDay >= MAX_CONTINUOUS_DAY;
            this.twoMonthContinuous = (firstMaxContinuousWorkingDay >= MAX_CONTINUOUS_DAY && secondMaxContinuousWorkingDay >= MAX_CONTINUOUS_DAY) || (secondMaxContinuousWorkingDay >= MAX_CONTINUOUS_DAY && thirdMaxContinuousWorkingDay >= MAX_CONTINUOUS_DAY);
        }

        public MultipleWarning() {
            this(0, 0, 0, "", "", "", "", "", "");
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

    private class DriveStatus {
        public final String date;
        public final boolean isWorking;

        public DriveStatus(String date, boolean isWorking) {
            this.date = date;
            this.isWorking = isWorking;
        }
    }

    public List<String> getAllDays() {
        return allDays;
    }

    public void setAllDays(List<String> allDays) {
        this.allDays = allDays;
    }

    public String getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(String firstDay) {
        this.firstDay = firstDay;
    }

    public String getLastDay() {
        return lastDay;
    }

    public void setLastDay(String lastDay) {
        this.lastDay = lastDay;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
