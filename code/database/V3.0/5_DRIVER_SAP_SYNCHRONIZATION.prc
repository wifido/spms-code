CREATE OR REPLACE PROCEDURE DRIVER_SAP_SYNCHRONIZATION AS

  /**
    *ÿ��22:30���ֹ��Ű�������SAP�ӿڱ�
  **/
  KEYVALUE VARCHAR2(900);
  --1.����ִ�����
  L_CALL_NO NUMBER;
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
    SELECT T.ID,
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
      FROM TT_DRIVER_SCHEDULING T, TM_OSS_EMPLOYEE E, TM_DEPARTMENT D
     WHERE T.DEPARTMENT_CODE = D.DEPT_CODE
       AND D.DEPT_ID = E.DEPT_ID
       AND T.EMPLOYEE_CODE = E.EMP_CODE
       AND (T.SYNC_STATE != 1 OR T.SYNC_STATE IS NULL)
       AND T.DAY_OF_MONTH >= '20150901'
          -- ȡ40�����ڵ�����
       AND T.DAY_OF_MONTH >= TO_CHAR(TRUNC(SYSDATE - 40), 'YYYYMMDD')
       AND T.DAY_OF_MONTH <= TO_CHAR(SYSDATE, 'YYYYMMDD')
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
       AND T.SCHEDULING_TYPE = '1'
       AND E.DATA_SOURCE = '2'
       AND E.EMP_POST_TYPE='5'
       AND T.CONFIGURE_CODE = '��'
    UNION ALL
    SELECT T.ID,
           T.EMPLOYEE_CODE,
           T.DAY_OF_MONTH,
           T.DAY_OF_MONTH,
           C.START_TIME ||'00' ,
           C.END_TIME ||'00' ,
           '',
           '',
           '2',
           SYSDATE,
           '',
           0,
           5
      FROM V_PUSH_DRIVER_SCHEDULING_DATA C,
           TT_DRIVER_SCHEDULING          T,
           TM_OSS_EMPLOYEE               E,
           TM_DEPARTMENT                 D
     WHERE T.CONFIGURE_CODE = C.CONFIGURE_CODE(+)
       AND T.YEAR_MONTH = C.MONTH(+)
       AND T.DEPARTMENT_CODE = D.DEPT_CODE
       AND D.DEPT_ID = E.DEPT_ID
       AND T.EMPLOYEE_CODE = E.EMP_CODE

       AND (T.SYNC_STATE != 1 OR T.SYNC_STATE IS NULL)
       AND T.DAY_OF_MONTH >= '20150901'
          -- ȡ40�����ڵ�����
       AND T.DAY_OF_MONTH >= TO_CHAR(TRUNC(SYSDATE - 40), 'YYYYMMDD')
       AND T.DAY_OF_MONTH <= TO_CHAR(SYSDATE, 'YYYYMMDD')

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
       AND T.SCHEDULING_TYPE = '1'
       AND E.DATA_SOURCE = '2'
       AND E.EMP_POST_TYPE='5'
       AND T.CONFIGURE_CODE <> '��';
       COMMIT;


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
       SET D.SYNC_STATE = 1
     WHERE D.ID IN
           (SELECT ID FROM TT_SAP_SYNCHRONOUS_TMP4 E);
    COMMIT;
    EXECUTE IMMEDIATE 'TRUNCATE TABLE TT_SAP_SYNCHRONOUS_TMP4';
    ----����SAP����--END
     END;
  END IF;
CHANGE_SAP_SYNCHRONIZATION;

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
