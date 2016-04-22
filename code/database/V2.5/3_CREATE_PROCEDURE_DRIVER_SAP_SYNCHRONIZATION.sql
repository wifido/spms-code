CREATE OR REPLACE PROCEDURE DRIVER_SAP_SYNCHRONIZATION AS

  /**
    *ÿ��22:30���ֹ��Ű�������SAP�ӿڱ�
  **/
  KEYVALUE VARCHAR2(900);
  --1.����ִ�����
  L_CALL_NO NUMBER;
  L_TMR_DAY_FLAG NUMBER;
  L_LAST_EMPCODE VARCHAR2(38);
  L_LAST_START_TIME NUMBER(10);
  L_LAST_DAY VARCHAR2(20);
BEGIN

  --2.����ִ�����
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
  --3.��ʼ��¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'DRIVER_SAP_SYNCHRONIZATION',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);
  --��ѯ�ӿ��Ƿ��
  SELECT C.KEY_VALUE
    INTO KEYVALUE
    FROM TL_SPMS_SYS_CONFIG C
   WHERE C.KEY_NAME = 'CD_WAREHOUSE2SAP_CLASS';
  --�жϽӿ��Ƿ��
  IF KEYVALUE = '1' THEN

    BEGIN
    ----����SAP����--BEGIN
    --1.������ʱ����
    INSERT INTO TT_SAP_SYNCHRONOUS_TMP4
      (ID,
       EMP_CODE,
       BEGIN_DATE,
       END_DATE,
       BEGIN_TM,
       END_TM,
       TMR_DAY_FLAG,
       OFF_DUTY_FLAG,
       CLASS_SYSTEM,
       CREATE_TM,
       NODE_KEY,
       STATE_FLG,
       EMP_POST_TYPE)
      SELECT T.SCHEDULING_ID,
             T.EMPLOYEE_CODE,
             T.DAY_OF_MONTH,
             T.DAY_OF_MONTH,
             '',
             '',
             '',
             'OFF',
             '2',
             SYSDATE,
             '',
             0,
             5
        FROM V_SCHEDULING_FOR_DRIVER_SAP T,
             TM_OSS_EMPLOYEE             E,
             TM_DEPARTMENT               D
       WHERE T.DEPARTMENT_CODE = D.DEPT_CODE
         AND D.DEPT_ID = E.DEPT_ID
         AND T.EMPLOYEE_CODE = E.EMP_CODE
         AND (T.SYNC_STATE != 1 OR T.SYNC_STATE IS NULL)
         AND (E.LAST_ZNO IS NULL OR
             T.DAY_OF_MONTH >= TO_CHAR(E.DATE_FROM, 'YYYYMMDD'))
            --������ְ��Ա�Ű�����
         AND (E.DIMISSION_DT IS NULL OR
             TO_CHAR(E.DIMISSION_DT, 'YYYYMMDD') > T.DAY_OF_MONTH)
            --����ת��ǰ������
         AND (E.TRANSFER_DATE IS NULL OR
             T.DAY_OF_MONTH >= TO_CHAR(E.TRANSFER_DATE, 'YYYYMMDD'))
            --������ְǰ������
         AND T.DAY_OF_MONTH >= TO_CHAR(E.SF_DATE, 'YYYYMMDD')

         AND T.DAY_OF_MONTH >= '20150101'
            -- ȡ40�����ڵ�����
         AND T.DAY_OF_MONTH >= TO_CHAR(TRUNC(SYSDATE - 40), 'YYYYMMDD')
         AND T.DAY_OF_MONTH <= TO_CHAR(SYSDATE, 'YYYYMMDD')
         AND E.DATA_SOURCE = '2'
         AND T.CONFIGURE_CODE ='��';


        FOR SAP_ROW IN ( SELECT T.SCHEDULING_ID,
           T.EMPLOYEE_CODE,
           T.DAY_OF_MONTH,
           T.START_TIME,
           T.END_TIME,
           T.SORT
      FROM V_SCHEDULING_FOR_DRIVER_SAP T, TM_OSS_EMPLOYEE E, TM_DEPARTMENT D
     WHERE T.DEPARTMENT_CODE = D.DEPT_CODE
       AND D.DEPT_ID = E.DEPT_ID
       AND T.EMPLOYEE_CODE = E.EMP_CODE
       AND (T.SYNC_STATE != 1 OR T.SYNC_STATE IS NULL)
       AND (E.LAST_ZNO IS NULL OR
           T.DAY_OF_MONTH >= TO_CHAR(E.DATE_FROM, 'YYYYMMDD'))
          --������ְ��Ա�Ű�����
       AND (E.DIMISSION_DT IS NULL OR
           TO_CHAR(E.DIMISSION_DT, 'YYYYMMDD') > T.DAY_OF_MONTH)
          --����ת��ǰ������
       AND (E.TRANSFER_DATE IS NULL OR
           T.DAY_OF_MONTH >= TO_CHAR(E.TRANSFER_DATE, 'YYYYMMDD'))
          --������ְǰ������
       AND T.DAY_OF_MONTH >= TO_CHAR(E.SF_DATE, 'YYYYMMDD')

       AND T.DAY_OF_MONTH >= '20150101'
          -- ȡ40�����ڵ�����
       AND T.DAY_OF_MONTH >= TO_CHAR(TRUNC(SYSDATE - 40), 'YYYYMMDD')
       AND T.DAY_OF_MONTH <= TO_CHAR(SYSDATE, 'YYYYMMDD')
       AND E.DATA_SOURCE = '2'
       AND T.CONFIGURE_CODE <> '��'
       ORDER BY T.DAY_OF_MONTH,t.EMPLOYEE_CODE,t.SORT
       )     LOOP
  IF SAP_ROW.EMPLOYEE_CODE = L_LAST_EMPCODE AND
     L_LAST_DAY = SAP_ROW.DAY_OF_MONTH and
     ( L_TMR_DAY_FLAG = 1 OR L_LAST_START_TIME >= SAP_ROW.START_TIME) THEN
      INSERT INTO TT_SAP_SYNCHRONOUS_TMP4
        (ID,
         EMP_CODE,
         BEGIN_DATE,
         END_DATE,
         BEGIN_TM,
         END_TM,
         TMR_DAY_FLAG,
         OFF_DUTY_FLAG,
         CLASS_SYSTEM,
         CREATE_TM,
         NODE_KEY,
         STATE_FLG,
         EMP_POST_TYPE)
      VALUES
        (SAP_ROW.SCHEDULING_ID,
         SAP_ROW.EMPLOYEE_CODE,
         TO_CHAR(TO_DATE(SAP_ROW.DAY_OF_MONTH, 'YYYYMMDD') + 1, 'YYYYMMDD'),
         TO_CHAR(TO_DATE(SAP_ROW.DAY_OF_MONTH, 'YYYYMMDD') + 1, 'YYYYMMDD'),
         SAP_ROW.START_TIME,
         SAP_ROW.END_TIME,
         'X',
         '',
         2,
         SYSDATE,
         '',
         0,
         5);
     else
    INSERT INTO TT_SAP_SYNCHRONOUS_TMP4
      (ID,
       EMP_CODE,
       BEGIN_DATE,
       END_DATE,
       BEGIN_TM,
       END_TM,
       TMR_DAY_FLAG,
       OFF_DUTY_FLAG,
       CLASS_SYSTEM,
       CREATE_TM,
       NODE_KEY,
       STATE_FLG,
       EMP_POST_TYPE)
    VALUES
      (SAP_ROW.SCHEDULING_ID,
       SAP_ROW.EMPLOYEE_CODE,
       SAP_ROW.DAY_OF_MONTH,
       SAP_ROW.DAY_OF_MONTH,
       SAP_ROW.START_TIME,
       SAP_ROW.END_TIME,
       '',
       '',
       2,
       SYSDATE,
       '',
       0,
       5);
    L_LAST_DAY        := SAP_ROW.DAY_OF_MONTH;
    L_LAST_START_TIME := SAP_ROW.START_TIME;
    L_LAST_EMPCODE    := SAP_ROW.EMPLOYEE_CODE;
    IF (SAP_ROW.START_TIME >= SAP_ROW.END_TIME) THEN
      L_TMR_DAY_FLAG := 1;
    ELSE
      L_TMR_DAY_FLAG := 0;
    END IF;
 end if;
END LOOP;

    --ɾ�����ڵ�����
    DELETE FROM TT_SAP_SYNCHRONOUS S
     WHERE EXISTS
     (SELECT 1
              FROM (SELECT EMP_CODE, BEGIN_DATE
                      FROM TT_SAP_SYNCHRONOUS_TMP4
                     GROUP BY EMP_CODE, BEGIN_DATE) TM
             WHERE TM.EMP_CODE = S.EMP_CODE
               AND TM.BEGIN_DATE = S.BEGIN_DATE��
            AND S.TMR_DAY_FLAG IS NULL;

    --�ǿ����������
    INSERT INTO TT_SAP_SYNCHRONOUS
      (ID,
       EMP_CODE,
       BEGIN_DATE,
       END_DATE,
       BEGIN_TM,
       END_TM,
       TMR_DAY_FLAG,
       OFF_DUTY_FLAG,
       CLASS_SYSTEM,
       CREATE_TM,
       NODE_KEY,
       STATE_FLG,
       SCHEDULE_DAILY_ID,
       EMP_POST_TYPE)
      SELECT SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL,
             EMP_CODE,
             BEGIN_DATE,
             END_DATE,
             BEGIN_TM,
             END_TM,
             TMR_DAY_FLAG,
             OFF_DUTY_FLAG,
             CLASS_SYSTEM,
             CREATE_TM,
             NODE_KEY,
             STATE_FLG,
             S.ID,
             S.EMP_POST_TYPE
        FROM TT_SAP_SYNCHRONOUS_TMP4 S;
        COMMIT;
    --�޸�״̬
    UPDATE TT_DRIVER_SCHEDULING D
       SET D.Sync_State = 1
     WHERE D.ID IN
           (SELECT ID FROM TT_SAP_SYNCHRONOUS_TMP4 E);
    COMMIT;
    EXECUTE IMMEDIATE 'TRUNCATE TABLE TT_SAP_SYNCHRONOUS_TMP4';
    ----����SAP����--END
    END;
  END IF;
  --4.������¼��־

  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'DRIVER_SAP_SYNCHRONIZATION',
                               SYSDATE,
                               NULL,
                               NULL,
                               'END',
                               0,
                               L_CALL_NO);
  --5.�쳣��¼��־
EXCEPTION
  WHEN OTHERS THEN
    --�ع�����
    ROLLBACK;
    PKG_OSS_COMM.STP_RUNNING_LOG('',
                                 'DRIVER_SAP_SYNCHRONIZATION',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);


END DRIVER_SAP_SYNCHRONIZATION;
/