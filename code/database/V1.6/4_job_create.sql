
DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**ÿ��2���賿4:00ͳ�Ƶ����Ű��**/SCHEDULING_COUNT(to_char(sysdate,''YYYY-MM''));',sysdate,'TRUNC(SYSDATE+2)+4/24');
END;
/
commit;



DECLARE job2011 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2011,
'/**ÿ��5���賿4:00ͳ����һ���Ű��**/SCHEDULING_COUNT(TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              ''YYYY-MM''));',sysdate,'TRUNC(LAST_DAY(SYSDATE))+5+4/24');
END;
/
commit;

