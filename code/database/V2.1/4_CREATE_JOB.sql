DECLARE
  JOB_COUNT NUMBER;
BEGIN
  DBMS_JOB.SUBMIT(JOB_COUNT, '/**ÿ���賿4��ͳ��������ת��CDPԤ����Ϣ**/SPMS2CDP_BY_OPERATION_TWO;',
                      SYSDATE,
                      'TRUNC(SYSDATE+1)+4/24');
  COMMIT;
END;

