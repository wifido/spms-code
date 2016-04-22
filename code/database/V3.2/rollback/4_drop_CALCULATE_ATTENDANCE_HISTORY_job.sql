declare
 id1  varchar2(20);
 begin  
  select  T.JOB into id1 from user_jobs t  where t.WHAT like '%CALCULATE_ATTENDANCE_HISTORY%';
  dbms_job.remove(id1); 
  end;
/ 
  commit;
