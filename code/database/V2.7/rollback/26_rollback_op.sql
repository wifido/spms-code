 insert into tt_pb_shedule_by_day
select * from  tt_Original_network_bak ;



 insert into tt_pb_shedule_by_day
select * from  tt_new_network_bak ;

commit;

drop table tt_Original_network_bak;

drop table tt_new_network_bak;