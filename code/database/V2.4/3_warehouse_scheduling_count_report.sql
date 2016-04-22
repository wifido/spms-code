INSERT INTO ts_module
  (MODULE_ID,
   PARENT_ID,
   MODULE_NAME,
   MODULE_CODE,
   MODULE_DESC,
   MODULE_ICON,
   MODULE_TYPE,
   APP_TYPE,
   ACTION_URL,
   SORT,
   BUNDLE_ID,
   HELP_URL)
VALUES
  (SEQ_TS_Module.nextval,
   (SELECT m.module_id
      FROM ts_module m
     WHERE m.module_code='warehouse'),
   '分点部考勤分析报表',
   'warehouse_scheduling_count_report',
   '分点部考勤分析报表',
   '',
   '4',
   '1',
   '/warehouse/toWarehouseSchedulingAnalysisReportPage.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID =
           (SELECT T.MODULE_ID
              FROM TS_MODULE T
             WHERE T.MODULE_CODE = 'warehouse')),
   null,
   '/index.htm');

INSERT INTO ts_module
  (MODULE_ID,
   PARENT_ID,
   MODULE_NAME,
   MODULE_CODE,
   MODULE_DESC,
   MODULE_ICON,
   MODULE_TYPE,
   APP_TYPE,
   ACTION_URL,
   SORT,
   BUNDLE_ID,
   HELP_URL)
VALUES
  (SEQ_TS_Module.nextval,
   (SELECT m.module_id
      FROM ts_module m
     WHERE m.module_code='warehouse_scheduling_count_report'),
   '查询',
   'query_schedulingAnalysisReport',
   '查询',
   '',
   '7',
   '1',
   '/schedulingMgt/query_schedulingAnalysisReport.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID =
           (SELECT T.MODULE_ID
              FROM TS_MODULE T
             WHERE T.MODULE_CODE = 'warehouse_scheduling_count_report')),
   null,
   '/index.htm');  


INSERT INTO ts_module
  (MODULE_ID,
   PARENT_ID,
   MODULE_NAME,
   MODULE_CODE,
   MODULE_DESC,
   MODULE_ICON,
   MODULE_TYPE,
   APP_TYPE,
   ACTION_URL,
   SORT,
   BUNDLE_ID,
   HELP_URL)
VALUES
  (SEQ_TS_Module.nextval,
   (SELECT m.module_id
      FROM ts_module m
     WHERE m.module_code='warehouse_scheduling_count_report'),
   '导出',
   'export_schedulingAnalysisReport',
   '导出',
   '',
   '7',
   '1',
   '/schedulingMgt/export_schedulingAnalysisReport.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID =
           (SELECT T.MODULE_ID
              FROM TS_MODULE T
             WHERE T.MODULE_CODE = 'warehouse_scheduling_count_report')),
   null,
   '/index.htm');  
   
commit;
   
-- Create table
create table TT_WAREH_SCHEDULED_REPORT
(
  ID                 NUMBER(38) not null,
  DEPARTMENT_CODE    VARCHAR2(38),
  SCHE_MONTH         VARCHAR2(10),
  SCHE_TOTAL_EMP_NUM NUMBER(10),
  SCHE_A_EMP_NUM     NUMBER(10),
  SCHE_A_DAYS        NUMBER(10),
  SCHE_C_EMP_NUM     NUMBER(10),
  SCHE_C_DAYS        NUMBER(10),
  SCHE_A_HOURS       NUMBER(10,2),
  SCHE_C_HOURS       NUMBER(10,2),
  CREATE_TIME        DATE default sysdate
);
-- Add comments to the columns 
comment on column TT_WAREH_SCHEDULED_REPORT.ID
  is '主键ID';
comment on column TT_WAREH_SCHEDULED_REPORT.DEPARTMENT_CODE
  is '网点编码';
comment on column TT_WAREH_SCHEDULED_REPORT.SCHE_MONTH
  is '统计的排班月份';
comment on column TT_WAREH_SCHEDULED_REPORT.SCHE_TOTAL_EMP_NUM
  is '总的排班人数';
comment on column TT_WAREH_SCHEDULED_REPORT.SCHE_A_EMP_NUM
  is '全日制排班人数';
comment on column TT_WAREH_SCHEDULED_REPORT.SCHE_A_DAYS
  is '全日制排班总天数';
comment on column TT_WAREH_SCHEDULED_REPORT.SCHE_C_EMP_NUM
  is '非全日制排班人数';
comment on column TT_WAREH_SCHEDULED_REPORT.SCHE_C_DAYS
  is '非全日制排总天数';
comment on column TT_WAREH_SCHEDULED_REPORT.SCHE_A_HOURS
  is '全日制排班小时数';
comment on column TT_WAREH_SCHEDULED_REPORT.SCHE_C_HOURS
  is '非全日制排班小时数';
comment on column TT_WAREH_SCHEDULED_REPORT.CREATE_TIME
  is '创建时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TT_WAREH_SCHEDULED_REPORT
  add primary key (ID);
  
-- Create sequence 
create sequence SEQ_TT_WAREH_SCHEDULED_REPORT
minvalue 1
maxvalue 999999999999999999999
start with 21
increment by 1
cache 20;

CREATE OR REPLACE PROCEDURE WAREH_COUNT_SCHE_REPORT(V_MONTH           IN VARCHAR2,
                                                    V_DEPARTMENT_CODE IN VARCHAR2) IS

  --*************************************************************
  -- AUTHOR  : HGX
  -- CREATED : 2015-07-16
  -- PURPOSE : 统计仓管排班分析报表
  --
  -- PARAMETER:
  -- NAME  V_MONTH            TYPE     VARCHAR2
  -- NAME  V_DEPARTMENT_CODE            TYPE     VARCHAR2
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************

  --1.定义执行序号
  L_CALL_NO NUMBER;

  V_EMP_POST_TYPE      NUMBER;
  V_IS_HAVE_COMMISSION NUMBER;
  V_EMP_TOTAL_NUM      NUMBER;
  V_EMP_A_NUM          NUMBER;
  V_EMP_C_NUM          NUMBER;
  V_A_SCHEDULING_DAYS  NUMBER;
  V_C_SCHEDULING_DAYS  NUMBER;
  V_A_SCHEDULING_HOURS NUMBER(10, 2);
  V_C_SCHEDULING_HOURS NUMBER(10, 2);
  REST_STR             VARCHAR2(10);
  DATA_FORMAT          VARCHAR2(20);

BEGIN

  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'WAREH_COUNT_SCHE_REPORT',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

  V_EMP_POST_TYPE      := 3;
  V_IS_HAVE_COMMISSION := 1;
  REST_STR             := '休';
  DATA_FORMAT          := 'YYYYMM';

  FOR DEPT_ROW IN (SELECT T.DEPARTMENT_CODE
                     FROM TT_SCHEDULE_DAILY T,
                          TM_OSS_EMPLOYEE E,
                          (SELECT DEPT_CODE, DEPT_ID
                             FROM TM_DEPARTMENT
                            WHERE DELETE_FLG = 0
                            START WITH DEPT_CODE = V_DEPARTMENT_CODE
                           CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE) D
                    WHERE T.EMPLOYEE_CODE = E.EMP_CODE
                      AND T.DEPARTMENT_CODE = D.DEPT_CODE
                      AND E.DEPT_ID = D.DEPT_ID
                      AND (E.DATE_FROM IS NULL OR
                          E.DATE_FROM < TO_DATE(V_MONTH, DATA_FORMAT) OR
                          E.DATE_FROM >=
                          ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
                      AND (E.DIMISSION_DT IS NULL OR
                          E.DIMISSION_DT < TO_DATE(V_MONTH, DATA_FORMAT) OR
                          E.DIMISSION_DT >=
                          ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
                      AND (E.TRANSFER_DATE IS NULL OR
                          E.TRANSFER_DATE < TO_DATE(V_MONTH, DATA_FORMAT) OR
                          E.TRANSFER_DATE >=
                          ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
                      AND E.IS_HAVE_COMMISSION = V_IS_HAVE_COMMISSION
                      AND T.MONTH_ID = V_MONTH
                      AND T.EMP_POST_TYPE = V_EMP_POST_TYPE
                    GROUP BY T.DEPARTMENT_CODE) LOOP
  
    ----总员工数
    SELECT COUNT(*)
      INTO V_EMP_TOTAL_NUM
      FROM (SELECT T.EMPLOYEE_CODE
              FROM TT_SCHEDULE_DAILY T, TM_DEPARTMENT D, TM_OSS_EMPLOYEE E
             WHERE T.EMPLOYEE_CODE = E.EMP_CODE
               AND T.DEPARTMENT_CODE = D.DEPT_CODE
               AND D.DEPT_ID = E.DEPT_ID
               AND (E.DATE_FROM IS NULL OR
                   E.DATE_FROM < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.DATE_FROM >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND (E.DIMISSION_DT IS NULL OR
                   E.DIMISSION_DT < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.DIMISSION_DT >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND (E.TRANSFER_DATE IS NULL OR
                   E.TRANSFER_DATE < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.TRANSFER_DATE >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND E.IS_HAVE_COMMISSION = V_IS_HAVE_COMMISSION
               AND T.MONTH_ID = V_MONTH
               AND T.EMP_POST_TYPE = V_EMP_POST_TYPE
               AND T.DEPARTMENT_CODE = DEPT_ROW.DEPARTMENT_CODE
             GROUP BY T.EMPLOYEE_CODE);
  
    ----全日制总员工数
  
    SELECT COUNT(*)
      INTO V_EMP_A_NUM
      FROM (SELECT T.EMPLOYEE_CODE
              FROM TT_SCHEDULE_DAILY T, TM_DEPARTMENT D, TM_OSS_EMPLOYEE E
             WHERE T.EMPLOYEE_CODE = E.EMP_CODE
               AND T.DEPARTMENT_CODE = D.DEPT_CODE
               AND D.DEPT_ID = E.DEPT_ID
               AND (E.DATE_FROM IS NULL OR
                   E.DATE_FROM < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.DATE_FROM >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND (E.DIMISSION_DT IS NULL OR
                   E.DIMISSION_DT < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.DIMISSION_DT >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND (E.TRANSFER_DATE IS NULL OR
                   E.TRANSFER_DATE < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.TRANSFER_DATE >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND E.IS_HAVE_COMMISSION = V_IS_HAVE_COMMISSION
               AND E.PERSG = 'A'
               AND T.MONTH_ID = V_MONTH
               AND T.EMP_POST_TYPE = V_EMP_POST_TYPE
               AND T.DEPARTMENT_CODE = DEPT_ROW.DEPARTMENT_CODE
             GROUP BY T.EMPLOYEE_CODE);
  
    ----非全日制总员工数
  
    SELECT COUNT(*)
      INTO V_EMP_C_NUM
      FROM (SELECT T.EMPLOYEE_CODE
              FROM TT_SCHEDULE_DAILY T, TM_DEPARTMENT D, TM_OSS_EMPLOYEE E
             WHERE T.EMPLOYEE_CODE = E.EMP_CODE
               AND T.DEPARTMENT_CODE = D.DEPT_CODE
               AND D.DEPT_ID = E.DEPT_ID
               AND (E.DATE_FROM IS NULL OR
                   E.DATE_FROM < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.DATE_FROM >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND (E.DIMISSION_DT IS NULL OR
                   E.DIMISSION_DT < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.DIMISSION_DT >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND (E.TRANSFER_DATE IS NULL OR
                   E.TRANSFER_DATE < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.TRANSFER_DATE >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND E.IS_HAVE_COMMISSION = V_IS_HAVE_COMMISSION
               AND E.PERSG = 'C'
               AND T.MONTH_ID = V_MONTH
               AND T.EMP_POST_TYPE = V_EMP_POST_TYPE
               AND T.DEPARTMENT_CODE = DEPT_ROW.DEPARTMENT_CODE
             GROUP BY T.EMPLOYEE_CODE);
  
    -----全日制排班总天数
    SELECT COUNT(*)
      INTO V_A_SCHEDULING_DAYS
      FROM (SELECT T.EMPLOYEE_CODE, T.DAY_OF_MONTH, COUNT(T.SCHEDULING_CODE)
              FROM TT_SCHEDULE_DAILY T, TM_DEPARTMENT D, TM_OSS_EMPLOYEE E
             WHERE T.EMPLOYEE_CODE = E.EMP_CODE
               AND T.DEPARTMENT_CODE = D.DEPT_CODE
               AND D.DEPT_ID = E.DEPT_ID
               AND (E.DATE_FROM IS NULL OR
                   E.DATE_FROM < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.DATE_FROM >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND (E.DIMISSION_DT IS NULL OR
                   E.DIMISSION_DT < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.DIMISSION_DT >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND (E.TRANSFER_DATE IS NULL OR
                   E.TRANSFER_DATE < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.TRANSFER_DATE >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND E.IS_HAVE_COMMISSION = V_IS_HAVE_COMMISSION
               AND E.PERSG = 'A'
               AND T.MONTH_ID = V_MONTH
               AND T.EMP_POST_TYPE = V_EMP_POST_TYPE
               AND T.SCHEDULING_CODE <> REST_STR
               AND T.DEPARTMENT_CODE = DEPT_ROW.DEPARTMENT_CODE
             GROUP BY T.EMPLOYEE_CODE, T.DAY_OF_MONTH);
  
    -----非全日制排班总天数
    SELECT COUNT(*)
      INTO V_C_SCHEDULING_DAYS
      FROM (SELECT T.EMPLOYEE_CODE, T.DAY_OF_MONTH, COUNT(T.SCHEDULING_CODE)
              FROM TT_SCHEDULE_DAILY T, TM_DEPARTMENT D, TM_OSS_EMPLOYEE E
             WHERE T.EMPLOYEE_CODE = E.EMP_CODE
               AND T.DEPARTMENT_CODE = D.DEPT_CODE
               AND D.DEPT_ID = E.DEPT_ID
               AND (E.DATE_FROM IS NULL OR
                   E.DATE_FROM < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.DATE_FROM >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND (E.DIMISSION_DT IS NULL OR
                   E.DIMISSION_DT < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.DIMISSION_DT >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND (E.TRANSFER_DATE IS NULL OR
                   E.TRANSFER_DATE < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.TRANSFER_DATE >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND E.IS_HAVE_COMMISSION = V_IS_HAVE_COMMISSION
               AND E.PERSG = 'C'
               AND T.MONTH_ID = V_MONTH
               AND T.EMP_POST_TYPE = V_EMP_POST_TYPE
               AND T.SCHEDULING_CODE <> REST_STR
               AND T.DEPARTMENT_CODE = DEPT_ROW.DEPARTMENT_CODE
             GROUP BY T.EMPLOYEE_CODE, T.DAY_OF_MONTH);
  
    -----全日制排班总天数
  
    SELECT NVL(SUM(COUNT_TIME_DISTANCE(START1_TIME, END1_TIME)), 0)
      INTO V_A_SCHEDULING_HOURS
      FROM (SELECT T.EMPLOYEE_CODE,
                   T.DAY_OF_MONTH,
                   T.SCHEDULING_CODE,
                   B.START1_TIME,
                   B.END1_TIME
              FROM TT_SCHEDULE_DAILY        T,
                   TM_DEPARTMENT            D,
                   TM_OSS_EMPLOYEE          E,
                   TM_PB_SCHEDULE_BASE_INFO B
             WHERE T.EMPLOYEE_CODE = E.EMP_CODE
               AND T.DEPARTMENT_CODE = D.DEPT_CODE
               AND T.SCHEDULING_CODE = B.SCHEDULE_CODE
               AND D.DEPT_ID = B.DEPT_ID
               AND D.DEPT_ID = E.DEPT_ID
               AND (E.DATE_FROM IS NULL OR
                   E.DATE_FROM < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.DATE_FROM >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND (E.DIMISSION_DT IS NULL OR
                   E.DIMISSION_DT < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.DIMISSION_DT >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND (E.TRANSFER_DATE IS NULL OR
                   E.TRANSFER_DATE < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.TRANSFER_DATE >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND E.IS_HAVE_COMMISSION = V_IS_HAVE_COMMISSION
               AND T.MONTH_ID = V_MONTH
               AND T.EMP_POST_TYPE = V_EMP_POST_TYPE
               AND T.SCHEDULING_CODE <> REST_STR
               AND T.DEPARTMENT_CODE = DEPT_ROW.DEPARTMENT_CODE
               AND E.PERSG = 'A'
             GROUP BY T.EMPLOYEE_CODE,
                      T.DAY_OF_MONTH,
                      T.SCHEDULING_CODE,
                      B.START1_TIME,
                      B.END1_TIME);
  
    -----全日制排班总天数
  
    SELECT NVL(SUM(COUNT_TIME_DISTANCE(START1_TIME, END1_TIME)), 0)
      INTO V_C_SCHEDULING_HOURS
      FROM (SELECT T.EMPLOYEE_CODE,
                   T.DAY_OF_MONTH,
                   T.SCHEDULING_CODE,
                   B.START1_TIME,
                   B.END1_TIME
              FROM TT_SCHEDULE_DAILY        T,
                   TM_DEPARTMENT            D,
                   TM_OSS_EMPLOYEE          E,
                   TM_PB_SCHEDULE_BASE_INFO B
             WHERE T.EMPLOYEE_CODE = E.EMP_CODE
               AND T.DEPARTMENT_CODE = D.DEPT_CODE
               AND T.SCHEDULING_CODE = B.SCHEDULE_CODE
               AND D.DEPT_ID = B.DEPT_ID
               AND D.DEPT_ID = E.DEPT_ID
               AND (E.DATE_FROM IS NULL OR
                   E.DATE_FROM < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.DATE_FROM >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND (E.DIMISSION_DT IS NULL OR
                   E.DIMISSION_DT < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.DIMISSION_DT >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND (E.TRANSFER_DATE IS NULL OR
                   E.TRANSFER_DATE < TO_DATE(V_MONTH, DATA_FORMAT) OR
                   E.TRANSFER_DATE >=
                   ADD_MONTHS(TO_DATE(V_MONTH, DATA_FORMAT), 1))
               AND E.IS_HAVE_COMMISSION = V_IS_HAVE_COMMISSION
               AND T.MONTH_ID = V_MONTH
               AND T.EMP_POST_TYPE = V_EMP_POST_TYPE
               AND T.SCHEDULING_CODE <> REST_STR
               AND T.DEPARTMENT_CODE = DEPT_ROW.DEPARTMENT_CODE
               AND E.PERSG = 'C'
             GROUP BY T.EMPLOYEE_CODE,
                      T.DAY_OF_MONTH,
                      T.SCHEDULING_CODE,
                      B.START1_TIME,
                      B.END1_TIME);
  
    DELETE FROM TT_WAREH_SCHEDULED_REPORT T
     WHERE T.DEPARTMENT_CODE = DEPT_ROW.DEPARTMENT_CODE
       AND T.SCHE_MONTH = V_MONTH;
  
    INSERT INTO TT_WAREH_SCHEDULED_REPORT
    VALUES
      (SEQ_TT_WAREH_SCHEDULED_REPORT.NEXTVAL,
       DEPT_ROW.DEPARTMENT_CODE,
       V_MONTH,
       V_EMP_TOTAL_NUM,
       V_EMP_A_NUM,
       V_A_SCHEDULING_DAYS,
       V_EMP_C_NUM,
       V_C_SCHEDULING_DAYS,
       V_A_SCHEDULING_HOURS,
       V_C_SCHEDULING_HOURS,
       SYSDATE);
  
    COMMIT;
  
  END LOOP;

  --4 结束记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'WAREH_COUNT_SCHE_REPORT',
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
                                 'WAREH_COUNT_SCHE_REPORT',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
  
END WAREH_COUNT_SCHE_REPORT;
/

