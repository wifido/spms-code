CREATE OR REPLACE FUNCTION GETS_TYPE_OF_EMP(POSITION_NAME IN VARCHAR,
                                            DUTY_SERIAL   IN VARCHAR,
                                            POSITION_ATTR IN VARCHAR)
  RETURN VARCHAR2 IS
  V_USER VARCHAR2(100);

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
                       '��������Ա') THEN
    V_USER := '3';
  elsif DUTY_SERIAL = '������������' THEN
    V_USER := '1';
  elsif POSITION_ATTR = 'һ��' THEN
    V_USER := '2';
    elsif  POSITION_NAME in ('����˾��',
'����˾��',
'˾���鳤',
'��������רԱ',
'������������',
'������������',
'����������',
'���������߼�����') THEN
    V_USER := '5';
  else
    V_USER := '0';
  END IF;
  RETURN(V_USER);
END GETS_TYPE_OF_EMP;
/
