alter table ti_vms_drive_convert add SYNC_EXPORT_REPORT number(1) default 0;
alter table ti_vms_drive_convert add SYNC_EXPORT_REPORT_TM date default sysdate;

comment on column TI_VMS_DRIVE_CONVERT.SYNC_EXPORT_REPORT
  is '是否处理到报表 0：未处理 1：已处理  2：处理中';
comment on column TI_VMS_DRIVE_CONVERT.SYNC_EXPORT_REPORT_TM
  is '同步时间';
