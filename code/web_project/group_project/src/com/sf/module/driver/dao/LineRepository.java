package com.sf.module.driver.dao;

import static com.sf.module.common.domain.sql.QueryStatement.select;
import static com.sf.module.common.domain.sql.SqlColumn.column;
import static com.sf.module.common.domain.sql.SqlColumn.dateColumn;
import static com.sf.module.common.domain.sql.SqlColumn.max;
import static com.sf.module.common.domain.sql.SqlExpression.QUERY_ARG;
import static com.sf.module.common.domain.sql.SqlExpression.equal;
import static com.sf.module.common.domain.sql.SqlExpression.in;
import static com.sf.module.common.domain.sql.SqlExpression.isNotNull;
import static com.sf.module.common.domain.sql.SqlExpression.isNull;
import static com.sf.module.common.domain.sql.SqlExpression.like;
import static com.sf.module.common.domain.sql.SqlExpression.outjoin;
import static com.sf.module.common.domain.sql.SqlExpression.withinChild;
import static com.sf.module.common.util.StringUtil.isNotBlank;

import com.sf.module.common.domain.sql.QueryStatement;
import com.sf.module.common.domain.sql.QueryStatement.TableAlias;

public class LineRepository {
	private static final String TABLE_DRIVER_LINE = "TM_DRIVER_LINE";
	private static final String TABLE_DEPARTMENT = "TM_DEPARTMENT";
	private static final String TABLE_TT_DRIVER_LINE_CONFIGURE_R = "TT_DRIVER_LINE_CONFIGURE_R";

	public static final String COL_START_TIME = "START_TIME";
	public static final String COL_END_TIME = "END_TIME";
	public static final String COL_BELONG_ZONE_CODE = "BELONG_ZONE_CODE";
	public static final String COL_VEHICLE_NUMBER = "VEHICLE_NUMBER";
	public static final String COL_VEHICLE_TYPE = "VEHICLE_TYPE";
	public static final String COL_VALID_STATUS = "VALID_STATUS";
	public static final String COL_DESTINATION_CODE = "DESTINATION_CODE";
	public static final String COL_SOURCE_CODE = "SOURCE_CODE";
	private static final String COL_MODIFY_TIME = "MODIFY_TIME";
	private static final String COL_MODIFIER = "MODIFIER";
	private static final String COL_CREATED_TIME = "CREATED_TIME";
	private static final String COL_CREATOR = "CREATOR";
	private static final String COL_DEPT_ID = "DEPT_ID";
	private static final String COL_DELETE_FLG = "DELETE_FLG";
	public static final String COL_DEPT_CODE = "DEPT_CODE";
	private static final String COL_PARENT_DEPT_CODE = "PARENT_DEPT_CODE";
	public static final String COL_INPUT_TYPE = "INPUT_TYPE";
	public static final String COL_SOURCE_DEPARTMENT_NAME = "SOURCE_NAME";
	public static final String COL_BELONG_DEPARTMENT_NAME = "BELONG_NAME";
	public static final String COL_DESTINATION_NAME = "DESTINATION_NAME";
	private static final String COL_DEPT_NAME = "DEPT_NAME";
	public static final String COL_TYPE_CODE = "TYPE_CODE";
	public static final String COL_AREA_CODE = "AREA_CODE";
	public static final String TABLE_TM_DEPARTMENT = "TM_DEPARTMENT";
	public static final String COL_LINE_ID = "id";
	public static final String YYYY_MM_DD_HH24_MM_SS = "yyyy-MM-dd hh24:mi:ss";
	private static final String ALIAS_SOURCE_DEPARTMENT = "SOURCE";
	private static final String ALIAS_DESTINATION_DEPARTMENT = "DESTINATION";
	private static final String ALIAS_BELONG_DEPARTMENT = "BELONG";
	private static final String ALIAS_CONFIGURE_STATUS = "LINE_STATUS";
	private static final String COL_CONFIG_LINE_ID = "LINE_ID";

	private static final TableAlias TABLE_ALIAS_DEPARTMENT_FOR_SOURCE = new TableAlias(TABLE_DEPARTMENT, ALIAS_SOURCE_DEPARTMENT);
	private static final TableAlias TABLE_ALIAS_SECOND_DEPARTMENT_FOR_DESTINATION = new TableAlias(TABLE_DEPARTMENT, ALIAS_DESTINATION_DEPARTMENT);
	private static final TableAlias TABLE_ALIAS_SECOND_DEPARTMENT_FOR_BELONG = new TableAlias(TABLE_DEPARTMENT, ALIAS_BELONG_DEPARTMENT);
	private static final TableAlias TABLE_ALIAS_DRIVER_LINE = new TableAlias(TABLE_DRIVER_LINE, TABLE_DRIVER_LINE);
	private static final TableAlias TABLE_ALIAS_TABLE_TT_DRIVER_LINE_CONFIGURE_R = new TableAlias(
			TABLE_TT_DRIVER_LINE_CONFIGURE_R,
			TABLE_TT_DRIVER_LINE_CONFIGURE_R);

	private static final String SQL_SEARCH_LINE_CONFIGURED_STATUS = select(
			column(TABLE_DRIVER_LINE, COL_LINE_ID),
			max(TABLE_TT_DRIVER_LINE_CONFIGURE_R, COL_CONFIG_LINE_ID, COL_CONFIG_LINE_ID))
			.from(TABLE_ALIAS_DRIVER_LINE, TABLE_ALIAS_TABLE_TT_DRIVER_LINE_CONFIGURE_R)
			.where(outjoin(column(TABLE_DRIVER_LINE, COL_LINE_ID), column(TABLE_TT_DRIVER_LINE_CONFIGURE_R, COL_CONFIG_LINE_ID)))
			.groupBy(column(TABLE_DRIVER_LINE, COL_LINE_ID))
			.toTable()
			.toString();

	private static final TableAlias TABLE_ALIAS_TABLE_TT_DRIVER_LINE_CONFIGURE_CLASSED_STATUS = new TableAlias(
			SQL_SEARCH_LINE_CONFIGURED_STATUS,
			ALIAS_CONFIGURE_STATUS);

	private static final String SQL_SEARCH_ALL_CHILDREN_ID_WITH_PARENT_ID = select(COL_DEPT_CODE)
			.from(TABLE_DEPARTMENT)
			.where(equal(COL_DELETE_FLG, 0))
			.startWithBy(COL_DEPT_ID)
			.connectBy(equal(COL_DEPT_CODE, COL_PARENT_DEPT_CODE))
			.toQuery()
			.toString();

	public static QueryStatement buildOriginalStatementForAllLines() {
		return select(
				column(ALIAS_CONFIGURE_STATUS, COL_CONFIG_LINE_ID, COL_CONFIG_LINE_ID),
				column(TABLE_DRIVER_LINE, COL_LINE_ID),
				COL_START_TIME,
				COL_END_TIME,
				COL_BELONG_ZONE_CODE,
				column(ALIAS_BELONG_DEPARTMENT, COL_DEPT_NAME, COL_BELONG_DEPARTMENT_NAME),
				COL_SOURCE_CODE,
				COL_VEHICLE_NUMBER,
				COL_VEHICLE_TYPE,
				COL_VALID_STATUS,
				column(ALIAS_SOURCE_DEPARTMENT, COL_DEPT_NAME, COL_SOURCE_DEPARTMENT_NAME),
				COL_DESTINATION_CODE,
				column(ALIAS_DESTINATION_DEPARTMENT, COL_DEPT_NAME, COL_DESTINATION_NAME),
				COL_MODIFIER,
				COL_INPUT_TYPE,
				dateColumn(COL_MODIFY_TIME, YYYY_MM_DD_HH24_MM_SS),
				COL_CREATOR,
				column(ALIAS_BELONG_DEPARTMENT, COL_AREA_CODE, COL_AREA_CODE),
				dateColumn(COL_CREATED_TIME, YYYY_MM_DD_HH24_MM_SS))
				.from(
						TABLE_ALIAS_DRIVER_LINE,
						TABLE_ALIAS_SECOND_DEPARTMENT_FOR_BELONG,
						TABLE_ALIAS_DEPARTMENT_FOR_SOURCE,
						TABLE_ALIAS_SECOND_DEPARTMENT_FOR_DESTINATION,
						TABLE_ALIAS_TABLE_TT_DRIVER_LINE_CONFIGURE_CLASSED_STATUS)
				.where(withinChild(COL_BELONG_ZONE_CODE, SQL_SEARCH_ALL_CHILDREN_ID_WITH_PARENT_ID))
				.and(equal(column(TABLE_DRIVER_LINE, COL_BELONG_ZONE_CODE), column(ALIAS_BELONG_DEPARTMENT, COL_DEPT_CODE)))
				.and(equal(column(TABLE_DRIVER_LINE, COL_SOURCE_CODE), column(ALIAS_SOURCE_DEPARTMENT, COL_DEPT_CODE)))
				.and(equal(column(TABLE_DRIVER_LINE, COL_DESTINATION_CODE), column(ALIAS_DESTINATION_DEPARTMENT, COL_DEPT_CODE)))
				.and(in(COL_INPUT_TYPE, 2))
				.and(outjoin(column(TABLE_DRIVER_LINE, COL_LINE_ID), column(ALIAS_CONFIGURE_STATUS, COL_LINE_ID)));
	}

	public static QueryStatement buildOriginalStatementForCountAllLines() {
		return select(COL_START_TIME)
				.from(
						TABLE_ALIAS_DRIVER_LINE,
						TABLE_ALIAS_SECOND_DEPARTMENT_FOR_BELONG,
						TABLE_ALIAS_DEPARTMENT_FOR_SOURCE,
						TABLE_ALIAS_SECOND_DEPARTMENT_FOR_DESTINATION,
						TABLE_ALIAS_TABLE_TT_DRIVER_LINE_CONFIGURE_CLASSED_STATUS)
				.where(withinChild(COL_BELONG_ZONE_CODE, SQL_SEARCH_ALL_CHILDREN_ID_WITH_PARENT_ID))
				.and(outjoin(column(TABLE_DRIVER_LINE, COL_LINE_ID), column(ALIAS_CONFIGURE_STATUS, COL_LINE_ID)))
				.and(equal(column(TABLE_DRIVER_LINE, COL_BELONG_ZONE_CODE), column(ALIAS_BELONG_DEPARTMENT, COL_DEPT_CODE)))
				.and(equal(column(TABLE_DRIVER_LINE, COL_SOURCE_CODE), column(ALIAS_SOURCE_DEPARTMENT, COL_DEPT_CODE)))
				.and(equal(column(TABLE_DRIVER_LINE, COL_DESTINATION_CODE), column(ALIAS_DESTINATION_DEPARTMENT, COL_DEPT_CODE)))
				.and(in(COL_INPUT_TYPE, 2));
	}

	public static void buildOptionalParameters(
			QueryStatement queryStatement,
			String vehicleNumber,
			String validStatus,
			String startTime,
			String configureStatus) {
		if (isNotBlank(vehicleNumber)) {
			queryStatement.and(like(COL_VEHICLE_NUMBER, QUERY_ARG));
		}
		if (isNotBlank(validStatus)) {
			queryStatement.and(withinChild(COL_VALID_STATUS, validStatus));
		}
		if (isNotBlank(startTime)) {
			String queryColumn = "'%" + startTime + "%'";
			queryStatement.and(like(COL_START_TIME, queryColumn)
					.or(like(COL_END_TIME, queryColumn))
					.or(like(COL_SOURCE_CODE, queryColumn))
					.or(like(COL_DESTINATION_CODE, queryColumn))
					.or(like(COL_VEHICLE_NUMBER, queryColumn)));
		}
		
		if (isNotBlank(configureStatus)) {
			// 当为0时，未配班; 否则 查询 已配班
			if(configureStatus.equals("0")) {
				queryStatement.and(isNull("LINE_STATUS.LINE_ID"));
			} else {
				queryStatement.and(isNotNull("LINE_STATUS.LINE_ID"));
			}
		}
		
		queryStatement.orderByAscending(COL_BELONG_ZONE_CODE, COL_START_TIME);
	}
	
	public static final String SQL_QUERY_LINE_BY_LINE_CONIGURE_ID = "" +
			" select t.id, \n "
			+"       t.start_time, \n "
			+"       t.end_time, \n "
			+"       t.belong_zone_code, \n "
			+"       t.source_code, \n "
			+"       t.destination_code, \n "
			+"       t.vehicle_number, \n "
			+"       t.vehicle_type, \n "
			+"       t.input_type, \n "
			+"       t.valid_status \n "
			+"  from tm_driver_line t, tt_driver_line_configure_r r \n "
			+" where r.line_id = t.id \n "
			+"   and t.valid_status = '1' \n "
			+"   and r.configure_id = ?   order by r.sort \n ";
	
	public static final String UPDATE_DRIVER_LINE = "update tm_driver_line line "
			+ " set line.start_time  = ?,"
			+ " line.end_time    = ?,"
			+ " line.modifier    = ?,"
			+ " line.modify_time = Sysdate"
			+ " where line.id in (select r.line_id"
			+ " from tt_driver_line_configure_r r"
			+ " where r.configure_id = ?)";
	
	public static final String QUERY_LINE_BY_DEPARTMENT_CODE_MONTH = "select l.start_time, l.end_time,l.SOURCE_CODE,l.DESTINATION_CODE, l.belong_zone_code dept_code,r.sort"
			+ " from tm_driver_line l, tt_driver_line_configure_r r"
			+ " where r.line_id = l.id"
			+ " and r.configure_id ="
			+ " (select distinct r.configure_id"
			+ " from tt_driver_line_configure c, tt_driver_line_configure_r r"
			+ " where c.id = r.configure_id"
			+ " and c.department_code = ?"
			+ " and c.code = ?" + " and c.month = ?)" + " order by r.sort";
	
	public static final String QUERY_DRIVING_LOG = "select t.drive_member,"
			+ " to_char(t.start_tm, 'yyyy-MM-dd HH24:mi:ss') start_tm,"
			+ " to_char(t.end_tm, 'yyyy-MM-dd HH24:mi:ss') end_tm,"
			+ " l.start_place," + " l.end_place"
			+ " from ti_vms_drive_convert t, ti_vms_driving_log_item l"
			+ " where t.driving_log_item_id = l.driving_log_item_id"
			+ " and t.drive_member = ?"
			+ " and to_char(t.start_tm, 'YYYY-MM') = ?"
			+ " and not exists(select k.drive_member from ti_vms_drive_convert_bak k"
			+ " where k.driving_log_item_id = t.driving_log_item_id)"
			+ " order by t.start_tm";
}