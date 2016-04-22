CREATE OR REPLACE PROCEDURE SPMS_TO_CDP_EMP_COUNT IS
  --*************************************************************
  -- AUTHOR  : JHC
  -- CREATED : 2015-03-05
  -- PURPOSE : 统计未来三天的一线、仓管在职人数与排班数
  --
  -- PARAMETER:
  -- NAME             TYPE            DESC
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************
BEGIN

  EXECUTE IMMEDIATE 'TRUNCATE TABLE TT_CDP_EMPNUM_SCHEDULINGNUM';

  FOR V_COUNT IN 1 .. 3 LOOP
      BEGIN
        SPMS_TO_CDP_BY_DISPATCH(V_COUNT);
        END;

       BEGIN
        SPMS_TO_CDP_BY_WAREHOUSE(V_COUNT);
        END;

  END LOOP;

END SPMS_TO_CDP_EMP_COUNT;
/
CREATE OR REPLACE PROCEDURE SPMS_TO_CDP_BY_WAREHOUSE(V_COUNT IN NUMBER) IS
  --*************************************************************
  -- AUTHOR  : JHC
  -- CREATED : 2015-03-05
  -- PURPOSE : 统计未来三天的一线、仓管在职人数与排班数
  --
  -- PARAMETER:
  -- NAME             TYPE            DESC
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************
  FULL_TIME                  NUMBER;
  NO_FULL_TIME               NUMBER;
  TOTAL_PAYROLLS             NUMBER;
  FULL_TIME_SCHE_COUNT       NUMBER;
  NO_FULL_TIME_SCHE_COUNT    NUMBER;
  NO_FULL_TIME_WORKING_HOURS NUMBER;
  NUMBER_OF_TOTAL_SCHEDULING NUMBER;
  ORDER_SCHEDULING_NUM       NUMBER;

BEGIN
  FOR DEPT_ROW IN (SELECT E.DEPT_ID,
                          D.DEPT_CODE,
                          D.AREA_CODE,
                          D.HQ_CODE,
                          D.DIVISION_CODE
                     FROM TM_OSS_EMPLOYEE E, TM_DEPARTMENT D
                    WHERE E.EMP_POST_TYPE = '3'
                      AND E.EMP_DUTY_NAME IN ('仓管员','仓管组长')
                      AND E.DEPT_ID = D.DEPT_ID
                      AND E.PERSG IS NOT NULL
                    GROUP BY E.DEPT_ID,
                             D.DEPT_CODE,
                             D.AREA_CODE,
                             D.HQ_CODE,
                             D.DIVISION_CODE)

   LOOP

    FULL_TIME                  := 0;
    NO_FULL_TIME               := 0;
    FULL_TIME_SCHE_COUNT       := 0;
    NO_FULL_TIME_SCHE_COUNT    := 0;
    NO_FULL_TIME_WORKING_HOURS := 0;
    ORDER_SCHEDULING_NUM       := 0;

    --获取全日制员工数
    SELECT COUNT(E.EMP_CODE)
      INTO FULL_TIME
      FROM TM_OSS_EMPLOYEE E
     WHERE E.EMP_POST_TYPE = '3'
       AND E.EMP_DUTY_NAME IN ('仓管员','仓管组长')
       AND E.PERSG = 'A'
       AND E.DEPT_ID = DEPT_ROW.DEPT_ID
       AND (E.DIMISSION_DT IS NULL OR E.DIMISSION_DT > SYSDATE + V_COUNT);

    -- 获取非全日制员工数
    SELECT COUNT(E.EMP_CODE)
      INTO NO_FULL_TIME
      FROM TM_OSS_EMPLOYEE E
     WHERE E.EMP_POST_TYPE = '3'
       AND E.EMP_DUTY_NAME IN ('仓管员','仓管组长')
       AND E.PERSG = 'C'
       AND E.DEPT_ID = DEPT_ROW.DEPT_ID
       AND (E.DIMISSION_DT IS NULL OR E.DIMISSION_DT > SYSDATE + V_COUNT);

    TOTAL_PAYROLLS := ROUND(FULL_TIME + (NO_FULL_TIME * 4 / 9), 2);

    -- 获取SPMS排班系统全日制排班员工数
    SELECT COUNT(TM_OSS_EMPLOYEE.EMP_CODE)
      INTO FULL_TIME_SCHE_COUNT
      FROM TM_OSS_EMPLOYEE TM_OSS_EMPLOYEE,
           TT_WAREHOUSE_EMP_DEPT_R ER,
           TM_DEPARTMENT D,
           TM_DEPARTMENT ERD,
           (SELECT DEPT_CODE
              FROM TM_DEPARTMENT
             WHERE DELETE_FLG = 0
               AND DEPT_ID = DEPT_ROW.DEPT_ID) DEPART
     WHERE TM_OSS_EMPLOYEE.EMP_CODE = ER.EMP_CODE(+)
       AND TM_OSS_EMPLOYEE.DEPT_ID = D.DEPT_ID
       AND ER.DEPT_ID = ERD.DEPT_ID(+)
       AND (TM_OSS_EMPLOYEE.DIMISSION_DT IS NULL OR
           TM_OSS_EMPLOYEE.DIMISSION_DT > SYSDATE + V_COUNT)

       AND (D.DEPT_CODE IN (DEPART.DEPT_CODE) OR
           ERD.DEPT_CODE IN (DEPART.DEPT_CODE))
       AND TM_OSS_EMPLOYEE.EMP_CODE IN
           (SELECT DISTINCT S.EMPLOYEE_CODE
              FROM TT_SCHEDULE_DAILY S
             WHERE S.DAY_OF_MONTH = TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD')
               AND S.EMP_POST_TYPE = '3'
               AND S.SCHEDULING_CODE != '休')
       AND TM_OSS_EMPLOYEE.EMP_POST_TYPE = '3'
       AND TM_OSS_EMPLOYEE.EMP_DUTY_NAME IN ('仓管员','仓管组长')
       AND TM_OSS_EMPLOYEE.PERSG = 'A';

    -- 获取非全日制排班员工数
      SELECT COUNT(TM_OSS_EMPLOYEE.EMP_CODE)
              INTO NO_FULL_TIME_SCHE_COUNT
              FROM TM_OSS_EMPLOYEE TM_OSS_EMPLOYEE,
             TT_WAREHOUSE_EMP_DEPT_R ER,
             TM_DEPARTMENT D,
             TM_DEPARTMENT ERD,
             (SELECT DEPT_CODE
                FROM TM_DEPARTMENT
               WHERE DELETE_FLG = 0
                 AND DEPT_ID = DEPT_ROW.DEPT_ID) DEPART
             WHERE TM_OSS_EMPLOYEE.EMP_CODE = ER.EMP_CODE(+)
               AND TM_OSS_EMPLOYEE.DEPT_ID = D.DEPT_ID
               AND ER.DEPT_ID = ERD.DEPT_ID(+)
               AND (TM_OSS_EMPLOYEE.DIMISSION_DT IS NULL OR
                   TM_OSS_EMPLOYEE.DIMISSION_DT > SYSDATE + V_COUNT)

               AND (D.DEPT_CODE IN (DEPART.DEPT_CODE) OR
                   ERD.DEPT_CODE IN (DEPART.DEPT_CODE))
               AND TM_OSS_EMPLOYEE.EMP_CODE IN
                   (SELECT DISTINCT S.EMPLOYEE_CODE
                      FROM TT_SCHEDULE_DAILY S
                     WHERE S.DAY_OF_MONTH =
                           TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD')
                       AND S.EMP_POST_TYPE = '3'
                       AND S.SCHEDULING_CODE != '休')
               AND TM_OSS_EMPLOYEE.EMP_POST_TYPE = '3'
               AND TM_OSS_EMPLOYEE.EMP_DUTY_NAME IN ('仓管员','仓管组长')
               AND TM_OSS_EMPLOYEE.PERSG = 'C';

            -- 所有非全排班人员工时之和/9
              SELECT NVL(ROUND(SUM(count_time_distance(TB.START1_TIME,
                                                       TB.END1_TIME)) / 9,
                               2),
                         0)
                      INTO NO_FULL_TIME_WORKING_HOURS
                      FROM TT_SCHEDULE_DAILY        T,
                     TM_DEPARTMENT            D,
                     TM_OSS_EMPLOYEE          E,
                     tm_pb_schedule_base_info tb
                     WHERE E.DEPT_ID = D.DEPT_ID
                       AND T.DEPARTMENT_CODE = D.DEPT_CODE
                       AND E.EMP_CODE = T.EMPLOYEE_CODE
                       AND E.DEPT_ID = TB.DEPT_ID
                       AND T.SCHEDULING_CODE = TB.SCHEDULE_CODE
                       AND TB.CLASS_TYPE = '2'
                       AND T.DAY_OF_MONTH =
                           TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD')
                       AND E.DEPT_ID = DEPT_ROW.DEPT_ID
                       AND T.EMP_POST_TYPE = '3'
                       AND E.PERSG = 'C'
                       AND E.EMP_DUTY_NAME IN ('仓管员','仓管组长')
                       AND E.EMP_POST_TYPE = '3'
                       AND (E.DIMISSION_DT IS NULL OR
                           E.DIMISSION_DT > SYSDATE + V_COUNT)
                       AND T.SCHEDULING_CODE != '休';



    NUMBER_OF_TOTAL_SCHEDULING := FULL_TIME_SCHE_COUNT +
                                  NO_FULL_TIME_WORKING_HOURS;

    INSERT INTO TT_CDP_EMPNUM_SCHEDULINGNUM
      (DAY_OF_MONTH,
       HQ_CODE,
       AREA_CODE,
       DEPT_CODE,
       DIVISION_CODE,
       FULL_TIME_NUM,
       N_FULL_TIME_NUM,
       TOTAL_PAYROLLS,
       FULL_TIME_SCHEDUL_NUM,
       N_FULL_TIME_SCHEDUL_NUM,
       SCHEDUL_NUM_TOTAL,
       POST_TYPE,
       CREATE_DATE)
    VALUES
      (TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD'),
       DEPT_ROW.HQ_CODE,
       DEPT_ROW.AREA_CODE,
       DEPT_ROW.DEPT_CODE,
       DEPT_ROW.DIVISION_CODE,
       FULL_TIME,
       NO_FULL_TIME,
       TOTAL_PAYROLLS,
       FULL_TIME_SCHE_COUNT,
       NO_FULL_TIME_SCHE_COUNT,
       NUMBER_OF_TOTAL_SCHEDULING,
       '仓管',
       SYSDATE);

    INSERT INTO TT_CDP_EMPNUM_SCHEDUL_HIS
      (DAY_OF_MONTH,
       HQ_CODE,
       AREA_CODE,
       DEPT_CODE,
       DIVISION_CODE,
       FULL_TIME_NUM,
       N_FULL_TIME_NUM,
       TOTAL_PAYROLLS,
       FULL_TIME_SCHEDUL_NUM,
       N_FULL_TIME_SCHEDUL_NUM,
       SCHEDUL_NUM_TOTAL,
       POST_TYPE,
       CREATE_DATE)
    VALUES
      (TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD'),
       DEPT_ROW.HQ_CODE,
       DEPT_ROW.AREA_CODE,
       DEPT_ROW.DEPT_CODE,
       DEPT_ROW.DIVISION_CODE,
       FULL_TIME,
       NO_FULL_TIME,
       TOTAL_PAYROLLS,
       FULL_TIME_SCHE_COUNT,
       NO_FULL_TIME_SCHE_COUNT,
       NUMBER_OF_TOTAL_SCHEDULING,
       '仓管',
       SYSDATE);
  END LOOP;
  COMMIT;
END SPMS_TO_CDP_BY_WAREHOUSE;
/
CREATE OR REPLACE PROCEDURE SPMS_TO_CDP_BY_dispatch(V_COUNT IN NUMBER) IS
  --*************************************************************
  -- AUTHOR  : JHC
  -- CREATED : 2015-03-05
  -- PURPOSE : 统计未来三天的一线、仓管在职人数与排班数
  --
  -- PARAMETER:
  -- NAME             TYPE            DESC
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************
  FULL_TIME                  NUMBER;
  NO_FULL_TIME               NUMBER;
  TOTAL_PAYROLLS             NUMBER;
  FULL_TIME_SCHE_COUNT       NUMBER;
  NO_FULL_TIME_SCHE_COUNT    NUMBER;
  NO_FULL_TIME_WORKING_HOURS NUMBER;
  NUMBER_OF_TOTAL_SCHEDULING NUMBER;
  ORDER_SCHEDULING_NUM       NUMBER;

BEGIN
  FOR DEPT_ROW IN (SELECT E.DEPT_ID,
                          D.DEPT_CODE,
                          D.AREA_CODE,
                          D.HQ_CODE,
                          D.DIVISION_CODE
                     FROM TM_OSS_EMPLOYEE E, TM_DEPARTMENT D
                    WHERE E.EMP_POST_TYPE = '2'
                      AND E.DEPT_ID = D.DEPT_ID
                      AND E.PERSG IS NOT NULL
                    GROUP BY E.DEPT_ID,
                             D.DEPT_CODE,
                             D.AREA_CODE,
                             D.HQ_CODE,
                             D.DIVISION_CODE)

   LOOP

    FULL_TIME                  := 0;
    NO_FULL_TIME               := 0;
    FULL_TIME_SCHE_COUNT       := 0;
    NO_FULL_TIME_SCHE_COUNT    := 0;
    NO_FULL_TIME_WORKING_HOURS := 0;
    ORDER_SCHEDULING_NUM       := 0;

    --获取全日制员工数
    SELECT COUNT(E.EMP_CODE)
      INTO FULL_TIME
      FROM TM_OSS_EMPLOYEE E
     WHERE E.EMP_POST_TYPE = '2'
       AND E.PERSG = 'A'
       AND E.DEPT_ID = DEPT_ROW.DEPT_ID
       AND (E.DIMISSION_DT IS NULL OR E.DIMISSION_DT > SYSDATE + V_COUNT);

    -- 获取非全日制员工数
    SELECT COUNT(E.EMP_CODE)
      INTO NO_FULL_TIME
      FROM TM_OSS_EMPLOYEE E
     WHERE E.EMP_POST_TYPE = '2'
       AND E.PERSG = 'C'
       AND E.DEPT_ID = DEPT_ROW.DEPT_ID
       AND (E.DIMISSION_DT IS NULL OR E.DIMISSION_DT > SYSDATE + V_COUNT);

    TOTAL_PAYROLLS := ROUND(FULL_TIME + (NO_FULL_TIME * 4 / 9), 2);

    -- 获取SPMS排班系统全日制排班员工数
    SELECT COUNT(E.EMP_CODE)
      INTO FULL_TIME_SCHE_COUNT
      FROM TM_OSS_EMPLOYEE E
     WHERE E.EMP_POST_TYPE = '2'
       AND E.PERSG = 'A'
       AND E.DEPT_ID = DEPT_ROW.DEPT_ID
       AND (E.DIMISSION_DT IS NULL OR E.DIMISSION_DT > SYSDATE + V_COUNT)
       AND E.EMP_CODE IN
           (SELECT T.EMPLOYEE_CODE
              FROM TT_SCHEDULE_DAILY T, TM_DEPARTMENT D
             WHERE T.DEPARTMENT_CODE = D.DEPT_CODE
               AND T.DAY_OF_MONTH = TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD')
               AND D.DEPT_ID = DEPT_ROW.DEPT_ID
               AND T.EMP_POST_TYPE = '2'
               AND T.BEGIN_TIME IS NOT NULL
               AND T.END_TIME IS NOT NULL);

    --获取订单调度系统全日制排班员工数
    SELECT COUNT(E.EMP_CODE)
      INTO ORDER_SCHEDULING_NUM
      FROM TM_OSS_EMPLOYEE E, TI_SCH_EMPLOYEECLASS_PLAIN TC
     WHERE E.EMP_CODE = TC.EMPLOYEEID
       AND E.EMP_POST_TYPE = '2'
       AND E.PERSG = 'A'
       AND E.DEPT_ID = DEPT_ROW.DEPT_ID
       AND (E.DIMISSION_DT IS NULL OR E.DIMISSION_DT > SYSDATE + V_COUNT)
       AND TC.DUTYDATE = TO_CHAR(SYSDATE + V_COUNT, 'YYYY-MM-DD');

    FULL_TIME_SCHE_COUNT := FULL_TIME_SCHE_COUNT + ORDER_SCHEDULING_NUM;

    -- 获取非全日制排班员工数
    SELECT COUNT(E.EMP_CODE)
      INTO NO_FULL_TIME_SCHE_COUNT
      FROM TM_OSS_EMPLOYEE E
     WHERE E.EMP_POST_TYPE = '2'
       AND E.PERSG = 'C'
       AND E.DEPT_ID = DEPT_ROW.DEPT_ID
       AND (E.DIMISSION_DT IS NULL OR E.DIMISSION_DT > SYSDATE + V_COUNT)
       AND E.EMP_CODE IN
           (SELECT T.EMPLOYEE_CODE
              FROM TT_SCHEDULE_DAILY T, TM_DEPARTMENT D
             WHERE T.DEPARTMENT_CODE = D.DEPT_CODE
               AND T.DAY_OF_MONTH = TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD')
               AND D.DEPT_ID = DEPT_ROW.DEPT_ID
               AND T.EMP_POST_TYPE = '2'
               AND T.BEGIN_TIME IS NOT NULL
               AND T.END_TIME IS NOT NULL);
    -- 所有非全排班人员工时之和/9
    SELECT NVL(ROUND(SUM((CASE
                           WHEN T.END_TIME - T.BEGIN_TIME > 0 THEN
                            T.END_TIME - T.BEGIN_TIME
                           ELSE
                            (T.END_TIME + 240000) - T.BEGIN_TIME
                         END) / 10000) / 9,
                     2),
               0)
      INTO NO_FULL_TIME_WORKING_HOURS
      FROM TT_SCHEDULE_DAILY T, TM_DEPARTMENT D, TM_OSS_EMPLOYEE E
     WHERE E.DEPT_ID = D.DEPT_ID
       AND T.DEPARTMENT_CODE = D.DEPT_CODE
       AND E.EMP_CODE = T.EMPLOYEE_CODE
       AND T.DAY_OF_MONTH = TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD')
       AND E.DEPT_ID = DEPT_ROW.DEPT_ID
       AND T.EMP_POST_TYPE = '2'
       AND E.PERSG = 'C'
       AND E.EMP_POST_TYPE = '2'
       AND (E.DIMISSION_DT IS NULL OR E.DIMISSION_DT > SYSDATE + V_COUNT)
       AND T.BEGIN_TIME IS NOT NULL
       AND T.END_TIME IS NOT NULL;

    NUMBER_OF_TOTAL_SCHEDULING := FULL_TIME_SCHE_COUNT +
                                  NO_FULL_TIME_WORKING_HOURS;

    INSERT INTO TT_CDP_EMPNUM_SCHEDULINGNUM
      (DAY_OF_MONTH,
       HQ_CODE,
       AREA_CODE,
       DEPT_CODE,
       DIVISION_CODE,
       FULL_TIME_NUM,
       N_FULL_TIME_NUM,
       TOTAL_PAYROLLS,
       FULL_TIME_SCHEDUL_NUM,
       N_FULL_TIME_SCHEDUL_NUM,
       SCHEDUL_NUM_TOTAL,
       POST_TYPE,
       CREATE_DATE)
    VALUES
      (TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD'),
       DEPT_ROW.HQ_CODE,
       DEPT_ROW.AREA_CODE,
       DEPT_ROW.DEPT_CODE,
       DEPT_ROW.DIVISION_CODE,
       FULL_TIME,
       NO_FULL_TIME,
       TOTAL_PAYROLLS,
       FULL_TIME_SCHE_COUNT,
       NO_FULL_TIME_SCHE_COUNT,
       NUMBER_OF_TOTAL_SCHEDULING,
       '一线',
       SYSDATE);

    INSERT INTO TT_CDP_EMPNUM_SCHEDUL_HIS
      (DAY_OF_MONTH,
       HQ_CODE,
       AREA_CODE,
       DEPT_CODE,
       DIVISION_CODE,
       FULL_TIME_NUM,
       N_FULL_TIME_NUM,
       TOTAL_PAYROLLS,
       FULL_TIME_SCHEDUL_NUM,
       N_FULL_TIME_SCHEDUL_NUM,
       SCHEDUL_NUM_TOTAL,
       POST_TYPE,
       CREATE_DATE)
    VALUES
      (TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD'),
       DEPT_ROW.HQ_CODE,
       DEPT_ROW.AREA_CODE,
       DEPT_ROW.DEPT_CODE,
       DEPT_ROW.DIVISION_CODE,
       FULL_TIME,
       NO_FULL_TIME,
       TOTAL_PAYROLLS,
       FULL_TIME_SCHE_COUNT,
       NO_FULL_TIME_SCHE_COUNT,
       NUMBER_OF_TOTAL_SCHEDULING,
       '一线',
       SYSDATE);
  END LOOP;
  COMMIT;
END SPMS_TO_CDP_BY_dispatch;
/