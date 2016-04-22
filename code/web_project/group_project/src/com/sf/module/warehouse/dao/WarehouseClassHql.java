package com.sf.module.warehouse.dao;

public class WarehouseClassHql {

	public static final String QUERY_CLASS_INFORMATION = "select bi.schedule_id,"
			+ " dept.area_code, "
			+ " dept.dept_code, "
			+ " bi.schedule_code, "
			+ " bi.start1_time, "
			+ " bi.end1_time, "
			+ " decode(bi.is_cross_day, "
			+ " 1,"
			+ " round(to_number(TO_DATE('2012-02-20 ' || bi.end1_time, "
			+ " 'yyyy-mm-dd hh24:mi') -"
			+ " TO_DATE('2012-02-19 ' || bi.start1_time, "
			+ " 'yyyy-mm-dd hh24:mi')) * 24, 2),"
			+ " round(to_number(TO_DATE('2012-02-20 ' || bi.end1_time, "
			+ " 'yyyy-mm-dd hh24:mi') - "
			+ " TO_DATE('2012-02-20 ' || bi.start1_time, "
			+ " 'yyyy-mm-dd hh24:mi')) * 24,2)) timeLength, "
			+ " to_char(bi.enable_dt,'yyyy-mm-dd') enable_dt, "
			+ " to_char(bi.disable_dt,'yyyy-mm-dd') disable_dt, "
			+ " decode(bi.is_cross_day,'1','是','否') is_cross_day "
			+ " from tm_pb_schedule_base_info bi, tm_department dept "
			+ " where bi.dept_id = dept.dept_id " + " AND bi.class_type = '2'";

	public static final String QUERY_CLASS_INFORMATION_COUNT = "select count(*) totalSize "
			+ " from tm_pb_schedule_base_info bi, tm_department dept "
			+ " where bi.dept_id = dept.dept_id " + " AND bi.class_type = '2'";

	public static final String ADD_CLASS_INFO_SQL = " insert into tm_pb_schedule_base_info \n "
			+ "   (SCHEDULE_ID, \n "
			+ "    SCHEDULE_CODE, \n "
			+ "    SCHEDULE_NAME, \n "
			+ "    DEPT_ID, \n "
			+ "    START1_TIME, \n "
			+ "    END1_TIME, \n "
			+ "    ENABLE_DT, \n "
			+ "    DISABLE_DT, \n "
			+ "    CREATE_TM, \n "
			+ "    MODIFIED_TM, \n "
			+ "    CREATE_EMP_CODE, \n "
			+ "    MODIFIED_EMP_CODE, \n "
			+ "    IS_CROSS_DAY, \n "
			+ "    CLASS_TYPE) \n "
			+ " values \n "
			+ "   (seq_px_base.nextval, \n "
			+ "    :schedule_code, \n "
			+ "    :schedule_name, \n "
			+ "    :dept_id, \n "
			+ "    :start1_time, \n "
			+ "    :end1_time, \n "
			+ "    to_date(:enable_dt, 'yyyy-mm-dd'), \n "
			+ "    to_date(:disable_dt, 'yyyy-mm-dd'), \n "
			+ "    sysdate, \n "
			+ "    null, \n "
			+ "    :create_emp_code, \n "
			+ "    null, \n "
			+ "    :is_cross_day, \n " + "    '2')  \n ";

	public static final String UPDATE_CLASS_INFO_SQL = " update tm_pb_schedule_base_info t\n"
			+ "    set t.start1_time       = :start1_time,\n"
			+ "        t.end1_time         = :end1_time,\n"
			+ "        t.disable_dt        = to_date(:disable_dt, 'yyyy-mm-dd'),\n"
			+ "        t.modified_tm       = sysdate,\n"
			+ "        t.is_cross_day      = :is_cross_day,\n"
			+ "        t.modified_emp_code = :modified_emp_code\n"
			+ "  where t.schedule_id = :schedule_id\n";

	public static final String VALID_CLASS_NAME_EXIST_SQL = "select t.schedule_code from tm_pb_schedule_base_info t  where "
			+ "t.schedule_code =:schedule_code  and t.dept_id = :dept_id and t.class_type='2' ";

	public static final String DELETE_CLASS_SQL = "delete tm_pb_schedule_base_info t where 1 = 1";

	public static final String EXIST_REFERENCE = " select distinct t.scheduling_code \n "
			+"   from tt_schedule_daily t, \n "
			+"        (select tm_pb_schedule_base_info.schedule_code, d.dept_code \n "
			+"           from tm_pb_schedule_base_info tm_pb_schedule_base_info, \n "
			+"                tm_department            d \n "
			+"          where tm_pb_schedule_base_info.schedule_id in (${parameter}) \n "
			+"            and tm_pb_schedule_base_info.class_type = '2' \n "
			+"            and tm_pb_schedule_base_info.dept_id = d.dept_id) c \n "
			+"  where t.scheduling_code = c.schedule_code \n "
			+"    and t.department_code = c.dept_code \n ";

	public static final String QUERY_CLASS_BY_DEPARTMENT_ID_SQL = " select t.* from tm_pb_schedule_base_info t" +
			"  where t.dept_id=:dept_id and t.class_type='2' and t.disable_dt > sysdate order by t.schedule_code";

}
