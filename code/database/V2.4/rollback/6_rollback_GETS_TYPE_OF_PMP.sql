CREATE OR REPLACE FUNCTION GETS_TYPE_OF_PMP(BUS_MODE    IN VARCHAR,
                                            GC_POSATTR  IN VARCHAR,
                                            GC_POSITION in varchar)
  RETURN VARCHAR2 IS
  V_USER VARCHAR2(100);

  /**
  *��λ���ͣ�1-����Ա��2-����Ա��3-�ֹܣ�
  */
BEGIN

  IF BUS_MODE = '�������ƻ�' AND GC_POSATTR = '1' THEN
    V_USER := '1';
  elsif GC_POSITION in ('�ֹ�Ա',
                        '�ֹ��鳤',
                        '����㲿����',
                        '����ֲ�����',
                        '�㲿����',
                        '�ֲ�����',
                        '�ֲ���������',
                        '������������',
                        '����������������') AND GC_POSATTR = '1' THEN
    V_USER := '3';
  elsif GC_POSATTR = '0' THEN
    V_USER := '2';
  elsif GC_POSITION in ('˾���鳤', '����˾��', '����˾��') AND GC_POSATTR = '1' then
    V_USER := '5';
  else
    V_USER := '0';
  END IF;
  RETURN(V_USER);
END GETS_TYPE_OF_PMP;
