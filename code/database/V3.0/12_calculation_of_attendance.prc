create or replace procedure calculation_of_attendance(dayofmonth in varchar) is
  --*************************************************************
  -- author  : smm
  -- created : 2015-11-11
  -- purpose : 统计考勤信息报表
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

  v_total_attendance_num        number;
  v_fulltime_attendance_num     number;
  v_not_fulltime_attendance_num number;
  v_out_attendance_num          number;
  v_total_worktime_count        number(10, 2);
  date_format                   varchar2(20);
  post_type_operation           varchar2(20);
  v_work_type_full_time         varchar2(20);
  v_work_type_not_full_time     varchar2(20);
  v_work_type_out_employee      varchar2(20);
  /* dayofmonth                    varchar2(20);*/

begin

  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG(dayofmonth,
                               'CALCULATION_OF_ATTENDANCE',
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
  /* dayofmonth                := TO_CHAR(SYSDATE - 8, 'yyyy-mm-dd');*/

  /*----删除当天的统计数据
  delete op_attendance_count_report t
   where to_char(t.day_of_month, date_format) = dayofmonth;*/

  ----遍历运作网点，逐个网点统计
  for dept_row in (select * from op_dept) loop
  
    ----初始化值
  
    v_total_attendance_num        := 0;
    v_fulltime_attendance_num     := 0;
    v_not_fulltime_attendance_num := 0;
    v_out_attendance_num          := 0;
    v_total_worktime_count        := 0;
  
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
  
    ----查询运作考勤时长
    select sum(t.kq_xss)
      into v_total_worktime_count
      from op_dept d, ti_tcas_spms_schedule t
     where d.dept_code = t.dept_code
       and t.position_type = post_type_operation
       and to_char(t.work_date, date_format) = dayofmonth
       and d.dept_code = dept_row.dept_code;
  
    update op_attendance_count_report
       set total_attendance_num        = v_total_attendance_num,
           fulltime_attendance_num     = v_fulltime_attendance_num,
           not_fulltime_attendance_num = v_not_fulltime_attendance_num,
           out_attendance_num          = v_out_attendance_num,
           total_worktime              = v_total_worktime_count
     where DEPT_CODE = dept_row.dept_code
       and to_char(DAY_OF_MONTH, date_format) = dayofmonth;
    commit;
  
  end loop;

  --4 结束记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'CALCULATION_OF_ATTENDANCE',
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
                                 'CALCULATION_OF_ATTENDANCE',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
  
end calculation_of_attendance;
/
