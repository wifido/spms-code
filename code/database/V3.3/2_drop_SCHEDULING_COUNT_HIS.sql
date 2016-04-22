declare
    id1 varchar2(20);
  begin
    select T.JOB
      into id1
      from user_jobs t
     where t.WHAT like '%/**每月1号17点，处理历员工排班统计表休、sw、off数据**/SCHEDULING_COUNT_HIS%';
    dbms_job.remove(id1);
  end;
/
  commit;
