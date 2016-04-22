CREATE OR REPLACE PROCEDURE HANDLE_SAP_ATTENCE AS
  --*************************************************************
  -- AUTHOR  : JHC
  -- CREATED : 2015-03-05
  -- PURPOSE : 处理考勤数据，插入考勤历史表和需要处理的数据临时表
  --
  -- PARAMETER:
  -- NAME             TYPE            DESC
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************
  --1.定义执行序号
  L_CALL_NO        NUMBER;
  V_SAP_DETAIL_NUM NUMBER;
  V_IS_DD          NUMBER;
BEGIN
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'HANDLE_SAP_ATTENCE',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);
  BEGIN

    SELECT COUNT(1)
      INTO V_SAP_DETAIL_NUM
      FROM TI_SAP_ZTHR_PT_DETAIL
     WHERE ROWNUM = 1;

    -- 处理加班、请假数据到加班、请假正式表
    HANDLE_JBANDQJ_SAP();

    IF V_SAP_DETAIL_NUM > 0 THEN

      /*EXECUTE IMMEDIATE 'TRUNCATE TABLE TI_SAP_ZTHR_PT_DETAIL_HIS';*/
      -- 将考勤接口表的数据插入到历史表
      INSERT INTO TI_SAP_ZTHR_PT_DETAIL_HIS
        (MANDT,
         PERNR,
         BEGDA,
         NACHN,
         ORGTX,
         ORGEH_T,
         PLANS_T,
         ZHRGWSX_T,
         PTEXT_G,
         PTEXT_K,
         ZTEXT,
         BEGUZ,
         ENDUZ,
         P10,
         P20,
         ZZ_CD,
         ZZ_ZT,
         ARBST,
         ZZ_CQSJ,
         ZZ_QJQK,
         ZZ_OFF,
         TXT,
         STDAZ,
         ZZ_KG,
         ZZ_SJCQ,
         DW,
         PAPER,
         CREATED_ON,
         CREATED_TS,
         SYNC_TM)
        SELECT MANDT,
               PERNR,
               BEGDA,
               NACHN,
               ORGTX,
               ORGEH_T,
               PLANS_T,
               ZHRGWSX_T,
               PTEXT_G,
               PTEXT_K,
               ZTEXT,
               BEGUZ,
               ENDUZ,
               P10,
               P20,
               ZZ_CD,
               ZZ_ZT,
               ARBST,
               ZZ_CQSJ,
               ZZ_QJQK,
               ZZ_OFF,
               TXT,
               STDAZ,
               ZZ_KG,
               ZZ_SJCQ,
               DW,
               PAPER,
               CREATED_ON,
               CREATED_TS,
               SYNC_TM
          FROM TI_SAP_ZTHR_PT_DETAIL D;
      COMMIT;

      /* 将符合条件的数据插入临时表  */
      INSERT INTO TI_SAP_ZTHR_PT_DETAIL_TMP
        (MANDT,
         PERNR,
         BEGDA,
         NACHN,
         ORGTX,
         ORGEH_T,
         PLANS_T,
         ZHRGWSX_T,
         PTEXT_G,
         PTEXT_K,
         ZTEXT,
         BEGUZ,
         ENDUZ,
         P10,
         P20,
         ZZ_CD,
         ZZ_ZT,
         ARBST,
         ZZ_CQSJ,
         ZZ_QJQK,
         ZZ_OFF,
         TXT,
         STDAZ,
         ZZ_KG,
         ZZ_SJCQ,
         DW,
         PAPER,
         CREATED_ON,
         CREATED_TS,
         SYNC_TM,
         SYNC_STATUS,
         POST_TYPE)
        SELECT MANDT,
               POST.EMP_CODE,
               D.BEGDA,
               NACHN,
               ORGTX,
               ORGEH_T,
               PLANS_T,
               ZHRGWSX_T,
               PTEXT_G,
               PTEXT_K,
               ZTEXT,
               BEGUZ,
               ENDUZ,
               P10,
               P20,
               ZZ_CD,
               ZZ_ZT,
               ARBST,
               ZZ_CQSJ,
               ZZ_QJQK,
               ZZ_OFF,
               TXT,
               STDAZ,
               ZZ_KG,
               ZZ_SJCQ,
               DW,
               SUBSTR(D.BEGDA, 1, 7),
               CREATED_ON,
               CREATED_TS,
               SYSDATE,
               0,
               POST.PREVIOUS_POST
          FROM TI_SAP_ZTHR_PT_DETAIL D,
               (SELECT GETS_EMP_CODE_ZERO_FILL(RC.EMP_CODE) PERNR,
                       RC.EMP_CODE EMP_CODE,
                       SC.BEGDA,
                       RC.PREVIOUS_POST
                  FROM TM_EMP_POST_CHANGE_RECORD RC
                  JOIN (SELECT LOG.PERNR, LOG.BEGDA, MIN(POST.ID) ID
                         FROM TI_SAP_ZTHR_PT_DETAIL     LOG,
                              TM_EMP_POST_CHANGE_RECORD POST
                        WHERE LOG.PERNR =
                              GETS_EMP_CODE_ZERO_FILL(POST.EMP_CODE)
                          AND TO_DATE(LOG.BEGDA, 'YYYY-MM-DD') <
                              POST.CHANGE_DATE
                        GROUP BY LOG.PERNR, LOG.BEGDA) SC
                    ON RC.ID = SC.ID) POST
         WHERE D.PERNR = POST.PERNR
           AND D.BEGDA = POST.BEGDA;
      COMMIT;

      /* 将符合条件的数据插入临时表  */
      INSERT INTO TI_SAP_ZTHR_PT_DETAIL_TMP
        (MANDT,
         PERNR,
         BEGDA,
         NACHN,
         ORGTX,
         ORGEH_T,
         PLANS_T,
         ZHRGWSX_T,
         PTEXT_G,
         PTEXT_K,
         ZTEXT,
         BEGUZ,
         ENDUZ,
         P10,
         P20,
         ZZ_CD,
         ZZ_ZT,
         ARBST,
         ZZ_CQSJ,
         ZZ_QJQK,
         ZZ_OFF,
         TXT,
         STDAZ,
         ZZ_KG,
         ZZ_SJCQ,
         DW,
         PAPER,
         CREATED_ON,
         CREATED_TS,
         SYNC_TM,
         SYNC_STATUS,
         POST_TYPE)
        SELECT MANDT,
               E.EMP_CODE,
               BEGDA,
               NACHN,
               ORGTX,
               ORGEH_T,
               PLANS_T,
               ZHRGWSX_T,
               PTEXT_G,
               PTEXT_K,
               ZTEXT,
               BEGUZ,
               ENDUZ,
               P10,
               P20,
               ZZ_CD,
               ZZ_ZT,
               ARBST,
               ZZ_CQSJ,
               ZZ_QJQK,
               ZZ_OFF,
               TXT,
               STDAZ,
               ZZ_KG,
               ZZ_SJCQ,
               DW,
               SUBSTR(BEGDA, 1, 7),
               CREATED_ON,
               CREATED_TS,
               SYSDATE,
               0,
               E.EMP_POST_TYPE
          FROM TI_SAP_ZTHR_PT_DETAIL D, TM_OSS_EMPLOYEE e
         where GETS_EMP_CODE_ZERO_FILL(e.EMP_CODE) = d.pernr
           AND (e.transfer_date is null or
                d.begda >= to_char(e.transfer_date, 'YYYY-MM-DD'));
      COMMIT;
      --F.删除接口表
      EXECUTE IMMEDIATE 'TRUNCATE TABLE TI_SAP_ZTHR_PT_DETAIL';

      FOR DETAIL_PAPER_ROW IN (SELECT DISTINCT T.PAPER
                                 FROM TI_SAP_ZTHR_PT_DETAIL_TMP T) LOOP
        -- 执行处理考勤零时表的数据
        HANDLE_ATIENCE_TMP(DETAIL_PAPER_ROW.PAPER);
      END LOOP;

    END IF;
    
  V_IS_DD := '';
    SELECT TO_CHAR(SYSDATE,'DD') INTO V_IS_DD FROM DUAL;
    
    IF V_IS_DD = 01 THEN 
      
     DELETE TI_TCAS_SPMS_SCHEDULE TMP
     WHERE tmp.Position_Type = '1'
     and tmp.work_date = to_date(to_char(sysdate -1,'yyyy-mm-dd'),'yyyy-mm-dd');
       
    INSERT INTO TI_TCAS_SPMS_SCHEDULE
      (SCHEDULE_ID,
       EMP_CODE,
       EMP_NAME,
       AREA_CODE,
       DEPT_CODE,
       GROUP_CODE,
       WORK_DATE,
       WORK_TIME,
       JOB_SEQ_CODE,
       JOB_SEQ,
       POSITION_TYPE,
       PERSON_TYPE,
       CREAT_EMP_CODE,
       CREAT_TIME,
       MODIFY_EMP_CODE,
       MODIFY_TIME,
       KQ_XSS,
       STDAZ,
       ARBST,
       PAPER)
      SELECT SEQ_TI_TCAS_SPMS_SCHEDULE.NEXTVAL,
        E.EMP_CODE,
        E.EMP_NAME,
        D.AREA_CODE,
        D.DEPT_CODE,
        DECODE(RC.GROUP_CODE, NULL, G.GROUP_CODE, RC.GROUP_CODE) GROUP_CODE,
        TD.SHEDULE_DT,
        decode(ti.training_code,null,DECODE(TD.SHEDULE_CODE, '休', 0, 1),0),
        PROCESS.PROCESS_CODE,
        DECODE(PC.DIFFICULTY_MODIFY_VALUE,
               NULL,
               NVL(PC.DIFFICULTY_VALUE, 0),
               DIFFICULTY_MODIFY_VALUE) DIFFICULTY_MODIFY_VALUE,
        1,
        E.PERSK_TXT,
        'ADMIN',
        SYSDATE,
        'ADMIN',
        SYSDATE,
        NULL,
        null,
        COUNT_TIME_DISTANCE(TB.START1_TIME, TB.END1_TIME) +
        COUNT_TIME_DISTANCE(TB.START2_TIME, TB.END2_TIME) +
        COUNT_TIME_DISTANCE(TB.START3_TIME, TB.END3_TIME),
        TO_CHAR(SYSDATE - 1, 'YYYY-MM')
   FROM TM_OSS_EMPLOYEE E
   LEFT JOIN TI_SAP_ZTHR_PT_DETAIL DET
     ON GETS_EMP_CODE_ZERO_FILL(E.EMP_CODE) = DET.PERNR
   JOIN TT_PB_SHEDULE_BY_DAY TD
     ON E.EMP_CODE = TD.EMP_CODE
     and e.dept_id = td.dept_id
    AND TD.SHEDULE_DT =to_date(to_char(sysdate -1,'yyyy-mm-dd'),'yyyy-mm-dd')
   LEFT JOIN TM_PB_SCHEDULE_BASE_INFO TB
     ON TD.DEPT_ID = TB.DEPT_ID
    AND TD.SHEDULE_CODE = TB.SCHEDULE_CODE
    AND TO_CHAR(TD.SHEDULE_DT, 'YYYY-MM') = TB.YM
    AND TB.CLASS_TYPE = '1'
   LEFT JOIN TM_DEPARTMENT D
     ON TD.DEPT_ID = D.DEPT_ID
   LEFT JOIN TM_PB_GROUP_INFO G
     ON E.GROUP_ID = G.GROUP_ID
   LEFT JOIN TT_PB_PROCESS_BY_DAY PROCESS
     ON E.EMP_CODE = PROCESS.EMP_CODE
    AND PROCESS.PROCESS_DT = to_date(to_char(sysdate -1,'yyyy-mm-dd'),'yyyy-mm-dd')
   LEFT JOIN TM_PB_PROCESS_INFO PC
     ON PROCESS.DEPT_ID = PC.DEPT_ID
    AND PROCESS.PROCESS_CODE = PC.PROCESS_CODE
   LEFT JOIN TT_PB_TRAINING_INFO TI
     ON E.EMP_CODE = TI.EMPLOYEE_CODE
    AND TI.DAY_OF_MONTH =to_char(sysdate -1,'yyyy-mm-dd')
   LEFT JOIN (SELECT SC.EMP_CODE,
                     SC.SHEDULE_DT,
                     NVL(GRP.GROUP_NAME, '') GROUP_NAME,
                     NVL(GRP.GROUP_CODE, '') GROUP_CODE
                FROM OP_EMP_GROUP_MODIFY_RECORD RC
                JOIN (SELECT LOG.EMP_CODE, LOG.SHEDULE_DT, MIN(RC.ID) ID
                       FROM TT_PB_SHEDULE_BY_DAY       LOG,
                            OP_EMP_GROUP_MODIFY_RECORD RC
                      WHERE LOG.EMP_CODE = RC.EMP_CODE
                        AND LOG.SHEDULE_DT < RC.ENABLE_TM
                        AND RC.ENABLE_STATE = 1
                      GROUP BY LOG.EMP_CODE, LOG.SHEDULE_DT) SC
                  ON RC.ID = SC.ID
                LEFT JOIN TM_PB_GROUP_INFO GRP
                  ON RC.PREV_GROUP_ID = GRP.GROUP_ID) RC
     ON TD.EMP_CODE = RC.EMP_CODE
    AND TD.SHEDULE_DT = RC.SHEDULE_DT
  WHERE (E.SF_DATE IS NULL OR TO_CHAR(TD.SHEDULE_DT, 'YYYYMMDD') >=
        TO_CHAR(E.SF_DATE, 'YYYYMMDD'))
    AND (E.LAST_ZNO IS NULL OR TO_CHAR(TD.SHEDULE_DT, 'YYYYMMDD') >=
        TO_CHAR(E.DATE_FROM, 'YYYYMMDD'))
    AND (E.DIMISSION_DT IS NULL OR TO_CHAR(E.DIMISSION_DT, 'YYYYMMDD') >
        TO_CHAR(TD.SHEDULE_DT, 'YYYYMMDD'))
    AND (E.TRANSFER_DATE IS NULL OR TO_CHAR(TD.SHEDULE_DT, 'YYYYMMDD') >=
        TO_CHAR(E.TRANSFER_DATE, 'YYYYMMDD'))
    AND TO_CHAR(TD.SHEDULE_DT, 'YYYYMMDD') >=
        TO_CHAR(E.SF_DATE, 'YYYYMMDD')
    and e.emp_post_type = '1';
        COMMIT;
        
        
        
    DELETE TI_TCAS_SPMS_SCHEDULE TMP
     WHERE tmp.Position_Type = '2'
     and tmp.work_date = to_date(to_char(sysdate -1,'yyyy-mm-dd'),'yyyy-mm-dd');
       
    INSERT INTO TI_TCAS_SPMS_SCHEDULE
      (SCHEDULE_ID,
       EMP_CODE,
       EMP_NAME,
       AREA_CODE,
       DEPT_CODE,
       GROUP_CODE,
       WORK_DATE,
       WORK_TIME,
       JOB_SEQ_CODE,
       JOB_SEQ,
       POSITION_TYPE,
       PERSON_TYPE,
       CREAT_EMP_CODE,
       CREAT_TIME,
       MODIFY_EMP_CODE,
       MODIFY_TIME,
       KQ_XSS,
       STDAZ,
       ARBST,
       PAPER)
         SELECT SEQ_TI_TCAS_SPMS_SCHEDULE.NEXTVAL,
             E.EMP_CODE,
             E.EMP_NAME,
             NVL(D.AREA_CODE, EMP_DEPT.AREA_CODE),
             NVL(D.DEPT_CODE, EMP_DEPT.DEPT_CODE),
             '',
              TO_DATE(ts.day_of_month, 'YYYYMMDD') BEGDA,
            decode(TI.TRAINING_CODE,null,decode(ts.scheduling_code,'休',0,1) ,0),
             '',
             '',
             2,
             E.PERSK_TXT,
             'ADMIN',
             SYSDATE,
             'ADMIN',
             SYSDATE,
             '',
             '',
             '',
             to_char(sysdate-1,'yyyy-mm')
        from TM_OSS_EMPLOYEE E
        JOIN TM_DEPARTMENT EMP_DEPT
          ON E.DEPT_ID = EMP_DEPT.DEPT_ID
        JOIN (SELECT DISTINCT EMPLOYEE_CODE,
                                   DEPARTMENT_CODE,
                                   DAY_OF_MONTH,
                                   d.scheduling_code
                     FROM TT_SCHEDULE_DAILY d
                    WHERE EMP_POST_TYPE = '3') TS
          ON e.emp_code = TS.EMPLOYEE_CODE
         AND TS.DAY_OF_MONTH =  to_char(sysdate -1, 'yyyymmdd')   
        LEFT JOIN TT_PB_TRAINING_INFO TI
          ON e.emp_code = TI.EMPLOYEE_CODE
         AND  TI.DAY_OF_MONTH = to_char(sysdate -1, 'yyyy-mm-dd')  
        LEFT JOIN TM_DEPARTMENT D
          ON TS.DEPARTMENT_CODE = D.DEPT_CODE
       WHERE E.IS_HAVE_COMMISSION = 1
         and e.emp_post_type = '3';
                 COMMIT;
      
    END IF;  
      
      

  END;
  --4.结束记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'HANDLE_SAP_ATTENCE',
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
                                 'HANDLE_SAP_ATTENCE',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
END HANDLE_SAP_ATTENCE;
/