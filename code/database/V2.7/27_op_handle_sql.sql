create table tt_Original_network_bak as 
select t.* from tt_pb_shedule_by_day t , tm_department d,
tm_oss_employee e 
where t.dept_id = d.dept_id
and t.emp_code = e.emp_code
and d.dept_code in ('P010XA',
'P411WA',
'P020WA',
'P020XA',
'P451WA',
'P571WA',
'P573XA',
'P755WA',
'P311WA',
'P022WA',
'P510WLA',
'P420WA')
and e.date_from >= date'2015-09-01'
and t.shedule_dt >= e.date_from;

create table tt_new_network_bak as 
select t.* from tt_pb_shedule_by_day t ,
tm_department d,
tm_oss_employee e 
where  d.dept_id = t.dept_id
and e.emp_code = t.emp_code
and e.dept_id = t.dept_id
and d.dept_code in ('010XD',
'411WB',
'020WF',
'020XF',
'451WWB',
'571WWA',
'573XF',
'755WE',
'311WA',
'022WD',
'510WWL',
'420XA')
and e.date_from >= date'2015-09-01'
and t.shedule_dt < e.date_from
and t.shedule_dt >= date'2015-09-01';




 delete  tt_pb_shedule_by_day st where st.id  in (select t.id from tt_pb_shedule_by_day t , tm_department d,
tm_oss_employee e 
where t.dept_id = d.dept_id
and t.emp_code = e.emp_code
and d.dept_code in ('P010XA',
'P411WA',
'P020WA',
'P020XA',
'P451WA',
'P571WA',
'P573XA',
'P755WA',
'P311WA',
'P022WA',
'P510WLA',
'P420WA')
and e.date_from >= date'2015-09-01'
and t.shedule_dt >= e.date_from);
commit;

 delete  tt_pb_shedule_by_day st where st.id  in (select t.* from tt_pb_shedule_by_day t ,
tm_department d,
tm_oss_employee e 
where  d.dept_id = t.dept_id
and e.emp_code = t.emp_code
and e.dept_id = t.dept_id
and d.dept_code in ('010XD',
'411WB',
'020WF',
'020XF',
'451WWB',
'571WWA',
'573XF',
'755WE',
'311WA',
'022WD',
'510WWL',
'420XA')
and e.date_from >= date'2015-09-01'
and t.shedule_dt < e.date_from
and t.shedule_dt >= date'2015-09-01');
commit;