-- Add/modify columns 
alter table TM_OSS_EMPLOYEE add VERSION_NUMBER  NUMBER;
-- Add comments to the columns 
comment on column TM_OSS_EMPLOYEE.VERSION_NUMBER
  is '°æ±¾ºÅ';
