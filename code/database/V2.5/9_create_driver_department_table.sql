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
  is '行车日志网点信息表';
-- Add comments to the columns 
comment on column ti_vms_driving_log_item.driving_log_item_id
  is '关联行车日志ID';
comment on column ti_vms_driving_log_item.driver_id
  is '驾驶员工号';
comment on column ti_vms_driving_log_item.start_place
  is '始发网点';
comment on column ti_vms_driving_log_item.end_place
  is '目的网点';
comment on column ti_vms_driving_log_item.start_tm
  is '开始时间';
comment on column ti_vms_driving_log_item.end_tm
  is '结束时间';
comment on column ti_vms_driving_log_item.created_tm
  is '创建时间';
comment on column ti_vms_driving_log_item.sys_tm
  is '同步时间';

  
grant all on TI_VMS_DRIVING_LOG_ITEM to spmsetl;