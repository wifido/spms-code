CREATE OR REPLACE PROCEDURE HANDLE_WAREHOUSE_ATIENCE(LIMITNUMBER IN NUMBER) IS
  --*************************************************************
  -- AUTHOR  : JHC
  -- CREATED : 2015-03-05
  -- PURPOSE : ���������������ݣ�ÿ10W����һ��
  --
  -- PARAMETER:
  -- NAME             TYPE            DESC
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************
  --1.����ִ�����
  L_CALL_NO NUMBER;

BEGIN
  --2.����ִ�����
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.��ʼ��¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'HANDLE_WAREHOUSE_ATIENCE',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);
  BEGIN
    /*  ����Ҫ�����10W���ݵ�״̬ ���� */
    UPDATE TI_SAP_ZTHR_PT_DETAIL_TMP TMP
       SET TMP.SYNC_STATUS = 1
     WHERE TMP.POST_TYPE = '3'
       AND TMP.SYNC_STATUS = 0
       AND ROWNUM <= LIMITNUMBER;
    COMMIT;

    /*  ��10W���ݲ������ӿڱ�  */
    INSERT INTO TI_TCAS_SPMS_SCHEDULE
      (SCHEDULE_ID,
       EMP_CODE,
       EMP_NAME,
       AREA_CODE,
       DEPT_CODE,
       GROUP_CODE,
       WORK_DATE,
       WORK_TIME,
       JOB_SEQ_CODE,
       JOB_SEQ,
       POSITION_TYPE,
       PERSON_TYPE,
       CREAT_EMP_CODE,
       CREAT_TIME,
       MODIFY_EMP_CODE,
       MODIFY_TIME,
       KQ_XSS,
       STDAZ,
       ARBST,
       PAPER)
      SELECT SEQ_TI_TCAS_SPMS_SCHEDULE.NEXTVAL,
             E.EMP_CODE,
             E.EMP_NAME,
             NVL(D.AREA_CODE, EMP_DEPT.AREA_CODE),
             NVL(D.DEPT_CODE, EMP_DEPT.DEPT_CODE),
             '',
             TO_DATE(LOG.BEGDA, 'YYYY-MM-DD') BEGDA,
             -- ����ϵ������
             --��Ϣ�ռӰࡢ�����ڼ��ռӰ࿼��ϵ��Ϊ1��
             -- ���Ű�ʱ��Ϊ0 ��ȴ�мӰ����̣���ֱ���üӰ�ʱ������8Сʱ
             --����=1�죬����ϵ��Ϊ0=
             --���򣬿���ϵ��=ʵ�ʳ��� + �Ӱ�ʱ��/�Ű�ʱ��
             CASE
               WHEN (JB.TXT = '��Ϣ�ռӰ�' OR JB.TXT = '�����ڼ��ռӰ�') THEN
                1
               WHEN ARBST = 0 THEN
                CASE
                  WHEN JB.DW = '����' THEN
                   NVL(JB.STDAZ, 0)
                  ELSE
                   NVL(JB.STDAZ, 0) / 8
                END
               WHEN TO_DATE(LOG.BEGDA, 'YYYY-MM-DD') =
                    TO_DATE(TI.DAY_OF_MONTH, 'YYYY-MM-DD') AND
                    TI.TRAINING_CODE IS NOT NULL THEN
                0
               WHEN ZZ_KG <> 0 THEN
                0
               ELSE
                CASE
                  WHEN JB.DW = '����' THEN
                   ROUND((NVL(JB.STDAZ, 0) * ARBST) / ARBST, 3) + LOG.ZZ_SJCQ
                  ELSE
                   ROUND(NVL(JB.STDAZ, 0) / ARBST, 3) + LOG.ZZ_SJCQ
                END
             END,
             '',
             '',
             2,
             E.PERSK_TXT,
             'ADMIN',
             SYSDATE,
             'ADMIN',
             SYSDATE,
             NVL(ARBST, 0) * LOG.ZZ_SJCQ +
             (CASE
                WHEN JB.DW = '����' THEN
                NVL(JB.STDAZ, 0) * NVL(ARBST, 0)
               ELSE
                NVL(JB.STDAZ, 0)
              END),
             CASE
               WHEN JB.DW = '����' THEN
                NVL(JB.STDAZ, 0) * NVL(ARBST, 0)
               ELSE
                NVL(JB.STDAZ, 0)
             END,
             NVL(ARBST, 0),
             LOG.PAPER
        FROM TI_SAP_ZTHR_PT_DETAIL_TMP LOG
        JOIN TM_OSS_EMPLOYEE E
          ON LOG.PERNR = E.EMP_CODE
        JOIN TM_DEPARTMENT EMP_DEPT
          ON E.DEPT_ID = EMP_DEPT.DEPT_ID
        LEFT JOIN TI_SAP_ZTHRPT0013 JB
          ON LOG.PERNR = JB.PERNR
         AND LOG.BEGDA = JB.BEGDA
        LEFT JOIN TT_PB_TRAINING_INFO TI
          ON LOG.PERNR = TI.EMPLOYEE_CODE
         AND TO_DATE(LOG.BEGDA, 'YYYY-MM-DD') =
             TO_DATE(TI.DAY_OF_MONTH, 'YYYY-MM-DD')
        LEFT JOIN (SELECT DISTINCT EMPLOYEE_CODE,
                                   DEPARTMENT_CODE,
                                   DAY_OF_MONTH
                     FROM TT_SCHEDULE_DAILY
                    WHERE EMP_POST_TYPE = '3') TS
          ON LOG.PERNR = TS.EMPLOYEE_CODE
         AND REPLACE(LOG.BEGDA, '-', '') = TS.DAY_OF_MONTH
        LEFT JOIN TM_DEPARTMENT D
          ON TS.DEPARTMENT_CODE = D.DEPT_CODE
       WHERE LOG.SYNC_STATUS = 1
         AND E.IS_HAVE_COMMISSION = 1
         and LOG.POST_TYPE = '3';

    /*  ����ɹ���ɾ�����������Ŀ�������*/
    DELETE TI_SAP_ZTHR_PT_DETAIL_TMP TMP WHERE TMP.SYNC_STATUS = 1
    and   TMP.POST_TYPE = '3';
    COMMIT;
  END;
  --4.������¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'HANDLE_WAREHOUSE_ATIENCE',
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
    /*   �쳣�������Ŀ��ڸ�Ϊ����ʧ��   */
    UPDATE TI_SAP_ZTHR_PT_DETAIL_TMP TMP
       SET TMP.SYNC_STATUS = 3
     WHERE TMP.SYNC_STATUS = 1;
    COMMIT;

    PKG_OSS_COMM.STP_RUNNING_LOG('',
                                 'HANDLE_WAREHOUSE_ATIENCE',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);

END HANDLE_WAREHOUSE_ATIENCE;
/
