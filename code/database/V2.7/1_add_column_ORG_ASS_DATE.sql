-- Add/modify columns 
alter table TI_SAP_SYNCHRONOUS_EMP add ORG_ASS_DATE date;
-- Add comments to the columns 
comment on column TI_SAP_SYNCHRONOUS_EMP.ORG_ASS_DATE
  is '转岗时间';



alter table TI_SAP_SYNCHRONOUS_EMP_his add ORG_ASS_DATE date;
-- Add comments to the columns 
comment on column TI_SAP_SYNCHRONOUS_EMP_his.ORG_ASS_DATE
  is '转岗时间';