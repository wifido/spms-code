-- Create table
create table TI_DRIVER_LOG_TIME_TMP
(
  ID           NUMBER(19) not null,
  DRIVE_MEMBER VARCHAR2(25),
  DRIVE_TM     DATE,
  END_TM       DATE,
  START_PLACE  VARCHAR2(500),
  END_PLACE    VARCHAR2(500),
  DRIVE_TIME   NUMBER(20,4)
);
-- Add comments to the columns 
comment on column TI_DRIVER_LOG_TIME_TMP.ID
  is '����ID';
comment on column TI_DRIVER_LOG_TIME_TMP.DRIVE_MEMBER
  is 'Ա������';
comment on column TI_DRIVER_LOG_TIME_TMP.DRIVE_TM
  is '��ʼʱ��';
comment on column TI_DRIVER_LOG_TIME_TMP.END_TM
  is '����ʱ��';
comment on column TI_DRIVER_LOG_TIME_TMP.START_PLACE
  is '��ʼ����';
comment on column TI_DRIVER_LOG_TIME_TMP.END_PLACE
  is '��������';
comment on column TI_DRIVER_LOG_TIME_TMP.DRIVE_TIME
  is '��ʻʱ��';
-- Create/Recreate indexes 
create index IDX_TI_DRIVER_TIME1 on TI_DRIVER_LOG_TIME_TMP (DRIVE_MEMBER, TO_CHAR(DRIVE_TM,'yyyymmdd'));



-- Create sequence 
create sequence SEQ_TI_DRIVER_TIME
minvalue 1
maxvalue 999999999999999999999999999
start with 131088
increment by 1
cache 20;