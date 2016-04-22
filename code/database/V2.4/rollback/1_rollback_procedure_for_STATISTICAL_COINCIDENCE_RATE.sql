CREATE OR REPLACE PROCEDURE STATISTICAL_COINCIDENCE_RATE IS

  --*************************************************************
  -- AUTHOR  : JHC
  -- CREATED : 2015-03-13
  -- PURPOSE : ��������ͳ���Ű��Ǻ��ʺ͹����Ǻ��ʡ�
  --
  -- PARAMETER:
  -- NAME             TYPE            DESCSTATISTICAL_COINCIDENCE_RATE
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************
  DEPT_TOTAL_COUNT      NUMBER; --����������
  DEPT_COINCIDE_COUNT   NUMBER; --�����Ǻ�����
  DEPT_COINCIDE_RATE    NUMBER; --�����Ǻ�����
  V_YEAR_MONTH            VARCHAR2(20); --�·�
  INDEX_DAY_OF_MONTH    VARCHAR2(20); --����
  EMP_TOTAL_COUNT       NUMBER; --Ա��������
  EMP_COINCID_COUNT     NUMBER; --Ա���Ǻ�����
  EMP_NOT_COINCID_COUNT NUMBER; --Ա�����Ǻ�����
  --1.����ִ�����
  L_CALL_NO NUMBER;
BEGIN
  --2.����ִ�����
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.��ʼ��¼��־
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

  ----�����������㣬���ͳ���Ű��Ǻ���
  FOR DEPT_ROW IN (SELECT * FROM OP_DEPT) LOOP

    -----��ʼ��ֵ
    DEPT_TOTAL_COUNT    := 0;
    DEPT_COINCIDE_COUNT := 0;
    DEPT_COINCIDE_RATE  := 0;

    -----ɾ���������Ǻ�ͳ������
    DELETE FROM TT_STATISTICAL_COINCIDENCE T
     WHERE T.YEAR_MONTH = V_YEAR_MONTH
       AND T.DEPT_ID = DEPT_ROW.DEPT_ID
       AND T.POSITION_TYPE = 1;

    -----ɾ��������ÿ��Ա���Ű��Ǻ�ͳ�����
    DELETE FROM TT_STATISTICAL_COINCIDENCE_EMP T
     WHERE T.DEPT_ID = DEPT_ROW.DEPT_ID
       AND T.YEAR_MONTH = YEAR_MONTH
       AND T.POSITION_TYPE = 1;

    COMMIT;

    -----�����������Ű��ÿ��Ա�������ͳ���Ű��Ǻ����
    FOR EMPLOYEE_SCHEDULING_ROW IN (SELECT T.YM, T.DEPT_ID, T.EMP_CODE
                                      FROM TT_PB_SHEDULE_BY_MONTH_LOG T,
                                           TM_OSS_EMPLOYEE            E
                                     WHERE T.EMP_CODE = E.EMP_CODE
                                       AND T.DEPT_ID = E.DEPT_ID
                                       AND T.DEPT_ID = DEPT_ROW.DEPT_ID
                                       AND T.YM = V_YEAR_MONTH
                                     GROUP BY T.YM, T.DEPT_ID, T.EMP_CODE) LOOP
      -----��ʼ��ֵ
      EMP_TOTAL_COUNT       := 0;
      EMP_COINCID_COUNT     := 0;

      EMP_NOT_COINCID_COUNT := 0;

      ----��ѯԱ���ܹ�����Ч�Ű�����
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

      -----��ѯԱ���Ű಻�Ǻϵ�����
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

      -----����Ա���Ű��Ǻ�ͳ������
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

    -----ͳ�Ʋ������Ű�����
    SELECT NVL(SUM(T.SCHEDULING_DAYS), 0)
      INTO DEPT_TOTAL_COUNT
      FROM TT_STATISTICAL_COINCIDENCE_EMP T
     WHERE T.DEPT_ID = DEPT_ROW.DEPT_ID
       AND T.YEAR_MONTH = V_YEAR_MONTH
       AND T.POSITION_TYPE = 1;

    -----ͳ�Ʋ������Ǻ�����
    SELECT NVL(SUM(T.COINCIDENCE_DAYS), 0)
      INTO DEPT_COINCIDE_COUNT
      FROM TT_STATISTICAL_COINCIDENCE_EMP T
     WHERE T.DEPT_ID = DEPT_ROW.DEPT_ID
       AND T.YEAR_MONTH = V_YEAR_MONTH
       AND T.POSITION_TYPE = 1;

    -----�����Ǻ���
    IF DEPT_COINCIDE_COUNT > 1 THEN
      DEPT_COINCIDE_RATE := ROUND(DEPT_COINCIDE_COUNT / DEPT_TOTAL_COUNT * 100);
    END IF;

    -----���벿���Ű��Ǻ�������
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
   --3.��ʼ��¼��־
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
    --�ع�����
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