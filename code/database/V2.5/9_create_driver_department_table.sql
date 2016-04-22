create table ti_vms_driving_log_item(
       driving_log_item_id number(19),
       driver_id number(19),
       start_place varchar2(100),
       end_place varchar2(200),
       start_tm date,
       end_tm date,
       created_tm date,
       sys_tm date default sysdate
);

-- Add comments to the table 
comment on table ti_vms_driving_log_item
  is '�г���־������Ϣ��';
-- Add comments to the columns 
comment on column ti_vms_driving_log_item.driving_log_item_id
  is '�����г���־ID';
comment on column ti_vms_driving_log_item.driver_id
  is '��ʻԱ����';
comment on column ti_vms_driving_log_item.start_place
  is 'ʼ������';
comment on column ti_vms_driving_log_item.end_place
  is 'Ŀ������';
comment on column ti_vms_driving_log_item.start_tm
  is '��ʼʱ��';
comment on column ti_vms_driving_log_item.end_tm
  is '����ʱ��';
comment on column ti_vms_driving_log_item.created_tm
  is '����ʱ��';
comment on column ti_vms_driving_log_item.sys_tm
  is 'ͬ��ʱ��';

  
grant all on TI_VMS_DRIVING_LOG_ITEM to spmsetl;