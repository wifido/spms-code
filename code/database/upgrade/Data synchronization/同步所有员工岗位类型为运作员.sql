
delete tm_oss_employee emp where emp.work_type = 6;

update tm_oss_employee emp set emp.emp_post_type = '1',emp.group_id = '';
commit;