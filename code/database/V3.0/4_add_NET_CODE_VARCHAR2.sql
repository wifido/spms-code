-- Add/modify columns 
alter table TM_OSS_EMPLOYEE_TMP add NET_CODE VARCHAR2(36);
-- Add comments to the columns 
comment on column TM_OSS_EMPLOYEE_TMP.NET_CODE
  is 'µ±Ç°ÍøÂç±àÂë';