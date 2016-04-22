CREATE OR REPLACE PROCEDURE DRIVER_WARNNING_COUNT(year_month in varchar) IS

  V_DRIVER_IDENTIFY VARCHAR(40);
  V_DRIVE_DAY       NUMBER(20);
  V_DEPT_CODE       VARCHAR(40);
  V_COUNT_NUM       NUMBER(20);
  V_EMP_NAME        VARCHAR(38);
  V_AREA_CODE       VARCHAR(38);
  --1.定义执行序号
  L_CALL_NO NUMBER;
BEGIN

  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'DRIVER_WARNNING_COUNT',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

  V_DRIVER_IDENTIFY := '';
  V_DRIVE_DAY       := '';
  V_DEPT_CODE       := '';
  V_COUNT_NUM       := 1;

  FOR DRIVER_ROW IN (SELECT T.DRIVER_IDENTIFY,
                            TO_CHAR(T.DRIVE_DAY, 'YYYYMMDD') DRIVE_DAY,
                            T.DEPT_CODE,
                            t.DRIVER_IDENTIFY || '00' EMP_NAME,
                            D.AREA_CODE
                       FROM TT_REPORT_FOR_DRIVER_WARNING T,
                            (SELECT T.DRIVER_IDENTIFY
                               FROM TT_REPORT_FOR_DRIVER_WARNING T
                              GROUP BY T.DEPT_CODE, T.DRIVER_IDENTIFY
                             HAVING(COUNT(T.DRIVER_IDENTIFY)) >= 7) TT,
                            Tm_Oss_Employee E,
                            Tm_Department D
                      WHERE NOT EXISTS
                      (SELECT 1
                               FROM TI_VMS_DRIVE_CONVERT_BAK BAK
                              WHERE BAK.DRIVING_LOG_ITEM_ID =
                                    T.DRIVING_LOG_ITEM_ID)
                        AND T.DRIVER_IDENTIFY = TT.DRIVER_IDENTIFY
                        AND E.EMP_CODE = T.DRIVER_IDENTIFY
                        AND D.DEPT_CODE = T.DEPT_CODE
                        AND to_char(t.drive_day, 'yyyy-mm') = year_month
                      GROUP BY T.DRIVER_IDENTIFY,
                               to_char(T.DRIVE_DAY, 'YYYYMMDD'),
                               T.DEPT_CODE,
                               E.EMP_NAME,
                               D.AREA_CODE
                      ORDER BY T.DRIVER_IDENTIFY,
                               to_char(T.DRIVE_DAY, 'YYYYMMDD'),
                               T.DEPT_CODE) LOOP
  
    IF V_DRIVER_IDENTIFY = DRIVER_ROW.DRIVER_IDENTIFY AND
       V_DEPT_CODE = DRIVER_ROW.DEPT_CODE THEN
    
      IF V_DRIVE_DAY + 1 = DRIVER_ROW.DRIVE_DAY THEN
        V_COUNT_NUM       := V_COUNT_NUM + 1;
        V_DRIVER_IDENTIFY := DRIVER_ROW.DRIVER_IDENTIFY;
        V_DRIVE_DAY       := DRIVER_ROW.DRIVE_DAY;
        V_DEPT_CODE       := DRIVER_ROW.DEPT_CODE;
        V_EMP_NAME        := DRIVER_ROW.EMP_NAME;
        V_AREA_CODE       := DRIVER_ROW.AREA_CODE;
      
      ELSE
        IF V_COUNT_NUM >= 7 THEN
          INSERT INTO tt_driver_warnning_info
            (AREA_CODE,
             DRIVER_IDENTIFY,
             DRIVE_DAY,
             DEPT_CODE,
             EMP_NAME,
             WARNNING_DAYS)
          VALUES
            (V_AREA_CODE,
             V_DRIVER_IDENTIFY,
             TO_DATE(V_DRIVE_DAY, 'YYYYMMDD'),
             V_DEPT_CODE,
             V_EMP_NAME,
             V_COUNT_NUM);
          COMMIT;
        END IF;
      
        V_EMP_NAME        := DRIVER_ROW.EMP_NAME;
        V_AREA_CODE       := DRIVER_ROW.AREA_CODE;
        V_DRIVER_IDENTIFY := DRIVER_ROW.DRIVER_IDENTIFY;
        V_DRIVE_DAY       := DRIVER_ROW.DRIVE_DAY;
        V_DEPT_CODE       := DRIVER_ROW.DEPT_CODE;
        V_COUNT_NUM       := 1;
      END IF;
    ELSE
    
      IF V_COUNT_NUM >= 7 THEN
        INSERT INTO tt_driver_warnning_info
          (AREA_CODE,
           DRIVER_IDENTIFY,
           DRIVE_DAY,
           DEPT_CODE,
           EMP_NAME,
           WARNNING_DAYS)
        VALUES
          (V_AREA_CODE,
           V_DRIVER_IDENTIFY,
           TO_DATE(V_DRIVE_DAY, 'YYYYMMDD'),
           V_DEPT_CODE,
           V_EMP_NAME,
           V_COUNT_NUM);
        COMMIT;
      END IF;
      V_EMP_NAME        := DRIVER_ROW.EMP_NAME;
      V_AREA_CODE       := DRIVER_ROW.AREA_CODE;
      V_DRIVER_IDENTIFY := DRIVER_ROW.DRIVER_IDENTIFY;
      V_DRIVE_DAY       := DRIVER_ROW.DRIVE_DAY;
      V_DEPT_CODE       := DRIVER_ROW.DEPT_CODE;
      V_COUNT_NUM       := 1;
    
    END IF;
  END LOOP;

  IF V_COUNT_NUM >= 7 THEN
    INSERT INTO tt_driver_warnning_info
      (AREA_CODE,
       DRIVER_IDENTIFY,
       DRIVE_DAY,
       DEPT_CODE,
       EMP_NAME,
       WARNNING_DAYS)
    VALUES
      (V_AREA_CODE,
       V_DRIVER_IDENTIFY,
       TO_DATE(V_DRIVE_DAY, 'YYYYMMDD'),
       V_DEPT_CODE,
       V_EMP_NAME,
       V_COUNT_NUM);
    COMMIT;
  END IF;

  --4.结束记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'DRIVER_WARNNING_COUNT',
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
                                 'DRIVER_WARNNING_COUNT',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
END DRIVER_WARNNING_COUNT;
/
