create or replace procedure DRIVER_WARNNING_SYNC_PROCESS as

  sync_total_size   NUMBER; -- ��¼ͬ��������
  sync_success_size NUMBER; -- ��¼�ɹ�����
  --1.����ִ�����
  L_CALL_NO NUMBER;

begin
  --2.����ִ�����
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
  -- ��¼��ʼ
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'DRIVER_WARNNING_SYNC_PROCESS',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

  select count(*)
    into sync_total_size
    from ti_vms_drive_convert t
   where t.sync_flag = 0;

  -- ��¼��Ҫͬ������������
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'DRIVER_WARNNING_SYNC_PROCESS',
                               SYSDATE,
                               NULL,
                               NULL,
                               '��Ҫͬ��������,������' || sync_total_size,
                               0,
                               L_CALL_NO);

  -- ������Ҫͬ��������״̬Ϊ3����ֹ�� ������������������
  update ti_vms_drive_convert sync
     set sync.sync_flag = 3
   where sync.sync_flag = 0;

  -- ͬ������ ��״̬Ϊ3��ͬ��
  insert into tt_report_for_driver_warning
    (ID,
     Vehicle_Code,
     driver_identify,
     drive_day,
     dept_code,
     created_tm)
    select sync.id,
           sync.vehicle_code,
           sync.drive_member,
           sync.Drive_tm,
           emp_dept.dept_code,
           sync.created_tm
          
      from ti_vms_drive_convert sync,
           (select emp.emp_code, dept.dept_code
              from tm_oss_employee emp, tm_department dept
             where emp.dept_id = dept.dept_id
               and dept.delete_flg = 0) emp_dept
     where sync.drive_member = emp_dept.emp_code
       and sync.sync_flag = 3;

  update ti_vms_drive_convert sync
     set sync.sync_flag = 1
   where sync.sync_flag = 3;

  -- ��¼�ɹ�ͬ�� ����
  sync_success_size := SQL%ROWCOUNT;

  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'DRIVER_WARNNING_SYNC_PROCESS',
                               SYSDATE,
                               NULL,
                               NULL,
                               'END ��Ҫͬ����������������' || sync_total_size ||
                               ',�ɹ�ͬ��������' || sync_success_size,
                               0,
                               L_CALL_NO);
  commit;

  -- ����Ԥ������
  DRIVER_WARNNING_COUNT(to_char(sysdate, 'YYYY-MM'));
  -- �����쳣��־
exception
  when others then
    -- ��¼�쳣
    PKG_OSS_COMM.STP_RUNNING_LOG('',
                                 'DRIVER_WARNNING_SYNC_PROCESS',
                                 SYSDATE,
                                 SQLERRM,
                                 SQLERRM,
                                 'ͬ�����ݷ����쳣',
                                 0,
                                 L_CALL_NO);
  
    rollback;
  
end DRIVER_WARNNING_SYNC_PROCESS;
/