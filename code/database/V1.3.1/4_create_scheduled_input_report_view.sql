-- 创建网点视图
create or replace view op_dept as
select dept.dept_code,dept.dept_id,dept.area_code,dept.type_code,dept.hq_code
  from tm_department dept
 where
 regexp_like(dept.dept_code, '^[0-9]{3,4}[WRX]');

 
-- 已分组人数
create or replace view op_emp_has_group_num as
select d.DEPT_CODE, nvl(a.counts, 0) counts
    from op_dept d,
         (select d.DEPT_CODE, count(*) counts
            from op_dept d, tm_oss_employee e,tm_pb_group_info g
           where d.DEPT_ID = e.dept_id
             and e.group_id=g.group_id
			 and e.emp_post_type = '1'
             and e.emp_code is not null
             and e.group_id is not null
             ---and (length(e.emp_code) = 6 or length(e.emp_code) = 9)
           group by d.DEPT_CODE) a
   where d.DEPT_CODE = a.DEPT_CODE(+);

-- 小组数
create or replace view op_group_num as
select d.DEPT_CODE, nvl(a.counts, 0) counts
    from op_dept d,
         (select d.DEPT_CODE, count(distinct g.group_code) counts
            from op_dept d, tm_pb_group_info g
           where d.DEPT_ID = g.dept_id
           group by d.DEPT_CODE) a
   where d.DEPT_CODE = a.DEPT_CODE(+);
   
-- 内部人员数量
create or replace view op_inner_emp_num as
select d.DEPT_CODE, nvl(a.counts, 0) counts
  from op_dept d,
       (select d.DEPT_CODE, count(e.emp_code) counts
          from op_dept d, tm_oss_employee e
         where d.DEPT_ID = e.dept_id
           and e.emp_post_type = '1'
           and (e.dimission_dt is null or e.dimission_dt  >  sysdate)
           and e.work_type not in (6,8,9)
         group by d.DEPT_CODE) a
 where d.DEPT_CODE = a.DEPT_CODE(+);

-- 外包人员数量
create or replace view op_outer_emp_num as
select d.DEPT_CODE, nvl(a.counts, 0) counts
  from op_dept d,
       (select d.DEPT_CODE, count(e.emp_code) counts
          from op_dept d, tm_oss_employee e
         where d.DEPT_ID = e.dept_id
       and e.emp_post_type = '1'
           and e.work_type = 6
           and (e.dimission_dt is null or e.dimission_dt  >  sysdate)
           and e.emp_post_type = '1'
         group by d.DEPT_CODE) a
where d.DEPT_CODE = a.DEPT_CODE(+);

-- 班别数量
create or replace view op_shedule_code_num as
select d.DEPT_CODE, nvl(a.counts, 0) counts
     from op_dept d,
          (select d.DEPT_CODE, count(distinct c.schedule_code) counts
             from op_dept d, tm_pb_schedule_base_info c
            where d.DEPT_ID = c.dept_id
            group by d.DEPT_CODE) a
    where d.DEPT_CODE = a.DEPT_CODE(+);
	
-- 工序数量
create or replace view op_process_code_num as
select d.DEPT_CODE, nvl(a.counts, 0) counts
     from op_dept d,
          (select d.DEPT_CODE, count(distinct c.process_code) counts
             from op_dept d, tm_pb_process_info c
            where d.DEPT_ID = c.dept_id
            and c.status='1'
            group by d.DEPT_CODE) a
    where d.DEPT_CODE = a.DEPT_CODE(+);
	
-- 运作排班确认内部人员数量
create or replace view op_sche_confirm_inner_emp_num as
select dept.dept_code,record.ym,count(record.emp_code) sche_confirm_inner_emp_num from op_dept dept,
(select dept.dept_code, record.ym, emp.emp_code
  from op_dept dept,
       tt_pb_shedule_by_month_log record,
       (select emp.emp_code, emp.dept_id, emp.dimission_dt
          from tm_oss_employee emp
         where emp.emp_post_type = 1
           and emp.work_type not in (6,8,9)
           and (emp.dimission_dt is null or emp.dimission_dt > sysdate)) emp
 where dept.dept_id = record.dept_id
   and dept.dept_id = emp.dept_id
   and record.dept_id = emp.dept_id
   and record.emp_code = emp.emp_code
 group by dept.dept_code, record.ym, emp.emp_code) record
 where dept.dept_code = record.dept_code(+)
 group by  dept.dept_code,record.ym;
 
-- 运作排班确认外包人员数量
create or replace view op_sche_confirm_outer_emp_num as
select dept.dept_code,record.ym,count(record.emp_code) sche_confirm_outer_emp_num from op_dept dept,
(select dept.dept_code,record.ym,emp.emp_code
from op_dept dept,
     tt_pb_shedule_by_month_log record,
     (select emp.emp_code, emp.dept_id,emp.dimission_dt
        from tm_oss_employee emp
       where emp.emp_post_type = 1
         and emp.work_type = 6 
		 and (emp.dimission_dt is null or emp.dimission_dt  >  sysdate)) emp
where dept.dept_id = record.dept_id
 and dept.dept_id = emp.dept_id
 and record.dept_id = emp.dept_id
 and record.emp_code = emp.emp_code
group by dept.dept_code, record.ym, emp.emp_code) record
where dept.dept_code = record.dept_code(+)
group by dept.dept_code,record.ym;

-- 运作工序确认内部人员数量
create or replace view op_pro_confirm_inner_emp_num as
select dept.dept_code, record.ym, count(record.emp_code) pro_inner_emp_comfirm_num
  from op_dept dept,(select dept.dept_code, process.ym, emp.emp_code
          from op_dept dept,
               tt_pb_process_by_month_log process,
               (select emp.emp_code, emp.dept_id, emp.dimission_dt
                  from tm_oss_employee emp
                 where emp.emp_post_type = 1
                   and emp.work_type not in (6,8,9)
                   and (emp.dimission_dt is null or
                       emp.dimission_dt > sysdate)) emp
         where dept.dept_id = process.dept_id
           and process.dept_id = emp.dept_id
           and dept.dept_id = emp.dept_id
           and process.emp_code = emp.emp_code
         group by dept.dept_code, process.ym, emp.emp_code) record
         where dept.dept_code = record.dept_code(+)
 group by dept.dept_code, record.ym;
 
-- 运作工序确认外包人员数量
create or replace view op_pro_comfirm_outer_emp_num as
select dept.dept_code, record.ym, count(record.emp_code) pro_outer_emp_comfirm_num
  from op_dept dept,(select dept.dept_code, process.ym, emp.emp_code
          from op_dept dept,
               tt_pb_process_by_month_log process,
               (select emp.emp_code, emp.dept_id, emp.dimission_dt
                  from tm_oss_employee emp
                 where emp.emp_post_type = 1
                   and emp.work_type = 6
                   and (emp.dimission_dt is null or
                       emp.dimission_dt > sysdate)) emp
         where dept.dept_id = process.dept_id
           and process.dept_id = emp.dept_id
           and dept.dept_id = emp.dept_id
           and process.emp_code = emp.emp_code
         group by dept.dept_code, process.ym, emp.emp_code) record
         where dept.dept_code = record.dept_code(+)
 group by dept.dept_code, record.ym;
 
-- 运作排班确认内部人员数量 and -- 运作工序确认内部人员数量
create or replace view sche_and_pro_confirm_inner_emp as
select n1.dept_code,
                n1.ym,
                n1.sche_confirm_inner_emp_num,
                n2.pro_inner_emp_comfirm_num
  from op_sche_confirm_inner_emp_num n1, op_pro_confirm_inner_emp_num n2
 where n1.dept_code = n2.dept_code(+)
   and n1.ym = n2.ym(+);
               
-- 运作排班确认外包人员数量 and  运作工序确认外包人员数量              
create or replace view sche_and_pro_confirm_outer_emp as
select n1.dept_code,
                n1.ym,
                n1.sche_confirm_outer_emp_num,
                n2.pro_outer_emp_comfirm_num
  from op_sche_confirm_outer_emp_num n1, op_pro_comfirm_outer_emp_num n2
 where n1.dept_code = n2.dept_code(+)
   and n1.ym = n2.ym(+)
 
 
