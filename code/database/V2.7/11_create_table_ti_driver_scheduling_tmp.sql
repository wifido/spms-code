-- Create table
create table TI_DRIVER_SCHEDULING_TMP
(
  ID              NUMBER(19) not null,
  DRIVE_MEMBER    VARCHAR2(25),
  DAY_OF_MONTH    VARCHAR2(50),
  DEPT_CODE       VARCHAR2(2000),
  SCHEDULING_TIME NUMBER(10,2)
);

-- Add comments to the table 
comment on table TI_DRIVER_SCHEDULING_TMP
  is '排班数据对比表';
-- Add comments to the columns 
comment on column TI_DRIVER_SCHEDULING_TMP.ID
  is '主键';
comment on column TI_DRIVER_SCHEDULING_TMP.DRIVE_MEMBER
  is '员工代码';
comment on column TI_DRIVER_SCHEDULING_TMP.DAY_OF_MONTH
  is '排班年月日';
comment on column TI_DRIVER_SCHEDULING_TMP.DEPT_CODE
  is '拼接网点';
comment on column TI_DRIVER_SCHEDULING_TMP.SCHEDULING_TIME
  is '排班时长(实际)';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TI_DRIVER_SCHEDULING_TMP
  add constraint PK_ID_PRIMARY primary key (ID);
  
-- Create/Recreate indexes 
create index IDX_DRIVER_SCH1 on TI_DRIVER_SCHEDULING_TMP (DRIVE_MEMBER, DAY_OF_MONTH);
