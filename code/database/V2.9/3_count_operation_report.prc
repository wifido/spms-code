create or replace procedure count_operation_report(dayofmonth in varchar) is

  --*************************************************************
  -- author  : hgx
  -- created : 2015-04-16
  -- purpose : 统计运作排班、考勤信息报表
  --
  -- parameter:
  -- name  dayofmonth            type     varchar
  --
  -- modify history
  -- person                    date                        comments
  -- -------------------------------------------------------------
  --
  --*************************************************************

  --1.定义执行序号
  L_CALL_NO NUMBER;

  v_total_emp_num               number;
  v_fulltime_emp_num            number;
  v_not_fulltime_emp_num        number;
  v_out_emp_num                 number;
  v_group_num                   number;
  v_class_num                   number;
  v_total_attendance_num        number;
  v_fulltime_attendance_num     number;
  v_not_fulltime_attendance_num number;
  v_out_attendance_num          number;
  v_total_rest_num              number;
  v_fulltime_rest_num           number;
  v_not_fulltime_rest_num       number;
  v_out_rest_num                number;
  v_total_worktime_count        number(10, 2);
  date_format                   varchar2(20);
  post_type_operation           varchar2(20);
  v_work_type_full_time         varchar2(20);
  v_work_type_not_full_time     varchar2(20);
  v_work_type_out_employee      varchar2(20);
  str_rest                      varchar2(20);
  v_total_scheduling_num        number;
  v_fulltime_scheduling_num     number;
  v_not_fulltime_scheduling_num number;
  v_out_scheduling_num          number;

begin

  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'COUNT_OPERATION_REPORT',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

  date_format               := 'yyyy-mm-dd';
  post_type_operation       := '1';
  v_work_type_full_time     := 'A';
  v_work_type_not_full_time := 'C';
  v_work_type_out_employee  := '6';
  str_rest                  := '休';

  ----删除当天的统计数据
  delete op_attendance_count_report t
   where to_char(t.day_of_month, date_format) = dayofmonth;

  ----遍历运作网点，逐个网点统计
  for dept_row in (select * from op_dept) loop
  
    ----初始化值
    v_total_emp_num               := 0;
    v_fulltime_emp_num            := 0;
    v_not_fulltime_emp_num        := 0;
    v_out_emp_num                 := 0;
    v_group_num                   := 0;
    v_class_num                   := 0;
    v_total_attendance_num        := 0;
    v_fulltime_attendance_num     := 0;
    v_not_fulltime_attendance_num := 0;
    v_out_attendance_num          := 0;
    v_total_rest_num              := 0;
    v_fulltime_rest_num           := 0;
    v_not_fulltime_rest_num       := 0;
    v_out_rest_num                := 0;
    v_total_worktime_count        := 0;
    v_total_scheduling_num        := 0;
    v_fulltime_scheduling_num     := 0;
    v_not_fulltime_scheduling_num := 0;
    v_out_scheduling_num          := 0;
  
    ----查询运作在职总人数
    select count(distinct e.emp_code)
      into v_total_emp_num
      from op_dept d, tm_oss_employee e
     where d.dept_id = e.dept_id
       and e.emp_post_type = post_type_operation
       and (e.dimission_dt is null or
           e.dimission_dt > to_date(dayofmonth, date_format))
       and d.dept_code = dept_row.dept_code;
  
    ----查询运作全日制在职人数
    select count(distinct e.emp_code)
      into v_fulltime_emp_num
      from op_dept d, tm_oss_employee e
     where d.dept_id = e.dept_id
       and e.emp_post_type = post_type_operation
       and e.persg = v_work_type_full_time
       and (e.dimission_dt is null or
           e.dimission_dt > to_date(dayofmonth, date_format))
       and d.dept_code = dept_row.dept_code;
  
    ----查询运作非全日制在职人数
    select count(distinct e.emp_code)
      into v_not_fulltime_emp_num
      from op_dept d, tm_oss_employee e
     where d.dept_id = e.dept_id
       and e.emp_post_type = post_type_operation
       and e.persg = v_work_type_not_full_time
       and (e.dimission_dt is null or
           e.dimission_dt > to_date(dayofmonth, date_format))
       and d.dept_code = dept_row.dept_code;
  
    ----查询运作外包在职人数
    select count(distinct e.emp_code)
      into v_out_emp_num
      from op_dept d, tm_oss_employee e
     where d.dept_id = e.dept_id
       and e.emp_post_type = post_type_operation
       and e.work_type = v_work_type_out_employee
       and (e.dimission_dt is null or
           e.dimission_dt > to_date(dayofmonth, date_format))
       and d.dept_code = dept_row.dept_code;
  
    ----查询运作班次数量
    select count(distinct c.schedule_code)
      into v_class_num
      from tt_pb_shedule_by_day s, op_dept d, tm_pb_schedule_base_info c
     where s.dept_id = d.dept_id
       and c.dept_id = s.dept_id
       and c.schedule_code = s.shedule_code
       and d.dept_code = dept_row.dept_code
       and to_char(s.shedule_dt, date_format) = dayofmonth;
  
    ----查询运作小组数量
    select count(distinct g.group_code)
      into v_group_num
      from tt_pb_shedule_by_day s,
           op_dept              d,
           tm_oss_employee      e,
           tm_pb_group_info     g
     where s.emp_code = e.emp_code
       and s.dept_id = d.dept_id
       and e.group_id = g.group_id
       and to_char(s.shedule_dt, date_format) = dayofmonth
       and d.dept_code = dept_row.dept_code;
  
    ----查询运作出勤总人数
    select count(distinct t.emp_code)
      into v_total_attendance_num
      from op_dept d, ti_tcas_spms_schedule t
     where d.dept_code = t.dept_code
       and t.position_type = post_type_operation
       and to_char(t.work_date, date_format) = dayofmonth
       and d.dept_code = dept_row.dept_code;
  
    ----查询运作全日制出勤人数
    select count(distinct t.emp_code)
      into v_fulltime_attendance_num
      from op_dept d, ti_tcas_spms_schedule t, tm_oss_employee e
     where t.dept_code = d.dept_code
       and t.emp_code = e.emp_code
       and t.position_type = post_type_operation
       and to_char(t.work_date, date_format) = dayofmonth
       and e.persg = v_work_type_full_time
       and d.dept_code = dept_row.dept_code;
  
    ----查询运作非全日制出勤人数
    select count(distinct t.emp_code)
      into v_not_fulltime_attendance_num
      from op_dept d, ti_tcas_spms_schedule t, tm_oss_employee e
     where t.dept_code = d.dept_code
       and t.emp_code = e.emp_code
       and t.position_type = post_type_operation
       and to_char(t.work_date, date_format) = dayofmonth
       and e.persg = v_work_type_not_full_time
       and d.dept_code = dept_row.dept_code;
  
    ----查询运作外包出勤人数
    select count(distinct t.emp_code)
      into v_out_attendance_num
      from op_dept d, ti_tcas_spms_schedule t, tm_oss_employee e
     where t.dept_code = d.dept_code
       and t.emp_code = e.emp_code
       and t.position_type = post_type_operation
       and to_char(t.work_date, date_format) = dayofmonth
       and e.work_type = v_work_type_out_employee
       and d.dept_code = dept_row.dept_code;
  
    ----查询运作排休总人数
    select count(distinct s.emp_code)
      into v_total_rest_num
      from op_dept d, tt_pb_shedule_by_day s
     where d.dept_id = s.dept_id
       and to_char(s.shedule_dt, date_format) = dayofmonth
       and s.shedule_code = str_rest
       and d.dept_code = dept_row.dept_code;
  
    ----查询运作全日制排休人数
    select count(distinct s.emp_code)
      into v_fulltime_rest_num
      from op_dept d, tt_pb_shedule_by_day s, tm_oss_employee e
     where d.dept_id = s.dept_id
       and s.emp_code = e.emp_code
       and e.persg = v_work_type_full_time
       and to_char(s.shedule_dt, date_format) = dayofmonth
       and s.shedule_code = str_rest
       and d.dept_code = dept_row.dept_code;
  
    ----查询运作非全日制排休人数
    select count(distinct s.emp_code)
      into v_not_fulltime_rest_num
      from op_dept d, tt_pb_shedule_by_day s, tm_oss_employee e
     where d.dept_id = s.dept_id
       and s.emp_code = e.emp_code
       and e.persg = v_work_type_not_full_time
       and to_char(s.shedule_dt, date_format) = dayofmonth
       and s.shedule_code = str_rest
       and d.dept_code = dept_row.dept_code;
  
    ----查询运作外包排休人数
    select count(distinct s.emp_code)
      into v_out_rest_num
      from op_dept d, tt_pb_shedule_by_day s, tm_oss_employee e
     where d.dept_id = s.dept_id
       and s.emp_code = e.emp_code
       and e.work_type = v_work_type_out_employee
       and to_char(s.shedule_dt, date_format) = dayofmonth
       and s.shedule_code = str_rest
       and d.dept_code = dept_row.dept_code;
  
    ----查询运作考勤时长
    select sum(t.kq_xss)
      into v_total_worktime_count
      from op_dept d, ti_tcas_spms_schedule t
     where d.dept_code = t.dept_code
       and t.position_type = post_type_operation
       and to_char(t.work_date, date_format) = dayofmonth
       and d.dept_code = dept_row.dept_code;
  
    ----查询运作总排班人数
    select nvl(count(*), 0)
      into v_total_scheduling_num
      from tm_oss_employee e, tt_pb_shedule_by_day t
     where t.emp_code = e.emp_code
       and t.dept_id = e.dept_id
       and t.dept_id = dept_row.dept_id
       and (e.dimission_dt is null or
           e.dimission_dt > to_date(dayofmonth, date_format))
       and e.emp_post_type = post_type_operation
       and shedule_code <> str_rest
       and SHEDULE_DT = to_date(dayofmonth, date_format);
  
    ----查询运作全日制排班人数
    select nvl(count(*), 0)
      into v_fulltime_scheduling_num
      from tm_oss_employee e, tt_pb_shedule_by_day t
     where t.emp_code = e.emp_code
       and t.dept_id = e.dept_id
       and t.dept_id = dept_row.dept_id
       and e.persg = v_work_type_full_time
       and (e.dimission_dt is null or
           e.dimission_dt > to_date(dayofmonth, date_format))
       and e.emp_post_type = post_type_operation
       and shedule_code <> str_rest
       and SHEDULE_DT = to_date(dayofmonth, date_format);
  
    ----查询运作非全日制排班人数
    select nvl(count(*), 0)
      into v_not_fulltime_scheduling_num
      from tm_oss_employee e, tt_pb_shedule_by_day t
     where t.emp_code = e.emp_code
       and t.dept_id = e.dept_id
       and t.dept_id = dept_row.dept_id
       and e.persg = v_work_type_not_full_time
       and (e.dimission_dt is null or
           e.dimission_dt > to_date(dayofmonth, date_format))
       and e.emp_post_type = post_type_operation
       and shedule_code <> str_rest
       and SHEDULE_DT = to_date(dayofmonth, date_format);
  
    ----查询运作外包排班人数 
    select nvl(count(*), 0)
      into v_out_scheduling_num
      from tm_oss_employee e, tt_pb_shedule_by_day t
     where t.emp_code = e.emp_code
       and t.dept_id = e.dept_id
       and t.dept_id = dept_row.dept_id
       and e.work_type = v_work_type_out_employee
       and (e.dimission_dt is null or
           e.dimission_dt > to_date(dayofmonth, date_format))
       and e.emp_post_type = post_type_operation
       and shedule_code <> str_rest
       and SHEDULE_DT = to_date(dayofmonth, date_format);
  
    insert into op_attendance_count_report
      (dept_code,
       day_of_month,
       total_emp_num,
       fulltime_emp_num,
       not_fulltime_emp_num,
       out_emp_num,
       group_num,
       class_num,
       total_attendance_num,
       fulltime_attendance_num,
       not_fulltime_attendance_num,
       out_attendance_num,
       total_rest_num,
       fulltime_rest_num,
       not_fulltime_rest_num,
       out_rest_num,
       total_worktime,
       count_date,
       total_scheduling_num,
       fulltime_scheduling_num,
       not_fulltime_scheduling_num,
       out_scheduling_num)
    values
      (dept_row.dept_code,
       to_date(dayofmonth, date_format),
       v_total_emp_num,
       v_fulltime_emp_num,
       v_not_fulltime_emp_num,
       v_out_emp_num,
       v_group_num,
       v_class_num,
       v_total_attendance_num,
       v_fulltime_attendance_num,
       v_not_fulltime_attendance_num,
       v_out_attendance_num,
       v_total_rest_num,
       v_fulltime_rest_num,
       v_not_fulltime_rest_num,
       v_out_rest_num,
       v_total_worktime_count,
       sysdate,
       v_total_scheduling_num,
       v_fulltime_scheduling_num,
       v_not_fulltime_scheduling_num,
       v_out_scheduling_num);
    commit;
  
  end loop;

  -- 插入区部数据
  insert into op_attendance_count_report
    select decode(dept.area_code, null, max(dept.dept_code), dept.area_code),
           t.day_of_month,
           sum(t.total_emp_num),
           sum(t.fulltime_emp_num),
           sum(t.not_fulltime_emp_num),
           sum(t.out_emp_num),
           sum(t.group_num),
           sum(t.class_num),
           sum(t.total_attendance_num),
           sum(t.fulltime_attendance_num),
           sum(t.not_fulltime_attendance_num),
           sum(t.out_attendance_num),
           sum(t.total_rest_num),
           sum(t.fulltime_rest_num),
           sum(t.Not_Fulltime_Rest_Num),
           sum(t.out_rest_num),
           decode(sum(t.total_worktime), null, 0, sum(t.total_worktime)),
           sysdate,
           decode(sum(t.total_scheduling_num),
                  null,
                  0,
                  sum(t.total_scheduling_num)),
           decode(sum(t.fulltime_scheduling_num),
                  null,
                  0,
                  sum(t.fulltime_scheduling_num)),
           decode(sum(t.not_fulltime_scheduling_num),
                  null,
                  0,
                  sum(t.not_fulltime_scheduling_num)),
           decode(sum(t.out_scheduling_num),
                  null,
                  0,
                  sum(t.out_scheduling_num))
      from op_attendance_count_report t, tm_department dept
     where t.dept_code = dept.dept_code
       and to_char(t.day_of_month, 'YYYY-MM-DD') = dayofmonth
       and dept.delete_flg = 0
       and dept.type_code in ('ZZC04-YJ',
                              'ZZC04-ERJ',
                              'ZZC05-SJ',
                              'HHZ05',
                              'QB03-YSZX',
                              'FB05-YSZX',
                              'FB04-JSZX',
                              'GWB04')
     group by dept.area_code, t.day_of_month
     order by dept.area_code, t.day_of_month;
     COMMIT;

  -- 经营本部数据
  insert into op_attendance_count_report
    select dept.hq_code,
           t.day_of_month,
           sum(t.total_emp_num),
           sum(t.fulltime_emp_num),
           sum(t.not_fulltime_emp_num),
           sum(t.out_emp_num),
           sum(t.group_num),
           sum(t.class_num),
           sum(t.total_attendance_num),
           sum(t.fulltime_attendance_num),
           sum(t.not_fulltime_attendance_num),
           sum(t.out_attendance_num),
           sum(t.total_rest_num),
           sum(t.fulltime_rest_num),
           sum(t.Not_Fulltime_Rest_Num),
           sum(t.out_rest_num),
           decode(sum(t.total_worktime), null, 0, sum(t.total_worktime)),
           sysdate,
           decode(sum(t.total_scheduling_num),
                  null,
                  0,
                  sum(t.total_scheduling_num)),
           decode(sum(t.fulltime_scheduling_num),
                  null,
                  0,
                  sum(t.fulltime_scheduling_num)),
           decode(sum(t.not_fulltime_scheduling_num),
                  null,
                  0,
                  sum(t.not_fulltime_scheduling_num)),
           decode(sum(t.out_scheduling_num),
                  null,
                  0,
                  sum(t.out_scheduling_num))
      from op_attendance_count_report t, tm_department dept
     where t.dept_code = dept.dept_code
       and to_char(t.day_of_month, 'YYYY-MM-DD') = dayofmonth
       and dept.delete_flg = 0
        and dept.Type_Level = 2
     group by dept.hq_code, t.day_of_month
     order by dept.hq_code, t.day_of_month;
COMMIT;
  -- 插入总部数据
  insert into op_attendance_count_report
    select '001',
           t.day_of_month,
           sum(t.total_emp_num),
           sum(t.fulltime_emp_num),
           sum(t.not_fulltime_emp_num),
           sum(t.out_emp_num),
           sum(t.group_num),
           sum(t.class_num),
           sum(t.total_attendance_num),
           sum(t.fulltime_attendance_num),
           sum(t.not_fulltime_attendance_num),
           sum(t.out_attendance_num),
           sum(t.total_rest_num),
           sum(t.fulltime_rest_num),
           sum(t.Not_Fulltime_Rest_Num),
           sum(t.out_rest_num),
           decode(sum(t.total_worktime), null, 0, sum(t.total_worktime)),
           sysdate,
           decode(sum(t.total_scheduling_num),
                  null,
                  0,
                  sum(t.total_scheduling_num)),
           decode(sum(t.fulltime_scheduling_num),
                  null,
                  0,
                  sum(t.fulltime_scheduling_num)),
           decode(sum(t.not_fulltime_scheduling_num),
                  null,
                  0,
                  sum(t.not_fulltime_scheduling_num)),
           decode(sum(t.out_scheduling_num),
                  null,
                  0,
                  sum(t.out_scheduling_num))
      from op_attendance_count_report t, tm_department dept
     where t.dept_code = dept.dept_code
       and to_char(t.day_of_month, 'YYYY-MM-DD') = dayofmonth
       and dept.delete_flg = 0
       and dept.type_level = 1
     group by t.day_of_month
     order by t.day_of_month;

  COMMIT;

  --4 结束记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'COUNT_OPERATION_REPORT',
                               SYSDATE,
                               NULL,
                               NULL,
                               'END',
                               0,
                               L_CALL_NO);
EXCEPTION
  WHEN OTHERS THEN
    --回滚数据
    ROLLBACK;
    PKG_OSS_COMM.STP_RUNNING_LOG('',
                                 'COUNT_OPERATION_REPORT',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
  
end count_operation_report;
/
