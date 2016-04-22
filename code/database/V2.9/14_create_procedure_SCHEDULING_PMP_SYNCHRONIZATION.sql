CREATE OR REPLACE PROCEDURE SCHEDULING_PMP_SYNCHRONIZATION AS
/**
  *ÿ��23:00��00:40��һ���Ű�������SAP�ӿڱ�
**/
  KEYVALUE VARCHAR2(900);
  --1.����ִ�����
  L_CALL_NO NUMBER;
BEGIN

  --2.����ִ�����
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
  --3.��ʼ��¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SCHEDULING_PMP_SYNCHRONIZATION',
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
   WHERE C.KEY_NAME = 'CD_SCH2SPMS_CLASS';
  --�жϽӿ��Ƿ��
  IF KEYVALUE = '1' THEN
    BEGIN
    ----����SAP����--begin
    --1.������ʱ����
    INSERT INTO TT_PMP_SYNCHRONOUS_TMP
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
             EMPLOYEE_CODE,
             DAY_OF_MONTH AS BEGIN_DATE,
             DAY_OF_MONTH AS END_DATE,
             decode(BEGIN_TIME,null,null,lpad(BEGIN_TIME,4,'0') ||'00') BEGIN_TIME,
             decode(END_TIME,null,null,lpad(END_TIME,4,'0') ||'00') END_TIME,
             CROSS_DAY_TYPE AS TMR_DAY_FLAG,
             DECODE(BEGIN_TIME, NULL, 'OFF', '') AS OFF_DUTY_FLAG,
             '2' AS CLASS_SYSTEM,
             SYSDATE,
             '',
             0 AS STATE_FLG,
             '2'
        FROM TT_SCHEDULE_DAILY T, TM_OSS_EMPLOYEE E,TM_DEPARTMENT D
       WHERE (T.SYNCHRO_STATUS != 1 OR T.SYNCHRO_STATUS IS NULL)
         AND T.EMP_POST_TYPE = '2'
         AND T.EMPLOYEE_CODE = E.EMP_CODE
            --����ת��������
         AND T.DEPARTMENT_CODE = D.DEPT_CODE
         AND E.DEPT_ID = D.DEPT_ID
         AND (E.LAST_ZNO is null or T.DAY_OF_MONTH >= TO_CHAR(E.DATE_FROM, 'YYYYMMDD'))
            --������ְ��Ա�Ű�����
         AND (E.DIMISSION_DT is null or TO_CHAR(E.DIMISSION_DT, 'YYYYMMDD') > t.DAY_OF_MONTH)
            --����ת��ǰ������
         AND (E.TRANSFER_DATE is null or T.DAY_OF_MONTH >= TO_CHAR(E.TRANSFER_DATE, 'YYYYMMDD'))
              --������ְǰ������
         AND  T.DAY_OF_MONTH >= TO_CHAR(E.Sf_Date, 'YYYYMMDD')

             --������Ա���ͣ�������������˾�Ӫ�а���
         AND E.WORK_TYPE = 6
            --���˺���͸۰�̨
         AND T.DEPARTMENT_CODE NOT IN
             (SELECT DEPT_CODE
                FROM TM_DEPARTMENT
               START WITH DEPT_CODE IN ('CN06', 'CN07')
              CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE)
        AND T.DAY_OF_MONTH >= '20150101'
            -- ȡ40�����ڵ�����
         AND T.DAY_OF_MONTH >= TO_CHAR(TRUNC(SYSDATE - 40), 'YYYYMMDD')
         AND T.DAY_OF_MONTH <= TO_CHAR(SYSDATE, 'YYYYMMDD')
         AND E.EMP_POST_TYPE = '2'
         AND E.DATA_SOURCE = '3';
    COMMIT;

    --ɾ�����ڵ�����
    DELETE FROM TT_PMP_SYNCHRONOUS S
     WHERE EXISTS
     (SELECT 1
              FROM (SELECT EMP_CODE, BEGIN_DATE
                      FROM TT_PMP_SYNCHRONOUS_TMP
                     GROUP BY EMP_CODE, BEGIN_DATE) TM
             WHERE TM.EMP_CODE = S.EMP_CODE
               AND TM.BEGIN_DATE = S.BEGIN_DATE��
     AND S.tmr_day_flag is null;

   /* AND S.EMP_POST_TYPE = '2'*/

    --�ǿ����������
    INSERT INTO TT_PMP_SYNCHRONOUS
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
      SELECT SEQ_TT_PMP_SYNCHRONOUS.NEXTVAL,
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
        FROM TT_PMP_SYNCHRONOUS_TMP S;

    UPDATE TT_SCHEDULE_DAILY D
       SET D.SYNCHRO_STATUS = 1
     WHERE D.ID IN
           (SELECT ID FROM TT_PMP_SYNCHRONOUS_TMP E)
       AND EMP_POST_TYPE = '2';
    COMMIT;
    EXECUTE IMMEDIATE 'TRUNCATE TABLE TT_PMP_SYNCHRONOUS_TMP';
    ----����SAP����--end
    END;
  END IF;
  --4.������¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SCHEDULING_PMP_SYNCHRONIZATION',
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
                                 'SCHEDULING_PMP_SYNCHRONIZATION',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
END SCHEDULING_PMP_SYNCHRONIZATION;
/