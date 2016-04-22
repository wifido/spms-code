drop table TI_TRANSFER_BATCH_LIST;

delete etlmgr.etl_incr_param p where p.WORKFLOW = 'WF_M_TI_TRANSFER_BATCH_LIST';

commit;