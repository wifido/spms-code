-- Add/modify columns 
alter table OP_ATTENDANCE_COUNT_REPORT add COM_FULL_ATTENDANCE_NUM NUMBER(10);
alter table OP_ATTENDANCE_COUNT_REPORT add COM_NOT_FULL_ATTENDANCE_NUM NUMBER(10);
alter table OP_ATTENDANCE_COUNT_REPORT add COM_OUT_ATTENDANCE_NUM NUMBER(10);
-- Add comments to the columns 
comment on column OP_ATTENDANCE_COUNT_REPORT.COM_FULL_ATTENDANCE_NUM
  is 'ȫ���ƿ���ƥ����';
comment on column OP_ATTENDANCE_COUNT_REPORT.COM_NOT_FULL_ATTENDANCE_NUM
  is '��ȫ���ƿ���ƥ����';
comment on column OP_ATTENDANCE_COUNT_REPORT.COM_OUT_ATTENDANCE_NUM
  is '�������ƥ����';
