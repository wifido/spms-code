CREATE OR REPLACE PROCEDURE SPMS2CDP_OPERATION_CLASS IS
  --*************************************************************
  -- AUTHOR  : KEDIYU
  -- CREATED : 2015-4-13
  -- PURPOSE : JHC
  -- SPMS排班系统推送给CDP系统接口表数据处理
  -- PARAMETER:
  -- NAME             TYPE            DESC
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************

  --1.定义执行序号
  L_CALL_NO NUMBER;
BEGIN
  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE TM_SPMS2CDP_BY_OPERATION_INFO';
  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SPMS2CDP_OPERATION_CLASS',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);
  FOR V_COUNT IN 1 .. 3 LOOP
    BEGIN
    
      FOR CDP_SHEDULE_ROW IN (SELECT D.DEPT_CODE
                                FROM TM_DEPARTMENT D
                               WHERE D.TYPE_CODE IN ('ZZC04-YJ',
                                                     'ZZC04-ERJ',
                                                     'ZZC05-SJ',
                                                     'HHZ05')) LOOP
        INSERT INTO TM_SPMS2CDP_BY_OPERATION_INFO
          (SCHEDULE_CODE,
           SCHEDULE_DT,
           DEPT_CODE,
           START1_TIME,
           END1_TIME,
           START2_TIME,
           END2_TIME,
           START3_TIME,
           END3_TIME,
           YM)
          SELECT DISTINCT PB.SCHEDULE_CODE,
                 TD.SHEDULE_DT,
                 D.DEPT_CODE,
                 TO_DATE(TO_CHAR(TD.SHEDULE_DT, 'YYYY-MM-DD') || ' ' ||
                         PB.START1_TIME,
                         'YYYY-MM-DD HH24:MI'),
                 CASE
                   WHEN PB.START1_TIME > PB.END1_TIME THEN
                    TO_DATE(TO_CHAR(TD.SHEDULE_DT + 1, 'YYYY-MM-DD') || ' ' || PB.END1_TIME,
                            'YYYY-MM-DD HH24:MI')
                   ELSE
                    TO_DATE(TO_CHAR(TD.SHEDULE_DT, 'YYYY-MM-DD') || ' ' || PB.END1_TIME,
                            'YYYY-MM-DD HH24:MI')
                 END,
                 CASE
                   WHEN PB.START2_TIME IS NOT NULL THEN
                    CASE
                      WHEN PB.START1_TIME > PB.END1_TIME THEN
                       TO_DATE(TO_CHAR(TD.SHEDULE_DT + 1, 'YYYY-MM-DD') || ' ' || PB.START2_TIME,
                               'YYYY-MM-DD HH24:MI')
                      ELSE
                       TO_DATE(TO_CHAR(TD.SHEDULE_DT, 'YYYY-MM-DD') || ' ' || PB.START2_TIME,
                               'YYYY-MM-DD HH24:MI')
                    END
                   ELSE
                    NULL
                 END,
                 CASE
                   WHEN PB.END2_TIME IS NOT NULL THEN
                    CASE
                      WHEN PB.START1_TIME > PB.END1_TIME OR PB.START2_TIME > PB.END2_TIME THEN
                       TO_DATE(TO_CHAR(TD.SHEDULE_DT + 1, 'YYYY-MM-DD') || ' ' || PB.END2_TIME,
                               'YYYY-MM-DD HH24:MI')
                      ELSE
                       TO_DATE(TO_CHAR(TD.SHEDULE_DT, 'YYYY-MM-DD') || ' ' || PB.END2_TIME,
                               'YYYY-MM-DD HH24:MI')
                    END
                   ELSE
                    NULL
                 END,
                 CASE
                   WHEN PB.START3_TIME IS NOT NULL THEN
                    CASE
                      WHEN PB.START1_TIME > PB.END1_TIME OR PB.START2_TIME > PB.END2_TIME THEN
                       TO_DATE(TO_CHAR(TD.SHEDULE_DT + 1, 'YYYY-MM-DD') || ' ' || PB.START3_TIME,
                               'YYYY-MM-DD HH24:MI')
                      ELSE
                       TO_DATE(TO_CHAR(TD.SHEDULE_DT, 'YYYY-MM-DD') || ' ' || PB.START3_TIME,
                               'YYYY-MM-DD HH24:MI')
                    END
                   ELSE
                    NULL
                 END,
                 CASE
                   WHEN PB.END3_TIME IS NOT NULL THEN
                    CASE
                      WHEN PB.START1_TIME > PB.END1_TIME OR PB.START2_TIME > PB.END2_TIME OR PB.START3_TIME > PB.END3_TIME THEN
                       TO_DATE(TO_CHAR(TD.SHEDULE_DT + 1, 'YYYY-MM-DD') || ' ' || PB.END3_TIME,
                               'YYYY-MM-DD HH24:MI')
                      ELSE
                       TO_DATE(TO_CHAR(TD.SHEDULE_DT, 'YYYY-MM-DD') || ' ' || PB.END3_TIME,
                               'YYYY-MM-DD HH24:MI')
                    END
                   ELSE
                    NULL
                 END,
                 PB.YM
            FROM TT_PB_SHEDULE_BY_DAY     TD,
                 TM_DEPARTMENT            D,
                 TM_PB_SCHEDULE_BASE_INFO PB
           WHERE TD.DEPT_ID = D.DEPT_ID
             AND TD.DEPT_ID = PB.DEPT_ID
             AND D.DEPT_CODE = CDP_SHEDULE_ROW.DEPT_CODE
             AND PB.YM = TO_CHAR(SYSDATE, 'YYYY-MM')
             AND TD.SHEDULE_CODE = PB.SCHEDULE_CODE
             AND TD.SHEDULE_DT =TO_DATE(TO_CHAR(SYSDATE+V_COUNT -1, 'YYYY-MM-DD'),'YYYY-MM-DD');
             
      END LOOP;
      COMMIT;
    END;
  END LOOP;
  --4.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SPMS2CDP_OPERATION_CLASS',
                               SYSDATE,
                               NULL,
                               NULL,
                               'END',
                               0,
                               L_CALL_NO);
                               
                                 --5.异常记录日志
EXCEPTION
  WHEN OTHERS THEN
    --回滚数据
    ROLLBACK;
    PKG_OSS_COMM.STP_RUNNING_LOG('',
                                 'SPMS2CDP_OPERATION_CLASS',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);

END SPMS2CDP_OPERATION_CLASS;
/
CREATE OR REPLACE PROCEDURE SPMS2CDP_BY_OPERATION IS
  --*************************************************************
  -- AUTHOR  : KEDIYU
  -- CREATED : 2015-4-13
  -- PURPOSE : JHC
  -- SPMS排班系统推送给CDP系统接口表数据处理
  -- PARAMETER:
  -- NAME             TYPE            DESC
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************
  -- 班次时长
  SHUTTLE_LENGTH NUMBER;
  -- 全日制员工在职人数
  FULL_TIME NUMBER;
  -- 非全日制员工在职人数
  NOT_FULL_TIME NUMBER;
  -- 外包在职人数
  OUT_EMP NUMBER;
  -- 网点ID
  V_DEPT_ID VARCHAR(18);
  -- 排班编码
  SCHEDULE_CODE VARCHAR(18);
  -- 全日制单个班别排班人数
  FULL_TIME_EMP_NUM NUMBER;
  -- 全日制排班人数
  FULL_TIME_EMP_NUM_COUNT NUMBER;
  -- 全日制出勤人数
  FULL_TIME_ATTENDANCE_COUNT NUMBER;
  -- 非全日制单个班别排班人数
  NOTFULL_TIME_EMP_NUM NUMBER;
  -- 非全日制排班人数
  NOTFULL_TIME_EMP_NUM_COUNT NUMBER;
  -- 非全日制出勤人数
  NOTFULL_TIME_ATTENDANCE_COUNT NUMBER;
  -- 外包单个班别人数
  OUT_EMP_NUM NUMBER;
  -- 外包排班人数
  OUT_EMP_NUM_COUNT NUMBER;
  -- 外包出勤人数
  OUT_EMP_ATTENDANCE_COUNT NUMBER;
  -- 出勤时长
  V_ATTENDANCE_LENGTH NUMBER;
  --1.定义执行序号
  L_CALL_NO NUMBER;
BEGIN
  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE TI_SPMS2CDP_OPERATION_REPORT';
  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SPMS2CDP_BY_OPERATION',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

  --  根据CDP中转班次清单循环每一个班次
  FOR CDP_SHEDULE_ROW IN (SELECT TRANS_BATCH_DATE,
                                 AREA_CODE,
                                 ZONE_CODE,
                                 TRANSFER_CODE,
                                 BEGIN_TM,
                                 END_TM,
                                 DATE_TM
                            FROM TI_TRANSFER_BATCH_LIST T) LOOP
  
    SHUTTLE_LENGTH                := 0;
    FULL_TIME                     := 0;
    NOT_FULL_TIME                 := 0;
    OUT_EMP                       := 0;
    FULL_TIME_EMP_NUM             := 0;
    FULL_TIME_EMP_NUM_COUNT       := 0;
    FULL_TIME_ATTENDANCE_COUNT    := 0;
    NOTFULL_TIME_EMP_NUM          := 0;
    NOTFULL_TIME_EMP_NUM_COUNT    := 0;
    NOTFULL_TIME_ATTENDANCE_COUNT := 0;
    OUT_EMP_NUM                   := 0;
    OUT_EMP_NUM_COUNT             := 0;
    OUT_EMP_ATTENDANCE_COUNT      := 0;
    V_ATTENDANCE_LENGTH           := 0;
  
    -- 计算班次时长
    SHUTTLE_LENGTH := ROUND(TO_NUMBER(CDP_SHEDULE_ROW.END_TM -
                                      CDP_SHEDULE_ROW.BEGIN_TM) * 24,
                            2);
    IF SHUTTLE_LENGTH < 0 THEN
      SHUTTLE_LENGTH := SHUTTLE_LENGTH + 24;
    END IF;
  
    -- 根据CDP中转场班次 网点代码获取网点ID
    SELECT DEPT_ID
      INTO V_DEPT_ID
      FROM TM_DEPARTMENT D
     WHERE D.DEPT_CODE = CDP_SHEDULE_ROW.ZONE_CODE;
  
    -- 统计全日制在职人数
    SELECT nvl(COUNT(E.EMP_CODE), 0)
      INTO FULL_TIME
      FROM TM_OSS_EMPLOYEE E
     WHERE E.DEPT_ID = V_DEPT_ID
       AND E.EMP_POST_TYPE = '1'
       AND E.PERSG = 'A'
       AND (E.DIMISSION_DT IS NULL OR
           E.DIMISSION_DT > CDP_SHEDULE_ROW.TRANS_BATCH_DATE);
    -- 统计外包在职人数
    SELECT nvl(COUNT(E.EMP_CODE), 0)
      INTO OUT_EMP
      FROM TM_OSS_EMPLOYEE E
     WHERE E.DEPT_ID = V_DEPT_ID
       AND E.EMP_POST_TYPE = '1'
       AND E.WORK_TYPE = '6'
       AND (E.DIMISSION_DT IS NULL OR
           E.DIMISSION_DT > CDP_SHEDULE_ROW.TRANS_BATCH_DATE);
    -- 统计非全日制在职人数
    SELECT nvl(COUNT(E.EMP_CODE), 0)
      INTO NOT_FULL_TIME
      FROM TM_OSS_EMPLOYEE E
     WHERE E.DEPT_ID = V_DEPT_ID
       AND E.EMP_POST_TYPE = '1'
       AND E.PERSG = 'C'
       AND (E.DIMISSION_DT IS NULL OR
           E.DIMISSION_DT > CDP_SHEDULE_ROW.TRANS_BATCH_DATE);
    -- 找出对应CDP中转场班次的开始时间和结束时间的班别 循环统计对应的排班人数和出勤人数
    FOR SCHEDULE_CODE_ROW IN (SELECT DISTINCT SCHEDULE_CODE
                                FROM (
SELECT SCHEDULE_CODE
  FROM TM_SPMS2CDP_BY_OPERATION_INFO T
 WHERE T.DEPT_CODE = CDP_SHEDULE_ROW.Zone_Code
   AND t.schedule_dt = CDP_SHEDULE_ROW.TRANS_BATCH_DATE
   AND T.YM = to_char(CDP_SHEDULE_ROW.TRANS_BATCH_DATE,'YYYY-MM')
   AND ((T.START1_TIME >=CDP_SHEDULE_ROW.Begin_Tm AND
       T.START1_TIME < CDP_SHEDULE_ROW.End_Tm)
       OR
       (T.END1_TIME > CDP_SHEDULE_ROW.Begin_Tm AND
       T.END1_TIME <= CDP_SHEDULE_ROW.End_Tm)
   OR
       (T.START2_TIME >= CDP_SHEDULE_ROW.Begin_Tm and
       T.START2_TIME < CDP_SHEDULE_ROW.End_Tm) 
        or (T.END2_TIME > CDP_SHEDULE_ROW.Begin_Tm AND
       T.END2_TIME <= CDP_SHEDULE_ROW.End_Tm)
       or (T.START3_TIME > CDP_SHEDULE_ROW.Begin_Tm AND
       START3_TIME < CDP_SHEDULE_ROW.End_Tm )
       or ( T.END3_TIME > CDP_SHEDULE_ROW.Begin_Tm AND
       END3_TIME < CDP_SHEDULE_ROW.End_Tm))
UNION ALL
SELECT T.SCHEDULE_CODE
  FROM TM_SPMS2CDP_BY_OPERATION_INFO T
 WHERE T.DEPT_CODE = CDP_SHEDULE_ROW.Zone_Code
   AND t.schedule_dt = CDP_SHEDULE_ROW.TRANS_BATCH_DATE
   AND T.YM = to_char(CDP_SHEDULE_ROW.TRANS_BATCH_DATE,'YYYY-MM')
   AND ((T.START1_TIME < CDP_SHEDULE_ROW.Begin_Tm AND
       T.END1_TIME >= CDP_SHEDULE_ROW.End_Tm) OR
       (T.START2_TIME < CDP_SHEDULE_ROW.Begin_Tm AND
       T.END3_TIME >= CDP_SHEDULE_ROW.End_Tm) OR
       (T.START3_TIME < CDP_SHEDULE_ROW.Begin_Tm AND
       T.END3_TIME >= CDP_SHEDULE_ROW.End_Tm))
UNION ALL
SELECT T.SCHEDULE_CODE
  FROM TM_SPMS2CDP_BY_OPERATION_INFO T
 WHERE T.DEPT_CODE = CDP_SHEDULE_ROW.Zone_Code
   AND t.schedule_dt = CDP_SHEDULE_ROW.TRANS_BATCH_DATE
   AND T.YM = to_char(CDP_SHEDULE_ROW.TRANS_BATCH_DATE,'YYYY-MM')
   AND CASE
         WHEN T.START1_TIME > T.END1_TIME THEN
          START1_TIME
         WHEN T.START2_TIME > T.END2_TIME THEN
          START2_TIME
         WHEN T.START3_TIME > T.END3_TIME THEN
          START3_TIME
         ELSE
          CDP_SHEDULE_ROW.End_Tm
       END < CDP_SHEDULE_ROW.End_Tm
)) LOOP
    
      FULL_TIME_EMP_NUM := 0;
    
      -- 统计全日制单个班别排班人数
      SELECT nvl(COUNT(*), 0)
        INTO FULL_TIME_EMP_NUM
        FROM TT_PB_SHEDULE_BY_DAY TB, TM_OSS_EMPLOYEE E
       WHERE E.DEPT_ID = TB.DEPT_ID
         AND E.EMP_CODE = TB.EMP_CODE
         AND E.EMP_POST_TYPE = '1'
         AND E.PERSG = 'A'
         AND (E.DIMISSION_DT IS NULL OR
             E.DIMISSION_DT > CDP_SHEDULE_ROW.TRANS_BATCH_DATE)
         AND TB.DEPT_ID = V_DEPT_ID
         AND TB.SHEDULE_CODE = SCHEDULE_CODE_ROW.SCHEDULE_CODE
         AND TB.SHEDULE_DT = CDP_SHEDULE_ROW.TRANS_BATCH_DATE;
    
      -- 统计全日制排班人数
      FULL_TIME_EMP_NUM_COUNT := FULL_TIME_EMP_NUM_COUNT +
                                 FULL_TIME_EMP_NUM;
    
      NOTFULL_TIME_EMP_NUM := 0;
      -- 非全日制单个班别排班人数
      SELECT nvl(COUNT(*), 0)
        INTO NOTFULL_TIME_EMP_NUM
        FROM TT_PB_SHEDULE_BY_DAY TB, TM_OSS_EMPLOYEE E
       WHERE E.DEPT_ID = TB.DEPT_ID
         AND E.EMP_CODE = TB.EMP_CODE
         AND E.EMP_POST_TYPE = '1'
         AND E.PERSG = 'C'
         AND (E.DIMISSION_DT IS NULL OR
             E.DIMISSION_DT > CDP_SHEDULE_ROW.TRANS_BATCH_DATE)
         AND TB.DEPT_ID = V_DEPT_ID
         AND TB.SHEDULE_CODE = SCHEDULE_CODE_ROW.SCHEDULE_CODE
         AND TB.SHEDULE_DT = CDP_SHEDULE_ROW.TRANS_BATCH_DATE;
    
      -- 非全日制排班人数
      NOTFULL_TIME_EMP_NUM_COUNT := NOTFULL_TIME_EMP_NUM_COUNT +
                                    NOTFULL_TIME_EMP_NUM;
      OUT_EMP_NUM                := 0;
    
      -- 外包单个班别排班人数
      SELECT nvl(COUNT(*), 0)
        INTO OUT_EMP_NUM
        FROM TT_PB_SHEDULE_BY_DAY TB, TM_OSS_EMPLOYEE E
       WHERE E.DEPT_ID = TB.DEPT_ID
         AND E.EMP_CODE = TB.EMP_CODE
         AND E.EMP_POST_TYPE = '1'
         AND E.WORK_TYPE = '6'
         AND (E.DIMISSION_DT IS NULL OR
             E.DIMISSION_DT > CDP_SHEDULE_ROW.TRANS_BATCH_DATE)
         AND TB.DEPT_ID = V_DEPT_ID
         AND TB.SHEDULE_CODE = SCHEDULE_CODE_ROW.SCHEDULE_CODE
         AND TB.SHEDULE_DT = CDP_SHEDULE_ROW.TRANS_BATCH_DATE;
      -- 外包排班人数
      OUT_EMP_NUM_COUNT := OUT_EMP_NUM_COUNT + OUT_EMP_NUM;
    
      V_ATTENDANCE_LENGTH := 0;
    
      -- 统计对应班别的有效时长 即出勤时长
      SELECT nvl(SUM(nvl(COUNT_ATTENDANCE_LENGTH(PB.START1_TIME,
                                                 PB.END1_TIME,
                                                 CDP_SHEDULE_ROW.BEGIN_TM,
                                                 CDP_SHEDULE_ROW.END_TM),
                         0) + nvl(COUNT_ATTENDANCE_LENGTH(PB.START2_TIME,
                                                          PB.END2_TIME,
                                                          CDP_SHEDULE_ROW.BEGIN_TM,
                                                          CDP_SHEDULE_ROW.END_TM),
                                  0) + nvl(COUNT_ATTENDANCE_LENGTH(PB.START3_TIME,
                                                                   PB.END3_TIME,
                                                                   CDP_SHEDULE_ROW.BEGIN_TM,
                                                                   CDP_SHEDULE_ROW.END_TM),
                                           0)),
                 0)
        INTO V_ATTENDANCE_LENGTH
        FROM TM_SPMS2CDP_BY_OPERATION_INFO PB
       WHERE PB.DEPT_CODE = CDP_SHEDULE_ROW.ZONE_CODE
         AND PB.SCHEDULE_CODE = SCHEDULE_CODE_ROW.SCHEDULE_CODE
         AND PB.SCHEDULE_DT = CDP_SHEDULE_ROW.TRANS_BATCH_DATE
         AND PB.YM = TO_CHAR(CDP_SHEDULE_ROW.TRANS_BATCH_DATE, 'YYYY-MM');
    
      -- 全日制出勤时长
      FULL_TIME_ATTENDANCE_COUNT := FULL_TIME_ATTENDANCE_COUNT +
                                    nvl(V_ATTENDANCE_LENGTH *
                                        FULL_TIME_EMP_NUM,
                                        0);
      -- 非全日制出勤时长
      NOTFULL_TIME_ATTENDANCE_COUNT := NOTFULL_TIME_ATTENDANCE_COUNT +
                                       nvl(V_ATTENDANCE_LENGTH *
                                           NOTFULL_TIME_EMP_NUM,
                                           0);
      -- 外包出勤时长
      OUT_EMP_ATTENDANCE_COUNT := OUT_EMP_ATTENDANCE_COUNT +
                                  nvl(V_ATTENDANCE_LENGTH * OUT_EMP_NUM, 0);
    
    END LOOP;
    -- 将数据插入接口表
    INSERT INTO TI_SPMS2CDP_OPERATION_REPORT
      (SCHEDULE_DT,
       AREA_CODE,
       DEPT_CODE,
       SCHEDULE_CODE,
       BEGIN_TM,
       END_TM,
       SHUTTLE_LENGTH,
       FTE_NUM,
       FTE_SCHEDULE_NUM,
       FTE_ATTENDANCE_NUM,
       OUT_EMP_NUM,
       OUT_SCHEDULE_NUM,
       OUT_ATTENDANCE_NUM,
       NFTE_NUM,
       NFTE_SCHEDULE_NUM,
       NFTE_ATTENDANCE_NUM,
       TOTAL_OF_ATTENDANCE,
       SYNC_DATE)
    VALUES
      (CDP_SHEDULE_ROW.TRANS_BATCH_DATE,
       CDP_SHEDULE_ROW.AREA_CODE,
       CDP_SHEDULE_ROW.ZONE_CODE,
       CDP_SHEDULE_ROW.TRANSFER_CODE,
       CDP_SHEDULE_ROW.BEGIN_TM,
       CDP_SHEDULE_ROW.END_TM,
       SHUTTLE_LENGTH,
       FULL_TIME,
       FULL_TIME_EMP_NUM_COUNT,
       FULL_TIME_ATTENDANCE_COUNT,
       OUT_EMP,
       OUT_EMP_NUM_COUNT,
       OUT_EMP_ATTENDANCE_COUNT,
       NOT_FULL_TIME,
       NOTFULL_TIME_EMP_NUM_COUNT,
       NOTFULL_TIME_ATTENDANCE_COUNT,
       FULL_TIME_ATTENDANCE_COUNT + OUT_EMP_ATTENDANCE_COUNT +
       NOTFULL_TIME_ATTENDANCE_COUNT,
       SYSDATE);
    COMMIT;
  
  END LOOP;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SPMS2CDP_BY_OPERATION',
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
                                 'SPMS2CDP_BY_OPERATION',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
  
END SPMS2CDP_BY_OPERATION;
/