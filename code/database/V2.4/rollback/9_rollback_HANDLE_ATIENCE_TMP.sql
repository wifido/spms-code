CREATE OR REPLACE PROCEDURE HANDLE_ATIENCE_TMP(V_PAPER IN VARCHAR) AS
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
     WHERE D.POST_TYPE = '1';

    IF OPERATIONROWNUM > 0 THEN

      DELETE FROM TI_TCAS_SPMS_SCHEDULE TCAS
       WHERE TCAS.PAPER = V_PAPER
         AND TCAS.POSITION_TYPE = '1'
         AND EXISTS
       (SELECT 1
                FROM TI_SAP_ZTHR_PT_DETAIL_TMP E
               WHERE TCAS.EMP_CODE = E.PERNR
                 AND to_char(TCAS.Work_Date, 'YYYY-MM-DD') = E.BEGDA);

      -- ���������Ŀ�������
      FOR V_COUNT IN 1 .. OPERATIONROWNUM LOOP
        HANDLE_OPERATION_ATIENCE(LIMITNUMBER);
      END LOOP;

      COMMIT;
    END IF;

    -- ȡ�ֹ���Աѭ������
    SELECT CEIL(COUNT(1) / LIMITNUMBER)
      INTO WAREHOUSEROWNUM
      FROM TI_SAP_ZTHR_PT_DETAIL_TMP D
     WHERE D.POST_TYPE = '3';

    IF WAREHOUSEROWNUM > 0 THEN
      DELETE FROM TI_TCAS_SPMS_SCHEDULE TCAS
       WHERE TCAS.PAPER = V_PAPER
         AND TCAS.POSITION_TYPE = '2'
         AND EXISTS
       (SELECT 1
                FROM TI_SAP_ZTHR_PT_DETAIL_TMP E
               WHERE TCAS.EMP_CODE = E.PERNR
                 AND to_char(TCAS.Work_Date, 'YYYY-MM-DD') = E.BEGDA);

      -- ����ֹܵĿ�������
      FOR V_COUNT IN 1 .. WAREHOUSEROWNUM LOOP
        HANDLE_WAREHOUSE_ATIENCE(LIMITNUMBER);
      END LOOP;

      COMMIT;
    END IF;

    -- ͬ�������Ա��Ϣ
    SYS_OPERATION_EMPLOYEE_INFO(V_PAPER);

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