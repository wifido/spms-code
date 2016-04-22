CREATE OR REPLACE PROCEDURE DELETE_SCHEDULING_DATA IS
  --*************************************************************
  -- AUTHOR  : JHC
  -- CREATED : 2015-03-05
  -- PURPOSE : 备份历史排班数据，删除过期数据（只保留一个季度的数据）
  --
  -- PARAMETER:
  -- NAME             TYPE            DESC
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************

  L_CALL_NO VARCHAR(10);
BEGIN
  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'DELETE_SCHEDULING_DATA',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);
  BEGIN

    DELETE TT_SCHEDULE_DAILY D
     WHERE D.MONTH_ID < TO_CHAR(ADD_MONTHS(TRUNC(SYSDATE), -2), 'YYYYMM');
    COMMIT;

    DELETE TT_SAP_SYNCHRONOUS D
     WHERE D.BEGIN_DATE <
           TO_CHAR(ADD_MONTHS(TRUNC(SYSDATE, 'MONTH'), -2), 'YYYYMMDD');
    COMMIT;

    DELETE TT_PB_SHEDULE_BY_MONTH D
     WHERE D.YM <
           TO_CHAR(ADD_MONTHS(TRUNC(SYSDATE, 'MONTH'), -2), 'YYYY-MM');
    COMMIT;

    DELETE TT_PB_SHEDULE_BY_DAY D
     WHERE D.SHEDULE_DT < ADD_MONTHS(TRUNC(SYSDATE, 'MONTH'), -2);
    COMMIT;
  END;

  --4.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'DELETE_SCHEDULING_DATA',
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
                                 'DELETE_SCHEDULING_DATA',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);

END DELETE_SCHEDULING_DATA;
/
