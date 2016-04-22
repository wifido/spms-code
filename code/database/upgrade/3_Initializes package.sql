-----------------------------------------------
-- Export file for user PUSHPN               --
-- Created by sfit0505 on 2014/9/15, 9:31:11 --
-----------------------------------------------

spool Initializes package.log

prompt
prompt Creating package PKG_OSS_COMM
prompt =============================
prompt
CREATE OR REPLACE PACKAGE PKG_OSS_COMM IS

  -- AUTHOR  : ZCJ
  -- CREATED : 2014-07-17
  -- PURPOSE : 公共包



---日志的标准化
  PROCEDURE STP_RUNNING_LOG(
                            P_PACKAGE_NAME VARCHAR2,
                            P_PROC_NAME    VARCHAR2,
                            P_EXCEP_DT     DATE,
                            P_EXCEP_CODE   VARCHAR2,
                            P_EXCEP_DESC   VARCHAR2,
                            P_EXCEP_REMK   VARCHAR2,
                            P_LINE_NO      NUMBER,
                            P_CALL_NO      NUMBER);

END PKG_OSS_COMM;
/

prompt
prompt Creating package PKG_OSS_INTERFACE_PROCESS
prompt ==========================================
prompt
CREATE OR REPLACE PACKAGE PKG_OSS_INTERFACE_PROCESS IS

  -- AUTHOR  : BP
  -- CREATED : 2014-7-17
  -- PURPOSE : 与Oracle Hr 数据同步接口处理

  --对接收到的数据进行全量新增或处理
  PROCEDURE STP_OSS_HR_ALL_OR_UPDATE(P_DATA_TYPE VARCHAR2,
                                     P_JOURANL   VARCHAR2);

END PKG_OSS_INTERFACE_PROCESS;
/

prompt
prompt Creating package PKG_OSS_SCHE_PROCESS
prompt =====================================
prompt
CREATE OR REPLACE PACKAGE PKG_OSS_SCHE_PROCESS IS

  -- Author  : houjingyu
  -- Created : 2014-6-19
  --*************************************************************
  TYPE CURSOR_TYPE IS REF CURSOR;
  --获取表中有效的排班数据
  PROCEDURE SPT_GET_VALID_SCHE_LIST(P_DEPTID     NUMBER,
                                    P_YM         VARCHAR2,
                                    P_EMP_COCE   VARCHAR2,
                                    P_OUT_CURSOR OUT CURSOR_TYPE);
  --获取未完成的各网点拥有排班权限的用户
  PROCEDURE SPT_GET_SCHE_UNFIN_EMP_LIST(P_YM         VARCHAR2,
                                        P_OUT_CURSOR OUT CURSOR_TYPE);
  --获取某网点下某月是否所有员工都有排班
  PROCEDURE SPT_ALL_EMP_IS_HAS_SCHE(P_YM     VARCHAR2,
                                    P_DEPTID NUMBER,
                                    P_OUT_CT OUT NUMBER);
  --获取有排班记录的员工是否排满班
  PROCEDURE SPT_ALL_SCHE_IS_FINISHED(P_YM     VARCHAR2,
                                     P_DEPTID NUMBER,
                                     P_OUT_CT OUT NUMBER);

  --获取未完成的各网点拥有排工序权限的用户
  PROCEDURE SPT_GET_PROCESS_UNFIN_EMP_LIST(P_YM         VARCHAR2,
                                           P_OUT_CURSOR OUT CURSOR_TYPE);
  --获取某网点下某月是否所有员工都有排工序
  PROCEDURE SPT_ALL_SCHE_IS_HAS_PROCESS(P_YM     VARCHAR2,
                                        P_DEPTID NUMBER,
                                        P_OUT_CT OUT NUMBER);

END PKG_OSS_SCHE_PROCESS;
/

prompt
prompt Creating package body PKG_OSS_COMM
prompt ==================================
prompt
CREATE OR REPLACE PACKAGE BODY PKG_OSS_COMM IS



---日志的标准化
  PROCEDURE STP_RUNNING_LOG(
                            P_PACKAGE_NAME VARCHAR2,
                            P_PROC_NAME    VARCHAR2,
                            P_EXCEP_DT     DATE,
                            P_EXCEP_CODE   VARCHAR2,
                            P_EXCEP_DESC   VARCHAR2,
                            P_EXCEP_REMK   VARCHAR2,
                            P_LINE_NO      NUMBER,
                            P_CALL_NO NUMBER) AS
    PRAGMA AUTONOMOUS_TRANSACTION;
  BEGIN
    INSERT INTO TL_EXCEPTION_LOG
      (SEQ_NO,
       PACKAGE_NAME,
       PROCEDURE_NAME,
       EXCEPTION_TM,
       EXCEPTION_CODE,
       EXCEPTION_DESC,
       EXCEPTION_REMK,
       LINE_NO,
       CALL_SNO)
    VALUES
      (SEQ_OSS_TL.Nextval,
       SUBSTR(P_PACKAGE_NAME, 1, 120),
       SUBSTR(P_PROC_NAME, 1, 120),
       P_EXCEP_DT,
       SUBSTR(P_EXCEP_CODE, 1, 60),
       SUBSTR(P_EXCEP_DESC, 1, 1000),
       SUBSTR(P_EXCEP_REMK, 1, 600),
       P_LINE_NO,
       P_CALL_NO);

    COMMIT;

  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('STP_RUNNING_LOG' || SQLCODE || '：' || SQLERRM);
      ROLLBACK;
  END STP_RUNNING_LOG;

END PKG_OSS_COMM;
/

prompt
prompt Creating package body PKG_OSS_INTERFACE_PROCESS
prompt ===============================================
prompt
CREATE OR REPLACE PACKAGE BODY PKG_OSS_INTERFACE_PROCESS IS
  PROCEDURE STP_OSS_HR_ALL_OR_UPDATE(P_DATA_TYPE VARCHAR2,
                                     P_JOURANL   VARCHAR2) AS
    V_COUNT             INT;
    V_DEPT_ID           NUMBER;
    V_INNER_MAX_EMPCODE INT := 100000000;
    L_CALL_NO           NUMBER;
    V_ERROR             VARCHAR2(1000);
    V_OLD_DEPT_ID       NUMBER;
    V_EMP_COUNT             INT;
  BEGIN
    SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
    --记录开始日期
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_OSS_INTERFACE_PROCESS',
                                 'STP_OSS_HR_ALL_OR_UPDATE',
                                 SYSDATE,
                                 NULL,
                                 NULL,
                                 'START',
                                 0,
                                 L_CALL_NO);
    SELECT COUNT(*)
      INTO V_COUNT
      FROM TS_OSS_ESB_BIG_FILE_RESEND
     WHERE DATA_TYPE = P_DATA_TYPE
       AND JOURNAL_ID = P_JOURANL;

    IF V_COUNT > 0 THEN
      ------如果是全量更新，只取对应包裹处理序列的值到tm_oss_employee表
      -----默认新增的都是有效的员工
      IF P_DATA_TYPE = 'HRS_EMP_INIT' THEN

        Loop
           SELECT COUNT(1) INTO V_EMP_COUNT FROM (
           SELECT emp_code FROM TI_OSS_HR_EMP_INFO t
        WHERE t.journal_id = P_JOURANL and
              t.duty_serial = '包裹处理序列'
              GROUP BY emp_code HAVING COUNT(1) > 1);

        IF V_EMP_COUNT > 0 THEN
         DELETE ti_oss_hr_emp_info WHERE  EMP_ID IN  (SELECT MIN(EMP_ID) FROM  ti_oss_hr_emp_info t
          WHERE t.journal_id = P_JOURANL and
                t.duty_serial = '包裹处理序列'
                GROUP BY emp_code HAVING COUNT(1) > 1);

           COMMIT;
         ELSE
           EXIT;
         END IF ;
        END LOOP;


        DELETE TM_OSS_EMPLOYEE E
         WHERE TO_NUMBER(E.EMP_CODE) < V_INNER_MAX_EMPCODE;

        INSERT INTO TM_OSS_EMPLOYEE
          (EMP_ID,
           EMP_CODE,
           EMP_NAME,
           EMP_DUTY_NAME,
           DEPT_ID,
           WORK_TYPE,
           EMAIL,
           SF_DATE,
           CREATE_EMP_CODE,
           CREATE_TM)
          SELECT SEQ_OSS_BASE.NEXTVAL,
                 T.EMP_CODE,
                 T.EMP_NAME,
                 T.POSITION_NAME,
                 DEPT.DEPT_ID,
                 DECODE(T.PERSON_TYPE,
                        '非全日制工',
                        1,
                        '基地见习生',
                        2,
                        '劳务派遣',
                        3,
                        '全日制员工',
                        4,
                        '实习生',
                        5,
                        '外包',
                        6,                        
                         '勤工助学',
                        7,
                         '代理',
                        8,
                         '个人承包经营者',
                        9,
                         '业务外包',
                        10,
                        0),
                 T.EMP_EMAIL,
                 TO_DATE(T.SF_DATE, 'yyyy-mm-dd'),
                 'ossinteface',
                 SYSDATE
            FROM TI_OSS_HR_EMP_INFO T
           INNER JOIN TM_DEPARTMENT DEPT
              ON T.DEPT_CODE = DEPT.DEPT_CODE
              WHERE T.JOURNAL_ID =P_JOURANL
                               AND T.DUTY_SERIAL = '包裹处理序列';
        COMMIT;
      END IF;

      -------如果是增量 通过游标遍历员工表是否存在，存在则新增，不存在则新增
      IF P_DATA_TYPE = 'EMP_ONE' THEN
        BEGIN

          FOR EMP_ROW IN (SELECT *
                            FROM TI_OSS_HR_EMP_INFO_ALTER HR
                           WHERE HR.JOURNAL_ID = P_JOURANL
                             AND HR.DUTY_SERIAL = '包裹处理序列') LOOP
            BEGIN
              SELECT T.DEPT_ID
                INTO V_DEPT_ID
                FROM TM_DEPARTMENT T
               WHERE T.DEPT_CODE = EMP_ROW.DEPT_CODE;

              UPDATE TM_OSS_EMPLOYEE EE
                 SET EE.EMP_NAME = EMP_ROW.EMP_NAME,
                     -----网点此次不直接修改，可能会是转网点
                     ----- ee.dept_id           = v_dept_id,
                     EE.EMP_DUTY_NAME     = EMP_ROW.POSITION_NAME,
                     EE.WORK_TYPE         = DECODE(EMP_ROW.PERSON_TYPE,
                                                  '非全日制工',
                                                  1,
                                                  '基地见习生',
                                                  2,
                                                  '劳务派遣',
                                                  3,
                                                  '全日制员工',
                                                  4,
                                                  '实习生',
                                                  5,
                                                  '外包',
                                                  6,                        
                                                   '勤工助学',
                                                  7,
                                                   '代理',
                                                  8,
                                                   '个人承包经营者',
                                                  9,
                                                   '业务外包',
                                                  10,
                                                  0),
                     EE.EMAIL             = EMP_ROW.EMP_EMAIL,
                     EE.DIMISSION_DT      = TO_DATE(EMP_ROW.CANCEL_DATE,
                                                    'yyyy-mm-dd'),
                     EE.SF_DATE           = TO_DATE(EMP_ROW.SF_DATE,
                                                    'yyyy-mm-dd'),
                     EE.MODIFIED_EMP_CODE = 'ossinterface',
                     EE.MODIFIED_TM       = SYSDATE
               WHERE EE.EMP_CODE = EMP_ROW.EMP_CODE;
              ----不存在则新增
              IF SQL%ROWCOUNT = 0 THEN
                ---------添加新增人员入驻供提醒
                INSERT INTO TI_OSS_HR_EMP_NEW_CHANGEDEPT
                  (EMP_ID,
                   EMP_CODE,
                   EMP_DUTY_NAME,
                   EMP_TYPE_NAME,
                   EMP_NAME,
                   EMP_GENDER,
                   EMP_EMAIL,
                   EMP_MOBILE,
                   EMP_OFFICEPHONE,
                   EMP_STUS,
                   REGISTER_DT,
                   LOGOUT_DT,
                   EMP_DESC,
                   VALID_FLG,
                   DEPT_CODE,
                   CHANGE_ZONE_TM,
                   INNER_FLG,
                   PERSON_TYPE,
                   POSITION_NAME,
                   POSITION_STATUS,
                   CREATED_EMP_CODE,
                   CREATED_TM,
                   MODIFIED_EMP_CODE,
                   MODIFIED_TM,
                   HRS_EMP_ID,
                   CANCEL_DATE,
                   SF_DATE,
                   CANCEL_FLAG,
                   DEAL_FLAG,
                   INTERFACE_ID)
                VALUES
                  (SEQ_OSS_TL.NEXTVAL,
                   EMP_ROW.EMP_CODE,
                   EMP_ROW.EMP_DUTY_NAME,
                   EMP_ROW.EMP_TYPE_NAME,
                   EMP_ROW.EMP_NAME,
                   EMP_ROW.EMP_GENDER,
                   EMP_ROW.EMP_EMAIL,
                   EMP_ROW.EMP_MOBILE,
                   EMP_ROW.EMP_OFFICEPHONE,
                   1,
                   EMP_ROW.REGISTER_DT,
                   EMP_ROW.LOGOUT_DT,
                   EMP_ROW.EMP_DESC,
                   EMP_ROW.VALID_FLG,
                   EMP_ROW.DEPT_CODE,
                   EMP_ROW.CHANGE_ZONE_TM,
                   EMP_ROW.INNER_FLG,
                   EMP_ROW.PERSON_TYPE,
                   EMP_ROW.POSITION_NAME,
                   EMP_ROW.POSITION_STATUS,
                   EMP_ROW.CREATED_EMP_CODE,
                   SYSDATE,
                   EMP_ROW.MODIFIED_EMP_CODE,
                   EMP_ROW.MODIFIED_TM,
                   EMP_ROW.HRS_EMP_ID,
                   EMP_ROW.CANCEL_DATE,
                   EMP_ROW.SF_DATE,
                   EMP_ROW.CANCEL_FLAG,
                   0,
                   EMP_ROW.EMP_ALTER_ID

                   );
              ELSE
                -----判断是否转网点
                SELECT EE.DEPT_ID
                  INTO V_OLD_DEPT_ID
                  FROM TM_OSS_EMPLOYEE EE
                 WHERE EE.EMP_CODE = EMP_ROW.EMP_CODE;
                IF V_OLD_DEPT_ID <> V_DEPT_ID THEN
                  INSERT INTO TI_OSS_HR_EMP_NEW_CHANGEDEPT
                    (EMP_ID,
                     EMP_CODE,
                     EMP_DUTY_NAME,
                     EMP_TYPE_NAME,
                     EMP_NAME,
                     EMP_GENDER,
                     EMP_EMAIL,
                     EMP_MOBILE,
                     EMP_OFFICEPHONE,
                     EMP_STUS,
                     REGISTER_DT,
                     LOGOUT_DT,
                     EMP_DESC,
                     VALID_FLG,
                     DEPT_CODE,
                     CHANGE_ZONE_TM,
                     INNER_FLG,
                     PERSON_TYPE,
                     POSITION_NAME,
                     POSITION_STATUS,
                     CREATED_EMP_CODE,
                     CREATED_TM,
                     MODIFIED_EMP_CODE,
                     MODIFIED_TM,
                     HRS_EMP_ID,
                     CANCEL_DATE,
                     SF_DATE,
                     CANCEL_FLAG,
                     DEAL_FLAG,
                     INTERFACE_ID)
                  VALUES
                    (SEQ_OSS_TL.NEXTVAL,
                     EMP_ROW.EMP_CODE,
                     EMP_ROW.EMP_DUTY_NAME,
                     EMP_ROW.EMP_TYPE_NAME,
                     EMP_ROW.EMP_NAME,
                     EMP_ROW.EMP_GENDER,
                     EMP_ROW.EMP_EMAIL,
                     EMP_ROW.EMP_MOBILE,
                     EMP_ROW.EMP_OFFICEPHONE,
                     2,
                     EMP_ROW.REGISTER_DT,
                     EMP_ROW.LOGOUT_DT,
                     EMP_ROW.EMP_DESC,
                     EMP_ROW.VALID_FLG,
                     EMP_ROW.DEPT_CODE,
                     EMP_ROW.CHANGE_ZONE_TM,
                     EMP_ROW.INNER_FLG,
                     EMP_ROW.PERSON_TYPE,
                     EMP_ROW.POSITION_NAME,
                     EMP_ROW.POSITION_STATUS,
                     EMP_ROW.CREATED_EMP_CODE,
                     SYSDATE,
                     EMP_ROW.MODIFIED_EMP_CODE,
                     EMP_ROW.MODIFIED_TM,
                     EMP_ROW.HRS_EMP_ID,
                     EMP_ROW.CANCEL_DATE,
                     EMP_ROW.SF_DATE,
                     EMP_ROW.CANCEL_FLAG,
                     0,
                     EMP_ROW.EMP_ALTER_ID

                     );
                END IF;

              END IF;
              ---------添加新增或转网点记录

              COMMIT;
              ----循环内异常处理
            EXCEPTION
              WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('STP_OSS_HR_ALL_OR_UPDATE' || SQLCODE || '：' ||
                                     SQLERRM);
                ROLLBACK;
            END;

          END LOOP;
        END;
      END IF;

    END IF;
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_OSS_INTERFACE_PROCESS',
                                 'STP_OSS_HR_ALL_OR_UPDATE',
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
      PKG_OSS_COMM.STP_RUNNING_LOG('PKG_OSS_INTERFACE_PROCESS',
                                   'STP_OSS_HR_ALL_OR_UPDATE',
                                   SYSDATE,
                                   SQLCODE,
                                   SQLERRM,
                                   'ERROR',
                                   0,
                                   L_CALL_NO);
  END STP_OSS_HR_ALL_OR_UPDATE;

END PKG_OSS_INTERFACE_PROCESS;
/

prompt
prompt Creating package body PKG_OSS_SCHE_PROCESS
prompt ==========================================
prompt
CREATE OR REPLACE PACKAGE BODY PKG_OSS_SCHE_PROCESS IS
  --获取表中有效的排班数据
  PROCEDURE SPT_GET_VALID_SCHE_LIST(P_DEPTID     NUMBER,
                                    P_YM         VARCHAR2,
                                    P_EMP_COCE   VARCHAR2,
                                    P_OUT_CURSOR OUT CURSOR_TYPE) IS
  BEGIN
    OPEN P_OUT_CURSOR FOR
      SELECT A.ID,
             A.EMP_CODE,
             A.SHEDULE_DT,
             A.DEPT_ID,
             A.SHEDULE_ID,
             A.SHEDULE_CODE,
             A.SHEDULE_MON_ID
        FROM TT_PB_SHEDULE_BY_DAY A,
             (SELECT DEPT_ID,
                     EMP_CODE,
                     YM,
                     SUBSTR(BETWEEN_DATE, 0, 10) START_DATE,
                     SUBSTR(BETWEEN_DATE, 12, 10) END_DATE,
                     SUBSTR(BETWEEN_DATE, 23, LENGTH(BETWEEN_DATE)) NEED_DAYS
                FROM (SELECT DEPT_ID,
                             EMP_CODE,
                             YM,
                             MAX(CASE
                                   WHEN START_TM IS NULL AND END_TM IS NULL AND
                                        DIMISSION_DT_TEMP <= FIRST_DAY THEN
                                    TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||
                                    TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' || '0'
                                   WHEN START_TM IS NULL AND END_TM IS NULL AND
                                        DIMISSION_DT_TEMP > FIRST_DAY AND
                                        DIMISSION_DT_TEMP <= LAST_DAY THEN
                                    TO_CHAR(FIRST_DAY, 'YYYY-MM-DD') || '~' ||
                                    TO_CHAR(DIMISSION_DT_TEMP - 1, 'YYYY-MM-DD') || '~' ||
                                    TO_CHAR(TO_NUMBER(TO_CHAR(DIMISSION_DT_TEMP,
                                                              'DD')) -
                                            TO_NUMBER(TO_CHAR(FIRST_DAY, 'DD')))
                                   WHEN START_TM IS NULL AND END_TM IS NULL AND
                                        DIMISSION_DT_TEMP > LAST_DAY THEN
                                    TO_CHAR(FIRST_DAY, 'YYYY-MM-DD') || '~' ||
                                    TO_CHAR(LAST_DAY, 'YYYY-MM-DD') || '~' ||
                                    TO_CHAR(LAST_DAY, 'DD')
                                   WHEN START_TM IS NOT NULL AND
                                        DIMISSION_DT_TEMP > FIRST_DAY AND
                                        END_TM > FIRST_DAY THEN
                                    TO_CHAR(GREATEST(FIRST_DAY, START_TM),
                                            'YYYY-MM-DD') || '~' ||
                                    TO_CHAR(DECODE(LEAST(LAST_DAY,
                                                         DIMISSION_DT_TEMP,
                                                         END_TM),
                                                   LAST_DAY,
                                                   LAST_DAY,
                                                   LEAST(LAST_DAY,
                                                         DIMISSION_DT_TEMP,
                                                         END_TM) - 1),
                                            'DD') || '~' ||
                                    DECODE(LEAST(LAST_DAY,
                                                 DIMISSION_DT_TEMP,
                                                 END_TM),
                                           LAST_DAY,
                                           TO_CHAR(TO_NUMBER(TO_CHAR(LEAST(LAST_DAY,
                                                                           DIMISSION_DT_TEMP,
                                                                           END_TM),
                                                                     'DD')) -
                                                   TO_NUMBER(TO_CHAR(GREATEST(FIRST_DAY,
                                                                              START_TM),
                                                                     'DD')) + 1),
                                           TO_CHAR(TO_NUMBER(TO_CHAR(LEAST(LAST_DAY,
                                                                           DIMISSION_DT_TEMP,
                                                                           END_TM),
                                                                     'DD')) -
                                                   TO_NUMBER(TO_CHAR(GREATEST(FIRST_DAY,
                                                                              START_TM),
                                                                     'DD'))))

                                   WHEN START_TM IS NOT NULL AND
                                        DIMISSION_DT_TEMP > LAST_DAY AND
                                        END_TM > LAST_DAY THEN
                                    TO_CHAR(GREATEST(FIRST_DAY, START_TM), 'DD') || '~' ||
                                    TO_CHAR(LAST_DAY, 'DD') || '~' ||
                                    TO_CHAR(TO_NUMBER(TO_CHAR(LAST_DAY, 'DD')) -
                                            TO_NUMBER(TO_CHAR(GREATEST(FIRST_DAY,
                                                                       START_TM),
                                                              'DD')) + 1)

                                   WHEN START_TM IS NOT NULL AND
                                        END_TM IS NOT NULL AND END_TM <= FIRST_DAY THEN
                                    TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||
                                    TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' || '0'

                                   WHEN START_TM IS NOT NULL AND
                                        END_TM IS NOT NULL AND
                                        DIMISSION_DT_TEMP <= FIRST_DAY THEN
                                    TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||
                                    TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' || '0'

                                   WHEN START_TM IS NOT NULL AND
                                        DIMISSION_DT_TEMP <= START_TM THEN
                                    TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||
                                    TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' || '0'

                                   WHEN START_TM IS NOT NULL AND
                                        LAST_DAY <= START_TM AND
                                        START_TM <= DIMISSION_DT_TEMP THEN
                                    TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||
                                    TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' || '0'
                                 END) BETWEEN_DATE
                        FROM (SELECT EM.DEPT_ID,
                                     MON.YM YM,
                                     EM.EMP_CODE,
                                     EM.EMP_NAME,
                                     EM.WORK_TYPE,
                                     EM.GROUP_ID,
                                     EM.DIMISSION_DT,
                                     CHG.CHANGE_DEPT_ID,
                                     START_TM,
                                     END_TM,
                                     TRUNC(TO_DATE(MON.YM || '-01', 'YYYY-MM-DD'),
                                           'MM') FIRST_DAY,
                                     NVL(EM.DIMISSION_DT,
                                         TO_DATE('2114-01-01', 'YYYY-MM-DD')) DIMISSION_DT_TEMP,
                                     LAST_DAY(TO_DATE(MON.YM || '-01',
                                                      'YYYY-MM-DD')) LAST_DAY
                                FROM TM_OSS_EMPLOYEE EM,
                                     TM_PB_GROUP_INFO G,
                                     (SELECT CHANGE_DEPT_ID,
                                             EMP_CODE,
                                             CHANGE_DEPT_CODE,
                                             START_TM,
                                             NVL(END_TM,
                                                 TO_DATE('2114-01-01',
                                                         'YYYY-MM-DD')) END_TM
                                        FROM (SELECT DEP.DEPT_ID CHANGE_DEPT_ID,
                                                     CH.EMP_CODE,
                                                     CH.DEPT_CODE CHANGE_DEPT_CODE,
                                                     CH.CHANGE_ZONE_TM START_TM,
                                                     LEAD(CH.CHANGE_ZONE_TM) OVER(PARTITION BY CH.EMP_CODE ORDER BY CH.CHANGE_ZONE_TM) END_TM
                                                FROM TI_OSS_HR_EMP_NEW_CHANGEDEPT CH,
                                                     TM_DEPARTMENT                DEP
                                               WHERE CH.DEPT_CODE =
                                                     DEP.DEPT_CODE
                                                 AND CH.EMP_STUS = 2)) CHG,
                                     TT_PB_SHEDULE_BY_MONTH MON,
                                     TT_PB_SHEDULE_BY_DAY D
                               WHERE EM.GROUP_ID = G.GROUP_ID
                                 AND EM.EMP_CODE = MON.EMP_CODE
                                 AND MON.ID = D.SHEDULE_MON_ID
                                 AND MON.EMP_CODE = CHG.EMP_CODE(+)
                                 AND MON.DEPT_ID = CHG.CHANGE_DEPT_ID(+)
                                 AND MON.EMP_CODE = CHG.EMP_CODE(+)
                                 AND EM.DEPT_ID = P_DEPTID
                                 AND EM.EMP_CODE = P_EMP_COCE
                                 AND MON.YM = P_YM)
                       GROUP BY EMP_CODE, DEPT_ID, YM)) B
       WHERE A.DEPT_ID = B.DEPT_ID
         AND A.EMP_CODE = B.EMP_CODE
         AND TO_CHAR(A.SHEDULE_DT, 'YYYY-MM') = B.YM
         AND A.SHEDULE_DT >= TO_DATE(START_DATE, 'YYYY-MM-DD')
         AND A.SHEDULE_DT <= TO_DATE(END_DATE, 'YYYY-MM-DD');

  END;
  --获取未完成的网点
  PROCEDURE SPT_GET_SCHE_UNFIN_EMP_LIST(P_YM         VARCHAR2,
                                        P_OUT_CURSOR OUT CURSOR_TYPE) IS
  BEGIN
    OPEN P_OUT_CURSOR FOR
      SELECT *
        FROM (
        WITH UN_FINISH_EMP_DEPTID AS
        (
            SELECT DEPT_ID
            FROM (SELECT DEPT_ID,
                         YM,
                         (CASE
                           WHEN START_TM IS NULL AND
                                END_TM IS NULL AND
                                DIMISSION_DT_TEMP <=
                                FIRST_DAY THEN
                            0
                           WHEN START_TM IS NULL AND
                                END_TM IS NULL AND
                                DIMISSION_DT_TEMP >
                                FIRST_DAY AND
                                DIMISSION_DT_TEMP <=
                                LAST_DAY THEN
                            TO_NUMBER(TO_CHAR(DIMISSION_DT_TEMP,
                                              'DD')) -
                            TO_NUMBER(TO_CHAR(FIRST_DAY,
                                              'DD'))
                           WHEN START_TM IS NULL AND
                                END_TM IS NULL AND
                                DIMISSION_DT_TEMP >
                                LAST_DAY THEN
                            TO_NUMBER(TO_CHAR(LAST_DAY,
                                              'DD'))

                           WHEN START_TM IS NOT NULL AND
                                DIMISSION_DT_TEMP >
                                FIRST_DAY AND
                                END_TM >
                                FIRST_DAY THEN
                            DECODE(LEAST(LAST_DAY,
                                         DIMISSION_DT_TEMP,
                                         END_TM),
                                   LAST_DAY,
                                   TO_NUMBER(TO_CHAR(LEAST(LAST_DAY,
                                                           DIMISSION_DT_TEMP,
                                                           END_TM),
                                                     'DD')) -
                                   TO_NUMBER(TO_CHAR(GREATEST(FIRST_DAY,
                                                              START_TM),
                                                     'DD')) + 1,
                                   TO_NUMBER(TO_CHAR(LEAST(LAST_DAY,
                                                           DIMISSION_DT_TEMP,
                                                           END_TM),
                                                     'DD')) -
                                   TO_NUMBER(TO_CHAR(GREATEST(FIRST_DAY,
                                                              START_TM),
                                                     'DD')))

                           WHEN START_TM IS NOT NULL AND
                                DIMISSION_DT_TEMP >
                                LAST_DAY AND
                                END_TM >
                                LAST_DAY THEN
                            TO_NUMBER(TO_CHAR(LAST_DAY,
                                              'DD')) -
                            TO_NUMBER(TO_CHAR(GREATEST(FIRST_DAY,
                                                       START_TM),
                                              'DD')) + 1

                           WHEN START_TM IS NOT NULL AND
                                END_TM IS NOT NULL AND
                                END_TM <=
                                FIRST_DAY THEN
                            0

                           WHEN START_TM IS NOT NULL AND
                                END_TM IS NOT NULL AND
                                DIMISSION_DT_TEMP <=
                                FIRST_DAY THEN
                            0

                           WHEN START_TM IS NOT NULL AND
                                DIMISSION_DT_TEMP <=
                                START_TM THEN
                            0

                           WHEN START_TM IS NOT NULL AND
                                LAST_DAY <=
                                START_TM AND
                                START_TM <=
                                DIMISSION_DT_TEMP THEN
                            0
                         END) NEED_DAYS
                    FROM (SELECT EM.DEPT_ID,
                                 EM.EMP_CODE,
                                 EM.EMP_NAME,
                                 START_TM,
                                 END_TM,
                                 MON.YM,
                                 TRUNC(TO_DATE(P_YM ||
                                               '-01',
                                               'YYYY-MM-DD'),
                                       'MM') FIRST_DAY,
                                 NVL(EM.DIMISSION_DT,
                                     TO_DATE('2114-01-01',
                                             'YYYY-MM-DD')) DIMISSION_DT_TEMP,
                                 LAST_DAY(TO_DATE(P_YM ||
                                                  '-01',
                                                  'YYYY-MM-DD')) LAST_DAY
                            FROM TM_OSS_EMPLOYEE EM,
                                 TM_PB_GROUP_INFO G,
                                 (SELECT CHANGE_DEPT_ID,
                                         EMP_CODE,
                                         CHANGE_DEPT_CODE,
                                         START_TM,
                                         NVL(END_TM,
                                             TO_DATE('2114-01-01',
                                                     'YYYY-MM-DD')) END_TM
                                    FROM (SELECT DEP.DEPT_ID CHANGE_DEPT_ID,
                                                 CH.EMP_CODE,
                                                 CH.DEPT_CODE CHANGE_DEPT_CODE,
                                                 CH.CHANGE_ZONE_TM START_TM,
                                                 LEAD(CH.CHANGE_ZONE_TM) OVER(PARTITION BY CH.EMP_CODE ORDER BY CH.CHANGE_ZONE_TM) END_TM
                                            FROM TI_OSS_HR_EMP_NEW_CHANGEDEPT CH,
                                                 TM_DEPARTMENT                DEP
                                           WHERE CH.DEPT_CODE =
                                                 DEP.DEPT_CODE
                                             AND CH.EMP_STUS = 2)) CHG,
                                 (
                                   SELECT YM,DEPT_ID,EMP_CODE
                                   FROM TT_PB_SHEDULE_BY_MONTH
                                   WHERE YM = P_YM
                                   ) MON
                           WHERE EM.GROUP_ID =
                                 G.GROUP_ID
                             AND EM.EMP_CODE =
                                 MON.EMP_CODE(+)
                             AND EM.DEPT_ID =
                                 MON.DEPT_ID(+)
                             AND MON.EMP_CODE =
                                 CHG.EMP_CODE(+)
                             AND MON.DEPT_ID =
                                 CHG.CHANGE_DEPT_ID(+)
                             AND MON.EMP_CODE =
                                 CHG.EMP_CODE(+)
                             ))
           WHERE YM IS NULL
             AND NEED_DAYS > 0),
           UN_FINISH_SCHE_DEPTID AS (
             SELECT DEPT_ID
             FROM (SELECT A.DEPT_ID,
                          A.EMP_CODE,
                          B.YM,
                          MAX(NEED_DAYS) NEED_DAYS,
                          COUNT(1) ACTUAL_DAYS
                     FROM TT_PB_SHEDULE_BY_DAY A,
                          (SELECT DEPT_ID,
                                  EMP_CODE,
                                  YM,
                                  SUBSTR(BETWEEN_DATE,
                                         0,
                                         10) START_DATE,
                                  SUBSTR(BETWEEN_DATE,
                                         12,
                                         10) END_DATE,
                                  SUBSTR(BETWEEN_DATE,
                                         23,
                                         LENGTH(BETWEEN_DATE)) NEED_DAYS
                             FROM (SELECT DEPT_ID,
                                          EMP_CODE,
                                          YM,
                                          MAX(CASE
                                                WHEN START_TM IS NULL AND
                                                     END_TM IS NULL AND
                                                     DIMISSION_DT_TEMP <=
                                                     FIRST_DAY THEN
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD') || '~' ||
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD') || '~' || '0'
                                                WHEN START_TM IS NULL AND
                                                     END_TM IS NULL AND
                                                     DIMISSION_DT_TEMP >
                                                     FIRST_DAY AND
                                                     DIMISSION_DT_TEMP <=
                                                     LAST_DAY THEN
                                                 TO_CHAR(FIRST_DAY,
                                                         'YYYY-MM-DD') || '~' ||
                                                 TO_CHAR(DIMISSION_DT_TEMP - 1,
                                                         'YYYY-MM-DD') || '~' ||
                                                 TO_CHAR(TO_NUMBER(TO_CHAR(DIMISSION_DT_TEMP,
                                                                           'DD')) -
                                                         TO_NUMBER(TO_CHAR(FIRST_DAY,
                                                                           'DD')))
                                                WHEN START_TM IS NULL AND
                                                     END_TM IS NULL AND
                                                     DIMISSION_DT_TEMP >
                                                     LAST_DAY THEN
                                                 TO_CHAR(FIRST_DAY,
                                                         'YYYY-MM-DD') || '~' ||
                                                 TO_CHAR(LAST_DAY,
                                                         'YYYY-MM-DD') || '~' ||
                                                 TO_CHAR(LAST_DAY,
                                                         'DD')
                                                WHEN START_TM IS NOT NULL AND
                                                     DIMISSION_DT_TEMP >
                                                     FIRST_DAY AND
                                                     END_TM >
                                                     FIRST_DAY THEN
                                                 TO_CHAR(GREATEST(FIRST_DAY,
                                                                  START_TM),
                                                         'YYYY-MM-DD') || '~' ||
                                                 TO_CHAR(DECODE(LEAST(LAST_DAY,
                                                                      DIMISSION_DT_TEMP,
                                                                      END_TM),
                                                                LAST_DAY,
                                                                LAST_DAY,
                                                                LEAST(LAST_DAY,
                                                                      DIMISSION_DT_TEMP,
                                                                      END_TM) - 1),
                                                         'DD') || '~' ||
                                                 DECODE(LEAST(LAST_DAY,
                                                              DIMISSION_DT_TEMP,
                                                              END_TM),
                                                        LAST_DAY,
                                                        TO_CHAR(TO_NUMBER(TO_CHAR(LEAST(LAST_DAY,
                                                                                        DIMISSION_DT_TEMP,
                                                                                        END_TM),
                                                                                  'DD')) -
                                                                TO_NUMBER(TO_CHAR(GREATEST(FIRST_DAY,
                                                                                           START_TM),
                                                                                  'DD')) + 1),
                                                        TO_CHAR(TO_NUMBER(TO_CHAR(LEAST(LAST_DAY,
                                                                                        DIMISSION_DT_TEMP,
                                                                                        END_TM),
                                                                                  'DD')) -
                                                                TO_NUMBER(TO_CHAR(GREATEST(FIRST_DAY,
                                                                                           START_TM),
                                                                                  'DD'))))

                                                WHEN START_TM IS NOT NULL AND
                                                     DIMISSION_DT_TEMP >
                                                     LAST_DAY AND
                                                     END_TM >
                                                     LAST_DAY THEN
                                                 TO_CHAR(GREATEST(FIRST_DAY,
                                                                  START_TM),
                                                         'DD') || '~' ||
                                                 TO_CHAR(LAST_DAY,
                                                         'DD') || '~' ||
                                                 TO_CHAR(TO_NUMBER(TO_CHAR(LAST_DAY,
                                                                           'DD')) -
                                                         TO_NUMBER(TO_CHAR(GREATEST(FIRST_DAY,
                                                                                    START_TM),
                                                                           'DD')) + 1)

                                                WHEN START_TM IS NOT NULL AND
                                                     END_TM IS NOT NULL AND
                                                     END_TM <=
                                                     FIRST_DAY THEN
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD') || '~' ||
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD') || '~' || '0'

                                                WHEN START_TM IS NOT NULL AND
                                                     END_TM IS NOT NULL AND
                                                     DIMISSION_DT_TEMP <=
                                                     FIRST_DAY THEN
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD') || '~' ||
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD') || '~' || '0'

                                                WHEN START_TM IS NOT NULL AND
                                                     DIMISSION_DT_TEMP <=
                                                     START_TM THEN
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD') || '~' ||
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD') || '~' || '0'

                                                WHEN START_TM IS NOT NULL AND
                                                     LAST_DAY <=
                                                     START_TM AND
                                                     START_TM <=
                                                     DIMISSION_DT_TEMP THEN
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD') || '~' ||
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD') || '~' || '0'
                                              END) BETWEEN_DATE
                                     FROM (SELECT EM.DEPT_ID,
                                                  MON.YM YM,
                                                  EM.EMP_CODE,
                                                  EM.EMP_NAME,
                                                  EM.WORK_TYPE,
                                                  EM.GROUP_ID,
                                                  EM.DIMISSION_DT,
                                                  CHG.CHANGE_DEPT_ID,
                                                  START_TM,
                                                  END_TM,
                                                  TRUNC(TO_DATE(MON.YM ||
                                                                '-01',
                                                                'YYYY-MM-DD'),
                                                        'MM') FIRST_DAY,
                                                  NVL(EM.DIMISSION_DT,
                                                      TO_DATE('2114-01-01',
                                                              'YYYY-MM-DD')) DIMISSION_DT_TEMP,
                                                  LAST_DAY(TO_DATE(MON.YM ||
                                                                   '-01',
                                                                   'YYYY-MM-DD')) LAST_DAY
                                             FROM TM_OSS_EMPLOYEE EM,
                                                  TM_PB_GROUP_INFO G,
                                                  (SELECT CHANGE_DEPT_ID,
                                                          EMP_CODE,
                                                          CHANGE_DEPT_CODE,
                                                          START_TM,
                                                          NVL(END_TM,
                                                              TO_DATE('2114-01-01',
                                                                      'YYYY-MM-DD')) END_TM
                                                     FROM (SELECT DEP.DEPT_ID CHANGE_DEPT_ID,
                                                                  CH.EMP_CODE,
                                                                  CH.DEPT_CODE CHANGE_DEPT_CODE,
                                                                  CH.CHANGE_ZONE_TM START_TM,
                                                                  LEAD(CH.CHANGE_ZONE_TM) OVER(PARTITION BY CH.EMP_CODE ORDER BY CH.CHANGE_ZONE_TM) END_TM
                                                             FROM TI_OSS_HR_EMP_NEW_CHANGEDEPT CH,
                                                                  TM_DEPARTMENT                DEP
                                                            WHERE CH.DEPT_CODE =
                                                                  DEP.DEPT_CODE
                                                              AND CH.EMP_STUS = 2)) CHG,
                                                  (
                                                   SELECT ID,YM,DEPT_ID,EMP_CODE
                                                   FROM TT_PB_SHEDULE_BY_MONTH
                                                   WHERE YM = P_YM
                                                   ) MON
                                            WHERE EM.GROUP_ID =
                                                  G.GROUP_ID
                                              AND EM.EMP_CODE =
                                                  MON.EMP_CODE
                                              AND MON.EMP_CODE =
                                                  CHG.EMP_CODE(+)
                                              AND MON.DEPT_ID =
                                                  CHG.CHANGE_DEPT_ID(+)
                                              AND MON.EMP_CODE =
                                                  CHG.EMP_CODE(+)
                                              )
                                    GROUP BY EMP_CODE,
                                             DEPT_ID,
                                             YM)) B
                    WHERE A.DEPT_ID =
                          B.DEPT_ID
                      AND A.EMP_CODE =
                          B.EMP_CODE
                      AND TO_CHAR(A.SHEDULE_DT,
                                  'YYYY-MM') = B.YM
                      AND A.SHEDULE_DT >=
                          TO_DATE(START_DATE,
                                  'YYYY-MM-DD')
                      AND A.SHEDULE_DT <=
                          TO_DATE(END_DATE,
                                  'YYYY-MM-DD')
                    GROUP BY A.DEPT_ID,
                             A.EMP_CODE,
                             B.YM)
            WHERE ACTUAL_DAYS < NEED_DAYS)

               SELECT U.DEPT_ID, E.EMP_CODE, E.EMP_EMAIL
                 FROM TS_USER         U,
                      TS_USER_ROLE    UR,
                      TS_ROLE_MODULE  RM,
                      TS_MODULE       M,
                      TM_EMPLOYEE E
                      WHERE U.USER_ID = UR.USER_ID
                      AND UR.ROLE_ID = RM.ROLE_ID
                      AND RM.MODULE_ID = M.MODULE_ID
                      AND U.EMP_ID = E.EMP_ID
                       AND M.MODULE_CODE = 'scheduling'
                      AND M.ACTION_URL = '/businessMgt/scheduling'
                      AND E.EMP_EMAIL IS NOT NULL
                      AND U.DEPT_ID IN
                          (SELECT DEPT_ID
                          FROM (SELECT DEPT_ID
                             FROM UN_FINISH_EMP_DEPTID
                           UNION ALL
                           SELECT DEPT_ID FROM UN_FINISH_SCHE_DEPTID)
                           GROUP BY DEPT_ID
                           ));


  END;

  PROCEDURE SPT_ALL_EMP_IS_HAS_SCHE(P_YM     VARCHAR2,
                                    P_DEPTID NUMBER,
                                    P_OUT_CT OUT NUMBER) IS
  BEGIN

    SELECT COUNT(1)
      INTO P_OUT_CT
      FROM (SELECT DEPT_ID,
                   YM,
                   (CASE
                     WHEN START_TM IS NULL AND END_TM IS NULL AND
                          DIMISSION_DT_TEMP <= FIRST_DAY THEN
                      0
                     WHEN START_TM IS NULL AND END_TM IS NULL AND
                          DIMISSION_DT_TEMP > FIRST_DAY AND
                          DIMISSION_DT_TEMP <= LAST_DAY THEN
                      TO_NUMBER(TO_CHAR(DIMISSION_DT_TEMP, 'DD')) -
                      TO_NUMBER(TO_CHAR(FIRST_DAY, 'DD'))
                     WHEN START_TM IS NULL AND END_TM IS NULL AND
                          DIMISSION_DT_TEMP > LAST_DAY THEN
                      TO_NUMBER(TO_CHAR(LAST_DAY, 'DD'))

                     WHEN START_TM IS NOT NULL AND
                          DIMISSION_DT_TEMP > FIRST_DAY AND
                          END_TM > FIRST_DAY THEN
                      DECODE(LEAST(LAST_DAY, DIMISSION_DT_TEMP, END_TM),
                             LAST_DAY,
                             TO_NUMBER(TO_CHAR(LEAST(LAST_DAY,
                                                     DIMISSION_DT_TEMP,
                                                     END_TM),
                                               'DD')) -
                             TO_NUMBER(TO_CHAR(GREATEST(FIRST_DAY, START_TM),
                                               'DD')) + 1,
                             TO_NUMBER(TO_CHAR(LEAST(LAST_DAY,
                                                     DIMISSION_DT_TEMP,
                                                     END_TM),
                                               'DD')) -
                             TO_NUMBER(TO_CHAR(GREATEST(FIRST_DAY, START_TM),
                                               'DD')))

                     WHEN START_TM IS NOT NULL AND
                          DIMISSION_DT_TEMP > LAST_DAY AND END_TM > LAST_DAY THEN
                      TO_NUMBER(TO_CHAR(LAST_DAY, 'DD')) -
                      TO_NUMBER(TO_CHAR(GREATEST(FIRST_DAY, START_TM), 'DD')) + 1

                     WHEN START_TM IS NOT NULL AND END_TM IS NOT NULL AND
                          END_TM <= FIRST_DAY THEN
                      0

                     WHEN START_TM IS NOT NULL AND END_TM IS NOT NULL AND
                          DIMISSION_DT_TEMP <= FIRST_DAY THEN
                      0

                     WHEN START_TM IS NOT NULL AND
                          DIMISSION_DT_TEMP <= START_TM THEN
                      0

                     WHEN START_TM IS NOT NULL AND LAST_DAY <= START_TM AND
                          START_TM <= DIMISSION_DT_TEMP THEN
                      0
                   END) NEED_DAYS
              FROM (SELECT EM.DEPT_ID,
                           EM.EMP_CODE,
                           EM.EMP_NAME,
                           START_TM,
                           END_TM,
                           MON.YM,
                           TRUNC(TO_DATE(P_YM || '-01', 'YYYY-MM-DD'), 'MM') FIRST_DAY,
                           NVL(EM.DIMISSION_DT,
                               TO_DATE('2114-01-01', 'YYYY-MM-DD')) DIMISSION_DT_TEMP,
                           LAST_DAY(TO_DATE(P_YM || '-01', 'YYYY-MM-DD')) LAST_DAY
                      FROM TM_OSS_EMPLOYEE EM,
                           TM_PB_GROUP_INFO G,
                           (SELECT CHANGE_DEPT_ID,
                                   EMP_CODE,
                                   CHANGE_DEPT_CODE,
                                   START_TM,
                                   NVL(END_TM,
                                       TO_DATE('2114-01-01', 'YYYY-MM-DD')) END_TM
                              FROM (SELECT DEP.DEPT_ID CHANGE_DEPT_ID,
                                           CH.EMP_CODE,
                                           CH.DEPT_CODE CHANGE_DEPT_CODE,
                                           CH.CHANGE_ZONE_TM START_TM,
                                           LEAD(CH.CHANGE_ZONE_TM) OVER(PARTITION BY CH.EMP_CODE ORDER BY CH.CHANGE_ZONE_TM) END_TM
                                      FROM TI_OSS_HR_EMP_NEW_CHANGEDEPT CH,
                                           TM_DEPARTMENT                DEP
                                     WHERE CH.DEPT_CODE = DEP.DEPT_CODE
                                       AND CH.EMP_STUS = 2)) CHG,
                           (
                           SELECT YM,DEPT_ID,EMP_CODE
                           FROM TT_PB_SHEDULE_BY_MONTH
                           WHERE YM = P_YM
                           ) MON
                     WHERE EM.GROUP_ID = G.GROUP_ID
                       AND EM.EMP_CODE = MON.EMP_CODE(+)
                       AND EM.DEPT_ID = MON.DEPT_ID(+)
                       AND MON.EMP_CODE = CHG.EMP_CODE(+)
                       AND MON.DEPT_ID = CHG.CHANGE_DEPT_ID(+)
                       AND MON.EMP_CODE = CHG.EMP_CODE(+)
                       AND EM.DEPT_ID = P_DEPTID
                       ))
     WHERE YM IS NULL
       AND NEED_DAYS > 0;
  END;
  PROCEDURE SPT_ALL_SCHE_IS_FINISHED(P_YM     VARCHAR2,
                                     P_DEPTID NUMBER,
                                     P_OUT_CT OUT NUMBER) IS
  BEGIN

    SELECT COUNT(1)
      INTO P_OUT_CT
      FROM (SELECT A.DEPT_ID,
                   A.EMP_CODE,
                   B.YM,
                   MAX(NEED_DAY) NEED_DAYS,
                   COUNT(1) ACTUAL_DAYS
              FROM TT_PB_SHEDULE_BY_DAY A,
                   (SELECT DEPT_ID,
                           EMP_CODE,
                           YM,
                           SUBSTR(BETWEEN_DATE, 0, 10) START_DATE,
                           SUBSTR(BETWEEN_DATE, 12, 10) END_DATE,
                           SUBSTR(BETWEEN_DATE, 23, LENGTH(BETWEEN_DATE)) NEED_DAY
                      FROM (SELECT DEPT_ID,
                                   EMP_CODE,
                                   YM,
                                   MAX(CASE
                                         WHEN START_TM IS NULL AND END_TM IS NULL AND
                                              DIMISSION_DT_TEMP <= FIRST_DAY THEN
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' || '0'
                                         WHEN START_TM IS NULL AND END_TM IS NULL AND
                                              DIMISSION_DT_TEMP > FIRST_DAY AND
                                              DIMISSION_DT_TEMP <= LAST_DAY THEN
                                          TO_CHAR(FIRST_DAY, 'YYYY-MM-DD') || '~' ||
                                          TO_CHAR(DIMISSION_DT_TEMP - 1,
                                                  'YYYY-MM-DD') || '~' ||
                                          TO_CHAR(TO_NUMBER(TO_CHAR(DIMISSION_DT_TEMP,
                                                                    'DD')) -
                                                  TO_NUMBER(TO_CHAR(FIRST_DAY, 'DD')))
                                         WHEN START_TM IS NULL AND END_TM IS NULL AND
                                              DIMISSION_DT_TEMP > LAST_DAY THEN
                                          TO_CHAR(FIRST_DAY, 'YYYY-MM-DD') || '~' ||
                                          TO_CHAR(LAST_DAY, 'YYYY-MM-DD') || '~' ||
                                          TO_CHAR(LAST_DAY, 'DD')
                                         WHEN START_TM IS NOT NULL AND
                                              DIMISSION_DT_TEMP > FIRST_DAY AND
                                              END_TM > FIRST_DAY THEN
                                          TO_CHAR(GREATEST(FIRST_DAY, START_TM),
                                                  'YYYY-MM-DD') || '~' ||
                                          TO_CHAR(DECODE(LEAST(LAST_DAY,
                                                               DIMISSION_DT_TEMP,
                                                               END_TM),
                                                         LAST_DAY,
                                                         LAST_DAY,
                                                         LEAST(LAST_DAY,
                                                               DIMISSION_DT_TEMP,
                                                               END_TM) - 1),
                                                  'DD') || '~' ||
                                          DECODE(LEAST(LAST_DAY,
                                                       DIMISSION_DT_TEMP,
                                                       END_TM),
                                                 LAST_DAY,
                                                 TO_CHAR(TO_NUMBER(TO_CHAR(LEAST(LAST_DAY,
                                                                                 DIMISSION_DT_TEMP,
                                                                                 END_TM),
                                                                           'DD')) -
                                                         TO_NUMBER(TO_CHAR(GREATEST(FIRST_DAY,
                                                                                    START_TM),
                                                                           'DD')) + 1),
                                                 TO_CHAR(TO_NUMBER(TO_CHAR(LEAST(LAST_DAY,
                                                                                 DIMISSION_DT_TEMP,
                                                                                 END_TM),
                                                                           'DD')) -
                                                         TO_NUMBER(TO_CHAR(GREATEST(FIRST_DAY,
                                                                                    START_TM),
                                                                           'DD'))))

                                         WHEN START_TM IS NOT NULL AND
                                              DIMISSION_DT_TEMP > LAST_DAY AND
                                              END_TM > LAST_DAY THEN
                                          TO_CHAR(GREATEST(FIRST_DAY, START_TM),
                                                  'DD') || '~' ||
                                          TO_CHAR(LAST_DAY, 'DD') || '~' ||
                                          TO_CHAR(TO_NUMBER(TO_CHAR(LAST_DAY, 'DD')) -
                                                  TO_NUMBER(TO_CHAR(GREATEST(FIRST_DAY,
                                                                             START_TM),
                                                                    'DD')) + 1)

                                         WHEN START_TM IS NOT NULL AND
                                              END_TM IS NOT NULL AND
                                              END_TM <= FIRST_DAY THEN
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' || '0'

                                         WHEN START_TM IS NOT NULL AND
                                              END_TM IS NOT NULL AND
                                              DIMISSION_DT_TEMP <= FIRST_DAY THEN
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' || '0'

                                         WHEN START_TM IS NOT NULL AND
                                              DIMISSION_DT_TEMP <= START_TM THEN
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' || '0'

                                         WHEN START_TM IS NOT NULL AND
                                              LAST_DAY <= START_TM AND
                                              START_TM <= DIMISSION_DT_TEMP THEN
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' || '0'
                                       END) BETWEEN_DATE
                              FROM (SELECT EM.DEPT_ID,
                                           MON.YM YM,
                                           EM.EMP_CODE,
                                           EM.EMP_NAME,
                                           EM.WORK_TYPE,
                                           EM.GROUP_ID,
                                           EM.DIMISSION_DT,
                                           CHG.CHANGE_DEPT_ID,
                                           START_TM,
                                           END_TM,
                                           TRUNC(TO_DATE(MON.YM || '-01',
                                                         'YYYY-MM-DD'),
                                                 'MM') FIRST_DAY,
                                           NVL(EM.DIMISSION_DT,
                                               TO_DATE('2114-01-01',
                                                       'YYYY-MM-DD')) DIMISSION_DT_TEMP,
                                           LAST_DAY(TO_DATE(MON.YM || '-01',
                                                            'YYYY-MM-DD')) LAST_DAY
                                      FROM TM_OSS_EMPLOYEE EM,
                                           TM_PB_GROUP_INFO G,
                                           (SELECT CHANGE_DEPT_ID,
                                                   EMP_CODE,
                                                   CHANGE_DEPT_CODE,
                                                   START_TM,
                                                   NVL(END_TM,
                                                       TO_DATE('2114-01-01',
                                                               'YYYY-MM-DD')) END_TM
                                              FROM (SELECT DEP.DEPT_ID CHANGE_DEPT_ID,
                                                           CH.EMP_CODE,
                                                           CH.DEPT_CODE CHANGE_DEPT_CODE,
                                                           CH.CHANGE_ZONE_TM START_TM,
                                                           LEAD(CH.CHANGE_ZONE_TM) OVER(PARTITION BY CH.EMP_CODE ORDER BY CH.CHANGE_ZONE_TM) END_TM
                                                      FROM TI_OSS_HR_EMP_NEW_CHANGEDEPT CH,
                                                           TM_DEPARTMENT                DEP
                                                     WHERE CH.DEPT_CODE =
                                                           DEP.DEPT_CODE
                                                       AND CH.EMP_STUS = 2)) CHG,
                                           (
                                           SELECT ID,YM,DEPT_ID,EMP_CODE
                                           FROM TT_PB_SHEDULE_BY_MONTH
                                           WHERE YM = P_YM
                                           ) MON
                                     WHERE EM.GROUP_ID = G.GROUP_ID
                                       AND EM.EMP_CODE = MON.EMP_CODE
                                       AND MON.EMP_CODE = CHG.EMP_CODE(+)
                                       AND MON.DEPT_ID = CHG.CHANGE_DEPT_ID(+)
                                       AND MON.EMP_CODE = CHG.EMP_CODE(+)
                                       AND EM.DEPT_ID = P_DEPTID
                                       )
                             GROUP BY EMP_CODE, DEPT_ID, YM)) B
             WHERE A.DEPT_ID = B.DEPT_ID
               AND A.EMP_CODE = B.EMP_CODE
               AND TO_CHAR(A.SHEDULE_DT, 'YYYY-MM') = B.YM
               AND A.SHEDULE_DT >= TO_DATE(START_DATE, 'YYYY-MM-DD')
               AND A.SHEDULE_DT <= TO_DATE(END_DATE, 'YYYY-MM-DD')
             GROUP BY A.DEPT_ID, A.EMP_CODE, B.YM)
     WHERE ACTUAL_DAYS < NEED_DAYS;
  END;

  PROCEDURE SPT_GET_PROCESS_UNFIN_EMP_LIST(P_YM         VARCHAR2,
                                           P_OUT_CURSOR OUT CURSOR_TYPE) IS
  BEGIN
    OPEN P_OUT_CURSOR FOR
      SELECT *
        FROM (
            WITH UN_FINISH_PROCESS_DEPTID AS (
             SELECT M.DEPT_ID
             FROM TT_PB_SCHE_CONFIRM C,
                  TT_PB_SHEDULE_BY_MONTH M,
                  (SELECT A.DEPT_ID,
                          A.EMP_CODE,
                          A.SHEDULE_DT,
                          A.SHEDULE_MON_ID
                     FROM TT_PB_SHEDULE_BY_DAY A,
                          (SELECT DEPT_ID,
                                  EMP_CODE,
                                  YM,
                                  SUBSTR(BETWEEN_DATE,
                                         0,
                                         10) START_DATE,
                                  SUBSTR(BETWEEN_DATE,
                                         12,
                                         10) END_DATE,
                                  SUBSTR(BETWEEN_DATE,
                                         23,
                                         LENGTH(BETWEEN_DATE)) NEED_DAYS
                             FROM (SELECT DEPT_ID,
                                          EMP_CODE,
                                          YM,
                                          MAX(CASE
                                                WHEN START_TM IS NULL AND
                                                     END_TM IS NULL AND
                                                     DIMISSION_DT_TEMP <=
                                                     FIRST_DAY THEN
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD') || '~' ||
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD')
                                                WHEN START_TM IS NULL AND
                                                     END_TM IS NULL AND
                                                     DIMISSION_DT_TEMP >
                                                     FIRST_DAY AND
                                                     DIMISSION_DT_TEMP <=
                                                     LAST_DAY THEN
                                                 TO_CHAR(FIRST_DAY,
                                                         'YYYY-MM-DD') || '~' ||
                                                 TO_CHAR(DIMISSION_DT_TEMP - 1,
                                                         'YYYY-MM-DD')
                                                WHEN START_TM IS NULL AND
                                                     END_TM IS NULL AND
                                                     DIMISSION_DT_TEMP >
                                                     LAST_DAY THEN
                                                 TO_CHAR(FIRST_DAY,
                                                         'YYYY-MM-DD') || '~' ||
                                                 TO_CHAR(LAST_DAY,
                                                         'YYYY-MM-DD')
                                                WHEN START_TM IS NOT NULL AND
                                                     DIMISSION_DT_TEMP >
                                                     FIRST_DAY AND
                                                     END_TM >
                                                     FIRST_DAY THEN
                                                 TO_CHAR(GREATEST(FIRST_DAY,
                                                                  START_TM),
                                                         'YYYY-MM-DD') || '~' ||
                                                 TO_CHAR(DECODE(LEAST(LAST_DAY,
                                                                      DIMISSION_DT_TEMP,
                                                                      END_TM),
                                                                LAST_DAY,
                                                                LAST_DAY,
                                                                LEAST(LAST_DAY,
                                                                      DIMISSION_DT_TEMP,
                                                                      END_TM) - 1),
                                                         'DD')

                                                WHEN START_TM IS NOT NULL AND
                                                     DIMISSION_DT_TEMP >
                                                     LAST_DAY AND
                                                     END_TM >
                                                     LAST_DAY THEN
                                                 TO_CHAR(GREATEST(FIRST_DAY,
                                                                  START_TM),
                                                         'DD') || '~' ||
                                                 TO_CHAR(LAST_DAY,
                                                         'DD')

                                                WHEN START_TM IS NOT NULL AND
                                                     END_TM IS NOT NULL AND
                                                     END_TM <=
                                                     FIRST_DAY THEN
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD') || '~' ||
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD')

                                                WHEN START_TM IS NOT NULL AND
                                                     END_TM IS NOT NULL AND
                                                     DIMISSION_DT_TEMP <=
                                                     FIRST_DAY THEN
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD') || '~' ||
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD')

                                                WHEN START_TM IS NOT NULL AND
                                                     DIMISSION_DT_TEMP <=
                                                     START_TM THEN
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD') || '~' ||
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD')

                                                WHEN START_TM IS NOT NULL AND
                                                     LAST_DAY <=
                                                     START_TM AND
                                                     START_TM <=
                                                     DIMISSION_DT_TEMP THEN
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD') || '~' ||
                                                 TO_CHAR(FIRST_DAY - 1,
                                                         'YYYY-MM-DD')
                                              END) BETWEEN_DATE
                                     FROM (SELECT EM.DEPT_ID,
                                                  MON.YM YM,
                                                  EM.EMP_CODE,
                                                  EM.EMP_NAME,
                                                  EM.WORK_TYPE,
                                                  EM.GROUP_ID,
                                                  EM.DIMISSION_DT,
                                                  CHG.CHANGE_DEPT_ID,
                                                  START_TM,
                                                  END_TM,
                                                  TRUNC(TO_DATE(MON.YM ||
                                                                '-01',
                                                                'YYYY-MM-DD'),
                                                        'MM') FIRST_DAY,
                                                  NVL(EM.DIMISSION_DT,
                                                      TO_DATE('2114-01-01',
                                                              'YYYY-MM-DD')) DIMISSION_DT_TEMP,
                                                  LAST_DAY(TO_DATE(MON.YM ||
                                                                   '-01',
                                                                   'YYYY-MM-DD')) LAST_DAY
                                             FROM TM_OSS_EMPLOYEE EM,
                                                  TM_PB_GROUP_INFO G,
                                                  (SELECT CHANGE_DEPT_ID,
                                                          EMP_CODE,
                                                          CHANGE_DEPT_CODE,
                                                          START_TM,
                                                          NVL(END_TM,
                                                              TO_DATE('2114-01-01',
                                                                      'YYYY-MM-DD')) END_TM
                                                     FROM (SELECT DEP.DEPT_ID CHANGE_DEPT_ID,
                                                                  CH.EMP_CODE,
                                                                  CH.DEPT_CODE CHANGE_DEPT_CODE,
                                                                  CH.CHANGE_ZONE_TM START_TM,
                                                                  LEAD(CH.CHANGE_ZONE_TM) OVER(PARTITION BY CH.EMP_CODE ORDER BY CH.CHANGE_ZONE_TM) END_TM
                                                             FROM TI_OSS_HR_EMP_NEW_CHANGEDEPT CH,
                                                                  TM_DEPARTMENT                DEP
                                                            WHERE CH.DEPT_CODE =
                                                                  DEP.DEPT_CODE
                                                              AND CH.EMP_STUS = 2)) CHG,
                                                   (
                                                   SELECT ID,YM,DEPT_ID,EMP_CODE
                                                   FROM TT_PB_SHEDULE_BY_MONTH
                                                   WHERE YM = P_YM
                                                   ) MON
                                            WHERE EM.GROUP_ID =
                                                  G.GROUP_ID
                                              AND EM.EMP_CODE =
                                                  MON.EMP_CODE
                                              AND MON.EMP_CODE =
                                                  CHG.EMP_CODE(+)
                                              AND MON.DEPT_ID =
                                                  CHG.CHANGE_DEPT_ID(+)
                                              AND MON.EMP_CODE =
                                                  CHG.EMP_CODE(+)
                                              )
                                    GROUP BY EMP_CODE,
                                             DEPT_ID,
                                             YM)) B
                    WHERE A.DEPT_ID =
                          B.DEPT_ID
                      AND A.EMP_CODE =
                          B.EMP_CODE
                      AND TO_CHAR(A.SHEDULE_DT,
                                  'YYYY-MM') = B.YM
                      AND A.SHEDULE_DT >=
                          TO_DATE(START_DATE,
                                  'YYYY-MM-DD')
                      AND A.SHEDULE_DT <=
                          TO_DATE(END_DATE,
                                  'YYYY-MM-DD')) SD,
                  TT_PB_PROCESS_BY_DAY PD
            WHERE C.DEPT_ID =
                  M.DEPT_ID
              AND C.YM = M.YM
              AND M.YM = P_YM
              AND M.ID =
                  SD.SHEDULE_MON_ID
              AND SD.DEPT_ID =
                  PD.DEPT_ID(+)
              AND SD.EMP_CODE =
                  PD.EMP_CODE(+)
              AND SD.SHEDULE_DT =
                  PD.PROCESS_DT(+)
              AND PD.ID IS NULL)

             SELECT U.DEPT_ID, E.EMP_CODE, E.EMP_EMAIL
               FROM TS_USER        U,
                    TS_USER_ROLE   UR,
                    TS_ROLE_MODULE RM,
                    TS_MODULE      M,
                    TM_EMPLOYEE    E
              WHERE U.USER_ID = UR.USER_ID
                AND UR.ROLE_ID = RM.ROLE_ID
                AND RM.MODULE_ID = M.MODULE_ID
                AND U.EMP_ID = E.EMP_ID
                AND M.MODULE_CODE = 'processMgt'
                AND M.ACTION_URL = '/businessMgt/processMgt'
                AND E.EMP_EMAIL IS NOT NULL
                AND U.DEPT_ID IN
                      (
                       SELECT DEPT_ID
                       FROM UN_FINISH_PROCESS_DEPTID
                       GROUP BY DEPT_ID
                       ));


  END;
  PROCEDURE SPT_ALL_SCHE_IS_HAS_PROCESS(P_YM     VARCHAR2,
                                        P_DEPTID NUMBER,
                                        P_OUT_CT OUT NUMBER) IS
  BEGIN
    SELECT COUNT(1)
      INTO P_OUT_CT
      FROM TT_PB_SCHE_CONFIRM     C,
           TT_PB_SHEDULE_BY_MONTH M,
           (SELECT A.DEPT_ID, A.EMP_CODE, A.SHEDULE_DT, A.SHEDULE_MON_ID
              FROM TT_PB_SHEDULE_BY_DAY A,
                   (SELECT DEPT_ID,
                           EMP_CODE,
                           YM,
                           SUBSTR(BETWEEN_DATE, 0, 10) START_DATE,
                           SUBSTR(BETWEEN_DATE, 12, 10) END_DATE,
                           SUBSTR(BETWEEN_DATE, 23, LENGTH(BETWEEN_DATE)) NEED_DAYS
                      FROM (SELECT DEPT_ID,
                                   EMP_CODE,
                                   YM,
                                   MAX(CASE
                                         WHEN START_TM IS NULL AND END_TM IS NULL AND
                                              DIMISSION_DT_TEMP <= FIRST_DAY THEN
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD')
                                         WHEN START_TM IS NULL AND END_TM IS NULL AND
                                              DIMISSION_DT_TEMP > FIRST_DAY AND
                                              DIMISSION_DT_TEMP <= LAST_DAY THEN
                                          TO_CHAR(FIRST_DAY, 'YYYY-MM-DD') || '~' ||
                                          TO_CHAR(DIMISSION_DT_TEMP - 1,
                                                  'YYYY-MM-DD')
                                         WHEN START_TM IS NULL AND END_TM IS NULL AND
                                              DIMISSION_DT_TEMP > LAST_DAY THEN
                                          TO_CHAR(FIRST_DAY, 'YYYY-MM-DD') || '~' ||
                                          TO_CHAR(LAST_DAY, 'YYYY-MM-DD')
                                         WHEN START_TM IS NOT NULL AND
                                              DIMISSION_DT_TEMP > FIRST_DAY AND
                                              END_TM > FIRST_DAY THEN
                                          TO_CHAR(GREATEST(FIRST_DAY, START_TM),
                                                  'YYYY-MM-DD') || '~' ||
                                          TO_CHAR(DECODE(LEAST(LAST_DAY,
                                                               DIMISSION_DT_TEMP,
                                                               END_TM),
                                                         LAST_DAY,
                                                         LAST_DAY,
                                                         LEAST(LAST_DAY,
                                                               DIMISSION_DT_TEMP,
                                                               END_TM) - 1),
                                                  'DD')

                                         WHEN START_TM IS NOT NULL AND
                                              DIMISSION_DT_TEMP > LAST_DAY AND
                                              END_TM > LAST_DAY THEN
                                          TO_CHAR(GREATEST(FIRST_DAY, START_TM),
                                                  'DD') || '~' ||
                                          TO_CHAR(LAST_DAY, 'DD')

                                         WHEN START_TM IS NOT NULL AND
                                              END_TM IS NOT NULL AND
                                              END_TM <= FIRST_DAY THEN
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD')

                                         WHEN START_TM IS NOT NULL AND
                                              END_TM IS NOT NULL AND
                                              DIMISSION_DT_TEMP <= FIRST_DAY THEN
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD')

                                         WHEN START_TM IS NOT NULL AND
                                              DIMISSION_DT_TEMP <= START_TM THEN
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD')

                                         WHEN START_TM IS NOT NULL AND
                                              LAST_DAY <= START_TM AND
                                              START_TM <= DIMISSION_DT_TEMP THEN
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||
                                          TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD')
                                       END) BETWEEN_DATE
                              FROM (SELECT EM.DEPT_ID,
                                           MON.YM YM,
                                           EM.EMP_CODE,
                                           EM.EMP_NAME,
                                           EM.WORK_TYPE,
                                           EM.GROUP_ID,
                                           EM.DIMISSION_DT,
                                           CHG.CHANGE_DEPT_ID,
                                           START_TM,
                                           END_TM,
                                           TRUNC(TO_DATE(MON.YM || '-01',
                                                         'YYYY-MM-DD'),
                                                 'MM') FIRST_DAY,
                                           NVL(EM.DIMISSION_DT,
                                               TO_DATE('2114-01-01',
                                                       'YYYY-MM-DD')) DIMISSION_DT_TEMP,
                                           LAST_DAY(TO_DATE(MON.YM || '-01',
                                                            'YYYY-MM-DD')) LAST_DAY
                                      FROM TM_OSS_EMPLOYEE EM,
                                           TM_PB_GROUP_INFO G,
                                           (SELECT CHANGE_DEPT_ID,
                                                   EMP_CODE,
                                                   CHANGE_DEPT_CODE,
                                                   START_TM,
                                                   NVL(END_TM,
                                                       TO_DATE('2114-01-01',
                                                               'YYYY-MM-DD')) END_TM
                                              FROM (SELECT DEP.DEPT_ID CHANGE_DEPT_ID,
                                                           CH.EMP_CODE,
                                                           CH.DEPT_CODE CHANGE_DEPT_CODE,
                                                           CH.CHANGE_ZONE_TM START_TM,
                                                           LEAD(CH.CHANGE_ZONE_TM) OVER(PARTITION BY CH.EMP_CODE ORDER BY CH.CHANGE_ZONE_TM) END_TM
                                                      FROM TI_OSS_HR_EMP_NEW_CHANGEDEPT CH,
                                                           TM_DEPARTMENT                DEP
                                                     WHERE CH.DEPT_CODE =
                                                           DEP.DEPT_CODE
                                                       AND CH.EMP_STUS = 2)) CHG,
                                           (
                                           SELECT YM,DEPT_ID,EMP_CODE
                                           FROM TT_PB_SHEDULE_BY_MONTH
                                           WHERE YM = P_YM
                                           ) MON
                                     WHERE EM.GROUP_ID = G.GROUP_ID
                                       AND EM.EMP_CODE = MON.EMP_CODE
                                       AND MON.EMP_CODE = CHG.EMP_CODE(+)
                                       AND MON.DEPT_ID = CHG.CHANGE_DEPT_ID(+)
                                       AND MON.EMP_CODE = CHG.EMP_CODE(+)
                                       AND EM.DEPT_ID = P_DEPTID)
                             GROUP BY EMP_CODE, DEPT_ID, YM)) B
             WHERE A.DEPT_ID = B.DEPT_ID
               AND A.EMP_CODE = B.EMP_CODE
               AND TO_CHAR(A.SHEDULE_DT, 'YYYY-MM') = B.YM
               AND A.SHEDULE_DT >= TO_DATE(START_DATE, 'YYYY-MM-DD')
               AND A.SHEDULE_DT <= TO_DATE(END_DATE, 'YYYY-MM-DD'))   SD,
           TT_PB_PROCESS_BY_DAY   PD
     WHERE C.DEPT_ID = M.DEPT_ID
       AND C.YM = M.YM
       AND M.ID = SD.SHEDULE_MON_ID
       AND SD.DEPT_ID = PD.DEPT_ID(+)
       AND SD.EMP_CODE = PD.EMP_CODE(+)
       AND SD.SHEDULE_DT = PD.PROCESS_DT(+)
       AND PD.ID IS NULL;
  END;

END PKG_OSS_SCHE_PROCESS;
/


spool off
