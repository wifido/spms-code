DECLARE jobno NUMBER;
BEGIN
select job into jobno  from user_jobs t where t.WHAT like '%OPERATION_REPORT_LASTWEEK%';
dbms_job.remove(jobno);
end; 
/
commit;


DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**ÿ����������20��ͳ�����ܳ���ͳ�Ʊ�**/OPERATION_REPORT_LASTWEEK;',TRUNC(next_day(sysdate,'Sunday'))+20/24,'TRUNC(next_day(sysdate,''Sunday''))+20/24');
END;
/
commit;