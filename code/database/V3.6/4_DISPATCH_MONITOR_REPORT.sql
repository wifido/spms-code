CREATE OR REPLACE PROCEDURE DISPATCH_MONITOR_REPORT(V_COUNT IN NUMBER) IS
  --*************************************************************
  -- AUTHOR  : JHC
  -- CREATED : 2016-03-07
  -- PURPOSE : 一线监控表
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
                               'DISPATCH_MONITOR_REPORT',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

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
     TYPE_LEVEL)
    WITH A AS
     (SELECT E.EMP_CODE,
             D.DEPT_ID,
             D.DEPT_CODE,
             D.AREA_CODE,
             D.HQ_CODE,
             D.DIVISION_CODE,
             D.TYPE_LEVEL,
             E.PERSG,
             DATA_SOURCE
        FROM TM_OSS_EMPLOYEE E
        JOIN TM_DEPARTMENT D
          ON E.DEPT_ID = D.DEPT_ID
       WHERE E.EMP_POST_TYPE = '2'
         AND (E.DIMISSION_DT > SYSDATE + V_COUNT OR E.DIMISSION_DT IS NULL)),
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
       WHERE (A.PERSG = 'A' AND A.DATA_SOURCE = '2')
          OR A.DATA_SOURCE = '3'
       GROUP BY A.DEPT_CODE, A.AREA_CODE, A.HQ_CODE, A.DIVISION_CODE),
    -- 非全在职
    FQZZZ AS
     (SELECT COUNT(1) NO_FULL_TIME, A.DEPT_CODE
        FROM A
       WHERE A.PERSG = 'C'
         AND A.DATA_SOURCE = '2'
       GROUP BY A.DEPT_CODE),
    
    --全日制排班人数
    QRZPBRS AS
     (SELECT DA.DEPARTMENT_CODE DEPT_CODE,
             COUNT(DISTINCT DA.EMPLOYEE_CODE) FULL_TIME_SCHE_COUNT
        FROM TT_SCHEDULE_DAILY DA
        JOIN A
          ON DA.DEPARTMENT_CODE = A.DEPT_CODE
         AND A.EMP_CODE = DA.EMPLOYEE_CODE
         AND ((A.PERSG = 'A' AND A.DATA_SOURCE = '2') OR A.DATA_SOURCE = '3')
         AND DA.BEGIN_TIME IS NOT NULL
         AND DA.EMP_POST_TYPE = '2'
         AND DA.END_TIME IS NOT NULL
         AND DA.DAY_OF_MONTH = TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD')
       GROUP BY DA.DEPARTMENT_CODE),
    QRZPXRS AS
     (SELECT DA.DEPARTMENT_CODE DEPT_CODE,
             COUNT(DISTINCT DA.EMPLOYEE_CODE) FULL_TIME_SCHE_COUNT
        FROM TT_SCHEDULE_DAILY DA
        JOIN A
          ON DA.DEPARTMENT_CODE = A.DEPT_CODE
         AND A.EMP_CODE = DA.EMPLOYEE_CODE
         AND ((A.PERSG = 'A' AND A.DATA_SOURCE = '2') OR A.DATA_SOURCE = '3')
         AND DA.BEGIN_TIME IS NULL
         AND DA.EMP_POST_TYPE = '2'
         AND DA.END_TIME IS NULL
         AND DA.DAY_OF_MONTH = TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD')
       GROUP BY DA.DEPARTMENT_CODE),
    -- 非全排班数
    FQRZPBRS AS
     (SELECT DA.DEPARTMENT_CODE DEPT_CODE,
             COUNT(DISTINCT DA.EMPLOYEE_CODE) NO_FULL_TIME_SCHE_COUNT
        FROM TT_SCHEDULE_DAILY DA
        JOIN A
          ON DA.DEPARTMENT_CODE = A.DEPT_CODE
         AND A.EMP_CODE = DA.EMPLOYEE_CODE
         AND A.PERSG = 'C'
         AND A.DATA_SOURCE = '2'
         AND DA.BEGIN_TIME IS NOT NULL
         AND DA.EMP_POST_TYPE = '2'
         AND DA.END_TIME IS NOT NULL
         AND DA.DAY_OF_MONTH = TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD')
       GROUP BY DA.DEPARTMENT_CODE),
    FQRZPXRS AS
     (SELECT DA.DEPARTMENT_CODE DEPT_CODE,
             COUNT(DISTINCT DA.EMPLOYEE_CODE) NO_FULL_TIME_SCHE_COUNT
        FROM TT_SCHEDULE_DAILY DA
        JOIN A
          ON DA.DEPARTMENT_CODE = A.DEPT_CODE
         AND A.EMP_CODE = DA.EMPLOYEE_CODE
         AND A.PERSG = 'C'
         AND DA.EMP_POST_TYPE = '2'
         AND A.DATA_SOURCE = '2'
         AND DA.BEGIN_TIME IS NULL
         AND DA.END_TIME IS NULL
         AND DA.DAY_OF_MONTH = TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD')
       GROUP BY DA.DEPARTMENT_CODE),
    QRZWPBRS AS
     (SELECT COUNT(1) FULL_TIME, A.DEPT_CODE
        FROM A
       WHERE A.EMP_CODE NOT IN
             (SELECT DA.EMPLOYEE_CODE
                FROM TT_SCHEDULE_DAILY DA
               WHERE DA.DEPARTMENT_CODE = A.DEPT_CODE
                 AND ((A.PERSG = 'A' AND A.DATA_SOURCE = '2') OR
                     A.DATA_SOURCE = '3')
                 AND DA.EMP_POST_TYPE = '2'
                 AND DA.DAY_OF_MONTH = TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD'))
         AND ((A.PERSG = 'A' AND A.DATA_SOURCE = '2') OR A.DATA_SOURCE = '3')
       GROUP BY A.DEPT_CODE),
    FQRZWPBRS AS
     (SELECT COUNT(1) FULL_TIME, A.DEPT_CODE
        FROM A
       WHERE A.EMP_CODE NOT IN
             (SELECT DA.EMPLOYEE_CODE
                FROM TT_SCHEDULE_DAILY DA
               WHERE DA.DEPARTMENT_CODE = A.DEPT_CODE
                 AND A.PERSG = 'C'
                 AND A.DATA_SOURCE = '2'
                 AND DA.EMP_POST_TYPE = '2'
                 AND DA.DAY_OF_MONTH = TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD'))
         AND A.PERSG = 'C'
         AND A.DATA_SOURCE = '2'
       GROUP BY A.DEPT_CODE)
    
    SELECT TRUNC(SYSDATE + V_COUNT),
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
           NVL(QRZWPBRS.FULL_TIME, 0),
           NVL(FQRZWPBRS.FULL_TIME, 0),
           SYSDATE,
           ZRS.TYPE_LEVEL
      FROM ZRS
      LEFT JOIN QRZZZ
        ON ZRS.DEPT_CODE = QRZZZ.DEPT_CODE
      LEFT JOIN FQZZZ
        ON ZRS.DEPT_CODE = FQZZZ.DEPT_CODE
      LEFT JOIN QRZPBRS
        ON ZRS.DEPT_CODE = QRZPBRS.DEPT_CODE
      LEFT JOIN QRZPXRS
        ON ZRS.DEPT_CODE = QRZPXRS.DEPT_CODE
      LEFT JOIN FQRZPBRS
        ON ZRS.DEPT_CODE = FQRZPBRS.DEPT_CODE
      LEFT JOIN FQRZPXRS
        ON ZRS.DEPT_CODE = FQRZPXRS.DEPT_CODE
      LEFT JOIN QRZWPBRS
        ON ZRS.DEPT_CODE = QRZWPBRS.DEPT_CODE
      LEFT JOIN FQRZWPBRS
        ON ZRS.DEPT_CODE = FQRZWPBRS.DEPT_CODE;
  COMMIT;

  --分部数据汇总
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
     TYPE_LEVEL)
    SELECT T.DAY_OF_MONTH,
           MAX(DEPT.HQ_CODE),
           MAX(DEPT.AREA_CODE),
           DEPT.DIVISION_CODE,
           DEPT.DIVISION_CODE,
           SUM(FULLTIME_EMP_NUM),
           SUM(NOT_FULLTIME_EMP_NUM),
           SUM(FULLTIME_SCHEDULING_NUM),
           SUM(FULLTIME_REST_NUM),
           SUM(NOT_FULLTIME_SCHEDULING_NUM),
           SUM(NOT_FULLTIME_REST_NUM),
           SUM(FULLTIME_NOT_SCHEDULING),
           SUM(NOT_FULLTIME_NOT_SCHEDULING),
           SYSDATE,
           3
      FROM TT_DISPATCH_MONITOR_REPORT T, TM_DEPARTMENT DEPT
     WHERE T.DEPT_CODE = DEPT.DEPT_CODE
       AND TO_CHAR(T.DAY_OF_MONTH, 'YYYYMMDD') =
           TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD')
       AND DEPT.DELETE_FLG = 0
       AND DEPT.TYPE_LEVEL = '4'
     GROUP BY DEPT.DIVISION_CODE, T.DAY_OF_MONTH
     ORDER BY DEPT.DIVISION_CODE, T.DAY_OF_MONTH;
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
     TYPE_LEVEL)
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
           2
      FROM TT_DISPATCH_MONITOR_REPORT T, TM_DEPARTMENT DEPT
     WHERE T.DEPT_CODE = DEPT.DEPT_CODE
       AND TO_CHAR(T.DAY_OF_MONTH, 'YYYYMMDD') =
           TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD')
       AND DEPT.DELETE_FLG = 0
       AND DEPT.TYPE_LEVEL = '3'
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
     TYPE_LEVEL)
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
           1
      FROM TT_DISPATCH_MONITOR_REPORT T, TM_DEPARTMENT DEPT
     WHERE T.DEPT_CODE = DEPT.DEPT_CODE
       AND TO_CHAR(T.DAY_OF_MONTH, 'YYYYMMDD') =
           TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD')
       AND DEPT.DELETE_FLG = 0
       AND DEPT.TYPE_LEVEL = '2'
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
     TYPE_LEVEL)
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
           0
      FROM TT_DISPATCH_MONITOR_REPORT T, TM_DEPARTMENT DEPT
     WHERE T.DEPT_CODE = DEPT.DEPT_CODE
       AND TO_CHAR(T.DAY_OF_MONTH, 'YYYYMMDD') =
           TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD')
       AND DEPT.DELETE_FLG = 0
       AND DEPT.TYPE_LEVEL = '1'
     GROUP BY T.DAY_OF_MONTH
     ORDER BY T.DAY_OF_MONTH;
  COMMIT;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'DISPATCH_MONITOR_REPORT',
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
                                 'DISPATCH_MONITOR_REPORT',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
END DISPATCH_MONITOR_REPORT;
/