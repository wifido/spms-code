DECLARE
   CHANG_JO NUMBER;

BEGIN
  DBMS_JOB.SUBMIT( CHANG_JO,
                  '/**ÿ��1��8�㣬������ʷ��������**/CALCULATE_ATTENDANCE_HISTORY;',
                  sysdate,
                  'TRUNC(LAST_DAY(SYSDATE))+1+8/24');
END;
/ 
COMMIT;
