create or replace procedure HANDLE_SAP_ATTENCE as
  --1.定义执行序号
  L_CALL_NO NUMBER;
begin
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
  begin
    insert into ti_sap_zthr_pt_detail_HIS
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
      select MANDT,
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
        from Ti_Sap_Zthr_Pt_Detail d;

    insert into ti_sap_zthr_pt_detail_tmp
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
      select MANDT,
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
        from Ti_Sap_Zthr_Pt_Detail d
    WHERE EXISTS(
      SELECT 1
        FROM tm_oss_employee e
       WHERE e.emp_code = d.pernr
         and e.emp_post_type in ('1', '3', '5'));
    commit;
      --f.删除接口表
        EXECUTE IMMEDIATE 'TRUNCATE TABLE TI_SAP_ZTHR_PT_DETAIL';

  end;
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
end HANDLE_SAP_ATTENCE;
