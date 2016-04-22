DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**每天20点30分，处理运作PMP排班数据到接口表**/OPERATION_PMP_SYNC;',
                  sysdate,
                  'TRUNC(SYSDATE+1)+20.5/24');
END;
/ 
commit;


DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**每天21点30分，处理一线PMP排班数据到接口表**/SCHEDULING_PMP_SYNCHRONIZATION;',
                  sysdate,
                  'TRUNC(SYSDATE+1)+21.5/24');
END;
/ 
commit;


DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**每天22点30分，处理仓管PMP排班数据到接口表**/WAREHOUSE_PMP_SYNCHRONIZATION;',
                  sysdate,
                  'TRUNC(SYSDATE+1)+22.5/24');
END;
/ 
commit;


DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**每天22点，处理香港区运作排班数据到接口表**/OPERATION2SAP_SYNCBY_HK;',
                  sysdate,
                  'TRUNC(SYSDATE+1)+22/24');
END;
/ 
commit;


 


