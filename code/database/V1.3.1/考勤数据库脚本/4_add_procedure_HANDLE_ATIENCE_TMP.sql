create or replace procedure HANDLE_ATIENCE_TMP as
  operationrownum number;
/*  warehouserownum number;
  driverrownum    number;*/
  limitnumber     number;
  --1.定义执行序号
  L_CALL_NO NUMBER;
begin
  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'HANDLE_ATIENCE_TMP',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);
  begin
    limitnumber := 100000;

    -- 取运作人员总数
    select CEIL(count(1) / limitnumber)
      into operationrownum
      from ti_sap_zthr_pt_detail_tmp d
     WHERE EXISTS (SELECT 1
              FROM tm_oss_employee e
             WHERE e.emp_code = d.pernr
               and e.emp_post_type = '1');

    -- 处理运作的考勤数据
    for v_count in 1 .. operationrownum loop
      SAP2SPMS_ZTHR_Handle_three(limitnumber);
    end loop;

    commit;
    --f.删除临时表
    EXECUTE IMMEDIATE 'TRUNCATE TABLE TI_SAP_ZTHR_PT_DETAIL_TMP';


  end;

  --4.结束记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'HANDLE_ATIENCE_TMP',
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
                                 'HANDLE_ATIENCE_TMP',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
end HANDLE_ATIENCE_TMP;
