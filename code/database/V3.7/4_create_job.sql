DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**ÿ��2�㣬����δ��7���һ�߼�ر�������**/DISPATCH_MONITOR_DAY;',
                  sysdate,
                  'TRUNC(SYSDATE+1)+2/24');
END;
/ 
commit;





DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**ÿ��5���賿ͳ������һ�߼�ر�������**/DISPATCH_MONITOR_MONTH;',TRUNC(LAST_DAY(SYSDATE))+5+3/24,'TRUNC(LAST_DAY(SYSDATE))+5+3/24');
END;
/
commit;
