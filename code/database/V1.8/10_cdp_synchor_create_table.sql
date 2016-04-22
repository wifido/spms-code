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
  is '��ת����嵥(�Ű�ϵͳ�ӿ�)';
-- Add comments to the columns 
comment on column TI_TRANSFER_BATCH_LIST.TRANS_BATCH_DATE
  is '����';
comment on column TI_TRANSFER_BATCH_LIST.AREA_CODE
  is '����';
comment on column TI_TRANSFER_BATCH_LIST.ZONE_CODE
  is '��ת��';
comment on column TI_TRANSFER_BATCH_LIST.TRANSFER_CODE
  is '��α���';
comment on column TI_TRANSFER_BATCH_LIST.BEGIN_TM
  is '�ƻ���ʼʱ��';
comment on column TI_TRANSFER_BATCH_LIST.END_TM
  is '�ƻ�����ʱ��';
  
GRANT ALL ON TI_TRANSFER_BATCH_LIST TO SPMSETL;