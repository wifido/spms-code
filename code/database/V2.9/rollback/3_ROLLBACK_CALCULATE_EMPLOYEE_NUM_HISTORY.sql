-- 删除总部代码回滚脚本     
delete op_attendance_count_report t where t.dept_code = '001';

-- 删除经营本部代码回滚脚本        
delete op_attendance_count_report t
 where t.dept_code in
       (select dept.hq_code
          from op_attendance_count_report t, tm_department dept
         where t.dept_code = dept.dept_code
           and dept.delete_flg = 0
           and t.count_date >= date '2015-07-01'
           and dept.Type_Level = 2
         group by dept.hq_code, t.day_of_month);

-- 删除区部代码回滚脚本
delete op_attendance_count_report t
 where t.dept_code in
       (select decode(dept.area_code,
                      null,
                      max(dept.dept_code),
                      dept.area_code)
          from op_attendance_count_report t, tm_department dept
         where t.dept_code = dept.dept_code
           and dept.delete_flg = 0
           and t.count_date >= date
         '2015-07-01'
           and dept.type_code in ('ZZC04-YJ',
                                  'ZZC04-ERJ',
                                  'ZZC05-SJ',
                                  'HHZ05',
                                  'QB03-YSZX',
                                  'FB05-YSZX',
                                  'FB04-JSZX',
                                  'GWB04')
         group by dept.area_code, t.day_of_month);

commit;
