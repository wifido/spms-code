DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**每天21点处理司机排班数据到接口表**/DRIVER_SAP_SYNCHRONIZATION;',sysdate,'TRUNC(SYSDATE+1)+21/24');
END;
/
commit;