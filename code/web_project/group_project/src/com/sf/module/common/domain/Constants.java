package com.sf.module.common.domain;

public class Constants {
	public final static long ONE_DAY = 86400000;

	public final static String COMMA = ",";
	public final static String BEAMS = "-";
	public final static String COLON = ":";
	public final static String POINT = ".";
	public static final String ROOT = "root";
	public static final String TOTAL_SIZE = "totalSize";
	public static final String TOTALSIZE = "TOTALSIZE";
	public static final String REST_MARK = "休";
	public static final String [] DEFAULT_CLASS = {"休","离","SW","OFF"};
	public static final String QUESTION_MARK = "?";
	public static final String AND = " and ";
	public static final String START = "start";
	public static final String LIMIT = "limit";

	// 仓管班别用到
	public static final String COLUMN_DEPT_CODE = "DEPT_CODE";
	public static final String CONDITION_DEPT_CODE = " AND DEPT.DEPT_CODE IN (SELECT dept_code  FROM TM_DEPARTMENT" + " WHERE DELETE_FLG = 0"
			+ " START WITH dept_code = $$ " + " CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE)";
	public static final String CONDITION_THIS_DEPT_CODE = " AND DEPT.DEPT_CODE IN (SELECT dept_code  FROM TM_DEPARTMENT" + " WHERE DELETE_FLG = 0  AND dept_code = $$)";
	public static final String COLUMN_ENABLE_DT = "ENABLE_DT";
	public static final String CONDITION_ENABLE_DT = " AND TO_CHAR(BI.ENABLE_DT,'YYYYMMDD') <= $$  AND TO_CHAR(BI.DISABLE_DT,'YYYYMMDD') > $$";
	public static final String COLUMN_DISABLE_DT = "DISABLE_DT";
	public static final String CONDITION_DISABLE_DT = " AND TO_CHAR(BI.DISABLE_DT,'YYYYMMDD') >= $$";
	public static final String COLUMN_EMP_POST_TYPE = "EMP_POST_TYPE";
    public static final String CONDITION_DEPARTMENT_CODE = "  START WITH dept_code = :in_departmentCode CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE ";
	public static final String CONDITION_THIS_DEPARTMENT_CODE = " AND dept_code = :in_departmentCode ";
	public static final String ALIASE_TM_DEPARTMENT = "TM_DEPARTMENT";
	public static final String ALIASE_TM_OSS_EMPLOYEE = "TM_OSS_EMPLOYEE";
	public static final String ALIASE_EMP_DEPT = "EMP_DEPT";
	public static final String ALIASE_TT_SCHEDULE_DAILY = "TT_SCHEDULE_DAILY";
	public static final String IS_CHECK_OF_NETWORK = "isCheckOfNetwork";

	public static final String COLUMN_ID = "ID";
	public static final String COLUMN_MONTH_ID = "MONTH_ID";
	public static final String COLUMN_DAY_OF_MONTH = "DAY_OF_MONTH";
	public static final String COLUMN_AREA_CODE = "AREA_CODE";
	public static final String COLUMN_EMP_CODE = "EMP_CODE";
	public static final String COLUMN_EMP_NAME = "EMP_NAME";
	public static final String COLUMN_WORK_TYPE_NAME = "WORK_TYPE_NAME";
	public static final String COLUMN_WORK_TYPE = "WORK_TYPE";
	public static final String COLUMN_MOTOR_DEPT = "MOTOR_DEPT";
	public static final String COLUMN_EMP_DUTY_NAME = "EMP_DUTY_NAME";
	public static final String COLUMN_SCHEDULING_CODE = "SCHEDULING_CODE";
	public static final String COLUMN_DEPARTMENT_CODE = "DEPARTMENT_CODE";
	public static final String COLUMN_EMPLOYEE_CODE = "EMPLOYEE_CODE";
	public static final String COLUMN_DEPT_ID = "DEPT_ID";

	public static final String CONDITION_MONTH_ID = "MONTH_ID = $$";
	public static final String CONDITION_EMP_CODE = "EMP_CODE = $$";
	public static final String CONDITION_DEPT_CODED = "DEPT_CODE IN ($$)";

	public static final String IMPORT_SUCCESSFUL = "导入成功 %d 条，导入失败 %d 条";
	public static final String KEY_MESSAGE = "msg";
	public static final String KEY_FILE_NAME = "fileName";
	public static final String NUMERIC_FORMAT = "#";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final int EXPORT_MAX = 60000;
	public static final String KEY_SUCCESS = "success";
	public static final String KEY_ERROR = "error";
	public static final String FIELD_FLAG = "flag";
	public static final String FIELD_QUERY_OUT_EMPLOYEE = "queryOutEmployee";
	public static final String KEY_FILE = "file";
	public static final String COLUMN_GROUP_CODE = "GROUP_CODE";
	public static final String COLUMN_GROUP_NAME = "GROUP_NAME";
	public static final String COLUMN_GROUP_ID = "GROUP_ID";
	public static final String FIELD_USER_ID = "userId";
	public static final String KEY_EMP_CODES = "empCodes";
	public static final String KEY_EMP_IDS = "empIds";

    public static final String IMPORT_RESULT = "importResult";
    public static final String PAGING_QUERY_STATE = "pagingQuery";
    public static final String SCHEDULING_TYPE = "schedulingType";
    public static final String DOWNLOAD_PATH = "downloadPath";
    public static final String KEY_EMPLOYEE_CODE = "employeeCode";
    public static final String KEY_YEAR_MONTH = "yearMonth";
    public static final String SCHEDULING_DAYS = "schedulingDays";
    public static final String KEY_CONFIGURE_CODE = "configureCode";
    public static final String KEY_DEPARTMENT_CODE = "departmentCode";
    public static final String KEY_YEAR_WEEK = "yearWeek";
    public static final String COLUMN_YM = "YM";
    public static final String KEY_START_TIME = "startTime";
    public static final String KEY_END_TIME = "endTime";
    public static final String KEY_AREA_CODE = "AREA_CODE";

    public static final String KEY_AREA_NAME = "AREA_NAME";
    public static final String KEY_DEPT_CODE = "DEPT_CODE";
    public static final String KEY_DEPT_NAME = "DEPT_NAME";
    public static final String KEY_DRIVER_IDENTIFY = "DRIVER_IDENTIFY";
    public static final String KEY_EMP_NAME = "EMP_NAME";
    public static final String KEY_WARNNING_DAYS = "WARNNING_DAYS";
    public static final String KEY_DRIVE_DAY = "DRIVE_DAY";
    public static final String KEY_CAPITAL_YEAR_MONTH = "YEAR_MONTH";
}
