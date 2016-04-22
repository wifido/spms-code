-- Create table
create table TT_PMP_SYNCHRONOUS_TMP
(
  ID            NUMBER(38),
  EMP_CODE      VARCHAR2(30),
  BEGIN_DATE    VARCHAR2(8),
  END_DATE      VARCHAR2(8),
  BEGIN_TM      VARCHAR2(6),
  END_TM        VARCHAR2(6),
  TMR_DAY_FLAG  VARCHAR2(1),
  OFF_DUTY_FLAG VARCHAR2(20),
  CLASS_SYSTEM  VARCHAR2(1),
  CREATE_TM     DATE default sysdate,
  NODE_KEY      VARCHAR2(30),
  STATE_FLG     NUMBER(1) default 0,
  ERROR_INFO    VARCHAR2(10),
  EMP_POST_TYPE VARCHAR2(1)
);
-- Add comments to the table 
comment on table TT_PMP_SYNCHRONOUS_TMP
  is '�Ű�ͬ����PMP��ʱ��';
-- Add comments to the columns 
comment on column TT_PMP_SYNCHRONOUS_TMP.ID
  is '�Ű�����ID';
comment on column TT_PMP_SYNCHRONOUS_TMP.EMP_CODE
  is 'Ա������';
comment on column TT_PMP_SYNCHRONOUS_TMP.BEGIN_DATE
  is '��ʼ����';
comment on column TT_PMP_SYNCHRONOUS_TMP.END_DATE
  is '��������';
comment on column TT_PMP_SYNCHRONOUS_TMP.BEGIN_TM
  is '��ʼʱ��';
comment on column TT_PMP_SYNCHRONOUS_TMP.END_TM
  is '����ʱ��';
comment on column TT_PMP_SYNCHRONOUS_TMP.TMR_DAY_FLAG
  is 'ǰһ���ʶ';
comment on column TT_PMP_SYNCHRONOUS_TMP.OFF_DUTY_FLAG
  is '��Ϣ��ʶ(��ϢΪOFF)';
comment on column TT_PMP_SYNCHRONOUS_TMP.CLASS_SYSTEM
  is '�Ű���Դ(2-�Ű�ϵͳ)';
comment on column TT_PMP_SYNCHRONOUS_TMP.CREATE_TM
  is '������������(�����ֶ�)';
comment on column TT_PMP_SYNCHRONOUS_TMP.STATE_FLG
  is 'ͬ��״̬ 0-��ʼ״̬1-���ڴ���2-ͬ���ɹ�3-ͬ��ʧ��';
comment on column TT_PMP_SYNCHRONOUS_TMP.ERROR_INFO
  is 'ͬ��ʧ�ܵĴ���ԭ��';
comment on column TT_PMP_SYNCHRONOUS_TMP.EMP_POST_TYPE
  is '��λ���ͣ�1-����Ա��2-����Ա[һ��]��3-�ֹܡ�4-�ͷ���';
