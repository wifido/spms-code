-- Create table
create table TT_SAPSYNCHRONOUS_HKTMP
(
  ID                NUMBER(38),
  EMP_CODE          VARCHAR2(30),
  BEGIN_DATE        VARCHAR2(8),
  END_DATE          VARCHAR2(8),
  BEGIN_TM          VARCHAR2(6),
  END_TM            VARCHAR2(6),
  TMR_DAY_FLAG      VARCHAR2(1),
  OFF_DUTY_FLAG     VARCHAR2(20),
  CLASS_SYSTEM      VARCHAR2(1),
  CREATE_TM         DATE,
  NODE_KEY          VARCHAR2(30),
  STATE_FLG         NUMBER(1),
  ERROR_INFO        VARCHAR2(1000),
  SCHEDULE_DAILY_ID NUMBER(38),
  EMP_POST_TYPE     VARCHAR2(1),
  SYNC_TM           DATE
);
-- Add comments to the table 
comment on table TT_SAPSYNCHRONOUS_HKTMP
  is '�Ű�ͬ����SAP��Ϣ��ʱ';
-- Add comments to the columns 
comment on column TT_SAPSYNCHRONOUS_HKTMP.TMR_DAY_FLAG
  is 'ǰһ���ʶ';
comment on column TT_SAPSYNCHRONOUS_HKTMP.OFF_DUTY_FLAG
  is '��Ϣ��ʶ(��ϢΪOFF)';
comment on column TT_SAPSYNCHRONOUS_HKTMP.CLASS_SYSTEM
  is '�Ű���Դ(2-�Ű�ϵͳ)';
comment on column TT_SAPSYNCHRONOUS_HKTMP.STATE_FLG
  is 'ͬ��״̬ 0-��ʼ״̬1-���ڴ���2-ͬ���ɹ�3-ͬ��ʧ��';
comment on column TT_SAPSYNCHRONOUS_HKTMP.ERROR_INFO
  is 'ͬ��ʧ�ܵĴ���ԭ��';
comment on column TT_SAPSYNCHRONOUS_HKTMP.EMP_POST_TYPE
  is '��λ���ͣ�1-����Ա��2-����Ա[һ��]��3-�ֹܡ�4-�ͷ���';
comment on column TT_SAPSYNCHRONOUS_HKTMP.SYNC_TM
  is '����ʱ��';
