create or replace procedure DRIVER_WARNNING_SYNC_PROCESS as

  sync_total_size   NUMBER; -- 记录同步总数据
  sync_success_size NUMBER; -- 记录成功条数

begin
  -- 记录开始
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'DRIVER_WARNNING_SYNC_PROCESS',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               1);

  select count(*)
    into sync_total_size
    from ti_vms_drive_convert t
   where t.sync_flag = 0;

  -- 记录需要同步的数据条数
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'DRIVER_WARNNING_SYNC_PROCESS',
                               SYSDATE,
                               NULL,
                               NULL,
                               '需要同步的数据,条数：' || sync_total_size,
                               0,
                               2);

  -- 更新需要同步的数据状态为3，防止： 多个事物产生垃圾数据
  update ti_vms_drive_convert sync
     set sync.sync_flag = 3
   where sync.sync_flag = 0;

  -- 同步数据 将状态为3的同步
  insert into tt_report_for_driver_warning
    (ID, Vehicle_Code, driver_identify, drive_day, dept_code, created_tm)
    select sync.id,
           sync.vehicle_code,
           sync.drive_member,
           sync.Drive_tm,
           emp_dept.dept_code,
           sync.created_tm
      from ti_vms_drive_convert sync,
           (select emp.emp_code, dept.dept_code
              from tm_employee emp, tm_department dept
             where emp.dept_code = dept.dept_code
               and emp.valid_flg = 1
               and dept.delete_flg = 0) emp_dept
     where sync.drive_member = emp_dept.emp_code
       and sync.sync_flag = 3;

  update ti_vms_drive_convert sync
     set sync.sync_flag = 1
   where sync.sync_flag = 3;

  -- 记录成功同步 条数
  sync_success_size := SQL%ROWCOUNT;

  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'DRIVER_WARNNING_SYNC_PROCESS',
                               SYSDATE,
                               NULL,
                               NULL,
                               'END 需要同步的数据总条数：' || sync_total_size ||
                               ',成功同步条数：' || sync_success_size,
                               0,
                               3);
  commit;
  -- 添加异常日志
exception
  when others then
    -- 记录异常
    PKG_OSS_COMM.STP_RUNNING_LOG('',
                                 'DRIVER_WARNNING_SYNC_PROCESS',
                                 SYSDATE,
                                 SQLERRM,
                                 SQLERRM,
                                 '同步数据发生异常',
                                 0,
                                 4);

    rollback;

end DRIVER_WARNNING_SYNC_PROCESS;
/
