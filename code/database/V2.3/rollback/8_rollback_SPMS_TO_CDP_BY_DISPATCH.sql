CREATE OR REPLACE PROCEDURE SPMS_TO_CDP_BY_DISPATCH(V_COUNT IN NUMBER) IS
  --*************************************************************
  -- AUTHOR  : JHC
  -- CREATED : 2015-03-05
  -- PURPOSE : 统计未来三天的一线、仓管在职人数与排班数
  --
  -- PARAMETER:
  -- NAME             TYPE            DESC
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************

  L_CALL_NO NUMBER;
BEGIN

  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SPMS_TO_CDP_BY_DISPATCH',
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
       POST_TYPE_KEY)
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
   WHERE E.EMP_POST_TYPE = '2'
     AND (E.DIMISSION_DT > SYSDATE + V_COUNT  OR E.DIMISSION_DT IS NULL)
     ),
--全日制在职人数
 FTN AS
 (SELECT COUNT(1) FULL_TIME,  A.DEPT_CODE,
         A.AREA_CODE,
         A.HQ_CODE,
         A.DIVISION_CODE
    FROM A
   where (A.PERSG = 'A' and a.DATA_SOURCE = '2') OR A.DATA_SOURCE = '3'
   GROUP BY  A.DEPT_CODE,
         A.AREA_CODE,
         A.HQ_CODE,
         A.DIVISION_CODE),
-- 非全在职
NFTN AS
 (SELECT COUNT(1) NO_FULL_TIME, A.DEPT_CODE
    FROM A
   where A.PERSG = 'C' and a.DATA_SOURCE = '2'
   GROUP BY A.DEPT_CODE),

--全日制排班人数
FTSC AS
 (SELECT da.department_code DEPT_CODE,
         COUNT(DISTINCT da.employee_code) FULL_TIME_SCHE_COUNT
    FROM TT_SCHEDULE_DAILY DA
    JOIN A
      ON DA.DEPARTMENT_CODE = A.DEPT_CODE
     AND A.EMP_CODE = DA.EMPLOYEE_CODE
     AND ((A.PERSG = 'A' and a.DATA_SOURCE = '2') OR A.DATA_SOURCE = '3')
     AND DA.BEGIN_TIME IS NOT NULL
     AND DA.END_TIME IS NOT NULL
     and da.day_of_month = to_char(SYSDATE + V_COUNT , 'yyyymmdd')
   group by da.department_code),
-- 非全排班数
NFTSC AS
 (SELECT da.department_code DEPT_CODE,
         count(distinct da.employee_code) NO_FULL_TIME_SCHE_COUNT
    FROM TT_SCHEDULE_DAILY DA
    JOIN A
      ON DA.DEPARTMENT_CODE = A.DEPT_CODE
     AND A.EMP_CODE = DA.EMPLOYEE_CODE
     AND A.PERSG = 'C'
     AND a.DATA_SOURCE = '2'
     AND DA.BEGIN_TIME IS NOT NULL
     AND DA.END_TIME IS NOT NULL
     and da.day_of_month = to_char(SYSDATE +V_COUNT , 'yyyymmdd')
     and da.employee_code not in (SELECT TC.EMPLOYEEID
                FROM TI_SCH_EMPLOYEECLASS_PLAIN TC
               WHERE TC.DUTYDATE = TO_CHAR(SYSDATE +V_COUNT , 'YYYY-MM-DD'))
   group by da.department_code),

-- 非全排班数
NFTSCNUM AS
 (SELECT da.department_code DEPT_CODE,
         NVL(ROUND(SUM((CASE
                         WHEN DA.END_TIME - DA.BEGIN_TIME > 0 THEN
                          DA.END_TIME - DA.BEGIN_TIME
                         ELSE
                          (DA.END_TIME + 240000) - DA.BEGIN_TIME
                       END) / 10000) / 9,
                   2),
             0) g_NUM_F
    FROM TT_SCHEDULE_DAILY DA
    JOIN A
      ON DA.DEPARTMENT_CODE = A.DEPT_CODE
     AND A.EMP_CODE = DA.EMPLOYEE_CODE
     AND A.PERSG = 'C'
     AND a.DATA_SOURCE = '2'
     AND DA.BEGIN_TIME IS NOT NULL
     AND DA.END_TIME IS NOT NULL
     and da.day_of_month = to_char(SYSDATE +V_COUNT , 'yyyymmdd')
     and da.employee_code not in (SELECT TC.EMPLOYEEID
                FROM TI_SCH_EMPLOYEECLASS_PLAIN TC
               WHERE TC.DUTYDATE = TO_CHAR(SYSDATE +V_COUNT , 'YYYY-MM-DD'))
   group by da.department_code),
   FTSCNTC AS
 (SELECT da.department_code DEPT_CODE,
         COUNT(DISTINCT da.employee_code) P_NUM_Q
    FROM TT_SCHEDULE_DAILY DA
    JOIN A
      ON DA.DEPARTMENT_CODE = A.DEPT_CODE
     AND A.EMP_CODE = DA.EMPLOYEE_CODE
     AND ((A.PERSG = 'A' and a.DATA_SOURCE = '2') OR A.DATA_SOURCE = '3')
     AND DA.BEGIN_TIME IS NOT NULL
     AND DA.END_TIME IS NOT NULL
     and da.day_of_month = to_char(SYSDATE +V_COUNT , 'yyyymmdd')
     and da.employee_code not in (SELECT TC.EMPLOYEEID
                FROM TI_SCH_EMPLOYEECLASS_PLAIN TC
               WHERE TC.DUTYDATE = TO_CHAR(SYSDATE +V_COUNT , 'YYYY-MM-DD'))
   group by da.department_code),
     TC AS
 ( SELECT A.DEPT_CODE ,COUNT(a.EMP_CODE) P_NUM_Q
    FROM TI_SCH_EMPLOYEECLASS_PLAIN PLA
    JOIN A
      ON PLA.EMPLOYEEID = A.EMP_CODE
  WHERE
      PLA.DUTYDATE= to_char(SYSDATE +V_COUNT , 'yyyy-mm-dd')
    GROUP by A.DEPT_CODE )
     SELECT
    TO_CHAR(SYSDATE + V_COUNT, 'YYYYMMDD'),
   HQ_CODE,
    AREA_CODE,
    ftn.DEPT_CODE,
    DIVISION_CODE,
       NVL(FTN.FULL_TIME, 0),
       NVL(NFTN.NO_FULL_TIME, 0) ,
       NVL(ROUND(FTN.FULL_TIME + (nvL(NFTN.NO_FULL_TIME,0) * 4 / 9), 2), 0) ,
       NVL(FTSCNTC.P_NUM_Q,0) +   nvl(TC.P_NUM_Q,0),
       NVL(NFTSC.NO_FULL_TIME_SCHE_COUNT, 0),

       nvl(NFTSCNUM.g_NUM_F,0) +
       NVL(FTSCNTC.P_NUM_Q,0) +  nvl(TC.P_NUM_Q,0),
            '一线',
       SYSDATE,
       2
  from FTN
  left JOIN NFTN
    ON FTN.DEPT_CODE = NFTN.DEPT_CODE
  left joIN FTSC
    ON FTN.DEPT_CODE =FTSC.DEPT_CODE
  leFt join NFTSC
    ON FTN.DEPT_CODE = NFTSC.DEPT_CODE
  leFt join NFTSCNUM
    ON FTN.DEPT_CODE =NFTSCNUM.DEPT_CODE
  LEFT JOIN FTSCNTC
  ON FTN.DEPT_CODE = FTSCNTC.DEPT_CODE
  LEFT JOIN TC
  on FTN.dept_code = TC.dept_code;
  COMMIT;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SPMS_TO_CDP_BY_DISPATCH',
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
                                 'SPMS_TO_CDP_BY_DISPATCH',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
END SPMS_TO_CDP_BY_DISPATCH;
/