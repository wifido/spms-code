create or replace procedure CALULATE_REST_NUM_HIS is

  v_procediure_name varchar2(2000) := 'CALULATE_REST_NUM_HIS';

  ----选定需要更新数据的筛选条件
  v_query_day_of_month constant date := to_date('2015-10-01', 'yyyy-mm-dd');

  cursor row_cursor(day_month date) is
    select DEPT_CODE, DAY_OF_MONTH
      from op_attendance_count_report
     where DAY_OF_MONTH BETWEEN day_month and SYSDATE;

  ----更新字段
  v_total_scheduling_num        number;
  v_fulltime_scheduling_num     number;
  v_not_fulltime_scheduling_num number;
  v_out_scheduling_num          number;
  v_total_rest_num              number;
  v_fulltime_rest_num           number;
  v_not_fulltime_rest_num       number;
  v_out_rest_num                number;

  post_type_operation       varchar2(20);
  v_work_type_full_time     varchar2(20);
  v_work_type_not_full_time varchar2(20);
  v_work_type_out_employee  varchar2(20);
  v_dept_code               varchar2(20);
  v_day_of_month            varchar2(20);
  str_rest                  varchar2(20);

  --1.定义执行序号
  L_CALL_NO NUMBER;

begin
  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               v_procediure_name,
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

  v_total_scheduling_num        := 0;
  v_fulltime_scheduling_num     := 0;
  v_not_fulltime_scheduling_num := 0;
  v_out_scheduling_num          := 0;
  v_total_rest_num              := 0;
  v_fulltime_rest_num           := 0;
  v_not_fulltime_rest_num       := 0;
  v_out_rest_num                := 0;

  v_work_type_full_time     := 'A';
  v_work_type_not_full_time := 'C';
  v_work_type_out_employee  := '6';
  post_type_operation       := '1';
  str_rest                  := '休';

  open row_cursor(v_query_day_of_month);
  fetch row_cursor
    into v_dept_code, v_day_of_month;
  while row_cursor%found loop
    ----查询运作全日制排休人数
    select count(distinct s.emp_code)
      into v_fulltime_rest_num
      from tm_department d, tt_pb_shedule_by_day s, tm_oss_employee e
     where d.dept_id = s.dept_id
       and s.emp_code = e.emp_code
       and e.persg = v_work_type_full_time
       and s.shedule_dt = v_day_of_month
       and s.shedule_code in (str_rest, 'SW', 'OFF')
       and d.dept_code = v_dept_code;
  
    ----查询运作非全日制排休人数
    select count(distinct s.emp_code)
      into v_not_fulltime_rest_num
      from tm_department d, tt_pb_shedule_by_day s, tm_oss_employee e
     where d.dept_id = s.dept_id
       and s.emp_code = e.emp_code
       and e.persg = v_work_type_not_full_time
       and s.shedule_dt = v_day_of_month
       and s.shedule_code in (str_rest, 'SW', 'OFF')
       and d.dept_code = v_dept_code;
  
    ----查询运作外包排休人数
    select count(distinct s.emp_code)
      into v_out_rest_num
      from tm_department d, tt_pb_shedule_by_day s, tm_oss_employee e
     where d.dept_id = s.dept_id
       and s.emp_code = e.emp_code
       and e.work_type = v_work_type_out_employee
       and s.shedule_dt = v_day_of_month
       and s.shedule_code in (str_rest, 'SW', 'OFF')
       and d.dept_code = v_dept_code;
  
    ----查询运作排休总人数   
    v_total_rest_num := v_fulltime_rest_num + v_not_fulltime_rest_num +
                        v_out_rest_num;
  
    ----计算运作全日制排班人数
    select nvl(count(*), 0)
      into v_fulltime_scheduling_num
      from tm_oss_employee e, tt_pb_shedule_by_day t, tm_department d
     where t.emp_code = e.emp_code
       and t.dept_id = e.dept_id
       and t.dept_id = d.dept_id
       and e.persg = v_work_type_full_time
       and (e.dimission_dt is null or e.dimission_dt > v_day_of_month)
       and e.emp_post_type = post_type_operation
       and shedule_code not in (str_rest, 'SW', 'OFF')
       and SHEDULE_DT = v_day_of_month
       and d.dept_code = v_dept_code;
  
    ----计算运作非全日制排班人数
    select nvl(count(*), 0)
      into v_not_fulltime_scheduling_num
      from tm_oss_employee e, tt_pb_shedule_by_day t, tm_department d
     where t.emp_code = e.emp_code
       and t.dept_id = e.dept_id
       and t.dept_id = d.dept_id
       and e.persg = v_work_type_not_full_time
       and (e.dimission_dt is null or e.dimission_dt > v_day_of_month)
       and e.emp_post_type = post_type_operation
       and shedule_code not in (str_rest, 'SW', 'OFF')
       and SHEDULE_DT = v_day_of_month
       and d.dept_code = v_dept_code;
  
    ----计算运作外包排班人数
    select nvl(count(*), 0)
      into v_out_scheduling_num
      from tm_oss_employee e, tt_pb_shedule_by_day t, tm_department d
     where t.emp_code = e.emp_code
       and t.dept_id = e.dept_id
       and t.dept_id = d.dept_id
       and e.work_type = v_work_type_out_employee
       and (e.dimission_dt is null or e.dimission_dt > v_day_of_month)
       and e.emp_post_type = post_type_operation
       and shedule_code not in (str_rest, 'SW', 'OFF')
       and SHEDULE_DT = v_day_of_month
       and d.dept_code = v_dept_code;
  
    ----计算运作总排班人数
    v_total_scheduling_num := v_fulltime_scheduling_num +
                              v_not_fulltime_scheduling_num +
                              v_out_scheduling_num;
    ----更新数据
    update op_attendance_count_report
       set total_scheduling_num        = v_total_scheduling_num,
           fulltime_scheduling_num     = v_fulltime_scheduling_num,
           not_fulltime_scheduling_num = v_not_fulltime_scheduling_num,
           out_scheduling_num          = v_out_scheduling_num,
           total_rest_num              = v_total_rest_num,
           fulltime_rest_num           = v_fulltime_rest_num,
           not_fulltime_rest_num       = v_not_fulltime_rest_num,
           out_rest_num                = v_out_rest_num
     where DEPT_CODE = v_dept_code
       and day_of_month = v_day_of_month;
    fetch row_cursor
      into v_dept_code, v_day_of_month;
  end loop;
  commit;

  ----更新区部考勤字段
  update OP_ATTENDANCE_COUNT_REPORT t
     set (TOTAL_SCHEDULING_NUM,
          FULLTIME_SCHEDULING_NUM,
          NOT_FULLTIME_SCHEDULING_NUM,
          OUT_SCHEDULING_NUM,
          TOTAL_REST_NUM,
          FULLTIME_REST_NUM,
          NOT_FULLTIME_REST_NUM,
          OUT_REST_NUM) =
         (SELECT SUM(total_scheduling_num),
                 SUM(fulltime_scheduling_num),
                 SUM(not_fulltime_scheduling_num),
                 SUM(out_scheduling_num),
                 SUM(total_rest_num),
                 SUM(fulltime_rest_num),
                 SUM(not_fulltime_rest_num),
                 SUM(out_rest_num)
            FROM (SELECT T.AREA_CODE, M.*
                    FROM OP_ATTENDANCE_COUNT_REPORT M, TM_DEPARTMENT T
                   WHERE M.DEPT_CODE = T.DEPT_CODE) f
           where f.AREA_CODE = t.dept_code
             and f.day_of_month = t.day_of_month
             and t.day_of_month between v_query_day_of_month and sysdate
           GROUP BY AREA_CODE, DAY_OF_MONTH)
  
   where exists
   (SELECT 1
            FROM (SELECT SUM(TOTAL_SCHEDULING_NUM) NUM_,
                         AREA_CODE,
                         DAY_OF_MONTH
                    FROM (SELECT T.AREA_CODE, M.*
                            FROM OP_ATTENDANCE_COUNT_REPORT M, TM_DEPARTMENT T
                           WHERE M.DEPT_CODE = T.DEPT_CODE) f
                   GROUP BY AREA_CODE, DAY_OF_MONTH) F
           WHERE f.AREA_CODE = t.dept_code
             and f.day_of_month = t.day_of_month
             and t.day_of_month between v_query_day_of_month and sysdate);
  commit;

  ----更新本部考勤字段
  update OP_ATTENDANCE_COUNT_REPORT t
     set (TOTAL_SCHEDULING_NUM,
          FULLTIME_SCHEDULING_NUM,
          NOT_FULLTIME_SCHEDULING_NUM,
          OUT_SCHEDULING_NUM,
          TOTAL_REST_NUM,
          FULLTIME_REST_NUM,
          NOT_FULLTIME_REST_NUM,
          OUT_REST_NUM) =
         (SELECT SUM(total_scheduling_num),
                 SUM(fulltime_scheduling_num),
                 SUM(not_fulltime_scheduling_num),
                 SUM(out_scheduling_num),
                 SUM(total_rest_num),
                 SUM(fulltime_rest_num),
                 SUM(not_fulltime_rest_num),
                 SUM(out_rest_num)
            FROM (SELECT T.HQ_CODE, M.*
                    FROM OP_ATTENDANCE_COUNT_REPORT M, TM_DEPARTMENT T
                   WHERE M.DEPT_CODE = T.DEPT_CODE) f
           where f.HQ_CODE = t.dept_code
             and f.day_of_month = t.day_of_month
             and t.day_of_month between v_query_day_of_month and sysdate
           GROUP BY HQ_CODE, DAY_OF_MONTH)
  
   where exists
   (SELECT 1
            FROM (SELECT SUM(TOTAL_SCHEDULING_NUM) NUM_,
                         HQ_CODE,
                         DAY_OF_MONTH
                    FROM (SELECT T.HQ_CODE, M.*
                            FROM OP_ATTENDANCE_COUNT_REPORT M, TM_DEPARTMENT T
                           WHERE M.DEPT_CODE = T.DEPT_CODE) f
                   GROUP BY HQ_CODE, DAY_OF_MONTH) F
           WHERE f.HQ_CODE = t.dept_code
             and f.day_of_month = t.day_of_month
             and t.day_of_month between v_query_day_of_month and sysdate);
  commit;

  ----更新总部考勤字段
  UPDATE OP_ATTENDANCE_COUNT_REPORT T
     SET (TOTAL_SCHEDULING_NUM,
          FULLTIME_SCHEDULING_NUM,
          NOT_FULLTIME_SCHEDULING_NUM,
          OUT_SCHEDULING_NUM,
          TOTAL_REST_NUM,
          FULLTIME_REST_NUM,
          NOT_FULLTIME_REST_NUM,
          OUT_REST_NUM) =
         (SELECT SUM(total_scheduling_num),
                 SUM(fulltime_scheduling_num),
                 SUM(not_fulltime_scheduling_num),
                 SUM(out_scheduling_num),
                 SUM(total_rest_num),
                 SUM(fulltime_rest_num),
                 SUM(not_fulltime_rest_num),
                 SUM(out_rest_num)
            FROM (SELECT SUM(TOTAL_ATTENDANCE_NUM) TOTAL_ATTENDANCE_NUM,
                         SUM(FULLTIME_ATTENDANCE_NUM) FULLTIME_ATTENDANCE_NUM,
                         SUM(NOT_FULLTIME_ATTENDANCE_NUM) NOT_FULLTIME_ATTENDANCE_NUM,
                         SUM(OUT_ATTENDANCE_NUM) OUT_ATTENDANCE_NUM,
                         SUM(TOTAL_WORKTIME) TOTAL_WORKTIME,
                         DAY_OF_MONTH
                    FROM (select *
                            from (SELECT M.*
                                    FROM OP_ATTENDANCE_COUNT_REPORT M
                                   WHERE M.DEPT_CODE <> '001') f,
                                 tm_department d
                           where f.dept_code = d.dept_code
                             and f.dept_code <> d.area_code)
                   GROUP BY DAY_OF_MONTH) F
           WHERE F.DAY_OF_MONTH = T.DAY_OF_MONTH
             AND T.DEPT_CODE = '001'
             AND T.DAY_OF_MONTH BETWEEN v_query_day_of_month and sysdate)
   WHERE T.DEPT_CODE = '001'
     AND T.DAY_OF_MONTH BETWEEN v_query_day_of_month and sysdate;
  commit;

  --4 结束记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'CALULATE_REST_NUM_HIS',
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
                                 'CALULATE_REST_NUM_HIS',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
end CALULATE_REST_NUM_HIS;
/
