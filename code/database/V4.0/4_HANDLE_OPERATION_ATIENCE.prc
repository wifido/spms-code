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
     WHERE TMP.POST_TYPE = '1'
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
       PAPER,
       CLASS_CODE,
       LEAVE_TYPE,
       ATTENDANCE_RATE)
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
                   ROUND((NVL(JB.STDAZ, 0) * LOG.ARBST) / LOG.ARBST, 3) +
                   LOG.ZZ_SJCQ
                  ELSE
                   ROUND(NVL(JB.STDAZ, 0) / LOG.ARBST, 3) + LOG.ZZ_SJCQ
                END
             END,
             CASE
               WHEN CASE
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
                          ROUND((NVL(JB.STDAZ, 0) * LOG.ARBST) / LOG.ARBST, 3) +
                          LOG.ZZ_SJCQ
                         ELSE
                          ROUND(NVL(JB.STDAZ, 0) / LOG.ARBST, 3) + LOG.ZZ_SJCQ
                       END
                    END <> 0 AND PROCESS.PROCESS_CODE = '��' THEN
                'OO'
               ELSE
                PROCESS.PROCESS_CODE
             END,
             DECODE(PC.DIFFICULTY_MODIFY_VALUE,
                    NULL,
                    NVL(PC.DIFFICULTY_VALUE, 0),
                    DIFFICULTY_MODIFY_VALUE) DIFFICULTY_MODIFY_VALUE,
             1,
             E.PERSK_TXT,
             'ADMIN',
             SYSDATE,
             'ADMIN',
             SYSDATE,
             NVL(LOG.ARBST, 0) * NVL(LOG.ZZ_SJCQ, 0) +
             (CASE
                WHEN JB.DW = '����' THEN
                 NVL(JB.STDAZ, 0) * NVL(LOG.ARBST, 0)
                ELSE
                 NVL(JB.STDAZ, 0)
              END),
             CASE
               WHEN JB.DW = '����' THEN
                NVL(JB.STDAZ, 0) * NVL(LOG.ARBST, 0)
               ELSE
                NVL(JB.STDAZ, 0)
             END,
             NVL(LOG.ARBST, 0),
             LOG.PAPER,
             td.shedule_code,
             qj.atext,
             case when td.shedule_code not in ('��','SW','OFF') 
               then
                 case when qj.atext is not null   and CASE
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
                          ROUND((NVL(JB.STDAZ, 0) * LOG.ARBST) / LOG.ARBST, 3) +
                          LOG.ZZ_SJCQ
                         ELSE
                          ROUND(NVL(JB.STDAZ, 0) / LOG.ARBST, 3) + LOG.ZZ_SJCQ
                       END
                    END < 1 then
                 1 
                 when CASE
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
                          ROUND((NVL(JB.STDAZ, 0) * LOG.ARBST) / LOG.ARBST, 3) +
                          LOG.ZZ_SJCQ
                         ELSE
                          ROUND(NVL(JB.STDAZ, 0) / LOG.ARBST, 3) + LOG.ZZ_SJCQ
                       END
                    END > 0.9 then 1
                    else
                      0 
                      end
               else  case when  
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
                          ROUND((NVL(JB.STDAZ, 0) * LOG.ARBST) / LOG.ARBST, 3) +
                          LOG.ZZ_SJCQ
                         ELSE
                          ROUND(NVL(JB.STDAZ, 0) / LOG.ARBST, 3) + LOG.ZZ_SJCQ
                       END
                    END <> 0 then 0
                    else
                      1
                      end
                  end
        FROM TI_SAP_ZTHR_PT_DETAIL_TMP LOG
        JOIN TM_OSS_EMPLOYEE E
          ON LOG.PERNR = E.EMP_CODE
        JOIN TT_PB_SHEDULE_BY_DAY TD
          ON LOG.PERNR = TD.EMP_CODE
         AND TO_DATE(LOG.BEGDA, 'YYYY-MM-DD') = TD.SHEDULE_DT
        LEFT JOIN TM_DEPARTMENT D
          ON TD.DEPT_ID = D.DEPT_ID
        LEFT JOIN TM_PB_GROUP_INFO G
          ON E.GROUP_ID = G.GROUP_ID
        LEFT JOIN TT_PB_PROCESS_BY_DAY PROCESS
          ON LOG.PERNR = PROCESS.EMP_CODE
         AND TO_DATE(LOG.BEGDA, 'YYYY-MM-DD') = PROCESS.PROCESS_DT
        LEFT JOIN TM_PB_PROCESS_INFO PC
          ON PROCESS.DEPT_ID = PC.DEPT_ID
         AND PROCESS.PROCESS_CODE = PC.PROCESS_CODE
        LEFT JOIN TT_PB_TRAINING_INFO TI
          ON LOG.PERNR = TI.EMPLOYEE_CODE
         AND TO_DATE(LOG.BEGDA, 'YYYY-MM-DD') =
             TO_DATE(TI.DAY_OF_MONTH, 'YYYY-MM-DD')
        LEFT JOIN TI_SAP_ZTHRPT0013 JB
          ON GETS_EMP_CODE_ZERO_FILL(LOG.PERNR) = JB.PERNR
         AND LOG.BEGDA = JB.BEGDA
        LEFT JOIN Ti_SAP_ZTHRPT0008 qj
          ON GETS_EMP_CODE_ZERO_FILL(LOG.PERNR) = qj.PERNR
         AND LOG.BEGDA = qj.BEGDA
        LEFT JOIN (SELECT SC.PERNR,
                          SC.BEGDA,
                          NVL(GRP.GROUP_NAME, '') GROUP_NAME,
                          NVL(GRP.GROUP_CODE, '') GROUP_CODE
                     FROM OP_EMP_GROUP_MODIFY_RECORD RC
                     JOIN (SELECT LOG.PERNR, LOG.BEGDA, MIN(RC.ID) ID
                            FROM TI_SAP_ZTHR_PT_DETAIL_TMP  LOG,
                                 OP_EMP_GROUP_MODIFY_RECORD RC
                           WHERE LOG.PERNR = RC.EMP_CODE
                             AND TO_DATE(LOG.BEGDA, 'YYYY-MM-DD') <
                                 RC.ENABLE_TM
                             AND RC.ENABLE_STATE = 1
                           GROUP BY LOG.PERNR, LOG.BEGDA) SC
                       ON RC.ID = SC.ID
                     LEFT JOIN TM_PB_GROUP_INFO GRP
                       ON RC.PREV_GROUP_ID = GRP.GROUP_ID) RC
          ON LOG.PERNR = RC.PERNR
         AND LOG.BEGDA = RC.BEGDA
       WHERE LOG.SYNC_STATUS = 1
         AND LOG.POST_TYPE = '1';

    /*  ����ɹ���ɾ�����������Ŀ�������*/
    DELETE TI_SAP_ZTHR_PT_DETAIL_TMP TMP
     WHERE TMP.SYNC_STATUS = 1
       AND TMP.POST_TYPE = '1';
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
