alter table tt_op_sch_statis_report add(sch_diurnal_count number,sch_non_diurnal_count number);

comment on column tt_op_sch_statis_report.sch_diurnal_count is '�Ű�ȷ��ȫ������';
comment on column tt_op_sch_statis_report.sch_non_diurnal_count is '�Ű�ȷ�Ϸ�ȫ��������';
