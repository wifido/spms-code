CREATE OR REPLACE PROCEDURE OPERATION_REPORT IS

  --*************************************************************
  -- AUTHOR  : HGX
  -- CREATED : 2015-04-16
  -- PURPOSE : 统计运作排班、考勤信息报表
  --
  -- PARAMETER:
  -- NAME  DAYOFMONTH            TYPE     VARCHAR
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************

  --1.定义执行序号
  L_CALL_NO NUMBER;

  V_TOTAL_EMP_NUM               NUMBER;
  V_FULLTIME_EMP_NUM            NUMBER;
  V_NOT_FULLTIME_EMP_NUM        NUMBER;
  V_OUT_EMP_NUM                 NUMBER;
  V_GROUP_NUM                   NUMBER;
  V_CLASS_NUM                   NUMBER;
  V_TOTAL_ATTENDANCE_NUM        NUMBER;
  V_FULLTIME_ATTENDANCE_NUM     NUMBER;
  V_NOT_FULLTIME_ATTENDANCE_NUM NUMBER;
  V_OUT_ATTENDANCE_NUM          NUMBER;
  V_TOTAL_REST_NUM              NUMBER;
  V_FULLTIME_REST_NUM           NUMBER;
  V_NOT_FULLTIME_REST_NUM       NUMBER;
  V_OUT_REST_NUM                NUMBER;
  V_TOTAL_WORKTIME_COUNT        NUMBER(10, 2);
  DATE_FORMAT                   VARCHAR2(20);
  POST_TYPE_OPERATION           VARCHAR2(20);
  V_WORK_TYPE_FULL_TIME         VARCHAR2(20);
  V_WORK_TYPE_NOT_FULL_TIME     VARCHAR2(20);
  V_WORK_TYPE_OUT_EMPLOYEE      VARCHAR2(20);
  STR_REST                      VARCHAR2(20);
  V_TOTAL_SCHEDULING_NUM        NUMBER;
  V_FULLTIME_SCHEDULING_NUM     NUMBER;
  V_NOT_FULLTIME_SCHEDULING_NUM NUMBER;
  V_OUT_SCHEDULING_NUM          NUMBER;
  V_COM_FULL_ATTENDANCE_NUM     NUMBER;
  V_COM_NOT_FULL_ATTENDANCE_NUM NUMBER;
  V_COM_OUT_ATTENDANCE_NUM      NUMBER;
  DAYOFMONTH                    VARCHAR2(20);

BEGIN
  DATE_FORMAT               := 'YYYY-MM-DD';
  POST_TYPE_OPERATION       := '1';
  V_WORK_TYPE_FULL_TIME     := 'A';
  V_WORK_TYPE_NOT_FULL_TIME := 'C';
  V_WORK_TYPE_OUT_EMPLOYEE  := '6';
  STR_REST                  := '休';
  DAYOFMONTH                := '';

  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'OPERATION_REPORT',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

  BEGIN
  
    --HANDLER_DEPT_POST_RECORD_DATA();
  
    -- 循环历史一周每一天
    -- FOR I IN -6 .. 0 LOOP
  
    -- SELECT TO_CHAR(trunc(SYSDATE + i) ,'YYYY-MM-DD')  INTO DAYOFMONTH  FROM DUAL;
    SELECT TO_CHAR(trunc(TO_DATE('2016-03-17', 'YYYY-MM-DD')), 'YYYY-MM-DD')
      INTO DAYOFMONTH
      FROM DUAL;
  
    ----删除当天的统计数据
    DELETE OP_ATTENDANCE_COUNT_REPORT T
     WHERE TO_CHAR(T.DAY_OF_MONTH, DATE_FORMAT) = DAYOFMONTH;
    COMMIT;
  
    FOR DATA_ROW IN (SELECT * FROM OP_DEPT) LOOP
      ----初始化值
      V_TOTAL_EMP_NUM               := 0;
      V_FULLTIME_EMP_NUM            := 0;
      V_NOT_FULLTIME_EMP_NUM        := 0;
      V_OUT_EMP_NUM                 := 0;
      V_GROUP_NUM                   := 0;
      V_CLASS_NUM                   := 0;
      V_TOTAL_ATTENDANCE_NUM        := 0;
      V_FULLTIME_ATTENDANCE_NUM     := 0;
      V_NOT_FULLTIME_ATTENDANCE_NUM := 0;
      V_OUT_ATTENDANCE_NUM          := 0;
      V_TOTAL_REST_NUM              := 0;
      V_FULLTIME_REST_NUM           := 0;
      V_NOT_FULLTIME_REST_NUM       := 0;
      V_OUT_REST_NUM                := 0;
      V_TOTAL_WORKTIME_COUNT        := 0;
      V_TOTAL_SCHEDULING_NUM        := 0;
      V_FULLTIME_SCHEDULING_NUM     := 0;
      V_NOT_FULLTIME_SCHEDULING_NUM := 0;
      V_OUT_SCHEDULING_NUM          := 0;
      V_COM_FULL_ATTENDANCE_NUM     := 0;
      V_COM_NOT_FULL_ATTENDANCE_NUM := 0;
      V_COM_OUT_ATTENDANCE_NUM      := 0;
    
      DELETE TT_EMPLOYEE_RECORD_HIS4;
    
      INSERT INTO TT_EMPLOYEE_RECORD_HIS4
        SELECT T1.EMP_CODE,
               T1.DEPT_CODE,
               T2.POST_TYPE,
               T3.PERSG,
               T3.PERSK,
               SYSDATE
          FROM (SELECT D.EMP_CODE, D.DEPT_CODE
                  FROM TT_DEPT_RECORD D
                 WHERE D.START_TIME <= TO_DATE(DAYOFMONTH, DATE_FORMAT)
                   AND (D.END_TIME >= TO_DATE(DAYOFMONTH, DATE_FORMAT) OR
                       D.END_TIME IS NULL)) T1,
               (SELECT P.EMP_CODE, P.POST_TYPE
                  FROM TT_POST_RECORD P
                 WHERE P.START_TIME <= TO_DATE(DAYOFMONTH, DATE_FORMAT)
                   AND (P.END_TIME >= TO_DATE(DAYOFMONTH, DATE_FORMAT) OR
                       P.END_TIME IS NULL)) T2,
               (SELECT T.EMP_CODE, T.PERSG, T.PERSK
                  FROM TI_SPMS_SAP_HCM_OUT_PGC_RECORD T
                 WHERE T.START_TIME <= TO_DATE(DAYOFMONTH, DATE_FORMAT)
                   AND (T.END_TIME >= TO_DATE(DAYOFMONTH, DATE_FORMAT) OR
                       T.END_TIME IS NULL)) T3,
               TM_OSS_EMPLOYEE T4
         WHERE T1.EMP_CODE = T2.EMP_CODE
           AND T2.EMP_CODE = T3.EMP_CODE
           AND T3.EMP_CODE = T4.EMP_CODE
           AND (T4.SF_DATE <= TO_DATE(DAYOFMONTH, DATE_FORMAT) OR
               T4.SF_DATE IS NULL)
           AND (T4.DIMISSION_DT > TO_DATE(DAYOFMONTH, DATE_FORMAT) OR
               T4.DIMISSION_DT IS NULL)
           AND T2.POST_TYPE = 1
           AND T1.DEPT_CODE = DATA_ROW.DEPT_CODE;
    
      ----查询运作在职总人数
      SELECT NVL(COUNT(1), 0)
        INTO V_TOTAL_EMP_NUM
        FROM TT_EMPLOYEE_RECORD_HIS4 T;
    
      ----查询运作全日制在职人数
      SELECT NVL(COUNT(1), 0)
        INTO V_FULLTIME_EMP_NUM
        FROM TT_EMPLOYEE_RECORD_HIS4 T
       WHERE T.PERSG = V_WORK_TYPE_FULL_TIME;
    
      ----查询运作非全日制在职人数
      SELECT NVL(COUNT(1), 0)
        INTO V_NOT_FULLTIME_EMP_NUM
        FROM TT_EMPLOYEE_RECORD_HIS4 T
       WHERE T.PERSG = V_WORK_TYPE_NOT_FULL_TIME;
    
      ----查询运作外包在职人数
      V_OUT_EMP_NUM := V_TOTAL_EMP_NUM - V_NOT_FULLTIME_EMP_NUM -
                       V_FULLTIME_EMP_NUM;
    
      ----查询运作班次数量
      SELECT NVL(COUNT(DISTINCT C.SCHEDULE_CODE), 0)
        INTO V_CLASS_NUM
        FROM TT_PB_SHEDULE_BY_DAY S, OP_DEPT D, TM_PB_SCHEDULE_BASE_INFO C
       WHERE S.DEPT_ID = D.DEPT_ID
         AND C.DEPT_ID = S.DEPT_ID
         AND C.SCHEDULE_CODE = S.SHEDULE_CODE
         AND D.DEPT_CODE = DATA_ROW.DEPT_CODE
         AND TO_CHAR(S.SHEDULE_DT, DATE_FORMAT) = DAYOFMONTH;
    
      ----查询运作小组数量
      SELECT NVL(COUNT(DISTINCT G.GROUP_CODE), 0)
        INTO V_GROUP_NUM
        FROM TT_PB_SHEDULE_BY_DAY S,
             TM_DEPARTMENT        D,
             TM_OSS_EMPLOYEE      E,
             TM_PB_GROUP_INFO     G
       WHERE S.EMP_CODE = E.EMP_CODE
         AND S.DEPT_ID = D.DEPT_ID
         AND E.GROUP_ID = G.GROUP_ID
         AND TO_CHAR(S.SHEDULE_DT, DATE_FORMAT) = DAYOFMONTH
         AND D.DEPT_CODE = DATA_ROW.DEPT_CODE;
    
      ----查询运作出勤总人数
      SELECT NVL(COUNT(DISTINCT T.EMP_CODE), 0)
        INTO V_TOTAL_ATTENDANCE_NUM
        FROM TM_DEPARTMENT           D,
             TI_TCAS_SPMS_SCHEDULE   T,
             TT_EMPLOYEE_RECORD_HIS4 E
       WHERE T.DEPT_CODE = D.DEPT_CODE
         AND T.EMP_CODE = E.EMP_CODE
         AND T.POSITION_TYPE = 1
         AND TO_CHAR(T.WORK_DATE, DATE_FORMAT) = DAYOFMONTH
         AND T.WORK_TIME <> 0
         AND D.DEPT_CODE = DATA_ROW.DEPT_CODE;
    
      ----查询运作全日制出勤人数
      SELECT NVL(COUNT(DISTINCT T.EMP_CODE), 0)
        INTO V_FULLTIME_ATTENDANCE_NUM
        FROM TM_DEPARTMENT           D,
             TI_TCAS_SPMS_SCHEDULE   T,
             TT_EMPLOYEE_RECORD_HIS4 E
       WHERE T.DEPT_CODE = D.DEPT_CODE
         AND T.EMP_CODE = E.EMP_CODE
         AND T.POSITION_TYPE = POST_TYPE_OPERATION
         AND E.PERSG = V_WORK_TYPE_FULL_TIME
         AND TO_CHAR(T.WORK_DATE, DATE_FORMAT) = DAYOFMONTH
         AND T.WORK_TIME <> 0
         AND D.DEPT_CODE = DATA_ROW.DEPT_CODE;
    
      ----查询运作非全日制出勤人数
      SELECT NVL(COUNT(DISTINCT T.EMP_CODE), 0)
        INTO V_NOT_FULLTIME_ATTENDANCE_NUM
        FROM TM_DEPARTMENT           D,
             TI_TCAS_SPMS_SCHEDULE   T,
             TT_EMPLOYEE_RECORD_HIS4 E
       WHERE T.DEPT_CODE = D.DEPT_CODE
         AND T.EMP_CODE = E.EMP_CODE
         AND T.POSITION_TYPE = POST_TYPE_OPERATION
         AND E.PERSG = V_WORK_TYPE_NOT_FULL_TIME
         AND TO_CHAR(T.WORK_DATE, DATE_FORMAT) = DAYOFMONTH
         AND T.WORK_TIME <> 0
         AND D.DEPT_CODE = DATA_ROW.DEPT_CODE;
    
      ----查询运作外包出勤人数
      SELECT NVL(COUNT(DISTINCT T.EMP_CODE), 0)
        INTO V_OUT_ATTENDANCE_NUM
        FROM TM_DEPARTMENT           D,
             TI_TCAS_SPMS_SCHEDULE   T,
             TT_EMPLOYEE_RECORD_HIS4 E
       WHERE T.DEPT_CODE = D.DEPT_CODE
         AND T.EMP_CODE = E.EMP_CODE
         AND T.POSITION_TYPE = POST_TYPE_OPERATION
         AND TO_CHAR(T.WORK_DATE, DATE_FORMAT) = DAYOFMONTH
         AND T.WORK_TIME <> 0
         AND E.PERSK = V_WORK_TYPE_OUT_EMPLOYEE
         AND D.DEPT_CODE = DATA_ROW.DEPT_CODE;
    
      ----查询运作排休总人数
      SELECT NVL(COUNT(DISTINCT S.EMP_CODE), 0)
        INTO V_TOTAL_REST_NUM
        FROM TT_EMPLOYEE_RECORD_HIS4 D,
             TT_PB_SHEDULE_BY_DAY    S,
             TM_DEPARTMENT           DEPT
       WHERE D.EMP_CODE = S.EMP_CODE
         AND S.DEPT_ID = DEPT.DEPT_ID
         AND D.DEPT_CODE = DEPT.DEPT_CODE
         AND TO_CHAR(S.SHEDULE_DT, DATE_FORMAT) = DAYOFMONTH
         AND S.SHEDULE_CODE IN (STR_REST, 'SW', 'OFF')
         AND D.DEPT_CODE = DATA_ROW.DEPT_CODE;
    
      ----查询运作全日制排休人数
      SELECT NVL(COUNT(DISTINCT S.EMP_CODE), 0)
        INTO V_FULLTIME_REST_NUM
        FROM TT_EMPLOYEE_RECORD_HIS4 D,
             TT_PB_SHEDULE_BY_DAY    S,
             TM_DEPARTMENT           DEPT
       WHERE D.EMP_CODE = S.EMP_CODE
         AND S.DEPT_ID = DEPT.DEPT_ID
         AND D.DEPT_CODE = DEPT.DEPT_CODE
         AND TO_CHAR(S.SHEDULE_DT, DATE_FORMAT) = DAYOFMONTH
         AND S.SHEDULE_CODE IN (STR_REST, 'SW', 'OFF')
         AND D.PERSG = V_WORK_TYPE_FULL_TIME
         AND D.DEPT_CODE = DATA_ROW.DEPT_CODE;
    
      ----查询运作非全日制排休人数
      SELECT NVL(COUNT(DISTINCT S.EMP_CODE), 0)
        INTO V_NOT_FULLTIME_REST_NUM
        FROM TT_EMPLOYEE_RECORD_HIS4 D,
             TT_PB_SHEDULE_BY_DAY    S,
             TM_DEPARTMENT           DEPT
       WHERE D.EMP_CODE = S.EMP_CODE
         AND S.DEPT_ID = DEPT.DEPT_ID
         AND D.DEPT_CODE = DEPT.DEPT_CODE
         AND TO_CHAR(S.SHEDULE_DT, DATE_FORMAT) = DAYOFMONTH
         AND S.SHEDULE_CODE IN (STR_REST, 'SW', 'OFF')
         AND D.PERSG = V_WORK_TYPE_NOT_FULL_TIME
         AND D.DEPT_CODE = DATA_ROW.DEPT_CODE;
    
      ----查询运作外包排休人数
      SELECT NVL(COUNT(DISTINCT S.EMP_CODE), 0)
        INTO V_OUT_REST_NUM
        FROM TT_EMPLOYEE_RECORD_HIS4 D,
             TT_PB_SHEDULE_BY_DAY    S,
             TM_DEPARTMENT           DEPT
       WHERE D.EMP_CODE = S.EMP_CODE
         AND S.DEPT_ID = DEPT.DEPT_ID
         AND D.DEPT_CODE = DEPT.DEPT_CODE
         AND D.PERSK = V_WORK_TYPE_OUT_EMPLOYEE
         AND TO_CHAR(S.SHEDULE_DT, DATE_FORMAT) = DAYOFMONTH
         AND S.SHEDULE_CODE IN (STR_REST, 'SW', 'OFF')
         AND D.DEPT_CODE = DATA_ROW.DEPT_CODE;
    
      ----查询运作考勤时长
      SELECT NVL(SUM(T.KQ_XSS), 0)
        INTO V_TOTAL_WORKTIME_COUNT
        FROM TI_TCAS_SPMS_SCHEDULE T, TT_EMPLOYEE_RECORD_HIS4 HI
       WHERE T.EMP_CODE = HI.EMP_CODE
         AND T.POSITION_TYPE = POST_TYPE_OPERATION
         AND TO_CHAR(T.WORK_DATE, DATE_FORMAT) = DAYOFMONTH
         AND T.DEPT_CODE = DATA_ROW.DEPT_CODE;
    
      ----查询运作总排班人数
      SELECT NVL(COUNT(*), 0)
        INTO V_TOTAL_SCHEDULING_NUM
        FROM TT_EMPLOYEE_RECORD_HIS4 E, TT_PB_SHEDULE_BY_DAY T
       WHERE T.EMP_CODE = E.EMP_CODE
         AND E.DEPT_CODE = DATA_ROW.DEPT_CODE
         AND T.SHEDULE_CODE NOT IN (STR_REST, 'SW', 'OFF')
         AND SHEDULE_DT = TO_DATE(DAYOFMONTH, DATE_FORMAT);
    
      ----查询运作全日制排班人数
      SELECT NVL(COUNT(*), 0)
        INTO V_FULLTIME_SCHEDULING_NUM
        FROM TT_EMPLOYEE_RECORD_HIS4 E,
             TT_PB_SHEDULE_BY_DAY    T,
             TM_DEPARTMENT           DEPT
       WHERE T.EMP_CODE = E.EMP_CODE
         AND T.DEPT_ID = DEPT.DEPT_ID
         AND E.DEPT_CODE = DEPT.DEPT_CODE
         AND DEPT.DEPT_CODE = DATA_ROW.DEPT_CODE
         AND E.PERSG = V_WORK_TYPE_FULL_TIME
         AND T.SHEDULE_CODE NOT IN (STR_REST, 'SW', 'OFF')
         AND SHEDULE_DT = TO_DATE(DAYOFMONTH, DATE_FORMAT);
    
      ----查询运作非全日制排班人数
      SELECT NVL(COUNT(*), 0)
        INTO V_NOT_FULLTIME_SCHEDULING_NUM
        FROM TT_EMPLOYEE_RECORD_HIS4 E,
             TT_PB_SHEDULE_BY_DAY    T,
             TM_DEPARTMENT           DEPT
       WHERE T.EMP_CODE = E.EMP_CODE
         AND T.DEPT_ID = DEPT.DEPT_ID
         AND E.DEPT_CODE = DEPT.DEPT_CODE
         AND DEPT.DEPT_CODE = DATA_ROW.DEPT_CODE
         AND E.PERSG = V_WORK_TYPE_NOT_FULL_TIME
         AND T.SHEDULE_CODE NOT IN (STR_REST, 'SW', 'OFF')
         AND SHEDULE_DT = TO_DATE(DAYOFMONTH, DATE_FORMAT);
    
      ----查询运作外包排班人数
      SELECT NVL(COUNT(*), 0)
        INTO V_OUT_SCHEDULING_NUM
        FROM TT_EMPLOYEE_RECORD_HIS4 E,
             TT_PB_SHEDULE_BY_DAY    T,
             TM_DEPARTMENT           DEPT
       WHERE T.EMP_CODE = E.EMP_CODE
         AND T.DEPT_ID = DEPT.DEPT_ID
         AND E.DEPT_CODE = DEPT.DEPT_CODE
         AND DEPT.DEPT_CODE = DATA_ROW.DEPT_CODE
         AND E.PERSK = V_WORK_TYPE_OUT_EMPLOYEE
         AND T.SHEDULE_CODE NOT IN (STR_REST, 'SW', 'OFF')
         AND SHEDULE_DT = TO_DATE(DAYOFMONTH, DATE_FORMAT);
    
      --全日制考勤匹配数   
        
        SELECT NVL(COUNT(DISTINCT T.EMP_CODE), 0)
        INTO V_COM_FULL_ATTENDANCE_NUM
        FROM TM_DEPARTMENT           D,
             TI_TCAS_SPMS_SCHEDULE   T,
             TT_EMPLOYEE_RECORD_HIS4 E
       WHERE T.DEPT_CODE = D.DEPT_CODE
         AND T.EMP_CODE = E.EMP_CODE
         AND T.POSITION_TYPE = POST_TYPE_OPERATION
         AND E.PERSG = V_WORK_TYPE_FULL_TIME
         AND TO_CHAR(T.WORK_DATE, DATE_FORMAT) = DAYOFMONTH
         AND T.ATTENDANCE_RATE = 1
         AND D.DEPT_CODE = DATA_ROW.DEPT_CODE;
         
      --非全日制考勤匹配数
          
       SELECT NVL(COUNT(DISTINCT T.EMP_CODE), 0)
        INTO V_COM_NOT_FULL_ATTENDANCE_NUM
        FROM TM_DEPARTMENT           D,
             TI_TCAS_SPMS_SCHEDULE   T,
             TT_EMPLOYEE_RECORD_HIS4 E
       WHERE T.DEPT_CODE = D.DEPT_CODE
         AND T.EMP_CODE = E.EMP_CODE
         AND T.POSITION_TYPE = POST_TYPE_OPERATION
         AND E.PERSG = V_WORK_TYPE_NOT_FULL_TIME
         AND TO_CHAR(T.WORK_DATE, DATE_FORMAT) = DAYOFMONTH
         AND T.ATTENDANCE_RATE = 1
         AND D.DEPT_CODE = DATA_ROW.DEPT_CODE;
         
      --外包考勤匹配数
    
       SELECT NVL(COUNT(DISTINCT T.EMP_CODE), 0)
        INTO V_COM_OUT_ATTENDANCE_NUM
        FROM TM_DEPARTMENT           D,
             TI_TCAS_SPMS_SCHEDULE   T,
             TT_EMPLOYEE_RECORD_HIS4 E
       WHERE T.DEPT_CODE = D.DEPT_CODE
         AND T.EMP_CODE = E.EMP_CODE
         AND T.POSITION_TYPE = POST_TYPE_OPERATION
         AND TO_CHAR(T.WORK_DATE, DATE_FORMAT) = DAYOFMONTH
         AND T.ATTENDANCE_RATE = 1
         AND E.PERSK = V_WORK_TYPE_OUT_EMPLOYEE
         AND D.DEPT_CODE = DATA_ROW.DEPT_CODE;
    
      INSERT INTO OP_ATTENDANCE_COUNT_REPORT
        (DEPT_CODE,
         DAY_OF_MONTH,
         TOTAL_EMP_NUM,
         FULLTIME_EMP_NUM,
         NOT_FULLTIME_EMP_NUM,
         OUT_EMP_NUM,
         GROUP_NUM,
         CLASS_NUM,
         TOTAL_ATTENDANCE_NUM,
         FULLTIME_ATTENDANCE_NUM,
         NOT_FULLTIME_ATTENDANCE_NUM,
         OUT_ATTENDANCE_NUM,
         TOTAL_REST_NUM,
         FULLTIME_REST_NUM,
         NOT_FULLTIME_REST_NUM,
         OUT_REST_NUM,
         TOTAL_WORKTIME,
         COUNT_DATE,
         TOTAL_SCHEDULING_NUM,
         FULLTIME_SCHEDULING_NUM,
         NOT_FULLTIME_SCHEDULING_NUM,
         OUT_SCHEDULING_NUM,
         COM_FULL_ATTENDANCE_NUM,
         COM_NOT_FULL_ATTENDANCE_NUM,
         COM_OUT_ATTENDANCE_NUM)
      VALUES
        (DATA_ROW.DEPT_CODE,
         TO_DATE(DAYOFMONTH, DATE_FORMAT),
         V_TOTAL_EMP_NUM,
         V_FULLTIME_EMP_NUM,
         V_NOT_FULLTIME_EMP_NUM,
         V_OUT_EMP_NUM,
         V_GROUP_NUM,
         V_CLASS_NUM,
         V_TOTAL_ATTENDANCE_NUM,
         V_FULLTIME_ATTENDANCE_NUM,
         V_NOT_FULLTIME_ATTENDANCE_NUM,
         V_OUT_ATTENDANCE_NUM,
         V_TOTAL_REST_NUM,
         V_FULLTIME_REST_NUM,
         V_NOT_FULLTIME_REST_NUM,
         V_OUT_REST_NUM,
         V_TOTAL_WORKTIME_COUNT,
         SYSDATE,
         V_TOTAL_SCHEDULING_NUM,
         V_FULLTIME_SCHEDULING_NUM,
         V_NOT_FULLTIME_SCHEDULING_NUM,
         V_OUT_SCHEDULING_NUM,
         V_COM_FULL_ATTENDANCE_NUM,
         V_COM_NOT_FULL_ATTENDANCE_NUM,
         V_COM_OUT_ATTENDANCE_NUM);
      COMMIT;
    END LOOP;
    -- 插入区部数据
    INSERT INTO OP_ATTENDANCE_COUNT_REPORT
    
      SELECT DECODE(DEPT.AREA_CODE,
                    NULL,
                    MAX(DEPT.DEPT_CODE),
                    DEPT.AREA_CODE),
             T.DAY_OF_MONTH,
             SUM(T.TOTAL_EMP_NUM),
             SUM(T.FULLTIME_EMP_NUM),
             SUM(T.NOT_FULLTIME_EMP_NUM),
             SUM(T.OUT_EMP_NUM),
             SUM(T.GROUP_NUM),
             SUM(T.CLASS_NUM),
             SUM(T.TOTAL_ATTENDANCE_NUM),
             SUM(T.FULLTIME_ATTENDANCE_NUM),
             SUM(T.NOT_FULLTIME_ATTENDANCE_NUM),
             SUM(T.OUT_ATTENDANCE_NUM),
             SUM(T.TOTAL_REST_NUM),
             SUM(T.FULLTIME_REST_NUM),
             SUM(T.NOT_FULLTIME_REST_NUM),
             SUM(T.OUT_REST_NUM),
             DECODE(SUM(T.TOTAL_WORKTIME), NULL, 0, SUM(T.TOTAL_WORKTIME)),
             SYSDATE,
             DECODE(SUM(T.TOTAL_SCHEDULING_NUM),
                    NULL,
                    0,
                    SUM(T.TOTAL_SCHEDULING_NUM)),
             DECODE(SUM(T.FULLTIME_SCHEDULING_NUM),
                    NULL,
                    0,
                    SUM(T.FULLTIME_SCHEDULING_NUM)),
             DECODE(SUM(T.NOT_FULLTIME_SCHEDULING_NUM),
                    NULL,
                    0,
                    SUM(T.NOT_FULLTIME_SCHEDULING_NUM)),
             DECODE(SUM(T.OUT_SCHEDULING_NUM),
                    NULL,
                    0,
                    SUM(T.OUT_SCHEDULING_NUM)),
             DECODE(SUM(T.COM_FULL_ATTENDANCE_NUM),
                    NULL,
                    0,
                    SUM(T.COM_FULL_ATTENDANCE_NUM)),
             DECODE(SUM(T.COM_NOT_FULL_ATTENDANCE_NUM),
                    NULL,
                    0,
                    SUM(T.COM_NOT_FULL_ATTENDANCE_NUM)),
             DECODE(SUM(T.COM_OUT_ATTENDANCE_NUM),
                    NULL,
                    0,
                    SUM(T.COM_OUT_ATTENDANCE_NUM))
        FROM OP_ATTENDANCE_COUNT_REPORT T, TM_DEPARTMENT DEPT
       WHERE T.DEPT_CODE = DEPT.DEPT_CODE
         AND TO_CHAR(T.DAY_OF_MONTH, 'YYYY-MM-DD') = DAYOFMONTH
         AND DEPT.DELETE_FLG = 0
         AND DEPT.TYPE_CODE IN ('ZZC04-YJ',
                                'ZZC04-ERJ',
                                'ZZC05-SJ',
                                'HHZ05',
                                'QB03-YSZX',
                                'FB05-YSZX',
                                'FB04-JSZX',
                                'GWB04')
       GROUP BY DEPT.AREA_CODE, T.DAY_OF_MONTH
       ORDER BY DEPT.AREA_CODE, T.DAY_OF_MONTH;
    COMMIT;
  
    -- 经营本部数据
    INSERT INTO OP_ATTENDANCE_COUNT_REPORT
      SELECT DEPT.HQ_CODE,
             T.DAY_OF_MONTH,
             SUM(T.TOTAL_EMP_NUM),
             SUM(T.FULLTIME_EMP_NUM),
             SUM(T.NOT_FULLTIME_EMP_NUM),
             SUM(T.OUT_EMP_NUM),
             SUM(T.GROUP_NUM),
             SUM(T.CLASS_NUM),
             SUM(T.TOTAL_ATTENDANCE_NUM),
             SUM(T.FULLTIME_ATTENDANCE_NUM),
             SUM(T.NOT_FULLTIME_ATTENDANCE_NUM),
             SUM(T.OUT_ATTENDANCE_NUM),
             SUM(T.TOTAL_REST_NUM),
             SUM(T.FULLTIME_REST_NUM),
             SUM(T.NOT_FULLTIME_REST_NUM),
             SUM(T.OUT_REST_NUM),
             DECODE(SUM(T.TOTAL_WORKTIME), NULL, 0, SUM(T.TOTAL_WORKTIME)),
             SYSDATE,
             DECODE(SUM(T.TOTAL_SCHEDULING_NUM),
                    NULL,
                    0,
                    SUM(T.TOTAL_SCHEDULING_NUM)),
             DECODE(SUM(T.FULLTIME_SCHEDULING_NUM),
                    NULL,
                    0,
                    SUM(T.FULLTIME_SCHEDULING_NUM)),
             DECODE(SUM(T.NOT_FULLTIME_SCHEDULING_NUM),
                    NULL,
                    0,
                    SUM(T.NOT_FULLTIME_SCHEDULING_NUM)),
             DECODE(SUM(T.OUT_SCHEDULING_NUM),
                    NULL,
                    0,
                    SUM(T.OUT_SCHEDULING_NUM)),
             DECODE(SUM(T.COM_FULL_ATTENDANCE_NUM),
                    NULL,
                    0,
                    SUM(T.COM_FULL_ATTENDANCE_NUM)),
             DECODE(SUM(T.COM_NOT_FULL_ATTENDANCE_NUM),
                    NULL,
                    0,
                    SUM(T.COM_NOT_FULL_ATTENDANCE_NUM)),
             DECODE(SUM(T.COM_OUT_ATTENDANCE_NUM),
                    NULL,
                    0,
                    SUM(T.COM_OUT_ATTENDANCE_NUM))
        FROM OP_ATTENDANCE_COUNT_REPORT T, TM_DEPARTMENT DEPT
       WHERE T.DEPT_CODE = DEPT.DEPT_CODE
         AND TO_CHAR(T.DAY_OF_MONTH, 'YYYY-MM-DD') = DAYOFMONTH
         AND DEPT.DELETE_FLG = 0
         AND DEPT.TYPE_LEVEL = 2
       GROUP BY DEPT.HQ_CODE, T.DAY_OF_MONTH
       ORDER BY DEPT.HQ_CODE, T.DAY_OF_MONTH;
    COMMIT;
    -- 插入总部数据
    INSERT INTO OP_ATTENDANCE_COUNT_REPORT
      SELECT '001',
             T.DAY_OF_MONTH,
             SUM(T.TOTAL_EMP_NUM),
             SUM(T.FULLTIME_EMP_NUM),
             SUM(T.NOT_FULLTIME_EMP_NUM),
             SUM(T.OUT_EMP_NUM),
             SUM(T.GROUP_NUM),
             SUM(T.CLASS_NUM),
             SUM(T.TOTAL_ATTENDANCE_NUM),
             SUM(T.FULLTIME_ATTENDANCE_NUM),
             SUM(T.NOT_FULLTIME_ATTENDANCE_NUM),
             SUM(T.OUT_ATTENDANCE_NUM),
             SUM(T.TOTAL_REST_NUM),
             SUM(T.FULLTIME_REST_NUM),
             SUM(T.NOT_FULLTIME_REST_NUM),
             SUM(T.OUT_REST_NUM),
             DECODE(SUM(T.TOTAL_WORKTIME), NULL, 0, SUM(T.TOTAL_WORKTIME)),
             SYSDATE,
             DECODE(SUM(T.TOTAL_SCHEDULING_NUM),
                    NULL,
                    0,
                    SUM(T.TOTAL_SCHEDULING_NUM)),
             DECODE(SUM(T.FULLTIME_SCHEDULING_NUM),
                    NULL,
                    0,
                    SUM(T.FULLTIME_SCHEDULING_NUM)),
             DECODE(SUM(T.NOT_FULLTIME_SCHEDULING_NUM),
                    NULL,
                    0,
                    SUM(T.NOT_FULLTIME_SCHEDULING_NUM)),
             DECODE(SUM(T.OUT_SCHEDULING_NUM),
                    NULL,
                    0,
                    SUM(T.OUT_SCHEDULING_NUM)),
             DECODE(SUM(T.COM_FULL_ATTENDANCE_NUM),
                    NULL,
                    0,
                    SUM(T.COM_FULL_ATTENDANCE_NUM)),
             DECODE(SUM(T.COM_NOT_FULL_ATTENDANCE_NUM),
                    NULL,
                    0,
                    SUM(T.COM_NOT_FULL_ATTENDANCE_NUM)),
             DECODE(SUM(T.COM_OUT_ATTENDANCE_NUM),
                    NULL,
                    0,
                    SUM(T.COM_OUT_ATTENDANCE_NUM))
        FROM OP_ATTENDANCE_COUNT_REPORT T, TM_DEPARTMENT DEPT
       WHERE T.DEPT_CODE = DEPT.DEPT_CODE
         AND TO_CHAR(T.DAY_OF_MONTH, 'YYYY-MM-DD') = DAYOFMONTH
         AND DEPT.DELETE_FLG = 0
         AND DEPT.TYPE_LEVEL = 1
       GROUP BY T.DAY_OF_MONTH
       ORDER BY T.DAY_OF_MONTH;
  
    COMMIT;
  
    -- END LOOP;
  
  END;
  --4 结束记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'OPERATION_REPORT',
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
                                 'OPERATION_REPORT',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
  
END OPERATION_REPORT;
/
