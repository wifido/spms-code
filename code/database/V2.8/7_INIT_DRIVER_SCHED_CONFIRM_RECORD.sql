begin
  insert into tt_driver_sched_confirm_record
    (id,
     area_code,
     department_code,
     year_week,
     employee_name,
     employee_code,
     confirm_status,
     confirm_time)
    select seq_driver_scheduled_confirm.nextval, t.*
      from (select dept.area_code,
                   dept.dept_code,
                   t.year_week,
                   emp.emp_name,
                   emp.emp_code,
                   t.confirm_status,
                   max(t.confirm_date)
              from tt_driver_scheduling t,
                   tm_oss_employee      emp,
                   tm_department        dept
             where t.employee_code = emp.emp_code
               and emp.dept_id = dept.dept_id
               and t.department_code = dept.dept_code
               and t.scheduling_type = 1
               and t.confirm_status = 1
               and t.DAY_OF_MONTH >= '20150901'
             group by dept.area_code,
                      dept.dept_code,
                      t.year_week,
                      emp.emp_name,
                      emp.emp_code,
                      t.confirm_status) t;

  commit;
end;
