
DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**ÿ���賿1��30ͳ��CDP�ӿ���Ա���ݺ��Ű�����**/SPMS_TO_CDP_EMP_COUNT;',sysdate,'TRUNC(SYSDATE+1)+1.5/24');
END;
/
commit;