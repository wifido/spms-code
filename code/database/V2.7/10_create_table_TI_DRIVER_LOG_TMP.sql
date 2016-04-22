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
  is '��ʻ���౨��';
-- Add comments to the columns 
comment on column TI_DRIVER_LOG_TMP.ID
  is '����';
comment on column TI_DRIVER_LOG_TMP.DRIVE_MEMBER
  is 'Ա������';
comment on column TI_DRIVER_LOG_TMP.DAY_OF_MONTH
  is '��������(yyyymmdd)';
comment on column TI_DRIVER_LOG_TMP.DAY_ATTENDANCE_TIME
  is '����ʱ��';
comment on column TI_DRIVER_LOG_TMP.DAY_DRIVE_TIME
  is '��ʻʱ��';
comment on column TI_DRIVER_LOG_TMP.DEPT_CODE
  is 'ƴ���������';
comment on column TI_DRIVER_LOG_TMP.COMPARE_STATUS
  is '�ԱȽ��';
  
alter table TI_DRIVER_LOG_TMP
  add constraint ID primary key (ID);
 
create index IDX_DRIVER_LOG_TMP on TI_DRIVER_LOG_TMP (DRIVE_MEMBER, DEPT_CODE);

create index IDX_DRIVER_LOG_TMP1 on TI_DRIVER_LOG_TMP (DRIVE_MEMBER, DAY_OF_MONTH);

create index IDX_STATUS on TI_DRIVER_LOG_TMP (COMPARE_STATUS);
