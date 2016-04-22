alter table OP_ATTENDANCE_COUNT_REPORT add(TOTAL_SCHEDULING_NUM NUMBER(10),FULLTIME_SCHEDULING_NUM NUMBER(10),NOT_FULLTIME_SCHEDULING_NUM NUMBER(10),OUT_SCHEDULING_NUM NUMBER(10));
-- Add comments to the columns 
comment on column OP_ATTENDANCE_COUNT_REPORT.TOTAL_SCHEDULING_NUM
  is '运作总排班数';
comment on column OP_ATTENDANCE_COUNT_REPORT.FULLTIME_SCHEDULING_NUM
  is '运作全日制排班数';
comment on column OP_ATTENDANCE_COUNT_REPORT.NOT_FULLTIME_SCHEDULING_NUM
  is '运作非全日制排班数';
comment on column OP_ATTENDANCE_COUNT_REPORT.OUT_SCHEDULING_NUM
  is '运作外包排班数';