CREATE OR REPLACE PROCEDURE HANDLE_ATIENCE_TMP AS
  --*************************************************************
  -- AUTHOR  : JHC
  -- CREATED : 2015-03-05
  -- PURPOSE : 处理临时表数据，根据人员岗位分批处理，每10W条数据处理一次。
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
  V_PAPER           VARCHAR(10);
  --1.定义执行序号
  L_CALL_NO NUMBER;
BEGIN
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
  BEGIN
    LIMITNUMBER := 100000;

    SELECT D.PAPER
      INTO V_PAPER
      FROM TI_SAP_ZTHR_PT_DETAIL_TMP D
     WHERE ROWNUM = 1;

    -- 取运作人员循环总数
    SELECT CEIL(COUNT(1) / LIMITNUMBER)
      INTO OPERATIONROWNUM
      FROM TI_SAP_ZTHR_PT_DETAIL_TMP D
     WHERE EXISTS (SELECT 1
              FROM TM_OSS_EMPLOYEE E
             WHERE E.EMP_CODE = D.PERNR
               AND E.EMP_POST_TYPE = '1');

    IF OPERATIONROWNUM > 0 THEN

      DELETE FROM TI_TCAS_SPMS_SCHEDULE TCAS
       WHERE TCAS.PAPER = V_PAPER
         AND TCAS.POSITION_TYPE = '1';

      -- 处理运作的考勤数据
      FOR V_COUNT IN 1 .. OPERATIONROWNUM LOOP
        HANDLE_OPERATION_ATIENCE(LIMITNUMBER);
      END LOOP;

      COMMIT;
    END IF;

    -- 取仓管人员循环总数
    SELECT CEIL(COUNT(1) / LIMITNUMBER)
      INTO WAREHOUSEROWNUM
      FROM TI_SAP_ZTHR_PT_DETAIL_TMP D
     WHERE EXISTS (SELECT 1
              FROM TM_OSS_EMPLOYEE E
             WHERE E.EMP_CODE = D.PERNR
               AND E.EMP_POST_TYPE = '3');

    IF WAREHOUSEROWNUM > 0 THEN
      DELETE FROM TI_TCAS_SPMS_SCHEDULE TCAS
       WHERE TCAS.PAPER = V_PAPER
         AND TCAS.POSITION_TYPE = '2';

      -- 处理仓管的考勤数据
      FOR V_COUNT IN 1 .. WAREHOUSEROWNUM LOOP
        HANDLE_WAREHOUSE_ATIENCE(LIMITNUMBER);
      END LOOP;

      COMMIT;
    END IF;
    
    -- 处理加班、请假数据到加班、请假正式表
    HANDLE_JBANDQJ_SAP();
    --F.删除临时表
    EXECUTE IMMEDIATE 'TRUNCATE TABLE TI_SAP_ZTHR_PT_DETAIL_TMP';

  END;

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
END HANDLE_ATIENCE_TMP;
/