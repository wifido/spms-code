CREATE OR REPLACE FUNCTION GETS_TYPE_OF_PMP(DEPT_TYPE   IN VARCHAR,
                                            GC_POSATTR  IN VARCHAR,
                                            GC_POSITION IN VARCHAR)
  RETURN VARCHAR2 IS
  V_USER         VARCHAR2(100);
  IS_EXIST       VARCHAR2(5);
  IS_EXIST_POST  VARCHAR2(5);
  IS_DRIVER_POST VARCHAR2(5);

  /**
  *岗位类型（1-运作员、2-收派员、3-仓管）
  */
BEGIN

  SELECT INSTR((SELECT KEY_VALUE
                 FROM TL_SPMS_SYS_CONFIG C
                WHERE C.KEY_NAME = 'OPERATION_DEPTCODE_FILTER'),
               DEPT_TYPE,
               1,
               1)
    INTO IS_EXIST
    FROM DUAL;

  SELECT INSTR((SELECT KEY_VALUE
                 FROM TL_SPMS_SYS_CONFIG C
                WHERE C.KEY_NAME = 'SAP_PRO_WAREHOUSE_POST'),
               GC_POSITION,
               1,
               1)
    INTO IS_EXIST_POST
    FROM DUAL;

  SELECT INSTR((SELECT KEY_VALUE
                 FROM TL_SPMS_SYS_CONFIG C
                WHERE C.KEY_NAME = 'PMP_DRIVER_POST'),
               GC_POSITION,
               1,
               1)
    INTO IS_DRIVER_POST
    FROM DUAL;

  IF IS_EXIST_POST <> 0 THEN
    IF IS_EXIST <> 0 THEN
      V_USER := '1';
    ELSE
      V_USER := '3';
    END IF;
  ELSIF GC_POSATTR = '0' THEN
    V_USER := '2';
  ELSIF IS_DRIVER_POST <> 0 THEN
    V_USER := '5';
  ELSE
    V_USER := '0';
  END IF;
  RETURN(V_USER);
END GETS_TYPE_OF_PMP;
/