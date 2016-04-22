

DECLARE job2010 NUMBER;

BEGIN

  DBMS_JOB.SUBMIT

    (  job2010

    , 'SCHEDULING_SAP_SYNCHRONIZATION;'

    ,sysdate,'sysdate+10/24*60'
	
    );

END;

/

commit;



