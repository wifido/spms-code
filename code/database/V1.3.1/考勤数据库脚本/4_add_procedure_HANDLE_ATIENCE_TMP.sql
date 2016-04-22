create or replace procedure HANDLE_ATIENCE_TMP as
  operationrownum number;
/*  warehouserownum number;
  driverrownum    number;*/
  limitnumber     number;
  --1.����ִ�����
  L_CALL_NO NUMBER;
begin
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
  begin
    limitnumber := 100000;

    -- ȡ������Ա����
    select CEIL(count(1) / limitnumber)
      into operationrownum
      from ti_sap_zthr_pt_detail_tmp d
     WHERE EXISTS (SELECT 1
              FROM tm_oss_employee e
             WHERE e.emp_code = d.pernr
               and e.emp_post_type = '1');

    -- ���������Ŀ�������
    for v_count in 1 .. operationrownum loop
      SAP2SPMS_ZTHR_Handle_three(limitnumber);
    end loop;

    commit;
    --f.ɾ����ʱ��
    EXECUTE IMMEDIATE 'TRUNCATE TABLE TI_SAP_ZTHR_PT_DETAIL_TMP';


  end;

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
end HANDLE_ATIENCE_TMP;
