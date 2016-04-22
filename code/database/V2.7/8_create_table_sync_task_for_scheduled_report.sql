-- Create sequence 
create sequence SEQ_SCHEDULED_REPORT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

-- Create table
create table SYNC_TASK_FOR_SCHEDULED_REPORT
(
  ID             NUMBER(38),
  EMPLOYEE_CODE  VARCHAR2(20),
  DAY_OF_MONTH   VARCHAR2(210),
  SYNC_STATUS    VARCHAR2(1),
  CONFIGURE_CODE VARCHAR2(20),
  OPERATION_TYPE VARCHAR2(1),
  CREATED_TIME   DATE default sysdate,
  UPDATED_TIME   DATE default sysdate,
  DEPT_CODE      VARCHAR2(20),
  YEAR_MONTH     VARCHAR2(20),
  SCHEDULE_ID    NUMBER(38)
);

-- Add comments to the table 
comment on table SYNC_TASK_FOR_SCHEDULED_REPORT
  is '�޸��Ű��¼��';
-- Add comments to the columns 
comment on column SYNC_TASK_FOR_SCHEDULED_REPORT.ID
  is '����';
comment on column SYNC_TASK_FOR_SCHEDULED_REPORT.EMPLOYEE_CODE
  is 'Ա������';
comment on column SYNC_TASK_FOR_SCHEDULED_REPORT.DAY_OF_MONTH
  is '������(yyyymmdd)';
comment on column SYNC_TASK_FOR_SCHEDULED_REPORT.SYNC_STATUS
  is 'ͬ��״̬';
comment on column SYNC_TASK_FOR_SCHEDULED_REPORT.CONFIGURE_CODE
  is '�Ű����';
comment on column SYNC_TASK_FOR_SCHEDULED_REPORT.OPERATION_TYPE
  is '��������';
comment on column SYNC_TASK_FOR_SCHEDULED_REPORT.CREATED_TIME
  is '����ʱ��';
comment on column SYNC_TASK_FOR_SCHEDULED_REPORT.UPDATED_TIME
  is '�޸�ʱ��';
comment on column SYNC_TASK_FOR_SCHEDULED_REPORT.DEPT_CODE
  is '�������';
comment on column SYNC_TASK_FOR_SCHEDULED_REPORT.YEAR_MONTH
  is '����(yyyy-mm)';
comment on column SYNC_TASK_FOR_SCHEDULED_REPORT.SCHEDULE_ID
  is '�Ű�����';
  
create index INX_SYNC_TASK_SCH1 on SYNC_TASK_FOR_SCHEDULED_REPORT (SYNC_STATUS);

create index INX_SYNC_TASK_SCH2 on SYNC_TASK_FOR_SCHEDULED_REPORT (EMPLOYEE_CODE, YEAR_MONTH);


  
