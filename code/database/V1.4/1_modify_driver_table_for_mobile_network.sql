alter table tm_driver_line add (mobile_network varchar2(1) default 0);

comment on column tm_driver_line.mobile_network is '�Ƿ����:0�� 1��';

update tm_driver_line line set line.mobile_network = '0';

commit;