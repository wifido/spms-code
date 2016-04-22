DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**每月4号晚上20点统计上月场地统计表**/OPERATION_REPORT_LASTMONTH;',TRUNC(LAST_DAY(SYSDATE))+4+20/24,'TRUNC(LAST_DAY(SYSDATE))+4+20/24');
END;
/
commit;


DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**每月最后一天晚上20点统计下月场地统计表**/OPERATION_REPORT_NEXTMONTH;',TRUNC(LAST_DAY(SYSDATE))+20/24,'TRUNC(LAST_DAY(SYSDATE)) + 20/24');
END;
/
commit;


DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**每周周日晚上20点统计上周场地统计表**/OPERATION_REPORT_LASTWEEK;',TRUNC(next_day(sysdate,'Sunday'))+20/24,'TRUNC(next_day(sysdate,''Sunday''))+20/24');
END;
/
commit;


DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**每天晚上20点统计下周场地统计表**/OPERATION_REPORT_NEXTWEEK;',TRUNC(sysdate) +1+19/24,'TRUNC(sysdate) +1+19/24');
END;
/
commit;



DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**处理人员所属网点岗位归属**/HANDLER_DEPT_POST_RECORD_DATA;', TRUNC(sysdate) +1+1/24,'TRUNC(sysdate) +1+1/24');
END;
/
commit;



DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**处理人员所属网点岗位归属**/HANDLER_DEPT_POST_RECORD_DATA;', TRUNC(sysdate) +1+13/24,'TRUNC(sysdate) +1+13/24');
END;
/
commit;
