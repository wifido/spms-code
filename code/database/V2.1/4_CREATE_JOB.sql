DECLARE
  JOB_COUNT NUMBER;
BEGIN
  DBMS_JOB.SUBMIT(JOB_COUNT, '/**每天凌晨4点统计运作中转场CDP预警信息**/SPMS2CDP_BY_OPERATION_TWO;',
                      SYSDATE,
                      'TRUNC(SYSDATE+1)+4/24');
  COMMIT;
END;

