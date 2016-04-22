CREATE OR REPLACE FUNCTION GETS_TYPE_OF_PMP(DEPT_TYPE  IN VARCHAR,
                                            GC_POSATTR  IN VARCHAR,
                                            GC_POSITION in varchar)
  RETURN VARCHAR2 IS
  V_USER VARCHAR2(100);
  IS_EXIST VARCHAR2(5);

  /**
  *岗位类型（1-运作员、2-收派员、3-仓管）
  */
BEGIN
 IF GC_POSITION IN ('仓管员',
                       '仓管组长',
                       '代理点部主管',
                       '代理分部经理',
                       '点部主管',
                       '分部经理',
                       '分部副经理',
                       '分部经理助理',
                       '运作管理主管',
                       '代理运作管理主管',
                       '质量管理员',
                       '叉车司机',
                       '理货员',
                       '理货组长',
                       '包裹处理员',
                       '运作组长',
                       '运力操作组长',
                       '关务理货员',
                       '运作员',
                       '补码员',
                       '补码组长',
                       '安检员',
                       '运力操作员',
                       '集配站经理',
                       '集配站主管',
                       '集配站营业员',
                       '分拣员') THEN
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
  elsif GC_POSATTR = '0' THEN
    V_USER := '2';
  elsif GC_POSITION in ('司机组长', '干线司机', '运作司机') then
    V_USER := '5';
  else
    V_USER := '0';
  END IF;
  RETURN(V_USER);
END GETS_TYPE_OF_PMP;
/