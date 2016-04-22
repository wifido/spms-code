DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**ÿ��4������20��ͳ�����³���ͳ�Ʊ�**/OPERATION_REPORT_LASTMONTH;',TRUNC(LAST_DAY(SYSDATE))+4+20/24,'TRUNC(LAST_DAY(SYSDATE))+4+20/24');
END;
/
commit;


DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**ÿ�����һ������20��ͳ�����³���ͳ�Ʊ�**/OPERATION_REPORT_NEXTMONTH;',TRUNC(LAST_DAY(SYSDATE))+20/24,'TRUNC(LAST_DAY(SYSDATE)) + 20/24');
END;
/
commit;


DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**ÿ����������20��ͳ�����ܳ���ͳ�Ʊ�**/OPERATION_REPORT_LASTWEEK;',TRUNC(next_day(sysdate,'Sunday'))+20/24,'TRUNC(next_day(sysdate,''Sunday''))+20/24');
END;
/
commit;


DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**ÿ������20��ͳ�����ܳ���ͳ�Ʊ�**/OPERATION_REPORT_NEXTWEEK;',TRUNC(sysdate) +1+19/24,'TRUNC(sysdate) +1+19/24');
END;
/
commit;



DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**������Ա���������λ����**/HANDLER_DEPT_POST_RECORD_DATA;', TRUNC(sysdate) +1+1/24,'TRUNC(sysdate) +1+1/24');
END;
/
commit;



DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**������Ա���������λ����**/HANDLER_DEPT_POST_RECORD_DATA;', TRUNC(sysdate) +1+13/24,'TRUNC(sysdate) +1+13/24');
END;
/
commit;
