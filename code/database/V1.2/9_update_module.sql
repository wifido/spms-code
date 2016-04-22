
UPDATE TS_MODULE M
   SET M.MODULE_NAME = '一线排班', M.MODULE_DESC = '一线排班'
 WHERE M.MODULE_ID IN (SELECT M1.MODULE_ID
                        FROM TS_MODULE M1
                        JOIN TS_MODULE M2 ON M1.PARENT_ID = M2.MODULE_ID
                       WHERE M1.MODULE_CODE = 'initAction'
                         AND M2.MODULE_CODE = 'oss');
COMMIT;