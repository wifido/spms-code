delete  FROM ts_module
where module_id = (
SELECT m1.module_id
      FROM ts_module m1
     WHERE m1.module_code = 'historyMonthModifySchedule');
     commit;