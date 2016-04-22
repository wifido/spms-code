-- Create table
create table TM_SPMS2CDP_BY_OPERATION_INFO
(
  SCHEDULE_CODE VARCHAR2(9),
  SCHEDULE_DT   DATE,
  DEPT_CODE     VARCHAR2(38),
  START1_TIME   DATE,
  END1_TIME     DATE,
  START2_TIME   DATE,
  END2_TIME     DATE,
  START3_TIME   DATE,
  END3_TIME     DATE,
  YM            VARCHAR2(25)
);
-- Add comments to the table 
comment on table TM_SPMS2CDP_BY_OPERATION_INFO
  is '��������Ϣ��';
-- Add comments to the columns 
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.SCHEDULE_CODE
  is '������';
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.SCHEDULE_DT
  is '�Ű�����';
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.DEPT_CODE
  is '�������';
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.START1_TIME
  is '��ʼʱ��һ';
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.END1_TIME
  is '����ʱ��һ';
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.START2_TIME
  is '��ʼʱ���';
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.END2_TIME
  is '����ʱ���';
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.START3_TIME
  is '��ʼʱ����';
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.END3_TIME
  is '����ʱ����';
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.YM
  is '����';
-- Create/Recreate indexes 
create index IDX_TM_SPMS2CDP_INFO1 on TM_SPMS2CDP_BY_OPERATION_INFO (DEPT_CODE, SCHEDULE_CODE, SCHEDULE_DT, YM);
