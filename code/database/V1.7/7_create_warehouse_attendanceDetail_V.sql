create or replace view v_scheduled_time_detail as
select spms.emp_code,
       spms.dept_code,
       spms.work_date,
       wmsys.wm_concat(base.start1_time || '-' ||
                                      base.end1_time) schedule_code
  from (select spms.*, sc.department_code, sc.scheduling_code, d.dept_id
          from ti_tcas_spms_schedule spms,
               tt_schedule_daily     sc,
               tm_department         d
         where spms.emp_code = sc.employee_code(+)
           and spms.dept_code = d.dept_code
           and to_char(spms.work_date, 'yyyymmdd') = sc.day_of_month(+)
           and spms.position_type = '2'
        ) spms,
       tm_pb_schedule_base_info base,
       tm_department d
 where spms.dept_code = d.dept_code
   and spms.scheduling_code = base.schedule_code(+)
   and spms.dept_id = base.dept_id(+)
 group by spms.emp_code, spms.dept_code, spms.work_date;