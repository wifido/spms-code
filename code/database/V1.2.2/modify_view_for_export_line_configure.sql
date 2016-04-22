create or replace view v_driver_export_line_configure as
select configure.id as configure_id,
       configure.code,
       configure.month,
       configure.department_code,
       config_dept.area_code,
       config_dept.hq_code,
       config_dept.parent_dept_code,
       configure.department_code || '-' || configure.code as configure_code,
       config_dept.dept_name,
       configure.valid_status,
       decode(configure.valid_status, 1, '有效', '无效') as configure_valid_status_lable,
       configure.type as type,
       substr(line.start_time, 0, 2) || ':' || substr(line.start_time, 3) as start_time,
       substr(line.end_time, 0, 2) || ':' || substr(line.end_time, 3) as end_time,
       line.belong_zone_code || '/' || belong_dept.dept_name as belong_department,
       line.source_code || '/' || source_dept.dept_name as source_department,
       line.destination_code || '/' || destination_dept.dept_name as destination_department,
       line.vehicle_number,
       line.vehicle_type,
       line.input_type,
       line.valid_status as line_valid_status,
       decode(line.valid_status, 1, '有效', '无效') as line_valid_status_lable,
       line.optimize_line_code
  from tt_driver_line_configure   configure,
       tt_driver_line_configure_r cr,
       tm_driver_line             line,
       tm_department              config_dept,
       tm_department              belong_dept,
       tm_department              source_dept,
       tm_department              destination_dept
 where configure.id = cr.configure_id
   and cr.line_id = line.id
   and configure.department_code = config_dept.dept_code
   and line.source_code = source_dept.dept_code
   and line.destination_code = destination_dept.dept_code
   and line.belong_zone_code = belong_dept.dept_code
 order by configure.code,configure.month, cr.sort;
