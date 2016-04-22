-- Add/modify columns 
alter table REPORT_DRIVER_SCHEDULING add AVERAGE_ATTENDANCE_TIME NUMBER(10,2);
-- Add comments to the columns 
comment on column REPORT_DRIVER_SCHEDULING.AVERAGE_ATTENDANCE_TIME
  is '�¶ȼ�ʻ����ʱ��(�Ű�)';

update REPORT_DRIVER_SCHEDULING set AVERAGE_ATTENDANCE_TIME ='0' where month < '2016-04';
commit;  
  
  -- Add/modify columns 
alter table REPORT_DRIVER_SCHEDULING add CONTINUOUS_ATTENDANCE_DAYS NUMBER(10);
-- Add comments to the columns 
comment on column REPORT_DRIVER_SCHEDULING.CONTINUOUS_ATTENDANCE_DAYS
  is '���������������(�Ű�)';

update REPORT_DRIVER_SCHEDULING set CONTINUOUS_ATTENDANCE_DAYS ='0' where month < '2016-04';
commit;