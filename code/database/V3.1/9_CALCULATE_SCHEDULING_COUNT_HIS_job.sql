DECLARE
   CHANG_JO NUMBER;

BEGIN
  DBMS_JOB.SUBMIT( CHANG_JO,
                  '/**每月1号14点，处理历员工排班统计表休、sw、off数据**/SCHEDULING_COUNT_HIS(to_char(''2015-10'',''yyyy-mm''));',
                  sysdate,
                  'TRUNC(LAST_DAY(SYSDATE))+1+14/24');
END;
/ 
COMMIT;