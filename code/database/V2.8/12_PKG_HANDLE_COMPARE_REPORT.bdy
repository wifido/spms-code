CREATE OR REPLACE PACKAGE BODY PKG_HANDLE_COMPARE_REPORT IS
  ---处理排班数据触发的数据对比任务
  PROCEDURE P_REPORT_CAUSE_BY_SCHEDULE IS
    --*************************************************************
    -- AUTHOR  : 张艺
    -- CREATED : 2015-09-08
    -- PURPOSE : 排班与行车日志异常对比报表执行任务;但是只处理排班数据触发的数据对比;
    --
    -- PARAMETER:
    -- NAME             TYPE            DESC
    --
    -- MODIFY HISTORY
    -- PERSON                    DATE                        COMMENTS
    -- -------------------------------------------------------------
    --
    --*************************************************************
  
    --排班中的线路条数;
    L_SCHEDULE_LINE_COUNT NUMBER;
    -- 临时存储线路网点的字段;
    L_DEPT_CODE VARCHAR2(2000);
    -- 行车日志中的线路条数;
    L_DRIVING_LINE_COUNT NUMBER;
    -- 最终结果;
    L_FINAL_RESULT NUMBER;
    -- 网点名称
    L_DEPT_NAME VARCHAR2(50);
    -- 区部代码
    L_AREA_CODE VARCHAR2(50);
    -- 区部名称
    L_AREA_NAME VARCHAR2(50);
    -- 员工姓名
    L_EMP_NAME VARCHAR2(50);
    --已存在报表数据;
    EXIST_REPORT NUMBER;
    ----行车日志对应的ID;
    L_LINE_ID VARCHAR2(21);
  
    --1.定义执行序号
    L_CALL_NO NUMBER;
  BEGIN
    --2.设置执行序号
    SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
    --3.开始记录日志
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                 'P_REPORT_CAUSE_BY_SCHEDULE',
                                 SYSDATE,
                                 NULL,
                                 NULL,
                                 'START',
                                 0,
                                 L_CALL_NO);
    --锁定一部分数据
    UPDATE TT_TASK_COMPARE SET SYNC_STATUS = 1;
    COMMIT;
  
    --查询计划任务表中的数据;为防止数据量大,需要控制每次执行的数量
    FOR V_TEMP IN (SELECT * FROM TT_TASK_COMPARE WHERE SYNC_STATUS = 1) LOOP
      ----CLEAN PARAMETER WITH DEFAULT-------------------------------------------------------------------------------------------------------------------
    
      -- 最终结果;
      L_FINAL_RESULT := -1;
      -- 网点名称
      L_DEPT_NAME := '';
      -- 区部代码
      L_AREA_CODE := '';
      -- 区部名称
      L_AREA_NAME := '';
      -- 员工名称;
      L_EMP_NAME   := '';
      EXIST_REPORT := 0;
      L_LINE_ID    := -1;
      --不是删除操作时;
      IF V_TEMP.OPERATION_TYPE <> 2 THEN
        -- 1.查询该日期对应的行车日志;
        SELECT COUNT(*)
          INTO L_SCHEDULE_LINE_COUNT
          FROM TT_DRIVER_LINE_CONFIGURE T, TT_DRIVER_LINE_CONFIGURE_R TT
         WHERE DEPARTMENT_CODE =
               substr(V_TEMP.CONFIGURE_CODE,
                      0,
                      instr(V_TEMP.CONFIGURE_CODE, '-') - 1)
           AND CODE = substr(V_TEMP.CONFIGURE_CODE,
                             instr(V_TEMP.CONFIGURE_CODE, '-') + 1)
           AND T.ID = TT.CONFIGURE_ID
           AND T.MONTH = V_TEMP.YEAR_MONTH;
      
      ELSE
        L_SCHEDULE_LINE_COUNT := 0;
      END IF;
    
      -- 2. 查询行车日志该日期下的线路条数;
      SELECT MAX(DEPT_CODE), MAX(ID)
        INTO L_DEPT_CODE, L_LINE_ID
        FROM TI_DRIVER_LOG_TMP
       WHERE DRIVE_MEMBER = V_TEMP.EMPLOYEE_CODE
         AND DAY_OF_MONTH = REPLACE(V_TEMP.DAY_OF_MONTH, '-', '');
    
      -- 计算被拼接的线路中的条数;
      IF L_DEPT_CODE IS NULL THEN
        L_DRIVING_LINE_COUNT := 0;
      ELSE
        SELECT LENGTH(L_DEPT_CODE) - LENGTH(REPLACE(L_DEPT_CODE, '-'))
          INTO L_DRIVING_LINE_COUNT
          FROM DUAL;
      END IF;
    
      -- 3.判断最终对比结果;
    
      /**
      * "已排班未行车", "已行车未排班", "排班大于行车线路", "排班少于行车线路"
      *
      **/
      ------------------------------------------SPLIT LINE -------------------------------------------------------------------
      -----获取到最终结果;
      IF L_SCHEDULE_LINE_COUNT = 0 THEN
        ---排版和行车都为0;只有可能在删除排班时出现在对比结果;
        IF L_DRIVING_LINE_COUNT = 0 THEN
          L_FINAL_RESULT := 5;
        ELSE
          ---未排班已行车;
          L_FINAL_RESULT := 2;
        END IF;
      ELSE
        IF L_SCHEDULE_LINE_COUNT = L_DRIVING_LINE_COUNT THEN
          --对比无异常;
          L_FINAL_RESULT := 0;
        ELSE
          IF L_DRIVING_LINE_COUNT = 0 THEN
            L_FINAL_RESULT := 1;
          ELSE
            IF L_SCHEDULE_LINE_COUNT < L_DRIVING_LINE_COUNT THEN
              L_FINAL_RESULT := 4;
            ELSE
              L_FINAL_RESULT := 3;
            END IF;
          END IF;
        END IF;
      END IF;
    
      --查询出网点对应的名称,区部名称;
      SELECT MAX(T.DEPT_NAME), MAX(T.AREA_CODE), MAX(TT.DEPT_NAME)
        INTO L_DEPT_NAME, L_AREA_CODE, L_AREA_NAME
        FROM TM_DEPARTMENT T, TM_DEPARTMENT TT
       WHERE T.DEPT_CODE = V_TEMP.DEPT_CODE
         AND T.DELETE_FLG = 0
         AND T.AREA_CODE = TT.DEPT_CODE;
    
      --查询员工姓名;
      SELECT MAX(T.EMP_NAME)
        INTO L_EMP_NAME
        FROM TM_OSS_EMPLOYEE T
       WHERE T.EMP_CODE = V_TEMP.EMPLOYEE_CODE;
    
      SELECT COUNT(*)
        INTO EXIST_REPORT
        FROM TT_COMPARED_REPORT
       WHERE EMPLOYEE_CODE = V_TEMP.EMPLOYEE_CODE
         AND DAY_MONTH = V_TEMP.DAY_OF_MONTH;
    
      IF EXIST_REPORT = 0 THEN
        INSERT INTO TT_COMPARED_REPORT
          (ID,
           ---------------
           EMPLOYEE_CODE,
           EMPLOYEE_NAME,
           -------------------
           DEPARTMENT_CODE,
           DEPARTMENT_NAME,
           ------------------
           AREA_CODE,
           AREA_NAME,
           ------------------
           COMPARE_RESULT,
           DAY_MONTH,
           MODIFIED_TIME)
        VALUES
          (V_TEMP.ID,
           ----------------
           V_TEMP.EMPLOYEE_CODE,
           L_EMP_NAME,
           ---------------
           V_TEMP.DEPT_CODE,
           L_DEPT_NAME,
           ----------------
           L_AREA_CODE,
           L_AREA_NAME,
           -------------------
           L_FINAL_RESULT,
           V_TEMP.DAY_OF_MONTH,
           SYSDATE);
      ELSE
        UPDATE TT_COMPARED_REPORT
           SET COMPARE_RESULT = L_FINAL_RESULT, MODIFIED_TIME = SYSDATE
         WHERE EMPLOYEE_CODE = V_TEMP.EMPLOYEE_CODE
           AND DAY_MONTH = V_TEMP.DAY_OF_MONTH;
      END IF;
    
      --写入报表数据;
      UPDATE TI_DRIVER_LOG_TMP SET COMPARE_STATUS = 1 WHERE ID = L_LINE_ID;
    END LOOP;
    ------------------------------------------SPLIT LINE -------------------------------------------------------------------
  
    DELETE TT_TASK_COMPARE WHERE SYNC_STATUS = 1;
    COMMIT;
    --4.结束记录日志
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                 'P_REPORT_CAUSE_BY_SCHEDULE',
                                 SYSDATE,
                                 NULL,
                                 NULL,
                                 'END',
                                 0,
                                 L_CALL_NO);
  
    --5.异常记录日志
  EXCEPTION
    WHEN OTHERS THEN
      UPDATE TT_TASK_COMPARE SET SYNC_STATUS = 0 WHERE SYNC_STATUS = 1;
      COMMIT;
      --回滚数据
      ROLLBACK;
      PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                   'P_REPORT_CAUSE_BY_SCHEDULE',
                                   SYSDATE,
                                   SQLCODE,
                                   SQLERRM,
                                   'ERROR',
                                   0,
                                   '');
  END P_REPORT_CAUSE_BY_SCHEDULE;

  --处理行车数据触发的数据对比任务
  PROCEDURE P_REPORT_CAUSE_BY_DRIVING IS
    --*************************************************************
    -- AUTHOR  : 张艺
    -- CREATED : 2015-09-08
    -- PURPOSE : 排班与行车日志异常对比报表执行任务;但是只处理行车日志数据触发的数据对比;
    --
    -- PARAMETER:
    -- NAME             TYPE            DESC
    --
    -- MODIFY HISTORY
    -- PERSON                    DATE                        COMMENTS
    -- -------------------------------------------------------------
    --
    --*************************************************************
    --- 当前年月
    TEMP_YEAR_MONTH VARCHAR2(20);
  
    --1.定义执行序号
    L_CALL_NO NUMBER;
  BEGIN
    TEMP_YEAR_MONTH := TO_CHAR(SYSDATE, 'YYYYMM');
  
    --2.设置执行序号
    SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
    --3.开始记录日志
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                 'P_REPORT_CAUSE_BY_DRIVING',
                                 SYSDATE,
                                 NULL,
                                 NULL,
                                 'START',
                                 0,
                                 L_CALL_NO);
  
    -- 更新数据为处理中
    update ti_driver_log_tmp t
       set t.compare_status = 3
     where t.compare_status = 0
       and substr(t.day_of_month, 0, 6) = TEMP_YEAR_MONTH;
  
    COMMIT;
  
    execute immediate 'truncate table TT_COMPARED_REPORT_TEMP';
  
    INSERT INTO TT_COMPARED_REPORT_TEMP
      SELECT J.ID,
             R.EMP_CODE,
             R.EMP_NAME,
             R.DEPT_CODE,
             R.DEPT_NAME,
             R.AREA_CODE,
             R.AREA_NAME,
             CASE
               WHEN L_SCHEDULE_LINE_COUNT <> 0 THEN
                CASE
                  WHEN L_DRIVING_LINE_COUNT = 0 THEN
                   1
                  WHEN L_DRIVING_LINE_COUNT <> 0 THEN
                   CASE
                     WHEN L_SCHEDULE_LINE_COUNT = L_DRIVING_LINE_COUNT THEN
                      0
                     WHEN L_SCHEDULE_LINE_COUNT > L_DRIVING_LINE_COUNT THEN
                      3
                     ELSE
                      4
                   END
                END
             -- 有排班
               WHEN L_SCHEDULE_LINE_COUNT = 0 THEN
                CASE
                  WHEN L_DRIVING_LINE_COUNT = 0 THEN
                   5
                  ELSE
                   2
                END --无排班
               ELSE
                2
             END COMPARE_RESULT,
             J.DAY_OF_MONTH,
             SYSDATE,
             SYSDATE
        FROM (SELECT MAX(P.ID) ID,
                     LENGTH(MAX(P.DEPT_CODE)) -
                     LENGTH(REPLACE(MAX(P.DEPT_CODE), '-')) L_DRIVING_LINE_COUNT,
                     P.DRIVE_MEMBER,
                     P.DAY_OF_MONTH
                FROM TI_DRIVER_LOG_TMP P
               GROUP BY P.DRIVE_MEMBER, P.DAY_OF_MONTH) J,
             (SELECT V_TEMP.DRIVE_MEMBER,
                     V_TEMP.DAY_OF_MONTH,
                     COUNT(R.CONFIGURE_ID) L_SCHEDULE_LINE_COUNT
                FROM TI_DRIVER_LOG_TMP          V_TEMP, --行车日志       
                     TT_DRIVER_SCHEDULING       T, --排班表
                     TT_DRIVER_LINE_CONFIGURE   E, --配班表       
                     TT_DRIVER_LINE_CONFIGURE_R R --配班与线路关系表
               WHERE T.EMPLOYEE_CODE = V_TEMP.DRIVE_MEMBER
                 AND T.DAY_OF_MONTH = V_TEMP.DAY_OF_MONTH
                 AND T.SCHEDULING_TYPE = 1
                 AND E.DEPARTMENT_CODE || '-' || E.CODE = T.CONFIGURE_CODE
                    --AND E.MONTH = T.DAY_OF_MONTH
                 AND E.ID = R.CONFIGURE_ID
               GROUP BY V_TEMP.DRIVE_MEMBER, V_TEMP.DAY_OF_MONTH) S,
             (SELECT B.DEPT_CODE,
                     B.DEPT_NAME,
                     C.DEPT_CODE AREA_CODE,
                     C.DEPT_NAME AREA_NAME,
                     A.EMP_NAME,
                     A.EMP_CODE
                FROM TM_OSS_EMPLOYEE A, TM_DEPARTMENT B, TM_DEPARTMENT C
               WHERE A.DEPT_ID = B.DEPT_ID
                 AND B.AREA_CODE = C.DEPT_CODE) R
       WHERE J.DRIVE_MEMBER = S.DRIVE_MEMBER(+)
         AND J.DAY_OF_MONTH = S.DAY_OF_MONTH(+)
         AND J.DRIVE_MEMBER = R.EMP_CODE
         AND SUBSTR(J.DAY_OF_MONTH, 0, 6) = TEMP_YEAR_MONTH;
  
    -- 行车日志数据处理当月的数据
    merge into TT_COMPARED_REPORT T1 --TT_COMPARED_REPORT表是需要更新的表
    using TT_COMPARED_REPORT_TEMP T2 -- 关联表
    on (T1.ID = T2.ID) --关联条件
    when matched then --匹配关联条件，作更新处理
      update
         set T1.COMPARE_RESULT = T2.COMPARE_RESULT,
             t1.MODIFIED_TIME  = sysdate --此处只是说明可以同时更新多个字段。
      
    
    when not matched then --不匹配关联条件，作插入处理。如果只是作更新，下面的语句可以省略。
      insert
      values
        (t2.ID,
         t2.employee_code,
         t2.employee_name,
         t2.department_code,
         t2.department_name,
         t2.area_code,
         t2.area_name,
         t2.COMPARE_RESULT,
         t2.day_month,
         sysdate,
         sysdate);
  
    ------------------------------------------SPLIT LINE -------------------------------------------------------------------
    --修改一下报表数据;
    UPDATE ti_driver_log_tmp
       SET COMPARE_STATUS = 1
     WHERE COMPARE_STATUS = 3;
    COMMIT;
  
    --4.结束记录日志
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                 'P_REPORT_CAUSE_BY_DRIVING',
                                 SYSDATE,
                                 NULL,
                                 NULL,
                                 'END',
                                 0,
                                 L_CALL_NO);
  
  EXCEPTION
    WHEN OTHERS THEN
      -- 发生异常时，回滚数据状态
      update ti_driver_log_tmp t
         set t.compare_status = 0
       where t.compare_status = 3;
    
      COMMIT;
      --回滚数据
      ROLLBACK;
      PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                   'P_REPORT_CAUSE_BY_DRIVING',
                                   SYSDATE,
                                   SQLCODE,
                                   SQLERRM,
                                   'ERROR',
                                   0,
                                   '');
  END P_REPORT_CAUSE_BY_DRIVING;

  -- 处理行车日志数据
  PROCEDURE HANDLE_DRIVING_CONVERT_DATA IS
    --*************************************************************
    -- AUTHOR  : 王健
    -- CREATED : 2015-09-09
    -- PURPOSE : ETL数据拉取成功后，处理换班行车日志数据并计算出勤时长、驾驶时长、拼接线路网点代码
    --
    -- PARAMETER:
    -- NAME             TYPE            DESC
    --
    -- MODIFY HISTORY
    -- PERSON                    DATE                        COMMENTS
    -- -------------------------------------------------------------
    --
    --*************************************************************
    TEMP_DRIVER_MEMBER        VARCHAR2(50); -- 员工代码
    TEMP_JOIN_DEPARTMENT_CODE VARCHAR2(2000); --  拼接线路网点
    TEMP_DRIVE_TM             VARCHAR2(2000); -- 换班时间
    TEMP_DAY_ATTENDANCE_TIME  NUMBER(19, 4); -- 出勤时长
    TEMP_DAY_DRIVE_TIME       NUMBER(19, 4); -- 驾驶时长
    TEMP_START_TIME           date; -- 开始时间
    TEMP_END_TIME             date; -- 结束时间
    TEMP_YEAR_MONTH           VARCHAR2(10); -- 年月(YYYY-MM)
  
    --1.定义执行序号
    L_CALL_NO NUMBER;
  
  BEGIN
    --2.设置执行序号
    SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
    --3.开始记录日志
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                 'HANDLE_DRIVING_CONVERT_DATA',
                                 SYSDATE,
                                 NULL,
                                 NULL,
                                 'START',
                                 0,
                                 L_CALL_NO);
    TEMP_YEAR_MONTH := TO_CHAR(SYSDATE, 'YYYY-MM');
  
    execute immediate 'truncate table TI_DRIVER_LOG_TIME_TMP';
  
    DELETE TI_DRIVER_LOG_TMP T
     WHERE substr(t.day_of_month, 0, 6) = REPLACE(TEMP_YEAR_MONTH, '-', '');
  
    insert into TI_DRIVER_LOG_TIME_TMP
      (id,
       drive_tm,
       end_tm,
       drive_member,
       start_place,
       end_place,
       DRIVE_TIME)
      select SEQ_TI_DRIVER_TIME.NEXTVAL,
             t.drive_tm,
             t.end_tm,
             t.drive_member,
             t.start_place,
             t.end_place,
             t.DRIVE_TIME
        from (select c.drive_tm,
                     lead(c.drive_tm, 1, c.end_tm) over(partition by c.driving_log_item_id order by c.drive_tm) end_tm,
                     c.drive_member,
                     c.driving_log_item_id,
                     i.start_place,
                     i.end_place,
                     lead(c.drive_tm, 1, c.end_tm) over(partition by c.driving_log_item_id order by c.drive_tm) - c.drive_tm DRIVE_TIME
                from ti_vms_drive_convert c, TI_VMS_DRIVING_LOG_ITEM i
               where c.driving_log_item_id = i.driving_log_item_id
                 and to_char(c.drive_tm, 'yyyy-mm') = TEMP_YEAR_MONTH
                 and NOT EXISTS
               (SELECT K.DRIVE_MEMBER
                        FROM TI_VMS_DRIVE_CONVERT_BAK K
                       WHERE K.DRIVING_LOG_ITEM_ID = c.DRIVING_LOG_ITEM_ID)
               order by c.drive_tm) t;
  
    -- 获取ETL每天拉取的数据
    FOR row_data IN (select t.drive_member,
                            to_char(t.drive_tm, 'yyyy-mm') year_month
                       from ti_vms_drive_convert t
                      where to_char(t.drive_tm, 'yyyy-mm') = TEMP_YEAR_MONTH
                      group by t.drive_member,
                               to_char(t.drive_tm, 'yyyy-mm')) LOOP
    
      -- 获取员工整月的数据，去重后的数据
      FOR TEMP IN (select t.drive_member,
                          to_char(t.drive_tm, 'yyyy-mm-dd') day_of_month
                     from ti_vms_drive_convert t
                    where t.drive_member = row_data.drive_member
                      and to_char(t.drive_tm, 'yyyy-mm') =
                          row_data.year_month
                      AND NOT EXISTS
                    (SELECT K.DRIVING_LOG_ITEM_ID
                             FROM TI_VMS_DRIVE_CONVERT_BAK K
                            WHERE K.DRIVING_LOG_ITEM_ID =
                                  T.DRIVING_LOG_ITEM_ID)
                    group by t.drive_member,
                             to_char(t.drive_tm, 'yyyy-mm-dd')
                    order by to_char(t.drive_tm, 'yyyy-mm-dd')) LOOP
      
        -- 初始化数据
        TEMP_DRIVER_MEMBER        := TEMP.DRIVE_MEMBER;
        TEMP_DRIVE_TM             := REPLACE(TEMP.DAY_OF_MONTH, '-', '');
        TEMP_JOIN_DEPARTMENT_CODE := '';
        TEMP_DAY_DRIVE_TIME       := 0;
        TEMP_DAY_ATTENDANCE_TIME  := 0;
      
        -- 获取员工当天的数据，计算出勤时长、排班时长
        FOR DRIVER_LOG_ROW IN (select *
                                 from TI_DRIVER_LOG_TIME_TMP t
                                where t.drive_member = TEMP.DRIVE_MEMBER
                                  and to_char(t.drive_tm, 'yyyymmdd') =
                                      TEMP_DRIVE_TM
                                order by t.drive_tm) LOOP
        
          -- 当拼接网点不为空时，只拼接目的网点
          if TEMP_JOIN_DEPARTMENT_CODE is not null then
            if lengthb(TEMP_JOIN_DEPARTMENT_CODE) < 1900 then
              TEMP_JOIN_DEPARTMENT_CODE := TEMP_JOIN_DEPARTMENT_CODE || '-' ||
                                           DRIVER_LOG_ROW.END_PLACE;
            end if;
            TEMP_END_TIME := DRIVER_LOG_ROW.END_TM;
            -- 拼接开始网点、结束网点
          else
            TEMP_JOIN_DEPARTMENT_CODE := DRIVER_LOG_ROW.START_PLACE || '-' ||
                                         DRIVER_LOG_ROW.END_PLACE;
          
            -- 获取上班时间
            TEMP_START_TIME := DRIVER_LOG_ROW.DRIVE_TM;
            -- 获取下班时间
            TEMP_END_TIME := DRIVER_LOG_ROW.END_TM;
          end if;
        
          -- 单条线路(结束时间-开始时间) 相加，得到出勤时长
          TEMP_DAY_DRIVE_TIME := TEMP_DAY_DRIVE_TIME +
                                 DRIVER_LOG_ROW.DRIVE_TIME;
        
        END LOOP;
      
        -- 计算出勤时长(最后结束时间-开始时间)
        select round(TEMP_END_TIME - TEMP_START_TIME, 2)
          into TEMP_DAY_ATTENDANCE_TIME
          from dual;
      
        -- 计算驾驶时长
        select round(TEMP_DAY_DRIVE_TIME, 2)
          into TEMP_DAY_DRIVE_TIME
          from dual;
      
        -- 当数据存在时，删除后新增
        delete TI_DRIVER_LOG_TMP tem
         where tem.drive_member = TEMP_DRIVER_MEMBER
           and tem.day_of_month = TEMP_DRIVE_TM;
      
        -- 新增
        INSERT INTO TI_DRIVER_LOG_TMP
          (ID,
           DRIVE_MEMBER,
           DAY_OF_MONTH,
           DAY_ATTENDANCE_TIME,
           DAY_DRIVE_TIME,
           DEPT_CODE)
        VALUES
          (SEQ_DRIVER_LOG.NEXTVAL,
           TEMP_DRIVER_MEMBER,
           TEMP_DRIVE_TM,
           TEMP_DAY_ATTENDANCE_TIME,
           TEMP_DAY_DRIVE_TIME,
           TEMP_JOIN_DEPARTMENT_CODE);
      END LOOP;
    
    END LOOP;
  
    update ti_vms_drive_convert t
       set t.sync_export_report = 1
     where t.sync_export_report = 0;
  
    COMMIT;
  
    --4.结束记录日志
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                 'HANDLE_DRIVING_CONVERT_DATA',
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
      PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                   'HANDLE_DRIVING_CONVERT_DATA',
                                   SYSDATE,
                                   SQLCODE,
                                   SQLERRM,
                                   'ERROR' || TEMP_DRIVER_MEMBER,
                                   0,
                                   L_CALL_NO);
  END HANDLE_DRIVING_CONVERT_DATA;

  -- 新增报表的排班
  PROCEDURE P_INSERT_report_for_temp(DAY1            IN VARCHAR2,
                                     EMPLOYEE_CODE   in VARCHAR2,
                                     DEPARTMENT_CODE IN VARCHAR2,
                                     CONFIGURE_CODE  IN VARCHAR2,
                                     MONTH           in VARCHAR2) IS
  begin
    IF DAY1 = '01' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY1, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '02' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY2, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '03' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY3, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '04' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY4, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '05' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY5, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '06' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY6, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '07' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY7, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '08' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY8, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '09' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY9, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '10' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY10, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '11' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY11, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '12' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY12, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '13' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY13, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '14' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY14, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '15' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY15, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '16' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY16, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '17' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY17, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '18' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY18, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '19' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY19, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '20' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY20, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '21' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY21, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '22' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY22, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '23' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY23, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
    IF DAY1 = '24' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY24, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '25' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY25, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
    IF DAY1 = '26' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY26, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
    IF DAY1 = '27' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY27, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
    IF DAY1 = '28' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY28, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '29' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY29, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
    IF DAY1 = '30' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY30, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
    IF DAY1 = '31' THEN
    
      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY31, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
  
  end P_INSERT_report_for_temp;

  --修改报表的排班
  PROCEDURE P_UPDATE_REPORT_FOR_TEMP(V_DAY1           IN VARCHAR2,
                                     V_EMPLOYEE_CODE  in VARCHAR2,
                                     V_DEPARMENT_CODE in VARCHAR2,
                                     V_CONFIGURE_CODE IN VARCHAR2,
                                     V_MONTH          IN VARCHAR2) IS
  
  begin
    IF V_DAY1 = '01' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY1            = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
  
    IF V_DAY1 = '02' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY2            = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
  
    IF V_DAY1 = '03' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY3            = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '04' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY4            = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '05' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY5            = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
  
    IF V_DAY1 = '06' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY6            = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '07' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY7            = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '08' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY8            = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '09' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY9            = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '10' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY10           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '11' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY11           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '12' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY12           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '13' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY13           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '14' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY14           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '15' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY15           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '16' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY16           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '17' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY17           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '18' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY18           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '19' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY19           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '20' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY20           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '21' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY21           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '22' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY22           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '23' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY23           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '24' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY24           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '25' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY25           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '26' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY26           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '27' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY27           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '28' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY28           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '29' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY29           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '30' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY30           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
    IF V_DAY1 = '31' THEN
    
      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY31           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;
    
    END IF;
  
  end P_UPDATE_REPORT_FOR_TEMP;

  -- 处理排班数据、计算吻合率
  procedure HANDLE_SCHEDULING_TASK_REPORT is
    --*************************************************************
    -- AUTHOR  : 王健
    -- CREATED : 2015-09-09
    -- PURPOSE : 排班数据导出，计算行车日志：出勤时长、驾驶时长、排班吻合率;
    --
    -- PARAMETER:
    -- NAME             TYPE            DESC
    --
    -- MODIFY HISTORY
    -- PERSON                    DATE                        COMMENTS
    -- -------------------------------------------------------------
    --
    --*************************************************************
    temp_sch_dept_code   varchar2(20); -- 排班网点代码
    temp_configure_code  varchar2(30); -- 配班代码
    temp_conf_dept_code  varchar2(20); --配班的网点
    temp_code            varchar2(20); -- 配班Code
    temp_emp_code        varchar2(20); -- 员工代码
    temp_year_month      varchar2(10); --配班年月(yyyy-mm)
    temp_day             varchar2(2); --配班的天dd
    temp_day_of_month    varchar2(10); -- 配班年月日(yyyymmdd)
    temp_scheduling_type number(2); --排班类型 0预排班、1实际排班
    temp_exist_count     number(10); -- 报表数据是否存在 1存在
    temp_is_leave        number; -- 是请假 1请假、0非请假
    temp_configure_type  number; -- 配班类型 1正常配班、0机动配班
  
    temp_match_drive_department varchar2(2000); -- 拼接对比的行车日志网点数据
    temp_natural_days           number; -- 月总自然天数
  
    temp_join_dept_code varchar2(2000); -- 拼接的排班线路所有网点
  
    temp_emp_name             varchar2(50); -- 员工姓名
    temp_department_name      varchar2(50); -- 网点名称
    temp_area_department_code varchar2(50); -- 区域代码
    temp_area_department_name varchar2(50); -- 区域名称
  
    temp_rate_count            number(10); -- 吻合总数
    temp_match_rate            number(10, 2); -- 计算的吻合率
    temp_attendance_time       number(10, 2); -- 出勤时长
    temp_drive_time            number(10, 2); -- 驾驶时长(实际)
    temp_drive_scheduling_time number(10, 2); -- 驾驶时长(排班)
    temp_scheduling_time       number(10, 2); -- 记录单条线路驾驶时长
    temp_total_rest_count      number(10); -- 总休息天数
    temp_is_rest               varchar2(3); -- 休
    temp_exist                 number; -- 行车日志存在网点数据
  
    --1.定义执行序号
    L_CALL_NO NUMBER;
  
  BEGIN
  
    --2.设置执行序号
    SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
    --3.开始记录日志
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                 'HANDLE_SCHEDULING_TASK_REPORT',
                                 SYSDATE,
                                 NULL,
                                 NULL,
                                 'START',
                                 0,
                                 L_CALL_NO);
    -- 排班类型为：实际排班
    temp_sch_dept_code   := '';
    temp_conf_dept_code  := '';
    temp_code            := '';
    temp_emp_code        := '';
    temp_year_month      := '';
    temp_day_of_month    := '';
    temp_is_rest         := '休';
    temp_scheduling_type := 1;
  
    update SYNC_TASK_FOR_SCHEDULED_REPORT task set task.sync_status = 3;
    COMMIT;
  
    -- 处理触发器的数据
    FOR row_data IN (select task.employee_code, task.year_month
                       from SYNC_TASK_FOR_SCHEDULED_REPORT task
                      where task.sync_status = 3
                      group by task.employee_code, task.year_month
                      order by task.employee_code, task.year_month) LOOP
    
      temp_year_month := row_data.Year_Month;
      temp_emp_code   := row_data.employee_code;
      -- 初始化出勤时长
      temp_attendance_time := 0;
      -- 初始化驾驶时长(实际)
      temp_drive_time := 0;
      -- 初始化驾驶时长(排班)
      temp_drive_scheduling_time := 0;
    
      -- 初始化排班吻合率值
      temp_rate_count := 0;
      temp_match_rate := 0;
    
      -- 通过 员工工号、年月、排班类型 查询排班数据
      FOR sch_row IN (select sch.department_code,
                             sch.employee_code,
                             sch.configure_code,
                             sch.year_month,
                             sch.day_of_month,
                             emp.emp_name,
                             dept.dept_name      dept_name,
                             area.dept_code      area_code,
                             area.dept_name      area_name
                        from tt_driver_scheduling sch,
                             tm_oss_employee      emp,
                             tm_department        dept,
                             tm_department        area
                       where sch.employee_code = emp.emp_code
                         and sch.department_code = dept.dept_code
                         and dept.area_code = area.dept_code
                         and sch.employee_code = temp_emp_code
                         and sch.year_month = temp_year_month
                         and sch.scheduling_type = temp_scheduling_type
                       order by sch.day_of_month) loop
      
        -- 获取配班的网点
        temp_conf_dept_code := substr(sch_row.configure_code,
                                      0,
                                      instr(sch_row.configure_code, '-') - 1);
        -- 获取配班代码
        temp_configure_code := sch_row.configure_code;
        -- 获取配班CODE
        temp_code := substr(sch_row.configure_code,
                            instr(sch_row.configure_code, '-') + 1);
        -- 获取排班的网点
        temp_sch_dept_code := sch_row.department_code;
        -- 存储年月日
        temp_day_of_month := sch_row.day_of_month;
        -- 存储排班天
        temp_day := substr(sch_row.day_of_month, 7);
        -- 存储员工姓名
        temp_emp_name := sch_row.emp_name;
        -- 网点代码名称
        temp_department_name := sch_row.dept_name;
        -- 区域代码
        temp_area_department_code := sch_row.area_code;
        -- 区域代码名称
        temp_area_department_name := sch_row.area_name;
        -- 初始化拼接网点
        temp_join_dept_code := '';
        -- 初始化排班线路总时长
        temp_scheduling_time := 0;
        -- 初始化对比网点为空
        temp_match_drive_department := '';
      
        -- 通过 配班代码、配班年月、配班网点 查询线路信息
        FOR line_row IN (select line.start_time,
                                line.end_time,
                                line.belong_zone_code,
                                line.source_code,
                                line.destination_code,
                                r.sort
                           from tt_driver_line_configure   conf,
                                tt_driver_line_configure_r r,
                                tm_driver_line             line
                          where conf.id = r.configure_id
                            and r.line_id = line.id
                            and conf.valid_status = 1
                            and conf.code = temp_code
                            and conf.month = temp_year_month
                            and conf.department_code = temp_conf_dept_code
                          ORDER BY R.SORT) LOOP
        
          -- 单条线路的驾驶时长
          temp_scheduling_time := temp_scheduling_time +
                                  CALCULATE_SCHEDULING_TIME(line_row.start_time,
                                                            line_row.end_time);
        
          -- 当拼接网点存在时，只拼接结束网点
          if temp_join_dept_code is not null then
            if lengthb(temp_join_dept_code) < 1900 then
              temp_join_dept_code := temp_join_dept_code || '-' ||
                                     line_row.destination_code;
            end if;
          else
            temp_join_dept_code := line_row.source_code || '-' ||
                                   line_row.destination_code;
          end if;
        END LOOP;
      
        -- 当排班为"休"
        if temp_configure_code = temp_is_rest then
          temp_rate_count := temp_rate_count + 1;
        else
          -- 查询是否是机动配班 1否、0 是
          select decode(max(t.type), null, 2, max(t.type))
            into temp_configure_type
            from tt_driver_line_configure t
           where t.valid_status = 1
             and t.department_code = temp_conf_dept_code
             and t.code = temp_code
             and t.month = temp_year_month;
        
          -- 查询请假审批通过状态 1审批通过、0审批未通过
          SELECT COUNT(1)
            INTO TEMP_IS_LEAVE
            FROM TT_DRIVER_APPLY APP
           WHERE APP.APPLY_EMPLOYEE_CODE = TEMP_EMP_CODE
             AND APP.DEPARTMENT_CODE = TEMP_SCH_DEPT_CODE
             AND APP.DAY_OF_MONTH = TEMP_DAY_OF_MONTH
             AND APP.APPLY_TYPE = 1
             AND APP.STATUS = 2
             and app.apply_id =
                 (select nvl(max(app.apply_id), 0)
                    from tt_driver_apply app
                   where app.day_of_month = TEMP_DAY_OF_MONTH
                     and app.apply_employee_code = TEMP_EMP_CODE);
        
          if temp_is_leave = 1 then
            temp_configure_code := '请假';
          elsif temp_configure_type = 0 then
            temp_configure_code := '机动' || temp_configure_code;
          end if;
        
          -- 当是机动排班时、或者 请假审批通过时
          if temp_configure_type = 0 or temp_is_leave = 1 then
            temp_rate_count := temp_rate_count + 1;
          else
            select count(t.dept_code)
              into temp_exist
              from ti_driver_log_tmp t
             where t.drive_member = temp_emp_code
               and t.day_of_month = temp_day_of_month;
          
            -- 当行车日志数据不为空时
            if temp_exist > 0 then
              select t.dept_code
                into temp_match_drive_department
                from ti_driver_log_tmp t
               where t.drive_member = temp_emp_code
                 and t.day_of_month = temp_day_of_month;
            
              -- 当包含：提货、接驳、收派、其它
              if WHETHER_CONTAIN_CHINESE(temp_match_drive_department) = 1 then
                temp_rate_count := temp_rate_count + 1;
              else
                -- 当网点相等时
                if temp_match_drive_department = temp_join_dept_code then
                  temp_rate_count := temp_rate_count + 1;
                end if;
              end if;
            end if;
          
          end if;
        
        end if;
      
        -- 当计算的行车日志数据存在时，先删除、再新增
        delete TI_DRIVER_SCHEDULING_TMP sch
         where sch.DRIVE_MEMBER = temp_emp_code
           and sch.Day_Of_Month = temp_day_of_month;
      
        -- 新增、计算的出勤时长、驾驶时长
        INSERT INTO TI_DRIVER_SCHEDULING_TMP
          (ID, DRIVE_MEMBER, DAY_OF_MONTH, DEPT_CODE, SCHEDULING_TIME)
        VALUES
          (SEQ_DRIVER_LOG.NEXTVAL,
           temp_emp_code,
           temp_day_of_month,
           temp_join_dept_code,
           temp_scheduling_time);
      
        -- 查看报表数据是否存在;
        SELECT COUNT(1)
          INTO temp_exist_count
          FROM REPORT_DRIVER_SCHEDULING T1
         WHERE T1.EMPLOYEE_CODE = temp_emp_code
           and t1.month = temp_year_month;
      
        -- 不存在新增、否则修改
        IF temp_exist_count = 0 THEN
          --INSERT
          P_INSERT_report_for_temp(temp_day,
                                   temp_emp_code,
                                   temp_sch_dept_code,
                                   temp_configure_code,
                                   temp_year_month);
        
        ELSE
          -- UPDATE
          P_UPDATE_REPORT_FOR_TEMP(temp_day,
                                   temp_emp_code,
                                   temp_sch_dept_code,
                                   temp_configure_code,
                                   temp_year_month);
        END IF;
      
      END LOOP;
    
      -- 获取当月总天数
      select to_char(last_day(to_date(temp_year_month, 'yyyy-mm')), 'dd')
        into temp_natural_days
        from dual;
    
      -- 查询休息的天数
      select count(1)
        into temp_total_rest_count
        from tt_driver_scheduling sch
       where sch.year_month = temp_year_month
         and sch.employee_code = temp_emp_code
         and sch.scheduling_type = temp_scheduling_type
         and sch.configure_code = temp_is_rest;
    
      -- 获取出勤时长(天)
      select nvl(sum(t.day_attendance_time), 0)
        into temp_attendance_time
        from ti_driver_log_tmp t
       where t.drive_member = temp_emp_code
         and substr(t.day_of_month, 0, 6) =
             replace(temp_year_month, '-', '');
    
      -- 获取排班驾驶时长(实际)
      select nvl(sum(t.day_drive_time), 0)
        into temp_drive_time
        from ti_driver_log_tmp t
       where t.drive_member = temp_emp_code
         and substr(t.day_of_month, 0, 6) =
             replace(temp_year_month, '-', '');
    
      -- 获取排班驾驶时长(排班)
      select nvl(sum(t.scheduling_time), 0)
        into temp_drive_scheduling_time
        From ti_driver_scheduling_tmp t
       where substr(t.day_of_month, 0, 6) =
             replace(temp_year_month, '-', '')
         and t.drive_member = temp_emp_code;
    
      -- 吻合率
      temp_match_rate := round(temp_rate_count / temp_natural_days * 100, 2);
    
      -- 更新报表数据
      UPDATE REPORT_DRIVER_SCHEDULING S
         SET S.EMPLOYEE_NAME       = temp_emp_name,
             S.DEPARTMENT_NAME     = temp_department_name,
             S.AREA_CODE           = temp_area_department_code,
             S.AREA_NAME           = temp_area_department_name,
             S.SCHEDULE_TYPE       = '正常',
             S.TOTAL_REST_COUNT    = temp_total_rest_count,
             S.ATTENDANCE_DURATION = round(temp_attendance_time * 24 /
                                           temp_natural_days,
                                           2),
             S.DRIVE_TIME_MONTH_T  = round(temp_drive_time * 24 /
                                           temp_natural_days,
                                           2),
             S.Drive_Time_Month_s  = round(temp_drive_scheduling_time /
                                           temp_natural_days,
                                           2),
             S.MATCH_RATE          = temp_match_rate
       where S.EMPLOYEE_CODE = temp_emp_code
         and S.MONTH = temp_year_month;
    END LOOP;
  
    DELETE SYNC_TASK_FOR_SCHEDULED_REPORT task where task.sync_status = 3;
  
    commit;
    --4.结束记录日志
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                 'HANDLE_SCHEDULING_TASK_REPORT',
                                 SYSDATE,
                                 NULL,
                                 NULL,
                                 'END',
                                 0,
                                 L_CALL_NO);
  
    --5.异常记录日志
  EXCEPTION
    WHEN OTHERS THEN
      -- 回滚同步状态
      update SYNC_TASK_FOR_SCHEDULED_REPORT task
         set task.sync_status = 0
       where task.sync_status = 3;
      COMMIT;
    
      --回滚数据
      ROLLBACK;
      PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                   'HANDLE_SCHEDULING_TASK_REPORT',
                                   SYSDATE,
                                   SQLCODE,
                                   SQLERRM,
                                   'ERROR' || temp_emp_code,
                                   0,
                                   L_CALL_NO);
    
  END HANDLE_SCHEDULING_TASK_REPORT;

end PKG_HANDLE_COMPARE_REPORT;
/
