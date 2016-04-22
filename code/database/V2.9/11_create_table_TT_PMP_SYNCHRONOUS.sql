-- Create table
create table TT_PMP_SYNCHRONOUS
(
  ID                NUMBER(38) not null,
  EMP_CODE          VARCHAR2(30),
  BEGIN_DATE        VARCHAR2(8),
  END_DATE          VARCHAR2(8),
  BEGIN_TM          VARCHAR2(6),
  END_TM            VARCHAR2(6),
  TMR_DAY_FLAG      VARCHAR2(1),
  OFF_DUTY_FLAG     VARCHAR2(20),
  CLASS_SYSTEM      VARCHAR2(1),
  CREATE_TM         DATE default sysdate,
  NODE_KEY          VARCHAR2(30),
  STATE_FLG         NUMBER(1) default 0,
  ERROR_INFO        VARCHAR2(1000),
  SCHEDULE_DAILY_ID NUMBER(38),
  EMP_POST_TYPE     VARCHAR2(1),
  SYNC_TM           DATE
);
-- Add comments to the table 
comment on table TT_PMP_SYNCHRONOUS
  is '�Ű�ͬ����PMP��Ϣ��';
-- Add comments to the columns 
comment on column TT_PMP_SYNCHRONOUS.ID
  is '����,���';
comment on column TT_PMP_SYNCHRONOUS.EMP_CODE
  is 'Ա������';
comment on column TT_PMP_SYNCHRONOUS.BEGIN_DATE
  is '��ʼ����';
comment on column TT_PMP_SYNCHRONOUS.END_DATE
  is '��������';
comment on column TT_PMP_SYNCHRONOUS.BEGIN_TM
  is '��ʼʱ��';
comment on column TT_PMP_SYNCHRONOUS.END_TM
  is '����ʱ��';
comment on column TT_PMP_SYNCHRONOUS.TMR_DAY_FLAG
  is 'ǰһ���ʶ';
comment on column TT_PMP_SYNCHRONOUS.OFF_DUTY_FLAG
  is '��Ϣ��ʶ(��ϢOFF)';
comment on column TT_PMP_SYNCHRONOUS.CLASS_SYSTEM
  is '�Ű���Դ(2-�Ű�ϵͳ)';
comment on column TT_PMP_SYNCHRONOUS.CREATE_TM
  is '������������(�����ֶ�)';
comment on column TT_PMP_SYNCHRONOUS.STATE_FLG
  is 'ͬ��״̬ 0-��ʼ״̬1-���ڴ���2-ͬ���ɹ�3-ͬ��ʧ��';
comment on column TT_PMP_SYNCHRONOUS.ERROR_INFO
  is 'ͬ��ʧ�ܵĴ���ԭ��';
comment on column TT_PMP_SYNCHRONOUS.SCHEDULE_DAILY_ID
  is '�Ű�ID';
comment on column TT_PMP_SYNCHRONOUS.EMP_POST_TYPE
  is '��λ���ͣ�1-����Ա��2-����Ա[һ��]��3-�ֹܡ�4-�ͷ���';
comment on column TT_PMP_SYNCHRONOUS.SYNC_TM
  is '����ʱ��';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TT_PMP_SYNCHRONOUS
  add primary key (ID);
-- Create/Recreate indexes 
create index IDX_PMP_SYNCHRONOUS_1 on TT_PMP_SYNCHRONOUS (BEGIN_DATE);
create index IDX_PMP_SYNCHRONOUS_2 on TT_PMP_SYNCHRONOUS (BEGIN_DATE, EMP_CODE, EMP_POST_TYPE);
create index IDX_PMP_SYNCHRONOUS_3 on TT_PMP_SYNCHRONOUS (STATE_FLG);
create index IDX_PMP_SYNCHRONOUS_4 on TT_PMP_SYNCHRONOUS (CREATE_TM);
grant select on TT_PMP_SYNCHRONOUS  to PMPETL;