DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**ÿ�µ�һ���賿3��30ɾ����ʷ�Ű����ݣ�ֻ����һ�����ȵ���ʷ�Ű�����**/DELETE_SCHEDULING_DATA;',sysdate,'TRUNC(last_day(sysdate))+1+3.5/24');
END;
/
commit;

