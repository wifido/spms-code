alter   table   tt_schedule_daily   add(SCHEDULING_CODE   varchar2(20) );

comment on column TT_SCHEDULE_DAILY.SCHEDULING_CODE
  is '仓管班别关联字段 ';