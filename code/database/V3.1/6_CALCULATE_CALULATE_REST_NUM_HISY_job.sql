DECLARE
   CHANG_JO NUMBER;

BEGIN
  DBMS_JOB.SUBMIT( CHANG_JO,
                  '/**ÿ��1��14�㣬������ʷ�����Ű�ΪSW OFF �ݵ�����**/CALULATE_REST_NUM_HIS;',
                  sysdate,
                  'TRUNC(LAST_DAY(SYSDATE))+1+14/24');
END;
/ 
COMMIT;
