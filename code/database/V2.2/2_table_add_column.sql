alter table tt_op_sch_statis_report add(diurnal_count number,non_diurnal_count number);

comment on column tt_op_sch_statis_report.sys_date is 'ͬ��ʱ��';
comment on column tt_op_sch_statis_report.diurnal_count is 'ȫ�����ù�����';
comment on column tt_op_sch_statis_report.non_diurnal_count is '��ȫ�����ù�����';