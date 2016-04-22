DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**每月第一天凌晨3点30删除历史排班数据，只保留一个季度的历史排班数据**/DELETE_SCHEDULING_DATA;',sysdate,'TRUNC(last_day(sysdate))+1+3.5/24');
END;
/
commit;

