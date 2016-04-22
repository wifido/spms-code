-- Create table
create table OPERATION_COUNT_SHEDULING
(
  MONTH_ID           VARCHAR2(28),
  AREA_CODE          VARCHAR2(38),
  DEPT_CODE          VARCHAR2(10),
  EMP_CODE           VARCHAR2(50),
  EMP_NAME           VARCHAR2(50),
  PERSK_TXT          VARCHAR2(50),
  SF_DATE            DATE,
  EMP_STATUS         NUMBER(1),
  SHEDULE_NUM        NUMBER(10),
  GROUP_NUM          NUMBER(10),
  PROCESS_NUM        NUMBER(10),
  LENGTH_TIME_OF_DAY NUMBER(10,2),
  REST_DAYS          NUMBER(10),
  TOTAL_ATTENDANCE   NUMBER(10),
  DEPT_ID            VARCHAR2(38)
);
-- Add comments to the table 
comment on table OPERATION_COUNT_SHEDULING
  is '�Ű��';
-- Add comments to the columns 
comment on column OPERATION_COUNT_SHEDULING.MONTH_ID
  is '�·�';
comment on column OPERATION_COUNT_SHEDULING.AREA_CODE
  is '��������';
comment on column OPERATION_COUNT_SHEDULING.DEPT_CODE
  is '�������';
comment on column OPERATION_COUNT_SHEDULING.EMP_CODE
  is 'Ա������';
comment on column OPERATION_COUNT_SHEDULING.EMP_NAME
  is 'Ա������';
comment on column OPERATION_COUNT_SHEDULING.PERSK_TXT
  is '��Ա����';
comment on column OPERATION_COUNT_SHEDULING.SF_DATE
  is '��ְʱ��';
comment on column OPERATION_COUNT_SHEDULING.EMP_STATUS
  is '��ְ״̬��1��ְ0��ְ��';
comment on column OPERATION_COUNT_SHEDULING.SHEDULE_NUM
  is '�������';
comment on column OPERATION_COUNT_SHEDULING.GROUP_NUM
  is 'С������';
comment on column OPERATION_COUNT_SHEDULING.PROCESS_NUM
  is '��������';
comment on column OPERATION_COUNT_SHEDULING.LENGTH_TIME_OF_DAY
  is '�վ�ʱ��';
comment on column OPERATION_COUNT_SHEDULING.REST_DAYS
  is '��Ϣ����';
comment on column OPERATION_COUNT_SHEDULING.TOTAL_ATTENDANCE
  is '����ʱ��';
comment on column OPERATION_COUNT_SHEDULING.DEPT_ID
  is '����ID';
-- Create/Recreate indexes 
create index INDEX_MONTH_ID on OPERATION_COUNT_SHEDULING (MONTH_ID);
