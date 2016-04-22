spool update procedures.log
prompt
prompt Creating procedure SCHEDULING_SAP_SYNCHRONIZATION
prompt =================================================
prompt
create or replace procedure SCHEDULING_SAP_SYNCHRONIZATION as
  the_synchronization_time varchar2(25);

begin

  -- 查询上次同步时间
  select to_char(SYNCHRONIZATION_TIME, 'yyyy-mm-dd hh24:mi:ss')
    into the_synchronization_time
    from TT_SAP_SYNCHRONOUS_RECORD
   where PROCEDURES_NAME = 'SCHEDULING_SAP_SYNCHRONIZATION';

  --将调度同步过来的排班数据同步到系统的排班表中
  DECLARE
    CURSOR SCH_EMP_CUR IS
      select SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL as id,
             '' as DEPARTMENT_CODE,
             BEGIN_TM,
             END_TM,
             begin_date,
             substr(begin_date, 0, 6),
             EMP_CODE,
             '' as CREATED_EMPLOYEE_CODE,
             '' as MODIFIED_EMPLOYEE_CODE,
             sysdate,
             sysdate,
             2,
             TMR_DAY_FLAG,
             '',
             ''
        from TT_SCH_EMP_ATTENCE_CLASS t
       where t.create_TM >
             to_date(the_synchronization_time, 'yyyy-mm-dd hh24:mi:ss');
    TYPE recstype IS TABLE OF TT_SCHEDULE_DAILY%ROWTYPE;
    recstypes recstype;
  BEGIN
    OPEN SCH_EMP_CUR;
    WHILE (TRUE) LOOP
      FETCH SCH_EMP_CUR BULK COLLECT
        INTO recstypes LIMIT 1000;
      FORALL i IN 1 .. recstypes.COUNT
        INSERT INTO TT_SCHEDULE_DAILY VALUES recstypes (i);
      COMMIT;
      EXIT WHEN SCH_EMP_CUR%NOTFOUND;
    END LOOP;
    CLOSE SCH_EMP_CUR;
  END;

  --将排班数据表中的数据同步到待发送给SAP接口的数据表中
  DECLARE
    --非跨天的数据库集合
    CURSOR Non_Cross_Day_Cur IS
      select SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL as id,
             EMPLOYEE_CODE,
             DAY_OF_MONTH as begin_date,
             DAY_OF_MONTH as end_date,
             BEGIN_TIME,
             END_TIME,
             '' as TMR_DAY_FLAG,
             decode(begin_time, null, 'OFF', '') as OFF_DUTY_FLAG,
             '2' as CLASS_SYSTEM,
             sysdate,
             '',
             0 as STATE_FLG,
             ''
        from TT_SCHEDULE_DAILY t
       where ( SYNCHRO_STATUS != 1  or  SYNCHRO_STATUS is null) and CROSS_DAY_TYPE is null
	      and EMP_POST_TYPE = '2'
              and DAY_OF_MONTH <= to_char(sysdate,'yyyymmdd');
    --跨天的数据集合
    CURSOR Cross_Day_Cur IS
      select SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL as id,
             EMPLOYEE_CODE,
             DAY_OF_MONTH as begin_date,
             DAY_OF_MONTH as end_date,
             BEGIN_TIME,
             END_TIME,
             '' as TMR_DAY_FLAG,
             decode(begin_time, null, 'OFF', '') as OFF_DUTY_FLAG,
             '2' as CLASS_SYSTEM,
             sysdate as CREATE_TM,
             '' as NODE_KEY,
             0 as STATE_FLG,
             ''
        from TT_SCHEDULE_DAILY t
       where  ( SYNCHRO_STATUS != 1  or  SYNCHRO_STATUS is null)
         and CROSS_DAY_TYPE = 'X'
	 and EMP_POST_TYPE = '2'
          and DAY_OF_MONTH <= to_char(sysdate,'yyyymmdd');

    TYPE rec IS TABLE OF TT_SAP_SYNCHRONOUS%ROWTYPE;
    recs rec;

    CROSS_DAY_row Cross_Day_Cur%rowtype;
  BEGIN

    --非跨天的数据集合
    OPEN Non_Cross_Day_Cur;
    WHILE (TRUE) LOOP
      FETCH Non_Cross_Day_Cur BULK COLLECT
        INTO recs LIMIT 1000;
      FORALL i IN 1 .. recs.COUNT
        INSERT INTO TT_SAP_SYNCHRONOUS VALUES recs (i);
      COMMIT;

      EXIT WHEN Non_Cross_Day_Cur%NOTFOUND;
    END LOOP;
    CLOSE Non_Cross_Day_Cur;
    --跨天的数据集合
    for CROSS_DAY_row in Cross_Day_Cur loop
      insert into tt_sap_synchronous
        (ID,
         EMP_CODE,
         BEGIN_DATE,
         END_DATE,
         BEGIN_TM,
         END_TM,
         TMR_DAY_FLAG,
         OFF_DUTY_FLAG,
         CLASS_SYSTEM,
         CREATE_TM,
         NODE_KEY,
         STATE_FLG)
      values
        (CROSS_DAY_row.id,
         CROSS_DAY_row.EMPLOYEE_CODE,
         CROSS_DAY_row.begin_date,
         CROSS_DAY_row.end_date,
         CROSS_DAY_row.BEGIN_TIME,
         CROSS_DAY_row.BEGIN_TIME,
         CROSS_DAY_row.TMR_DAY_FLAG,
         CROSS_DAY_row.OFF_DUTY_FLAG,
         CROSS_DAY_row.CLASS_SYSTEM,
         CROSS_DAY_row.CREATE_TM,
         CROSS_DAY_row.NODE_KEY,
         CROSS_DAY_row.STATE_FLG);

      insert into tt_sap_synchronous
        (ID,
         EMP_CODE,
         BEGIN_DATE,
         END_DATE,
         BEGIN_TM,
         END_TM,
         TMR_DAY_FLAG,
         OFF_DUTY_FLAG,
         CLASS_SYSTEM,
         CREATE_TM,
         NODE_KEY,
         STATE_FLG)
      values
        (SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL,
         CROSS_DAY_row.EMPLOYEE_CODE,
         CROSS_DAY_row.begin_date,
         CROSS_DAY_row.end_date,
         '000000',
          CROSS_DAY_row.END_TIME,
         'X',
         CROSS_DAY_row.OFF_DUTY_FLAG,
         CROSS_DAY_row.CLASS_SYSTEM,
         CROSS_DAY_row.CREATE_TM,
         CROSS_DAY_row.NODE_KEY,
         CROSS_DAY_row.STATE_FLG);
    end loop;
    COMMIT;
  END;
--记录当前更新时间
  update TT_SAP_SYNCHRONOUS_RECORD
     set SYNCHRONIZATION_TIME = sysdate
   where PROCEDURES_NAME = 'SCHEDULING_SAP_SYNCHRONIZATION';

     update TT_SCHEDULE_DAILY
     set SYNCHRO_STATUS = 1
   where DAY_OF_MONTH <= to_char(sysdate,'yyyymmdd')  and EMP_POST_TYPE = '2';

end SCHEDULING_SAP_SYNCHRONIZATION;
/
prompt
prompt Creating procedure OPERATION_TO_THE_INTERFACE 
prompt =================================================
prompt
create or replace procedure OPERATION_TO_THE_INTERFACE as
  --the_synchronization_time varchar2(25);
begin
  -- 查询上次同步时间
  --select to_char(SYNCHRONIZATION_TIME, 'yyyy-mm-dd hh24:mi:ss')
   -- into the_synchronization_time
   -- from TT_SAP_SYNCHRONOUS_RECORD
  -- where PROCEDURES_NAME = 'OPERATION_TO_THE_INTERFACE';
  DECLARE
    CURSOR OPERATION_SCH_CUR IS
      select SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL as id,
             EMPLOYEE_CODE,
             begin_date,
             end_date,
             begin_time,
             end_time,
             TMR_DAY_FLAG,
             OFF_DUTY_FLAG,
             CLASS_SYSTEM,
             CREATE_TM,
             NODE_KEY,
             STATE_FLG,
             ERROR_INFO
        from (select td.emp_code EMPLOYEE_CODE,
                     to_char(td.shedule_dt, 'yyyymmdd') as begin_date,
                     to_char(td.shedule_dt, 'yyyymmdd') as end_date,
                     decode(td.shedule_code, '休', '', to_char(replace(lpad(ts.start1_time,5,'0') || '00',':',''))) as begin_time,
                     decode(td.shedule_code, '休', '',  to_char(replace(lpad(ts.end1_time,5,'0') || '00',':','')))as end_time,
                     '' as TMR_DAY_FLAG,
                     decode(ts.start1_time, null, 'OFF', '') as OFF_DUTY_FLAG,
                     '2' as CLASS_SYSTEM,
                     sysdate as CREATE_TM,
                     '' as NODE_KEY,
                     0 as STATE_FLG,
                     ''as ERROR_INFO
                from tt_pb_shedule_by_day     td,
                     tt_pb_shedule_by_month   tm,
                     TM_PB_SCHEDULE_BASE_INFO ts,
                     TM_OSS_EMPLOYEE          emp
               where td.dept_id = tm.dept_id
                 and tm.id = td.shedule_mon_id
                 and tm.dept_id = td.dept_id
                 and td.dept_id = ts.dept_id(+)
                 and emp.emp_code = td.emp_code
                 and emp.work_type != 6
                 and tm.emp_code = td.emp_code
                 and tm.commit_status = 1
                 and td.synchro_status = 0
                 and td.shedule_code = ts.schedule_code(+)
                 and to_char(td.shedule_dt, 'yyyymmdd') <= to_char(sysdate,'yyyymmdd')
              union
              select td.emp_code EMPLOYEE_CODE,
                     to_char(td.shedule_dt, 'yyyymmdd') as begin_date,
                     to_char(td.shedule_dt, 'yyyymmdd') as end_date,
                     decode(td.shedule_code, '休', '', to_char(replace(lpad(ts.start2_time,5,'0') || '00',':',''))) as begin_time,
                     decode(td.shedule_code, '休', '',  to_char(replace(lpad(ts.end2_time,5,'0') || '00',':','')))as end_time,
                     '' as TMR_DAY_FLAG,
                     decode(ts.start2_time, null, 'OFF', '') as OFF_DUTY_FLAG,
                     '2' as CLASS_SYSTEM,
                     sysdate as CREATE_TM,
                     '' as NODE_KEY,
                     0 as STATE_FLG,
                     ''as ERROR_INFO
                from tt_pb_shedule_by_day     td,
                     tt_pb_shedule_by_month   tm,
                     TM_PB_SCHEDULE_BASE_INFO ts,
                     TM_OSS_EMPLOYEE          emp
               where td.dept_id = tm.dept_id
                 and tm.id = td.shedule_mon_id
                 and emp.emp_code = td.emp_code
                 and td.dept_id = ts.dept_id(+)
                 and emp.work_type != 6
                 and tm.dept_id = td.dept_id
                 and tm.emp_code = td.emp_code
                 and tm.commit_status = 1
                 and td.synchro_status = 0
                 and td.shedule_code = ts.schedule_code(+)
                 and ts.start2_time is not null
                 and to_char(td.shedule_dt, 'yyyymmdd') <= to_char(sysdate,'yyyymmdd')
              union
              select td.emp_code EMPLOYEE_CODE,
                     to_char(td.shedule_dt, 'yyyymmdd') as begin_date,
                     to_char(td.shedule_dt, 'yyyymmdd') as end_date,
                      decode(td.shedule_code, '休', '', to_char(replace(lpad(ts.start3_time,5,'0') || '00',':',''))) as begin_time,
                     decode(td.shedule_code, '休', '',  to_char(replace(lpad(ts.end3_time,5,'0') || '00',':','')))as end_time,
                     '' as TMR_DAY_FLAG,
                     decode(ts.start3_time, null, 'OFF', '') as OFF_DUTY_FLAG,
                     '2' as CLASS_SYSTEM,
                     sysdate as CREATE_TM,
                     '' as NODE_KEY,
                     0 as STATE_FLG,
                     '' as ERROR_INFO
                from tt_pb_shedule_by_day     td,
                     tt_pb_shedule_by_month   tm,
                     TM_PB_SCHEDULE_BASE_INFO ts,
                     TM_OSS_EMPLOYEE          emp
               where td.dept_id = tm.dept_id
                 and tm.id = td.shedule_mon_id
                  and td.dept_id = ts.dept_id(+)
                 and emp.emp_code = td.emp_code
                 and emp.work_type != 6
                 and tm.dept_id = td.dept_id
                 and tm.emp_code = td.emp_code
                 and tm.commit_status = 1
                 and td.synchro_status = 0
                 and td.shedule_code = ts.schedule_code(+)
                  and to_char(td.shedule_dt, 'yyyymmdd') <= to_char(sysdate,'yyyymmdd')
                 and ts.start3_time is not null);

    TYPE rec IS TABLE OF TT_SAP_SYNCHRONOUS%ROWTYPE;
    recs rec;

  BEGIN
    OPEN OPERATION_SCH_CUR;
    WHILE (TRUE) LOOP
      FETCH OPERATION_SCH_CUR BULK COLLECT
        INTO recs LIMIT 1000;
      FORALL i IN 1 .. recs.COUNT
        INSERT INTO TT_SAP_SYNCHRONOUS VALUES recs (i);
      COMMIT;

      EXIT WHEN OPERATION_SCH_CUR%NOTFOUND;
    END LOOP;
    CLOSE OPERATION_SCH_CUR;
  END;

  update  tt_pb_shedule_by_day  set  synchro_status = 1  where id in(select distinct td.id
                from tt_pb_shedule_by_day     td,
                     tt_pb_shedule_by_month   tm,
                     TM_PB_SCHEDULE_BASE_INFO ts,
                     TM_OSS_EMPLOYEE          emp
              where td.dept_id = tm.dept_id
                 and tm.id = td.shedule_mon_id
                 and emp.emp_code = td.emp_code
                 and td.dept_id = ts.dept_id(+)
                 and emp.work_type != 6
                 and tm.dept_id = td.dept_id
                 and tm.emp_code = td.emp_code
                 and tm.commit_status = 1
                 and td.synchro_status = 0
                 and td.shedule_code = ts.schedule_code(+)
                 and to_char(td.shedule_dt, 'yyyymmdd') <= to_char(sysdate,'yyyymmdd'));
  --记录当前更新时间
 -- update TT_SAP_SYNCHRONOUS_RECORD
  ---   set SYNCHRONIZATION_TIME = sysdate
  ---   where PROCEDURES_NAME = 'OPERATION_TO_THE_INTERFACE';

end OPERATION_TO_THE_INTERFACE;
/

prompt
prompt Creating procedure WAREHOUSE_SAP_SYNCHRONIZATION 
prompt =================================================
prompt
create or replace procedure WAREHOUSE_SAP_SYNCHRONIZATION is
begin
   --将排班数据表中的数据同步到待发送给SAP接口的数据表中
  DECLARE
    --非跨天的数据库集合
    CURSOR Non_Cross_Day_Cur IS
      select SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL as id,
                    td.employee_code EMPLOYEE_CODE,
                    td.day_of_month as begin_date,
                    td.day_of_month as end_date,
                    decode(td.scheduling_code, '休', '',  to_char(replace(ts.start1_time,':',''),'000000')) as begin_time,
                    decode(td.scheduling_code, '休', '', to_char(replace(ts.end1_time,':',''),'000000')) as end_time,
                     '' as TMR_DAY_FLAG,
                     decode(ts.start1_time, null, 'OFF', '') as OFF_DUTY_FLAG,
                     '2' as CLASS_SYSTEM,
                     sysdate as CREATE_TM,
                     '' as NODE_KEY,
                     0 as STATE_FLG,
                     ''
                from TT_SCHEDULE_DAILY td,
                     TM_PB_SCHEDULE_BASE_INFO ts,
                     TM_OSS_EMPLOYEE          emp,
                     (select distinct dept_code from tm_department t_dept,TM_PB_SCHEDULE_BASE_INFO t_sch where t_dept.dept_id = t_sch.dept_id) dept
               where
                     td.department_code = dept.dept_code(+)
                 and emp.emp_code = td.employee_code
                 and td.synchro_status = 0
                 and td.emp_post_type = '3'
                 and td.scheduling_code = ts.schedule_code(+)
                 and (instr(decode(td.scheduling_code, '休', '2', '1'),'2')>0 or   ts.IS_CROSS_DAY = '0')
                 and td.day_of_month <= to_char(sysdate,'yyyymmdd');


    --跨天的数据集合
    CURSOR Cross_Day_Cur IS
      select SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL as id,
                    td.employee_code EMPLOYEE_CODE,
                    td.day_of_month as begin_date,
                    td.day_of_month as end_date,
                     decode(td.scheduling_code, '休', '',  to_char(replace(ts.start1_time,':',''),'000000')) as begin_time,
                    decode(td.scheduling_code, '休', '', to_char(replace(ts.end1_time,':',''),'000000')) as end_time,
                     '' as TMR_DAY_FLAG,
                     decode(ts.start1_time, null, 'OFF', '') as OFF_DUTY_FLAG,
                     '2' as CLASS_SYSTEM,
                     sysdate as CREATE_TM,
                     '' as NODE_KEY,
                     0 as STATE_FLG,
                     ''
                from TT_SCHEDULE_DAILY td,
                     TM_PB_SCHEDULE_BASE_INFO ts,
                     TM_OSS_EMPLOYEE          emp,
                     (select distinct dept_code from tm_department t_dept,TM_PB_SCHEDULE_BASE_INFO t_sch where t_dept.dept_id = t_sch.dept_id) dept

               where
                     td.department_code = dept.dept_code(+)
                 and emp.emp_code = td.employee_code
                 and td.synchro_status = 0
                 and td.emp_post_type = '3'
                 and td.scheduling_code = ts.schedule_code(+)
                 and ts.IS_CROSS_DAY = '1'
                 and instr(decode(td.scheduling_code, '休', '2', '1'),'1')>0
                 and td.day_of_month <= to_char(sysdate,'yyyymmdd');

    TYPE rec IS TABLE OF TT_SAP_SYNCHRONOUS%ROWTYPE;
    recs rec;

    CROSS_DAY_row Cross_Day_Cur%rowtype;
  BEGIN

    --非跨天的数据集合
    OPEN Non_Cross_Day_Cur;
    WHILE (TRUE) LOOP
      FETCH Non_Cross_Day_Cur BULK COLLECT
        INTO recs LIMIT 1000;
      FORALL i IN 1 .. recs.COUNT
        INSERT INTO TT_SAP_SYNCHRONOUS VALUES recs (i);
      COMMIT;

      EXIT WHEN Non_Cross_Day_Cur%NOTFOUND;
    END LOOP;
    CLOSE Non_Cross_Day_Cur;
    --跨天的数据集合
    for CROSS_DAY_row in Cross_Day_Cur loop
      insert into tt_sap_synchronous
        (ID,
         EMP_CODE,
         BEGIN_DATE,
         END_DATE,
         BEGIN_TM,
         END_TM,
         TMR_DAY_FLAG,
         OFF_DUTY_FLAG,
         CLASS_SYSTEM,
         CREATE_TM,
         NODE_KEY,
         STATE_FLG)
      values
        (CROSS_DAY_row.id,
         CROSS_DAY_row.EMPLOYEE_CODE,
         CROSS_DAY_row.begin_date,
         CROSS_DAY_row.end_date,
         CROSS_DAY_row.BEGIN_TIME,
         CROSS_DAY_row.BEGIN_TIME,
         CROSS_DAY_row.TMR_DAY_FLAG,
         CROSS_DAY_row.OFF_DUTY_FLAG,
         CROSS_DAY_row.CLASS_SYSTEM,
         CROSS_DAY_row.CREATE_TM,
         CROSS_DAY_row.NODE_KEY,
         CROSS_DAY_row.STATE_FLG);

      insert into tt_sap_synchronous
        (ID,
         EMP_CODE,
         BEGIN_DATE,
         END_DATE,
         BEGIN_TM,
         END_TM,
         TMR_DAY_FLAG,
         OFF_DUTY_FLAG,
         CLASS_SYSTEM,
         CREATE_TM,
         NODE_KEY,
         STATE_FLG)
      values
        (SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL,
         CROSS_DAY_row.EMPLOYEE_CODE,
         CROSS_DAY_row.begin_date,
         CROSS_DAY_row.end_date,
         '000000',
          CROSS_DAY_row.END_TIME,
         'X',
         CROSS_DAY_row.OFF_DUTY_FLAG,
         CROSS_DAY_row.CLASS_SYSTEM,
         CROSS_DAY_row.CREATE_TM,
         CROSS_DAY_row.NODE_KEY,
         CROSS_DAY_row.STATE_FLG);
    end loop;
    COMMIT;
  END;

     update TT_SCHEDULE_DAILY
     set SYNCHRO_STATUS = 1
   where DAY_OF_MONTH <= to_char(sysdate,'yyyymmdd') and emp_post_type = '3';
end WAREHOUSE_SAP_SYNCHRONIZATION;
/


spool off