package com.sf.module.warehouse.dao;

public class WarehouseSchedulingHql {

	public final static String GET_EMPLOYEE_SQL = "  select t.emp_code, \n "
			+ "          t.emp_id, \n "
			+ "          t.emp_name, \n "
			+ "          t.emp_duty_name, \n "
			+ "          t.dept_id, \n "
			+ "          decode_emp_worktype(t.work_type) work_type, \n "
			+ "          t.email, \n "
			+ "          t.dimission_dt, \n "
			+ "          t.emp_post_type, \n "
			+ "          t.transfer_date, \n "
			+ "          t.date_from, \n "
			+ "          t.is_have_commission, \n "
			+ "          t.sf_date, \n "
			+ "          listagg(d.dept_code, ',') within GROUP(order by t.emp_id) dynamic_depts \n "
			+ "     from tm_oss_employee t, tt_warehouse_emp_dept_r r, tm_department d \n "
			+ "    where t.emp_code = r.emp_code(+) \n "
			+ "      and r.dept_id = d.dept_id(+) \n "
			+ "      and t.emp_code = ? \n " + "    group by t.emp_id, \n "
			+ "             t.emp_code, \n " + "             t.emp_id, \n "
			+ "             t.emp_name, \n "
			+ "             t.emp_duty_name, \n "
			+ "             t.dept_id, \n " + "             t.work_type, \n "
			+ "             t.email, \n " + "             t.dimission_dt, \n "
			+ "             t.emp_post_type, \n "
			+ "          	t.transfer_date, \n "
			+ "          	t.date_from, \n "
			+ "             t.is_have_commission, \n "
			+ "             t.sf_date \n ";

	public final static String DELETE_EMPLOYEE_SCHEDULING_SQL = " delete  from tt_schedule_daily t "
			+ " where t.employee_code  = ?   "
			+ " and t.day_of_month  in ( :day_of_month ) "
			+ " and t.emp_post_type  = '3' ";

	public final static String SQL_UPDATE_SCHEDULING_MODIFY_INFO = " update tt_schedule_daily t set t.modified_time = sysdate ," +
			" t.modified_employee_code = ? " +
			" where t.employee_code= ? " +
			" and t.month_id = ? " +
			" and t.emp_post_type = '3' ";
	
	public final static String CHECK_IS_SCHEDULING_SQL = " select t.*\n"
			+ "   from tt_schedule_daily t\n"
			+ "  where t.employee_code = ? \n"
			+ "    and t.department_code != ? \n"
			+ "    and t.day_of_month = ? \n"
			+ "    and t.scheduling_code != '休' \n";
	
	public final static String SQL_FOR_COUNT_NUMBER_OF_SCHEDULING = ""
			+ " select count(distinct t.day_of_month) totalSize \n "
			+ "   from tt_schedule_daily t \n "
			+ "  where t.employee_code = ? \n "
			+ "    and t.day_of_month in (:dayOfMonth) \n "
			+ "    and t.emp_post_type = '3' \n "
			+ "  \n ";

	public final static String QUERY_EMPLOYEE_DYNAMIC_DEPT_SQL = " select t.dept_id from "
			+ " tt_warehouse_emp_dept_r  t  where t.emp_code = ?";

	public final static String QUERY_SCHEDULING_INFORMATION_COUNT = "select count(distinct tm_oss_employee.emp_code) totalSize\n "
			+ "  from tm_oss_employee         tm_oss_employee,\n "
			+ "       tt_warehouse_emp_dept_r er,\n "
			+ "       tm_department           d,\n "
			+ "       tm_department           erd,\n "
			+ "       (SELECT dept_code \n "
			+ "       FROM TM_DEPARTMENT	\n "
			+ "       WHERE DELETE_FLG = 0	\n " 
			+ "        ${deptCode}) DEPART \n "
			+ " where tm_oss_employee.emp_code = er.emp_code(+)\n "
			+ "   and tm_oss_employee.dept_id = d.dept_id\n "
			+ "   and er.dept_id = erd.dept_id(+)\n "
			+ "   and tm_oss_employee.emp_post_type = '3'\n "
		    + "	  and (d.dept_code in \n   "
			+ "        (DEPART.DEPT_CODE) or   \n "
			+ "         erd.dept_code in   \n "
			+ "         (DEPART.DEPT_CODE))  \n  "
			+ "   and tm_oss_employee.emp_code in\n "
			+ "       (select distinct s.employee_code\n "
			+ "          from tt_schedule_daily s\n "
			+ "         where s.month_id = :MONTH_ID" 
			+ "         and s.emp_post_type = '3' ) ${dynamicParameters} \n ";

	public final static String QUERY_SCHEDULING_INFORMATION_SQL =  "select tt_schedule_daily.id,\n "
            + "       tt_schedule_daily.month_id,\n "
            + "       tt_schedule_daily.day_of_month,\n "
            + "       tt_schedule_daily.employee_code,\n "
            + "       tt_schedule_daily.scheduling_code,\n "
            + "       to_char(tt_schedule_daily.modified_time,'yyyy-MM-dd HH24:mi:ss') modified_time, \n "
            + "       tt_schedule_daily.modified_employee_code,\n "
            + "       tm_oss_employee.emp_name,\n "
            + "       tm_department.area_code,\n "
            + "       tt_schedule_daily.department_code motor_dept,\n "
            + "       tm_department.dept_code,\n "
            + "       decode_emp_worktype(tm_oss_employee.work_type) work_type_name,\n "
            + "       tm_oss_employee.emp_duty_name\n "
            + "  from tt_schedule_daily tt_schedule_daily,\n "
            + "       (select emp_code\n "
            + "          from (select emp_code, rownum rn\n "
            + "                  from (select distinct tm_oss_employee.emp_code\n "
            + "                          from tm_oss_employee         tm_oss_employee,\n "
            + "                               tt_warehouse_emp_dept_r er,\n "
            + "                               tm_department           d,\n "
            + "                               tm_department           erd,\n "
			+ "                               (SELECT dept_code \n "
			+ "                      			 FROM TM_DEPARTMENT	\n "
			+ "                 			     WHERE DELETE_FLG = 0	\n " 
			+ "                  			    ${deptCode}) DEPART \n "
            + "                         where tm_oss_employee.emp_code = er.emp_code(+)\n "
            + "                           and tm_oss_employee.dept_id = d.dept_id\n "
            + "                           and er.dept_id = erd.dept_id(+)\n "
            + "                           and tm_oss_employee.emp_post_type = '3'\n "
            + "	  						  and (d.dept_code in    "
			+ "                               (DEPART.DEPT_CODE) or    "
			+ "                               erd.dept_code in    "
			+ "                               (DEPART.DEPT_CODE))    "
            + "                           and tm_oss_employee.emp_code in\n "
            + "                               (select distinct s.employee_code\n "
            + "                                  from tt_schedule_daily s\n "
            + "                                 where s.month_id = :MONTH_ID\n "
            + "                                  and s.emp_post_type = '3' )\n "
            + "                           ${dynamicParameters} )) t\n "
            + "         where rn > :start\n "
            + "           and rn <= :limit\n "
            + "         order by t.emp_code) employee,\n "
            + "       tm_oss_employee tm_oss_employee,\n "
            + "       tm_department tm_department\n "
            + " where employee.emp_code = tt_schedule_daily.employee_code\n "
            + "   and employee.emp_code = tm_oss_employee.emp_code\n "
            + "   and tm_oss_employee.dept_id = tm_department.dept_id \n " 
            + "	  and tt_schedule_daily.month_id = :MONTH_ID\n "
            + "	  and tt_schedule_daily.emp_post_type = '3' \n "
            + " order by tt_schedule_daily.modified_time desc \n ";

	public final static String QUERY_NOT_SCHEDULING_EMPLOYEE_TOTAL_SQL = " select count(distinct tm_oss_employee.emp_code) totalSize \n "
			+ "    from tm_oss_employee         tm_oss_employee, \n "
			+ " 		tt_warehouse_emp_dept_r er, \n "
			+ " 		tm_department           d, \n "
			+ " 		tm_department           erd \n "
			+ "   where tm_oss_employee.emp_code = er.emp_code(+) \n "
			+ " 	and tm_oss_employee.dept_id = d.dept_id \n "
			+ " 	and er.dept_id = erd.dept_id(+) \n "
			+ " 	and (tm_oss_employee.dimission_dt is null or  \n "
			+ "          tm_oss_employee.dimission_dt > sysdate)  \n "
			+ " 	and tm_oss_employee.emp_post_type = '3' \n "
			+ " 	and (d.dept_code = :departmentCode or erd.dept_code = :departmentCode) \n "
			+ " 	and tm_oss_employee.emp_code not in \n "
			+ " 		(select distinct s.employee_code \n "
			+ " 		   from tt_schedule_daily s \n "
			+ " 		  where s.month_id = :month \n "
			+ " 			and s.emp_post_type = '3'  \n "
			+ " 			and s.department_code = :departmentCode ) \n ";

	public final static String QUERY_NOT_SCHEDULING_EMPLOYEE_SQL = " select * \n "
			+ "   from (select d.dept_code, \n "
			+ "                t.emp_code, \n "
			+ "                t.emp_name, \n "
			+ "                t.emp_duty_name, \n "
			+ "                t.persk_txt, \n "
			+ "                t.dimission_dt, \n "
			+ "                t.sf_date, \n "
			+ "                t.transfer_date, \n "
			+ "                t.date_from, \n "
			+ "                decode_emp_worktype(t.work_type) work_type_name, \n "
			+ "                rownum rn \n "
			+ "           from tm_oss_employee t, tm_department d \n "
			+ "          where t.dept_id = d.dept_id \n "
			+ "            and t.emp_code in \n "
			+ "                (select distinct tm_oss_employee.emp_code \n "
			+ "                   from tm_oss_employee        tm_oss_employee, \n "
			+ "                        tt_warehouse_emp_dept_r er, \n "
			+ "                        tm_department           d, \n "
			+ "                        tm_department           erd \n "
			+ "                  where tm_oss_employee.emp_code = er.emp_code(+) \n "
			+ "                    and tm_oss_employee.dept_id = d.dept_id \n "
			+ "                    and er.dept_id = erd.dept_id(+) \n "
			+ " 				   and (tm_oss_employee.dimission_dt is null or  \n "
			+ "          				tm_oss_employee.dimission_dt > sysdate)  \n "
			+ "                    and tm_oss_employee.emp_post_type = '3' \n "
			+ "                    ${parameter1} \n"	
			+ "                    and (d.dept_code = :departmentCode or erd.dept_code = :departmentCode ) \n "
			+ "                    and tm_oss_employee.emp_code not in \n "
			+ "                        (select distinct s.employee_code \n "
			+ "                           from tt_schedule_daily s \n "
			+ "                          where s.month_id = :month \n "
			+ "                            and s.emp_post_type = '3' \n"
			+ "                            and s.department_code = :departmentCode))) TM_OSS_EMPLOYEE  \n "
			+ "  where rn > :start \n "
			+ "    and rn < :limit \n ";

	public final static String QUERY_EXIST_SCHEDULING_SQL = " select  tt_schedule_daily.employee_code "
			+ "   from tt_schedule_daily tt_schedule_daily "
			+ "  where tt_schedule_daily.emp_post_type = '3' "
			+ "    and tt_schedule_daily.month_id = :monthId "
			+ "    and tt_schedule_daily.employee_code = :empCode ";
	
	public final static String SQL_DELETE_SCHEDULING_BY_MONTH = " delete from tt_schedule_daily t "
			+ "	where t.month_id=:monthId and t.employee_code=:empCode and t.department_code=:deptCode and t.emp_post_type = '3' ";
	
	public final static String SQL_QUERY_SCHEDULING_BY_SPECIFIED_PARAMETERS = " select  tt_schedule_daily.* "
			+ "   from tt_schedule_daily tt_schedule_daily "
			+ "  where tt_schedule_daily.emp_post_type = '3' "
			+ "    and tt_schedule_daily.month_id = ? "
			+ "    and tt_schedule_daily.employee_code = ? " +
			" 	   and tt_schedule_daily.department_code = ?   order by tt_schedule_daily.day_of_month ";
	
	
	public final static String SQL_QUERY_DEPARTMENT_CODE_BY_RECURSIVE = ""
					+ "   select d.dept_code \n "
					+ "     from TM_DEPARTMENT d \n "
					+ "    where d.DELETE_FLG = 0 \n "
					+ "    START WITH d.dept_code = :departmentCode \n "
					+ "   CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE \n ";
	
	public final static String SQL_QUERY_ALL_EMPLOYEE_CODE_BY_DEPARTMENT_CODE = ""
			+ " select distinct e.emp_code \n "
			+ "   from tm_oss_employee e, \n "
			+ "        tm_department d, \n "
			+ "        ("+SQL_QUERY_DEPARTMENT_CODE_BY_RECURSIVE+") dr \n "
			+ "  where e.dept_id = d.dept_id \n "
			+ "    and d.dept_code = dr.dept_code \n "
			+ "    and e.emp_post_type = '3' \n "
			+ "    and (e.dimission_dt is null or e.dimission_dt > sysdate) \n "
			+ " union \n "
			+ " select distinct e.emp_code \n "
			+ "   from tm_oss_employee e, tt_warehouse_emp_dept_r er, tm_department d \n "
			+ "  where e.emp_code = er.emp_code \n "
			+ "    and er.dept_id = d.dept_id \n "
			+ "    and e.emp_post_type = '3' \n "
			+ "    and (e.dimission_dt is null or e.dimission_dt > sysdate) \n "
			+ "    and d.dept_code = :departmentCode \n ";
	
	public final static String SQL_QUERY_EMPLOYEE_CODE_WITH_SCHULINGED = ""
					+ " select distinct e.emp_code \n "
					+ "       from tm_oss_employee e, tt_schedule_daily s \n "
					+ "      where s.employee_code = e.emp_code \n "
					+ "        and s.month_id = :month \n "
					+ "        and s.emp_post_type = '3' \n ";
	
	
	public final static String SQL_QUERY_NO_SCHEDULING_EMPLOYEE_FOR_EXPORT =""
			+ "   		select d.dept_code, \n "
			+ "                t.emp_code, \n "
			+ "                t.emp_name, \n "
			+ "                t.emp_duty_name, \n "
			+ "                t.persk_txt, \n "
			+ "                t.dimission_dt, \n "
			+ "                t.sf_date, \n "
			+ "                t.transfer_date, \n "
			+ "                t.date_from, \n "
			+ "                decode_emp_worktype(t.work_type) work_type_name \n "
			+ "           from  " 
			+ "  			 ("+SQL_QUERY_ALL_EMPLOYEE_CODE_BY_DEPARTMENT_CODE+" minus ("+SQL_QUERY_EMPLOYEE_CODE_WITH_SCHULINGED+" INTERSECT "+SQL_QUERY_ALL_EMPLOYEE_CODE_BY_DEPARTMENT_CODE+") ) e, "
			+ "    			tm_department d, tm_oss_employee t \n "
			+ "          where t.emp_code = e.emp_code and t.dept_id =  d.dept_id \n "
			+ "           order by d.dept_code \n ";
	
	public final static String SQL_CHECK_USER_PERMISSIONS = " SELECT count(*) TOTALSIZE "
			+ " FROM (SELECT DEPT_ID FROM TM_DEPARTMENT "
			+ " WHERE DELETE_FLG = 0 and DEPT_CODE = :dept_code) D,"
			+ " TS_USER_DEPT UD WHERE D.DEPT_ID = UD.DEPT_ID AND UD.USER_ID = :user_id ";
}
