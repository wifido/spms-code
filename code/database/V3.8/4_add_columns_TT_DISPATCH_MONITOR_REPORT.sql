
-- Add/modify columns 
alter table TT_DISPATCH_MONITOR_REPORT add EMP_POST_TYPE varchar2(1);
-- Add comments to the columns 
comment on column TT_DISPATCH_MONITOR_REPORT.EMP_POST_TYPE
  is '岗位类型（2-一线、3-仓管）';

update TT_DISPATCH_MONITOR_REPORT set EMP_POST_TYPE ='2';
commit;  
  
