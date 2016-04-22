BEGIN
  FOR JOB_ROW IN (SELECT JOB
                    FROM USER_JOBS
                   WHERE WHAT LIKE '%WAREH_SCHEDUL_AGREEMENT%') LOOP
    DBMS_JOB.REMOVE(JOB_ROW.JOB);
  END LOOP;
  COMMIT;
END;
/

DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,
'/**ÿ��1���賿3:00ͳ�Ƶ����Ű��**/WAREH_SCHEDUL_AGREEMENT(to_char(sysdate,''YYYYMM''));',sysdate,'TRUNC(SYSDATE+1)+3/24');
END;
/
commit;



DECLARE job2011 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2011,
'/**ÿ��5���賿3:00ͳ����һ���Ű��**/WAREH_SCHEDUL_AGREEMENT(TO_CHAR(ADD_MONTHS(SYSDATE, -1),
                                              ''YYYYMM''));',sysdate,'TRUNC(LAST_DAY(SYSDATE))+5+3/24');
END;
/
commit;

