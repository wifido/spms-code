-- Add/modify columns 
alter table TT_SCH_EMP_ATTENCE_CLASS add SYNC_TM date;
-- Add comments to the columns 
comment on column TT_SCH_EMP_ATTENCE_CLASS.SYNC_TM
  is '数据同步时间';
