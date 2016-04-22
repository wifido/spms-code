alter table TM_PB_GROUP_INFO add ENABLE_DT date;
comment on column TM_PB_GROUP_INFO.ENABLE_DT is '生效日期';
alter table TT_PB_PROCESS_BY_MONTH add COMMIT_STATUS number;
comment on column TT_PB_PROCESS_BY_MONTH.COMMIT_STATUS  is '提交确认标志(1-已确认 0-未确认)';
alter table TT_PB_SHEDULE_BY_MONTH add COMMIT_STATUS number;
comment on column TT_PB_SHEDULE_BY_MONTH.COMMIT_STATUS  is '提交确认标志(1-已确认 0-未确认)';
alter table TT_PB_SHEDULE_BY_MONTH add SYNCHRO_STATUS number;
comment on column TT_PB_SHEDULE_BY_MONTH.SYNCHRO_STATUS  is '同步状态 1表示已同步';

alter table TT_PB_SHEDULE_BY_DAY add SYNCHRO_STATUS number;
comment on column TT_PB_SHEDULE_BY_DAY.SYNCHRO_STATUS  is '同步状态 1表示已同步';

alter table TT_SCHEDULE_DAILY add SYNCHRO_STATUS number;
comment on column TT_SCHEDULE_DAILY.SYNCHRO_STATUS  is '同步状态 1表示已同步';

alter table TM_PB_PROCESS_INFO modify "ESTIMATE_VALUE" NUMBER(4,2);
alter table TM_PB_PROCESS_INFO modify "INTENSITY_VALUE" NUMBER(4,2);
alter table TM_PB_PROCESS_INFO modify "SKILL_VALUE" NUMBER(4,2);
alter table TM_PB_PROCESS_INFO modify "DIFFICULTY_VALUE" NUMBER(4,2);
alter table TM_PB_PROCESS_INFO modify "DIFFICULTY_MODIFY_VALUE" NUMBER(4,2);








