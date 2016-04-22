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
  L_CALL_NO        NUMBER;
  V_SAP_DETAIL_NUM NUMBER;
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
  
    SELECT COUNT(1)
      INTO V_SAP_DETAIL_NUM
      FROM TI_SAP_ZTHR_PT_DETAIL
     WHERE ROWNUM = 1;
  
    -- ����Ӱࡢ������ݵ��Ӱࡢ�����ʽ��
    HANDLE_JBANDQJ_SAP();
  
    IF V_SAP_DETAIL_NUM > 0 THEN
    
      /*EXECUTE IMMEDIATE 'TRUNCATE TABLE TI_SAP_ZTHR_PT_DETAIL_HIS';*/
      -- �����ڽӿڱ�����ݲ��뵽��ʷ��
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
         SYNC_STATUS,
         POST_TYPE)
        SELECT MANDT,
               POST.EMP_CODE,
               D.BEGDA,
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
               SUBSTR(D.BEGDA, 1, 7),
               CREATED_ON,
               CREATED_TS,
               SYSDATE,
               0,
               POST.PREVIOUS_POST
          FROM TI_SAP_ZTHR_PT_DETAIL D,
               (SELECT GETS_EMP_CODE_ZERO_FILL(RC.EMP_CODE) PERNR,
                       RC.EMP_CODE EMP_CODE,
                       SC.BEGDA,
                       RC.PREVIOUS_POST
                  FROM TM_EMP_POST_CHANGE_RECORD RC
                  JOIN (SELECT LOG.PERNR, LOG.BEGDA, MIN(POST.ID) ID
                         FROM TI_SAP_ZTHR_PT_DETAIL     LOG,
                              TM_EMP_POST_CHANGE_RECORD POST
                        WHERE LOG.PERNR =
                              GETS_EMP_CODE_ZERO_FILL(POST.EMP_CODE)
                          AND TO_DATE(LOG.BEGDA, 'YYYY-MM-DD') <
                              POST.CHANGE_DATE
                        GROUP BY LOG.PERNR, LOG.BEGDA) SC
                    ON RC.ID = SC.ID) POST
         WHERE D.PERNR = POST.PERNR
           AND D.BEGDA = POST.BEGDA;
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
         SYNC_STATUS,
         POST_TYPE)
        SELECT MANDT,
               E.EMP_CODE,
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
               SUBSTR(BEGDA, 1, 7),
               CREATED_ON,
               CREATED_TS,
               SYSDATE,
               0,
               E.EMP_POST_TYPE
          FROM TI_SAP_ZTHR_PT_DETAIL D, TM_OSS_EMPLOYEE e
         where GETS_EMP_CODE_ZERO_FILL(e.EMP_CODE) = d.pernr
           AND (e.transfer_date is null or
                d.begda >= to_char(e.transfer_date, 'YYYY-MM-DD'));
      COMMIT;
      --F.ɾ���ӿڱ�
      EXECUTE IMMEDIATE 'TRUNCATE TABLE TI_SAP_ZTHR_PT_DETAIL';
    
      FOR DETAIL_PAPER_ROW IN (SELECT DISTINCT T.PAPER
                                 FROM TI_SAP_ZTHR_PT_DETAIL_TMP T) LOOP
        -- ִ�д�������ʱ�������
        HANDLE_ATIENCE_TMP(DETAIL_PAPER_ROW.PAPER);
      END LOOP;
    
    END IF;
  
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