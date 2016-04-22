
DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**每天凌晨0点30运作班别数据，加上对应日期**/SPMS2CDP_OPERATION_CLASS;',sysdate,'TRUNC(SYSDATE+1)+0.5/24');
END;
/
commit;






DECLARE job2011 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2011,
'/**每天凌晨3点30统计运作在职、排班人数和出勤时长，处理到CDP接口表**/SPMS2CDP_BY_OPERATION;',sysdate,'TRUNC(SYSDATE+1)+3.5/24');
END;
/
commit;



