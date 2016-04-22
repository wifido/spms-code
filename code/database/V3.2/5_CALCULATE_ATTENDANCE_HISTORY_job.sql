DECLARE
   CHANG_JO NUMBER;

BEGIN
  DBMS_JOB.SUBMIT( CHANG_JO,
                  '/**每月1号8点，处理历史考勤数据**/CALCULATE_ATTENDANCE_HISTORY;',
                  sysdate,
                  'TRUNC(LAST_DAY(SYSDATE))+1+8/24');
END;
/ 
COMMIT;
