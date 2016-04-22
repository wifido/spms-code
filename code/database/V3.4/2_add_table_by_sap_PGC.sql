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
  is '人员类型变动主题';
-- Add comments to the columns 
comment on column TI_SPMS_SAP_HCM_OUT_PGC.ID
  is '主键ID';
comment on column TI_SPMS_SAP_HCM_OUT_PGC.BEGDA
  is '开始日期';
comment on column TI_SPMS_SAP_HCM_OUT_PGC.EMP_CODE
  is '员工工号';
comment on column TI_SPMS_SAP_HCM_OUT_PGC.ENDDA
  is '结束日期';
comment on column TI_SPMS_SAP_HCM_OUT_PGC.PERSG
  is '员工组';
comment on column TI_SPMS_SAP_HCM_OUT_PGC.PERSK
  is '员工子组';
comment on column TI_SPMS_SAP_HCM_OUT_PGC.LASTUPDATE
  is '更新时间';
  
  

