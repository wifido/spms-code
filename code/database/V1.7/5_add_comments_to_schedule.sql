alter table TI_TCAS_SPMS_SCHEDULE add KQ_XSS VARCHAR2(12);
-- Add comments to the columns 
comment on column TI_TCAS_SPMS_SCHEDULE.KQ_XSS
  is '����Сʱ��';
  
  
alter table TI_TCAS_SPMS_SCHEDULE add STDAZ  NUMBER(7,2);
-- Add comments to the columns 
comment on column TI_TCAS_SPMS_SCHEDULE.STDAZ
  is '�Ӱ�ʱ��';
  
alter table TI_TCAS_SPMS_SCHEDULE add ARBST  VARCHAR2(12);
-- Add comments to the columns 
comment on column TI_TCAS_SPMS_SCHEDULE.ARBST
  is '�Ű�ʱ��';
  
alter table TI_TCAS_SPMS_SCHEDULE add PAPER  VARCHAR2(18);
-- Add comments to the columns 
comment on column TI_TCAS_SPMS_SCHEDULE.PAPER
  is '��������';