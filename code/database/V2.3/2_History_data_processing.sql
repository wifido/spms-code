update TT_PB_SHEDULE_BY_MONTH t set  t.work_type = (select e.work_type from tm_oss_employee e where e.emp_code = t.emp_code
), t.emp_name = (select e.emp_name from tm_oss_employee e where e.emp_code = t.emp_code);


update TT_PB_SHEDULE_BY_MONTH_log t set  t.work_type = (select e.work_type from tm_oss_employee e where e.emp_code = t.emp_code
), t.emp_name = (select e.emp_name from tm_oss_employee e where e.emp_code = t.emp_code);

commit;