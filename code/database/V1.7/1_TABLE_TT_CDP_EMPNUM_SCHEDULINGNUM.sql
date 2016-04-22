-- Create table
create table TI_SCH_EMPLOYEECLASS_PLAIN
(
  ID         NUMBER(20) not null,
  EMPLOYEEID VARCHAR2(20),
  DUTYDATE   VARCHAR2(20),
  CREATETIME DATE,
  SYNC_TM    DATE default SYSDATE
);
-- Add comments to the table 
comment on table TI_SCH_EMPLOYEECLASS_PLAIN
  is 'SCH_CDH����Աֵ����Ϣ�ӿڱ�';
-- Add comments to the columns 
comment on column TI_SCH_EMPLOYEECLASS_PLAIN.ID
  is '�߼�����';
comment on column TI_SCH_EMPLOYEECLASS_PLAIN.EMPLOYEEID
  is '����Ա����';
comment on column TI_SCH_EMPLOYEECLASS_PLAIN.DUTYDATE
  is 'ֵ������';
comment on column TI_SCH_EMPLOYEECLASS_PLAIN.CREATETIME
  is 'SCH_CDH����ʱ��';
comment on column TI_SCH_EMPLOYEECLASS_PLAIN.SYNC_TM
  is 'ͬ��ʱ��';
-- GrantRevoke object privileges 
grant all on TI_SCH_EMPLOYEECLASS_PLAIN to SPMSETL;

-- Create table
create table TT_CDP_EMPNUM_SCHEDULINGNUM
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
comment on table TT_CDP_EMPNUM_SCHEDULINGNUM
  is 'CDPͳ�����ݽӿڱ�';
-- Add comments to the columns 
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.DAY_OF_MONTH
  is '����';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.HQ_CODE
  is '��Ӫ����';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.AREA_CODE
  is '����';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.DEPT_CODE
  is '����';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.DIVISION_CODE
  is '�ֲ�';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.FULL_TIME_NUM
  is 'ȫ������ְ����';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.N_FULL_TIME_NUM
  is '��ȫ��ְ����';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.TOTAL_PAYROLLS
  is '��ְ�����ϼ�';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.FULL_TIME_SCHEDUL_NUM
  is 'ȫ�����Ű�����';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.N_FULL_TIME_SCHEDUL_NUM
  is '��ȫ�Ű�����';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.SCHEDUL_NUM_TOTAL
  is '�Ű������ϼ�';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.POST_TYPE
  is '��λ����
';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.CREATE_DATE
  is '����ʱ��';