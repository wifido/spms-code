BEGIN
  FOR JOB_ROW IN (SELECT JOB
                    FROM USER_JOBS
                   WHERE WHAT LIKE '%OP_SCH_INPUT_REPORT%') LOOP
    DBMS_JOB.REMOVE(JOB_ROW.JOB);
  END LOOP;
  COMMIT;
END;
/

DECLARE
  job2010 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2010,
'/**每隔1天凌晨3:00统计当月排班表**/OP_SCH_INPUT_REPORT(to_char(sysdate,''YYYY-MM''));',sysdate,'TRUNC(SYSDATE+1)+3/24');
END;
/ 
commit;


DECLARE
  job2011 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT(job2011,'/**每隔1天中午12:00统计当月排班表**/OP_SCH_INPUT_REPORT(to_char(sysdate,''YYYY-MM''));',sysdate,'TRUNC(SYSDATE + 1)+12/24');
END;
/ 
commit;