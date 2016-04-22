
DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**每隔2天凌晨4:00统计当月排班表**/SCHEDULING_COUNT(to_char(sysdate,''YYYY-MM''));',sysdate,'TRUNC(SYSDATE+2)+4/24');
END;
/
commit;



DECLARE job2011 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2011,
'/**每月5号凌晨4:00统计上一月排班表**/SCHEDULING_COUNT(TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              ''YYYY-MM''));',sysdate,'TRUNC(LAST_DAY(SYSDATE))+5+4/24');
END;
/
commit;

