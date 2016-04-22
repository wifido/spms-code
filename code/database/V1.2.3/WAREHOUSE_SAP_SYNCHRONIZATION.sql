CREATE OR REPLACE PROCEDURE WAREHOUSE_SAP_SYNCHRONIZATION IS
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
                               'WAREHOUSE_SAP_SYNCHRONIZATION',
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
    ----����SAP����--begin
    --1.������ʱ����
    INSERT INTO TT_SAP_SYNCHRONOUS_TMP3
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
      SELECT D.ID,
             D.EMPLOYEE_CODE,
             D.DAY_OF_MONTH BEGIN_DATE,
             D.DAY_OF_MONTH END_DATE,
             DECODE(B.START1_TIME,
                    NULL,
                    NULL,
                    SUBSTR(REPLACE(LPAD(B.START1_TIME, 5, '0'), ':', '') || '00',
                           1,
                           6)) BEGIN_TM1,
             DECODE(B.END1_TIME,
                    NULL,
                    NULL,
                    SUBSTR(REPLACE(LPAD(B.END1_TIME, 5, '0'), ':', '') || '00',
                           1,
                           6)) END_TM1,
             CASE
               WHEN B.IS_CROSS_DAY = '1' AND
                    SUBSTR(REPLACE(LPAD(B.START1_TIME, 5, '0'), ':', '') || '00',
                           1,
                           6) <
                    SUBSTR(REPLACE(LPAD(B.END1_TIME, 5, '0'), ':', '') || '00',
                           1,
                           6) THEN
                'X'
               ELSE
                ''
             END TMR_DAY_FLAG,
             '' OFF_DUTY_FLAG,
             '2' CLASS_SYSTEM,
             SYSDATE CREATE_TM,
             '' NODE_KEY,
             0 STATE_FLG,
             '3'
        FROM TT_SCHEDULE_DAILY D,
             TM_DEPARTMENT T,
             TM_PB_SCHEDULE_BASE_INFO B,
             TM_OSS_EMPLOYEE E
      --������Ա��
       WHERE D.EMPLOYEE_CODE = E.EMP_CODE
            --������Ա���ͣ�������������˾�Ӫ�а���
         AND E.WORK_TYPE NOT IN (6, 8, 9)
            --��������
         AND D.DEPARTMENT_CODE = T.DEPT_CODE
         AND D.SCHEDULING_CODE = B.SCHEDULE_CODE
            -- ����ת������
         AND T.DEPT_ID = E.DEPT_ID
        -- AND (E.LAST_ZNO is null or D.DAY_OF_MONTH >= TO_CHAR(E.DATE_FROM, 'YYYYMMDD'))
         AND B.DEPT_ID = E.DEPT_ID
            --���˲ֹ�
         AND D.EMP_POST_TYPE = '3'
         AND D.DAY_OF_MONTH >= '20141201'
            --ȡ����ǰ������
         AND D.DAY_OF_MONTH <= TO_CHAR(SYSDATE, 'YYYYMMDD')
         AND (E.DIMISSION_DT is null or TO_CHAR(E.DIMISSION_DT, 'YYYYMMDD') > D.DAY_OF_MONTH)
           --����ת��ǰ������
       --  AND (E.TRANSFER_DATE is null or D.DAY_OF_MONTH >= TO_CHAR(E.TRANSFER_DATE, 'YYYYMMDD'))
            --���˺���͸۰�̨
         AND D.DEPARTMENT_CODE NOT IN
             (SELECT DEPT_CODE
                FROM TM_DEPARTMENT
               START WITH DEPT_CODE IN ('CN06', 'CN07')
              CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE)
         AND D.SCHEDULING_CODE IS NOT NULL
         AND D.SCHEDULING_CODE <> '��'
         AND (D.SYNCHRO_STATUS != 1 OR D.SYNCHRO_STATUS IS NULL)
            --ȡ�������
         AND B.CLASS_TYPE = '2'
         AND E.EMP_POST_TYPE = '3'
      UNION ALL
      SELECT D.ID,
             D.EMPLOYEE_CODE,
             D.DAY_OF_MONTH BEGIN_DATE,
             D.DAY_OF_MONTH END_DATE,
             NULL BEGIN_TM1,
             NULL END_TM1,
             '' TMR_DAY_FLAG,
             'OFF' OFF_DUTY_FLAG,
             '2' CLASS_SYSTEM,
             SYSDATE CREATE_TM,
             '' NODE_KEY,
             0 STATE_FLG,
             '3'
        FROM TT_SCHEDULE_DAILY D,
             TM_OSS_EMPLOYEE E,
             TM_DEPARTMENT T
              --������Ա��
       WHERE D.EMPLOYEE_CODE = E.EMP_CODE
             --����ת������
          AND D.DEPARTMENT_CODE = T.DEPT_CODE
          AND E.DEPT_ID = T.DEPT_ID
        --  AND (E.LAST_ZNO is null or D.DAY_OF_MONTH >= TO_CHAR(E.DATE_FROM, 'YYYYMMDD'))
       --  AND (E.TRANSFER_DATE is null or D.DAY_OF_MONTH >= TO_CHAR(E.TRANSFER_DATE, 'YYYYMMDD'))
            --������Ա���ͣ�������������˾�Ӫ�а���
         AND E.WORK_TYPE NOT IN (6, 8, 9)
            --���˲ֹ�
         AND D.EMP_POST_TYPE = '3'
         AND D.DAY_OF_MONTH >= '20141201'
            --ȡ����ǰ������
         AND D.DAY_OF_MONTH <= TO_CHAR(SYSDATE, 'YYYYMMDD')
            --���˺���͸۰�̨
         AND D.DEPARTMENT_CODE NOT IN
             (SELECT DEPT_CODE
                FROM TM_DEPARTMENT
               START WITH DEPT_CODE IN ('CN06', 'CN07')
              CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE)
         AND (E.DIMISSION_DT is null or TO_CHAR(E.DIMISSION_DT, 'YYYYMMDD') > D.DAY_OF_MONTH)
         AND D.SCHEDULING_CODE = '��'
         AND (D.SYNCHRO_STATUS != 1 OR D.SYNCHRO_STATUS IS NULL)
         AND E.EMP_POST_TYPE = '3';
    COMMIT;

    --ɾ�����ڵ�����
    DELETE FROM TT_SAP_SYNCHRONOUS S
     WHERE EXISTS
     (SELECT 1
              FROM (SELECT EMP_CODE, BEGIN_DATE
                      FROM TT_SAP_SYNCHRONOUS_TMP3
                     GROUP BY EMP_CODE, BEGIN_DATE) TM
             WHERE TM.EMP_CODE = S.EMP_CODE
               AND TM.BEGIN_DATE = S.BEGIN_DATE��
       AND S.EMP_POST_TYPE = '3';

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
        FROM TT_SAP_SYNCHRONOUS_TMP3 S
       WHERE S.TMR_DAY_FLAG IS NULL;
  --���촦��,�������
  FOR SAP_ROW IN (SELECT * FROM TT_SAP_SYNCHRONOUS_TMP3 S
   WHERE S.TMR_DAY_FLAG = 'X') LOOP

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
        VALUES
          (SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL,
           SAP_ROW.EMP_CODE��
           SAP_ROW.BEGIN_DATE,
           SAP_ROW.BEGIN_DATE,
           SAP_ROW.BEGIN_TM,
           SAP_ROW.BEGIN_TM,
           '',
           SAP_ROW.OFF_DUTY_FLAG,
           SAP_ROW.CLASS_SYSTEM,
           SAP_ROW.CREATE_TM,
           SAP_ROW.NODE_KEY,
           SAP_ROW.STATE_FLG,
           SAP_ROW.ID,
           SAP_ROW.EMP_POST_TYPE);

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
        VALUES
          (SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL,
           SAP_ROW.EMP_CODE,
           TO_CHAR(TO_DATE(SAP_ROW.BEGIN_DATE, 'YYYYMMDD') + 1,
                   'YYYYMMDD'),
           TO_CHAR(TO_DATE(SAP_ROW.BEGIN_DATE, 'YYYYMMDD') + 1,
                   'YYYYMMDD'),
           SAP_ROW.BEGIN_TM,
           SAP_ROW.END_TM,
           SAP_ROW.TMR_DAY_FLAG,
           SAP_ROW.OFF_DUTY_FLAG,
           SAP_ROW.CLASS_SYSTEM,
           SAP_ROW.CREATE_TM,
           SAP_ROW.NODE_KEY,
           SAP_ROW.STATE_FLG,
           SAP_ROW.ID,
           SAP_ROW.EMP_POST_TYPE);
      END LOOP;
    --�޸�״̬
    UPDATE TT_SCHEDULE_DAILY D
       SET D.SYNCHRO_STATUS = 1
     WHERE D.ID IN
           (SELECT ID FROM TT_SAP_SYNCHRONOUS_TMP3 E)
       AND EMP_POST_TYPE = '3';
    COMMIT;
    EXECUTE IMMEDIATE 'TRUNCATE TABLE TT_SAP_SYNCHRONOUS_TMP3';
    ----����SAP����--end
    END;
  END IF;
  --4.������¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'WAREHOUSE_SAP_SYNCHRONIZATION',
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
                                 'WAREHOUSE_SAP_SYNCHRONIZATION',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
END WAREHOUSE_SAP_SYNCHRONIZATION;
