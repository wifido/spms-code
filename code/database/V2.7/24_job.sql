DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**每隔30分钟，处理排班数据触发数据和行车日志触发的数据**/P_HAND_DRIVE_CONVERT_AND_SCH;',
                  sysdate,
                  'sysdate+30/24/60');
END;
/ 
commit;

DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**每隔30分钟，计算排班吻合率报表**/PKG_HANDLE_COMPARE_REPORT.HANDLE_SCHEDULING_TASK_REPORT;',
                  sysdate,
                  'sysdate+30/24/60');
END;
/ 
commit;

DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**每天22点，计算排班吻合率报表**/PKG_HANDLE_COMPARE_REPORT.HANDLE_DRIVING_CONVERT_DATA;',
                  sysdate,
                  'TRUNC(SYSDATE+1)+22/24');
END;
/ 
commit;
