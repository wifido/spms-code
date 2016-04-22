  -- Create sequence 
create sequence SEQ_SPMS_INTERFACE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;


-- Create table
create table TI_SPMS_SAP_HCM_OUT_PGC
(
  ID         NUMBER(38) not null,
  BEGDA      VARCHAR2(40),
  EMP_CODE   VARCHAR2(40),
  ENDDA      VARCHAR2(40),
  PERSG      VARCHAR2(1),
  PERSK      VARCHAR2(2),
  LASTUPDATE DATE default sysdate
);
-- Add comments to the table 
comment on table TI_SPMS_SAP_HCM_OUT_PGC
  is '��Ա���ͱ䶯����';
-- Add comments to the columns 
comment on column TI_SPMS_SAP_HCM_OUT_PGC.ID
  is '����ID';
comment on column TI_SPMS_SAP_HCM_OUT_PGC.BEGDA
  is '��ʼ����';
comment on column TI_SPMS_SAP_HCM_OUT_PGC.EMP_CODE
  is 'Ա������';
comment on column TI_SPMS_SAP_HCM_OUT_PGC.ENDDA
  is '��������';
comment on column TI_SPMS_SAP_HCM_OUT_PGC.PERSG
  is 'Ա����';
comment on column TI_SPMS_SAP_HCM_OUT_PGC.PERSK
  is 'Ա������';
comment on column TI_SPMS_SAP_HCM_OUT_PGC.LASTUPDATE
  is '����ʱ��';
  
  

