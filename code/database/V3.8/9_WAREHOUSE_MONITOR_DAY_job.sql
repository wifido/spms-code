DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**每天1点，处理系统日期-1至系统日期+7仓管监控报表数据**/WAREHOUSE_MONITOR_DAY;',
                  sysdate,
                  'TRUNC(SYSDATE+1)+1/24');
END;
/ 
commit;
