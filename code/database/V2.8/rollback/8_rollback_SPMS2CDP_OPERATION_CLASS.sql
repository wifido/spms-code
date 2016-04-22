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
  FOR V_COUNT IN 1 .. 5 LOOP
    BEGIN

      FOR CDP_SHEDULE_ROW IN (SELECT D.DEPT_CODE FROM op_dept D
        ) LOOP
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
                            WHEN LPAD(PB.START1_TIME, 5, '0') > LPAD(PB.END1_TIME, 5, '0') THEN
                             TO_DATE(TO_CHAR(TD.SHEDULE_DT + 1, 'YYYY-MM-DD') || ' ' || PB.END1_TIME,
                                     'YYYY-MM-DD HH24:MI')
                            ELSE
                             TO_DATE(TO_CHAR(TD.SHEDULE_DT, 'YYYY-MM-DD') || ' ' || PB.END1_TIME,
                                     'YYYY-MM-DD HH24:MI')
                          END,
                          CASE
                            WHEN PB.START2_TIME IS NOT NULL THEN
                             CASE
                               WHEN LPAD(PB.START1_TIME, 5, '0') > LPAD(PB.END1_TIME, 5, '0') THEN
                                TO_DATE(TO_CHAR(TD.SHEDULE_DT + 1, 'YYYY-MM-DD') || ' ' ||
                                        PB.START2_TIME,
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
                               WHEN LPAD(PB.START1_TIME, 5, '0') > LPAD(PB.END1_TIME, 5, '0') OR
                                    LPAD(PB.START2_TIME, 5, '0') > LPAD(PB.END2_TIME, 5, '0') THEN
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
                               WHEN LPAD(PB.START1_TIME, 5, '0') > LPAD(PB.END1_TIME, 5, '0') OR
                                    LPAD(PB.START2_TIME, 5, '0') > LPAD(PB.END2_TIME, 5, '0') THEN
                                TO_DATE(TO_CHAR(TD.SHEDULE_DT + 1, 'YYYY-MM-DD') || ' ' ||
                                        PB.START3_TIME,
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
                               WHEN LPAD(PB.START1_TIME, 5, '0') > LPAD(PB.END1_TIME, 5, '0') OR
                                    LPAD(PB.START2_TIME, 5, '0') > LPAD(PB.END2_TIME, 5, '0') OR
                                    LPAD(PB.START3_TIME, 5, '0') > LPAD(PB.END3_TIME, 5, '0') THEN
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
             AND TD.SHEDULE_DT =
                 TO_DATE(TO_CHAR(SYSDATE + V_COUNT - 2, 'YYYY-MM-DD'),
                         'YYYY-MM-DD');

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