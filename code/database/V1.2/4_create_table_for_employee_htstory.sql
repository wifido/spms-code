-- Create table
create table TM_OSS_EMPLOYEE_HISTORY
(
  EMP_ID             NUMBER(38) not null,
  EMP_CODE           VARCHAR2(20),
  EMP_NAME           VARCHAR2(500),
  EMP_DUTY_NAME      VARCHAR2(100),
  DEPT_ID            NUMBER(19),
  GROUP_ID           NUMBER(38),
  CREATE_TM          DATE,
  MODIFIED_TM        DATE,
  CREATE_EMP_CODE    VARCHAR2(20),
  MODIFIED_EMP_CODE  VARCHAR2(20),
  WORK_TYPE          NUMBER(1),
  EMAIL              VARCHAR2(100),
  DIMISSION_DT       DATE,
  SF_DATE            DATE,
  EMP_POST_TYPE      VARCHAR2(1),
  IS_HAVE_COMMISSION VARCHAR2(1),
  POSITION_ATTR      VARCHAR2(20),
  DUTY_SERIAL        VARCHAR2(20),
  VERSION_NUMBER     NUMBER
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table TM_OSS_EMPLOYEE_HISTORY
  is '��Ա��Ϣ��ʷ��';
-- Add comments to the columns 
comment on column TM_OSS_EMPLOYEE_HISTORY.EMP_ID
  is '����ID';
comment on column TM_OSS_EMPLOYEE_HISTORY.EMP_CODE
  is '���ţ�ϵͳ�Զ����ɣ���100000000��ʼ;�ڲ���Աʹ�����ʹ���)';
comment on column TM_OSS_EMPLOYEE_HISTORY.EMP_NAME
  is '����';
comment on column TM_OSS_EMPLOYEE_HISTORY.EMP_DUTY_NAME
  is 'ְλ';
comment on column TM_OSS_EMPLOYEE_HISTORY.DEPT_ID
  is '����ID';
comment on column TM_OSS_EMPLOYEE_HISTORY.GROUP_ID
  is 'С��ID';
comment on column TM_OSS_EMPLOYEE_HISTORY.CREATE_TM
  is '����ʱ��';
comment on column TM_OSS_EMPLOYEE_HISTORY.MODIFIED_TM
  is '�޸�ʱ��';
comment on column TM_OSS_EMPLOYEE_HISTORY.CREATE_EMP_CODE
  is '�����˹���';
comment on column TM_OSS_EMPLOYEE_HISTORY.MODIFIED_EMP_CODE
  is '�޸��˹���';
comment on column TM_OSS_EMPLOYEE_HISTORY.WORK_TYPE
  is '�ù�����(1-��ȫ���ƹ���2-���ؼ�ϰ����3-������ǲ��4-ȫ����Ա����5-ʵϰ����6-���)';
comment on column TM_OSS_EMPLOYEE_HISTORY.EMAIL
  is '��������';
comment on column TM_OSS_EMPLOYEE_HISTORY.DIMISSION_DT
  is '��ְ����';
comment on column TM_OSS_EMPLOYEE_HISTORY.SF_DATE
  is '��ְ����';
comment on column TM_OSS_EMPLOYEE_HISTORY.EMP_POST_TYPE
  is '��λ���ͣ�1-����Ա��2-����Ա��3-�ֹܡ�4-�ͷ���';
comment on column TM_OSS_EMPLOYEE_HISTORY.IS_HAVE_COMMISSION
  is '�ֹ���Ա��Ϣ �Ƿ���������� 0�����룬1����';
comment on column TM_OSS_EMPLOYEE_HISTORY.POSITION_ATTR
  is '�������ͣ�һ�ߡ����ߣ�';
comment on column TM_OSS_EMPLOYEE_HISTORY.DUTY_SERIAL
  is '��λ����';
comment on column TM_OSS_EMPLOYEE_HISTORY.VERSION_NUMBER
  is '�汾��';
