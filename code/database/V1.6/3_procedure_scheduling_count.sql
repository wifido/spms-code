CREATE OR REPLACE PROCEDURE SCHEDULING_COUNT(COUNT_YM in varchar) IS

  --*************************************************************
  -- AUTHOR  : JHC
  -- CREATED : 2015-03-13
  -- PURPOSE : 根据网点排班情况。
  --
  -- PARAMETER:
  -- NAME             TYPE            SCHEDULING_COUNT
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************
  SHEDULE_NUM        NUMBER;
  GROUP_NUM          NUMBER;
  PROCESS_NUM        NUMBER;
  SHEDULE_TIME_COUNT NUMBER;
  LENGTH_TIME_OF_DAY NUMBER;
  REST_DAYS          NUMBER;
  TOTAL_ATTENDANCE   NUMBER;

BEGIN

  DELETE OPERATION_COUNT_SHEDULING TMP WHERE TMP.MONTH_ID = COUNT_YM;
  -- 查询运作所有网点所有员工。
  FOR EMP_NUM_ROW IN (SELECT TD.DEPT_ID,
                             TD.AREA_CODE,
                             TD.DEPT_CODE,
                             E.EMP_CODE,
                             E.EMP_NAME,
                             E.PERSK_TXT,
                             E.WORK_TYPE,
                             E.SF_DATE,
                             CASE
                               WHEN E.DIMISSION_DT IS NULL THEN
                                1
                               WHEN E.DIMISSION_DT > SYSDATE THEN
                                1
                               WHEN E.DIMISSION_DT <= SYSDATE THEN
                                0
                             END EMP_STATUS
                        FROM TM_DEPARTMENT TD, TM_OSS_EMPLOYEE E
                       WHERE E.DEPT_ID = TD.DEPT_ID
                         AND E.EMP_POST_TYPE = '1'
                         and E.DEPT_ID in
                             (select DEPT_ID
                                from TM_DEPARTMENT T
                               where REGEXP_LIKE(T.DEPT_CODE,
                                                 '^[0-9]{3,4}[WRX]')
                               START WITH T.DEPT_ID = 1
                              CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE)) LOOP

    SHEDULE_NUM        := 0;
    GROUP_NUM          := 0;
    PROCESS_NUM        := 0;
    SHEDULE_TIME_COUNT := 0;
    LENGTH_TIME_OF_DAY := 0;
    REST_DAYS          := 0;
    TOTAL_ATTENDANCE   := 0;
    --  查询班别数
    SELECT COUNT(*) CLASS_TOTAL
      INTO SHEDULE_NUM
      FROM (SELECT TD.EMP_CODE, TD.SHEDULE_CODE, TD.DEPT_ID, TM.YM
              FROM TT_PB_SHEDULE_BY_DAY TD, TT_PB_SHEDULE_BY_MONTH TM
             WHERE TD.DEPT_ID = TM.DEPT_ID
               AND TD.SHEDULE_MON_ID = TM.ID
               AND TD.SHEDULE_CODE NOT IN ('休', '离')
               AND TM.YM = COUNT_YM
               AND TM.DEPT_ID = EMP_NUM_ROW.DEPT_ID
               AND TM.EMP_CODE = EMP_NUM_ROW.EMP_CODE
             GROUP BY TD.EMP_CODE, TD.SHEDULE_CODE, TD.DEPT_ID, TM.YM) MONTH_TOTAL;

    --  查询小组数
    SELECT COUNT(EMP_CODE)
      INTO GROUP_NUM
      FROM OP_EMP_GROUP_MODIFY_RECORD RC
     WHERE TO_CHAR(RC.ENABLE_TM, 'YYYY-MM') = COUNT_YM
       AND RC.EMP_CODE = EMP_NUM_ROW.EMP_CODE
       AND RC.ENABLE_STATE = 1
       AND RC.PREV_GROUP_ID IS NOT NULL;

    SELECT COUNT(EMP_CODE) + GROUP_NUM
      INTO GROUP_NUM
      FROM OP_EMP_GROUP_MODIFY_RECORD RC
     WHERE TO_CHAR(RC.ENABLE_TM, 'YYYY-MM') = COUNT_YM
       AND RC.EMP_CODE = EMP_NUM_ROW.EMP_CODE
       AND RC.ENABLE_STATE = 1
       AND RC.PREV_GROUP_ID IS NULL;

    --  查询工序数
    SELECT COUNT(*) AS CLASS_TOTAL
      INTO PROCESS_NUM
      FROM (SELECT TD.EMP_CODE
              FROM TT_PB_PROCESS_BY_DAY TD, TT_PB_PROCESS_BY_MONTH TM
             WHERE TD.DEPT_ID = TM.DEPT_ID
               AND TD.PROCESS_MON_ID = TM.ID
               AND TD.PROCESS_CODE NOT IN ('休', '离')
               AND TM.YM = COUNT_YM
               AND TM.DEPT_ID = EMP_NUM_ROW.DEPT_ID
               AND TM.EMP_CODE = EMP_NUM_ROW.EMP_CODE
             GROUP BY TD.EMP_CODE, TD.PROCESS_CODE, TD.DEPT_ID, TM.YM);

    --  查询员工排班时长
    FOR SHEDULE_CODE_ROW IN (SELECT TD.EMP_CODE,
                                    TD.SHEDULE_CODE,
                                    COUNT(SHEDULE_CODE) SHEDULE_CODE_NUM,
                                    YM,
                                    TD.DEPT_ID
                               FROM TT_PB_SHEDULE_BY_DAY   TD,
                                    TT_PB_SHEDULE_BY_MONTH TM
                              WHERE TD.DEPT_ID = TM.DEPT_ID
                                AND TD.SHEDULE_MON_ID = TM.ID
                                AND TD.SHEDULE_CODE NOT IN ('休', '离')
                                AND TM.YM = COUNT_YM
                                AND TM.DEPT_ID = EMP_NUM_ROW.DEPT_ID
                                AND TM.EMP_CODE = EMP_NUM_ROW.EMP_CODE
                              GROUP BY TD.EMP_CODE,
                                       TD.SHEDULE_CODE,
                                       TD.DEPT_ID,
                                       TM.YM) LOOP
      SELECT (COUNT_TIME_DISTANCE(TB.START1_TIME, TB.END1_TIME) +
             COUNT_TIME_DISTANCE(TB.START2_TIME, TB.END2_TIME) +
             COUNT_TIME_DISTANCE(TB.START3_TIME, TB.END3_TIME)) *
             SHEDULE_CODE_ROW.SHEDULE_CODE_NUM + SHEDULE_TIME_COUNT
        INTO SHEDULE_TIME_COUNT
        FROM TM_PB_SCHEDULE_BASE_INFO TB
       WHERE TB.YM = SHEDULE_CODE_ROW.YM
         AND TB.SCHEDULE_CODE = SHEDULE_CODE_ROW.SHEDULE_CODE
         AND TB.DEPT_ID = SHEDULE_CODE_ROW.DEPT_ID
         AND TB.CLASS_TYPE = '1';
    END LOOP;

    -- 查询员工日均时长
    SELECT COUNT(*)
      INTO LENGTH_TIME_OF_DAY
      FROM TT_PB_SHEDULE_BY_DAY TD, TT_PB_SHEDULE_BY_MONTH TM
     WHERE TD.DEPT_ID = TM.DEPT_ID
       AND TD.SHEDULE_MON_ID = TM.ID
       AND TD.SHEDULE_CODE NOT IN ('休', '离')
       AND TM.YM = COUNT_YM
       AND TM.DEPT_ID = EMP_NUM_ROW.DEPT_ID
       AND TM.EMP_CODE = EMP_NUM_ROW.EMP_CODE;

    IF LENGTH_TIME_OF_DAY != 0 THEN
      LENGTH_TIME_OF_DAY := ROUND(SHEDULE_TIME_COUNT / LENGTH_TIME_OF_DAY,2);
    END IF;
    -- 查询员工休息天数
    SELECT COUNT(*)
      INTO REST_DAYS
      FROM TT_PB_SHEDULE_BY_DAY TD, TT_PB_SHEDULE_BY_MONTH TM
     WHERE TD.DEPT_ID = TM.DEPT_ID
       AND TD.SHEDULE_MON_ID = TM.ID
       AND TD.SHEDULE_CODE = '休'
       AND TM.YM = COUNT_YM
       AND TM.DEPT_ID = EMP_NUM_ROW.DEPT_ID
       AND TM.EMP_CODE = EMP_NUM_ROW.EMP_CODE;

    -- 查询员工出勤天数
    SELECT COUNT(*)
      INTO TOTAL_ATTENDANCE
      FROM TI_TCAS_SPMS_SCHEDULE T
     WHERE TO_CHAR(T.WORK_DATE, 'YYYY-MM') = COUNT_YM
       AND T.EMP_CODE = EMP_NUM_ROW.EMP_CODE
       AND T.WORK_TIME != 0;

    INSERT INTO OPERATION_COUNT_SHEDULING
      (MONTH_ID,
       AREA_CODE,
       DEPT_CODE,
       EMP_CODE,
       EMP_NAME,
       PERSK_TXT,
       SF_DATE,
       EMP_STATUS,
       SHEDULE_NUM,
       GROUP_NUM,
       PROCESS_NUM,
       LENGTH_TIME_OF_DAY,
       REST_DAYS,
       TOTAL_ATTENDANCE,
       DEPT_ID)
    VALUES
      (COUNT_YM,
       EMP_NUM_ROW.AREA_CODE,
       EMP_NUM_ROW.DEPT_CODE,
       EMP_NUM_ROW.EMP_CODE,
       EMP_NUM_ROW.EMP_NAME,
       CASE WHEN EMP_NUM_ROW.WORK_TYPE = 6 THEN '外包' else
       EMP_NUM_ROW.PERSK_TXT end,
       EMP_NUM_ROW.SF_DATE,
       EMP_NUM_ROW.EMP_STATUS,
       SHEDULE_NUM,
       GROUP_NUM,
       PROCESS_NUM,
       LENGTH_TIME_OF_DAY,
       REST_DAYS,
       TOTAL_ATTENDANCE,
       EMP_NUM_ROW.DEPT_ID);
    COMMIT;

  END LOOP;

END SCHEDULING_COUNT;
/