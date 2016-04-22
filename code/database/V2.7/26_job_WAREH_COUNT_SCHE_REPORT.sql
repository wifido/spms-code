DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**每天晚上22点将分点部考勤数据处理到分点部考勤分析表**/WAREH_COUNT_SCHE_REPORT;',
                  sysdate,
                  'TRUNC(SYSDATE+1)+22/24');
END;
/ 
commit;