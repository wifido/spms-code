-- Create table
create table TT_DRIVER_SCHED_CONFIRM_RECORD
(
  ID              NUMBER(10) not null,
  AREA_CODE       VARCHAR2(50),
  DEPARTMENT_CODE VARCHAR2(50),
  YEAR_WEEK       VARCHAR2(10),
  EMPLOYEE_NAME   VARCHAR2(50),
  EMPLOYEE_CODE   VARCHAR2(20),
  CONFIRM_STATUS  NUMBER(1),
  CONFIRM_TIME    DATE
);

-- Add comments to the table 
comment on table TT_DRIVER_SCHED_CONFIRM_RECORD
  is '排班确认记录表';
-- Add comments to the columns 
comment on column TT_DRIVER_SCHED_CONFIRM_RECORD.ID
  is '主键';
comment on column TT_DRIVER_SCHED_CONFIRM_RECORD.AREA_CODE
  is '地区代码';
comment on column TT_DRIVER_SCHED_CONFIRM_RECORD.DEPARTMENT_CODE
  is '网点代码';
comment on column TT_DRIVER_SCHED_CONFIRM_RECORD.YEAR_WEEK
  is '年周(yyyy-week)';
comment on column TT_DRIVER_SCHED_CONFIRM_RECORD.EMPLOYEE_NAME
  is '员工名称';
comment on column TT_DRIVER_SCHED_CONFIRM_RECORD.EMPLOYEE_CODE
  is '员工代码';
comment on column TT_DRIVER_SCHED_CONFIRM_RECORD.CONFIRM_STATUS
  is '确认状态';
comment on column TT_DRIVER_SCHED_CONFIRM_RECORD.CONFIRM_TIME
  is '确认时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TT_DRIVER_SCHED_CONFIRM_RECORD
  add constraint PK_SCHED_CONFIRM_REPORT_ID primary key (ID);
--Create index with the table
create index INX_DRIVER_SCHEDULING8 on TT_DRIVER_SCHEDULING (YEAR_WEEK, SCHEDULING_TYPE, CONFIRM_STATUS);


