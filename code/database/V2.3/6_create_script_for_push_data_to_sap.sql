
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
  is '行车日志ID';
comment on column TT_SAP_DRIVER_LOG.PERNR
  is '员工工号';
comment on column TT_SAP_DRIVER_LOG.ZAUSW
  is '员工工号';
comment on column TT_SAP_DRIVER_LOG.LDATE
  is '刷卡日期 ''20140429''';
comment on column TT_SAP_DRIVER_LOG.LTIME
  is '刷卡时间 ''083000''';
comment on column TT_SAP_DRIVER_LOG.LDAYT
  is '刷卡日期+时间 ‘20140429083000’';
comment on column TT_SAP_DRIVER_LOG.ORIGF
  is '刷卡数据来源标识';
comment on column TT_SAP_DRIVER_LOG.ABWGR
  is '补卡原因';
comment on column TT_SAP_DRIVER_LOG.PDC_USRUP
  is '考勤机序列号';
comment on column TT_SAP_DRIVER_LOG.ZHRXGBZ
  is '标识 “I：新增”  "D：删除" ';
comment on column TT_SAP_DRIVER_LOG.STATUS
  is '同步状态： 0：未同步  1：同步成功 2：同步失败';
comment on column TT_SAP_DRIVER_LOG.SYNC_DATE
  is '同步时间';
comment on column TT_SAP_DRIVER_LOG.FAIL_DESC
  is '失败原因';
comment on column TT_SAP_DRIVER_LOG.CREATE_DATE
  is '记录创建时间';
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
  is '行车日志ID';
comment on column TT_SAP_DRIVER_LOG_HIS.PERNR
  is '员工工号';
comment on column TT_SAP_DRIVER_LOG_HIS.ZAUSW
  is '员工工号';
comment on column TT_SAP_DRIVER_LOG_HIS.LDATE
  is '刷卡日期 ''20140429''';
comment on column TT_SAP_DRIVER_LOG_HIS.LTIME
  is '刷卡时间 ''083000''';
comment on column TT_SAP_DRIVER_LOG_HIS.LDAYT
  is '刷卡日期+时间 ‘20140429083000’';
comment on column TT_SAP_DRIVER_LOG_HIS.ORIGF
  is '刷卡数据来源标识';
comment on column TT_SAP_DRIVER_LOG_HIS.ABWGR
  is '补卡原因';
comment on column TT_SAP_DRIVER_LOG_HIS.PDC_USRUP
  is '考勤机序列号';
comment on column TT_SAP_DRIVER_LOG_HIS.ZHRXGBZ
  is '标识 “I：新增”  "D：删除" ';
comment on column TT_SAP_DRIVER_LOG_HIS.STATUS
  is '同步状态： 0：未同步  1：同步成功 2：同步失败';
comment on column TT_SAP_DRIVER_LOG_HIS.SYNC_DATE
  is '同步时间';
comment on column TT_SAP_DRIVER_LOG_HIS.FAIL_DESC
  is '失败原因';
comment on column TT_SAP_DRIVER_LOG_HIS.CREATE_DATE
  is '记录创建时间';
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

