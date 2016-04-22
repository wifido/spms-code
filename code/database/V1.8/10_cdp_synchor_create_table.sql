-- Create table
create table TI_TRANSFER_BATCH_LIST
(
  TRANS_BATCH_DATE DATE,
  AREA_CODE        VARCHAR2(50 CHAR),
  ZONE_CODE        VARCHAR2(50 CHAR),
  TRANSFER_CODE    VARCHAR2(50 CHAR),
  BEGIN_TM         DATE,
  END_TM           DATE,
  DATE_TM          DATE,
  SYNC_TM          DATE DEFAULT SYSDATE
);
-- Add comments to the table 
comment on table TI_TRANSFER_BATCH_LIST
  is '中转班次清单(排班系统接口)';
-- Add comments to the columns 
comment on column TI_TRANSFER_BATCH_LIST.TRANS_BATCH_DATE
  is '日期';
comment on column TI_TRANSFER_BATCH_LIST.AREA_CODE
  is '地区';
comment on column TI_TRANSFER_BATCH_LIST.ZONE_CODE
  is '中转场';
comment on column TI_TRANSFER_BATCH_LIST.TRANSFER_CODE
  is '班次编码';
comment on column TI_TRANSFER_BATCH_LIST.BEGIN_TM
  is '计划开始时间';
comment on column TI_TRANSFER_BATCH_LIST.END_TM
  is '计划结束时间';
  
GRANT ALL ON TI_TRANSFER_BATCH_LIST TO SPMSETL;