alter table tm_driver_line add (mobile_network varchar2(1) default 0);

comment on column tm_driver_line.mobile_network is '是否机动:0是 1否';

update tm_driver_line line set line.mobile_network = '0';

commit;