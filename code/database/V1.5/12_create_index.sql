
-- Create/Recreate indexes 
create index IDEX_EMP_CODE on TI_SAP_ZTHR_PT_DETAIL (pernr);

-- Create/Recreate indexes 
create index INDEX_DEPT_ID on TT_PB_SHEDULE_BY_MONTH_LOG (DEPT_ID);
  
create index INDEX_EMP_CODE on TT_PB_SHEDULE_BY_MONTH_LOG (EMP_CODE);

create index INDEX_YM on TT_PB_SHEDULE_BY_MONTH_LOG (YM);
