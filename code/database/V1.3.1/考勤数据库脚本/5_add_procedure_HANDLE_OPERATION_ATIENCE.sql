create or replace procedure HANDLE_OPERATION_ATIENCE(limitnumber in NUMBER) is
  --1.定义执行序号
  L_CALL_NO NUMBER;

begin
  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'HANDLE_OPERATION_ATIENCE',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);
  begin
    update ti_sap_zthr_pt_detail_tmp tmp
       set tmp.sync_status = 1
     where EXISTS (SELECT 1
              FROM tm_oss_employee e
             WHERE e.emp_code = tmp.pernr
               and e.emp_post_type = '1')
       and tmp.sync_status = 0
       and rownum <= limitnumber;
    commit;

    insert into TI_TCAS_SPMS_SCHEDULE
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
       MODIFY_TIME)
      select SEQ_TI_TCAS_SPMS_SCHEDULE.NEXTVAL,
             e.emp_code,
             e.emp_name,
             d.area_code,
             d.dept_code,
             decode(rc.group_code, null, g.group_code, rc.group_code) group_code,
             to_date(log.begda, 'YYYYMMDD') begda,
             round((to_date(log.enduz, 'HH24MISS') -
                   to_date(log.beguz, 'HH24MISS')) * 24,
                   1),
             process.process_code,
             decode(pc.difficulty_modify_value,
                    null,
                    nvl(pc.difficulty_value, 0),
                    difficulty_modify_value) difficulty_modify_value,
             1,
             e.persk_txt,
             'admin',
             sysdate,
             'admin',
             sysdate
        FROM TI_SAP_ZTHR_PT_DETAIL_tmp log,
             TM_OSS_EMPLOYEE E,
             tt_pb_process_by_day process,
             tm_pb_group_info g,
             tm_pb_process_info pc,
             tm_department d,
             (select sc.pernr,
                     sc.begda,
                     nvl(grp.group_name, '') group_name,
                     nvl(grp.group_code, '') group_code
                from op_emp_group_modify_record rc,
                     (select log.pernr, log.begda, min(rc.id) id
                        from TI_SAP_ZTHR_PT_DETAIL_tmp log,
                             op_emp_group_modify_record rc
                       where log.pernr = rc.emp_code
                         and to_date(log.begda, 'YYYYMMDD') < rc.enable_tm
                         and rc.enable_state = 1
                       group by log.pernr, log.begda) sc,
                     tm_pb_group_info grp
               where rc.id = sc.id
                 and rc.prev_group_id = grp.group_id(+)) rc
       WHERE log.pernr = e.emp_code(+)
         and log.pernr = process.emp_code(+)
         and log.begda = to_char(process.process_dt(+), 'YYYYMMDD')
         and e.dept_id = d.dept_id(+)
         and e.group_id = g.group_id(+)
         and process.dept_id = pc.dept_id(+)
         and process.process_code = pc.process_code(+)
         and log.pernr = rc.pernr(+)
         and log.begda = rc.begda(+)
         AND log.SYNC_STATUS = 1
         and e.emp_post_type = '1';

    delete TI_SAP_ZTHR_PT_DETAIL_tmp tmp where tmp.sync_status = 1;
    commit;
  end;
  --4.结束记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'HANDLE_OPERATION_ATIENCE',
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
    update ti_sap_zthr_pt_detail_tmp tmp
       set tmp.sync_status = 3
     where tmp.sync_status = 1;
    commit;

    PKG_OSS_COMM.STP_RUNNING_LOG('',
                                 'HANDLE_OPERATION_ATIENCE',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);

end HANDLE_OPERATION_ATIENCE;
