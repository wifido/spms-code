CREATE OR REPLACE PROCEDURE SPMS_TO_CDP_BY_WAREHOUSE(V_COUNT IN NUMBER) IS
  --*************************************************************
  -- AUTHOR  : JHC
  -- CREATED : 20V_COUNT5-03-05
  -- PURPOSE : 统计昨天到未来三天的仓管在职人数与排班数
  --
  -- PARAMETER:
  -- NAME             TYPE            DESC
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************
  --V_COUNT.定义执行序号
  L_CALL_NO NUMBER;
BEGIN
  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SPMS_TO_CDP_BY_WAREHOUSE',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);
  INSERT INTO TT_CDP_EMPNUM_SCHEDULINGNUM
    (DAY_OF_MONTH,
     HQ_CODE,
     AREA_CODE,
     DEPT_CODE,
     DIVISION_CODE,
     FULL_TIME_NUM,
     N_FULL_TIME_NUM,
     TOTAL_PAYROLLS,
     FULL_TIME_SCHEDUL_NUM,
     N_FULL_TIME_SCHEDUL_NUM,
     SCHEDUL_NUM_TOTAL,
     POST_TYPE,
     CREATE_DATE,
     post_type_key)
  
    WITH A AS
     (SELECT E.EMP_CODE,
             D.DEPT_ID,
             D.DEPT_CODE,
             D.AREA_CODE,
             D.HQ_CODE,
             D.DIVISION_CODE,
             E.PERSG,
             DATA_SOURCE
        FROM TM_OSS_EMPLOYEE E
        JOIN TM_DEPARTMENT D
          ON E.DEPT_ID = D.DEPT_ID
       WHERE E.EMP_POST_TYPE = '3'
            
         AND (E.DIMISSION_DT > SYSDATE + V_COUNT OR E.DIMISSION_DT IS NULL)
         AND E.EMP_DUTY_NAME IN ('仓管员', '仓管组长')),
    --在职人数
    NUMberTime AS
     (SELECT COUNT(1) FULL_TIME,
             A.DEPT_CODE,
             A.AREA_CODE,
             A.HQ_CODE,
             A.DIVISION_CODE
        FROM A
       GROUP BY A.DEPT_CODE, A.AREA_CODE, A.HQ_CODE, A.DIVISION_CODE),
    --全日制在职人数
    FTN AS
     (SELECT COUNT(V_COUNT) FULL_TIME,
             A.DEPT_CODE,
             A.AREA_CODE,
             A.HQ_CODE,
             A.DIVISION_CODE
        FROM A
       WHERE (A.PERSG = 'A' AND A.DATA_SOURCE = '2')
          OR A.DATA_SOURCE = '3'
       GROUP BY A.DEPT_CODE, A.AREA_CODE, A.HQ_CODE, A.DIVISION_CODE),
    -- 非全在职
    NFTN AS
     (SELECT COUNT(V_COUNT) NO_FULL_TIME, A.DEPT_CODE
        FROM A
       WHERE A.PERSG = 'C'
         AND A.DATA_SOURCE = '2'
       GROUP BY A.DEPT_CODE),
    
    FTSC_V AS
     (SELECT SD.DEPARTMENT_CODE DEPT_CODE, SD.EMPLOYEE_CODE
        FROM TT_SCHEDULE_DAILY SD, TM_OSS_EMPLOYEE E
       WHERE E.EMP_CODE = SD.EMPLOYEE_CODE
         AND SD.DAY_OF_MONTH = TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD')
         AND SD.EMP_POST_TYPE = '3'
         AND SD.SCHEDULING_CODE != '休'
         AND E.EMP_DUTY_NAME IN ('仓管员', '仓管组长')
         AND ((E.PERSG = 'A' AND E.DATA_SOURCE = '2') OR E.DATA_SOURCE = '3')
       GROUP BY SD.DEPARTMENT_CODE, SD.EMPLOYEE_CODE),
    --全日制排班人数
    FTSC AS
     (SELECT DEPT_CODE, COUNT(1) FULL_TIME_SCHE_COUNT
        FROM FTSC_V
       GROUP BY DEPT_CODE),
    
    NFTSC_V AS
     (SELECT SD.DEPARTMENT_CODE DEPT_CODE, SD.EMPLOYEE_CODE
        FROM TT_SCHEDULE_DAILY SD, TM_OSS_EMPLOYEE E
       WHERE E.EMP_CODE = SD.EMPLOYEE_CODE
         AND SD.DAY_OF_MONTH = TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD')
         AND SD.EMP_POST_TYPE = '3'
         AND SD.SCHEDULING_CODE != '休'
         AND E.EMP_DUTY_NAME IN ('仓管员', '仓管组长')
         AND (E.PERSG = 'C' AND E.DATA_SOURCE = '2')
       GROUP BY SD.DEPARTMENT_CODE, SD.EMPLOYEE_CODE),
    --非全日制排班
    NFTSC AS
     (SELECT DEPT_CODE, COUNT(1) NO_FULL_TIME_SC
        FROM NFTSC_V
       GROUP BY DEPT_CODE),
    -- 非全排班数
    NFTSCNUM AS
     (SELECT DEPT_CODE, SUM(NO_FULL_TIME_CN) NO_FULL_TIME_CN
        FROM (SELECT A.DEPT_CODE DEPT_CODE,
                     NVL(ROUND(SUM(COUNT_TIME_DISTANCE(TB.START1_TIME,
                                                       TB.END1_TIME)) / 9,
                               2),
                         0) NO_FULL_TIME_CN
              
                FROM TT_SCHEDULE_DAILY T
                JOIN A
                  ON T.EMPLOYEE_CODE = A.EMP_CODE
                 AND T.DEPARTMENT_CODE = A.DEPT_CODE
                JOIN TM_PB_SCHEDULE_BASE_INFO TB
                  ON (TB.DEPT_ID = A.DEPT_ID AND
                     T.SCHEDULING_CODE = TB.SCHEDULE_CODE)
               WHERE T.DAY_OF_MONTH = TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD')
                 AND T.EMP_POST_TYPE = '3'
                 AND TB.CLASS_TYPE = '2'
                 AND T.SCHEDULING_CODE <> '休'
                 AND A.PERSG = 'C'
                 AND A.DATA_SOURCE = '2'
               GROUP BY DEPT_CODE
              UNION ALL
              SELECT DISTINCT DEPT_CODE,
                              NVL(ROUND(SUM(COUNT_TIME_DISTANCE(TB.START1_TIME,
                                                                TB.END1_TIME)) / 9,
                                        2),
                                  0) NO_FULL_TIME_CN
                FROM TT_SCHEDULE_DAILY T
                JOIN TM_DEPARTMENT D
                  ON T.DEPARTMENT_CODE = D.DEPT_CODE
                JOIN TM_OSS_EMPLOYEE E
                  ON E.EMP_CODE = T.EMPLOYEE_CODE
                JOIN TM_PB_SCHEDULE_BASE_INFO TB
                  ON (D.DEPT_ID = TB.DEPT_ID AND
                     T.SCHEDULING_CODE = TB.SCHEDULE_CODE)
               WHERE T.DAY_OF_MONTH = TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD')
                 AND TB.CLASS_TYPE = '2'
                 AND T.SCHEDULING_CODE <> '休'
                 AND E.PERSG = 'C'
                 AND E.DATA_SOURCE = '2'
                 AND E.DEPT_ID <> D.DEPT_ID
               GROUP BY DEPT_CODE)
       GROUP BY DEPT_CODE)
    SELECT TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD'),
           NUMberTime.HQ_CODE,
           NUMberTime.AREA_CODE,
           NUMberTime.DEPT_CODE,
           NUMberTime.DIVISION_CODE,
           NVL(FTN.FULL_TIME, 0),
           NVL(NFTN.NO_FULL_TIME, 0),
           NVL(ROUND(NVL(FTN.FULL_TIME,0) + (NVL(NFTN.NO_FULL_TIME, 0) * 4 / 9), 2),
               0),
           NVL(FTSC.FULL_TIME_SCHE_COUNT, 0),
           NVL(NFTSC.NO_FULL_TIME_SC, 0),
           NVL(NFTSCNUM.NO_FULL_TIME_CN, 0) +
           NVL(FTSC.FULL_TIME_SCHE_COUNT, 0),
           '仓管',
           SYSDATE,
           3
      FROM NUMberTime
      LEFT JOIN FTN
        ON NUMberTime.DEPT_CODE = FTN.DEPT_CODE
      LEFT JOIN NFTN
        ON NUMberTime.DEPT_CODE = NFTN.DEPT_CODE
      LEFT JOIN FTSC
        ON NUMberTime.DEPT_CODE = FTSC.DEPT_CODE
      LEFT JOIN NFTSC
        ON NUMberTime.DEPT_CODE = NFTSC.DEPT_CODE
      LEFT JOIN NFTSCNUM
        ON NUMberTime.DEPT_CODE = NFTSCNUM.DEPT_CODE;

  COMMIT;
  --4.结束记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SPMS_TO_CDP_BY_WAREHOUSE',
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
                                 'SPMS_TO_CDP_BY_WAREHOUSE',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
END SPMS_TO_CDP_BY_WAREHOUSE;
/