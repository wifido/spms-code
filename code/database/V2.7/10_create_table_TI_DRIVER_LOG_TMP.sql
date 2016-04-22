-- Create sequence 
create sequence SEQ_DRIVER_LOG
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

-- Create table
create table TI_DRIVER_LOG_TMP
(
  ID                  NUMBER(19) not null,
  DRIVE_MEMBER        VARCHAR2(25),
  DAY_OF_MONTH        VARCHAR2(50),
  DAY_ATTENDANCE_TIME NUMBER(19,2),
  DAY_DRIVE_TIME      NUMBER(19,2),
  DEPT_CODE           VARCHAR2(2000),
  COMPARE_STATUS      NUMBER(1) default 0
);

-- Add comments to the table 
comment on table TI_DRIVER_LOG_TMP
  is '驾驶换班报表';
-- Add comments to the columns 
comment on column TI_DRIVER_LOG_TMP.ID
  is '主键';
comment on column TI_DRIVER_LOG_TMP.DRIVE_MEMBER
  is '员工代码';
comment on column TI_DRIVER_LOG_TMP.DAY_OF_MONTH
  is '换班日期(yyyymmdd)';
comment on column TI_DRIVER_LOG_TMP.DAY_ATTENDANCE_TIME
  is '出勤时长';
comment on column TI_DRIVER_LOG_TMP.DAY_DRIVE_TIME
  is '驾驶时长';
comment on column TI_DRIVER_LOG_TMP.DEPT_CODE
  is '拼接网点代码';
comment on column TI_DRIVER_LOG_TMP.COMPARE_STATUS
  is '对比结果';
  
alter table TI_DRIVER_LOG_TMP
  add constraint ID primary key (ID);
 
create index IDX_DRIVER_LOG_TMP on TI_DRIVER_LOG_TMP (DRIVE_MEMBER, DEPT_CODE);

create index IDX_DRIVER_LOG_TMP1 on TI_DRIVER_LOG_TMP (DRIVE_MEMBER, DAY_OF_MONTH);

create index IDX_STATUS on TI_DRIVER_LOG_TMP (COMPARE_STATUS);
