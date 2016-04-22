-------------------------------------------------
-- Export file for user PUSHPN                 --
-- Created by sfit0505 on 2014/10/15, 10:09:29 --
-------------------------------------------------

spool tt_warehouse_emp_dept_r.log

alter table TM_PB_SCHEDULE_BASE_INFO add(IS_CROSS_DAY varchar2(1) null);
comment on column TM_PB_SCHEDULE_BASE_INFO.IS_CROSS_DAY is '开始时间--结束时间 是否为跨天  0为不跨天，1为夸天';


alter table TM_PB_SCHEDULE_BASE_INFO add(CLASS_TYPE varchar2(1) null);
comment on column TM_PB_SCHEDULE_BASE_INFO.CLASS_TYPE is '班别类型   1、运作班别   2、仓管班别';

spool off
