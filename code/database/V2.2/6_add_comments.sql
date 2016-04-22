alter table TI_SAP_ZTHR_PT_DETAIL_TMP add POST_TYPE VARCHAR2(1);
-- Add comments to the columns 
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.POST_TYPE
  is '岗位类型1运作3仓管';