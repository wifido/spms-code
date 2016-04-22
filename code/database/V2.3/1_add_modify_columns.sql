-- Add/modify columns 
alter table TT_PB_SHEDULE_BY_MONTH add EMP_NAME VARCHAR2(500);
alter table TT_PB_SHEDULE_BY_MONTH add WORK_TYPE NUMBER(2);
-- Add comments to the columns 
comment on column TT_PB_SHEDULE_BY_MONTH.EMP_NAME
  is '被排班人姓名';
comment on column TT_PB_SHEDULE_BY_MONTH.WORK_TYPE
  is '用工类型';




-- Add/modify columns 
alter table TT_PB_SHEDULE_BY_MONTH_LOG add EMP_NAME VARCHAR2(500);
alter table TT_PB_SHEDULE_BY_MONTH_LOG add WORK_TYPE NUMBER(2);
-- Add comments to the columns 
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.EMP_NAME
  is '被排班人姓名';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.WORK_TYPE
  is '用工类型';