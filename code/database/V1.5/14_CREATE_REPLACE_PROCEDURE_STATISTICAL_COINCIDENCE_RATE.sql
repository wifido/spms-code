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
  DEPTCOUNT      NUMBER;
  COINCIDECOUNT  NUMBER;
  ONESUBMITCOUNT NUMBER;
  RESULTNUM      NUMBER;

BEGIN
  DEPTCOUNT     := 0;
  COINCIDECOUNT := 0;

  FOR DEPT_ROW IN (SELECT TD.DEPT_ID, TD.DEPT_CODE
                     FROM TM_DEPARTMENT TD
                    WHERE REGEXP_LIKE(TD.DEPT_CODE, '^[0-9]{3,4}[WRX]')
                    START WITH TD.DEPT_ID = 1
                   CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE)
  
   LOOP
  
    -- 查询提交大于2次的员工总排班总数
    SELECT NVL(SUM(T.COUNT_NUM), 0)
      INTO DEPTCOUNT
      FROM (SELECT (SYSDATE - ADD_MONTHS(SYSDATE, -1)) -
                   GET_COUNT_BY_DEPT(TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                             'YYYY-MM'),
                                     SF_DATE,
                                     DATE_FROM,
                                     TRANSFER_DATE,
                                     E.DIMISSION_DT) COUNT_NUM
              FROM TM_OSS_EMPLOYEE E
             WHERE E.DEPT_ID = DEPT_ROW.DEPT_ID
               AND E.EMP_CODE IN
                   (SELECT EMP_CODE
                      FROM TT_PB_SHEDULE_BY_MONTH_LOG T
                     WHERE T.DEPT_ID = DEPT_ROW.DEPT_ID
                       AND T.YM = TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'YYYY-MM')
                     GROUP BY EMP_CODE
                    HAVING COUNT(EMP_CODE) > 1)) T;
  
    -- 查询提交确认一次的员工总排班总数
    SELECT NVL(SUM(T.COUNT_NUM), 0)
      INTO ONESUBMITCOUNT
      FROM (SELECT (SYSDATE - ADD_MONTHS(SYSDATE, -1)) -
                   GET_COUNT_BY_DEPT(TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                             'YYYY-MM'),
                                     SF_DATE,
                                     DATE_FROM,
                                     TRANSFER_DATE,
                                     E.DIMISSION_DT) COUNT_NUM
              FROM TM_OSS_EMPLOYEE E
             WHERE E.DEPT_ID = DEPT_ROW.DEPT_ID
               AND E.EMP_CODE IN
                   (SELECT EMP_CODE
                      FROM TT_PB_SHEDULE_BY_MONTH_LOG T
                     WHERE T.DEPT_ID = DEPT_ROW.DEPT_ID
                       AND T.YM = TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'YYYY-MM')
                     GROUP BY EMP_CODE
                    HAVING COUNT(EMP_CODE) = 1)) T;
  
    DEPTCOUNT := DEPTCOUNT + ONESUBMITCOUNT;
    BEGIN
    
      FOR COMMIT_ROW IN (SELECT T.YM, T.DEPT_ID, T.EMP_CODE
                           FROM TT_PB_SHEDULE_BY_MONTH_LOG T,
                                TM_OSS_EMPLOYEE            E
                          WHERE T.DEPT_ID = E.DEPT_ID
                            AND T.EMP_CODE = E.EMP_CODE
                            AND T.DEPT_ID = DEPT_ROW.DEPT_ID
                            AND T.YM =
                                TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'YYYY-MM')
                          GROUP BY T.YM, T.DEPT_ID, T.EMP_CODE
                         HAVING COUNT(T.EMP_CODE) > 1) LOOP
      
        SELECT (GET_COUNT_BY_COINCIDE(MAX_LOG.DAY1,
                                      MIN_LOG.DAY1,
                                      E.DIMISSION_DT,
                                      '01',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY2,
                                      MIN_LOG.DAY2,
                                      E.DIMISSION_DT,
                                      '02',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY3,
                                      MIN_LOG.DAY3,
                                      E.DIMISSION_DT,
                                      '03',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY4,
                                      MIN_LOG.DAY4,
                                      E.DIMISSION_DT,
                                      '04',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY5,
                                      MIN_LOG.DAY5,
                                      E.DIMISSION_DT,
                                      '05',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY6,
                                      MIN_LOG.DAY6,
                                      E.DIMISSION_DT,
                                      '06',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY7,
                                      MIN_LOG.DAY7,
                                      E.DIMISSION_DT,
                                      '07',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY8,
                                      MIN_LOG.DAY8,
                                      E.DIMISSION_DT,
                                      '08',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY9,
                                      MIN_LOG.DAY9,
                                      E.DIMISSION_DT,
                                      '09',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY10,
                                      MIN_LOG.DAY10,
                                      E.DIMISSION_DT,
                                      '10',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY11,
                                      MIN_LOG.DAY11,
                                      E.DIMISSION_DT,
                                      '11',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY12,
                                      MIN_LOG.DAY12,
                                      E.DIMISSION_DT,
                                      '12',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY13,
                                      MIN_LOG.DAY13,
                                      E.DIMISSION_DT,
                                      '13',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY14,
                                      MIN_LOG.DAY14,
                                      E.DIMISSION_DT,
                                      '14',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY15,
                                      MIN_LOG.DAY15,
                                      E.DIMISSION_DT,
                                      '15',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY16,
                                      MIN_LOG.DAY16,
                                      E.DIMISSION_DT,
                                      '16',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY17,
                                      MIN_LOG.DAY17,
                                      E.DIMISSION_DT,
                                      '17',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY18,
                                      MIN_LOG.DAY18,
                                      E.DIMISSION_DT,
                                      '18',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY19,
                                      MIN_LOG.DAY19,
                                      E.DIMISSION_DT,
                                      '19',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY20,
                                      MIN_LOG.DAY20,
                                      E.DIMISSION_DT,
                                      '20',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY21,
                                      MIN_LOG.DAY21,
                                      E.DIMISSION_DT,
                                      '21',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY22,
                                      MIN_LOG.DAY22,
                                      E.DIMISSION_DT,
                                      '22',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY23,
                                      MIN_LOG.DAY23,
                                      E.DIMISSION_DT,
                                      '23',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY24,
                                      MIN_LOG.DAY24,
                                      E.DIMISSION_DT,
                                      '24',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY25,
                                      MIN_LOG.DAY25,
                                      E.DIMISSION_DT,
                                      '25',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY26,
                                      MIN_LOG.DAY26,
                                      E.DIMISSION_DT,
                                      '26',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY27,
                                      MIN_LOG.DAY27,
                                      E.DIMISSION_DT,
                                      '27',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY28,
                                      MIN_LOG.DAY28,
                                      E.DIMISSION_DT,
                                      '28',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY29,
                                      MIN_LOG.DAY29,
                                      E.DIMISSION_DT,
                                      '29',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY30,
                                      MIN_LOG.DAY30,
                                      E.DIMISSION_DT,
                                      '30',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY31,
                                      MIN_LOG.DAY31,
                                      E.DIMISSION_DT,
                                      '31',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) + COINCIDECOUNT)
          INTO COINCIDECOUNT
          FROM （SELECT *
          FROM TT_PB_SHEDULE_BY_MONTH_LOG T
         WHERE T.ID IN (SELECT MAX(T.ID)
                          FROM TT_PB_SHEDULE_BY_MONTH_LOG T
                         WHERE T.DEPT_ID = COMMIT_ROW.DEPT_ID
                           AND T.EMP_CODE = COMMIT_ROW.EMP_CODE
                           AND T.YM = COMMIT_ROW.YM
                         GROUP BY T.YM, T.DEPT_ID, T.EMP_CODE
                        HAVING COUNT(T.EMP_CODE) > 1)) MAX_LOG, (SELECT *
                                                                   FROM TT_PB_SHEDULE_BY_MONTH_LOG T
                                                                  WHERE T.ID IN
                                                                        (SELECT MIN(T.ID)
                                                                           FROM TT_PB_SHEDULE_BY_MONTH_LOG T
                                                                          WHERE T.DEPT_ID =
                                                                                COMMIT_ROW.DEPT_ID
                                                                            AND T.EMP_CODE =
                                                                                COMMIT_ROW.EMP_CODE
                                                                            AND T.YM =
                                                                                COMMIT_ROW.YM
                                                                          GROUP BY T.YM,
                                                                                   T.DEPT_ID,
                                                                                   T.EMP_CODE
                                                                         HAVING COUNT(T.EMP_CODE) > 1)) MIN_LOG, TM_OSS_EMPLOYEE E
         WHERE E.EMP_CODE = MIN_LOG.EMP_CODE
           AND E.EMP_CODE = MAX_LOG.EMP_CODE
           AND E.DEPT_ID = MIN_LOG.DEPT_ID
           AND E.DEPT_ID = MAX_LOG.DEPT_ID;
      
      END LOOP;
    
      -- 吻合数 = 多次提交确认的总量 + 一次提交确认的总量
      COINCIDECOUNT := COINCIDECOUNT + ONESUBMITCOUNT;
      -- 计算吻合率
      -- 吻合数/网点总量 = 吻合率
    
      IF (DEPTCOUNT = 0) THEN
        RESULTNUM     := 0;
        COINCIDECOUNT := 0;
      ELSE
        RESULTNUM := NVL(ROUND((NVL(COINCIDECOUNT, 0) / DEPTCOUNT), 2) * 100,
                         0);
      END IF;
    
      INSERT INTO TT_STATISTICAL_COINCIDENCE
        (DEPT_ID,
         DEPT_CODE,
         YEAR_MONTH,
         COINCIDENCE_RATE,
         COINCIDENCERATE_COUNT,
         DEPT_COUNT,
         POSITION_TYPE)
      VALUES
        (DEPT_ROW.DEPT_ID,
         DEPT_ROW.DEPT_CODE,
         TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'YYYY-MM'),
         RESULTNUM,
         NVL(COINCIDECOUNT, 0),
         NVL(DEPTCOUNT, 0),
         1);
      COMMIT;
    END;
  
    BEGIN
    
      RESULTNUM     := 0;
      COINCIDECOUNT := 0;
      DEPTCOUNT     := 0;
      -- 统计网点下工序安排总量
      -- 查询提交确认大于2次的工序安排总数
      SELECT NVL(SUM(T.COUNT_NUM), 0)
        INTO DEPTCOUNT
        FROM (SELECT (SYSDATE - ADD_MONTHS(SYSDATE, -1)) -
                     GET_COUNT_BY_DEPT(TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                               'YYYY-MM'),
                                       SF_DATE,
                                       DATE_FROM,
                                       TRANSFER_DATE,
                                       E.DIMISSION_DT) COUNT_NUM
                FROM TM_OSS_EMPLOYEE E
               WHERE E.DEPT_ID = DEPT_ROW.DEPT_ID
                 AND E.EMP_CODE IN
                     (SELECT EMP_CODE
                        FROM TT_PB_PROCESS_BY_MONTH_LOG T
                       WHERE T.DEPT_ID = DEPT_ROW.DEPT_ID
                         AND T.YM =
                             TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'YYYY-MM')
                       GROUP BY EMP_CODE
                      HAVING COUNT(EMP_CODE) > 1)) T;
      -- 查询提交确认一次的工序总数
      SELECT NVL(SUM(T.COUNT_NUM), 0)
        INTO ONESUBMITCOUNT
        FROM (SELECT (SYSDATE - ADD_MONTHS(SYSDATE, -1)) -
                     GET_COUNT_BY_DEPT(TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                               'YYYY-MM'),
                                       SF_DATE,
                                       DATE_FROM,
                                       TRANSFER_DATE,
                                       E.DIMISSION_DT) COUNT_NUM
                FROM TM_OSS_EMPLOYEE E
               WHERE E.DEPT_ID = DEPT_ROW.DEPT_ID
                 AND E.EMP_CODE IN
                     (SELECT EMP_CODE
                        FROM TT_PB_PROCESS_BY_MONTH_LOG T
                       WHERE T.DEPT_ID = DEPT_ROW.DEPT_ID
                         AND T.YM =
                             TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'YYYY-MM')
                       GROUP BY EMP_CODE
                      HAVING COUNT(EMP_CODE) = 1)) T;
      DEPTCOUNT := DEPTCOUNT + ONESUBMITCOUNT;
    
      -- 统计网点下排班吻合数
      -- 根据员工分组 循环累加每个员工排班的吻合数
      -- 统计网点下工序吻合数
      FOR COMMIT_ROW IN (SELECT T.YM, T.DEPT_ID, T.EMP_CODE
                           FROM TT_PB_PROCESS_BY_MONTH_LOG T,
                                TM_OSS_EMPLOYEE            E
                          WHERE T.DEPT_ID = E.DEPT_ID
                            AND T.EMP_CODE = E.EMP_CODE
                            AND T.DEPT_ID = DEPT_ROW.DEPT_ID
                            AND T.YM =
                                TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'YYYY-MM')
                          GROUP BY T.YM, T.DEPT_ID, T.EMP_CODE
                         HAVING COUNT(T.EMP_CODE) > 1) LOOP
        -- 根据员工分组 循环累加每个员工排班的吻合数
        SELECT (GET_COUNT_BY_COINCIDE(MAX_LOG.DAY1,
                                      MIN_LOG.DAY1,
                                      E.DIMISSION_DT,
                                      '01',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY2,
                                      MIN_LOG.DAY2,
                                      E.DIMISSION_DT,
                                      '02',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY3,
                                      MIN_LOG.DAY3,
                                      E.DIMISSION_DT,
                                      '03',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY4,
                                      MIN_LOG.DAY4,
                                      E.DIMISSION_DT,
                                      '04',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY5,
                                      MIN_LOG.DAY5,
                                      E.DIMISSION_DT,
                                      '05',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY6,
                                      MIN_LOG.DAY6,
                                      E.DIMISSION_DT,
                                      '06',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY7,
                                      MIN_LOG.DAY7,
                                      E.DIMISSION_DT,
                                      '07',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY8,
                                      MIN_LOG.DAY8,
                                      E.DIMISSION_DT,
                                      '08',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY9,
                                      MIN_LOG.DAY9,
                                      E.DIMISSION_DT,
                                      '09',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY10,
                                      MIN_LOG.DAY10,
                                      E.DIMISSION_DT,
                                      '10',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY11,
                                      MIN_LOG.DAY11,
                                      E.DIMISSION_DT,
                                      '11',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY12,
                                      MIN_LOG.DAY12,
                                      E.DIMISSION_DT,
                                      '12',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY13,
                                      MIN_LOG.DAY13,
                                      E.DIMISSION_DT,
                                      '13',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY14,
                                      MIN_LOG.DAY14,
                                      E.DIMISSION_DT,
                                      '14',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY15,
                                      MIN_LOG.DAY15,
                                      E.DIMISSION_DT,
                                      '15',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY16,
                                      MIN_LOG.DAY16,
                                      E.DIMISSION_DT,
                                      '16',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY17,
                                      MIN_LOG.DAY17,
                                      E.DIMISSION_DT,
                                      '17',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY18,
                                      MIN_LOG.DAY18,
                                      E.DIMISSION_DT,
                                      '18',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY19,
                                      MIN_LOG.DAY19,
                                      E.DIMISSION_DT,
                                      '19',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY20,
                                      MIN_LOG.DAY20,
                                      E.DIMISSION_DT,
                                      '20',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY21,
                                      MIN_LOG.DAY21,
                                      E.DIMISSION_DT,
                                      '21',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY22,
                                      MIN_LOG.DAY22,
                                      E.DIMISSION_DT,
                                      '22',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY23,
                                      MIN_LOG.DAY23,
                                      E.DIMISSION_DT,
                                      '23',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY24,
                                      MIN_LOG.DAY24,
                                      E.DIMISSION_DT,
                                      '24',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY25,
                                      MIN_LOG.DAY25,
                                      E.DIMISSION_DT,
                                      '25',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY26,
                                      MIN_LOG.DAY26,
                                      E.DIMISSION_DT,
                                      '26',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY27,
                                      MIN_LOG.DAY27,
                                      E.DIMISSION_DT,
                                      '27',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY28,
                                      MIN_LOG.DAY28,
                                      E.DIMISSION_DT,
                                      '28',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY29,
                                      MIN_LOG.DAY29,
                                      E.DIMISSION_DT,
                                      '29',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY30,
                                      MIN_LOG.DAY30,
                                      E.DIMISSION_DT,
                                      '30',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) +
               GET_COUNT_BY_COINCIDE(MAX_LOG.DAY31,
                                      MIN_LOG.DAY31,
                                      E.DIMISSION_DT,
                                      '31',
                                      TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              'YYYY-MM'),
                                      E.SF_DATE,
                                      E.TRANSFER_DATE,
                                      E.DATE_FROM) + COINCIDECOUNT)
          INTO COINCIDECOUNT
          FROM （SELECT *
          FROM TT_PB_PROCESS_BY_MONTH_LOG T
         WHERE T.ID IN (SELECT MAX(T.ID)
                          FROM TT_PB_PROCESS_BY_MONTH_LOG T
                         WHERE T.DEPT_ID = COMMIT_ROW.DEPT_ID
                           AND T.EMP_CODE = COMMIT_ROW.EMP_CODE
                           AND T.YM = COMMIT_ROW.YM
                         GROUP BY T.YM, T.DEPT_ID, T.EMP_CODE
                        HAVING COUNT(T.EMP_CODE) > 1)) MAX_LOG, (SELECT *
                                                                   FROM TT_PB_PROCESS_BY_MONTH_LOG T
                                                                  WHERE T.ID IN
                                                                        (SELECT MIN(T.ID)
                                                                           FROM TT_PB_PROCESS_BY_MONTH_LOG T
                                                                          WHERE T.DEPT_ID =
                                                                                COMMIT_ROW.DEPT_ID
                                                                            AND T.EMP_CODE =
                                                                                COMMIT_ROW.EMP_CODE
                                                                            AND T.YM =
                                                                                COMMIT_ROW.YM
                                                                          GROUP BY T.YM,
                                                                                   T.DEPT_ID,
                                                                                   T.EMP_CODE
                                                                         HAVING COUNT(T.EMP_CODE) > 1)) MIN_LOG, TM_OSS_EMPLOYEE E
         WHERE E.EMP_CODE = MIN_LOG.EMP_CODE
           AND E.EMP_CODE = MAX_LOG.EMP_CODE
           AND E.DEPT_ID = MIN_LOG.DEPT_ID
           AND E.DEPT_ID = MAX_LOG.DEPT_ID;
      END LOOP;
    
      COINCIDECOUNT := COINCIDECOUNT + ONESUBMITCOUNT;
      -- 计算吻合率
      -- 吻合数/网点总量 = 吻合率
    
      IF (DEPTCOUNT = 0) THEN
        RESULTNUM     := 0;
        COINCIDECOUNT := 0;
      ELSE
        RESULTNUM := NVL(ROUND((NVL(COINCIDECOUNT, 0) / DEPTCOUNT), 2) * 100,
                         0);
      END IF;
    
      INSERT INTO TT_STATISTICAL_COINCIDENCE
        (DEPT_ID,
         DEPT_CODE,
         YEAR_MONTH,
         COINCIDENCE_RATE,
         COINCIDENCERATE_COUNT,
         DEPT_COUNT,
         POSITION_TYPE)
      VALUES
        (DEPT_ROW.DEPT_ID,
         DEPT_ROW.DEPT_CODE,
         TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'YYYY-MM'),
         RESULTNUM,
         NVL(COINCIDECOUNT, 0),
         NVL(DEPTCOUNT, 0),
         2);
      COMMIT;
    END;
  END LOOP;

END STATISTICAL_COINCIDENCE_RATE;
/