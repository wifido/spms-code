DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**ÿ��21�㴦��˾���Ű����ݵ��ӿڱ�**/DRIVER_SAP_SYNCHRONIZATION;',sysdate,'TRUNC(SYSDATE+1)+21/24');
END;
/
commit;