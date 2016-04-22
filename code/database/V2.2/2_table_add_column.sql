alter table tt_op_sch_statis_report add(diurnal_count number,non_diurnal_count number);

comment on column tt_op_sch_statis_report.sys_date is '同步时间';
comment on column tt_op_sch_statis_report.diurnal_count is '全日制用工总数';
comment on column tt_op_sch_statis_report.non_diurnal_count is '非全日制用工总数';