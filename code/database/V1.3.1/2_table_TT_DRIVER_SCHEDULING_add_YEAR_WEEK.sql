alter table TT_DRIVER_SCHEDULING add YEAR_WEEK VARCHAR2(10);
-- Add comments to the columns 
comment on column TT_DRIVER_SCHEDULING.YEAR_WEEK
  is '年周 格式（2014-01）';