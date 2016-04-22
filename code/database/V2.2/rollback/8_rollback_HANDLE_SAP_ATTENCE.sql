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


  SELECT COUNT(1) INTO V_SAP_DETAIL_NUM
  FROM TI_SAP_ZTHR_PT_DETAIL WHERE ROWNUM  = 1;

  IF V_SAP_DETAIL_NUM >0 THEN

  EXECUTE IMMEDIATE 'TRUNCATE TABLE TI_SAP_ZTHR_PT_DETAIL_HIS';
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
       SYNC_STATUS)
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
             SUBSTR(BEGDA,1,7),
             CREATED_ON,
             CREATED_TS,
             SYSDATE,
             0
        FROM TI_SAP_ZTHR_PT_DETAIL D, (SELECT GETS_EMP_CODE_ZERO_FILL(EMP_CODE) V_EMP_CODE, EMP_CODE FROM TM_OSS_EMPLOYEE WHERE EMP_POST_TYPE = '1' OR EMP_POST_TYPE = '3') E
       WHERE E.V_EMP_CODE = D.PERNR;
    COMMIT;
    --F.ɾ���ӿڱ�
    EXECUTE IMMEDIATE 'TRUNCATE TABLE TI_SAP_ZTHR_PT_DETAIL';

      FOR DETAIL_PAPER_ROW IN (SELECT DISTINCT T.PAPER  FROM TI_SAP_ZTHR_PT_DETAIL_TMP T) LOOP
            -- ִ�д�������ʱ�������
            HANDLE_ATIENCE_TMP(DETAIL_PAPER_ROW.PAPER);
      END LOOP;
         -- ����Ӱࡢ������ݵ��Ӱࡢ�����ʽ��
    HANDLE_JBANDQJ_SAP();
    
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