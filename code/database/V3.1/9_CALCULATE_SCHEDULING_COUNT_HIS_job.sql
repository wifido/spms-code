DECLARE
   CHANG_JO NUMBER;

BEGIN
  DBMS_JOB.SUBMIT( CHANG_JO,
                  '/**ÿ��1��14�㣬������Ա���Ű�ͳ�Ʊ��ݡ�sw��off����**/SCHEDULING_COUNT_HIS(to_char(''2015-10'',''yyyy-mm''));',
                  sysdate,
                  'TRUNC(LAST_DAY(SYSDATE))+1+14/24');
END;
/ 
COMMIT;