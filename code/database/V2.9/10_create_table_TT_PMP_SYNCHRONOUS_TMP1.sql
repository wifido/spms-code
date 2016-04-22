-- Create table
create table TT_PMP_SYNCHRONOUS_TMP1
(
  ID             NUMBER(38),
  EMP_CODE       VARCHAR2(30),
  BEGIN_DATE     VARCHAR2(8),
  END_DATE       VARCHAR2(8),
  BEGIN_TM1      VARCHAR2(6),
  END_TM1        VARCHAR2(6),
  BEGIN_TM_FLAG1 NUMBER(1),
  BEGIN_TM2      VARCHAR2(6),
  END_TM2        VARCHAR2(6),
  BEGIN_TM_FLAG2 NUMBER(1),
  BEGIN_TM3      VARCHAR2(6),
  END_TM3        VARCHAR2(6),
  BEGIN_TM_FLAG3 NUMBER(1),
  TMR_DAY_FLAG   VARCHAR2(1),
  OFF_DUTY_FLAG  VARCHAR2(20),
  CLASS_SYSTEM   VARCHAR2(1),
  CREATE_TM      DATE default sysdate,
  NODE_KEY       VARCHAR2(30),
  STATE_FLG      NUMBER(1) default 0,
  ERROR_INFO     VARCHAR2(10),
  EMP_POST_TYPE  VARCHAR2(1)
);
-- Add comments to the table 
comment on table TT_PMP_SYNCHRONOUS_TMP1
  is '�����Ű�����ͬ����PMP��ʱ��';
-- Add comments to the columns 
comment on column TT_PMP_SYNCHRONOUS_TMP1.ID
  is '�����Ű�����ID';
comment on column TT_PMP_SYNCHRONOUS_TMP1.EMP_CODE
  is 'Ա������';
comment on column TT_PMP_SYNCHRONOUS_TMP1.BEGIN_DATE
  is '��ʼ����';
comment on column TT_PMP_SYNCHRONOUS_TMP1.END_DATE
  is '��������';
comment on column TT_PMP_SYNCHRONOUS_TMP1.BEGIN_TM1
  is '��ʼʱ��1';
comment on column TT_PMP_SYNCHRONOUS_TMP1.END_TM1
  is '����ʱ��1';
comment on column TT_PMP_SYNCHRONOUS_TMP1.BEGIN_TM_FLAG1
  is 'ʱ���1�Ƿ���ֵ��0�ޣ�1��';
comment on column TT_PMP_SYNCHRONOUS_TMP1.BEGIN_TM2
  is '��ʼʱ��2';
comment on column TT_PMP_SYNCHRONOUS_TMP1.END_TM2
  is '����ʱ��2';
comment on column TT_PMP_SYNCHRONOUS_TMP1.BEGIN_TM_FLAG2
  is 'ʱ���2���Ƿ���ֵ��0�ޣ�1��';
comment on column TT_PMP_SYNCHRONOUS_TMP1.BEGIN_TM3
  is '��ʼʱ��3';
comment on column TT_PMP_SYNCHRONOUS_TMP1.END_TM3
  is '����ʱ��3';
comment on column TT_PMP_SYNCHRONOUS_TMP1.BEGIN_TM_FLAG3
  is 'ʱ���3���Ƿ���ֵ��0�ޣ�1��';
comment on column TT_PMP_SYNCHRONOUS_TMP1.TMR_DAY_FLAG
  is 'ǰһ���ʶ';
comment on column TT_PMP_SYNCHRONOUS_TMP1.OFF_DUTY_FLAG
  is '��Ϣ��ʶ(��ϢΪOFF)';
comment on column TT_PMP_SYNCHRONOUS_TMP1.CLASS_SYSTEM
  is '�Ű���Դ(2-�Ű�ϵͳ)';
comment on column TT_PMP_SYNCHRONOUS_TMP1.CREATE_TM
  is '������������(�����ֶ�)';
comment on column TT_PMP_SYNCHRONOUS_TMP1.STATE_FLG
  is 'ͬ��״̬ 0-��ʼ״̬1-���ڴ���2-ͬ���ɹ�3-ͬ��ʧ��';
comment on column TT_PMP_SYNCHRONOUS_TMP1.ERROR_INFO
  is 'ͬ��ʧ�ܵĴ���ԭ��';
comment on column TT_PMP_SYNCHRONOUS_TMP1.EMP_POST_TYPE
  is '��λ���ͣ�1-����Ա��2-����Ա[һ��]��3-�ֹܡ�4-�ͷ���';
