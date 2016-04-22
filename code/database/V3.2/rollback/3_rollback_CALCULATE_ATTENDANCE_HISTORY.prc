create or replace procedure CALCULATE_ATTENDANCE_HISTORY is
  --*************************************************************
  -- author  : smm
  -- created : 2015-11-18
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

  ----选定需要更新数据的筛选条件
  v_query_day_of_month constant date := to_date('2015-09-01', 'yyyy-mm-dd');

  cursor row_cursor(day_month date) is
    select DEPT_CODE, DAY_OF_MONTH
      from op_attendance_count_report
     where (total_attendance_num = 0 or total_attendance_num is null)
       and DAY_OF_MONTH BETWEEN day_month and SYSDATE;

  ----更新字段
  v_total_attendance_num        number;
  v_fulltime_attendance_num     number;
  v_not_fulltime_attendance_num number;
  v_out_attendance_num          number;
  v_total_worktime_count        number;
  post_type_operation           varchar2(20);
  v_work_type_full_time         varchar2(20);
  v_work_type_not_full_time     varchar2(20);
  v_work_type_out_employee      varchar2(20);
  v_dept_code                   varchar2(20);
  v_day_of_month                varchar2(20);

  --1.定义执行序号
  L_CALL_NO NUMBER;

begin
  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'CALCULATE_ATTENDANCE_HISTORY',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);
  --初始化
  v_total_attendance_num        := 0;
  v_fulltime_attendance_num     := 0;
  v_not_fulltime_attendance_num := 0;
  v_out_attendance_num          := 0;
  v_total_worktime_count        := 0;
  v_work_type_full_time         := 'A';
  v_work_type_not_full_time     := 'C';
  v_work_type_out_employee      := '6';
  post_type_operation           := '1';

  open row_cursor(v_query_day_of_month);
  fetch row_cursor
    into v_dept_code, v_day_of_month;
  while row_cursor%found loop
  
    ----查询运作全日制出勤人数
    select count(distinct t.emp_code)
      into v_fulltime_attendance_num
      from tm_department d, ti_tcas_spms_schedule t, tm_oss_employee e
     where t.dept_code = d.dept_code
       and t.emp_code = e.emp_code
       and t.position_type = post_type_operation
       and t.work_date = v_day_of_month
       and e.persg = v_work_type_full_time
       and d.dept_code = v_dept_code;
  
    ----查询运作非全日制出勤人数
    select count(distinct t.emp_code)
      into v_not_fulltime_attendance_num
      from tm_department d, ti_tcas_spms_schedule t, tm_oss_employee e
     where t.dept_code = d.dept_code
       and t.emp_code = e.emp_code
       and t.position_type = post_type_operation
       and t.work_date = v_day_of_month
       and e.persg = v_work_type_not_full_time
       and d.dept_code = v_dept_code;
  
    ----查询运作外包出勤人数
    select count(distinct t.emp_code)
      into v_out_attendance_num
      from tm_department d, ti_tcas_spms_schedule t, tm_oss_employee e
     where t.dept_code = d.dept_code
       and t.emp_code = e.emp_code
       and t.position_type = post_type_operation
       and t.work_date = v_day_of_month
       and e.work_type = v_work_type_out_employee
       and d.dept_code = v_dept_code;
  
    ----查询运作考勤总时长
    select sum(t.kq_xss)
      into v_total_worktime_count
      from tm_department d, ti_tcas_spms_schedule t
     where d.dept_code = t.dept_code
       and t.position_type = post_type_operation
       and t.work_date = v_day_of_month
       and d.dept_code = v_dept_code;
  
    ----计算运作出考勤总人数
    v_total_attendance_num := v_fulltime_attendance_num +
                              v_not_fulltime_attendance_num +
                              v_out_attendance_num;
    ----更新数据    
    update op_attendance_count_report
       set total_attendance_num        = v_total_attendance_num,
           fulltime_attendance_num     = v_fulltime_attendance_num,
           not_fulltime_attendance_num = v_not_fulltime_attendance_num,
           out_attendance_num          = v_out_attendance_num,
           total_worktime              = v_total_worktime_count
     where DEPT_CODE = v_dept_code
       and day_of_month = v_day_of_month;
    fetch row_cursor
      into v_dept_code, v_day_of_month;
  end loop;
  commit;

  ----更新区部考勤字段
  update OP_ATTENDANCE_COUNT_REPORT t
     set (TOTAL_ATTENDANCE_NUM,
          FULLTIME_ATTENDANCE_NUM,
          NOT_FULLTIME_ATTENDANCE_NUM,
          OUT_ATTENDANCE_NUM,
          TOTAL_WORKTIME) =
         (SELECT SUM(TOTAL_ATTENDANCE_NUM),
                 SUM(FULLTIME_ATTENDANCE_NUM),
                 SUM(NOT_FULLTIME_ATTENDANCE_NUM),
                 SUM(OUT_ATTENDANCE_NUM),
                 SUM(TOTAL_WORKTIME)
            FROM (SELECT T.AREA_CODE, M.*
                    FROM OP_ATTENDANCE_COUNT_REPORT M, TM_DEPARTMENT T
                   WHERE M.DEPT_CODE = T.DEPT_CODE) f
           where f.AREA_CODE = t.dept_code
             and f.day_of_month = t.day_of_month
             and t.day_of_month between v_query_day_of_month and sysdate
           GROUP BY AREA_CODE, DAY_OF_MONTH)
  
   where exists
   (SELECT 1
            FROM (SELECT SUM(TOTAL_ATTENDANCE_NUM) NUM_,
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
     set (TOTAL_ATTENDANCE_NUM,
          FULLTIME_ATTENDANCE_NUM,
          NOT_FULLTIME_ATTENDANCE_NUM,
          OUT_ATTENDANCE_NUM,
          TOTAL_WORKTIME) =
         (SELECT SUM(TOTAL_ATTENDANCE_NUM),
                 SUM(FULLTIME_ATTENDANCE_NUM),
                 SUM(NOT_FULLTIME_ATTENDANCE_NUM),
                 SUM(OUT_ATTENDANCE_NUM),
                 SUM(TOTAL_WORKTIME)
            FROM (SELECT T.HQ_CODE, M.*
                    FROM OP_ATTENDANCE_COUNT_REPORT M, TM_DEPARTMENT T
                   WHERE M.DEPT_CODE = T.DEPT_CODE
                     and M.DEPT_CODE <> T.AREA_CODE) f
           where f.HQ_CODE = t.dept_code
             and f.day_of_month = t.day_of_month
             and t.day_of_month between v_query_day_of_month and sysdate
           GROUP BY HQ_CODE, DAY_OF_MONTH)
  
   where exists
   (SELECT 1
            FROM (SELECT SUM(TOTAL_ATTENDANCE_NUM) NUM_,
                         HQ_CODE,
                         DAY_OF_MONTH
                    FROM (SELECT T.HQ_CODE, M.*
                            FROM OP_ATTENDANCE_COUNT_REPORT M, TM_DEPARTMENT T
                           WHERE M.DEPT_CODE = T.DEPT_CODE
                             and M.DEPT_CODE <> T.AREA_CODE) f
                   GROUP BY HQ_CODE, DAY_OF_MONTH) F
           WHERE f.HQ_CODE = t.dept_code
             and f.day_of_month = t.day_of_month
             and t.day_of_month between v_query_day_of_month and sysdate);
  commit;

  ----更新总部考勤字段
  UPDATE OP_ATTENDANCE_COUNT_REPORT T
     SET (TOTAL_ATTENDANCE_NUM,
          FULLTIME_ATTENDANCE_NUM,
          NOT_FULLTIME_ATTENDANCE_NUM,
          OUT_ATTENDANCE_NUM,
          TOTAL_WORKTIME) =
         (SELECT TOTAL_ATTENDANCE_NUM,
                 FULLTIME_ATTENDANCE_NUM,
                 NOT_FULLTIME_ATTENDANCE_NUM,
                 OUT_ATTENDANCE_NUM,
                 TOTAL_WORKTIME
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
                               'CALCULATE_ATTENDANCE_HISTORY',
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
                                 'CALCULATE_ATTENDANCE_HISTORY',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
end CALCULATE_ATTENDANCE_HISTORY;
/
