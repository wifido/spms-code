CREATE OR REPLACE FUNCTION GETS_TYPE_OF_EMP2(POSITION_NAME IN VARCHAR,
                                             POSITION_ATTR IN VARCHAR,
                                             DEPT_TYPE     IN VARCHAR)
  RETURN VARCHAR2 IS
  V_USER   VARCHAR2(100);
  IS_EXIST VARCHAR2(5);

  /**
  *��λ���ͣ�1-����Ա��2-����Ա��3-�ֹܣ�
  */
BEGIN
  IF POSITION_NAME IN ('�ֹ�Ա',
                       '�ֹ��鳤',
                       '����㲿����',
                       '����ֲ�����',
                       '�㲿����',
                       '�ֲ�����',
                       '�ֲ�������',
                       '�ֲ���������',
                       '������������',
                       '����������������',
                       '��������Ա',
                       '�泵˾��',
                       '���Ա',
                       '����鳤',
                       '��������Ա',
                       '�����鳤',
                       '���������鳤',
                       '�������Ա',
                       '����Ա',
                       '����Ա',
                       '�����鳤',
                       '����Ա',
                       '��������Ա',
                       '����վ����',
                       '����վ����',
                       '����վӪҵԱ',
                       '�ּ�Ա') THEN
    SELECT INSTR((SELECT KEY_VALUE
                   FROM TL_SPMS_SYS_CONFIG C
                  WHERE C.KEY_NAME = 'OPERATION_DEPTCODE_FILTER'),
                 DEPT_TYPE,
                 1,
                 1)
      INTO IS_EXIST
      FROM DUAL;
    IF IS_EXIST <> 0 THEN
      V_USER := '1';
    ELSE
      V_USER := '3';
    END IF;
  ELSIF POSITION_ATTR = 'һ��' THEN
    V_USER := '2';
  ELSIF POSITION_NAME IN ('����˾��',
                          '����˾��',
                          '˾���鳤',
                          '��������רԱ',
                          '������������',
                          '������������',
                          '����������',
                          '���������߼�����') THEN
    V_USER := '5';
  ELSE
    V_USER := '0';
  END IF;
  RETURN(V_USER);
END GETS_TYPE_OF_EMP2;
/
