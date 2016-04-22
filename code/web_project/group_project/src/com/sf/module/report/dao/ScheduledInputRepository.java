package com.sf.module.report.dao;

public class ScheduledInputRepository {
	public final static String queryScheduledInputStatistical = "select t.*, rownum rn "
			+ " from (select t.year_month, "
			+ " t.hq_code, "
			+ " t.area_code, "
			+ " t.department_code, "
			+ " t.inner_emp inner_employee, "
			+ " t.diurnal_count,"
			+ " t.non_diurnal_count,"
			+ " t.outer_emp outer_employee, "
			+ " t.group_number group_count, "
			+ " t.grouping_number grouping_count, "
			+ " t.class_number class_count, "
			+ " t.confirm_process confirm_process_count, "
			+ " t.sch_confirm_inner_emp sched_confirm_inner_emp_count, "
			+ " t.SCH_DIURNAL_COUNT, "
			+ " t.SCH_NON_DIURNAL_COUNT, "
			+ " t.sch_confirm_outer_emp sched_confirm_outer_emp_count, "
			+ " t.process_sch_inner_emp pro_confirm_inner_emp_count, "
			+ " t.process_sch_outer_emp pro_confirm_outer_emp_count, "
			+ " round(t.sch_complete_rate * 100, 2) scheduling_complete_rate "
			+ " from spms.tt_op_sch_statis_report t "
			+ " order by t.hq_code, t.area_code, t.department_code) t "
			+ " where t.department_code in "
			+ " (SELECT dept_code "
			+ " FROM TM_DEPARTMENT "
			+ " WHERE DELETE_FLG = 0 "
			+ " START WITH dept_code = ? "
			+ " CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE) "
			+ " and t.year_month = ? ";

    public static String getQueryCountSql() {
        return queryScheduledInputStatistical;
    }

    public static String getQueryPageSql() {
        return "select * from (" + queryScheduledInputStatistical + " ) t where t.rn > ? and t.rn <= ?";
    }
}
