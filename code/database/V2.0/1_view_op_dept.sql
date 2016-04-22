create or replace view op_dept as
select dept.dept_code,
       dept.dept_id,
       dept.area_code,
       dept.type_code,
       dept.hq_code
  from tm_department dept
 where dept.type_code in ('ZZC04-YJ',
                          'ZZC04-ERJ',
                          'ZZC05-SJ',
                          'HHZ05',
                          'QB03-YSZX',
                          'FB05-YSZX',
                          'FB04-JSZX');
