CREATE OR REPLACE PROCEDURE HANDLE_SAP_ATTENCE AS
  --*************************************************************
  -- AUTHOR  : JHC
  -- CREATED : 2015-03-05
  -- PURPOSE : ���������ݣ����뿼����ʷ�����Ҫ�����������ʱ��
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
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.��ʼ��¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'HANDLE_SAP_ATTENCE',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);
  BEGIN

  /* �����ڽӿڱ�����ݲ��뵽��ʷ��  */
    INSERT INTO TI_SAP_ZTHR_PT_DETAIL_HIS
      (MANDT,
       PERNR,
       BEGDA,
       NACHN,
       ORGTX,
       ORGEH_T,
       PLANS_T,
       ZHRGWSX_T,
       PTEXT_G,
       PTEXT_K,
       ZTEXT,
       BEGUZ,
       ENDUZ,
       P10,
       P20,
       ZZ_CD,
       ZZ_ZT,
       ARBST,
       ZZ_CQSJ,
       ZZ_QJQK,
       ZZ_OFF,
       TXT,
       STDAZ,
       ZZ_KG,
       ZZ_SJCQ,
       DW,
       PAPER,
       CREATED_ON,
       CREATED_TS,
       SYNC_TM)
      SELECT MANDT,
             PERNR,
             BEGDA,
             NACHN,
             ORGTX,
             ORGEH_T,
             PLANS_T,
             ZHRGWSX_T,
             PTEXT_G,
             PTEXT_K,
             ZTEXT,
             BEGUZ,
             ENDUZ,
             P10,
             P20,
             ZZ_CD,
             ZZ_ZT,
             ARBST,
             ZZ_CQSJ,
             ZZ_QJQK,
             ZZ_OFF,
             TXT,
             STDAZ,
             ZZ_KG,
             ZZ_SJCQ,
             DW,
             PAPER,
             CREATED_ON,
             CREATED_TS,
             SYNC_TM
        FROM TI_SAP_ZTHR_PT_DETAIL D;
    COMMIT;

  /* ���������������ݲ�����ʱ��  */
    INSERT INTO TI_SAP_ZTHR_PT_DETAIL_TMP
      (MANDT,
       PERNR,
       BEGDA,
       NACHN,
       ORGTX,
       ORGEH_T,
       PLANS_T,
       ZHRGWSX_T,
       PTEXT_G,
       PTEXT_K,
       ZTEXT,
       BEGUZ,
       ENDUZ,
       P10,
       P20,
       ZZ_CD,
       ZZ_ZT,
       ARBST,
       ZZ_CQSJ,
       ZZ_QJQK,
       ZZ_OFF,
       TXT,
       STDAZ,
       ZZ_KG,
       ZZ_SJCQ,
       DW,
       PAPER,
       CREATED_ON,
       CREATED_TS,
       SYNC_TM,
       SYNC_STATUS)
      SELECT MANDT,
             PERNR,
             BEGDA,
             NACHN,
             ORGTX,
             ORGEH_T,
             PLANS_T,
             ZHRGWSX_T,
             PTEXT_G,
             PTEXT_K,
             ZTEXT,
             BEGUZ,
             ENDUZ,
             P10,
             P20,
             ZZ_CD,
             ZZ_ZT,
             ARBST,
             ZZ_CQSJ,
             ZZ_QJQK,
             ZZ_OFF,
             TXT,
             STDAZ,
             ZZ_KG,
             ZZ_SJCQ,
             DW,
             PAPER,
             CREATED_ON,
             CREATED_TS,
             SYNC_TM,
             0
        FROM TI_SAP_ZTHR_PT_DETAIL D
       WHERE EXISTS (SELECT 1
                FROM TM_OSS_EMPLOYEE E
               WHERE E.EMP_CODE = D.PERNR
               AND (E.EMP_POST_TYPE = '1'OR E.EMP_POST_TYPE = '3'));
    COMMIT;
    --F.ɾ���ӿڱ�
    EXECUTE IMMEDIATE 'TRUNCATE TABLE TI_SAP_ZTHR_PT_DETAIL';

    -- ִ�д�������ʱ�������
    HANDLE_ATIENCE_TMP();
  END;
  --4.������¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'HANDLE_SAP_ATTENCE',
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
                                 'HANDLE_SAP_ATTENCE',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
END HANDLE_SAP_ATTENCE;
/
CREATE OR REPLACE PROCEDURE HANDLE_ATIENCE_TMP AS
  --*************************************************************
  -- AUTHOR  : JHC
  -- CREATED : 2015-03-05
  -- PURPOSE : ������ʱ�����ݣ�������Ա��λ��������ÿ10W�����ݴ���һ�Ρ�
  --
  -- PARAMETER:
  -- NAME             TYPE            DESC
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************

  OPERATIONROWNUM NUMBER;
  LIMITNUMBER     NUMBER;
  WAREHOUSEROWNUM NUMBER;
  --1.����ִ�����
  L_CALL_NO NUMBER;
BEGIN
  --2.����ִ�����
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.��ʼ��¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'HANDLE_ATIENCE_TMP',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);
  BEGIN
    LIMITNUMBER := 100000;

   -- ȡ������Աѭ������
    SELECT CEIL(COUNT(1) / LIMITNUMBER)
      INTO OPERATIONROWNUM
      FROM TI_SAP_ZTHR_PT_DETAIL_TMP D
     WHERE EXISTS (SELECT 1
              FROM TM_OSS_EMPLOYEE E
             WHERE E.EMP_CODE = D.PERNR
               AND E.EMP_POST_TYPE = '1');

    -- ���������Ŀ�������
    FOR V_COUNT IN 1 .. OPERATIONROWNUM LOOP
      HANDLE_OPERATION_ATIENCE(LIMITNUMBER);
    END LOOP;

    COMMIT;


    -- ȡ�ֹ���Աѭ������
    SELECT CEIL(COUNT(1) / LIMITNUMBER)
      INTO WAREHOUSEROWNUM
      FROM TI_SAP_ZTHR_PT_DETAIL_TMP D
     WHERE EXISTS (SELECT 1
              FROM TM_OSS_EMPLOYEE E
             WHERE E.EMP_CODE = D.PERNR
               AND E.EMP_POST_TYPE = '3');

    -- ����ֹܵĿ�������
    FOR V_COUNT IN 1 .. WAREHOUSEROWNUM LOOP
      HANDLE_WAREHOUSE_ATIENCE(LIMITNUMBER);
    END LOOP;

    COMMIT;


    --F.ɾ����ʱ��
    EXECUTE IMMEDIATE 'TRUNCATE TABLE TI_SAP_ZTHR_PT_DETAIL_TMP';


  END;

  --4.������¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'HANDLE_ATIENCE_TMP',
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
                                 'HANDLE_ATIENCE_TMP',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
END HANDLE_ATIENCE_TMP;
/
CREATE OR REPLACE PROCEDURE HANDLE_OPERATION_ATIENCE(LIMITNUMBER IN NUMBER) IS
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
                               'HANDLE_OPERATION_ATIENCE',
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
     WHERE EXISTS (SELECT 1
              FROM TM_OSS_EMPLOYEE E
             WHERE E.EMP_CODE = TMP.PERNR
               AND E.EMP_POST_TYPE = '1')
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
       MODIFY_TIME)
      SELECT SEQ_TI_TCAS_SPMS_SCHEDULE.NEXTVAL,
             E.EMP_CODE,
             E.EMP_NAME,
             D.AREA_CODE,
             D.DEPT_CODE,
             DECODE(RC.GROUP_CODE, NULL, G.GROUP_CODE, RC.GROUP_CODE) GROUP_CODE,
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
                  WHEN JB.DW = 'ʱ' THEN
                   NVL(JB.STDAZ, 0) / 8
                  ELSE
                   NVL(JB.STDAZ, 0)
                END
               WHEN TO_DATE(LOG.BEGDA, 'YYYY-MM-DD') =
                    TO_DATE(TI.DAY_OF_MONTH, 'YYYY-MM-DD') AND
                    TI.TRAINING_CODE IS NOT NULL THEN
                0
               WHEN ZZ_KG = 1 THEN
                0
               ELSE
                CASE
                  WHEN JB.DW = 'ʱ' THEN
                   ROUND(NVL(JB.STDAZ, 0) / ARBST, 1) + LOG.ZZ_CQSJ
                  ELSE
                   ROUND((NVL(JB.STDAZ, 0) * ARBST) / ARBST, 1) + LOG.ZZ_CQSJ
                END
             END,
             PROCESS.PROCESS_CODE,
             DECODE(PC.DIFFICULTY_MODIFY_VALUE,
                    NULL,
                    NVL(PC.DIFFICULTY_VALUE, 0),
                    DIFFICULTY_MODIFY_VALUE) DIFFICULTY_MODIFY_VALUE,
             1,
             E.PERSK_TXT,
             'ADMIN',
             SYSDATE,
             'ADMIN',
             SYSDATE
        FROM TI_SAP_ZTHR_PT_DETAIL_TMP LOG,
             TM_OSS_EMPLOYEE E,
             TT_PB_PROCESS_BY_DAY PROCESS,
             TM_PB_GROUP_INFO G,
             TM_PB_PROCESS_INFO PC,
             TM_DEPARTMENT D,
             TT_PB_TRAINING_INFO TI,
             TI_SAP_ZTHRPT0013 JB,
             (SELECT SC.PERNR,
                     SC.BEGDA,
                     NVL(GRP.GROUP_NAME, '') GROUP_NAME,
                     NVL(GRP.GROUP_CODE, '') GROUP_CODE
                FROM OP_EMP_GROUP_MODIFY_RECORD RC,
                     (SELECT LOG.PERNR, LOG.BEGDA, MIN(RC.ID) ID
                        FROM TI_SAP_ZTHR_PT_DETAIL_TMP  LOG,
                             OP_EMP_GROUP_MODIFY_RECORD RC
                       WHERE LOG.PERNR = RC.EMP_CODE
                         AND TO_DATE(LOG.BEGDA, 'YYYY-MM-DD') < RC.ENABLE_TM
                         AND RC.ENABLE_STATE = 1
                       GROUP BY LOG.PERNR, LOG.BEGDA) SC,
                     TM_PB_GROUP_INFO GRP
               WHERE RC.ID = SC.ID
                 AND RC.PREV_GROUP_ID = GRP.GROUP_ID(+)) RC
       WHERE LOG.PERNR = E.EMP_CODE(+)
         AND LOG.PERNR = PROCESS.EMP_CODE(+)
         AND LOG.PERNR = TI.EMPLOYEE_CODE(+)
         AND LOG.PERNR = JB.PAPER(+)
         AND LOG.BEGDA = JB.BEGDA(+)
         AND TO_DATE(LOG.BEGDA, 'YYYY-MM-DD') =
             TO_DATE(TI.DAY_OF_MONTH(+), 'YYYY-MM-DD')
         AND TO_DATE(LOG.BEGDA, 'YYYY-MM-DD') = PROCESS.PROCESS_DT(+)
         AND E.DEPT_ID = D.DEPT_ID(+)
         AND E.GROUP_ID = G.GROUP_ID(+)
         AND PROCESS.DEPT_ID = PC.DEPT_ID(+)
         AND PROCESS.PROCESS_CODE = PC.PROCESS_CODE(+)
         AND LOG.PERNR = RC.PERNR(+)
         AND LOG.BEGDA = RC.BEGDA(+)
         AND LOG.SYNC_STATUS = 1
         AND E.EMP_POST_TYPE = '1';
  
    /*  ����ɹ���ɾ�����������Ŀ�������*/
    DELETE TI_SAP_ZTHR_PT_DETAIL_TMP TMP WHERE TMP.SYNC_STATUS = 1;
    COMMIT;
  END;
  --4.������¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'HANDLE_OPERATION_ATIENCE',
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
                                 'HANDLE_OPERATION_ATIENCE',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
  
END HANDLE_OPERATION_ATIENCE;
/
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
     WHERE EXISTS (SELECT 1
              FROM TM_OSS_EMPLOYEE E
             WHERE E.EMP_CODE = TMP.PERNR
               AND E.EMP_POST_TYPE = '3')
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
       MODIFY_TIME)
      SELECT SEQ_TI_TCAS_SPMS_SCHEDULE.NEXTVAL,
             E.EMP_CODE,
             E.EMP_NAME,
             D.AREA_CODE,
             D.DEPT_CODE,
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
                  WHEN JB.DW = 'ʱ' THEN
                   NVL(JB.STDAZ, 0) / 8
                  ELSE
                   NVL(JB.STDAZ, 0)
                END
               WHEN TO_DATE(LOG.BEGDA, 'YYYY-MM-DD') =
                    TO_DATE(TI.DAY_OF_MONTH, 'YYYY-MM-DD') AND
                    TI.TRAINING_CODE IS NOT NULL THEN
                0
               WHEN ZZ_KG = 1 THEN
                0
               ELSE
                CASE
                  WHEN JB.DW = 'ʱ' THEN
                   ROUND(NVL(JB.STDAZ, 0) / ARBST, 1) + LOG.ZZ_SJCQ
                  ELSE
                   ROUND((NVL(JB.STDAZ, 0) * ARBST) / ARBST, 1) + LOG.ZZ_SJCQ
                END
             END,
             '',
             '',
             2,
             E.PERSK_TXT,
             'ADMIN',
             SYSDATE,
             'ADMIN',
             SYSDATE
        FROM TI_SAP_ZTHR_PT_DETAIL_TMP LOG,
             TM_OSS_EMPLOYEE           E,
             TM_DEPARTMENT             D,
             TT_PB_TRAINING_INFO       TI,
             TI_SAP_ZTHRPT0013         JB,
             tt_schedule_daily         ts
             
       WHERE LOG.PERNR = E.EMP_CODE(+)
         AND LOG.PERNR = JB.PAPER(+)
         AND LOG.BEGDA = JB.BEGDA(+)
         AND TS.DEPARTMENT_CODE = D.DEPT_CODE(+)
         AND LOG.PERNR = TS.Employee_Code(+)
         AND LOG.PERNR = TI.EMPLOYEE_CODE(+)
         AND TO_DATE(LOG.BEGDA, 'YYYY-MM-DD') =
             TO_DATE(TI.DAY_OF_MONTH(+), 'YYYY-MM-DD')
         AND LOG.SYNC_STATUS = 1
         AND E.EMP_POST_TYPE = '3'
         AND E.IS_HAVE_COMMISSION = 1;
  
    /*  ����ɹ���ɾ�����������Ŀ�������*/
    DELETE TI_SAP_ZTHR_PT_DETAIL_TMP TMP WHERE TMP.SYNC_STATUS = 1;
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