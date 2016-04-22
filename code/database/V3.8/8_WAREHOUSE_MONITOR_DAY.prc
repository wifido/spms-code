CREATE OR REPLACE PROCEDURE WAREHOUSE_MONITOR_DAY IS
  --*************************************************************
  -- AUTHOR  : SMM
  -- CREATED : 2016-04-08
  -- PURPOSE : 仓管监控
  --
  -- PARAMETER:
  -- NAME             TYPE            DESC
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************

  L_CALL_NO NUMBER;
BEGIN

  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'WAREHOUSE_MONITOR_DAY',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

  EXECUTE IMMEDIATE 'TRUNCATE TABLE TT_WAREHOUSE_DAILY_DAY';

  INSERT INTO TT_WAREHOUSE_DAILY_DAY
    SELECT *
      FROM TT_SCHEDULE_DAILY T
     WHERE T.EMP_POST_TYPE = '3'
       AND T.MONTH_ID = TO_CHAR(SYSDATE, 'YYYYMM')
       AND T.DAY_OF_MONTH >= TO_CHAR(SYSDATE - 1, 'YYYYMMDD')
       AND T.DAY_OF_MONTH <= TO_CHAR(SYSDATE + 6, 'YYYYMMDD');
  COMMIT;

  for V_COUNT IN (SELECT TRUNC(SYSDATE) - 1 + (ROWNUM - 1) S_DATE
                    FROM DUAL
                  CONNECT BY ROWNUM <=
                             TRUNC(SYSDATE) + 7 - TRUNC(SYSDATE) + 1) LOOP
  
    delete TT_DISPATCH_MONITOR_REPORT t
     where t.day_of_month = V_COUNT.S_DATE
       and t.emp_post_type = '3';
  
    INSERT INTO TT_DISPATCH_MONITOR_REPORT
      (DAY_OF_MONTH,
       HQ_CODE,
       AREA_CODE,
       DEPT_CODE,
       DIVISION_CODE,
       FULLTIME_EMP_NUM,
       NOT_FULLTIME_EMP_NUM,
       FULLTIME_SCHEDULING_NUM,
       FULLTIME_REST_NUM,
       NOT_FULLTIME_SCHEDULING_NUM,
       NOT_FULLTIME_REST_NUM,
       FULLTIME_NOT_SCHEDULING,
       NOT_FULLTIME_NOT_SCHEDULING,
       CREATE_DATE,
       TYPE_LEVEL,
       EMP_POST_TYPE)
      WITH A AS
       (SELECT E.EMP_CODE,
               D.DEPT_ID,
               D.DEPT_CODE,
               D.AREA_CODE,
               D.HQ_CODE,
               D.DIVISION_CODE,
               D.TYPE_LEVEL,
               E.PERSG,
               E.Emp_Post_Type,
               DATA_SOURCE
          FROM TM_OSS_EMPLOYEE E
          JOIN TM_DEPARTMENT D
            ON E.DEPT_ID = D.DEPT_ID
         WHERE E.EMP_POST_TYPE = '3'
           AND (E.DIMISSION_DT > V_COUNT.S_DATE OR E.DIMISSION_DT IS NULL)
           AND E.EMP_DUTY_NAME IN ('仓管员', '仓管组长')),
      --在职人数
      ZRS AS
       (SELECT COUNT(1) FULL_TIME,
               A.DEPT_CODE,
               A.AREA_CODE,
               A.HQ_CODE,
               A.DIVISION_CODE,
               A.TYPE_LEVEL
          FROM A
         GROUP BY A.DEPT_CODE,
                  A.AREA_CODE,
                  A.HQ_CODE,
                  A.DIVISION_CODE,
                  A.TYPE_LEVEL),
      --全日制在职人数
      QRZZZ AS
       (SELECT COUNT(1) FULL_TIME,
               A.DEPT_CODE,
               A.AREA_CODE,
               A.HQ_CODE,
               A.DIVISION_CODE
          FROM A
         WHERE PERSG = 'A'
         GROUP BY A.DEPT_CODE, A.AREA_CODE, A.HQ_CODE, A.DIVISION_CODE),
      -- 非全在职
      FQZZZ AS
       (SELECT COUNT(1) NO_FULL_TIME, A.DEPT_CODE
          FROM A
         WHERE A.PERSG = 'C'
         GROUP BY A.DEPT_CODE),
      
      --当前网点实际全日制排班人数
      QRZPBD AS
       (SELECT DA.DEPARTMENT_CODE DEPT_CODE,
               COUNT(DISTINCT DA.EMPLOYEE_CODE) FULL_TIME_SCHE_COUNT
          FROM TT_WAREHOUSE_DAILY_DAY DA
          JOIN A
            ON DA.DEPARTMENT_CODE = A.DEPT_CODE
           AND A.EMP_CODE = DA.EMPLOYEE_CODE
         WHERE A.PERSG = 'A'
           AND DA.EMP_POST_TYPE = '3'
           AND DA.SCHEDULING_CODE <> '休'
         GROUP BY DA.DEPARTMENT_CODE),
      
      --当前网点实际全日制排休人数
      QRZPXD AS
       (SELECT DA.DEPARTMENT_CODE DEPT_CODE,
               COUNT(DISTINCT DA.EMPLOYEE_CODE) FULL_TIME_SCHE_COUNT
          FROM TT_WAREHOUSE_DAILY_DAY DA
          JOIN A
            ON DA.DEPARTMENT_CODE = A.DEPT_CODE
           AND A.EMP_CODE = DA.EMPLOYEE_CODE
         WHERE A.PERSG = 'A'
           AND DA.EMP_POST_TYPE = '3'
           AND DA.SCHEDULING_CODE = '休'        
           AND DA.DAY_OF_MONTH = TO_CHAR(V_COUNT.S_DATE, 'YYYYMMDD')
         GROUP BY DA.DEPARTMENT_CODE),
      -- 当前网点实际非全排班数
      FQRZPBD AS
       (SELECT t.DEPARTMENT_CODE DEPT_CODE,
               COUNT(DISTINCT t.EMPLOYEE_CODE) NO_FULL_TIME_SCHE_COUNT
          FROM TT_WAREHOUSE_DAILY_DAY t
          JOIN A
            ON t.DEPARTMENT_CODE = A.DEPT_CODE
           AND A.EMP_CODE = t.EMPLOYEE_CODE
         WHERE A.PERSG = 'C'
           AND t.DAY_OF_MONTH = TO_CHAR(V_COUNT.S_DATE, 'YYYYMMDD')
           AND t.EMP_POST_TYPE = '3'
           AND t.SCHEDULING_CODE <> '休'
         GROUP BY t.DEPARTMENT_CODE),
      --当前网点实际非全日制排休
      FQRZPXD AS
       (SELECT t.DEPARTMENT_CODE DEPT_CODE,
               COUNT(DISTINCT t.EMPLOYEE_CODE) NO_FULL_TIME_SCHE_COUNT
          FROM TT_WAREHOUSE_DAILY_DAY t
          JOIN A
            ON t.DEPARTMENT_CODE = A.DEPT_CODE
           AND A.EMP_CODE = t.EMPLOYEE_CODE
         WHERE A.PERSG = 'C'
           AND t.DAY_OF_MONTH = TO_CHAR(V_COUNT.S_DATE, 'YYYYMMDD')
           AND t.EMP_POST_TYPE = '3'
           AND t.SCHEDULING_CODE = '休'
         GROUP BY t.DEPARTMENT_CODE),     
      --全日制排班人数
      QRZPBRS AS
       (SELECT t.DEPARTMENT_CODE DEPT_CODE,
               COUNT(DISTINCT t.EMPLOYEE_CODE) FULL_TIME_SCHE_COUNT
          FROM TT_WAREHOUSE_DAILY_DAY t
          JOIN TM_OSS_EMPLOYEE E
            ON E.EMP_CODE = T.EMPLOYEE_CODE
         WHERE T.DAY_OF_MONTH = TO_CHAR(V_COUNT.S_DATE, 'YYYYMMDD')
           AND T.SCHEDULING_CODE <> '休'
           AND E.PERSG = 'A'
           AND t.EMP_POST_TYPE = '3'
           AND (E.DIMISSION_DT > V_COUNT.S_DATE OR E.DIMISSION_DT IS NULL)
           AND E.EMP_DUTY_NAME IN ('仓管员', '仓管组长')
         GROUP BY t.DEPARTMENT_CODE),      
      --全日制排休人数
      QRZPXRS AS
       (SELECT t.DEPARTMENT_CODE DEPT_CODE,
               COUNT(DISTINCT t.EMPLOYEE_CODE) FULL_TIME_SCHE_COUNT
          FROM TT_WAREHOUSE_DAILY_DAY t
          JOIN TM_OSS_EMPLOYEE E
            ON E.EMP_CODE = t.EMPLOYEE_CODE
         WHERE T.DAY_OF_MONTH = TO_CHAR(V_COUNT.S_DATE, 'YYYYMMDD')
           AND T.SCHEDULING_CODE = '休'
           AND E.PERSG = 'A'
           AND t.EMP_POST_TYPE = '3'
           AND (E.DIMISSION_DT > V_COUNT.S_DATE OR E.DIMISSION_DT IS NULL)
           AND E.EMP_DUTY_NAME IN ('仓管员', '仓管组长')
         GROUP BY t.DEPARTMENT_CODE),
      -- 非全排班数
      FQRZPBRS AS
       (SELECT t.DEPARTMENT_CODE DEPT_CODE,
               COUNT(DISTINCT t.EMPLOYEE_CODE) NO_FULL_TIME_SCHE_COUNT
          FROM TT_WAREHOUSE_DAILY_DAY t
          JOIN TM_OSS_EMPLOYEE E
            ON E.EMP_CODE = T.EMPLOYEE_CODE
         WHERE T.DAY_OF_MONTH = TO_CHAR(V_COUNT.S_DATE, 'YYYYMMDD')
           AND T.SCHEDULING_CODE <> '休'
           AND E.PERSG = 'C'
           AND t.EMP_POST_TYPE = '3'
           AND (E.DIMISSION_DT > V_COUNT.S_DATE OR E.DIMISSION_DT IS NULL)
           AND E.EMP_DUTY_NAME IN ('仓管员', '仓管组长')
         GROUP BY t.DEPARTMENT_CODE),
      --非全日制排休
      FQRZPXRS AS
       (SELECT t.DEPARTMENT_CODE DEPT_CODE,
               COUNT(DISTINCT t.EMPLOYEE_CODE) NO_FULL_TIME_SCHE_COUNT
          FROM TT_WAREHOUSE_DAILY_DAY t
          JOIN TM_OSS_EMPLOYEE E
            ON E.EMP_CODE = t.EMPLOYEE_CODE
         WHERE T.DAY_OF_MONTH = TO_CHAR(V_COUNT.S_DATE, 'YYYYMMDD')
           AND T.SCHEDULING_CODE = '休'
           AND E.PERSG = 'C'
           AND t.EMP_POST_TYPE = '3'
           AND (E.DIMISSION_DT > V_COUNT.S_DATE OR E.DIMISSION_DT IS NULL)
           AND E.EMP_DUTY_NAME IN ('仓管员', '仓管组长')
         GROUP BY t.DEPARTMENT_CODE)
      
      SELECT TRUNC(V_COUNT.S_DATE),
             ZRS.HQ_CODE,
             ZRS.AREA_CODE,
             ZRS.DEPT_CODE,
             ZRS.DIVISION_CODE,
             NVL(QRZZZ.FULL_TIME, 0),
             NVL(FQZZZ.NO_FULL_TIME, 0),
             NVL(QRZPBRS.FULL_TIME_SCHE_COUNT, 0),
             NVL(QRZPXRS.FULL_TIME_SCHE_COUNT, 0),
             NVL(FQRZPBRS.NO_FULL_TIME_SCHE_COUNT, 0),
             NVL(FQRZPXRS.NO_FULL_TIME_SCHE_COUNT, 0),
             NVL(QRZZZ.FULL_TIME, 0) -
             (NVL(QRZPBD.FULL_TIME_SCHE_COUNT, 0) +
              NVL(QRZPXD.FULL_TIME_SCHE_COUNT, 0)),
             NVL(FQZZZ.NO_FULL_TIME, 0) -
             (NVL(FQRZPBD.NO_FULL_TIME_SCHE_COUNT, 0) +
              NVL(FQRZPXD.NO_FULL_TIME_SCHE_COUNT, 0)),
             SYSDATE,
             ZRS.TYPE_LEVEL,
             3
        FROM ZRS
        LEFT JOIN QRZZZ
          ON ZRS.DEPT_CODE = QRZZZ.DEPT_CODE
        LEFT JOIN FQZZZ
          ON ZRS.DEPT_CODE = FQZZZ.DEPT_CODE
        LEFT JOIN QRZPBD
          ON ZRS.DEPT_CODE = QRZPBD.DEPT_CODE
        LEFT JOIN QRZPXD
          ON ZRS.DEPT_CODE = QRZPXD.DEPT_CODE
        LEFT JOIN FQRZPBD
          ON ZRS.DEPT_CODE = FQRZPBD.DEPT_CODE
        LEFT JOIN FQRZPXD
          ON ZRS.DEPT_CODE = FQRZPXD.DEPT_CODE
        LEFT JOIN QRZPBRS
          ON ZRS.DEPT_CODE = QRZPBRS.DEPT_CODE
        LEFT JOIN QRZPXRS
          ON ZRS.DEPT_CODE = QRZPXRS.DEPT_CODE
        LEFT JOIN FQRZPBRS
          ON ZRS.DEPT_CODE = FQRZPBRS.DEPT_CODE
        LEFT JOIN FQRZPXRS
          ON ZRS.DEPT_CODE = FQRZPXRS.DEPT_CODE;
    COMMIT;
  
    --区部数据汇总
    INSERT INTO TT_DISPATCH_MONITOR_REPORT
      (DAY_OF_MONTH,
       HQ_CODE,
       AREA_CODE,
       DEPT_CODE,
       DIVISION_CODE,
       FULLTIME_EMP_NUM,
       NOT_FULLTIME_EMP_NUM,
       FULLTIME_SCHEDULING_NUM,
       FULLTIME_REST_NUM,
       NOT_FULLTIME_SCHEDULING_NUM,
       NOT_FULLTIME_REST_NUM,
       FULLTIME_NOT_SCHEDULING,
       NOT_FULLTIME_NOT_SCHEDULING,
       CREATE_DATE,
       TYPE_LEVEL,
       Emp_Post_Type)
      SELECT T.DAY_OF_MONTH,
             MAX(DEPT.HQ_CODE),
             MAX(DEPT.AREA_CODE),
             DEPT.AREA_CODE,
             '',
             SUM(FULLTIME_EMP_NUM),
             SUM(NOT_FULLTIME_EMP_NUM),
             SUM(FULLTIME_SCHEDULING_NUM),
             SUM(FULLTIME_REST_NUM),
             SUM(NOT_FULLTIME_SCHEDULING_NUM),
             SUM(NOT_FULLTIME_REST_NUM),
             SUM(FULLTIME_NOT_SCHEDULING),
             SUM(NOT_FULLTIME_NOT_SCHEDULING),
             SYSDATE,
             2,
             3
        FROM TT_DISPATCH_MONITOR_REPORT T, TM_DEPARTMENT DEPT
       WHERE T.DEPT_CODE = DEPT.DEPT_CODE
         AND TO_CHAR(T.DAY_OF_MONTH, 'YYYYMMDD') =
             TO_CHAR(V_COUNT.S_DATE, 'YYYYMMDD')
         AND DEPT.DELETE_FLG = 0
         AND DEPT.TYPE_LEVEL in ('3', '4')
         AND T.EMP_POST_TYPE = '3'
       GROUP BY DEPT.AREA_CODE, T.DAY_OF_MONTH
       ORDER BY DEPT.AREA_CODE, T.DAY_OF_MONTH;
    COMMIT;
  
    --经营本部数据汇总
    INSERT INTO TT_DISPATCH_MONITOR_REPORT
      (DAY_OF_MONTH,
       HQ_CODE,
       AREA_CODE,
       DEPT_CODE,
       DIVISION_CODE,
       FULLTIME_EMP_NUM,
       NOT_FULLTIME_EMP_NUM,
       FULLTIME_SCHEDULING_NUM,
       FULLTIME_REST_NUM,
       NOT_FULLTIME_SCHEDULING_NUM,
       NOT_FULLTIME_REST_NUM,
       FULLTIME_NOT_SCHEDULING,
       NOT_FULLTIME_NOT_SCHEDULING,
       CREATE_DATE,
       TYPE_LEVEL,
       Emp_Post_Type)
      SELECT T.DAY_OF_MONTH,
             MAX(DEPT.HQ_CODE),
             '',
             DEPT.HQ_CODE,
             '',
             SUM(FULLTIME_EMP_NUM),
             SUM(NOT_FULLTIME_EMP_NUM),
             SUM(FULLTIME_SCHEDULING_NUM),
             SUM(FULLTIME_REST_NUM),
             SUM(NOT_FULLTIME_SCHEDULING_NUM),
             SUM(NOT_FULLTIME_REST_NUM),
             SUM(FULLTIME_NOT_SCHEDULING),
             SUM(NOT_FULLTIME_NOT_SCHEDULING),
             SYSDATE,
             1,
             3
        FROM TT_DISPATCH_MONITOR_REPORT T, TM_DEPARTMENT DEPT
       WHERE T.DEPT_CODE = DEPT.DEPT_CODE
         AND TO_CHAR(T.DAY_OF_MONTH, 'YYYYMMDD') =
             TO_CHAR(V_COUNT.S_DATE, 'YYYYMMDD')
         AND DEPT.DELETE_FLG = 0
         AND DEPT.TYPE_LEVEL = '2'
         AND T.EMP_POST_TYPE = '3'
       GROUP BY DEPT.HQ_CODE, T.DAY_OF_MONTH
       ORDER BY DEPT.HQ_CODE, T.DAY_OF_MONTH;
    COMMIT;
  
    --总部数据汇总
    INSERT INTO TT_DISPATCH_MONITOR_REPORT
      (DAY_OF_MONTH,
       HQ_CODE,
       AREA_CODE,
       DEPT_CODE,
       DIVISION_CODE,
       FULLTIME_EMP_NUM,
       NOT_FULLTIME_EMP_NUM,
       FULLTIME_SCHEDULING_NUM,
       FULLTIME_REST_NUM,
       NOT_FULLTIME_SCHEDULING_NUM,
       NOT_FULLTIME_REST_NUM,
       FULLTIME_NOT_SCHEDULING,
       NOT_FULLTIME_NOT_SCHEDULING,
       CREATE_DATE,
       TYPE_LEVEL,
       Emp_Post_Type)
      SELECT T.DAY_OF_MONTH,
             '',
             '',
             '001',
             '',
             SUM(FULLTIME_EMP_NUM),
             SUM(NOT_FULLTIME_EMP_NUM),
             SUM(FULLTIME_SCHEDULING_NUM),
             SUM(FULLTIME_REST_NUM),
             SUM(NOT_FULLTIME_SCHEDULING_NUM),
             SUM(NOT_FULLTIME_REST_NUM),
             SUM(FULLTIME_NOT_SCHEDULING),
             SUM(NOT_FULLTIME_NOT_SCHEDULING),
             SYSDATE,
             0,
             3
        FROM TT_DISPATCH_MONITOR_REPORT T, TM_DEPARTMENT DEPT
       WHERE T.DEPT_CODE = DEPT.DEPT_CODE
         AND TO_CHAR(T.DAY_OF_MONTH, 'YYYYMMDD') =
             TO_CHAR(V_COUNT.S_DATE, 'YYYYMMDD')
         AND DEPT.DELETE_FLG = 0
         AND DEPT.TYPE_LEVEL = '1'
         AND T.EMP_POST_TYPE = '3'
       GROUP BY T.DAY_OF_MONTH
       ORDER BY T.DAY_OF_MONTH;
    COMMIT;
  
  END LOOP;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'WAREHOUSE_MONITOR_DAY',
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
                                 'WAREHOUSE_MONITOR_DAY',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
END WAREHOUSE_MONITOR_DAY;
/
