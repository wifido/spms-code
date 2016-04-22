CREATE OR REPLACE PROCEDURE SYS_OPERATION_EMPLOYEE_INFO(YEAR_MONTH IN VARCHAR2) IS
--*************************************************************
  -- AUTHOR  : WJ
  -- CREATED : 2015-04-23
  -- PURPOSE : 处理运作外包人员，将数据同步到计提接口表
  --
  --*************************************************************

  -- 定义执行序号
  L_CALL_NO NUMBER;
BEGIN
  -- 设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  -- 记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SYS_OPERATION_EMPLOYEE_INFO' || YEAR_MONTH,
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

  BEGIN
    -- 同步数据
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
      select SEQ_TI_TCAS_SPMS_SCHEDULE.NEXTVAL,
             emp.emp_code,
             emp.emp_name,
             dept.area_code,
             dept.dept_code,
             DECODE(track_team.GROUP_CODE,
                    NULL,
                    team.GROUP_CODE,
                    track_team.GROUP_CODE) GROUP_CODE,
             d.shedule_dt,
             0,
             process.process_code,
             DECODE(pro_info.DIFFICULTY_MODIFY_VALUE,
                    NULL,
                    NVL(pro_info.DIFFICULTY_VALUE, 0),
                    pro_info.DIFFICULTY_MODIFY_VALUE) DIFFICULTY_MODIFY_VALUE,
             1,
             '外包',
             'ADMIN',
             sysdate,
             'ADMIN',
             sysdate,
             0,
             0,
             count_time_distance(c.start1_time, c.end1_time) +
             count_time_distance(c.start2_time, c.end2_time) +
             count_time_distance(c.start3_time, c.end3_time) overtime_hours,
             c.ym
        from tm_oss_employee emp,
             tm_department dept,
             tt_pb_shedule_by_day d,
             tm_pb_schedule_base_info c,
             (SELECT sc.emp_code,
                     sc.shedule_dt,
                     NVL(GRP.GROUP_NAME, '') GROUP_NAME,
                     NVL(GRP.GROUP_CODE, '') GROUP_CODE
                FROM OP_EMP_GROUP_MODIFY_RECORD RC
                JOIN (SELECT log.emp_code, log.shedule_dt, MIN(RC.ID) ID
                       FROM tt_pb_shedule_by_day       LOG,
                            OP_EMP_GROUP_MODIFY_RECORD RC
                      WHERE LOG.Emp_Code = RC.EMP_CODE
                        AND log.shedule_dt < RC.ENABLE_TM
                        AND RC.ENABLE_STATE = 1
                      GROUP BY log.emp_code, log.shedule_dt) SC
                  ON RC.ID = SC.ID
                LEFT JOIN TM_PB_GROUP_INFO GRP
                  ON RC.PREV_GROUP_ID = GRP.GROUP_ID) track_team,
             TM_PB_GROUP_INFO team,
             TT_PB_PROCESS_BY_DAY process,
             TM_PB_PROCESS_INFO pro_info
       where emp.dept_id = dept.dept_id
         and dept.dept_id = d.dept_id
         and d.dept_id = c.dept_id(+)
         and emp.emp_code = d.emp_code
         and d.shedule_code = c.schedule_code(+)
         and to_char(d.shedule_dt, 'yyyy-mm') = c.ym(+)
         and emp.emp_post_type = 1
         and emp.work_type = 6
         and d.shedule_dt = track_team.shedule_dt(+)
         and d.emp_code = track_team.emp_code(+)
         and d.emp_code = process.emp_code(+)
         and d.dept_id = process.dept_id(+)
         and d.shedule_dt = process.process_dt(+)
         and process.process_code = pro_info.process_code(+)
         and process.dept_id = pro_info.dept_id(+)
         and emp.group_id = team.group_id(+)
         and c.ym = YEAR_MONTH;
    COMMIT;
  END;
  --结束记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SYS_OPERATION_EMPLOYEE_INFO',
                               SYSDATE,
                               NULL,
                               NULL,
                               'END',
                               0,
                               L_CALL_NO);
END SYS_OPERATION_EMPLOYEE_INFO;
/