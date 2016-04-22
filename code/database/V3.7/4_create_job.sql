DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**每天2点，处理未来7天的一线监控报表数据**/DISPATCH_MONITOR_DAY;',
                  sysdate,
                  'TRUNC(SYSDATE+1)+2/24');
END;
/ 
commit;





DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**每月5号凌晨统计上月一线监控报表数据**/DISPATCH_MONITOR_MONTH;',TRUNC(LAST_DAY(SYSDATE))+5+3/24,'TRUNC(LAST_DAY(SYSDATE))+5+3/24');
END;
/
commit;
