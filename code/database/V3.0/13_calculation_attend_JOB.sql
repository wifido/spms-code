DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
                   '/**ÿ��4:00ͳ�����������Ű���Ϣ**/CALCULATION_OF_ATTENDANCE(TO_CHAR(SYSDATE-8,
                                              ''YYYY-MM-DD''));',
                  SYSDATE,
                 'TRUNC(SYSDATE+1)+4/24');
END;
/
commit;
                                              
