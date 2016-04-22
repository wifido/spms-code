
DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**每天凌晨1点30统计CDP接口人员数据和排班数据**/SPMS_TO_CDP_EMP_COUNT;',sysdate,'TRUNC(SYSDATE+1)+1.5/24');
END;
/
commit;