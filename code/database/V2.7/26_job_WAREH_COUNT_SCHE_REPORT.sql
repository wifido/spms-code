DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**ÿ������22�㽫�ֵ㲿�������ݴ����ֵ㲿���ڷ�����**/WAREH_COUNT_SCHE_REPORT;',
                  sysdate,
                  'TRUNC(SYSDATE+1)+22/24');
END;
/ 
commit;