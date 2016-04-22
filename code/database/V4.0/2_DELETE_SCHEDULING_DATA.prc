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
  
    INSERT INTO TT_SCHEDULE_DAILY_HIS
      SELECT *
        FROM SPMS.TT_SCHEDULE_DAILY T
       WHERE T.MONTH_ID < TO_CHAR(ADD_MONTHS(TRUNC(SYSDATE), -2), 'YYYYMM');
  
    BEGIN
      loop
        Delete tt_schedule_daily D
         where D.MONTH_ID <
               TO_CHAR(ADD_MONTHS(TRUNC(SYSDATE), -2), 'YYYYMM')
           and rownum <=
               (SELECT count(*)
                  FROM SPMS.TT_SCHEDULE_DAILY T
                 WHERE T.MONTH_ID <
                       TO_CHAR(ADD_MONTHS(TRUNC(SYSDATE), -2), 'YYYYMM'));
        exit when SQL%rowcount <= 5000;
        commit;
      end loop;
      commit;
    END;
  
    INSERT INTO TT_SAP_SYNCHRONOUS_HIS
      SELECT *
        FROM SPMS.TT_SAP_SYNCHRONOUS D
       WHERE D.BEGIN_DATE <
             TO_CHAR(ADD_MONTHS(TRUNC(SYSDATE, 'MONTH'), -2), 'YYYYMMDD');
  
    BEGIN
      loop
        Delete TT_SAP_SYNCHRONOUS D
         WHERE D.BEGIN_DATE <
               TO_CHAR(ADD_MONTHS(TRUNC(SYSDATE, 'MONTH'), -2), 'YYYYMMDD')
           and rownum <=
               (SELECT count(*)
                  FROM SPMS.TT_SAP_SYNCHRONOUS D
                 WHERE D.BEGIN_DATE <
                       TO_CHAR(ADD_MONTHS(TRUNC(SYSDATE, 'MONTH'), -2),
                               'YYYYMMDD'));
        exit when SQL%rowcount <= 5000;
        commit;
      end loop;
      commit;
    END;
  
    BEGIN
      loop
        Delete TT_SCH_EMP_ATTENCE_CLASS D
         WHERE D.BEGIN_DATE < ADD_MONTHS(TRUNC(SYSDATE, 'MONTH'), -2)
           and rownum <=
               (select count(*)
                  from TT_SCH_EMP_ATTENCE_CLASS D
                 WHERE D.BEGIN_DATE <
                       ADD_MONTHS(TRUNC(SYSDATE, 'MONTH'), -2));
        exit when SQL%rowcount <= 5000;
        commit;
      end loop;
      commit;
    END;
  
    INSERT INTO TT_PB_SHEDULE_BY_MONTH_HIS
      SELECT *
        FROM SPMS.TT_PB_SHEDULE_BY_MONTH D
       WHERE D.YM <
             TO_CHAR(ADD_MONTHS(TRUNC(SYSDATE, 'MONTH'), -2), 'YYYY-MM');
  
    BEGIN
      loop
        Delete TT_PB_SHEDULE_BY_MONTH D
         WHERE D.YM <
               TO_CHAR(ADD_MONTHS(TRUNC(SYSDATE, 'MONTH'), -2), 'YYYY-MM')
           and rownum <=
               (SELECT count(*)
                  FROM SPMS.TT_PB_SHEDULE_BY_MONTH D
                 WHERE D.YM < TO_CHAR(ADD_MONTHS(TRUNC(SYSDATE, 'MONTH'), -2),
                                      'YYYY-MM'));
        exit when SQL%rowcount <= 5000;
        commit;
      end loop;
      commit;
    END;
  
    INSERT INTO TT_PB_SHEDULE_BY_DAY_HIS
      SELECT *
        FROM SPMS.TT_PB_SHEDULE_BY_DAY D
       WHERE D.SHEDULE_DT < ADD_MONTHS(TRUNC(SYSDATE, 'MONTH'), -2);
  
    BEGIN
      loop
        DELETE TT_PB_SHEDULE_BY_DAY D
         WHERE D.SHEDULE_DT < ADD_MONTHS(TRUNC(SYSDATE, 'MONTH'), -2)
           and rownum <=
               (SELECT count(*)
                  FROM SPMS.TT_PB_SHEDULE_BY_DAY D
                 WHERE D.SHEDULE_DT <
                       ADD_MONTHS(TRUNC(SYSDATE, 'MONTH'), -2));
        exit when SQL%rowcount <= 5000;
        commit;
      end loop;
      commit;
    END;
  
    INSERT INTO TT_DRIVER_SCHEDULING_HIS
      SELECT *
        FROM SPMS.TT_DRIVER_SCHEDULING D
       WHERE D.year_month <
             TO_CHAR(ADD_MONTHS(TRUNC(SYSDATE, 'MONTH'), -6), 'YYYY-MM');
  
    BEGIN    
      loop
        Delete TT_DRIVER_SCHEDULING D
         WHERE D.year_month <
               TO_CHAR(ADD_MONTHS(TRUNC(SYSDATE, 'MONTH'), -6), 'YYYY-MM')
           and rownum <=
               (SELECT count(*)
                  FROM SPMS.TT_DRIVER_SCHEDULING D
                 WHERE D.year_month <
                       TO_CHAR(ADD_MONTHS(TRUNC(SYSDATE, 'MONTH'), -6),
                               'YYYY-MM'));
        exit when SQL%rowcount <= 5000;
        commit;
      end loop;
      commit;
    END;
  
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
