create or replace procedure CALCULATE_EMPLOYEE_NUM_HISTORY is

  v_procediure_name varchar2(2000) := 'CALCULATE_EMPLOYEE_NUM_HISTORY';

  ----选定需要更新数据的筛选条件
  v_query_day_of_month constant date := to_date('2015-07-01', 'yyyy-mm-dd');

  cursor row_cursor(day_month date) is
    select DEPT_CODE, DAY_OF_MONTH
      from op_attendance_count_report
     where total_scheduling_num is null
       and DAY_OF_MONTH BETWEEN day_month and SYSDATE
       for update;

  str_rest                  constant varchar2(20) := '休';
  v_work_type_full_time     constant tm_oss_employee.persg%type := 'A';
  v_work_type_not_full_time constant tm_oss_employee.persg%type := 'C';
  v_work_type_out_employee  constant tm_oss_employee.work_type%type := '6';
  post_type_operation       constant varchar2(20) := '1';

  v_dept_code    op_attendance_count_report.dept_code%type;
  v_day_of_month op_attendance_count_report.day_of_month%type;
  v_dept_id      tm_department.dept_id%type;

  ----更新字段
  v_total_scheduling_num        op_attendance_count_report.total_scheduling_num%type;
  v_fulltime_scheduling_num     op_attendance_count_report.fulltime_scheduling_num%type;
  v_not_fulltime_scheduling_num op_attendance_count_report.not_fulltime_scheduling_num%type;
  v_out_scheduling_num          op_attendance_count_report.out_scheduling_num%type;

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

  open row_cursor(v_query_day_of_month);
  fetch row_cursor
    into v_dept_code, v_day_of_month;
  while row_cursor%found loop
    ----查询网点ID
    select t.dept_id
      into v_dept_id
      from tm_department t
     where t.dept_code = v_dept_code;
    ----计算运作全日制排班人数
    select nvl(count(*), 0)
      into v_fulltime_scheduling_num
      from tm_oss_employee e, tt_pb_shedule_by_day t
     where t.emp_code = e.emp_code
       and t.dept_id = e.dept_id
       and t.dept_id = v_dept_id
       and e.persg = v_work_type_full_time
       and (e.dimission_dt is null or e.dimission_dt > v_day_of_month)
       and e.emp_post_type = post_type_operation
       and shedule_code <> str_rest
       and SHEDULE_DT = v_day_of_month;
  
    ----计算运作非全日制排班人数
    select nvl(count(*), 0)
      into v_not_fulltime_scheduling_num
      from tm_oss_employee e, tt_pb_shedule_by_day t
     where t.emp_code = e.emp_code
       and t.dept_id = e.dept_id
       and t.dept_id = v_dept_id
       and e.persg = v_work_type_not_full_time
       and (e.dimission_dt is null or e.dimission_dt > v_day_of_month)
       and e.emp_post_type = post_type_operation
       and shedule_code <> str_rest
       and SHEDULE_DT = v_day_of_month;
  
    ----计算运作外包排班人数 
    select nvl(count(*), 0)
      into v_out_scheduling_num
      from tm_oss_employee e, tt_pb_shedule_by_day t
     where t.emp_code = e.emp_code
       and t.dept_id = e.dept_id
       and t.dept_id = v_dept_id
       and e.work_type = v_work_type_out_employee
       and (e.dimission_dt is null or e.dimission_dt > v_day_of_month)
       and e.emp_post_type = post_type_operation
       and shedule_code <> str_rest
       and SHEDULE_DT = v_day_of_month;
  
    ----计算运作总排班人数 
    v_total_scheduling_num := v_fulltime_scheduling_num +
                              v_not_fulltime_scheduling_num +
                              v_out_scheduling_num;
    ----更新数据
    update op_attendance_count_report
       set total_scheduling_num        = v_total_scheduling_num,
           fulltime_scheduling_num     = v_fulltime_scheduling_num,
           not_fulltime_scheduling_num = v_not_fulltime_scheduling_num,
           out_scheduling_num          = v_out_scheduling_num
     where current of row_cursor;
    fetch row_cursor
      into v_dept_code, v_day_of_month;
  end loop;
  commit;

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
	   and t.count_date >= date'2015-07-01'
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
	   and t.count_date >= date'2015-07-01'
       and dept.delete_flg = 0
       and dept.Type_Level = 2
     group by dept.hq_code, t.day_of_month
     order by dept.hq_code, t.day_of_month;

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
	   and t.count_date >= date'2015-07-01'
       and dept.delete_flg = 0
       and dept.type_level = 1
     group by t.day_of_month
     order by t.day_of_month;
	 
	 commit;

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
end CALCULATE_EMPLOYEE_NUM_HISTORY;
/
