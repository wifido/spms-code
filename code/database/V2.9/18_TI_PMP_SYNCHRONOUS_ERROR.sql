-- Create table
create table TI_PMP_SYNCHRONOUS_ERROR
(
  ID            NUMBER(38) not null,
  EMP_CODE      VARCHAR2(30),
  BEGIN_DATE    VARCHAR2(8),
  END_DATE      VARCHAR2(8),
  BEGIN_TM      VARCHAR2(6),
  END_TM        VARCHAR2(6),
  TMR_DAY_FLAG  VARCHAR2(1),
  OFF_DUTY_FLAG VARCHAR2(20),
  CLASS_SYSTEM  VARCHAR2(1),
  THEME_NAME    VARCHAR2(100),
  ERROR_INFO    VARCHAR2(300),
  LASTUPDATE    DATE default sysdate,
  EXTRAINFO1    VARCHAR2(100),
  EXTRAINFO2    VARCHAR2(300),
  EXTRAINFO3    VARCHAR2(1000),
  DEAL_FLAG     NUMBER(1) default 0
);
-- Add comments to the table 
comment on table TI_PMP_SYNCHRONOUS_ERROR
  is 'SAPͬ�����Ű������Ϣ��ӿڱ�';
-- Add comments to the columns 
comment on column TI_PMP_SYNCHRONOUS_ERROR.ID
  is '����,���';
comment on column TI_PMP_SYNCHRONOUS_ERROR.EMP_CODE
  is 'Ա������';
comment on column TI_PMP_SYNCHRONOUS_ERROR.BEGIN_DATE
  is '��ʼ����';
comment on column TI_PMP_SYNCHRONOUS_ERROR.END_DATE
  is '��������';
comment on column TI_PMP_SYNCHRONOUS_ERROR.BEGIN_TM
  is '��ʼʱ��';
comment on column TI_PMP_SYNCHRONOUS_ERROR.END_TM
  is '����ʱ��';
comment on column TI_PMP_SYNCHRONOUS_ERROR.TMR_DAY_FLAG
  is 'ǰһ���ʶ';
comment on column TI_PMP_SYNCHRONOUS_ERROR.OFF_DUTY_FLAG
  is '��Ϣ��ʶ(ONΪ��Ϣ��Ĭ��ΪOFF)';
comment on column TI_PMP_SYNCHRONOUS_ERROR.CLASS_SYSTEM
  is '�Ű���Դ(2-����ϵͳ)';
comment on column TI_PMP_SYNCHRONOUS_ERROR.THEME_NAME
  is '��������';
comment on column TI_PMP_SYNCHRONOUS_ERROR.ERROR_INFO
  is 'ͬ��ʧ�ܵĴ���ԭ��';
comment on column TI_PMP_SYNCHRONOUS_ERROR.LASTUPDATE
  is 'ͬ��ʱ��';
comment on column TI_PMP_SYNCHRONOUS_ERROR.EXTRAINFO1
  is '��չ�ֶ�1';
comment on column TI_PMP_SYNCHRONOUS_ERROR.EXTRAINFO2
  is '��չ�ֶ�2';
comment on column TI_PMP_SYNCHRONOUS_ERROR.EXTRAINFO3
  is '��չ�ֶ�3';
comment on column TI_PMP_SYNCHRONOUS_ERROR.DEAL_FLAG
  is '1:�Ѵ���0:δ����2��ͬ������';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TI_PMP_SYNCHRONOUS_ERROR
  add primary key (ID);
-- Create/Recreate indexes 
create index IDX_PMP_SYNCHRONOUS_ERROR1 on TI_PMP_SYNCHRONOUS_ERROR (DEAL_FLAG, ERROR_INFO, BEGIN_DATE);
-- Grant/Revoke object privileges 
grant select, insert, update, delete on TI_PMP_SYNCHRONOUS_ERROR to PMPETL;
