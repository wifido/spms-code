-- Create table
create table TT_CDP_EMPNUM_SCHEDUL_HIS
(
  DAY_OF_MONTH            VARCHAR2(10),
  HQ_CODE                 VARCHAR2(30),
  AREA_CODE               VARCHAR2(30),
  DEPT_CODE               VARCHAR2(30),
  DIVISION_CODE           VARCHAR2(30),
  FULL_TIME_NUM           NUMBER(10),
  N_FULL_TIME_NUM         NUMBER(10),
  TOTAL_PAYROLLS          NUMBER(10,2),
  FULL_TIME_SCHEDUL_NUM   NUMBER(10),
  N_FULL_TIME_SCHEDUL_NUM NUMBER(10),
  SCHEDUL_NUM_TOTAL       NUMBER(10,2),
  POST_TYPE               VARCHAR2(10),
  CREATE_DATE             DATE
);
-- Add comments to the table 
comment on table TT_CDP_EMPNUM_SCHEDUL_HIS
  is 'CDPͳ�����ݽӿڱ�';
-- Add comments to the columns 
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.DAY_OF_MONTH
  is '����';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.HQ_CODE
  is '��Ӫ����';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.AREA_CODE
  is '����';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.DEPT_CODE
  is '����';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.DIVISION_CODE
  is '�ֲ�';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.FULL_TIME_NUM
  is 'ȫ������ְ����';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.N_FULL_TIME_NUM
  is '��ȫ��ְ����';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.TOTAL_PAYROLLS
  is '��ְ�����ϼ�';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.FULL_TIME_SCHEDUL_NUM
  is 'ȫ�����Ű�����';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.N_FULL_TIME_SCHEDUL_NUM
  is '��ȫ�Ű�����';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.SCHEDUL_NUM_TOTAL
  is '�Ű������ϼ�';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.POST_TYPE
  is '��λ����';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.CREATE_DATE
  is '����ʱ��';
