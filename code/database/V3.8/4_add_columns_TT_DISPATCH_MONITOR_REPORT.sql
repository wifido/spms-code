
-- Add/modify columns 
alter table TT_DISPATCH_MONITOR_REPORT add EMP_POST_TYPE varchar2(1);
-- Add comments to the columns 
comment on column TT_DISPATCH_MONITOR_REPORT.EMP_POST_TYPE
  is '��λ���ͣ�2-һ�ߡ�3-�ֹܣ�';

update TT_DISPATCH_MONITOR_REPORT set EMP_POST_TYPE ='2';
commit;  
  
