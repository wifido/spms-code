DECLARE
   CHANG_JO NUMBER;

BEGIN
  DBMS_JOB.SUBMIT( CHANG_JO,
                  '/**每月1号14点，处理历史场地排班为SW OFF 休的数据**/CALULATE_REST_NUM_HIS;',
                  sysdate,
                  'TRUNC(LAST_DAY(SYSDATE))+1+14/24');
END;
/ 
COMMIT;
