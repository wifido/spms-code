create or replace view op_all_sche_confirm_data as
select t.ym,t.id,t.dept_id,t.emp_code,t.day1 shedule_code,t.ym||'-01' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day2,t.ym||'-02' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day3,t.ym||'-03' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day4,t.ym||'-04' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day5,t.ym||'-05' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day6,t.ym||'-06' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day7,t.ym||'-07' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day8,t.ym||'-08' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day9,t.ym||'-09' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day10,t.ym||'-10' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day11,t.ym||'-11' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day12,t.ym||'-12' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day13,t.ym||'-13' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day14,t.ym||'-14' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day15,t.ym||'-15' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day16,t.ym||'-16' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day17,t.ym||'-17' shedule_dt from tt_pb_shedule_by_month_log t

union
select t.ym,t.id,t.dept_id,t.emp_code,t.day18,t.ym||'-18' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day19,t.ym||'-19' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day20,t.ym||'-20' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day21,t.ym||'-21' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day22,t.ym||'-22' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day23,t.ym||'-23' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day24,t.ym||'-24' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day25,t.ym||'-25' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day26,t.ym||'-26' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day27,t.ym||'-27' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day28,t.ym||'-28' shedule_dt from tt_pb_shedule_by_month_log t
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day29,t.ym||'-29' shedule_dt from tt_pb_shedule_by_month_log t WHERE t.day29 IS NOT NULL
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day30,t.ym||'-30' shedule_dt from tt_pb_shedule_by_month_log t WHERE t.day30 IS NOT NULL
union
select t.ym,t.id,t.dept_id,t.emp_code,t.day31,t.ym||'-31' shedule_dt from tt_pb_shedule_by_month_log t WHERE t.day31 IS NOT NULL;
