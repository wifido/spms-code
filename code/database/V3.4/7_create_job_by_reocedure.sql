DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**每天中午12点半处理预入职员工进员工表**/SAP_ZTHR_ETL_HR_EMF;',sysdate,'TRUNC(SYSDATE+1)+12.5/24');
END;
/
commit;



DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**每天晚上0点半处理预入职员工进员工表**/SAP_ZTHR_ETL_HR_EMF;',sysdate,'TRUNC(SYSDATE+1)+0.5/24');
END;
/
commit;







