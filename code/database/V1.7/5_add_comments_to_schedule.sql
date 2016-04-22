alter table TI_TCAS_SPMS_SCHEDULE add KQ_XSS VARCHAR2(12);
-- Add comments to the columns 
comment on column TI_TCAS_SPMS_SCHEDULE.KQ_XSS
  is '考勤小时数';
  
  
alter table TI_TCAS_SPMS_SCHEDULE add STDAZ  NUMBER(7,2);
-- Add comments to the columns 
comment on column TI_TCAS_SPMS_SCHEDULE.STDAZ
  is '加班时数';
  
alter table TI_TCAS_SPMS_SCHEDULE add ARBST  VARCHAR2(12);
-- Add comments to the columns 
comment on column TI_TCAS_SPMS_SCHEDULE.ARBST
  is '排班时长';
  
alter table TI_TCAS_SPMS_SCHEDULE add PAPER  VARCHAR2(18);
-- Add comments to the columns 
comment on column TI_TCAS_SPMS_SCHEDULE.PAPER
  is '考勤年月';