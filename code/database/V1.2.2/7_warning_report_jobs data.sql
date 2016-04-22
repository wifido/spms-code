DECLARE job2011 NUMBER;

BEGIN
  DBMS_JOB.SUBMIT

    (job2011
    , 'DRIVER_WARNNING_SYNC_PROCESS;'   -- 执行脚本 
    ,sysdate
	,'SYSDATE + 1/24'  -- 1小时执行一次
    );
END;
/
commit;



