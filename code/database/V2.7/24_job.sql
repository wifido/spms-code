DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**ÿ��30���ӣ������Ű����ݴ������ݺ��г���־����������**/P_HAND_DRIVE_CONVERT_AND_SCH;',
                  sysdate,
                  'sysdate+30/24/60');
END;
/ 
commit;

DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**ÿ��30���ӣ������Ű��Ǻ��ʱ���**/PKG_HANDLE_COMPARE_REPORT.HANDLE_SCHEDULING_TASK_REPORT;',
                  sysdate,
                  'sysdate+30/24/60');
END;
/ 
commit;

DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**ÿ��22�㣬�����Ű��Ǻ��ʱ���**/PKG_HANDLE_COMPARE_REPORT.HANDLE_DRIVING_CONVERT_DATA;',
                  sysdate,
                  'TRUNC(SYSDATE+1)+22/24');
END;
/ 
commit;
