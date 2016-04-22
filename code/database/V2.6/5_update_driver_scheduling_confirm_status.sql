update tt_driver_scheduling sch set sch.confirm_status = 0 where sch.confirm_status is null;
commit;