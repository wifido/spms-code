alter table ti_vms_drive_convert add SYNC_EXPORT_REPORT number(1) default 0;
alter table ti_vms_drive_convert add SYNC_EXPORT_REPORT_TM date default sysdate;

comment on column TI_VMS_DRIVE_CONVERT.SYNC_EXPORT_REPORT
  is '�Ƿ������� 0��δ���� 1���Ѵ���  2��������';
comment on column TI_VMS_DRIVE_CONVERT.SYNC_EXPORT_REPORT_TM
  is 'ͬ��ʱ��';
