--查询线路配置视图
create or replace view v_dirver_line_cofnigure as
select startline.id,
       startline.code,
       startline.department_code,
	   startline.department_code||'-'||startline.code configure_code,
       startline.valid_status,
       startline.type,
       startline.create_tm,
       startline.modified_tm,
       startline.create_emp_code,
       startline.modified_emp_code,
       startline.start_time,
       startline.source_code,
       startline.source_name,
       startline.month,
       lastline.end_time,
       lastline.destination_name,
       lastline.destination_code
  from (select c.*, l.start_time, l.source_code, d.dept_name as source_name
          from tt_driver_line_configure   c,
               tt_driver_line_configure_r cr,
               tm_department              d,
               tm_driver_line             l
         where c.id = cr.configure_id
           and cr.line_id = l.id
           and l.source_code = d.dept_code
           and (c.id, cr.sort) in (select c.id, min(cr.sort) min_sort
                                     from tt_driver_line_configure   c,
                                          tt_driver_line_configure_r cr
                                    where c.id = cr.configure_id
                                    group by c.id)) startline,

       (select c.code,
               c.department_code,
               c.month,
               l.end_time,
               l.destination_code,
               d.dept_name destination_name
          from tt_driver_line_configure   c,
               tt_driver_line_configure_r cr,
               tm_department              d,
               tm_driver_line             l
         where c.id = cr.configure_id
           and cr.line_id = l.id
           and l.destination_code = d.dept_code
           and (c.id, cr.sort) in (select c.id, max(cr.sort) max_sort
                                     from tt_driver_line_configure   c,
                                          tt_driver_line_configure_r cr
                                    where c.id = cr.configure_id
                                    group by c.id)) lastline

 where startline.code = lastline.code
   and startline.department_code = lastline.department_code
   and startline.month = lastline.month;