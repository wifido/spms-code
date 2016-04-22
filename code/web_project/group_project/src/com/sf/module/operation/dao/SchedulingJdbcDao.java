/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-17     houjingyu       创建
 **********************************************/

package com.sf.module.operation.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;

import com.sf.module.operation.domain.Scheduling;
import com.sf.module.operation.util.CommonUtil;

/**
 * 
 * 排班的JdbcDao实现类
 * 
 * @author houjingyu 2014-06-17
 * 
 */
public class SchedulingJdbcDao extends OssJdbcDao implements ISchedulingJdbcDao {
	// 根据网点,月份,工号获取有效的排班信息
	public List<Scheduling> getScheBy(final Long deptId, final String ym,
			final String empCode) {
		return this.getJdbcTemplate().execute(
				"call PKG_OSS_SCHE_PROCESS.SPT_GET_VALID_SCHE_LIST(?,?,?,?)",
				new CallableStatementCallback<List<Scheduling>>() {
					public List<Scheduling> doInCallableStatement(
							CallableStatement ps) throws SQLException,
							DataAccessException {
						ResultSet rs = null;
						try {
							int outParameter = 4;
							ps.setLong(1, deptId);
							ps.setString(2, ym);
							ps.setString(3, empCode);
							ps.registerOutParameter(outParameter,
									OracleTypes.CURSOR);
							ps.execute();
							List<Scheduling> data = new ArrayList<Scheduling>();
							if (ps.getObject(outParameter) == null) {
								return data;
							}

							rs = (ResultSet) ps.getObject(outParameter);
							while (rs.next()) {
								Scheduling s = new Scheduling();
								s.setId(rs.getLong("ID"));
								s.setDeptId(rs.getLong("DEPT_ID"));
								s.setEmpCode(rs.getString("EMP_CODE"));
								s.setScheduleCode(rs.getString("SHEDULE_CODE"));
								// s.setSheduleId(rs.getLong("SHEDULE_ID"));
								s.setSheduleDt(rs.getDate("SHEDULE_DT"));
								s.setSheduleMonId(rs.getLong("SHEDULE_MON_ID"));
								data.add(s);
							}
							return data;
						} finally {
							if (rs != null) {
								rs.close();
							}
						}
						
					}
				});
	}

	// 检查所选日期是否在所选的班别代码有效期内
	public boolean checkScheDtInDateRange(Long deptId, String schecode, Date dt) {
		String sql = " SELECT CASE"
				+ " WHEN ? >= ENABLE_DT AND"
				+ "  ? < NVL(DISABLE_DT, TO_DATE('2114-01-01', 'YYYY-MM-DD')) THEN"
				+ " 1" + " ELSE" + " 0" + " END FLAG"
				+ " FROM TM_PB_SCHEDULE_BASE_INFO T" + " WHERE T.DEPT_ID = ?"
				+ " AND T.SCHEDULE_CODE = ? and CLASS_TYPE='1' ";
		Object[] args = { CommonUtil.converDate(dt), CommonUtil.converDate(dt),
				deptId, schecode };
		List<Integer> list = this.getJdbcTemplate().queryForList(sql, args,
				Integer.class);
		if (list != null && list.size() > 0) {
			if (list.get(0) == 1) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public Integer getGroupDtEmpDtValid(Long deptId, String empCode, Date dt) {
		String sql = "SELECT "
				+ "        CASE"
				+ "          WHEN G.DISABLE_DT IS NULL THEN"
				+ "           1"
				+ "          WHEN G.DISABLE_DT IS NOT NULL AND TRUNC(?,'MM') >= G.DISABLE_DT THEN"
				+ "           0"
				+ "          WHEN G.DISABLE_DT IS NOT NULL AND TRUNC(?,'MM') < G.DISABLE_DT AND G.DISABLE_DT<= ?  THEN"
				+ "           1"
				+ "          WHEN G.DISABLE_DT IS NOT NULL AND ? < G.DISABLE_DT THEN"
				+ "           1" + "        END GROUP_FLAG"
				+ "   FROM TM_OSS_EMPLOYEE T, TM_PB_GROUP_INFO G"
				+ "  WHERE T.DEPT_ID = G.DEPT_ID"
				+ "    AND T.GROUP_ID = G.GROUP_ID" + "    AND T.EMP_CODE = ?"
				+ "    AND T.DEPT_ID = ?";
		Object[] args = { CommonUtil.converDate(dt), CommonUtil.converDate(dt),
				CommonUtil.converDate(dt), CommonUtil.converDate(dt), empCode,
				deptId };
		return this.getJdbcTemplate().queryForObject(sql, args, Integer.class);
	}

	public Date[] getValidDateRange(Long deptid, String empcode, String ym) {
		Date[] dt = new Date[2];
		String sql = "SELECT MAX(CASE"
				+ " WHEN START_TM IS NULL AND END_TM IS NULL AND"
				+ "    DIMISSION_DT_TEMP <= FIRST_DAY THEN"
				+ "                          NULL"
				+ " WHEN START_TM IS NULL AND END_TM IS NULL AND"
				+ "                              DIMISSION_DT_TEMP > FIRST_DAY AND"
				+ "                              DIMISSION_DT_TEMP <= LAST_DAY THEN"
				+ "                          TO_CHAR(FIRST_DAY, 'YYYY-MM-DD') || '~' ||"
				+ "                          TO_CHAR(DIMISSION_DT_TEMP - 1, 'YYYY-MM-DD')"
				+ " WHEN START_TM IS NULL AND END_TM IS NULL AND"
				+ "                              DIMISSION_DT_TEMP > LAST_DAY THEN"
				+ "                          TO_CHAR(FIRST_DAY, 'YYYY-MM-DD') || '~' ||"
				+ "                           TO_CHAR(LAST_DAY, 'YYYY-MM-DD')  "
				+ " WHEN START_TM IS NOT NULL AND"
				+ "                              DIMISSION_DT_TEMP > FIRST_DAY AND"
				+ "                              END_TM > FIRST_DAY THEN"
				+ "                          TO_CHAR(GREATEST(FIRST_DAY, START_TM), 'YYYY-MM-DD') || '~' ||"
				+ "                           TO_CHAR(DECODE(LEAST(LAST_DAY,"
				+ "                            DIMISSION_DT_TEMP,"
				+ "                            END_TM),"
				+ "                            LAST_DAY,"
				+ "                            LAST_DAY,"
				+ "                            LEAST(LAST_DAY,"
				+ "                            DIMISSION_DT_TEMP,"
				+ "                            END_TM) - 1),"
				+ "                            'YYYY-MM-DD')"
				+ " WHEN START_TM IS NOT NULL AND"
				+ "                          DIMISSION_DT_TEMP > LAST_DAY AND END_TM > LAST_DAY THEN"
				+ "                          TO_CHAR(GREATEST(FIRST_DAY, START_TM), 'YYYY-MM-DD')"
				+ "                          || '~' || TO_CHAR(LAST_DAY, 'YYYY-MM-DD')"
				+ " WHEN START_TM IS NOT NULL AND END_TM IS NOT NULL AND"
				+ "                              END_TM <= FIRST_DAY THEN"
				+ "                          NULL"
				+ " WHEN START_TM IS NOT NULL AND END_TM IS NOT NULL AND"
				+ "                              DIMISSION_DT_TEMP <= FIRST_DAY THEN"
				+ "                          NULL"
				+ " WHEN START_TM IS NOT NULL AND"
				+ "                              DIMISSION_DT_TEMP <= START_TM THEN"
				+ "                          NULL"
				+ " WHEN START_TM IS NOT NULL AND LAST_DAY <= START_TM AND"
				+ "                              START_TM <= DIMISSION_DT_TEMP THEN"
				+ "                          NULL"
				+ "           END)"
				+ "  FROM (SELECT EM.DEPT_ID,"
				+ "               EM.EMP_CODE,"
				+ "               EM.DIMISSION_DT,"
				+ "               CHG.CHANGE_DEPT_ID,"
				+ "               START_TM,"
				+ "               END_TM,"
				+ "               TRUNC(TO_DATE(? || '-01', 'YYYY-MM-DD'), 'MM') FIRST_DAY,"
				+ "               NVL(EM.DIMISSION_DT, TO_DATE('2114-01-01', 'YYYY-MM-DD')) DIMISSION_DT_TEMP,"
				+ "               LAST_DAY(TO_DATE(? || '-01', 'YYYY-MM-DD')) LAST_DAY"
				+ "          FROM TM_OSS_EMPLOYEE EM,"
				+ "               (SELECT CHANGE_DEPT_ID,"
				+ "                       EMP_CODE,"
				+ "                       CHANGE_DEPT_CODE,"
				+ "                       START_TM,"
				+ "                       NVL(END_TM, TO_DATE('2114-01-01', 'YYYY-MM-DD')) END_TM"
				+ "                  FROM (SELECT D.DEPT_ID CHANGE_DEPT_ID,"
				+ "                               CH.EMP_CODE,"
				+ "                               CH.DEPT_CODE CHANGE_DEPT_CODE,"
				+ "                               CH.CHANGE_ZONE_TM START_TM,"
				+ "                               LEAD(CH.CHANGE_ZONE_TM) OVER(PARTITION BY CH.EMP_CODE ORDER BY CH.CHANGE_ZONE_TM) END_TM"
				+ "                          FROM TI_OSS_HR_EMP_NEW_CHANGEDEPT CH,"
				+ "                               TM_DEPARTMENT                 D"
				+ "                         WHERE CH.DEPT_CODE = D.DEPT_CODE"
				+ "                           AND CH.EMP_STUS = 2)) CHG"
				+ "         WHERE EM.EMP_CODE = CHG.EMP_CODE(+)"
				+ "           AND EM.DEPT_ID = CHG.CHANGE_DEPT_ID(+))"
				+ " WHERE EMP_CODE = ?" + "   AND DEPT_ID = ?"
				+ " GROUP BY EMP_CODE, DEPT_ID";
		Object[] args = { ym, ym, empcode, deptid };
		List<String> list = this.getJdbcTemplate().queryForList(sql, args,
				String.class);
		if (list != null && list.size() > 0) {
			String a = list.get(0);
			if (null != a) {
				String[] d = list.get(0).split("~");
				dt[0] = CommonUtil.getYmd(d[0]);
				dt[1] = CommonUtil.getYmd(d[1]);
			}
		}
		return dt;
	}
	public Scheduling findByCondition(Date dt, String empcode) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ID,DEPT_ID,SHEDULE_DT,EMP_CODE,CREATE_TM,MODIFIED_TM,CREATE_EMP_CODE,MODIFIED_EMP_CODE,SHEDULE_MON_ID,SHEDULE_CODE ")
		.append(" FROM TT_PB_SHEDULE_BY_DAY WHERE EMP_CODE=?  AND SHEDULE_DT = ? ");
		List<Scheduling> lst = this.queryForList(sb.toString(), new Object[] {empcode, CommonUtil.converDate(dt)}, Scheduling.class);
		return lst == null || lst.isEmpty() ? null : lst.get(0);
	}
	
}