CREATE OR REPLACE FUNCTION GETS_TYPE_OF_EMP(POSITION_NAME IN VARCHAR,
                                            DUTY_SERIAL   IN VARCHAR,
                                            POSITION_ATTR IN VARCHAR)
  RETURN VARCHAR2 IS
  V_USER VARCHAR2(100);

  /**
  *岗位类型（1-运作员、2-收派员、3-仓管）
  */
BEGIN

  IF POSITION_NAME IN ('仓管员',
                       '仓管组长',
                       '代理点部主管',
                       '代理分部经理',
                       '点部主管',
                       '分部经理',
                       '分部副经理',
                       '分部经理助理',
                       '运作管理主管',
                       '代理运作管理主管',
                       '质量管理员') THEN
    V_USER := '3';
  elsif DUTY_SERIAL = '包裹处理序列' THEN
    V_USER := '1';
  elsif POSITION_ATTR = '一线' THEN
    V_USER := '2';
    elsif  POSITION_NAME in ('运作司机',
'干线司机',
'司机组长',
'车辆管理专员',
'车辆管理主管',
'车辆管理副经理',
'车辆管理经理',
'车辆管理副高级经理') THEN
    V_USER := '5';
  else
    V_USER := '0';
  END IF;
  RETURN(V_USER);
END GETS_TYPE_OF_EMP;
/
