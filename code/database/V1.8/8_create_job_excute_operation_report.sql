
DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**ÿ���賿0��30����������ݣ����϶�Ӧ����**/SPMS2CDP_OPERATION_CLASS;',sysdate,'TRUNC(SYSDATE+1)+0.5/24');
END;
/
commit;






DECLARE job2011 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2011,
'/**ÿ���賿3��30ͳ��������ְ���Ű������ͳ���ʱ��������CDP�ӿڱ�**/SPMS2CDP_BY_OPERATION;',sysdate,'TRUNC(SYSDATE+1)+3.5/24');
END;
/
commit;



