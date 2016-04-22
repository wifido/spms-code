/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-20     houjingyu       创建
 **********************************************/

package com.sf.module.operation.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;

import com.sf.framework.base.IPage;
import com.sf.module.operation.action.dto.ScheduleDto;
import com.sf.module.operation.domain.MonthConfirmStatus;
import com.sf.module.operation.domain.OutEmployee;
import com.sf.module.operation.domain.SchedulMgt;
import com.sf.module.operation.domain.SchedulingBase;
import com.sf.module.operation.util.CommonUtil;

/**
 * 
 * 排班管理的JdbcDao实现类
 * 
 * @author houjingyu 2014-06-20
 * 
 */
public class SchedulMgtJdbcDao extends OssJdbcDao implements ISchedulMgtJdbcDao {

	public int getMgtCount(Long userId, ScheduleDto dto) {
		String sql = "    select ("
				+  "		SELECT COUNT(1)"
				+  "          FROM (select MAX(L.ID), L.EMP_CODE, L.DEPT_ID, L.YM"
				+  "                  from TT_PB_SHEDULE_BY_MONTH_LOG L";
		if (!StringUtils.isBlank(dto.getYm())) {
			sql = sql + " where L.YM = '" + dto.getYm() + "'";
		}
		sql = sql + "       AND DEPT_ID IN"
				+  "                       (SELECT DEPT_ID"
				+  "                          FROM TM_DEPARTMENT"
				+  "                         WHERE DELETE_FLG = 0"
				+  "                        CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE"
				+  "                         START WITH DEPT_ID = ?)"
				+  "                 GROUP BY L.EMP_CODE, L.DEPT_ID, L.YM))  +"
				+ "           (SELECT COUNT(ID)   "
				+ "              from TT_PB_SHEDULE_BY_MONTH   "
				+ "             where DEPT_ID IN (SELECT DEPT_ID   "
				+ "                                 FROM TM_DEPARTMENT   "
				+ "                                WHERE DELETE_FLG = 0   " 
				+ "                               CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE   "
				+ "                                START WITH DEPT_ID = ?) and COMMIT_STATUS <> 1";
		if (!StringUtils.isBlank(dto.getYm())) {
			sql = sql + " and ym = '" + dto.getYm() + "'";
		}
		sql = sql + ") " + "   from dual   ";
		Object[] args = { dto.getDeptId(), dto.getDeptId() };
		return this.getJdbcTemplate().queryForObject(sql, args, Integer.class);
	}

	// 分页查询
	public IPage<SchedulMgt> findPage(Long userId, ScheduleDto dto, int pageSize, int pageIndex) {
		String sql = "                                 SELECT ID, "
				+ "                                        D.DEPT_ID, "
				+ "                                        D.AREA_CODE, "
				+ "                                        D.DEPT_CODE, "
				+ "                                        COMMIT_STATUS, "
				+ "                                        DECODE(MON_WORK_TYPE,NULL, decode_emp_worktype(work_type), decode_emp_worktype(MON_WORK_TYPE)) work_Type_Str, "
				+ "                                        DECODE(MON_EMP_NAME,NULL,EMP_NAME,MON_EMP_NAME) EMP_NAME, "
				+ "                                        EMP_CODE, "
				+ "                                        YM, "
				+ "                                        GROUP_ID, "
				+ "                                        DIMISSION_DT, "
				+ "                                        TRANSFER_DATE, "
				+ "                                        LAST_ZNO, "
				+ "                                        EMP_POST_TYPE, "
				+ "                                        DATE_FROM, "
				+ "                                        EMP_DUTY_NAME, "
				+ "                                        SF_DATE, "
				+ "                                        DAY1, "
				+ "                                        DAY2, "
				+ "                                        DAY3, "
				+ "                                        DAY4, "
				+ "                                        DAY5, "
				+ "                                        DAY6, "
				+ "                                        DAY7, "
				+ "                                        DAY8, "
				+ "                                        DAY9, "
				+ "                                        DAY10, "
				+ "                                        DAY11, "
				+ "                                        DAY12, "
				+ "                                        DAY13, "
				+ "                                        DAY14, "
				+ "                                        DAY15, "
				+ "                                        DAY16, "
				+ "                                        DAY17, "
				+ "                                        DAY18, "
				+ "                                        DAY19, "
				+ "                                        DAY20, "
				+ "                                        DAY21, "
				+ "                                        DAY22, "
				+ "                                        DAY23, "
				+ "                                        DAY24, "
				+ "                                        DAY25, "
				+ "                                        DAY26, "
				+ "                                        DAY27, "
				+ "                                        DAY28, "
				+ "                                        DAY29, "
				+ "                                        DAY30, "
				+ "                                        DAY31, "
				+ "                                        BETWEEN_DATE, "
				+ "                                        VERSION "
				+ "                                   FROM (SELECT MAX(B.ID) ID, "
				+ "                                                B.DEPT_ID, "
				+ "                                                A.EMP_CODE, "
				+ "                                              B.COMMIT_STATUS, "
				+ "                                                B.YM, "
				+ "                                               MAX(MON_WORK_TYPE) MON_WORK_TYPE, "
				+ "                                                MAX( MON_EMP_NAME) MON_EMP_NAME, "
				+ "                                                MAX(EMP_NAME) EMP_NAME, "
				+ "                                                MAX(GROUP_ID) GROUP_ID, "
				+ "                                                MAX(DIMISSION_DT) DIMISSION_DT, "
				+ "                                                MAX(TRANSFER_DATE) TRANSFER_DATE, "
				+ "                                                MAX(LAST_ZNO) LAST_ZNO, "
				+ "                                                MAX(EMP_POST_TYPE) EMP_POST_TYPE, "
				+ "                                                MAX(DATE_FROM) DATE_FROM, "
				+ "                                                MAX(SF_DATE) SF_DATE, "
				+ "                                                MAX(WORK_TYPE) WORK_TYPE, "
				+ "                                                MAX(EMP_DUTY_NAME) EMP_DUTY_NAME,"
				+ "                                                MAX(DAY1) DAY1, "
				+ "                                                MAX(DAY2) DAY2, "
				+ "                                                MAX(DAY3) DAY3, "
				+ "                                                MAX(DAY4) DAY4, "
				+ "                                                MAX(DAY5) DAY5, "
				+ "                                                MAX(DAY6) DAY6, "
				+ "                                                MAX(DAY7) DAY7, "
				+ "                                                MAX(DAY8) DAY8, "
				+ "                                                MAX(DAY9) DAY9, "
				+ "                                                MAX(DAY10) DAY10, "
				+ "                                                MAX(DAY11) DAY11, "
				+ "                                                MAX(DAY12) DAY12, "
				+ "                                                MAX(DAY13) DAY13, "
				+ "                                                MAX(DAY14) DAY14, "
				+ "                                                MAX(DAY15) DAY15, "
				+ "                                                MAX(DAY16) DAY16, "
				+ "                                                MAX(DAY17) DAY17, "
				+ "                                                MAX(DAY18) DAY18, "
				+ "                                                MAX(DAY19) DAY19, "
				+ "                                                MAX(DAY20) DAY20, "
				+ "                                                MAX(DAY21) DAY21, "
				+ "                                                MAX(DAY22) DAY22, "
				+ "                                                MAX(DAY23) DAY23, "
				+ "                                                MAX(DAY24) DAY24, "
				+ "                                                MAX(DAY25) DAY25, "
				+ "                                                MAX(DAY26) DAY26, "
				+ "                                                MAX(DAY27) DAY27, "
				+ "                                                MAX(DAY28) DAY28, "
				+ "                                                MAX(DAY29) DAY29, "
				+ "                                                MAX(DAY30) DAY30, "
				+ "                                                MAX(DAY31) DAY31, "
				+ "                                                MAX(VERSION) VERSION, "
				+ "                                                MAX(START_DATE||'~'||END_DATE) BETWEEN_DATE, "
				+ "                                                MAX(NEED_DAY) NEED_DAYS, "
				+ "                                                COUNT(1) ACTUAL_DAYS "
				+ "                                           FROM TT_PB_SHEDULE_BY_DAY A, "
				+ "                                                (SELECT ID, "
				+ "                                                        DEPT_ID, "
				+ "                                                        EMP_CODE, "
				+ "                                                        COMMIT_STATUS, "
				+ "                                                        YM, "
				+ "                                                        EMP_NAME, "
				+ "                                                        MON_WORK_TYPE, "
				+ "                                                        MON_EMP_NAME, "
				+ "                                                        GROUP_ID, "
				+ "                                                        DIMISSION_DT, "
				+ "                                                        TRANSFER_DATE, "
				+ "                                                        LAST_ZNO, "
				+ "                                                        EMP_POST_TYPE, "
				+ "                                                        DATE_FROM, "
				+ "                                                        EMP_DUTY_NAME,"
				+ "                                                        SF_DATE, "
				+ "                                                        WORK_TYPE, "
				+ "                                                        DAY1, "
				+ "                                                        DAY2, "
				+ "                                                        DAY3, "
				+ "                                                        DAY4, "
				+ "                                                        DAY5, "
				+ "                                                        DAY6, "
				+ "                                                        DAY7, "
				+ "                                                        DAY8, "
				+ "                                                        DAY9, "
				+ "                                                        DAY10, "
				+ "                                                        DAY11, "
				+ "                                                        DAY12, "
				+ "                                                        DAY13, "
				+ "                                                        DAY14, "
				+ "                                                        DAY15, "
				+ "                                                        DAY16, "
				+ "                                                        DAY17, "
				+ "                                                        DAY18, "
				+ "                                                        DAY19, "
				+ "                                                        DAY20, "
				+ "                                                        DAY21, "
				+ "                                                        DAY22, "
				+ "                                                        DAY23, "
				+ "                                                        DAY24, "
				+ "                                                        DAY25, "
				+ "                                                        DAY26, "
				+ "                                                        DAY27, "
				+ "                                                        DAY28, "
				+ "                                                        DAY29, "
				+ "                                                        DAY30, "
				+ "                                                        DAY31, "
				+ "                                                        VERSION, "
				+ "                                                        SUBSTR(BETWEEN_DATE, 0, 10) START_DATE, "
				+ "                                                        SUBSTR(BETWEEN_DATE, 12, 10) END_DATE, "
				+ "                                                        SUBSTR(BETWEEN_DATE, 23, LENGTH(BETWEEN_DATE)) NEED_DAY "
				+ "                                                   FROM (SELECT MAX(ID) ID, "
				+ "                                                                DEPT_ID, "
				+ "                                                                EMP_CODE, "
				+ "                                                                COMMIT_STATUS, "
				+ "                                                                YM, "
				+ "                                                                MAX(MON_WORK_TYPE) MON_WORK_TYPE, "
				+ "                                                                MAX(MON_EMP_NAME)  MON_EMP_NAME, "
				+ "                                                                MAX(EMP_NAME) EMP_NAME, "
				+ "                                                                MAX(GROUP_ID) GROUP_ID, "
				+ "                                                                MAX(DIMISSION_DT) DIMISSION_DT, "
				+ "                                                                MAX(TRANSFER_DATE) TRANSFER_DATE, "
				+ "                                                                MAX(LAST_ZNO)  LAST_ZNO, "
				+ "                                                                MAX(EMP_POST_TYPE) EMP_POST_TYPE, "
				+ "                                                                MAX(DATE_FROM) DATE_FROM, "
				+ "                                                                MAX(EMP_DUTY_NAME) EMP_DUTY_NAME, "
				+ "                                                                MAX(SF_DATE) SF_DATE, "
				+ "                                                                MAX(WORK_TYPE) WORK_TYPE, "
				+ "                                                                MAX(DAY1) DAY1, "
				+ "                                                                MAX(DAY2) DAY2, "
				+ "                                                                MAX(DAY3) DAY3, "
				+ "                                                                MAX(DAY4) DAY4, "
				+ "                                                                MAX(DAY5) DAY5, "
				+ "                                                                MAX(DAY6) DAY6, "
				+ "                                                                MAX(DAY7) DAY7, "
				+ "                                                                MAX(DAY8) DAY8, "
				+ "                                                                MAX(DAY9) DAY9, "
				+ "                                                                MAX(DAY10) DAY10, "
				+ "                                                                MAX(DAY11) DAY11, "
				+ "                                                                MAX(DAY12) DAY12, "
				+ "                                                                MAX(DAY13) DAY13, "
				+ "                                                                MAX(DAY14) DAY14, "
				+ "                                                                MAX(DAY15) DAY15, "
				+ "                                                                MAX(DAY16) DAY16, "
				+ "                                                                MAX(DAY17) DAY17, "
				+ "                                                                MAX(DAY18) DAY18, "
				+ "                                                                MAX(DAY19) DAY19, "
				+ "                                                                MAX(DAY20) DAY20, "
				+ "                                                                MAX(DAY21) DAY21, "
				+ "                                                                MAX(DAY22) DAY22, "
				+ "                                                                MAX(DAY23) DAY23, "
				+ "                                                                MAX(DAY24) DAY24, "
				+ "                                                                MAX(DAY25) DAY25, "
				+ "                                                                MAX(DAY26) DAY26, "
				+ "                                                                MAX(DAY27) DAY27, "
				+ "                                                                MAX(DAY28) DAY28, "
				+ "                                                                MAX(DAY29) DAY29, "
				+ "                                                                MAX(DAY30) DAY30, "
				+ "                                                                MAX(DAY31) DAY31, "
				+ "                                                                MAX(VERSION) VERSION, "
				+ "                                                                MAX(CASE "
				+ "                                                                      WHEN  DIMISSION_DT_TEMP <= FIRST_DAY THEN "
				+ "                                                                       TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' || "
				+ "                                                                       TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' || '0' "
				+ "                                                                      WHEN DIMISSION_DT_TEMP > FIRST_DAY AND "
				+ "                                                                           DIMISSION_DT_TEMP <= LAST_DAY THEN "
				+ "                                                                       TO_CHAR(FIRST_DAY, 'YYYY-MM-DD') || '~' || "
				+ "                                                                       TO_CHAR(DIMISSION_DT_TEMP - 1, 'YYYY-MM-DD') || '~' || "
				+ "                                                                       TO_CHAR(TO_NUMBER(TO_CHAR(DIMISSION_DT_TEMP, "
				+ "                                                                                                 'DD')) - "
				+ "                                                                               TO_NUMBER(TO_CHAR(FIRST_DAY, 'DD'))) "
				+ "                                                                      WHEN  DIMISSION_DT_TEMP > LAST_DAY THEN "
				+ "                                                                       TO_CHAR(FIRST_DAY, 'YYYY-MM-DD') || '~' || "
				+ "                                                                       TO_CHAR(LAST_DAY, 'YYYY-MM-DD') || '~' || "
				+ "                                                                       TO_CHAR(LAST_DAY, 'DD') "
				+ "                                                                    END) BETWEEN_DATE "
				+ "                                                           FROM (SELECT MON.DEPT_ID, "
				+ "                                                                        MON.ID, "
				+ "                                                                         MON.COMMIT_STATUS, "
				+ "                                                                        MON.YM, "
				+ "                                                                        MON.DAY1, "
				+ "                                                                        MON.DAY2, "
				+ "                                                                        MON.DAY3, "
				+ "                                                                        MON.DAY4, "
				+ "                                                                        MON.DAY5, "
				+ "                                                                        MON.DAY6, "
				+ "                                                                        MON.DAY7, "
				+ "                                                                        MON.DAY8, "
				+ "                                                                        MON.DAY9, "
				+ "                                                                        MON.DAY10, "
				+ "                                                                        MON.DAY11, "
				+ "                                                                        MON.DAY12, "
				+ "                                                                        MON.DAY13, "
				+ "                                                                        MON.DAY14, "
				+ "                                                                        MON.DAY15, "
				+ "                                                                        MON.DAY16, "
				+ "                                                                        MON.DAY17, "
				+ "                                                                        MON.DAY18, "
				+ "                                                                        MON.DAY19, "
				+ "                                                                        MON.DAY20, "
				+ "                                                                        MON.DAY21, "
				+ "                                                                        MON.DAY22, "
				+ "                                                                        MON.DAY23, "
				+ "                                                                        MON.DAY24, "
				+ "                                                                        MON.DAY25, "
				+ "                                                                        MON.DAY26, "
				+ "                                                                        MON.DAY27, "
				+ "                                                                        MON.DAY28, "
				+ "                                                                        MON.DAY29, "
				+ "                                                                        MON.DAY30, "
				+ "                                                                        MON.DAY31, "
				+ "                                                                        MON.VERSION, "
				+ "                                                                        EM.EMP_CODE, "
				+ "                                                                        EM.EMP_NAME, "
				+ "                                                                        EM.WORK_TYPE, "
				+ "                                                                        EM.GROUP_ID, "
				+ "                                                                        EM.DIMISSION_DT, "
				+ "                                                                        EM.TRANSFER_DATE, "
				+ "                                                                        EM.DATE_FROM, "
				+ "                                                                        EM.EMP_DUTY_NAME, "
				+ "																		   EM.SF_DATE,"
				+ "                                                                        EM.LAST_ZNO, "
				+ "                                                                        EM.EMP_POST_TYPE, "
				+ "                                                                        MON.WORK_TYPE  MON_WORK_TYPE, "
				+ "                                                                        MON.EMP_NAME MON_EMP_NAME, "
/*				+ "																	       case   "
				+ "                      												   when greatest(nvl(transfer_date, sysdate - 10000),     "
			    + "                                    									   nvl(date_from, sysdate - 10000),     "
			    + "                                  									   nvl(sf_date, sysdate - 10000)) >     "
				+ "                         											   TRUNC(TO_DATE(MON.YM || '-01', 'YYYY-MM-DD'),     "
				+ "                       										           'MM') then     "
				+ "                 								                       greatest(nvl(transfer_date, sysdate - 10000),     "
				+ "                  						                               nvl(date_from, sysdate - 10000),     "
				+ "                 						                               nvl(sf_date, sysdate - 10000))     "
				+ "               								                           else     "
				+ "                								                           TRUNC(TO_DATE(MON.YM || '-01', 'YYYY-MM-DD'), 'MM')     "
				+ "               								                           end FIRST_DAY ,  "*/
				+ "																		   TRUNC(TO_DATE(MON.YM || '-01', " 
                + "       																   'YYYY-MM-DD'), " 																					  
                + "      																   'MM') FIRST_DAY, "
				+ "                                                              	          NVL(EM.DIMISSION_DT, "
				+ "                                                                            TO_DATE('2114-01-01', 'YYYY-MM-DD')) DIMISSION_DT_TEMP, "
				+ "                                                                        LAST_DAY(TO_DATE(MON.YM || '-01', "
				+ "                                                                                         'YYYY-MM-DD')) LAST_DAY "
				+ "                                                                   FROM TM_OSS_EMPLOYEE EM, "
				+ "                                                                        TM_PB_GROUP_INFO G, "
				+ "                                                                        TT_PB_SHEDULE_BY_MONTH MON "
				+ "                                                                  WHERE EM.GROUP_ID = G.GROUP_ID(+)  "
				+ "                                                                    AND EM.EMP_CODE = MON.EMP_CODE  ";

		if (CommonUtil.isAdmin(userId)) {
			sql = sql
					+ " AND MON.DEPT_ID IN (SELECT DEPT_ID FROM TM_DEPARTMENT WHERE DELETE_FLG = 0 CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE START WITH DEPT_ID = ? )";
		} else {
			sql = sql
					+ " AND MON.DEPT_ID IN (SELECT D.DEPT_ID FROM (SELECT DEPT_ID FROM TM_DEPARTMENT "
					+ "  WHERE DELETE_FLG = 0 CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE START WITH DEPT_ID = ?) D, TS_USER_DEPT UD WHERE D.DEPT_ID = UD.DEPT_ID AND UD.USER_ID =" + userId + ")";
		}
		sql = sql + "                                   )" + "                         GROUP BY EMP_CODE, DEPT_ID, YM, COMMIT_STATUS)) B"
				+ "         WHERE A.DEPT_ID = B.DEPT_ID" + "           AND A.EMP_CODE = B.EMP_CODE" + "           AND TO_CHAR(A.SHEDULE_DT, 'YYYY-MM') = B.YM"
				+ "           AND A.SHEDULE_DT >= TO_DATE(START_DATE, 'YYYY-MM-DD')" + "           AND A.SHEDULE_DT <= TO_DATE(END_DATE, 'YYYY-MM-DD')"
				+ "         GROUP BY B.DEPT_ID, A.EMP_CODE, B.YM, B.COMMIT_STATUS) M,"
				+ "       TM_DEPARTMENT D" + " WHERE M.DEPT_ID = D.DEPT_ID";
		if (!StringUtils.isBlank(dto.getEmpCode())) {
			sql = sql + " AND EMP_CODE LIKE '%" + dto.getEmpCode() + "%'";
		}
		if (!StringUtils.isBlank(dto.getEmpName())) {
			sql = sql + " AND EMP_NAME LIKE '%" + dto.getEmpName() + "%'";
		}
		if (!StringUtils.isBlank(dto.getYm())) {
			sql = sql + " AND YM ='" + dto.getYm() + "'";
		}
		if (dto.getTeamId() != null) {
			sql = sql + " AND GROUP_ID = " + dto.getTeamId();
		}
		if (dto.getStatus() != null && dto.getStatus() != 3) {
			String[] whereStr = { "", " AND ACTUAL_DAYS = NEED_DAYS", " AND ACTUAL_DAYS < NEED_DAYS", "", " AND MON.COMMIT_STATUS = 1 ",
					" AND MON.COMMIT_STATUS = 0 " };
			sql = sql + whereStr[dto.getStatus()];
		}
		if (!StringUtils.isBlank(dto.getClassName())) {
			sql = sql + " AND ID IN (SELECT SHEDULE_MON_ID FROM TT_PB_SHEDULE_BY_DAY Y" + " WHERE SHEDULE_CODE = '" + dto.getClassName()
					+ "' GROUP BY SHEDULE_MON_ID)";
		}
		Object[] args = { dto.getDeptId() };
		return this.fetchPage(sql, args, pageSize, pageIndex, SchedulMgt.class);
	}

	// 分页查询
	public IPage<SchedulMgt> schedulMgtConfirmImport(ScheduleDto dto, int pageSize, int pageIndex) {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT id,");
		sql.append("  D.AREA_CODE,");
		sql.append("  D.DEPT_CODE,");
		sql.append("  DECODE(tlog.emp_name,NULL, em.emp_name, tlog.emp_name) emp_name,");
		sql.append("  em.emp_duty_name,");
		sql.append("  DECODE(tlog.work_type,NULL, decode_emp_worktype(em.work_type), decode_emp_worktype(tlog.work_type)) work_Type_Str,  ");
		sql.append("  tlog.YM, ");
		sql.append("  tlog.DEPT_ID,");
		sql.append("  tlog.EMP_CODE,");
		sql.append("  tlog.DAY1, ");
		sql.append("  tlog.DAY2, ");
		sql.append("  tlog.DAY3, ");
		sql.append("  tlog.DAY4, ");
		sql.append("  tlog.DAY5, ");
		sql.append("  tlog.DAY6, ");
		sql.append("  tlog.DAY7, ");
		sql.append("  tlog.DAY8, ");
		sql.append("  tlog.DAY9, ");
		sql.append("  tlog.DAY10, ");
		sql.append("  tlog.DAY11, ");
		sql.append("  tlog.DAY12, ");
		sql.append("  tlog.DAY13, ");
		sql.append("  tlog.DAY14, ");
		sql.append("  tlog.DAY15, ");
		sql.append("  tlog.DAY16, ");
		sql.append("  tlog.DAY17, ");
		sql.append("  tlog.DAY18, ");
		sql.append("  tlog.DAY19, ");
		sql.append("  tlog.DAY20, ");
		sql.append("  tlog.DAY21, ");
		sql.append("  tlog.DAY22, ");
		sql.append("  tlog.DAY23, ");
		sql.append("  tlog.DAY24, ");
		sql.append("  tlog.DAY25, ");
		sql.append("  tlog.DAY26, ");
		sql.append("  tlog.DAY27, ");
		sql.append("  tlog.DAY28, ");
		sql.append("  tlog.DAY29, ");
		sql.append("  tlog.DAY30, ");
		sql.append("  tlog.DAY31, ");
		sql.append("  tlog.CREATE_TM, ");
		sql.append("  tlog.MODIFIED_TM, ");
		sql.append("  tlog.CREATE_EMP_CODE, ");
		sql.append("  tlog.MODIFIED_EMP_CODE, ");
		sql.append("  tlog.VERSION ");
		sql.append("  FROM TT_PB_SHEDULE_BY_MONTH_LOG  tlog,");
		sql.append("  TM_DEPARTMENT d ,TM_OSS_EMPLOYEE em  where d.dept_id = tlog.dept_id ");
		sql.append("  and tlog.emp_code = em.emp_code ");
		sql.append("  and tlog.DEPT_ID IN  (SELECT DEPT_ID FROM TM_DEPARTMENT ");
		sql.append("  WHERE DELETE_FLG = 0 and DEPT_ID = ?)   and tlog.id in (select t.g_id from " );
		sql.append("  (select max(g.id) g_id,g.emp_code, g.ym, g.dept_id");
		sql.append("  from TT_PB_SHEDULE_BY_MONTH_LOG g");
		sql.append("  group by g.emp_code, g.ym, g.dept_id");
		sql.append("  )t) ");
		if (!StringUtils.isBlank(dto.getYm())) {
			sql.append(" and tlog.ym = '" + dto.getYm() + "'");
		}

		Object[] args = { dto.getDeptId() };
		return this.fetchPage(sql.toString(), args, pageSize, pageIndex, SchedulMgt.class);
	}

	public IPage<SchedulMgt> schedulMgtNoConfirmImport(ScheduleDto dto, int pageSize, int pageIndex) {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT id,");
		sql.append("  D.AREA_CODE,");
		sql.append("  D.DEPT_CODE,");
		sql.append("  DECODE(tm.emp_name,null,em.emp_name,tm.emp_name) emp_name,");
		sql.append("  em.emp_duty_name,");
		sql.append("  DECODE(tm.work_type,NULL, decode_emp_worktype(em.work_type), decode_emp_worktype(tm.work_type)) work_Type_Str, ");
		sql.append("  tm.YM, ");
		sql.append("  tm.DEPT_ID,");
		sql.append("  tm.EMP_CODE,");
		sql.append("  tm.DAY1, ");
		sql.append("  tm.DAY2, ");
		sql.append("  tm.DAY3, ");
		sql.append("  tm.DAY4, ");
		sql.append("  tm.DAY5, ");
		sql.append("  tm.DAY6, ");
		sql.append("  tm.DAY7, ");
		sql.append("  tm.DAY8, ");
		sql.append("  tm.DAY9, ");
		sql.append("  tm.DAY10, ");
		sql.append("  tm.DAY11, ");
		sql.append("  tm.DAY12, ");
		sql.append("  tm.DAY13, ");
		sql.append("  tm.DAY14, ");
		sql.append("  tm.DAY15, ");
		sql.append("  tm.DAY16, ");
		sql.append("  tm.DAY17, ");
		sql.append("  tm.DAY18, ");
		sql.append("  tm.DAY19, ");
		sql.append("  tm.DAY20, ");
		sql.append("  tm.DAY21, ");
		sql.append("  tm.DAY22, ");
		sql.append("  tm.DAY23, ");
		sql.append("  tm.DAY24, ");
		sql.append("  tm.DAY25, ");
		sql.append("  tm.DAY26, ");
		sql.append("  tm.DAY27, ");
		sql.append("  tm.DAY28, ");
		sql.append("  tm.DAY29, ");
		sql.append("  tm.DAY30, ");
		sql.append("  tm.DAY31, ");
		sql.append("  tm.COMMIT_STATUS,");
		sql.append("  tm.CREATE_TM, ");
		sql.append("  tm.MODIFIED_TM, ");
		sql.append("  tm.CREATE_EMP_CODE, ");
		sql.append("  tm.MODIFIED_EMP_CODE, ");
		sql.append("  tm.VERSION ");
		sql.append("  FROM TT_PB_SHEDULE_BY_MONTH  tm,");
		sql.append("  TM_DEPARTMENT d ,TM_OSS_EMPLOYEE em where d.dept_id = tm.dept_id ");
		sql.append("  and tm.emp_code = em.emp_code ");
		sql.append("  and tm.DEPT_ID  = ?  ");
		if (!StringUtils.isBlank(dto.getYm())) {
			sql.append(" and tm.ym = '" + dto.getYm() + "'");
		}
		Object[] args = { dto.getDeptId() };
		return this.fetchPage(sql.toString(), args, pageSize, pageIndex, SchedulMgt.class);
	}

	// 根据deptid,empcode查找员工
	public OutEmployee getEmpByCode(Long deptId, String empCode) {
		String sql = "SELECT T.EMP_CODE," 
				+ "       T.EMP_NAME," 
				+ "       T.DEPT_ID," 
				+ "       T.GROUP_ID,"
				+"		  T.SF_DATE AS sf_time,"
				+ "  	greatest(nvl(transfer_date, sysdate - 10000), nvl(date_from, sysdate - 10000), nvl(sf_date, sysdate - 10000)) sf_date,T.dimission_dt,"
				+ "       T.WORK_TYPE," 
				+ "       G.DISABLE_DT" 
				+ "  FROM TM_OSS_EMPLOYEE T, TM_PB_GROUP_INFO G" 
				+ " WHERE T.DEPT_ID = G.DEPT_ID(+)"
				+ "   AND T.GROUP_ID = G.GROUP_ID(+)" 
				+ "   AND T.EMP_CODE = ?" 
				+ "   AND T.DEPT_ID = ?";
		Object[] args = { empCode, deptId };
		List<OutEmployee> lst = this.queryForList(sql, args, OutEmployee.class);
		if (lst != null && lst.size() > 0) {
			return lst.get(0);
		}
		return null;
	}
	
	public OutEmployee checkExistByEmpByCode(Long deptId, String empCode) {
		String sql = "SELECT T.EMP_CODE " 
				+ "  FROM TM_OSS_EMPLOYEE T " 
				+ " WHERE T.EMP_CODE = ? " 
				+ "   AND T.DEPT_ID = ? "
				+ "   AND T.emp_post_type =1";
		Object[] args = { empCode, deptId };
		List<OutEmployee> lst = this.queryForList(sql, args, OutEmployee.class);
		if (lst != null && lst.size() > 0) {
			return lst.get(0);
		}
		return null;
	}

	// 根据deptid,scheid获取班别信息
	public List<SchedulingBase> getScheInfo(Long deptId, String scheCode, String ym) {
		String sql = " SELECT DEPT_ID, SCHEDULE_CODE, ENABLE_DT, DISABLE_DT ," 
				+"  T.Start1_Time , T.END1_TIME,T.START2_TIME ,T.END2_TIME, T.START3_TIME,T.END3_TIME " 
				+ " FROM TM_PB_SCHEDULE_BASE_INFO T" 
				+ " WHERE T.DEPT_ID = ?"
				+ " AND T.YM = ? "
				+ " AND T.SCHEDULE_CODE = ?  and CLASS_TYPE = '1'";
		Object[] args = { deptId, ym, scheCode };
		return this.queryForList(sql, args, SchedulingBase.class);
	}
	
	public List<SchedulingBase> getLastMonthLastDayScheInfo(Long deptId, String empCode, String sheduleDt) {
		String sql =  "select T.* from tt_pb_shedule_by_day d , TM_PB_SCHEDULE_BASE_INFO t  "
			     + "where t.dept_id = d.dept_id "
			     + "and to_char(d.shedule_dt,'YYYY-MM') = T.YM "
			     + "AND T.SCHEDULE_CODE = D.SHEDULE_CODE "
			     + "AND T.CLASS_TYPE = '1' "
			     + "AND d.shedule_dt = TO_DATE(?,'YYYY-MM-DD') "
			     + "AND D.DEPT_ID = ? "
			     + "AND D.EMP_CODE = ? ";
		Object[] args = { sheduleDt, deptId, empCode };
		return this.queryForList(sql, args, SchedulingBase.class);
	}

	// 获取接收邮件的员工
	public List<OutEmployee> getUnFinishedEmp(final String ym) {
		return this.getJdbcTemplate().execute("call PKG_OSS_SCHE_PROCESS.SPT_GET_SCHE_UNFIN_EMP_LIST(?,?)", new CallableStatementCallback<List<OutEmployee>>() {
			public List<OutEmployee> doInCallableStatement(CallableStatement ps) throws SQLException, DataAccessException {
				ResultSet rs = null;
				try {
					int outParameter = 2;
					ps.setString(1, ym);
					ps.registerOutParameter(outParameter, OracleTypes.CURSOR);
					ps.execute();

					List<OutEmployee> data = new ArrayList<OutEmployee>();
					if (ps.getObject(outParameter) == null) {
						return data;
					}
					rs = (ResultSet) ps.getObject(outParameter);
					while (rs.next()) {
						OutEmployee emp = new OutEmployee();
						emp.setDeptId(rs.getLong("DEPT_ID"));
						emp.setEmpCode(rs.getString("EMP_CODE"));
						emp.setEmail(rs.getString("EMP_EMAIL"));
						data.add(emp);
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

	// 判断该月当前网点下的所有员工有无排班
	public boolean allEmpIsHasSche(final Long deptId, final String ym) {
		return this.getJdbcTemplate().execute("{call PKG_OSS_SCHE_PROCESS.SPT_ALL_EMP_IS_HAS_SCHE(?,?,?)}", new CallableStatementCallback<Boolean>() {
			public Boolean doInCallableStatement(CallableStatement ps) throws SQLException, DataAccessException {
				int outParameter = 3;
				ps.setString(1, ym);
				ps.setLong(2, deptId);
				ps.registerOutParameter(outParameter, OracleTypes.NUMBER);
				ps.execute();
				int ct = ps.getInt(outParameter);
				if (ct > 0) {
					return false;
				} else {
					return true;
				}
			}
		});
	}

	// 如果网点下所有员工都排班，判断一个月该员工排满班
	public boolean allScheIsFinished(final Long deptId, final String ym) {
		return this.getJdbcTemplate().execute("{call PKG_OSS_SCHE_PROCESS.SPT_ALL_SCHE_IS_FINISHED(?,?,?)}", new CallableStatementCallback<Boolean>() {
			public Boolean doInCallableStatement(CallableStatement ps) throws SQLException, DataAccessException {
				int outParameter = 3;
				ps.setString(1, ym);
				ps.setLong(2, deptId);
				ps.registerOutParameter(outParameter, OracleTypes.NUMBER);
				ps.execute();
				int ct = ps.getInt(outParameter);
				if (ct > 0) {
					return false;
				} else {
					return true;
				}
			}
		});
	}

	public SchedulMgt findByCondition(Long deptid, String ym, String empcode) {
		String sql = "select id," 
				+ "       ym," 
				+ "       dept_id," 
				+ "       emp_code," 
				+ "       day1," 
				+ "       day2," 
				+ "       day3," 
				+ "       day4,"
				+ "       day5," 
				+ "       day6," 
				+ "       day7," 
				+ "       day8," 
				+ "       day9," 
				+ "       day10," 
				+ "       day11," 
				+ "       day12,"
				+ "       day13," 
				+ "       day14," 
				+ "       day15," 
				+ "       day16," 
				+ "       day17," 
				+ "       day18," 
				+ "       day19," 
				+ "       day20,"
				+ "       day21," 
				+ "       day22," 
				+ "       day23," 
				+ "       day24," 
				+ "       day25," 
				+ "       day26," 
				+ "       day27," 
				+ "       day28,"
				+ "       day29," 
				+ "       day30," 
				+ "       day31," 
				+ "       create_tm," 
				+ "       modified_tm," 
				+ "       create_emp_code,"
				+ "       modified_emp_code," 
				+ "       version" 
				+ "  from tt_pb_shedule_by_month t where t.dept_id = ? and t.ym = ? and t.emp_code = ?";
		List<SchedulMgt> list = super.queryForList(sql, new Object[] { deptid, ym, empcode }, SchedulMgt.class);

		return list == null || list.isEmpty() ? null : list.get(0);
	}

	public IPage<OutEmployee> findEmpPage(ScheduleDto dto, int pageSize, int pageIndex) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT E.EMP_ID, D.AREA_CODE, D.DEPT_NAME, E.EMP_CODE, E.EMP_NAME ")
		.append("FROM TM_OSS_EMPLOYEE E, TM_PB_GROUP_INFO G, TM_DEPARTMENT D ")
		.append("WHERE E.GROUP_ID = G.GROUP_ID(+)  AND E.EMP_POST_TYPE ='1' ")
		.append("AND E.DEPT_ID = G.DEPT_ID(+) ")
		.append("AND E.DEPT_ID = D.DEPT_ID ")
		.append("AND E.DEPT_ID = ? ")
		.append("AND (E.DIMISSION_DT > SYSDATE or E.dimission_dt is null) ");
		if (!StringUtils.isBlank(dto.getEmpCode())) {
			sb.append(" AND EMP_CODE LIKE '%").append(dto.getEmpCode()).append("%'");
		}
		if (!StringUtils.isBlank(dto.getEmpName())) {
			sb.append(" AND EMP_NAME LIKE '%").append(dto.getEmpName()).append("%'");
		}
		if (dto.getTeamId() != null) {
			sb.append(" AND G.GROUP_ID = ").append(dto.getTeamId());
		}
		Object[] args = { dto.getDeptId() };
		return this.fetchPage(sb.toString(), args, pageSize, pageIndex, OutEmployee.class);
	}

	@Override
	public List<MonthConfirmStatus> getConfirmList() {
		String sql = "select " + " *  from TT_PB_SCHE_CONFIRM ";
		List<MonthConfirmStatus> confirmStatus = super.queryForList(sql, null, MonthConfirmStatus.class);
		return confirmStatus;
	}

	@Override
	public boolean getToConfirmThe(Long deptId, String schedulingIds) {
		boolean toConfirmThe = true;
		String sql = " select tm.id from tt_pb_shedule_by_month_log t ," + "  tt_pb_shedule_by_month tm where"
				+ " t.ym = tm.ym and t.dept_id = tm.dept_id and t.emp_code = tm.emp_code" + " and t.dept_id = ? and tm.id = ?";
		String[] schedulingIdsArray = schedulingIds.replaceAll(",$", "").split(",", -1);
		for (String schedulingId : schedulingIdsArray) {
			Long schedulId = Long.parseLong(schedulingId);
			Object[] args = { deptId, schedulId };
			List<SchedulMgt> lst = this.queryForList(sql, args, SchedulMgt.class);
			toConfirmThe = !(lst != null && lst.size() > 0) && toConfirmThe;
		}
		return toConfirmThe;
	}

	@Override
	public List<OutEmployee> getNotSchedulEmployee(Long deptid, String ym) {
		String sql = " SELECT T.*  "
				+ "  FROM TM_OSS_EMPLOYEE T  "
				+ " WHERE T.DEPT_ID in(SELECT DEPT_ID"
				+ "          FROM TM_DEPARTMENT"
				+ "         WHERE DELETE_FLG = 0"
				+ "        CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE"
				+ "         START WITH DEPT_ID = ?) "
				+ "   AND T.EMP_POST_TYPE = '1' "
				+ "   AND T.EMP_CODE NOT IN (SELECT TMON.EMP_CODE  "
				+ "                            FROM TT_PB_SHEDULE_BY_MONTH TMON  "
				+ "                           WHERE  TMON.DEPT_ID = T.DEPT_ID"
				+ "                             AND TMON.YM = ? )  "
				+ "   AND (T.DIMISSION_DT > TO_DATE( ? ||'-01','YYYY-MM-DD')"
				+ " OR T.DIMISSION_DT IS NULL) ";
		Object[] args = { deptid,ym,ym};
		List<OutEmployee> lst = this.queryForList(sql, args, OutEmployee.class);
		return lst;
	}
	
	@Override
	public boolean queryModifyPermissions(Long userId) {
		String sql =  " SELECT count(*) "
				 +    "        FROM TS_USER        U, "
				 +    "             TS_USER_ROLE   UR, "
				 +    "             TS_ROLE_MODULE RM, "
				 +    "             TS_MODULE      M   "
				 +    "       WHERE U.USER_ID = UR.USER_ID "
				 +    "         AND UR.ROLE_ID = RM.ROLE_ID "
				 +    "         AND RM.MODULE_ID = M.MODULE_ID "
				 +    "         AND M.MODULE_CODE = 'modify_operation_historty_scheduling' "
				 +    "         and U.USER_ID = ? ";
		Object[] args = {userId};
		List<Integer> list = this.getJdbcTemplate().queryForList(sql, args,
				Integer.class);
		if (list != null && list.size() > 0) 
			return Integer.parseInt(list.get(0).toString()) > 0;
		return false;
	}

	@Override
	public boolean queryModifyMonthPermissions(Long userId) {
		String sql =  " SELECT count(*) "
				 +    "        FROM TS_USER        U, "
				 +    "             TS_USER_ROLE   UR, "
				 +    "             TS_ROLE_MODULE RM, "
				 +    "             TS_MODULE      M   "
				 +    "       WHERE U.USER_ID = UR.USER_ID "
				 +    "         AND UR.ROLE_ID = RM.ROLE_ID "
				 +    "         AND RM.MODULE_ID = M.MODULE_ID "
				 +    "         AND M.MODULE_CODE = 'historyMonthModifySchedule' "
				 +    "         and U.USER_ID = ? ";
		Object[] args = {userId};
		List<Integer> list = this.getJdbcTemplate().queryForList(sql, args,
				Integer.class);
		if (list != null && list.size() > 0) 
			return Integer.parseInt(list.get(0).toString()) > 0;
		return false;
	}
}