package com.sf.module.warehouse.dao;

public class WarehouseAttendanceRepository {
	public static final String  STRING_DYNAMIC_PARAMETER = "${parameter}";
	
	private static final String SQL_FOR_QUERY_RECURSIVE_DEPARTMENT = "" 
            + "                     (SELECT dept_code \n "
            + "                          FROM TM_DEPARTMENT \n "
            + "                         WHERE DELETE_FLG = 0 \n "
            + "                         START WITH dept_code = :DEPARTMENT_CODE \n "
            + "                        CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE) dept \n ";
	
	public static final String SQL_QUERY_ATTENDANCE_COUNT = "" 
			+ "  select count(*) TOTALSIZE \n "
			+ "    from (select t.dept_code,t.emp_code, to_char(t.work_date, 'yyyymm') ym \n "
			+ "            from TI_TCAS_SPMS_SCHEDULE t, \n "
			+				SQL_FOR_QUERY_RECURSIVE_DEPARTMENT
			+ "           where t.dept_code = dept.dept_code \n "
			+ "             and to_char(t.work_date, 'yyyymm') =  :MONTH_ID  \n "
			+ "             and t.position_type = '2' \n "
			+				STRING_DYNAMIC_PARAMETER
			+ "           group by t.dept_code,t.emp_code, to_char(t.work_date, 'yyyymm')) \n ";
	
	public static final String SQL_QUERY_ATTENDANCE_LIST = "" 
			+ " select t.*,ym,e.emp_duty_name \n "
			+ "   from (select dept_code,emp_code, ym, rownum rn \n "
			+ "           from (select t.dept_code,t.emp_code, to_char(t.work_date, 'yyyymm') ym \n "
			+ "                   from TI_TCAS_SPMS_SCHEDULE t, \n "
			+						SQL_FOR_QUERY_RECURSIVE_DEPARTMENT
			+ "                  where t.dept_code = dept.dept_code \n "
			+ "                    and to_char(t.work_date, 'yyyymm') = :MONTH_ID \n "
			+ "                    and t.position_type = '2' \n "
			+ 						STRING_DYNAMIC_PARAMETER
			+ "                  group by t.dept_code,t.emp_code, to_char(t.work_date, 'yyyymm'))) employees, \n "
			+ "        TI_TCAS_SPMS_SCHEDULE t ,\n "
			+ "        tm_oss_employee e \n "
			+ "  where employees.emp_code = t.emp_code \n "
			+ "    and employees.dept_code = t.dept_code \n "
			+ "    and t.emp_code = e.emp_code \n "
			+ "    and employees.ym = to_char(t.work_date, 'yyyymm') \n "
			+ "    and employees.rn > :start \n "
			+ "    and employees.rn <= :limit \n "
			+ "   \n ";
			
}
