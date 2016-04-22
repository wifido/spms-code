CREATE OR REPLACE PROCEDURE SCHEDULING_SAP_SYNCHRONIZATION AS
/**
  *每天23:00、00:40将一线排班推送至SAP接口表
**/
  KEYVALUE VARCHAR2(900);
  --1.定义执行序号
  L_CALL_NO NUMBER;
BEGIN

  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SCHEDULING_SAP_SYNCHRONIZATION',
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
   WHERE C.KEY_NAME = 'CD_SCH2SPMS_CLASS';
  --判断接口是否打开
  IF KEYVALUE = '1' THEN
    --将调度同步过来的排班数据同步到系统的排班表中
      BEGIN
       ----推送排班接口数据--begin
      --插入接口数据至临时表
      INSERT INTO TT_SCHEDULE_DAILY_TMP
        (ID,
         DEPARTMENT_CODE,
         BEGIN_TIME,
         END_TIME,
         DAY_OF_MONTH,
         MONTH_ID,
         EMPLOYEE_CODE,
         CREATED_EMPLOYEE_CODE,
         MODIFIED_EMPLOYEE_CODE,
         CREATE_TIME,
         MODIFIED_TIME,
         EMP_POST_TYPE,
         CROSS_DAY_TYPE,
         SYNCHRO_STATUS,
         SCHEDULING_CODE,
         DATA_SOURCE)
        SELECT T.ID,
               D.DEPT_CODE,
               T.BEGIN_TM,
               T.END_TM,
               REPLACE(TO_CHAR(T.BEGIN_DATE, 'YYYY-MM-DD'), '-', '') BEGIN_DATE,
               SUBSTR(REPLACE(TO_CHAR(T.BEGIN_DATE, 'YYYY-MM-DD'), '-', ''),
                      0,
                      6) AS DAYFORMONTH,
               T.EMP_CODE AS EMP_CODE,
               '',
               '',
               SYSDATE,
               SYSDATE,
               2,
               CASE
                 WHEN T.TMR_DAY_FLAG = 'X' AND
                      TO_NUMBER(T.BEGIN_TM) < TO_NUMBER(T.END_TM) THEN
                  T.TMR_DAY_FLAG
                 ELSE
                  ''
               END,
               NULL,
               NULL,
               '1'
          FROM TT_SCH_EMP_ATTENCE_CLASS T,
               TM_OSS_EMPLOYEE          E,
               TM_DEPARTMENT            D
         WHERE T.EMP_CODE = E.EMP_CODE
           AND E.DEPT_ID = D.DEPT_ID(+)
            --过滤转网排班数据
          -- AND (E.LAST_ZNO is null or TO_CHAR(T.BEGIN_DATE, 'YYYYMMDD') >= TO_CHAR(E.DATE_FROM, 'YYYYMMDD'))
             --过滤离职数据
           AND (E.DIMISSION_DT IS NULL OR TO_CHAR(T.BEGIN_DATE, 'YYYYMMDD') <   TO_CHAR(E.DIMISSION_DT, 'YYYYMMDD'))
             --过滤转岗前的数据
         --  AND (E.TRANSFER_DATE IS NULL OR TO_CHAR(T.BEGIN_DATE, 'YYYYMMDD') >= TO_CHAR(E.TRANSFER_DATE, 'YYYYMMDD'))

           AND (T.SYNCHRO_STATUS = 0 OR T.SYNCHRO_STATUS IS NULL);
      COMMIT;
      --删除已经存在数据
      DELETE FROM TT_SCHEDULE_DAILY D
       WHERE EXISTS (SELECT 1
                FROM (SELECT EMPLOYEE_CODE, DAY_OF_MONTH
                        FROM TT_SCHEDULE_DAILY_TMP
                       GROUP BY EMPLOYEE_CODE, DAY_OF_MONTH) CL
               WHERE CL.EMPLOYEE_CODE = D.EMPLOYEE_CODE
                 AND CL.DAY_OF_MONTH = D.DAY_OF_MONTH);
      --插入排班表
      INSERT INTO TT_SCHEDULE_DAILY
        (ID,
         DEPARTMENT_CODE,
         BEGIN_TIME,
         END_TIME,
         DAY_OF_MONTH,
         MONTH_ID,
         EMPLOYEE_CODE,
         CREATED_EMPLOYEE_CODE,
         MODIFIED_EMPLOYEE_CODE,
         CREATE_TIME,
         MODIFIED_TIME,
         EMP_POST_TYPE,
         CROSS_DAY_TYPE,
         SYNCHRO_STATUS,
         SCHEDULING_CODE,
         DATA_SOURCE)
        SELECT SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL,
               TMP.DEPARTMENT_CODE,
               TMP.BEGIN_TIME,
               TMP.END_TIME,
               TMP.DAY_OF_MONTH,
               TMP.MONTH_ID,
               TMP.EMPLOYEE_CODE,
               TMP.CREATED_EMPLOYEE_CODE,
               TMP.MODIFIED_EMPLOYEE_CODE,
               TMP.CREATE_TIME,
               TMP.MODIFIED_TIME,
               TMP.EMP_POST_TYPE,
               TMP.CROSS_DAY_TYPE,
               TMP.SYNCHRO_STATUS,
               TMP.SCHEDULING_CODE,
               TMP.DATA_SOURCE
          FROM TT_SCHEDULE_DAILY_TMP TMP;
      --更新状态
      UPDATE TT_SCH_EMP_ATTENCE_CLASS T
         SET T.SYNCHRO_STATUS = 1
       WHERE T.ID IN
             (SELECT ID FROM TT_SCHEDULE_DAILY_TMP E);
      COMMIT;
      EXECUTE IMMEDIATE 'TRUNCATE TABLE TT_SCHEDULE_DAILY_TMP';
    END;
    ----推送排班接口数据--end

    BEGIN
    ----推送SAP数据--begin
    --1.插入临时数据
    INSERT INTO TT_SAP_SYNCHRONOUS_TMP
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
             EMPLOYEE_CODE,
             DAY_OF_MONTH AS BEGIN_DATE,
             DAY_OF_MONTH AS END_DATE,
             decode(BEGIN_TIME,null,null,lpad(BEGIN_TIME,4,'0') ||'00') BEGIN_TIME,
             decode(END_TIME,null,null,lpad(END_TIME,4,'0') ||'00') END_TIME,
             CROSS_DAY_TYPE AS TMR_DAY_FLAG,
             DECODE(BEGIN_TIME, NULL, 'OFF', '') AS OFF_DUTY_FLAG,
             '2' AS CLASS_SYSTEM,
             SYSDATE,
             '',
             0 AS STATE_FLG,
             '2'
        FROM TT_SCHEDULE_DAILY T, TM_OSS_EMPLOYEE E,TM_DEPARTMENT D
       WHERE (T.SYNCHRO_STATUS != 1 OR T.SYNCHRO_STATUS IS NULL)
         AND T.EMP_POST_TYPE = '2'
         AND T.EMPLOYEE_CODE = E.EMP_CODE
            --过滤转网点数据
         AND T.DEPARTMENT_CODE = D.DEPT_CODE
         AND E.DEPT_ID = D.DEPT_ID
        -- AND (E.LAST_ZNO is null or T.DAY_OF_MONTH >= TO_CHAR(E.DATE_FROM, 'YYYYMMDD'))
            --过滤离职人员排班数据
         AND (E.DIMISSION_DT is null or TO_CHAR(E.DIMISSION_DT, 'YYYYMMDD') > t.DAY_OF_MONTH)
            --过滤转岗前的数据
        -- AND (E.TRANSFER_DATE is null or T.DAY_OF_MONTH >= TO_CHAR(E.TRANSFER_DATE, 'YYYYMMDD'))
             --过滤人员类型：外包、代理、个人经营承包者
         AND E.WORK_TYPE NOT IN (6, 8, 9)
            --过滤海外和港澳台
         AND T.DEPARTMENT_CODE NOT IN
             (SELECT DEPT_CODE
                FROM TM_DEPARTMENT
               START WITH DEPT_CODE IN ('CN06', 'CN07')
              CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE)
         AND T.DAY_OF_MONTH >= '20141201'
         AND T.DAY_OF_MONTH <= TO_CHAR(SYSDATE, 'YYYYMMDD')
         AND E.EMP_POST_TYPE = '2';
    COMMIT;

    --删除存在的数据
    DELETE FROM TT_SAP_SYNCHRONOUS S
     WHERE EXISTS
     (SELECT 1
              FROM (SELECT EMP_CODE, BEGIN_DATE
                      FROM TT_SAP_SYNCHRONOUS_TMP
                     GROUP BY EMP_CODE, BEGIN_DATE) TM
             WHERE TM.EMP_CODE = S.EMP_CODE
               AND TM.BEGIN_DATE = S.BEGIN_DATE）
    AND S.EMP_POST_TYPE = '2';

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
        FROM TT_SAP_SYNCHRONOUS_TMP S
       WHERE S.TMR_DAY_FLAG IS NULL;
  --跨天处理,拆分数据
  FOR SAP_ROW IN (SELECT * FROM TT_SAP_SYNCHRONOUS_TMP S
   WHERE S.TMR_DAY_FLAG = 'X') LOOP

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
        VALUES
          (SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL,
           SAP_ROW.EMP_CODE，
           SAP_ROW.BEGIN_DATE,
           SAP_ROW.BEGIN_DATE,
           SAP_ROW.BEGIN_TM,
           SAP_ROW.BEGIN_TM,
           '',
           SAP_ROW.OFF_DUTY_FLAG,
           SAP_ROW.CLASS_SYSTEM,
           SAP_ROW.CREATE_TM,
           SAP_ROW.NODE_KEY,
           SAP_ROW.STATE_FLG,
           SAP_ROW.ID,
           SAP_ROW.EMP_POST_TYPE);

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
        VALUES
          (SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL,
           SAP_ROW.EMP_CODE,
           TO_CHAR(TO_DATE(SAP_ROW.BEGIN_DATE, 'YYYYMMDD') + 1,
                   'YYYYMMDD'),
           TO_CHAR(TO_DATE(SAP_ROW.BEGIN_DATE, 'YYYYMMDD') + 1,
                   'YYYYMMDD'),
           SAP_ROW.BEGIN_TM,
           SAP_ROW.END_TM,
           SAP_ROW.TMR_DAY_FLAG,
           SAP_ROW.OFF_DUTY_FLAG,
           SAP_ROW.CLASS_SYSTEM,
           SAP_ROW.CREATE_TM,
           SAP_ROW.NODE_KEY,
           SAP_ROW.STATE_FLG,
           SAP_ROW.ID,
           SAP_ROW.EMP_POST_TYPE);
      END LOOP;
    --修改状态
    UPDATE TT_SCHEDULE_DAILY D
       SET D.SYNCHRO_STATUS = 1
     WHERE D.ID IN
           (SELECT ID FROM TT_SAP_SYNCHRONOUS_TMP E)
       AND EMP_POST_TYPE = '2';
    COMMIT;
    EXECUTE IMMEDIATE 'TRUNCATE TABLE TT_SAP_SYNCHRONOUS_TMP';
    ----推送SAP数据--end
    END;
  END IF;
  --4.结束记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SCHEDULING_SAP_SYNCHRONIZATION',
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
                                 'SCHEDULING_SAP_SYNCHRONIZATION',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
END SCHEDULING_SAP_SYNCHRONIZATION;
