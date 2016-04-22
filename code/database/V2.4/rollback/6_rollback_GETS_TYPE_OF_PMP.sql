CREATE OR REPLACE FUNCTION GETS_TYPE_OF_PMP(BUS_MODE    IN VARCHAR,
                                            GC_POSATTR  IN VARCHAR,
                                            GC_POSITION in varchar)
  RETURN VARCHAR2 IS
  V_USER VARCHAR2(100);

  /**
  *岗位类型（1-运作员、2-收派员、3-仓管）
  */
BEGIN

  IF BUS_MODE = '运作伙伴计划' AND GC_POSATTR = '1' THEN
    V_USER := '1';
  elsif GC_POSITION in ('仓管员',
                        '仓管组长',
                        '代理点部主管',
                        '代理分部经理',
                        '点部主管',
                        '分部经理',
                        '分部经理助理',
                        '运作管理主管',
                        '代理运作管理主管') AND GC_POSATTR = '1' THEN
    V_USER := '3';
  elsif GC_POSATTR = '0' THEN
    V_USER := '2';
  elsif GC_POSITION in ('司机组长', '干线司机', '运作司机') AND GC_POSATTR = '1' then
    V_USER := '5';
  else
    V_USER := '0';
  END IF;
  RETURN(V_USER);
END GETS_TYPE_OF_PMP;
