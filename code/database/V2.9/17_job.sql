DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**ÿ��20��30�֣���������PMP�Ű����ݵ��ӿڱ�**/OPERATION_PMP_SYNC;',
                  sysdate,
                  'TRUNC(SYSDATE+1)+20.5/24');
END;
/ 
commit;


DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**ÿ��21��30�֣�����һ��PMP�Ű����ݵ��ӿڱ�**/SCHEDULING_PMP_SYNCHRONIZATION;',
                  sysdate,
                  'TRUNC(SYSDATE+1)+21.5/24');
END;
/ 
commit;


DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**ÿ��22��30�֣�����ֹ�PMP�Ű����ݵ��ӿڱ�**/WAREHOUSE_PMP_SYNCHRONIZATION;',
                  sysdate,
                  'TRUNC(SYSDATE+1)+22.5/24');
END;
/ 
commit;


DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                  '/**ÿ��22�㣬��������������Ű����ݵ��ӿڱ�**/OPERATION2SAP_SYNCBY_HK;',
                  sysdate,
                  'TRUNC(SYSDATE+1)+22/24');
END;
/ 
commit;


 


