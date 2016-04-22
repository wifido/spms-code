alter table tt_op_sch_statis_report add(sch_diurnal_count number,sch_non_diurnal_count number);

comment on column tt_op_sch_statis_report.sch_diurnal_count is '排班确认全日总数';
comment on column tt_op_sch_statis_report.sch_non_diurnal_count is '排班确认非全日制总数';
