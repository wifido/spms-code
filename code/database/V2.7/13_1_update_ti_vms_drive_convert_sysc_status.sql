update ti_vms_drive_convert t
   set t.sync_export_report = 1
 where t.start_tm < date'2015-08-01';

commit;
