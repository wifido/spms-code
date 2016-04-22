create or replace procedure OP_SCH_INPUT_REPORT(YEAR_MONTH in varchar2) is
  area_code               VARCHAR2(30);
  department_code         varchar2(30);
  inner_emp_number        NUMBER;
  outer_emp_number        NUMBER;
  group_number            NUMBER;
  grouping_number         NUMBER;
  diurnal_number          NUMBER;
  non_diurnal_number      NUMBER;
  class_number            NUMBER;
  confirm_process_number  NUMBER;
  scheduled_confirm_inner NUMBER;
  scheduled_confirm_outer NUMBER;
  process_scheduled_inner NUMBER;
  process_scheduled_outer NUMBER;
  scheduled_complete_rate NUMBER;
  V_YEAR_MONTH            varchar2(10);
  --1.定义执行序号
  L_CALL_NO NUMBER;

begin
  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'OP_SCH_INPUT_REPORT' || YEAR_MONTH,
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);
  --删除历史统计数据
  V_YEAR_MONTH := YEAR_MONTH;
  delete tt_op_sch_statis_report r where r.year_month = V_YEAR_MONTH;

  for dept_row in (select *
                     from op_dept t
                    order by t.hq_code, t.area_code, t.dept_code) loop
    group_number            := 0;
    grouping_number         := 0;
    diurnal_number          := 0;
    non_diurnal_number      := 0;
    class_number            := 0;
    confirm_process_number  := 0;
    scheduled_confirm_inner := 0;
    scheduled_confirm_outer := 0;
    process_scheduled_inner := 0;
    process_scheduled_outer := 0;
	scheduled_complete_rate NUMBER;
    department_code         := dept_row.dept_code;
    area_code               := dept_row.area_code;
  
    -- 内部人员数量
    select nvl(t.counts, 0) counts
      into inner_emp_number
      from op_dept d,
           (select d.DEPT_CODE, count(e.emp_code) counts
              from op_dept d, tm_oss_employee e
             where d.DEPT_ID = e.dept_id
               and e.emp_post_type = '1'
               and (e.dimission_dt is null or e.dimission_dt > sysdate)
               and e.work_type not in (6, 8, 9)
             group by d.DEPT_CODE) t
     where d.DEPT_CODE = t.DEPT_CODE(+)
       and d.dept_code = dept_row.dept_code;
  
    -- 全日制用工数量
    select nvl(t.counts, 0) counts
      into diurnal_number
      from op_dept d,
           (select d.DEPT_CODE, count(e.emp_code) counts
              from op_dept d, tm_oss_employee e
             where d.DEPT_ID = e.dept_id
               and e.emp_post_type = '1'
               and e.persg_txt = '全日制用工'
               and (e.dimission_dt is null or e.dimission_dt > sysdate)
               and e.work_type not in (6, 8, 9)
             group by d.DEPT_CODE) t
     where d.DEPT_CODE = t.DEPT_CODE(+)
       and d.dept_code = dept_row.dept_code;
  
    -- 非全日制用工数量
    select nvl(t.counts, 0) counts
      into non_diurnal_number
      from op_dept d,
           (select d.DEPT_CODE, count(e.emp_code) counts
              from op_dept d, tm_oss_employee e
             where d.DEPT_ID = e.dept_id
               and e.emp_post_type = '1'
               and e.persg_txt = '非全日制用工'
               and (e.dimission_dt is null or e.dimission_dt > sysdate)
               and e.work_type not in (6, 8, 9)
             group by d.DEPT_CODE) t
     where d.DEPT_CODE = t.DEPT_CODE(+)
       and d.dept_code = dept_row.dept_code;
  
    -- 外包人员数量
    select nvl(t.counts, 0) counts
      into outer_emp_number
      from op_dept d,
           (select d.DEPT_CODE, count(e.emp_code) counts
              from op_dept d, tm_oss_employee e
             where d.DEPT_ID = e.dept_id
               and e.emp_post_type = '1'
               and e.work_type = 6
               and (e.dimission_dt is null or e.dimission_dt > sysdate)
               and e.emp_post_type = '1'
             group by d.DEPT_CODE) t
     where d.DEPT_CODE = t.DEPT_CODE(+)
       and d.dept_code = dept_row.dept_code;
  
    -- 小组数
    select nvl(t.counts, 0) counts
      into group_number
      from op_dept d,
           (select d.DEPT_CODE, count(distinct g.group_code) counts
              from op_dept d, tm_pb_group_info g
             where d.DEPT_ID = g.dept_id
             group by d.DEPT_CODE) t
     where d.DEPT_CODE = t.DEPT_CODE(+)
       and d.dept_code = dept_row.dept_code;
  
    -- 已分组人数
    select nvl(t.counts, 0) counts
      into grouping_number
      from op_dept d,
           (select d.DEPT_CODE, count(*) counts
              from op_dept d, tm_oss_employee e, tm_pb_group_info g
             where d.DEPT_ID = e.dept_id
               and e.group_id = g.group_id
               and e.emp_post_type = '1'
               and e.emp_code is not null
               and e.group_id is not null
             group by d.DEPT_CODE) t
     where d.DEPT_CODE = t.DEPT_CODE(+)
       and d.dept_code = dept_row.dept_code;
  
    -- 班别数量
    select nvl(t.counts, 0) counts
      into class_number
      from op_dept d,
           (select d.DEPT_CODE, c.ym, count(distinct c.schedule_code) counts
              from op_dept d, tm_pb_schedule_base_info c
             where d.DEPT_ID = c.dept_id
               and c.ym = V_YEAR_MONTH
             group by d.DEPT_CODE, c.ym) t
     where d.DEPT_CODE = t.DEPT_CODE(+)
       and d.dept_code = dept_row.dept_code;
  
    -- 工序数量
    select nvl(t.counts, 0) counts
      into confirm_process_number
      from op_dept d,
           (select d.DEPT_CODE, count(distinct c.process_code) counts
              from op_dept d, tm_pb_process_info c
             where d.DEPT_ID = c.dept_id
               and c.status = '1'
             group by d.DEPT_CODE) t
     where d.DEPT_CODE = t.DEPT_CODE(+)
       and d.dept_code = dept_row.dept_code;
  
    -- 运作排班确认内部人员数量
    select count(dept.dept_code) sche_confirm_inner_emp_num
      into scheduled_confirm_inner
      from op_dept dept,
           (select dept.dept_code, record.ym, emp.emp_code
              from op_dept dept,
                   tt_pb_shedule_by_month_log record,
                   (select emp.emp_code, emp.dept_id, emp.dimission_dt
                      from tm_oss_employee emp
                     where emp.emp_post_type = 1
                       and emp.data_source = '2'
                       and (emp.dimission_dt is null or
                           emp.dimission_dt > sysdate)) emp
             where dept.dept_id = record.dept_id(+)
               and dept.dept_id = emp.dept_id
               and record.dept_id = emp.dept_id
               and record.emp_code = emp.emp_code
            
             group by dept.dept_code, record.ym, emp.emp_code) record
     where dept.dept_code = record.dept_code(+)
       and record.ym = V_YEAR_MONTH
       and dept.dept_code = dept_row.dept_code;
  
    -- 运作排班确认外包人员数量
    select count(record.emp_code) sche_confirm_outer_emp_num
      into scheduled_confirm_outer
      from op_dept dept,
           (select dept.dept_code, record.ym, emp.emp_code
              from op_dept dept,
                   tt_pb_shedule_by_month_log record,
                   (select emp.emp_code, emp.dept_id, emp.dimission_dt
                      from tm_oss_employee emp
                     where emp.emp_post_type = 1
                       and emp.work_type = 6
                       and (emp.dimission_dt is null or
                           emp.dimission_dt > sysdate)) emp
             where dept.dept_id = record.dept_id
               and dept.dept_id = emp.dept_id
               and record.dept_id = emp.dept_id
               and record.emp_code = emp.emp_code
             group by dept.dept_code, record.ym, emp.emp_code) record
     where dept.dept_code = record.dept_code(+)
       and dept.dept_code = dept_row.dept_code
       and record.ym = V_YEAR_MONTH;
  
    -- 运作工序确认内部人员数量
    select count(record.emp_code) pro_inner_emp_comfirm_num
      into process_scheduled_inner
      from op_dept dept,
           (select dept.dept_code, process.ym, emp.emp_code
              from op_dept dept,
                   tt_pb_process_by_month_log process,
                   (select emp.emp_code, emp.dept_id, emp.dimission_dt
                      from tm_oss_employee emp
                     where emp.emp_post_type = 1
                       and emp.data_source = '2'
                       and (emp.dimission_dt is null or
                           emp.dimission_dt > sysdate)) emp
             where dept.dept_id = process.dept_id
               and process.dept_id = emp.dept_id
               and dept.dept_id = emp.dept_id
               and process.emp_code = emp.emp_code
             group by dept.dept_code, process.ym, emp.emp_code) record
     where dept.dept_code = record.dept_code(+)
       and dept.dept_code = dept_row.dept_code
       and record.ym = V_YEAR_MONTH;
  
    -- 运作工序确认外包人员数量
    select count(record.emp_code) pro_outer_emp_comfirm_num
      into process_scheduled_outer
      from op_dept dept,
           (select dept.dept_code, process.ym, emp.emp_code
              from op_dept dept,
                   tt_pb_process_by_month_log process,
                   (select emp.emp_code, emp.dept_id, emp.dimission_dt
                      from tm_oss_employee emp
                     where emp.emp_post_type = 1
                       and emp.work_type = 6
                       and (emp.dimission_dt is null or
                           emp.dimission_dt > sysdate)) emp
             where dept.dept_id = process.dept_id
               and process.dept_id = emp.dept_id
               and dept.dept_id = emp.dept_id
               and process.emp_code = emp.emp_code
             group by dept.dept_code, process.ym, emp.emp_code) record
     where dept.dept_code = record.dept_code(+)
       and dept.dept_code = dept_row.dept_code
       and record.ym = V_YEAR_MONTH;
  
    if inner_emp_number != 0 then
      scheduled_complete_rate := round(scheduled_confirm_inner /
                                       inner_emp_number,
                                       4);
    end if;
  
    -- 统计数据
    insert into tt_op_sch_statis_report
      (id,
       year_month,
       hq_code,
       area_code,
       department_code,
       inner_emp,
       outer_emp,
       group_number,
       grouping_number,
       class_number,
       confirm_process,
       sch_confirm_inner_emp,
       sch_confirm_outer_emp,
       process_sch_inner_emp,
       process_sch_outer_emp,
       sch_complete_rate,
       diurnal_count,
       non_diurnal_count)
    values
      (seq_op_sch_statis_report.nextval,
       V_YEAR_MONTH,
       dept_row.hq_code,
       area_code,
       department_code,
       inner_emp_number,
       outer_emp_number,
       group_number,
       grouping_number,
       class_number,
       confirm_process_number,
       scheduled_confirm_inner,
       scheduled_confirm_outer,
       process_scheduled_inner,
       process_scheduled_outer,
       scheduled_complete_rate,
       diurnal_number,
       non_diurnal_number);
  
  end loop;
  COMMIT;
  --4.结束记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'OP_SCH_INPUT_REPORT',
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
                                 'OP_SCH_INPUT_REPORT',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
end OP_SCH_INPUT_REPORT;
/
