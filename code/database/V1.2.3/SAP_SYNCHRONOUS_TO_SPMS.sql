CREATE OR REPLACE PROCEDURE SAP_SYNCHRONOUS_TO_SPMS AS
  /**
   *每天02:00、14:00将SAP人员数据至TM_OSS_EMPLOYEE接口表
  **/
  --1.定义执行序号
  L_CALL_NO NUMBER;
  KEYVALUE  VARCHAR(900);

BEGIN

  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SAP_SYNCHRONOUS_TO_SPMS',
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
   WHERE C.KEY_NAME = 'CD_SAP2SPMS_EMP';
  IF KEYVALUE = '1' THEN

    BEGIN

      --插入临时表
      INSERT INTO TM_OSS_EMPLOYEE_TMP T
        (T.EMP_ID,
         T.EMP_CODE,
         T.EMP_NAME,
         T.EMP_DUTY_NAME,
         T.DEPT_ID,
         T.CREATE_TM,
         T.CREATE_EMP_CODE,
         T.WORK_TYPE,
         T.EMAIL,
         T.DIMISSION_DT,
         T.SF_DATE,
         T.EMP_POST_TYPE,
         T.POSITION_ATTR,
         T.DUTY_SERIAL,
         T.PERSON_TYPE,
         T.PERSG,
         T.PERSG_TXT,
         T.PERSK,
         T.PERSK_TXT,
         T.DATE_FROM,
         T.LAST_ZNO,
         T.CANCEL_FLAG,
         T.DATA_SOURCE)
     SELECT S.EMP_ID,
            S.EMP_NUM,
            S.LAST_NAME,
            P.POSITION_NAME,
            D.DEPT_ID,
            SYSDATE,
            'SAP',
            S.PERSK,
            S.MAIL_ADDRESS,
            S.CANCEL_DATE,
            S.SF_DATE,
            GETS_TYPE_OF_EMP(P.POSITION_NAME, P.ZHRGWXL, P.POSITION_ATTR),
            P.POSITION_ATTR,
            P.ZHRGWXL,
            S.PERSON_TYPE,
            S.PERSG,
            S.PERSG_TXT,
            S.PERSK,
            S.PERSK_TXT,
            S.DATE_FROM,
            S.LAST_ZNO,
            S.CANCEL_FLAG,
            '2'
       FROM TI_SAP_SYNCHRONOUS_EMP S,
            TI_SAP_SYNCHRONOUS_POS P,
            TM_DEPARTMENT          D
      WHERE S.POSITION_ID = P.POSITION_ID(+)
        AND S.NET_CODE = D.DEPT_CODE(+)
        AND S.DEAL_FLAG = 0
           --过滤一拖一辅岗
        AND P.IS_ODO_CJ IS NULL;
       COMMIT;

       --更新已经存在的信息
       UPDATE TM_OSS_EMPLOYEE E
        SET
        (E.EMP_NAME,
         E.EMP_DUTY_NAME,
         E.DEPT_ID,
         E.MODIFIED_TM,
         E.MODIFIED_EMP_CODE,
         E.WORK_TYPE,
         E.EMAIL,
         E.DIMISSION_DT,
         E.SF_DATE,
         E.EMP_POST_TYPE,
         E.POSITION_ATTR,
         E.DUTY_SERIAL,
         E.VERSION_NUMBER,
         E.PERSON_TYPE,
         E.PERSG,
         E.PERSG_TXT,
         E.PERSK,
         E.PERSK_TXT,
         E.DATE_FROM,
         E.LAST_ZNO,
         E.CANCEL_FLAG,
         E.SYNC_TM,
         E.DATA_SOURCE,
         E.Transfer_Date)
        =
       (SELECT TMP.EMP_NAME,
               TMP.EMP_DUTY_NAME,
               TMP.DEPT_ID,
               TMP.MODIFIED_TM,
               TMP.MODIFIED_EMP_CODE,
               TMP.WORK_TYPE,
               TMP.EMAIL,
               TMP.DIMISSION_DT,
               TMP.SF_DATE,
               TMP.EMP_POST_TYPE,
               TMP.POSITION_ATTR,
               TMP.DUTY_SERIAL,
               DECODE(E.VERSION_NUMBER, NULL, 0, E.VERSION_NUMBER + 1),
               TMP.PERSON_TYPE,
               TMP.PERSG,
               TMP.PERSG_TXT,
               TMP.PERSK,
               TMP.PERSK_TXT,
               TMP.DATE_FROM,
               TMP.LAST_ZNO,
               TMP.CANCEL_FLAG,
               SYSDATE,
               TMP.DATA_SOURCE,
                CASE
                   WHEN TMP.EMP_POST_TYPE <> E.EMP_POST_TYPE
                     THEN
                       SYSDATE
                       ELSE
                         NULL
                         END
          FROM TM_OSS_EMPLOYEE_TMP TMP
         WHERE TMP.EMP_CODE = E.EMP_CODE
           )
        WHERE E.EMP_CODE IN (
          SELECT TMP.EMP_CODE
            FROM TM_OSS_EMPLOYEE_TMP TMP
            WHERE TMP.EMP_CODE = E.EMP_CODE
        );

        --插入不存在的值
        INSERT INTO TM_OSS_EMPLOYEE E
          (E.EMP_ID,
           E.EMP_CODE,
           E.EMP_NAME,
           E.EMP_DUTY_NAME,
           E.DEPT_ID,
           E.CREATE_TM,
           E.CREATE_EMP_CODE,
           E.WORK_TYPE,
           E.EMAIL,
           E.DIMISSION_DT,
           E.SF_DATE,
           E.EMP_POST_TYPE,
           E.POSITION_ATTR,
           E.DUTY_SERIAL,
           E.VERSION_NUMBER,
           E.PERSON_TYPE,
           E.PERSG,
           E.PERSG_TXT,
           E.PERSK,
           E.PERSK_TXT,
           E.DATE_FROM,
           E.LAST_ZNO,
           E.CANCEL_FLAG,
           E.SYNC_TM,
           E.DATA_SOURCE)
          SELECT SEQ_OSS_BASE.NEXTVAL,
                 TMP.EMP_CODE,
                 TMP.EMP_NAME,
                 TMP.EMP_DUTY_NAME,
                 TMP.DEPT_ID,
                 TMP.MODIFIED_TM,
                 TMP.MODIFIED_EMP_CODE,
                 TMP.WORK_TYPE,
                 TMP.EMAIL,
                 TMP.DIMISSION_DT,
                 TMP.SF_DATE,
                 TMP.EMP_POST_TYPE,
                 TMP.POSITION_ATTR,
                 TMP.DUTY_SERIAL,
                 0,
                 TMP.PERSON_TYPE,
                 TMP.PERSG,
                 TMP.PERSG_TXT,
                 TMP.PERSK,
                 TMP.PERSK_TXT,
                 TMP.DATE_FROM,
                 TMP.LAST_ZNO,
                 TMP.CANCEL_FLAG,
                 SYSDATE,
                 TMP.DATA_SOURCE
            FROM TM_OSS_EMPLOYEE_TMP TMP
           WHERE NOT EXISTS
                 (SELECT 1 FROM TM_OSS_EMPLOYEE EE WHERE EE.EMP_CODE = TMP.EMP_CODE);
         --更改接口表状态
          UPDATE TI_SAP_SYNCHRONOUS_EMP S
             SET S.DEAL_FLAG = 1
           WHERE S.EMP_ID IN
                 (SELECT TMP.EMP_ID FROM TM_OSS_EMPLOYEE_TMP TMP);
          COMMIT;
          EXECUTE IMMEDIATE 'TRUNCATE TABLE TM_OSS_EMPLOYEE_TMP';
    END;

  END IF;
  --4.结束记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SAP_SYNCHRONOUS_TO_SPMS',
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
                                 'SAP_SYNCHRONOUS_TO_SPMS',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
END SAP_SYNCHRONOUS_TO_SPMS;
