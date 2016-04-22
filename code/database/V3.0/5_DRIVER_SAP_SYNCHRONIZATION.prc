CREATE OR REPLACE PROCEDURE DRIVER_SAP_SYNCHRONIZATION AS

  /**
    *每天22:30将仓管排班推送至SAP接口表
  **/
  KEYVALUE VARCHAR2(900);
  --1.定义执行序号
  L_CALL_NO NUMBER;
BEGIN

  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'DRIVER_SAP_SYNCHRONIZATION',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

  --查询接口是否打开
  SELECT C.KEY_VALUE
    INTO KEYVALUE
    FROM TL_SPMS_SYS_CONFIG C
   WHERE C.KEY_NAME = 'CD_WAREHOUSE2SAP_CLASS';
  --判断接口是否打开
  IF KEYVALUE = '1' THEN

    BEGIN
    ----推送SAP数据--BEGIN
    --1.插入临时数据

    INSERT INTO TT_SAP_SYNCHRONOUS_TMP4
      (ID,
       EMP_CODE,
       BEGIN_DATE,
       END_DATE,
       BEGIN_TM,
       END_TM,
       TMR_DAY_FLAG,
       OFF_DUTY_FLAG,
       CLASS_SYSTEM,
       CREATE_TM,
       NODE_KEY,
       STATE_FLG,
       EMP_POST_TYPE)
    SELECT T.ID,
           T.EMPLOYEE_CODE,
           T.DAY_OF_MONTH,
           T.DAY_OF_MONTH,
           '',
           '',
           '',
           'OFF',
           '2',
           SYSDATE,
           '',
           0,
           5
      FROM TT_DRIVER_SCHEDULING T, TM_OSS_EMPLOYEE E, TM_DEPARTMENT D
     WHERE T.DEPARTMENT_CODE = D.DEPT_CODE
       AND D.DEPT_ID = E.DEPT_ID
       AND T.EMPLOYEE_CODE = E.EMP_CODE
       AND (T.SYNC_STATE != 1 OR T.SYNC_STATE IS NULL)
       AND T.DAY_OF_MONTH >= '20150901'
          -- 取40天以内的数据
       AND T.DAY_OF_MONTH >= TO_CHAR(TRUNC(SYSDATE - 40), 'YYYYMMDD')
       AND T.DAY_OF_MONTH <= TO_CHAR(SYSDATE, 'YYYYMMDD')
       AND (E.LAST_ZNO IS NULL OR
           T.DAY_OF_MONTH >= TO_CHAR(E.DATE_FROM, 'YYYYMMDD'))
          --过滤离职人员排班数据
       AND (E.DIMISSION_DT IS NULL OR
           TO_CHAR(E.DIMISSION_DT, 'YYYYMMDD') > T.DAY_OF_MONTH)
          --过滤转岗前的数据
       AND (E.TRANSFER_DATE IS NULL OR
           T.DAY_OF_MONTH >= TO_CHAR(E.TRANSFER_DATE, 'YYYYMMDD'))
          --过滤入职前的数据
       AND T.DAY_OF_MONTH >= TO_CHAR(E.SF_DATE, 'YYYYMMDD')
       AND T.SCHEDULING_TYPE = '1'
       AND E.DATA_SOURCE = '2'
       AND E.EMP_POST_TYPE='5'
       AND T.CONFIGURE_CODE = '休'
    UNION ALL
    SELECT T.ID,
           T.EMPLOYEE_CODE,
           T.DAY_OF_MONTH,
           T.DAY_OF_MONTH,
           C.START_TIME ||'00' ,
           C.END_TIME ||'00' ,
           '',
           '',
           '2',
           SYSDATE,
           '',
           0,
           5
      FROM V_PUSH_DRIVER_SCHEDULING_DATA C,
           TT_DRIVER_SCHEDULING          T,
           TM_OSS_EMPLOYEE               E,
           TM_DEPARTMENT                 D
     WHERE T.CONFIGURE_CODE = C.CONFIGURE_CODE(+)
       AND T.YEAR_MONTH = C.MONTH(+)
       AND T.DEPARTMENT_CODE = D.DEPT_CODE
       AND D.DEPT_ID = E.DEPT_ID
       AND T.EMPLOYEE_CODE = E.EMP_CODE

       AND (T.SYNC_STATE != 1 OR T.SYNC_STATE IS NULL)
       AND T.DAY_OF_MONTH >= '20150901'
          -- 取40天以内的数据
       AND T.DAY_OF_MONTH >= TO_CHAR(TRUNC(SYSDATE - 40), 'YYYYMMDD')
       AND T.DAY_OF_MONTH <= TO_CHAR(SYSDATE, 'YYYYMMDD')

       AND (E.LAST_ZNO IS NULL OR
           T.DAY_OF_MONTH >= TO_CHAR(E.DATE_FROM, 'YYYYMMDD'))
          --过滤离职人员排班数据
       AND (E.DIMISSION_DT IS NULL OR
           TO_CHAR(E.DIMISSION_DT, 'YYYYMMDD') > T.DAY_OF_MONTH)
          --过滤转岗前的数据
       AND (E.TRANSFER_DATE IS NULL OR
           T.DAY_OF_MONTH >= TO_CHAR(E.TRANSFER_DATE, 'YYYYMMDD'))
          --过滤入职前的数据
       AND T.DAY_OF_MONTH >= TO_CHAR(E.SF_DATE, 'YYYYMMDD')
       AND T.SCHEDULING_TYPE = '1'
       AND E.DATA_SOURCE = '2'
       AND E.EMP_POST_TYPE='5'
       AND T.CONFIGURE_CODE <> '休';
       COMMIT;


    --删除存在的数据
    DELETE FROM TT_SAP_SYNCHRONOUS S
     WHERE EXISTS
     (SELECT 1
              FROM (SELECT EMP_CODE, BEGIN_DATE
                      FROM TT_SAP_SYNCHRONOUS_TMP4
                     GROUP BY EMP_CODE, BEGIN_DATE) TM
             WHERE TM.EMP_CODE = S.EMP_CODE
               AND TM.BEGIN_DATE = S.BEGIN_DATE）
            AND S.TMR_DAY_FLAG IS NULL;

    --非跨天插入数据
    INSERT INTO TT_SAP_SYNCHRONOUS
      (ID,
       EMP_CODE,
       BEGIN_DATE,
       END_DATE,
       BEGIN_TM,
       END_TM,
       TMR_DAY_FLAG,
       OFF_DUTY_FLAG,
       CLASS_SYSTEM,
       CREATE_TM,
       NODE_KEY,
       STATE_FLG,
       SCHEDULE_DAILY_ID,
       EMP_POST_TYPE)
      SELECT SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL,
             EMP_CODE,
             BEGIN_DATE,
             END_DATE,
             BEGIN_TM,
             END_TM,
             TMR_DAY_FLAG,
             OFF_DUTY_FLAG,
             CLASS_SYSTEM,
             CREATE_TM,
             NODE_KEY,
             STATE_FLG,
             S.ID,
             S.EMP_POST_TYPE
        FROM TT_SAP_SYNCHRONOUS_TMP4 S;
        COMMIT;
    --修改状态
    UPDATE TT_DRIVER_SCHEDULING D
       SET D.SYNC_STATE = 1
     WHERE D.ID IN
           (SELECT ID FROM TT_SAP_SYNCHRONOUS_TMP4 E);
    COMMIT;
    EXECUTE IMMEDIATE 'TRUNCATE TABLE TT_SAP_SYNCHRONOUS_TMP4';
    ----推送SAP数据--END
     END;
  END IF;
CHANGE_SAP_SYNCHRONIZATION;

  --4.结束记录日志
PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'DRIVER_SAP_SYNCHRONIZATION',
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
                                 'DRIVER_SAP_SYNCHRONIZATION',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
END DRIVER_SAP_SYNCHRONIZATION;
/
