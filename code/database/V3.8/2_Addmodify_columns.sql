-- Add/modify columns 
alter table REPORT_DRIVER_SCHEDULING add AVERAGE_ATTENDANCE_TIME NUMBER(10,2);
-- Add comments to the columns 
comment on column REPORT_DRIVER_SCHEDULING.AVERAGE_ATTENDANCE_TIME
  is '月度驾驶出勤时长(排班)';

update REPORT_DRIVER_SCHEDULING set AVERAGE_ATTENDANCE_TIME ='0' where month < '2016-04';
commit;  
  
  -- Add/modify columns 
alter table REPORT_DRIVER_SCHEDULING add CONTINUOUS_ATTENDANCE_DAYS NUMBER(10);
-- Add comments to the columns 
comment on column REPORT_DRIVER_SCHEDULING.CONTINUOUS_ATTENDANCE_DAYS
  is '最大连续出勤天数(排班)';

update REPORT_DRIVER_SCHEDULING set CONTINUOUS_ATTENDANCE_DAYS ='0' where month < '2016-04';
commit;