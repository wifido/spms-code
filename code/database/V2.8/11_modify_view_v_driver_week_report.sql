create or replace view v_driver_week_report as
select distinct dept.area_code,
            dept.dept_code,
            t.year_week,
            emp.emp_name,
            emp.emp_code,
            t.confirm_status,
            '' confirm_date
       from tt_driver_scheduling t, tm_oss_employee emp, tm_department dept
     where t.employee_code = emp.emp_code
         /*and emp.dept_id = dept.dept_id*/
         and t.department_code = dept.dept_code
        and t.scheduling_type = 1
        and t.confirm_status = 0
     union all
     select t.area_code,
            t.department_code,
            t.year_week,
            t.employee_name,
            t.employee_code,
            t.confirm_status,
            to_char(t.confirm_time, 'yyyy-MM-dd HH24:mi:ss') confirm_date
       from Tt_Driver_Sched_Confirm_Record t;
