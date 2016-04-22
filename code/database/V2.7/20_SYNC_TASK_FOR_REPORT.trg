CREATE OR REPLACE TRIGGER SYNC_TASK_FOR_REPORT
  BEFORE insert OR UPDATE OR DELETE on tt_driver_scheduling
  for each row
declare
  -- ��������;
  V_OPERATE_TYPE NUMBER;
  --�Ű����Ӧ��ID;
  V_ID NUMBER;

  V_EMP_CODE        VARCHAR2(21);
  V_DAY_OF_MONTH    VARCHAR2(21);
  V_CONFIGURE_CODE  VARCHAR2(21);
  V_DEPARTMENT_CODE VARCHAR2(21);
  V_YEAR_MONTH      VARCHAR2(21);
  V_SCHEDULE_TYPE   NUMBER;
BEGIN
  --���Ű�����Ϊʵ���Ű�ʱ�Ž��д���

  IF DELETING THEN
    V_OPERATE_TYPE    := 2;
    V_ID              := :OLD.ID;
    V_EMP_CODE        := :OLD.EMPLOYEE_CODE;
    V_DAY_OF_MONTH    := :OLD.DAY_OF_MONTH;
    V_CONFIGURE_CODE  := :OLD.CONFIGURE_CODE;
    V_YEAR_MONTH      := :OLD.YEAR_MONTH;
    V_DEPARTMENT_CODE := :OLD.DEPARTMENT_CODE;
    V_SCHEDULE_TYPE   := :OLD.SCHEDULING_TYPE;
  ELSE
  
    V_OPERATE_TYPE := 0;
    V_ID           := :NEW.ID;
  
    V_EMP_CODE        := :NEW.EMPLOYEE_CODE;
    V_DAY_OF_MONTH    := :NEW.DAY_OF_MONTH;
    V_CONFIGURE_CODE  := :NEW.CONFIGURE_CODE;
    V_YEAR_MONTH      := :NEW.YEAR_MONTH;
    V_DEPARTMENT_CODE := :NEW.DEPARTMENT_CODE;
    V_SCHEDULE_TYPE   := :NEW.SCHEDULING_TYPE;
  END IF;

  IF V_SCHEDULE_TYPE = 1 THEN
    INSERT INTO TT_TASK_COMPARE
      (ID,
       EMPLOYEE_CODE,
       DAY_OF_MONTH,
       SYNC_STATUS,
       CONFIGURE_CODE,
       OPERATION_TYPE,
       CREATED_TIME,
       UPDATED_TIME,
       DEPT_CODE,
       YEAR_MONTH)
    VALUES
      (SEQ_OSS_BASE.NEXTVAL,
       V_EMP_CODE,
       V_DAY_OF_MONTH,
       0,
       V_CONFIGURE_CODE,
       V_OPERATE_TYPE,
       SYSDATE,
       SYSDATE,
       V_DEPARTMENT_CODE,
       V_YEAR_MONTH);
  END IF;

  -- �ַ��Ǻ��ʱ�������
  insert into SYNC_TASK_FOR_SCHEDULED_REPORT
    (id, Employee_Code, YEAR_MONTH)
  values
    (seq_SCHEDULED_REPORT.Nextval, V_EMP_CODE, V_YEAR_MONTH);

EXCEPTION
  WHEN OTHERS THEN
    --�ع�����
    ROLLBACK;
END SYNC_TASK_FOR_REPORT;
/