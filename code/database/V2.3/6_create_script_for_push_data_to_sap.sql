
-- Create table
create table TT_SAP_DRIVER_LOG
(
  LOG_ID      NUMBER(38) not null,
  PERNR       VARCHAR2(20),
  ZAUSW       VARCHAR2(20),
  LDATE       VARCHAR2(8),
  LTIME       VARCHAR2(6),
  LDAYT       VARCHAR2(14),
  ORIGF       VARCHAR2(1),
  ABWGR       VARCHAR2(4),
  PDC_USRUP   VARCHAR2(20),
  ZHRXGBZ     VARCHAR2(1),
  STATUS      NUMBER(1) default 0,
  SYNC_DATE   DATE,
  FAIL_DESC   VARCHAR2(100),
  CREATE_DATE DATE
);
-- Add comments to the columns 
comment on column TT_SAP_DRIVER_LOG.LOG_ID
  is '�г���־ID';
comment on column TT_SAP_DRIVER_LOG.PERNR
  is 'Ա������';
comment on column TT_SAP_DRIVER_LOG.ZAUSW
  is 'Ա������';
comment on column TT_SAP_DRIVER_LOG.LDATE
  is 'ˢ������ ''20140429''';
comment on column TT_SAP_DRIVER_LOG.LTIME
  is 'ˢ��ʱ�� ''083000''';
comment on column TT_SAP_DRIVER_LOG.LDAYT
  is 'ˢ������+ʱ�� ��20140429083000��';
comment on column TT_SAP_DRIVER_LOG.ORIGF
  is 'ˢ��������Դ��ʶ';
comment on column TT_SAP_DRIVER_LOG.ABWGR
  is '����ԭ��';
comment on column TT_SAP_DRIVER_LOG.PDC_USRUP
  is '���ڻ����к�';
comment on column TT_SAP_DRIVER_LOG.ZHRXGBZ
  is '��ʶ ��I��������  "D��ɾ��" ';
comment on column TT_SAP_DRIVER_LOG.STATUS
  is 'ͬ��״̬�� 0��δͬ��  1��ͬ���ɹ� 2��ͬ��ʧ��';
comment on column TT_SAP_DRIVER_LOG.SYNC_DATE
  is 'ͬ��ʱ��';
comment on column TT_SAP_DRIVER_LOG.FAIL_DESC
  is 'ʧ��ԭ��';
comment on column TT_SAP_DRIVER_LOG.CREATE_DATE
  is '��¼����ʱ��';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TT_SAP_DRIVER_LOG
  add primary key (LOG_ID);

  

-- Create table
create table TT_SAP_DRIVER_LOG_HIS
(
  LOG_ID      NUMBER(38) not null,
  PERNR       VARCHAR2(20),
  ZAUSW       VARCHAR2(20),
  LDATE       VARCHAR2(8),
  LTIME       VARCHAR2(6),
  LDAYT       VARCHAR2(14),
  ORIGF       VARCHAR2(1),
  ABWGR       VARCHAR2(4),
  PDC_USRUP   VARCHAR2(20),
  ZHRXGBZ     VARCHAR2(1),
  STATUS      NUMBER(1) default 0,
  SYNC_DATE   DATE,
  FAIL_DESC   VARCHAR2(100),
  CREATE_DATE DATE
);
-- Add comments to the columns 
comment on column TT_SAP_DRIVER_LOG_HIS.LOG_ID
  is '�г���־ID';
comment on column TT_SAP_DRIVER_LOG_HIS.PERNR
  is 'Ա������';
comment on column TT_SAP_DRIVER_LOG_HIS.ZAUSW
  is 'Ա������';
comment on column TT_SAP_DRIVER_LOG_HIS.LDATE
  is 'ˢ������ ''20140429''';
comment on column TT_SAP_DRIVER_LOG_HIS.LTIME
  is 'ˢ��ʱ�� ''083000''';
comment on column TT_SAP_DRIVER_LOG_HIS.LDAYT
  is 'ˢ������+ʱ�� ��20140429083000��';
comment on column TT_SAP_DRIVER_LOG_HIS.ORIGF
  is 'ˢ��������Դ��ʶ';
comment on column TT_SAP_DRIVER_LOG_HIS.ABWGR
  is '����ԭ��';
comment on column TT_SAP_DRIVER_LOG_HIS.PDC_USRUP
  is '���ڻ����к�';
comment on column TT_SAP_DRIVER_LOG_HIS.ZHRXGBZ
  is '��ʶ ��I��������  "D��ɾ��" ';
comment on column TT_SAP_DRIVER_LOG_HIS.STATUS
  is 'ͬ��״̬�� 0��δͬ��  1��ͬ���ɹ� 2��ͬ��ʧ��';
comment on column TT_SAP_DRIVER_LOG_HIS.SYNC_DATE
  is 'ͬ��ʱ��';
comment on column TT_SAP_DRIVER_LOG_HIS.FAIL_DESC
  is 'ʧ��ԭ��';
comment on column TT_SAP_DRIVER_LOG_HIS.CREATE_DATE
  is '��¼����ʱ��';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TT_SAP_DRIVER_LOG_HIS
  add primary key (LOG_ID);
  

-- Create sequence 
create sequence SEQ_TT_SAP_DRIVER_LOG
minvalue 1
maxvalue 999999999999999999
start with 11
increment by 1
cache 20;

