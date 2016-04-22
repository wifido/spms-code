CREATE OR REPLACE PROCEDURE WAREH_SCHEDUL_AGREEMENT(YEAR_MONTH in varchar) IS

  DEPT_CODE         VARCHAR2(20);
  AGREE_RATE        NUMBER;
  AGREE_COUNT       NUMBER;
  TOTAL_COUNT       NUMBER;
  TOTAL_DAY         NUMBER;
  WORKING_EMP_COUNT NUMBER;
  MAXDATE           DATE;
  MINDATE           DATE;
  MODIFY_COUNT      NUMBER;
  v_YEAR_MONTH      VARCHAR2(10);

  --1.定义执行序号
  L_CALL_NO NUMBER;

BEGIN
  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'WAREH_SCHEDUL_AGREEMENT',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

  -- 查询设置当月总天数
  V_YEAR_MONTH := YEAR_MONTH;
  SELECT ADD_MONTHS(TO_DATE(V_YEAR_MONTH, 'YYYYMM'), 1) -
         TO_DATE(V_YEAR_MONTH, 'YYYYMM')
    INTO TOTAL_DAY
    FROM DUAL;

  -- 删除历史数据

  DELETE TT_WAREH_SCHEDULED_AGREE_RATE E WHERE E.YEAR_MONTH = V_YEAR_MONTH;

  -- 循环当月所有网点的排班数据，已去重
  FOR SCH_ROW IN (select distinct d.dept_code, d.DEPT_ID
                    from tm_oss_employee e, tm_department d
                   where e.dept_id = d.dept_id
                     and e.emp_post_type = '3'
                     and (e.dimission_dt is null or e.dimission_dt > sysdate)) LOOP
    -- 设置初始值
    AGREE_COUNT := 0;
    AGREE_RATE  := 0;
    TOTAL_COUNT := 0;
    DEPT_CODE   := SCH_ROW.dept_code;
  
    -- 计算排班总量(通过网点、月份 查询当前网点所有的人员信息，包括 转网、转岗、离职时间)
    FOR EMP_ROW IN (SELECT T.EMP_CODE,
                           T.MAX_DATE,
                           T.DIMISSION_DT,
                           T.DEPT_ID,
                           T.dept_code,
                           TO_DATE(T.MONTH_ID, 'YYYYMM') MONTH_DAY,
                           T.MONTH_ID
                      FROM (SELECT DISTINCT EMP.EMP_CODE,
                                            GREATEST(NVL(EMP.SF_DATE,
                                                         SYSDATE - 10000),
                                                     NVL(EMP.DATE_FROM,
                                                         SYSDATE - 10000),
                                                     NVL(EMP.TRANSFER_DATE,
                                                         SYSDATE - 10000),
                                                     TRUNC(to_date(V_YEAR_MONTH,
                                                                   'YYYYMM'),
                                                           'MM')) MAX_DATE,
                                            d.month_id,
                                            dept.dept_code,
                                            emp.dimission_dt,
                                            dept.dept_id
                              FROM TM_OSS_EMPLOYEE   EMP,
                                   TM_DEPARTMENT     DEPT,
                                   TT_SCHEDULE_DAILY d
                             WHERE EMP.DEPT_ID = DEPT.DEPT_ID
                               and emp.dept_id = SCH_ROW.DEPT_ID
                               AND EMP.EMP_POST_TYPE = 3
                               and d.employee_code = emp.emp_code
                               AND d.MONTH_ID = V_YEAR_MONTH) T) LOOP
      MAXDATE := LAST_DAY(SYSDATE);
      -- 当已离职
      IF EMP_ROW.DIMISSION_DT IS NOT NULL THEN
        -- 离职日期小于当前时间
        IF EMP_ROW.DIMISSION_DT < LAST_DAY(SYSDATE) THEN
          MAXDATE := EMP_ROW.DIMISSION_DT;
        END IF;
        MINDATE := EMP_ROW.MAX_DATE;
        IF MAXDATE > MINDATE THEN
          TOTAL_COUNT := TOTAL_COUNT + ROUND(TO_NUMBER(MAXDATE - MINDATE));
        END IF;
      ELSE
        IF EMP_ROW.MONTH_DAY >= EMP_ROW.MAX_DATE THEN
          TOTAL_COUNT := TOTAL_COUNT + TOTAL_DAY;
        ELSE
          TOTAL_COUNT := TOTAL_COUNT +
                         (TOTAL_DAY - TO_CHAR(EMP_ROW.MAX_DATE, 'DD') + 1);
        END IF;
      END IF;
    
    END LOOP;
  
    -- 设置修改次数
    SELECT DECODE(SUM(L.MODIFY_DAY_COUNT), NULL, 0, SUM(L.MODIFY_DAY_COUNT)) MODIFY_COUNT
      INTO MODIFY_COUNT
      FROM TT_WAREH_SCHEDULED_MODIFY_LOG L, TM_OSS_EMPLOYEE EMP
     WHERE L.EMPLOYEE_CODE = EMP.EMP_CODE
       AND L.DEPARTMENT_ID = EMP.DEPT_ID
       AND EMP.EMP_POST_TYPE = 3
       AND L.YEAR_MONTH = V_YEAR_MONTH
       AND L.DEPARTMENT_ID = SCH_ROW.DEPT_ID;
  
    IF TOTAL_COUNT = 0 THEN
      AGREE_COUNT := 0;
      AGREE_RATE  := 0;
    ELSE
      IF MODIFY_COUNT >= TOTAL_COUNT THEN
        AGREE_COUNT := 0;
        AGREE_RATE  := 0;
      ELSE
        AGREE_COUNT := TOTAL_COUNT - MODIFY_COUNT;
        AGREE_RATE  := ROUND(AGREE_COUNT / TOTAL_COUNT, 4);
      END IF;
    END IF;
  
    WORKING_EMP_COUNT := 0;
  
    select count(1)
      INTO WORKING_EMP_COUNT
      from tm_oss_employee emp
     where (emp.dimission_dt is null or emp.dimission_dt > sysdate)
       and emp.dept_id = SCH_ROW.dept_id
       and emp.emp_post_type = 3;
  
    INSERT INTO TT_WAREH_SCHEDULED_AGREE_RATE
      (ID,
       YEAR_MONTH,
       DEPARTMENT_CODE,
       SCHED_AGREE_RATE,
       SCHED_AGREE_COUNT,
       working_emp_count,
       SCHED_TOTAL)
    VALUES
      (SEQ_WAREH_SCHED_MODIFY_LOG.NEXTVAL,
       V_YEAR_MONTH,
       DEPT_CODE,
       AGREE_RATE,
       AGREE_COUNT,
       WORKING_EMP_COUNT,
       TOTAL_COUNT);
  
  END LOOP;

  COMMIT;
  --4.结束记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'WAREH_SCHEDUL_AGREEMENT',
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
                                 'WAREH_SCHEDUL_AGREEMENT',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
END WAREH_SCHEDUL_AGREEMENT;
/