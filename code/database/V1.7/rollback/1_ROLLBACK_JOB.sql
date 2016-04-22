drop table TT_CDP_EMPNUM_SCHEDULINGNUM;

drop table TT_CDP_EMPNUM_SCHEDUL_HIS;

drop PROCEDURE  SPMS_TO_CDP_BY_WAREHOUSE;

drop PROCEDURE  SPMS_TO_CDP_BY_dispatch;

drop PROCEDURE  SPMS_TO_CDP_EMP_COUNT;



alter table TI_TCAS_SPMS_SCHEDULE drop column KQ_XSS;
alter table TI_TCAS_SPMS_SCHEDULE drop column STDAZ;
alter table TI_TCAS_SPMS_SCHEDULE drop column ARBST;
alter table TI_TCAS_SPMS_SCHEDULE drop column PAPER;


drop table TI_SCH_EMPLOYEECLASS_PLAIN;