package com.sf.module.driver.dao;

import static com.sf.module.common.util.StringUtil.isNotBlank;

import java.util.Map;

public class DriverSchedulingRepository {
	private static final String COL_EMPLOYEE_CODE = "employeeCode";
	private static final String COL_EMPLOYEE_NAME = "employeeName";
	private static final String NAME_DAYNAMIC_PARAMETER = "${dynamicParameters}";
	private static final String CONFIRM_STATUS_PARAMETER = "${dynamicConfirmStatusCondition}";
	public static final String COL_DEPARTMENT_CODE = "departmentCode";
	public static final String COL_YEARWEEK = "yearWeek";
	public static final String COL_CONFIRM_STATUS = "confirm_status";
	public static final String COL_EMP_CODE = "emp_code";
	
	public static final String SQL_QUERY_DRIVER_SCHEDULING_COUNT = "select count(distinct employee_code) totalSize \n "
		            + "  from (select t.employee_code, \n "
		            + "               t.year_month, \n "
		            + "               t.department_code \n "
		            + "          from tt_driver_scheduling t,tm_oss_employee employee \n "
		            + "         where t.employee_code = employee.emp_code \n "
		            + "		   and t.department_code in \n"
		            + "                     (SELECT dept_code \n "
		            + "                          FROM TM_DEPARTMENT \n "
		            + "                         WHERE (DELETE_FLG = 0) \n "
		            + "                         START WITH DEPT_ID = \n "
		            + "                                    (select d.dept_id \n "
		            + "                                       from TM_DEPARTMENT d \n "
		            + "                                      where d.dept_code = :departmentCode) \n "
		            + "                        CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE) \n "
		            + "                 ${dynamicParameters} \n "
		            + "         group by t.employee_code, \n "
		            + "                  t.year_month, \n "
		            + "                  t.department_code \n "
		            + "         order by t.employee_code) \n ";

    public static final String SQL_QUERY_DRIVER_SCHEDULING = "select department.area_code, \n "
                    + "       department.dept_code, \n "
                    + "       department.dept_name dept_desc, \n "
                    + "       employee.emp_name, \n "
                    + "       employee.emp_code, \n "
                    + "       scheduling.year_week, \n "
                    + "       scheduling.year_month, \n "
                    + "       scheduling.day_of_month, \n "
                    + "       scheduling.work_type, \n "
                    + "       scheduling.configure_code, \n "
                    + "       app.APPLY_TYPE, \n "
                    + "       to_char(scheduling.create_time,'YYYY-MM-DD HH24:mi:ss') create_time, \n "
                    + "       scheduling.created_employee_code, \n "
                    + "       to_char(scheduling.modified_time,'YYYY-MM-DD HH24:mi:ss') modified_time, \n "
                    + "       scheduling.modified_employee_code, \n "
                    + "       to_char(department.dept_id) dept_id, \n "
                    + "       decode(area_department.dept_desc, '', '', area_department.dept_desc) department_area_desc, \n "
                    + "       scheduling.scheduling_type, \n"
					+ "       configure.type configureType, \n"
					+ "       decode(configure.attendance_duration, null, 0,configure.attendance_duration) attendance_duration, \n"
					+ "       decode(configure.drive_duration, null, 0,configure.drive_duration) drive_duration \n"
					+ "  from (select t.employee_code, \n "
                    + "               t.year_month, \n "
                    + "               t.department_code, \n "
                    + "               rownum rn \n "
                    + "          from (select t.employee_code, \n "
                    + "                       t.year_month, \n "
                    + "                       t.department_code \n "
                    + "                  from tt_driver_scheduling t,tm_oss_employee employee \n "
                    + "                 where t.employee_code = employee.emp_code \n "
                    + "					and t.department_code in  \n"
                    + "                     (SELECT dept_code \n "
                    + "                          FROM TM_DEPARTMENT \n "
                    + "                         WHERE (DELETE_FLG = 0) \n "
                    + "                         START WITH DEPT_ID = \n "
                    + "                                    (select d.dept_id \n "
                    + "                                       from TM_DEPARTMENT d \n "
                    + "                                      where d.dept_code = :departmentCode) \n "
                    + "                        CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE) \n "
//                    + "					and employee.emp_post_type = 5 \n "
                    + "                 ${dynamicParameters} \n "
                    + "                 group by t.employee_code, \n "
                    + "                          t.year_month, \n "
                    + "                          t.department_code \n "
                    + "                 order by t.employee_code) t) t, \n "
                    + "       tm_oss_employee employee, \n "
                    + "       tt_driver_scheduling scheduling, \n "
                    + "       tm_department department, \n "
                    + "       tm_department area_department, \n "
                    + "       v_dirver_line_cofnigure configure, \n "
					+ "       (select app.* from tt_driver_apply app, \n "
					+ "       		(select max(y.apply_id) apply_id, y.day_of_month from tt_driver_apply y \n "
					+ "       			where y.status = 2 group by y.day_of_month, \n "
					+ "       				y.apply_employee_code, \n "
					+ "       				y.department_code) t where app.apply_id = t.apply_id) app \n "
                    + " where t.employee_code = employee.emp_code \n "
                    + "   and t.employee_code = scheduling.employee_code \n "
                    + "   and t.year_month = scheduling.year_month \n "
                    + "   and t.department_code = scheduling.department_code \n "
                    + "   and t.department_code = department.dept_code \n "
                    + "   and department.area_code = area_department.dept_code(+) \n "
                    + "   and scheduling.year_month = configure.month(+) \n "
/*        			+ "   and scheduling.department_code = configure.department_code(+) \n "*/
        			+ "   and scheduling.configure_code = configure.configure_code(+)  \n "
                    + "   and scheduling.scheduling_type = 1 \n "
    				+ "   ${dynamicParameters}\n "
    				+ "   and scheduling.employee_code = app.apply_employee_code(+) \n"
    				+ "	  and scheduling.day_of_month = app.day_of_month(+) \n";

    public static final String SQL_QUERY_NO_SCHEDULING_EMPLOYEE= ""
    				+ " select e.emp_code, \n "
					+ "        e.emp_name, \n "
					+ "        d.dept_name, \n "
					+ "        d.dept_code, \n "
					+ "        d.area_code, \n "
					+ "        e.work_type \n "
					+ "   from tm_oss_employee e, tm_department d \n "
					+ "  where e.dept_id = d.dept_id \n "
					+ "		and (e.dimission_dt is null or \n "
					+ "	       e.dimission_dt > sysdate) \n "
					+ "    	and e.emp_post_type = '5' \n "
					+ NAME_DAYNAMIC_PARAMETER
					+ "    and d.dept_id in \n "
					+ "        (select dept_id \n "
					+ "           from tm_department \n "
					+ "          where (delete_flg = 0) \n "
					+ "          start with dept_code = :departmentCode \n "
					+ "         connect by prior dept_code = parent_dept_code) \n "
					+ " minus \n "
					+ " select e.emp_code, \n "
					+ "        e.emp_name, \n "
					+ "        d.dept_name, \n "
					+ "        d.dept_code, \n "
					+ "        d.area_code, \n "
					+ "        e.work_type \n "
					+ "   from tm_oss_employee e, tt_driver_scheduling s, tm_department d \n "
					+ "  where e.emp_code = s.employee_code \n "
					+ "		and (e.dimission_dt is null or \n "
					+ "	       e.dimission_dt > sysdate) \n "
					+ "    and e.emp_post_type = '5' \n "
					+ "    and s.year_week = :year_week \n "
					+ "    and e.dept_id = d.dept_id \n "
					+ "    and s.department_code = d.dept_code \n "
					+ NAME_DAYNAMIC_PARAMETER
					+ "    and d.dept_id in \n "
					+ "        (select dept_id \n "
					+ "           from tm_department \n "
					+ "          where (delete_flg = 0) \n "
					+ "          start with dept_code = :departmentCode  \n "
					+ "         connect by prior dept_code = parent_dept_code) \n "
					+ "  group by e.emp_code, \n "
					+ "           e.emp_name, \n "
					+ "           d.dept_name, \n "
					+ "           d.dept_code, \n "
					+ "           d.area_code, \n "
					+ "           e.work_type \n ";

    public static final String SQL_QUERY_NO_SCHEDULING_EMPLOYEE_COUNT= ""
					+ " select e.emp_code \n "
					+ "   from tm_oss_employee e, tm_department d \n "
					+ "  where e.dept_id = d.dept_id \n "
					+ "		and (e.dimission_dt is null or \n "
					+ "	       e.dimission_dt > sysdate) \n "
					+ "    and e.emp_post_type = '5' \n "
					+ NAME_DAYNAMIC_PARAMETER
					+ "    and d.dept_id in \n "
					+ "        (select dept_id \n "
					+ "           from tm_department \n "
					+ "          where (delete_flg = 0) \n "
					+ "          start with dept_code = :departmentCode \n "
					+ "         connect by prior dept_code = parent_dept_code) \n "
					+ " minus \n "
					+ " select e.emp_code \n "
					+ "   from tm_oss_employee e, tt_driver_scheduling s, tm_department d \n "
					+ "  where e.emp_code = s.employee_code \n "
					+ "		and (e.dimission_dt is null or \n "
					+ "	       e.dimission_dt > sysdate) \n "
					+ "    and e.emp_post_type = '5' \n "
					+ "    and s.year_week = :year_week \n "
					+ "    and e.dept_id = d.dept_id \n "
					+ "    and s.department_code = d.dept_code \n "
					+ NAME_DAYNAMIC_PARAMETER
					+ "    and d.dept_id in \n "
					+ "        (select dept_id \n "
					+ "           from tm_department \n "
					+ "          where (delete_flg = 0) \n "
					+ "          start with dept_code = :departmentCode  \n "
					+ "         connect by prior dept_code = parent_dept_code) \n "
					+ "  group by e.emp_code \n ";

	public static String buildDynamicParamForQueryNoSchedulingEmployee(String sql,Map<String, String> params) {
		StringBuilder danymicParams = new StringBuilder();
		if (isNotBlank(params.get(COL_EMPLOYEE_CODE))) {
			danymicParams.append(" and e.emp_code = :employeeCode \n");
		}
		if (isNotBlank(params.get(COL_EMPLOYEE_NAME))) {
			danymicParams.append(" and e.emp_name like :employeeName \n");
			params.put(COL_EMPLOYEE_NAME, "%" + params.get(COL_EMPLOYEE_NAME) + "%");
		}
		return sql.replace(NAME_DAYNAMIC_PARAMETER, danymicParams.toString());
	}

    public static String buildQueryConfigureSchedulingAllLine() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("select line.input_type,")
                .append("line.start_time,")
                .append("line.end_time,")
                .append("line.optimize_line_code,")
                .append("line.vehicle_type,")
                .append("line.source_code,")
                .append("line.belong_zone_code,")
                .append("source.dept_name source_name,")
                .append("belong.dept_name belong_name,")
                .append("line.destination_code,")
                .append("destination.dept_name   destination_name,")
                .append("line.vehicle_number,")
                .append("line.valid_status")
                .append(" from tm_driver_line line,")
                .append(" tm_department  belong,")
                .append(" tm_department  source,")
                .append(" tm_department  destination")
                .append(" where line.belong_zone_code = belong.dept_code")
                .append(" and line.source_code = source.dept_code")
                .append(" and line.destination_code = destination.dept_code")
                .append(" and line.id in (select r.line_id")
                .append(" from tt_driver_line_configure   configure,")
                .append(" tt_driver_line_configure_r r")
                .append(" where configure.id = r.configure_id")
                .append(" and configure.code = :configureCode")
                .append(" and configure.department_code in ")
                .append(" (SELECT DEPT_CODE")
                .append(" FROM TM_DEPARTMENT")
                .append(" WHERE (DELETE_FLG = 0)")
                .append(" START WITH DEPT_ID = :departmentId")
                .append(" CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE))")
                .append(" order by line.start_time");

        return stringBuilder.toString();
    }

    public static String QUERY_DRIVER_SCHEDULED_BY_WEEK_SIZE = "select count(1) \n "
            + "from (select g.department_code, g.employee_code, g.year_week \n "
            + "from tt_driver_scheduling g \n "
            + "where g.scheduling_type = 1 \n "
            + CONFIRM_STATUS_PARAMETER
            + "group by g.department_code, g.employee_code, g.year_week) t, \n "
            + "tm_oss_employee employee, \n "
            + " tm_department dept \n"
            + "where t.employee_code = employee.emp_code \n "
            + " and t.department_code = dept.dept_code \n "
            + "and t.department_code in \n "
            + "(SELECT dept_code \n "
            + "FROM TM_DEPARTMENT \n "
            + "WHERE (DELETE_FLG = 0) \n "
            + "START WITH DEPT_ID = (select d.dept_id \n "
            + "from TM_DEPARTMENT d \n "
            + "where d.dept_code = ?) \n "
            + "CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE) \n "
//            + "and employee.emp_post_type = 5 \n "
            + "and t.year_week = ? \n "
            + NAME_DAYNAMIC_PARAMETER;

    public static String QUERY_DRIVER_SCHEDULED_BY_WEEK = "select t.employee_code emp_code, \n "
            + "       t.emp_name, \n "
            + "       scheduling.year_week, \n "
            + "       scheduling.year_month, \n "
            + "       scheduling.day_of_month, \n "
            + "       scheduling.scheduling_type, \n "
            + "       scheduling.work_type, \n "
            + "       scheduling.configure_code, \n "
            + "       app.APPLY_TYPE, \n "
            + "       to_char(scheduling.create_time,'YYYY-MM-DD HH24:mi:ss') create_time, \n "
            + "       scheduling.created_employee_code, \n "
            + "       to_char(scheduling.modified_time,'YYYY-MM-DD HH24:mi:ss') modified_time, \n "
            + "       scheduling.modified_employee_code, \n "
            + "       department.area_code, \n "
            + "       department.dept_code, \n "
            + "       department.dept_name dept_desc, \n "
            + "       scheduling.confirm_status, \n "
            + "       to_char(scheduling.confirm_date,'YYYY-MM-DD HH24:mi:ss') confirm_date, \n "
            + "       to_char(department.dept_id) dept_id, \n "
            + "       decode(area_department.dept_name, '', '', area_department.dept_name) department_area_desc, \n "
			+ "       scheduling.scheduling_type, \n "
			+ "       configure.type configureType \n "
            + "  from tt_driver_scheduling scheduling, \n "
            + "       (select t.employee_code, \n "
            + "               t.year_week, \n "
            + "               t.department_code, \n "
            + "               t.emp_name, \n "
            + "               rownum rn \n "
            + "          from (select t.employee_code, \n "
            + "                       t.year_week, \n "
            + "                       t.department_code, \n "
            + "                       employee.emp_name \n "
            + "                  from (select g.department_code, g.employee_code, g.year_week \n "
            + "                          from tt_driver_scheduling g \n "
            + "                         where g.scheduling_type = 1 \n "
            + CONFIRM_STATUS_PARAMETER
            + "                         group by g.department_code, \n "
            + "                                  g.employee_code, \n "
            + "                                  g.year_week) t, \n "
            + "                       tm_oss_employee employee, \n "
            + "						  tm_department dept \n"	
            + "                 where t.employee_code = employee.emp_code \n "
            + "                   and t.department_code = dept.dept_code \n"
            + "                   and t.department_code in \n "
            + "                       (SELECT dept_code \n "
            + "                          FROM TM_DEPARTMENT \n "
            + "                         WHERE (DELETE_FLG = 0) \n "
            + "                         START WITH DEPT_ID = \n "
            + "                                    (select d.dept_id \n "
            + "                                       from TM_DEPARTMENT d \n "
            + "                                      where d.dept_code = ?) \n "
            + "                        CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE) \n "
//            + "                   and employee.emp_post_type = 5 \n "
            + NAME_DAYNAMIC_PARAMETER
            + "                   and t.year_week = ? \n "
            + "                 order by t.department_code, t.employee_code, t.year_week) t) t, \n "
            + "       tm_department department, \n "
            + "       tm_department area_department, \n "
			+ "		  v_dirver_line_cofnigure configure, \n "
			+ "       (select app.* from tt_driver_apply app, \n "
			+ "       		(select max(y.apply_id) apply_id, y.day_of_month from tt_driver_apply y \n "
			+ "       			where y.status = 2 group by y.day_of_month, \n "
			+ "       				y.apply_employee_code, \n "
			+ "       				y.department_code) t where app.apply_id = t.apply_id) app \n "
            + " where scheduling.employee_code = t.employee_code \n "
            + "   and scheduling.year_week = t.year_week \n "
            + "   and scheduling.department_code = t.department_code \n "
            + "   and scheduling.scheduling_type = 1 \n "
            + "   and scheduling.department_code = department.dept_code \n "
            + "   and t.department_code = department.dept_code \n "
            + "   and department.area_code = area_department.dept_code(+) \n "
			+ "   and scheduling.year_month = configure.month(+) \n "
/*			+ "   and scheduling.department_code = configure.department_code(+) \n "*/
			+ "   and scheduling.configure_code = configure.configure_code(+)  \n "
            + NAME_DAYNAMIC_PARAMETER
            + "   and scheduling.employee_code = app.apply_employee_code(+) \n"
			+ "	  and scheduling.day_of_month = app.day_of_month(+) \n"
            + "  and t.rn > ? and t.rn <= ? \n ";
    
    public static final String QUERY_WEEKLY_EXPORT_SQL = " select * "
    		+ " from v_driver_week_report t "
    		+ " where t.dept_code in "
    		+ " (select dept.dept_code "
    		+ " from tm_department dept "
    		+ " where dept.delete_flg = 0 "
    		+ " start with dept.dept_code = :departmentCode "
    		+ " connect by prior dept.dept_code = dept.parent_dept_code) "
    		+ " and t.year_week = :yearWeek" 
    		+ " %s" 
    		+ " order by t.emp_code,t.dept_code,t.confirm_date";
    
	public static final String QUERY_SCHEDULING_PRACTICAL = "select * from report_driver_scheduling t "
			+ " where t.month = :yearMonth"
			+ " and t.department_code in "
			+ " (SELECT DEPT_CODE FROM TM_DEPARTMENT B WHERE DELETE_FLG = 0  START WITH DEPT_CODE = :departmentCode"
			+ " CONNECT BY PRIOR B.DEPT_CODE = B.PARENT_DEPT_CODE)";
	
	public static final String INSERT_SCHEDULING_CONFIRM = " insert into tt_driver_sched_confirm_record"
			+ " select SEQ_driver_scheduled_confirm.Nextval,"
			+ " dept.area_code,"
			+ " dept.dept_code,"
			+ " t.year_week,"
			+ " emp.emp_name,"
			+ " t.employee_code,"
			+ " 1,"
			+ " sysdate"
			+ " from tt_driver_scheduling t,"
			+ " tm_department        dept,"
			+ " tm_oss_employee      emp"
			+ " where t.department_code = dept.dept_code"
			+ " and t.employee_code = emp.emp_code"
			+ " and dept.dept_id = emp.dept_id"
			+ " and t.employee_code = ?"
			+ " and t.year_week = ?"
			+ " and t.scheduling_type = 1"
			+ " and rownum = 1";
	
	public static final String QUERY_SWITCHING_TIME = "select decode(to_char(emp.date_from, 'YYYYMMDD'), null,0,to_char(emp.date_from, 'YYYYMMDD')) date_from,"
			+ "       decode(to_char(emp.transfer_date, 'YYYYMMDD'), null, 0,to_char(emp.transfer_date, 'YYYYMMDD')) transfer_date,"
			+ "       to_char(emp.sf_date, 'YYYYMMDD') sf_date,"
			+ "       decode(to_char(emp.dimission_dt, 'YYYYMMDD'), null,0,to_char(emp.dimission_dt, 'YYYYMMDD')) dimission_dt"
			+ "  from tm_oss_employee emp"
			+ " where emp.emp_code = ?"
			+ "   and emp.dept_id = (select dept.dept_id"
			+ "                        from tm_department dept"
			+ "                       where dept.dept_code = ?)";
	
	public static final String QUERY_EMPLOYEE_CONVERT_DATE = "select decode(to_char(emp.date_from, 'YYYYMMDD'), null,0,to_char(emp.date_from, 'YYYYMMDD')) date_from,"
			+ "       decode(to_char(emp.transfer_date, 'YYYYMMDD'), null, 0,to_char(emp.transfer_date, 'YYYYMMDD')) transfer_date,"
			+ "       to_char(emp.sf_date, 'YYYYMMDD') sf_date,"
			+ "       decode(to_char(emp.dimission_dt, 'YYYYMMDD'), null,0,to_char(emp.dimission_dt, 'YYYYMMDD')) dimission_dt,"
			+ "       dept.dept_code, "
			+ "       emp.last_zno, "
			+ "		  emp.emp_post_type	"
			+ "  from tm_oss_employee emp, tm_department dept "
			+ " where emp.dept_id = dept.dept_id "
			+ "   and emp.emp_code = ? ";
	
	public static final String QUERY_WHETHER_CAN_BE_MODIFIED =   "select count(*) count "
			 + "  from tm_emp_dept_change_record t  "
			 + " where t.id = (select min(id) "
			 + "                 from tm_emp_dept_change_record t "
			 + "                where to_char(t.change_date,'yyyymmdd') > ? "
			 + "                and t.emp_code = ?) "
			 + "   and t.last_zno = ? ";

	public static final String DELETE_SWITCHING_DEPARTMENT_SCHEDULING = "delete tt_driver_scheduling sch "
			+ " where sch.employee_code = :employeeCode and sch.department_code != :departmentCode " + " and sch.day_of_month in (:dayOfMonths)";
	
}
