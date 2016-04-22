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
  is '�Ű�ȷ�ϼ�¼��';
-- Add comments to the columns 
comment on column TT_DRIVER_SCHED_CONFIRM_RECORD.ID
  is '����';
comment on column TT_DRIVER_SCHED_CONFIRM_RECORD.AREA_CODE
  is '��������';
comment on column TT_DRIVER_SCHED_CONFIRM_RECORD.DEPARTMENT_CODE
  is '�������';
comment on column TT_DRIVER_SCHED_CONFIRM_RECORD.YEAR_WEEK
  is '����(yyyy-week)';
comment on column TT_DRIVER_SCHED_CONFIRM_RECORD.EMPLOYEE_NAME
  is 'Ա������';
comment on column TT_DRIVER_SCHED_CONFIRM_RECORD.EMPLOYEE_CODE
  is 'Ա������';
comment on column TT_DRIVER_SCHED_CONFIRM_RECORD.CONFIRM_STATUS
  is 'ȷ��״̬';
comment on column TT_DRIVER_SCHED_CONFIRM_RECORD.CONFIRM_TIME
  is 'ȷ��ʱ��';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TT_DRIVER_SCHED_CONFIRM_RECORD
  add constraint PK_SCHED_CONFIRM_REPORT_ID primary key (ID);
--Create index with the table
create index INX_DRIVER_SCHEDULING8 on TT_DRIVER_SCHEDULING (YEAR_WEEK, SCHEDULING_TYPE, CONFIRM_STATUS);


