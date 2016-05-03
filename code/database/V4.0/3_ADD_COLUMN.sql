-- Add/modify columns 
alter table OP_ATTENDANCE_COUNT_REPORT add COM_FULL_ATTENDANCE_NUM NUMBER(10);
alter table OP_ATTENDANCE_COUNT_REPORT add COM_NOT_FULL_ATTENDANCE_NUM NUMBER(10);
alter table OP_ATTENDANCE_COUNT_REPORT add COM_OUT_ATTENDANCE_NUM NUMBER(10);
-- Add comments to the columns 
comment on column OP_ATTENDANCE_COUNT_REPORT.COM_FULL_ATTENDANCE_NUM
  is '全日制考勤匹配数';
comment on column OP_ATTENDANCE_COUNT_REPORT.COM_NOT_FULL_ATTENDANCE_NUM
  is '非全日制考勤匹配数';
comment on column OP_ATTENDANCE_COUNT_REPORT.COM_OUT_ATTENDANCE_NUM
  is '外包考勤匹配数';

  
-- Add/modify columns 
alter table TI_TCAS_SPMS_SCHEDULE add ATTENDANCE_RATE NUMBER(1);
-- Add comments to the columns 
comment on column TI_TCAS_SPMS_SCHEDULE.ATTENDANCE_RATE
  is '是否满足匹配出勤（1满足，0不满足）';


-- Add/modify columns 
alter table TI_TCAS_SPMS_tmp1 add ATTENDANCE_RATE NUMBER(1);
-- Add comments to the columns 
comment on column TI_TCAS_SPMS_SCHEDULE.ATTENDANCE_RATE
  is '是否满足匹配出勤（1满足，0不满足）';

-- Add/modify columns 
alter table TI_TCAS_SPMS_tmp2 add ATTENDANCE_RATE NUMBER(1);
-- Add comments to the columns 
comment on column TI_TCAS_SPMS_SCHEDULE.ATTENDANCE_RATE
  is '是否满足匹配出勤（1满足，0不满足）';

-- Add/modify columns 
alter table TI_TCAS_SPMS_tmp3 add ATTENDANCE_RATE NUMBER(1);
-- Add comments to the columns 
comment on column TI_TCAS_SPMS_SCHEDULE.ATTENDANCE_RATE
  is '是否满足匹配出勤（1满足，0不满足）';
  
-- Add/modify columns 
alter table TI_TCAS_SPMS_tmp4 add ATTENDANCE_RATE NUMBER(1);
-- Add comments to the columns 
comment on column TI_TCAS_SPMS_SCHEDULE.ATTENDANCE_RATE
  is '是否满足匹配出勤（1满足，0不满足）';