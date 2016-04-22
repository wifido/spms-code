
DECLARE job2010 NUMBER;

BEGIN
 DBMS_JOB.SUBMIT(job2010,'/**每月4号21:00扫描排班、工序安排提交确认表，统计上个月吻合率**/STATISTICAL_COINCIDENCE_RATE;',sysdate,'TRUNC(LAST_DAY(SYSDATE))+4+21/24');
END;
/
commit;



DECLARE  JOB_SPMS2TCAS NUMBER;
BEGIN 
    SYS.DBMS_JOB.SUBMIT(JOB_SAP2SPMS_EMP, '/**每天21:00将考勤数据同步至计提接口表**/HANDLE_SAP_ATTENCE;', SYSDATE, 'TRUNC(SYSDATE+1)+21/24');
    COMMIT;
END;
/