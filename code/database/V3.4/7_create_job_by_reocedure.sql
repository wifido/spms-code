DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**ÿ������12��봦��Ԥ��ְԱ����Ա����**/SAP_ZTHR_ETL_HR_EMF;',sysdate,'TRUNC(SYSDATE+1)+12.5/24');
END;
/
commit;



DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**ÿ������0��봦��Ԥ��ְԱ����Ա����**/SAP_ZTHR_ETL_HR_EMF;',sysdate,'TRUNC(SYSDATE+1)+0.5/24');
END;
/
commit;







