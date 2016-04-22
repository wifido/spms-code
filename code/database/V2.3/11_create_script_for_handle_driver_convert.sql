----create view for query driver convert
CREATE OR REPLACE VIEW V_DRIVER_CONVERT AS
SELECT C."ID",C."VEHICLE_CODE",C."DRIVE_MEMBER",C."DRIVE_MILES",C."DRIVE_TM",C."DRIVE_SPAN",C."DRIVING_LOG_ITEM_ID",C."START_MILES",C."END_MILES",C."START_TM",C."END_TM",C."CREATED_EMP_CODE",C."CREATED_TM",C."MODIFIED_EMP_CODE",C."MODIFIED_TM",C."SYNC_FLAG",C."SYNC_TM",C."SAP_SYNC_FLAG"
  FROM TM_EMP_POST_CHANGE_RECORD R,
       TI_VMS_DRIVE_CONVERT C,
       TM_OSS_EMPLOYEE E,
       （SELECT C.DRIVE_MEMBER,
       MIN(R.CHANGE_DATE) CHANGE_DATE
  FROM TM_EMP_POST_CHANGE_RECORD R, TI_VMS_DRIVE_CONVERT C
 WHERE C.DRIVE_MEMBER = R.EMP_CODE
   AND C.START_TM <= R.CHANGE_DATE
 GROUP BY C.DRIVE_MEMBER）T
 WHERE C.DRIVE_MEMBER = R.EMP_CODE
   AND C.DRIVE_MEMBER = E.EMP_CODE
   AND R.EMP_CODE = T.DRIVE_MEMBER
   AND R.CHANGE_DATE = T.CHANGE_DATE
   AND E.WORK_TYPE <> 6
   AND R.PREVIOUS_POST = 5
UNION
SELECT C."ID",C."VEHICLE_CODE",C."DRIVE_MEMBER",C."DRIVE_MILES",C."DRIVE_TM",C."DRIVE_SPAN",C."DRIVING_LOG_ITEM_ID",C."START_MILES",C."END_MILES",C."START_TM",C."END_TM",C."CREATED_EMP_CODE",C."CREATED_TM",C."MODIFIED_EMP_CODE",C."MODIFIED_TM",C."SYNC_FLAG",C."SYNC_TM",C."SAP_SYNC_FLAG"
  FROM TI_VMS_DRIVE_CONVERT C, TM_OSS_EMPLOYEE E
 WHERE C.DRIVE_MEMBER = E.EMP_CODE
   AND E.WORK_TYPE <> 6
   AND E.EMP_POST_TYPE = 5;

   
----create view for query driver convert_bak
CREATE OR REPLACE VIEW V_DRIVER_CONVERT_BAK AS
SELECT C."ID",C."VEHICLE_CODE",C."DRIVE_MEMBER",C."DRIVE_MILES",C."DRIVE_TM",C."DRIVE_SPAN",C."DRIVING_LOG_ITEM_ID",C."START_MILES",C."END_MILES",C."START_TM",C."END_TM",C."CREATED_EMP_CODE",C."CREATED_TM",C."MODIFIED_EMP_CODE",C."MODIFIED_TM",C."DELETE_TM",C."SYS_TM",C."SYNC_FLAG"
  FROM TM_EMP_POST_CHANGE_RECORD R,
       TI_VMS_DRIVE_CONVERT_BAK C,
       TM_OSS_EMPLOYEE E,
       （SELECT C.DRIVE_MEMBER,
       MIN(R.CHANGE_DATE) CHANGE_DATE
  FROM TM_EMP_POST_CHANGE_RECORD R, TI_VMS_DRIVE_CONVERT_BAK C
 WHERE C.DRIVE_MEMBER = R.EMP_CODE
   AND C.START_TM <= R.CHANGE_DATE
 GROUP BY C.DRIVE_MEMBER）T
 WHERE C.DRIVE_MEMBER = R.EMP_CODE
   AND C.DRIVE_MEMBER = E.EMP_CODE
   AND R.EMP_CODE = T.DRIVE_MEMBER
   AND R.CHANGE_DATE = T.CHANGE_DATE
   AND E.WORK_TYPE <> 6
   AND R.PREVIOUS_POST = 5
UNION
SELECT C."ID",C."VEHICLE_CODE",C."DRIVE_MEMBER",C."DRIVE_MILES",C."DRIVE_TM",C."DRIVE_SPAN",C."DRIVING_LOG_ITEM_ID",C."START_MILES",C."END_MILES",C."START_TM",C."END_TM",C."CREATED_EMP_CODE",C."CREATED_TM",C."MODIFIED_EMP_CODE",C."MODIFIED_TM",C."DELETE_TM",C."SYS_TM",C."SYNC_FLAG"
  FROM TI_VMS_DRIVE_CONVERT_BAK C, TM_OSS_EMPLOYEE E
 WHERE C.DRIVE_MEMBER = E.EMP_CODE
   AND E.WORK_TYPE <> 6
   AND E.EMP_POST_TYPE = 5;
   
   

CREATE OR REPLACE PROCEDURE HANDLE_DRIVE_CONVER_DATA IS

  --*************************************************************
  -- AUTHOR  : HGX
  -- CREATED : 2015-07-07
  -- PURPOSE : 处理司机行车日志表(TI_VMS_DRIVE_CONVERT,TI_VMS_DRIVE_CONVERT_BAK)
  --           到同步行车日志接口表 TT_SAP_DRIVER_LOG
  --
  -- PARAMETER:
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************

  --1.定义执行序号
  L_CALL_NO NUMBER;

BEGIN

  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'HAND_DRIVE_CONVER_DATA',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

  ----处理新增行车日志
  FOR CONVERT_ROW IN (SELECT T.ID,
                             T.DRIVE_MEMBER,
                             T.START_TM,
                             T.END_TM,
                             T.SYNC_FLAG
                        FROM V_DRIVER_CONVERT T
                       WHERE T.SAP_SYNC_FLAG = 0) LOOP
  
    INSERT INTO TT_SAP_DRIVER_LOG
      (LOG_ID,
       PERNR,
       ZAUSW,
       LDATE,
       LTIME,
       LDAYT,
       ORIGF,
       ABWGR,
       PDC_USRUP,
       ZHRXGBZ,
       STATUS,
       SYNC_DATE,
       FAIL_DESC,
       CREATE_DATE)
    VALUES
      (SEQ_TT_SAP_DRIVER_LOG.NEXTVAL,
       CONVERT_ROW.DRIVE_MEMBER,
       CONVERT_ROW.DRIVE_MEMBER,
       TO_CHAR(CONVERT_ROW.START_TM, 'YYYYMMDD'),
       TO_CHAR(CONVERT_ROW.START_TM, 'HH24MISS'),
       TO_CHAR(CONVERT_ROW.START_TM, 'YYYYMMDDHH24MISS'),
       NULL,
       NULL,
       'C' || CONVERT_ROW.ID,
       'I',
       0,
       NULL,
       NULL,
       SYSDATE);
  
    INSERT INTO TT_SAP_DRIVER_LOG
      (LOG_ID,
       PERNR,
       ZAUSW,
       LDATE,
       LTIME,
       LDAYT,
       ORIGF,
       ABWGR,
       PDC_USRUP,
       ZHRXGBZ,
       STATUS,
       SYNC_DATE,
       FAIL_DESC,
       CREATE_DATE)
    VALUES
      (SEQ_TT_SAP_DRIVER_LOG.NEXTVAL,
       CONVERT_ROW.DRIVE_MEMBER,
       CONVERT_ROW.DRIVE_MEMBER,
       TO_CHAR(CONVERT_ROW.END_TM, 'YYYYMMDD'),
       TO_CHAR(CONVERT_ROW.END_TM, 'HH24MISS'),
       TO_CHAR(CONVERT_ROW.END_TM, 'YYYYMMDDHH24MISS'),
       NULL,
       NULL,
       'C' || CONVERT_ROW.ID,
       'I',
       0,
       NULL,
       NULL,
       SYSDATE);
  
    UPDATE TI_VMS_DRIVE_CONVERT T
       SET T.SAP_SYNC_FLAG = 1
     WHERE T.ID = CONVERT_ROW.ID;
  
  END LOOP;

  COMMIT;

  ----处理删除行车日志
  FOR CONVERT_ROW IN (SELECT T.ID,
                             T.DRIVE_MEMBER,
                             T.START_TM,
                             T.END_TM,
                             T.SYNC_FLAG
                        FROM V_DRIVER_CONVERT_BAK T
                       WHERE T.SYNC_FLAG = 0) LOOP
  
    INSERT INTO TT_SAP_DRIVER_LOG
      (LOG_ID,
       PERNR,
       ZAUSW,
       LDATE,
       LTIME,
       LDAYT,
       ORIGF,
       ABWGR,
       PDC_USRUP,
       ZHRXGBZ,
       STATUS,
       SYNC_DATE,
       FAIL_DESC,
       CREATE_DATE)
    VALUES
      (SEQ_TT_SAP_DRIVER_LOG.NEXTVAL,
       CONVERT_ROW.DRIVE_MEMBER,
       CONVERT_ROW.DRIVE_MEMBER,
       TO_CHAR(CONVERT_ROW.START_TM, 'YYYYMMDD'),
       TO_CHAR(CONVERT_ROW.START_TM, 'HH24MISS'),
       TO_CHAR(CONVERT_ROW.START_TM, 'YYYYMMDDHH24MISS'),
       NULL,
       NULL,
       'C' || CONVERT_ROW.ID,
       'D',
       0,
       NULL,
       NULL,
       SYSDATE);
  
    INSERT INTO TT_SAP_DRIVER_LOG
      (LOG_ID,
       PERNR,
       ZAUSW,
       LDATE,
       LTIME,
       LDAYT,
       ORIGF,
       ABWGR,
       PDC_USRUP,
       ZHRXGBZ,
       STATUS,
       SYNC_DATE,
       FAIL_DESC,
       CREATE_DATE)
    VALUES
      (SEQ_TT_SAP_DRIVER_LOG.NEXTVAL,
       CONVERT_ROW.DRIVE_MEMBER,
       CONVERT_ROW.DRIVE_MEMBER,
       TO_CHAR(CONVERT_ROW.END_TM, 'YYYYMMDD'),
       TO_CHAR(CONVERT_ROW.END_TM, 'HH24MISS'),
       TO_CHAR(CONVERT_ROW.END_TM, 'YYYYMMDDHH24MISS'),
       NULL,
       NULL,
       'C' || CONVERT_ROW.ID,
       'D',
       0,
       NULL,
       NULL,
       SYSDATE);
  
    UPDATE TI_VMS_DRIVE_CONVERT_BAK T
       SET T.SYNC_FLAG = 1
     WHERE T.ID = CONVERT_ROW.ID;
  
  END LOOP;

  COMMIT;

  --4 结束记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'HAND_DRIVE_CONVER_DATA',
                               SYSDATE,
                               NULL,
                               NULL,
                               'END',
                               0,
                               L_CALL_NO);
EXCEPTION
  WHEN OTHERS THEN
    --回滚数据
    ROLLBACK;
    PKG_OSS_COMM.STP_RUNNING_LOG('',
                                 'HAND_DRIVE_CONVER_DATA',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
  
END HANDLE_DRIVE_CONVER_DATA;
/


DECLARE
  jobNumber NUMBER;
BEGIN
  SYS.DBMS_JOB.SUBMIT(jobNumber,
                      '/**每天00:00处理司机行车日志数据到SAP行车日志接口表**/HANDLE_DRIVE_CONVER_DATA;',
                      SYSDATE,
                      'TRUNC(SYSDATE+1)+0/24');
  COMMIT;
END;
/


