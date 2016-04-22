-- add/modify columns 
alter table tt_driver_line_configure add month varchar2(20);
-- add comments to the columns 
comment on column tt_driver_line_configure.month
  is '配班月份  格式：2014-08';
