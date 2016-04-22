-----------------------------------------------
-- Export file for user PUSHPN               --
-- Created by sfit0505 on 2014/9/15, 9:31:46 --
-----------------------------------------------

spool Initializes procedures.log

prompt
prompt Creating procedure ADD_SCHEDULING_DATA
prompt ======================================
prompt
create or replace procedure add_Scheduling_Data (mth in varchar2,dept in varchar2 ) is
      CURSOR emp_cur IS
      select *
      from (select t.*, rownum rn from tm_employee t )
     where rn > 1
       and rn < 50;
      --backup_name varchar2(200);
begin
     -- backup_name
     --create table  'tt_schedule_daily_'||to_char(sysdate,'yyyymmddhhmiss')   as select * from tt_schedule_daily ;
     --backup_name:= 'tt_schedule_daily_'||to_char(sysdate,'mmddhh');
     --select 'tt_schedule_daily_'||to_char(sysdate,'yyyymmddhhmiss')  into backup_name;
     --execute immediate  'create table  '|| backup_name ||' as select * from tt_schedule_daily';


     FOR emp_row IN  emp_cur

     LOOP
             for i in 1..31 loop

               insert into tt_schedule_daily
                (ID,
                 DEPARTMENT_CODE,
                 BEGIN_TIME,
                 END_TIME,
                 DAY_OF_MONTH,
                 MONTH_ID,
                 EMPLOYEE_CODE,
                 CREATED_EMPLOYEE_CODE,
                 MODIFIED_EMPLOYEE_CODE,
                 CREATE_TIME,
                 MODIFIED_TIME)
              values
                (seq_schedule_datly_s.nextval,
                 dept,
                 '0830',
                 '1800',
                  i,
                 mth,
                 emp_row.emp_code,
                 emp_row.emp_code,
                 emp_row.emp_code,
                 to_date('25-08-2014 10:29:17', 'dd-mm-yyyy hh24:mi:ss'),
                 to_date('25-08-2014 10:29:17', 'dd-mm-yyyy hh24:mi:ss'));
             end loop;
             commit;

             dbms_output.put_line(emp_row.emp_code);
             --dbms_output.put_line(backup_name);

     END LOOP;

end add_Scheduling_Data;
/

prompt
prompt Creating procedure PROC_OSS_SHEDULE_PROCESS_AUTO
prompt ================================================
prompt
create or replace procedure proc_oss_shedule_process_auto
(V_INPUT_DT IN DATE DEFAULT SYSDATE) is

  ---author：Eva Wong
  --created：2014-08-11
  --purpose: 每月25号自动生成排班和工序数据


  v_curren_dt     VARCHAR2(10); --传入日期

BEGIN
  -- 传入日期月份+1
v_curren_dt:= to_char(add_months(V_INPUT_DT,1),'YYYY-MM');

  --删除临时表
  execute immediate 'truncate table tm_validate_emp';

  commit;

--插入有效员工数据

INSERT INTO tm_validate_emp
  ( YM, DEPT_ID, EMP_CODE, VERSION, CREATE_EMP_CODE)
  SELECT
         YM,
         S.DEPT_ID,
         S.EMP_CODE,
         0,
         'system-auto'
    FROM (SELECT EM.EMP_CODE, v_curren_dt ym, EM.DEPT_ID
           FROM TM_OSS_EMPLOYEE EM,
                TM_PB_GROUP_INFO G,
                (SELECT CHANGE_DEPT_ID,
                        EMP_CODE,
                        CHANGE_DEPT_CODE,
                        START_TM,
                        NVL(END_TM, TO_DATE('2114-01-01', 'YYYY-MM-DD')) END_TM
                   FROM (SELECT DEP.DEPT_ID CHANGE_DEPT_ID,
                                CH.EMP_CODE,
                                CH.DEPT_CODE CHANGE_DEPT_CODE,
                                CH.CHANGE_ZONE_TM START_TM,
                                LEAD(CH.CHANGE_ZONE_TM) OVER(PARTITION BY CH.EMP_CODE ORDER BY CH.CHANGE_ZONE_TM) END_TM
                           FROM TI_OSS_HR_EMP_NEW_CHANGEDEPT CH,
                                TM_DEPARTMENT                DEP
                          WHERE CH.DEPT_CODE = DEP.DEPT_CODE
                            AND CH.EMP_STUS = 2)) CHG
          WHERE EM.GROUP_ID = G.GROUP_ID
            AND NVL(EM.DIMISSION_DT, DATE '2114-01-01') >
                TO_DATE(v_curren_dt || '-01', 'yyyy-mm-dd')
            AND EM.EMP_CODE = CHG.EMP_CODE(+)
            AND CHG.CHANGE_DEPT_ID IS NULL)s WHERE EMP_CODE NOT IN (SELECT EMP_CODE
                            FROM TT_PB_SHEDULE_BY_MONTH TM
                           WHERE TM.DEPT_ID = S.DEPT_ID
                             AND YM =v_curren_dt
                             AND TM.EMP_CODE = S.EMP_CODE);
COMMIT;

-- 插入离职或转岗位人员数据

INSERT INTO tm_validate_emp
  ( YM, DEPT_ID, EMP_CODE, VERSION, CREATE_EMP_CODE)
  SELECT
         YM,
         S.CHANGE_DEPT_ID,
         S.EMP_CODE,
         0,
         'system-auto'
    FROM (SELECT EM.EMP_CODE, v_curren_dt YM, CHANGE_DEPT_ID
            FROM TM_OSS_EMPLOYEE EM,
                 TM_PB_GROUP_INFO G,
                 (SELECT CHANGE_DEPT_ID,
                         EMP_CODE,
                         CHANGE_DEPT_CODE,
                         START_TM,
                         NVL(END_TM, TO_DATE('2114-01-01', 'YYYY-MM-DD')) END_TM
                    FROM (SELECT DEP.DEPT_ID CHANGE_DEPT_ID,
                                 CH.EMP_CODE,
                                 CH.DEPT_CODE CHANGE_DEPT_CODE,
                                 CH.CHANGE_ZONE_TM START_TM,
                                 LEAD(CH.CHANGE_ZONE_TM) OVER(PARTITION BY CH.EMP_CODE ORDER BY CH.CHANGE_ZONE_TM) END_TM
                            FROM TI_OSS_HR_EMP_NEW_CHANGEDEPT CH,
                                 TM_DEPARTMENT                DEP
                           WHERE CH.DEPT_CODE = DEP.DEPT_CODE
                             AND CH.EMP_STUS = 2)) CHG
           WHERE EM.GROUP_ID = G.GROUP_ID
             AND NVL(EM.DIMISSION_DT, DATE '2114-01-01') >
                 TO_DATE(v_curren_dt || '-01', 'yyyy-mm-dd')
             AND EM.EMP_CODE = CHG.EMP_CODE(+)
             AND CHG.CHANGE_DEPT_ID IS NOT NULL
             AND ((TO_DATE(v_curren_dt || '-01', 'yyyy-mm-dd') >= START_TM AND
                 TO_DATE(v_curren_dt || '-01', 'yyyy-mm-dd') < END_TM) OR
                 (START_TM >= TO_DATE(v_curren_dt || '-01', 'yyyy-mm-dd')  AND
                  START_TM <=
                  LAST_DAY(TO_DATE(v_curren_dt || '-01', 'yyyy-mm-dd'))))) S
   WHERE EMP_CODE NOT IN (SELECT EMP_CODE
                            FROM TT_PB_SHEDULE_BY_MONTH TM
                           WHERE TM.DEPT_ID = S.CHANGE_DEPT_ID
                             AND YM =v_curren_dt
                             AND TM.EMP_CODE = S.EMP_CODE);
COMMIT;


-- 插入临时表数据到排班月份表
INSERT INTO TT_PB_SHEDULE_BY_MONTH
  (ID, YM, DEPT_ID, EMP_CODE, VERSION, CREATE_TM, CREATE_EMP_CODE)
  SELECT SEQ_OSS_BASE.NEXTVAL,
         T.YM,
         T.DEPT_ID,
         T.EMP_CODE,
         T.VERSION,
         SYSDATE,
         T.CREATE_EMP_CODE
    FROM TM_VALIDATE_EMP T;
COMMIT;

--插入排班月份表数据到排班day 表

INSERT INTO TT_PB_SHEDULE_BY_DAY
  (ID, DEPT_ID, SHEDULE_DT, EMP_CODE, SHEDULE_MON_ID)
  SELECT SEQ_OSS_BASE.NEXTVAL,
         T.DEPT_ID,
         TO_DATE(T.YM || '-01', 'yyyy-mm-dd'),
         T.EMP_CODE,
         T.ID
    FROM TT_PB_SHEDULE_BY_MONTH T
   WHERE YM = V_CURREN_DT
     AND CREATE_EMP_CODE = 'system-auto';
COMMIT;

-- 插入临时表数据导工序月份表
INSERT INTO TT_PB_PROCESS_BY_MONTH
  (ID, YM, DEPT_ID, EMP_CODE, VERSION, CREATE_TM, CREATE_EMP_CODE)
  SELECT SEQ_OSS_BASE.NEXTVAL,
         T.YM,
         T.DEPT_ID,
         T.EMP_CODE,
         T.VERSION,
         SYSDATE,
         T.CREATE_EMP_CODE
    FROM TM_VALIDATE_EMP T;
COMMIT;


--插入工序月份表数据到工序day 表

  INSERT INTO TT_PB_PROCESS_BY_DAY
    (ID, DEPT_ID, PROCESS_DT, EMP_CODE, PROCESS_MON_ID)
    SELECT SEQ_OSS_BASE.NEXTVAL,
           T.DEPT_ID,
           TO_DATE(T.YM || '-01', 'yyyy-mm-dd'),
           T.EMP_CODE,
           T.ID
      FROM TT_PB_PROCESS_BY_MONTH T
     WHERE YM = V_CURREN_DT
       AND CREATE_EMP_CODE = 'system-auto';
  COMMIT;




exception
  when others then
    rollback;
 -- 添加异常日志
    commit;

end proc_oss_shedule_process_auto;
/

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
             (select DEPT_ID
                from TM_OSS_EMPLOYEE tm, TT_SCH_EMP_ATTENCE_CLASS tt
               where tm.emp_code = tt.emp_code) as DEPARTMENT_CODE,
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
             0 as STATE_FLG
        from TT_SCHEDULE_DAILY t
       where ( SYNCHRO_STATUS != 1  or  SYNCHRO_STATUS is null) and CROSS_DAY_TYPE is null
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
             0 as STATE_FLG
        from TT_SCHEDULE_DAILY t
       where  ( SYNCHRO_STATUS != 1  or  SYNCHRO_STATUS is null)
         and CROSS_DAY_TYPE = 'X'
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
   where DAY_OF_MONTH <= to_char(sysdate,'yyyymmdd');

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
             STATE_FLG
        from (select td.emp_code EMPLOYEE_CODE,
                     to_char(td.shedule_dt, 'yyyymmdd') as begin_date,
                     to_char(td.shedule_dt, 'yyyymmdd') as end_date,
                    decode(td.shedule_code, '休', '', (replace(ts.start1_time,':',''))|| '00' ) as begin_time,
                    decode(td.shedule_code, '休', '', (replace(ts.start1_time,':',''))|| '00' ) as end_time, 
                     '' as TMR_DAY_FLAG,
                     decode(ts.start1_time, null, 'OFF', '') as OFF_DUTY_FLAG,
                     '2' as CLASS_SYSTEM,
                     sysdate as CREATE_TM,
                     '' as NODE_KEY,
                     0 as STATE_FLG
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
                     decode(ts.start2_time, '', '', (replace(ts.start2_time,':',''))|| '00' )  as begin_time,
                     decode(ts.end2_time, '', '', (replace( ts.end2_time,':',''))|| '00' ) as end_time,
                     '' as TMR_DAY_FLAG,
                     decode(ts.start2_time, null, 'OFF', '') as OFF_DUTY_FLAG,
                     '2' as CLASS_SYSTEM,
                     sysdate as CREATE_TM,
                     '' as NODE_KEY,
                     0 as STATE_FLG
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
                     decode(ts.start3_time, '', '', (replace(ts.start3_time,':',''))|| '00' )  as begin_time,
                     decode(ts.end3_time, '', '', (replace( ts.end3_time,':',''))|| '00' ) as end_time,
                     '' as TMR_DAY_FLAG,
                     decode(ts.start3_time, null, 'OFF', '') as OFF_DUTY_FLAG,
                     '2' as CLASS_SYSTEM,
                     sysdate as CREATE_TM,
                     '' as NODE_KEY,
                     0 as STATE_FLG
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

spool off
