CREATE OR REPLACE PROCEDURE DELETE_SCHEDULING_DATA IS
  --*************************************************************
  -- AUTHOR  : JHC
  -- CREATED : 2015-03-05
  -- PURPOSE : ������ʷ�Ű����ݣ�ɾ���������ݣ�ֻ����һ�����ȵ����ݣ�
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
  --2.����ִ�����
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.��ʼ��¼��־
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

  --4.��ʼ��¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'DELETE_SCHEDULING_DATA',
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
                                 'DELETE_SCHEDULING_DATA',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);

END DELETE_SCHEDULING_DATA;
/
