alter table TT_DRIVER_LINE_CONFIGURE add Attendance_duration number(19,4) default 0;
alter table TT_DRIVER_LINE_CONFIGURE add Drive_duration number(19,4) default 0;
-- Add comments to the columns 
comment on column TT_DRIVER_LINE_CONFIGURE.Attendance_duration
  is '����ʱ��';
comment on column TT_DRIVER_LINE_CONFIGURE.Drive_duration
  is '��ʻʱ��';