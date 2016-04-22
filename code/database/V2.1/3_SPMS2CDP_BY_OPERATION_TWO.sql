CREATE OR REPLACE PROCEDURE SPMS2CDP_BY_OPERATION_TWO AS
  --*************************************************************
  -- AUTHOR  : KEDIYU
  -- CREATED : 2015-4-13
  -- PURPOSE : JHC
  -- SPMS�Ű�ϵͳ���͸�CDPϵͳ�ӿڱ����ݴ���
  -- PARAMETER:
  -- NAME             TYPE            DESC
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************
  -- ���ʱ��
  SHUTTLE_LENGTH NUMBER;
  -- ȫ����Ա����ְ����
  FULL_TIME NUMBER;
  -- ��ȫ����Ա����ְ����
  NOT_FULL_TIME NUMBER;
  -- �����ְ����
  OUT_EMP NUMBER;
  -- ����ID
  V_DEPT_ID VARCHAR(18);
  -- �Ű����
  SCHEDULE_CODE VARCHAR(18);
  -- ȫ���Ƶ�������Ű�����
  FULL_TIME_EMP_NUM NUMBER;
  -- ȫ�����Ű�����
  FULL_TIME_EMP_NUM_COUNT NUMBER;
  -- ȫ���Ƴ�������
  FULL_TIME_ATTENDANCE_COUNT NUMBER;
  -- ��ȫ���Ƶ�������Ű�����
  NOTFULL_TIME_EMP_NUM NUMBER;
  -- ��ȫ�����Ű�����
  NOTFULL_TIME_EMP_NUM_COUNT NUMBER;
  -- ��ȫ���Ƴ�������
  NOTFULL_TIME_ATTENDANCE_COUNT NUMBER;
  -- ��������������
  OUT_EMP_NUM NUMBER;
  -- ����Ű�����
  OUT_EMP_NUM_COUNT NUMBER;
  -- �����������
  OUT_EMP_ATTENDANCE_COUNT NUMBER;
  -- ����ʱ��
  V_ATTENDANCE_LENGTH NUMBER;

  V_DEPT_NUMBER NUMBER;
  --1.����ִ�����
  L_CALL_NO NUMBER;

BEGIN
  --2.����ִ�����
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE TI_SPMS2CDP_OPERATION_WARNING';

  EXECUTE IMMEDIATE 'TRUNCATE TABLE TT_SPMS2CDP_NUM';

  --3.��ʼ��¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SPMS2CDP_BY_OPERATION_TWO',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

  --  ����CDP��ת����嵥ѭ��ÿһ�����
  FOR CDP_SHEDULE_ROW IN (SELECT *
                            FROM CDP2SMPS_TRANS_BATCH_DATA t) LOOP
  
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
  
    -- ������ʱ��
    SHUTTLE_LENGTH := ROUND(TO_NUMBER(CDP_SHEDULE_ROW.END_TM -
                                      CDP_SHEDULE_ROW.BEGIN_TM) * 24,
                            2);
    IF SHUTTLE_LENGTH < 0 THEN
      SHUTTLE_LENGTH := SHUTTLE_LENGTH + 24;
    END IF;
  
    SELECT NVL(COUNT(*), 0)
      INTO V_DEPT_NUMBER
      FROM TM_DEPARTMENT D
     WHERE D.DEPT_CODE = CDP_SHEDULE_ROW.ZONE_CODE
       AND D.DELETE_FLG = 0;
  
    IF V_DEPT_NUMBER = 0 THEN
      -- �����ݲ���ӿڱ�
      INSERT INTO TI_SPMS2CDP_OPERATION_WARNING
        (SCHEDULE_DT,
         AREA_CODE,
         DEPT_CODE,
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
         CDP_SHEDULE_ROW.BEGIN_TM,
         CDP_SHEDULE_ROW.END_TM,
         SHUTTLE_LENGTH,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         SYSDATE);
    ELSE
      -- ����CDP��ת����� ��������ȡ����ID
      SELECT DEPT_ID
        INTO V_DEPT_ID
        FROM TM_DEPARTMENT D
       WHERE D.DEPT_CODE = CDP_SHEDULE_ROW.ZONE_CODE;
    
      -- ͳ��ȫ������ְ����
      SELECT NVL(COUNT(E.EMP_CODE), 0)
        INTO FULL_TIME
        FROM TM_OSS_EMPLOYEE E
       WHERE E.DEPT_ID = V_DEPT_ID
         AND E.EMP_POST_TYPE = '1'
         AND E.PERSG = 'A'
         AND (E.DIMISSION_DT IS NULL OR
             E.DIMISSION_DT > CDP_SHEDULE_ROW.TRANS_BATCH_DATE);
      -- ͳ�������ְ����
      SELECT NVL(COUNT(E.EMP_CODE), 0)
        INTO OUT_EMP
        FROM TM_OSS_EMPLOYEE E
       WHERE E.DEPT_ID = V_DEPT_ID
         AND E.EMP_POST_TYPE = '1'
         AND E.WORK_TYPE = '6'
         AND (E.DIMISSION_DT IS NULL OR
             E.DIMISSION_DT > CDP_SHEDULE_ROW.TRANS_BATCH_DATE);
      -- ͳ�Ʒ�ȫ������ְ����
      SELECT NVL(COUNT(E.EMP_CODE), 0)
        INTO NOT_FULL_TIME
        FROM TM_OSS_EMPLOYEE E
       WHERE E.DEPT_ID = V_DEPT_ID
         AND E.EMP_POST_TYPE = '1'
         AND E.PERSG = 'C'
         AND (E.DIMISSION_DT IS NULL OR
             E.DIMISSION_DT > CDP_SHEDULE_ROW.TRANS_BATCH_DATE);
      -- �ҳ���ӦCDP��ת����εĿ�ʼʱ��ͽ���ʱ��İ�� ѭ��ͳ�ƶ�Ӧ���Ű������ͳ�������
      FOR SCHEDULE_CODE_ROW IN (SELECT DISTINCT SCHEDULE_CODE, SCHEDULE_DT
                                  FROM (SELECT SCHEDULE_CODE, SCHEDULE_DT
                                          FROM TM_SPMS2CDP_BY_OPERATION_INFO T
                                         WHERE T.DEPT_CODE =
                                               CDP_SHEDULE_ROW.ZONE_CODE
                                           AND T.YM =
                                               TO_CHAR(CDP_SHEDULE_ROW.TRANS_BATCH_DATE,
                                                       'YYYY-MM')
                                           AND ((T.START1_TIME >=
                                               CDP_SHEDULE_ROW.BEGIN_TM AND
                                               T.START1_TIME <
                                               CDP_SHEDULE_ROW.END_TM) OR
                                               (T.END1_TIME >
                                               CDP_SHEDULE_ROW.BEGIN_TM AND
                                               T.END1_TIME <=
                                               CDP_SHEDULE_ROW.END_TM) OR
                                               (T.START2_TIME >=
                                               CDP_SHEDULE_ROW.BEGIN_TM AND
                                               T.START2_TIME <
                                               CDP_SHEDULE_ROW.END_TM) OR
                                               (T.END2_TIME >
                                               CDP_SHEDULE_ROW.BEGIN_TM AND
                                               T.END2_TIME <=
                                               CDP_SHEDULE_ROW.END_TM) OR
                                               (T.START3_TIME >
                                               CDP_SHEDULE_ROW.BEGIN_TM AND
                                               START3_TIME <
                                               CDP_SHEDULE_ROW.END_TM) OR
                                               (T.END3_TIME >
                                               CDP_SHEDULE_ROW.BEGIN_TM AND
                                               END3_TIME <
                                               CDP_SHEDULE_ROW.END_TM))
                                        UNION ALL
                                        SELECT T.SCHEDULE_CODE, t.schedule_dt
                                          FROM TM_SPMS2CDP_BY_OPERATION_INFO T
                                         WHERE T.DEPT_CODE =
                                               CDP_SHEDULE_ROW.ZONE_CODE
                                           AND T.YM =
                                               TO_CHAR(CDP_SHEDULE_ROW.TRANS_BATCH_DATE,
                                                       'YYYY-MM')
                                           AND ((T.START1_TIME <
                                               CDP_SHEDULE_ROW.BEGIN_TM AND
                                               T.END1_TIME >=
                                               CDP_SHEDULE_ROW.END_TM) OR
                                               (T.START2_TIME <
                                               CDP_SHEDULE_ROW.BEGIN_TM AND
                                               T.END3_TIME >=
                                               CDP_SHEDULE_ROW.END_TM) OR
                                               (T.START3_TIME <
                                               CDP_SHEDULE_ROW.BEGIN_TM AND
                                               T.END3_TIME >=
                                               CDP_SHEDULE_ROW.END_TM))
                                        UNION ALL
                                        SELECT T.SCHEDULE_CODE, t.schedule_dt
                                          FROM TM_SPMS2CDP_BY_OPERATION_INFO T
                                         WHERE T.DEPT_CODE =
                                               CDP_SHEDULE_ROW.ZONE_CODE
                                           AND T.YM =
                                               TO_CHAR(CDP_SHEDULE_ROW.TRANS_BATCH_DATE,
                                                       'YYYY-MM')
                                           AND CASE
                                                 WHEN T.START1_TIME >
                                                      T.END1_TIME THEN
                                                  START1_TIME
                                                 WHEN T.START2_TIME >
                                                      T.END2_TIME THEN
                                                  START2_TIME
                                                 WHEN T.START3_TIME >
                                                      T.END3_TIME THEN
                                                  START3_TIME
                                                 ELSE
                                                  CDP_SHEDULE_ROW.END_TM
                                               END < CDP_SHEDULE_ROW.END_TM)) LOOP
      
        INSERT INTO TT_SPMS2CDP_NUM
          (DEPT_CODE, EMP_CODE, PERSG)
          SELECT CDP_SHEDULE_ROW.ZONE_CODE, E.EMP_CODE, E.PERSG
            FROM TT_PB_SHEDULE_BY_DAY TB, TM_OSS_EMPLOYEE E
           WHERE E.DEPT_ID = TB.DEPT_ID
             AND E.EMP_CODE = TB.EMP_CODE
             AND E.EMP_POST_TYPE = '1'
             AND E.PERSG = 'A'
             AND (E.DIMISSION_DT IS NULL OR
                 E.DIMISSION_DT > CDP_SHEDULE_ROW.TRANS_BATCH_DATE)
             AND TB.DEPT_ID = V_DEPT_ID
             AND TB.SHEDULE_CODE = SCHEDULE_CODE_ROW.SCHEDULE_CODE
             AND TB.SHEDULE_DT = SCHEDULE_CODE_ROW.SCHEDULE_DT;
      
        INSERT INTO TT_SPMS2CDP_NUM
          (DEPT_CODE, EMP_CODE, PERSG)
          SELECT CDP_SHEDULE_ROW.ZONE_CODE, E.EMP_CODE, E.PERSG
            FROM TT_PB_SHEDULE_BY_DAY TB, TM_OSS_EMPLOYEE E
           WHERE E.DEPT_ID = TB.DEPT_ID
             AND E.EMP_CODE = TB.EMP_CODE
             AND E.EMP_POST_TYPE = '1'
             AND E.PERSG = 'C'
             AND (E.DIMISSION_DT IS NULL OR
                 E.DIMISSION_DT > CDP_SHEDULE_ROW.TRANS_BATCH_DATE)
             AND TB.DEPT_ID = V_DEPT_ID
             AND TB.SHEDULE_CODE = SCHEDULE_CODE_ROW.SCHEDULE_CODE
             AND TB.SHEDULE_DT = SCHEDULE_CODE_ROW.SCHEDULE_DT;
      
        INSERT INTO TT_SPMS2CDP_NUM
          (DEPT_CODE, EMP_CODE, PERSG)
          SELECT CDP_SHEDULE_ROW.ZONE_CODE, E.EMP_CODE, e.Work_Type
            FROM TT_PB_SHEDULE_BY_DAY TB, TM_OSS_EMPLOYEE E
           WHERE E.DEPT_ID = TB.DEPT_ID
             AND E.EMP_CODE = TB.EMP_CODE
             AND E.EMP_POST_TYPE = '1'
             AND E.WORK_TYPE = '6'
             AND (E.DIMISSION_DT IS NULL OR
                 E.DIMISSION_DT > CDP_SHEDULE_ROW.TRANS_BATCH_DATE)
             AND TB.DEPT_ID = V_DEPT_ID
             AND TB.SHEDULE_CODE = SCHEDULE_CODE_ROW.SCHEDULE_CODE
             AND TB.SHEDULE_DT = SCHEDULE_CODE_ROW.SCHEDULE_DT;
      
        -- ͳ��ȫ���Ƶ�������Ű�����
        SELECT NVL(COUNT(*), 0)
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
           AND TB.SHEDULE_DT = SCHEDULE_CODE_ROW.SCHEDULE_DT;
      
        SELECT NVL(COUNT(*), 0)
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
           AND TB.SHEDULE_DT = SCHEDULE_CODE_ROW.SCHEDULE_DT;
      
        -- �����������Ű�����
        SELECT NVL(COUNT(*), 0)
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
           AND TB.SHEDULE_DT = SCHEDULE_CODE_ROW.SCHEDULE_DT;
      
        V_ATTENDANCE_LENGTH := 0;
      
        -- ͳ�ƶ�Ӧ������Чʱ�� ������ʱ��
        SELECT NVL(SUM(NVL(COUNT_ATTENDANCE_LENGTH(PB.START1_TIME,
                                                   PB.END1_TIME,
                                                   CDP_SHEDULE_ROW.BEGIN_TM,
                                                   CDP_SHEDULE_ROW.END_TM),
                           0) + NVL(COUNT_ATTENDANCE_LENGTH(PB.START2_TIME,
                                                            PB.END2_TIME,
                                                            CDP_SHEDULE_ROW.BEGIN_TM,
                                                            CDP_SHEDULE_ROW.END_TM),
                                    0) + NVL(COUNT_ATTENDANCE_LENGTH(PB.START3_TIME,
                                                                     PB.END3_TIME,
                                                                     CDP_SHEDULE_ROW.BEGIN_TM,
                                                                     CDP_SHEDULE_ROW.END_TM),
                                             0)),
                   0)
          INTO V_ATTENDANCE_LENGTH
          FROM TM_SPMS2CDP_BY_OPERATION_INFO PB
         WHERE PB.DEPT_CODE = CDP_SHEDULE_ROW.ZONE_CODE
           AND PB.SCHEDULE_CODE = SCHEDULE_CODE_ROW.SCHEDULE_CODE
           AND PB.SCHEDULE_DT = SCHEDULE_CODE_ROW.SCHEDULE_DT
           AND PB.YM = TO_CHAR(CDP_SHEDULE_ROW.TRANS_BATCH_DATE, 'YYYY-MM');
      
        -- ȫ���Ƴ���ʱ��
        FULL_TIME_ATTENDANCE_COUNT := FULL_TIME_ATTENDANCE_COUNT +
                                      NVL(V_ATTENDANCE_LENGTH *
                                          FULL_TIME_EMP_NUM,
                                          0);
        -- ��ȫ���Ƴ���ʱ��
        NOTFULL_TIME_ATTENDANCE_COUNT := NOTFULL_TIME_ATTENDANCE_COUNT +
                                         NVL(V_ATTENDANCE_LENGTH *
                                             NOTFULL_TIME_EMP_NUM,
                                             0);
        -- �������ʱ��
        OUT_EMP_ATTENDANCE_COUNT := OUT_EMP_ATTENDANCE_COUNT +
                                    NVL(V_ATTENDANCE_LENGTH * OUT_EMP_NUM,
                                        0);
      
      END LOOP;
    
      SELECT COUNT(DISTINCT T.EMP_CODE)
        INTO FULL_TIME_EMP_NUM_COUNT
        FROM TT_SPMS2CDP_NUM T
       WHERE T.PERSG = 'A'
         AND T.DEPT_CODE = CDP_SHEDULE_ROW.ZONE_CODE;
    
      SELECT COUNT(DISTINCT T.EMP_CODE)
        INTO NOTFULL_TIME_EMP_NUM_COUNT
        FROM TT_SPMS2CDP_NUM T
       WHERE T.PERSG = 'C'
         AND T.DEPT_CODE = CDP_SHEDULE_ROW.ZONE_CODE;
    
      SELECT COUNT(DISTINCT T.EMP_CODE)
        INTO OUT_EMP_NUM_COUNT
        FROM TT_SPMS2CDP_NUM T
       WHERE T.PERSG = '6'
         AND T.DEPT_CODE = CDP_SHEDULE_ROW.ZONE_CODE;
    
      -- �����ݲ���ӿڱ�
      INSERT INTO TI_SPMS2CDP_OPERATION_WARNING
        (SCHEDULE_DT,
         AREA_CODE,
         DEPT_CODE,
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
    END IF;
  END LOOP;
  COMMIT;

  --3.��ʼ��¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SPMS2CDP_BY_OPERATION_TWO',
                               SYSDATE,
                               NULL,
                               NULL,
                               'END',
                               0,
                               L_CALL_NO);

EXCEPTION
  WHEN OTHERS THEN
    --�ع�����
    ROLLBACK;
    PKG_OSS_COMM.STP_RUNNING_LOG('',
                                 'SPMS2CDP_BY_OPERATION_TWO',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
  
END SPMS2CDP_BY_OPERATION_TWO;
