CREATE OR REPLACE PROCEDURE HANDLE_SAP_ATTENCE AS
  --*************************************************************
  -- AUTHOR  : JHC
  -- CREATED : 2015-03-05
  -- PURPOSE : 处理考勤数据，插入考勤历史表和需要处理的数据临时表
  --
  -- PARAMETER:
  -- NAME             TYPE            DESC
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************
  --1.定义执行序号
  L_CALL_NO NUMBER;
  V_SAP_DETAIL_NUM NUMBER;
BEGIN
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
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
    -- 将考勤接口表的数据插入到历史表
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

    /* 将符合条件的数据插入临时表  */
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
    --F.删除接口表
    EXECUTE IMMEDIATE 'TRUNCATE TABLE TI_SAP_ZTHR_PT_DETAIL';

      FOR DETAIL_PAPER_ROW IN (SELECT DISTINCT T.PAPER  FROM TI_SAP_ZTHR_PT_DETAIL_TMP T) LOOP
            -- 执行处理考勤零时表的数据
            HANDLE_ATIENCE_TMP(DETAIL_PAPER_ROW.PAPER);
      END LOOP;
         -- 处理加班、请假数据到加班、请假正式表
    HANDLE_JBANDQJ_SAP();
    
    END IF;
  END;
  --4.结束记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'HANDLE_SAP_ATTENCE',
                               SYSDATE,
                               NULL,
                               NULL,
                               'END',
                               0,
                               L_CALL_NO);
  --5.异常记录日志
EXCEPTION
  WHEN OTHERS THEN
    --回滚数据
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