alter table TM_PB_GROUP_INFO add ENABLE_DT date;
comment on column TM_PB_GROUP_INFO.ENABLE_DT is '��Ч����';
alter table TT_PB_PROCESS_BY_MONTH add COMMIT_STATUS number;
comment on column TT_PB_PROCESS_BY_MONTH.COMMIT_STATUS  is '�ύȷ�ϱ�־(1-��ȷ�� 0-δȷ��)';
alter table TT_PB_SHEDULE_BY_MONTH add COMMIT_STATUS number;
comment on column TT_PB_SHEDULE_BY_MONTH.COMMIT_STATUS  is '�ύȷ�ϱ�־(1-��ȷ�� 0-δȷ��)';
alter table TT_PB_SHEDULE_BY_MONTH add SYNCHRO_STATUS number;
comment on column TT_PB_SHEDULE_BY_MONTH.SYNCHRO_STATUS  is 'ͬ��״̬ 1��ʾ��ͬ��';

alter table TT_PB_SHEDULE_BY_DAY add SYNCHRO_STATUS number;
comment on column TT_PB_SHEDULE_BY_DAY.SYNCHRO_STATUS  is 'ͬ��״̬ 1��ʾ��ͬ��';

alter table TT_SCHEDULE_DAILY add SYNCHRO_STATUS number;
comment on column TT_SCHEDULE_DAILY.SYNCHRO_STATUS  is 'ͬ��״̬ 1��ʾ��ͬ��';

alter table TM_PB_PROCESS_INFO modify "ESTIMATE_VALUE" NUMBER(4,2);
alter table TM_PB_PROCESS_INFO modify "INTENSITY_VALUE" NUMBER(4,2);
alter table TM_PB_PROCESS_INFO modify "SKILL_VALUE" NUMBER(4,2);
alter table TM_PB_PROCESS_INFO modify "DIFFICULTY_VALUE" NUMBER(4,2);
alter table TM_PB_PROCESS_INFO modify "DIFFICULTY_MODIFY_VALUE" NUMBER(4,2);








