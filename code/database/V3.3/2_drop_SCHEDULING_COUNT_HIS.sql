declare
    id1 varchar2(20);
  begin
    select T.JOB
      into id1
      from user_jobs t
     where t.WHAT like '%/**ÿ��1��17�㣬������Ա���Ű�ͳ�Ʊ��ݡ�sw��off����**/SCHEDULING_COUNT_HIS%';
    dbms_job.remove(id1);
  end;
/
  commit;
