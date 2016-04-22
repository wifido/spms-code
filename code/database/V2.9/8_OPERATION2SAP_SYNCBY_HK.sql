CREATE OR REPLACE PROCEDURE OPERATION2SAP_SYNCBY_HK AS
  /**
   *ÿ��22:00�������Ű�������SAP�ӿڱ�
  **/
  KEYVALUE VARCHAR2(900);
  --1.����ִ�����
  L_CALL_NO NUMBER;
BEGIN
  --2.����ִ�����
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.��ʼ��¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'OPERATION2SAP_SYNCBY_HK',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

  BEGIN
    --��ѯ�ӿ��Ƿ��
    SELECT C.KEY_VALUE
      INTO KEYVALUE
      FROM TL_SPMS_SYS_CONFIG C
     WHERE C.KEY_NAME = 'CD_PRO2SAP_CLASS';

    --�жϽӿ��Ƿ��
    IF KEYVALUE = '1' THEN
      BEGIN
        
  INSERT INTO TT_SAPSYNCHRONOUS_HKTMP
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
     ERROR_INFO,
     SCHEDULE_DAILY_ID,
     EMP_POST_TYPE)
    SELECT d.id,
           E.EMP_CODE,
           TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') BEGIN_DATE,
           TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') END_DATE,
           '',
           '',
           '',
           d.shedule_code,
           '2' CLASS_SYSTEM,
           SYSDATE CREATE_TM,
           '' NODE_KEY,
           0 STATE_FLG,
           '' ERROR_INFO,
           d.id,
           '1'
      FROM TT_PB_SHEDULE_BY_DAY     D,
           TT_PB_SHEDULE_BY_MONTH   M,
           TM_PB_SCHEDULE_BASE_INFO B,
           TM_OSS_EMPLOYEE          E
    --�����±�
     WHERE D.SHEDULE_MON_ID = M.ID
          --������Ա��
       AND D.EMP_CODE = E.EMP_CODE
          --����ת���Ű�����
       AND D.DEPT_ID = E.DEPT_ID
          
       AND (E.LAST_ZNO is null or TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') >=
           TO_CHAR(E.DATE_FROM, 'YYYYMMDD'))
          --������ְ��Ա�Ű�����
       AND (E.DIMISSION_DT is null or TO_CHAR(E.DIMISSION_DT, 'YYYYMMDD') >
           TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD'))
          --����ת��ǰ������
       AND (E.TRANSFER_DATE IS NULL OR TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') >=
           TO_CHAR(E.TRANSFER_DATE, 'YYYYMMDD'))
          --������ְ����֮ǰ������
       AND TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') >=
           TO_CHAR(E.SF_DATE, 'YYYYMMDD')
          --��������
       AND D.DEPT_ID = B.DEPT_ID
       AND D.SHEDULE_CODE = B.SCHEDULE_CODE
       AND D.SHEDULE_DT >= DATE
     '2015-01-01'
          --ȡ��ʷǰ40�������
       AND D.SHEDULE_DT >= TRUNC(SYSDATE - 40)
          --ȡ����ǰ������
       AND D.SHEDULE_DT <= TRUNC(SYSDATE)
          --������Ա���ͣ�������������˾�Ӫ�а���
       AND E.WORK_TYPE NOT IN (6, 8, 9)
          --���˺���͸۰�̨
       AND D.DEPT_ID IN
           (SELECT DEPT_ID
              FROM TM_DEPARTMENT
             START WITH DEPT_CODE IN ('CN07')
            CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE)
       AND D.SHEDULE_CODE IS NOT NULL
       AND (D.SYNCHRO_STATUS != 1 OR D.SYNCHRO_STATUS IS NULL)
          --ȡ�������
       AND B.YM = M.YM
       AND B.CLASS_TYPE = '1'
       AND E.EMP_POST_TYPE = '1'
       AND E.DATA_SOURCE = '2'
    UNION ALL
    --'��'״̬����
    SELECT d.id,
           E.EMP_CODE,
           TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') BEGIN_DATE,
           TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') END_DATE,
           '',
           '',
           '',
           d.shedule_code,
           '2' CLASS_SYSTEM,
           SYSDATE CREATE_TM,
           '' NODE_KEY,
           0 STATE_FLG,
           '' ERROR_INFO,
           d.id,
           '1'
      FROM TT_PB_SHEDULE_BY_DAY   D,
           TT_PB_SHEDULE_BY_MONTH M,
           --ȡȷ�ϵ��±���Ϣ
           
           TM_OSS_EMPLOYEE E
    --�����±�
     WHERE D.SHEDULE_MON_ID = M.ID
          --������ȷ�ϱ�
          
          --������Ա��
       AND D.EMP_CODE = E.EMP_CODE
          --����ת���Ű�����
       AND D.DEPT_ID = E.DEPT_ID
          -- ������ְǰ���Ű�����
       AND (E.SF_DATE IS NULL OR TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') >=
           TO_CHAR(E.SF_DATE, 'YYYYMMDD'))
       AND (E.LAST_ZNO is null or TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') >=
           TO_CHAR(E.DATE_FROM, 'YYYYMMDD'))
          --������ְ��Ա�Ű�����
       AND (E.DIMISSION_DT is null or TO_CHAR(E.DIMISSION_DT, 'YYYYMMDD') >
           TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD'))
          --����ת��ǰ������
       AND (E.TRANSFER_DATE IS NULL OR TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') >=
           TO_CHAR(E.TRANSFER_DATE, 'YYYYMMDD'))
          --������ְ����֮ǰ������
       AND TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') >=
           TO_CHAR(E.SF_DATE, 'YYYYMMDD')
       AND D.SHEDULE_DT >= DATE
     '2015-01-01'
          --ȡ��ʷǰ40�������
       AND D.SHEDULE_DT >= TRUNC(SYSDATE - 40)
          --ȡ����ǰ������
       AND D.SHEDULE_DT <= TRUNC(SYSDATE)
          --������Ա���ͣ�������������˾�Ӫ�а���
       AND E.WORK_TYPE NOT IN (6, 8, 9)
          --���˺���͸۰�̨
       AND D.DEPT_ID IN
           (SELECT DEPT_ID
              FROM TM_DEPARTMENT
             START WITH DEPT_CODE IN ('CN07')
            CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE)
       AND (D.SHEDULE_CODE = '��' OR D.SHEDULE_CODE = 'SW' OR
           D.SHEDULE_CODE = 'OFF')
       AND (D.SYNCHRO_STATUS != 1 OR D.SYNCHRO_STATUS IS NULL)
       AND E.EMP_POST_TYPE = '1'
       AND E.DATA_SOURCE = '2';

      
        --b.ɾ�����ڵ�����
        DELETE FROM TT_SAP_SYNCHRONOUS S
         WHERE EXISTS
         (SELECT 1
                  FROM (SELECT EMP_CODE, BEGIN_DATE
                          FROM TT_SAPSYNCHRONOUS_HKTMP
                         GROUP BY EMP_CODE, BEGIN_DATE) TM
                 WHERE TM.EMP_CODE = S.EMP_CODE
                   AND TM.BEGIN_DATE = S.BEGIN_DATE��;

        --c.��ʱ���1����ӿڱ�[ֻ��һ��ʱ���1]
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
          TMP.EMP_CODE,
          TMP.BEGIN_DATE,
          TMP.END_DATE,
          TMP.BEGIN_TM,
          TMP.END_TM,
          TMP.TMR_DAY_FLAG,
          TMP.OFF_DUTY_FLAG,
          TMP.CLASS_SYSTEM,
          SYSDATE,
          TMP.NODE_KEY,
          TMP.STATE_FLG,
          TMP.ID,
          TMP.EMP_POST_TYPE
     FROM TT_SAPSYNCHRONOUS_HKTMP TMP;

    
        --e.�޸�״̬
        UPDATE TT_PB_SHEDULE_BY_DAY D
          SET SYNCHRO_STATUS = 1
        WHERE D.ID IN (SELECT ID FROM TT_SAPSYNCHRONOUS_HKTMP E);
        COMMIT;
        --f.ɾ����ʱ��
        EXECUTE IMMEDIATE 'TRUNCATE TABLE TT_SAPSYNCHRONOUS_HKTMP';
        ----���������Ű�������SAP�ӿڱ�--END
      END;
    END IF;
  END;

  --4.������¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'OPERATION2SAP_SYNCBY_HK',
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
                                 'OPERATION2SAP_SYNCBY_HK',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);

END OPERATION2SAP_SYNCBY_HK;
/