CREATE OR REPLACE PROCEDURE STATISTICAL_COINCIDENCE_RATE IS

  --*************************************************************
  -- AUTHOR  : JHC
  -- CREATED : 2015-03-13
  -- PURPOSE : 根据网点统计排班吻合率和工序吻合率。
  --
  -- PARAMETER:
  -- NAME             TYPE            DESCSTATISTICAL_COINCIDENCE_RATE
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************
  DEPT_TOTAL_COUNT      NUMBER; --部门总数量
  DEPT_COINCIDE_COUNT   NUMBER; --部门吻合数量
  DEPT_COINCIDE_RATE    NUMBER; --部门吻合数量
  V_YEAR_MONTH            VARCHAR2(20); --月份
  INDEX_DAY_OF_MONTH    VARCHAR2(20); --日期
  EMP_TOTAL_COUNT       NUMBER; --员工总数量
  EMP_COINCID_COUNT     NUMBER; --员工吻合数量
  EMP_NOT_COINCID_COUNT NUMBER; --员工不吻合数量
  --1.定义执行序号
  L_CALL_NO NUMBER;
BEGIN
  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'STATISTICAL_COINCIDENCE_RATE',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);
              

  
index_day_of_month := to_char(sysdate, 'DD');

  if index_day_of_month = '05' then
    V_YEAR_MONTH := to_char(add_months(sysdate, -1), 'YYYY-MM');
  else 
      V_YEAR_MONTH  := to_char(sysdate, 'YYYY-MM');
  end if;

  ----遍历运作网点，逐个统计排班吻合率
  FOR DEPT_ROW IN (SELECT * FROM OP_DEPT) LOOP

    -----初始化值
    DEPT_TOTAL_COUNT    := 0;
    DEPT_COINCIDE_COUNT := 0;
    DEPT_COINCIDE_RATE  := 0;

    -----删除网点排吻合统计数据
    DELETE FROM TT_STATISTICAL_COINCIDENCE T
     WHERE T.YEAR_MONTH = V_YEAR_MONTH
       AND T.DEPT_ID = DEPT_ROW.DEPT_ID
       AND T.POSITION_TYPE = 1;

    -----删除网点下每个员工排班吻合统计情况
    DELETE FROM TT_STATISTICAL_COINCIDENCE_EMP T
     WHERE T.DEPT_ID = DEPT_ROW.DEPT_ID
       AND T.YEAR_MONTH = YEAR_MONTH
       AND T.POSITION_TYPE = 1;

    COMMIT;

    -----遍历网点下排班的每个员工，逐个统计排班吻合情况
    FOR EMPLOYEE_SCHEDULING_ROW IN (SELECT T.YM, T.DEPT_ID, T.EMP_CODE
                                      FROM TT_PB_SHEDULE_BY_MONTH_LOG T,
                                           TM_OSS_EMPLOYEE            E
                                     WHERE T.EMP_CODE = E.EMP_CODE
                                       AND T.DEPT_ID = E.DEPT_ID
                                       AND T.DEPT_ID = DEPT_ROW.DEPT_ID
                                       AND T.YM = V_YEAR_MONTH
                                     GROUP BY T.YM, T.DEPT_ID, T.EMP_CODE) LOOP
      -----初始化值
      EMP_TOTAL_COUNT       := 0;
      EMP_COINCID_COUNT     := 0;

      EMP_NOT_COINCID_COUNT := 0;

      ----查询员工总共的有效排班天数
      SELECT NVL(EFFECTIVE_NUM, 0)
        INTO EMP_TOTAL_COUNT
        FROM (SELECT EFFECTIVE_NUM, ROWNUM RN
                FROM (SELECT T.EFFECTIVE_NUM
                        FROM TT_PB_SHEDULE_BY_MONTH_LOG T
                       WHERE T.DEPT_ID = DEPT_ROW.DEPT_ID
                         AND T.YM = V_YEAR_MONTH
                         AND T.EMP_CODE = EMPLOYEE_SCHEDULING_ROW.EMP_CODE
                       ORDER BY T.MODIFIED_TM DESC))
       WHERE RN = 1;

      -----查询员工排班不吻合的天数
      SELECT NVL(SUM(T.COINCIDE_NUM), 0)
        INTO EMP_NOT_COINCID_COUNT
        FROM TT_PB_SHEDULE_BY_MONTH_LOG T
       WHERE T.DEPT_ID = DEPT_ROW.DEPT_ID
         AND T.YM = V_YEAR_MONTH
         AND T.EMP_CODE = EMPLOYEE_SCHEDULING_ROW.EMP_CODE;

      EMP_COINCID_COUNT := EMP_TOTAL_COUNT - EMP_NOT_COINCID_COUNT;

      IF EMP_COINCID_COUNT < 0 THEN
        EMP_COINCID_COUNT := 0;
      END IF;

      -----插入员工排班吻合统计数据
      INSERT INTO TT_STATISTICAL_COINCIDENCE_EMP
        (EMP_CODE,
         DEPT_ID,
         YEAR_MONTH,
         SCHEDULING_DAYS,
         COINCIDENCE_DAYS,
         POSITION_TYPE,
         COUNT_DATE)
      VALUES
        (EMPLOYEE_SCHEDULING_ROW.EMP_CODE,
         DEPT_ROW.DEPT_ID,
         V_YEAR_MONTH,
         EMP_TOTAL_COUNT,
         EMP_COINCID_COUNT,
         1,
         SYSDATE);
      COMMIT;

    END LOOP;

    -----统计部门总排班天数
    SELECT NVL(SUM(T.SCHEDULING_DAYS), 0)
      INTO DEPT_TOTAL_COUNT
      FROM TT_STATISTICAL_COINCIDENCE_EMP T
     WHERE T.DEPT_ID = DEPT_ROW.DEPT_ID
       AND T.YEAR_MONTH = V_YEAR_MONTH
       AND T.POSITION_TYPE = 1;

    -----统计部门总吻合数量
    SELECT NVL(SUM(T.COINCIDENCE_DAYS), 0)
      INTO DEPT_COINCIDE_COUNT
      FROM TT_STATISTICAL_COINCIDENCE_EMP T
     WHERE T.DEPT_ID = DEPT_ROW.DEPT_ID
       AND T.YEAR_MONTH = V_YEAR_MONTH
       AND T.POSITION_TYPE = 1;

    -----计算吻合率
    IF DEPT_COINCIDE_COUNT > 1 THEN
      DEPT_COINCIDE_RATE := ROUND(DEPT_COINCIDE_COUNT / DEPT_TOTAL_COUNT * 100);
    END IF;

    -----插入部门排班吻合率数据
    INSERT INTO TT_STATISTICAL_COINCIDENCE
      (DEPT_ID,
       YEAR_MONTH,
       COINCIDENCE_RATE,
       COINCIDENCERATE_COUNT,
       DEPT_COUNT,
       POSITION_TYPE,
       DEPT_CODE,
       COUNT_DATE)
    VALUES
      (DEPT_ROW.DEPT_ID,
       V_YEAR_MONTH,
       DEPT_COINCIDE_RATE,
       DEPT_COINCIDE_COUNT,
       DEPT_TOTAL_COUNT,
       1,
       DEPT_ROW.DEPT_CODE,
       SYSDATE);
    COMMIT;

  END LOOP;
   --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'STATISTICAL_COINCIDENCE_RATE',
                               SYSDATE,
                               NULL,
                               NULL,
                               'END;',
                               0,
                               L_CALL_NO);
                               EXCEPTION
  WHEN OTHERS THEN
    --回滚数据
    ROLLBACK;
    PKG_OSS_COMM.STP_RUNNING_LOG('',
                                 'STATISTICAL_COINCIDENCE_RATE',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);

END STATISTICAL_COINCIDENCE_RATE;
/