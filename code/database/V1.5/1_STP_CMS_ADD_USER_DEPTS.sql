CREATE OR REPLACE PROCEDURE STP_CMS_ADD_ACCR_DEPTS AS
  --*************************************************************
  -- AUTHOR  : KEDIYU
  -- CREATED : 2015-01-05
  -- PURPOSE : 自动根据权限钥匙授权记录，设置继承子网点权限
  --
  -- PARAMETER:
  -- NAME             TYPE            DESC
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************
  NUMVALUE NUMBER;
  --1.定义执行序号
  L_CALL_NO NUMBER;
  
  CURSOR CUR IS
    SELECT ACCR.USER_ID, ACCR.DEPT_ID
      FROM TS_ACCREDIT_DEPT ACCR, TM_DEPARTMENT DEPT
     WHERE DEPT.DEPT_ID = ACCR.DEPT_ID
       AND EXISTS
    --存在子节点
     (SELECT PARENTDEPT.DEPT_ID
              FROM TM_DEPARTMENT PARENTDEPT
             WHERE PARENTDEPT.PARENT_DEPT_CODE = DEPT.DEPT_CODE);

BEGIN
 --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'STP_CMS_ADD_ACCR_DEPTS',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);
  
  --遍利TS_ACCREDIT_DEPT表
  FOR CUR_RESULT IN CUR LOOP
    BEGIN
      SELECT COUNT(1)
        INTO NUMVALUE
        FROM (SELECT T.DEPT_ID
                FROM TM_DEPARTMENT T
              CONNECT BY PRIOR T.DEPT_CODE = T.PARENT_DEPT_CODE
               START WITH T.DEPT_ID = CUR_RESULT.DEPT_ID) ALLSUBDEPT
       WHERE NOT EXISTS (SELECT E.DEPT_ID
                FROM TS_ACCREDIT_DEPT E
               WHERE E.USER_ID = CUR_RESULT.USER_ID
                 AND E.DEPT_ID = ALLSUBDEPT.DEPT_ID);
      IF NUMVALUE > 0 THEN
        BEGIN

          INSERT INTO TS_ACCREDIT_DEPT
            (USER_ID, DEPT_ID)
            SELECT CUR_RESULT.USER_ID, ALLSUBDEPT.DEPT_ID
              FROM (SELECT T.DEPT_ID
                      FROM TM_DEPARTMENT T
                    CONNECT BY PRIOR T.DEPT_CODE = T.PARENT_DEPT_CODE
                     START WITH T.DEPT_ID = CUR_RESULT.DEPT_ID) ALLSUBDEPT
             WHERE NOT EXISTS (SELECT E.DEPT_ID
                      FROM TS_ACCREDIT_DEPT E
                     WHERE E.USER_ID = CUR_RESULT.USER_ID
                       AND E.DEPT_ID = ALLSUBDEPT.DEPT_ID);
          COMMIT;
        EXCEPTION
          WHEN OTHERS THEN
            ROLLBACK;

               --记录错误日志
               PKG_OSS_COMM.STP_RUNNING_LOG('',
                                    'STP_CMS_ADD_ACCR_DEPTS',
                                    SYSDATE,
                                    SQLCODE,
                                    SQLERRM,
                                    'ERROR',
                                    0,
                                    L_CALL_NO);
            COMMIT;
        END;
      END IF;
    END;
  END LOOP;

  --记录结束日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                                  'STP_CMS_ADD_ACCR_DEPTS',
                                  SYSDATE,
                                  NULL,
                                  NULL,
                                  'END',
                                  0,
                                  L_CALL_NO);
END;
/

CREATE OR REPLACE PROCEDURE STP_CMS_ADD_USER_DEPTS AS
  --*************************************************************
  -- AUTHOR  : KEDIYU
  -- CREATED : 2015-01-05
  -- PURPOSE : 自动根据用户网点权限记录，设置继承子网点权限
  --
  -- PARAMETER:
  -- NAME             TYPE            DESC
  --
  -- MODIFY HISTORY
  -- PERSON                    DATE                        COMMENTS
  -- -------------------------------------------------------------
  --
  --*************************************************************
  NUMVALUE NUMBER;
 --1.定义执行序号
  L_CALL_NO NUMBER;
  
  CURSOR CUR IS
    SELECT USER_DEPT.USER_ID, USER_DEPT.DEPT_ID
      FROM TS_USER_DEPT USER_DEPT, TM_DEPARTMENT DEPT
     WHERE DEPT.DEPT_ID = USER_DEPT.DEPT_ID
       AND USER_DEPT.INHERITED_FLG = 1
       AND EXISTS
    --存在子节点
     (SELECT PARENTDEPT.DEPT_ID
              FROM TM_DEPARTMENT PARENTDEPT
             WHERE PARENTDEPT.PARENT_DEPT_CODE = DEPT.DEPT_CODE);

BEGIN

--2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'STP_CMS_ADD_USER_DEPTS',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

  --遍利TS_USER_DEPT表
  FOR CUR_RESULT IN CUR LOOP
    BEGIN
      SELECT COUNT(1)
        INTO NUMVALUE
        FROM (SELECT T.DEPT_ID
                FROM TM_DEPARTMENT T
              CONNECT BY PRIOR T.DEPT_CODE = T.PARENT_DEPT_CODE
               START WITH T.DEPT_ID = CUR_RESULT.DEPT_ID) ALLSUBDEPT
       WHERE NOT EXISTS (SELECT E.DEPT_ID
                FROM TS_USER_DEPT E
               WHERE E.USER_ID = CUR_RESULT.USER_ID
                 AND E.DEPT_ID = ALLSUBDEPT.DEPT_ID);
      IF NUMVALUE > 0 THEN
        BEGIN

          INSERT INTO TS_USER_DEPT
            (USER_ID, DEPT_ID,INHERITED_FLG)
            SELECT CUR_RESULT.USER_ID, ALLSUBDEPT.DEPT_ID, 1
              FROM (SELECT T.DEPT_ID
                      FROM TM_DEPARTMENT T
                    CONNECT BY PRIOR T.DEPT_CODE = T.PARENT_DEPT_CODE
                     START WITH T.DEPT_ID = CUR_RESULT.DEPT_ID) ALLSUBDEPT
             WHERE NOT EXISTS (SELECT E.DEPT_ID
                      FROM TS_USER_DEPT E
                     WHERE E.USER_ID = CUR_RESULT.USER_ID
                       AND E.DEPT_ID = ALLSUBDEPT.DEPT_ID);
          COMMIT;
        EXCEPTION
          WHEN OTHERS THEN
            ROLLBACK;

               --记录错误日志
               PKG_OSS_COMM.STP_RUNNING_LOG('STP_CMS_ADD_USER_DEPTS',
                                    'STP_CMS_ADD_USER_DEPTS',
                                    SYSDATE,
                                    SQLCODE,
                                    SQLERRM,
                                    'ERROR',
                                    0,
                                   L_CALL_NO);
            COMMIT;
        END;
      END IF;
    END;
  END LOOP;

  --记录结束日志
  PKG_OSS_COMM.STP_RUNNING_LOG('STP_CMS_ADD_USER_DEPTS',
                                  'STP_CMS_ADD_USER_DEPTS',
                                  SYSDATE,
                                  NULL,
                                  NULL,
                                  'END',
                                  0,
                                  L_CALL_NO);

END;
/