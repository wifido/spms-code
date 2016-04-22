/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-08     houjingyu       创建
 **********************************************/

package com.sf.module.operation.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;

import com.sf.framework.base.IPage;
import com.sf.module.operation.action.dto.ProcessDto;
import com.sf.module.operation.domain.OutEmployee;
import com.sf.module.operation.domain.ProcessConfirmStatus;
import com.sf.module.operation.domain.ProcessDetail;
import com.sf.module.operation.domain.ProcessMgt;
import com.sf.module.operation.util.CommonUtil;

/**
 * 
 * 月度工序实体的JdbcDao实现类
 * 
 * @author houjingyu 2014-07-08
 * 
 */
public class ProcessMgtJdbcDao extends OssJdbcDao implements IProcessMgtJdbcDao {

	public IPage<ProcessMgt> findPage(Long userId, ProcessDto dto, int pageSize, int pageIndex) {
		String sql = 				" SELECT P.ID,"
				+ "                P.YM,"
				+ "                P.DEPT_ID,"
				+ "                D.DEPT_CODE,"
				+ "                D.AREA_CODE,"
				+ "                P.EMP_CODE,"
				+ "                E.EMP_NAME,"
				+ "                e.TRANSFER_DATE,"
				+ "                e.SF_DATE,"
				+ "                e.DATE_FROM,"
				+ "                e.EMP_POST_TYPE,"
				+ "                e.LAST_ZNO,"
				+ "                e.DIMISSION_DT,"
				+ "                decode_emp_worktype(E.work_type) work_Type_Str,"
				+ "                E.GROUP_ID,"
				+ "                E.BETWEEN_DATE,"
				+ "                P.DAY1,"
				+ "                P.DAY2,"
				+ "                P.DAY3,"
				+ "                P.DAY4,"
				+ "                P.DAY5,"
				+ "                P.DAY6,"
				+ "                P.DAY7,"
				+ "                P.DAY8,"
				+ "                P.DAY9,"
				+ "                P.DAY10,"
				+ "                P.DAY11,"
				+ "                P.DAY12,"
				+ "                P.DAY13,"
				+ "                P.DAY14,"
				+ "                P.DAY15,"
				+ "                P.DAY16,"
				+ "                P.DAY17,"
				+ "                P.DAY18,"
				+ "                P.DAY19,"
				+ "                P.DAY20,"
				+ "                P.DAY21,"
				+ "                P.DAY22,"
				+ "                P.DAY23,"
				+ "                P.DAY24,"
				+ "                P.DAY25,"
				+ "                P.DAY26,"
				+ "                P.DAY27,"
				+ "                P.DAY28,"
				+ "                P.DAY29,"
				+ "                P.DAY30,"
				+ "                P.DAY31,"
				+ "                P.VERSION,"
				+ "                p.COMMIT_STATUS"
				+ "           FROM TT_PB_PROCESS_BY_MONTH P,"
				+ "                TM_DEPARTMENT D,"
				+ "                (SELECT PD.PROCESS_MON_ID,"
				+ "                        MAX(SD.EMP_NAME) EMP_NAME,"
				+ "                        MAX(SD.WORK_TYPE) WORK_TYPE,"
				+ "                        MAX(SD.GROUP_ID) GROUP_ID,"
				+ "                        max(sd.TRANSFER_DATE) TRANSFER_DATE,"
				+ "                        max(sd.SF_DATE) SF_DATE,"
				+ "                        MAX(sd.LAST_ZNO) LAST_ZNO,"
				+ "                        MAX(sd.DATE_FROM) DATE_FROM,"
				+ "                        MAX(sd.EMP_POST_TYPE) EMP_POST_TYPE,"
				+ "                        MAX(sd.DIMISSION_DT) DIMISSION_DT,"
				+ "                        MAX(NEED_DAYS) NEED_DAYS,"
				+ "                        COUNT(1) ACTUAL_DAYS,"
				+ "                        MAX(BETWEEN_DATE) BETWEEN_DATE"
				+ "                   FROM TT_PB_SCHE_CONFIRM C,"
				+ "                        TT_PB_SHEDULE_BY_MONTH M,"
				+ "                        (SELECT A.DEPT_ID,"
				+ "                                A.EMP_CODE,"
				+ "                                A.SHEDULE_DT,"
				+ "                                A.SHEDULE_MON_ID,"
				+ "                                B.EMP_NAME,"
				+ "                                B.WORK_TYPE,"
				+ "                                B.TRANSFER_DATE,"
				+ "                                B.SF_DATE,"
				+ "                                B.DATE_FROM ,"
				+ "                                B.LAST_ZNO ,"
				+ "                                B.EMP_POST_TYPE,"
				+ "                                B.DIMISSION_DT,"
				+ "                                B.GROUP_ID,"
				+ "                                (B.END_DATE - B.START_DATE + 1) NEED_DAYS,"
				+ "                                TO_CHAR(B.START_DATE, 'YYYY-MM-DD') || '~' ||"
				+ "                                TO_CHAR(B.END_DATE, 'YYYY-MM-DD') BETWEEN_DATE,"
				+ "                                B.START_DATE,"
				+ "                                B.END_DATE"
				+ "                           FROM TT_PB_SHEDULE_BY_DAY A,"
				+ "                                (SELECT DEPT_ID,"
				+ "                                        EMP_CODE,"
				+ "                                        EMP_NAME,"
				+ "                                        WORK_TYPE,"
				+ "                                        TRANSFER_DATE,"
				+ "                                        SF_DATE,"
				+ "                                        DATE_FROM ,"
				+ "                                        LAST_ZNO ,"
				+ "                                        EMP_POST_TYPE,"
				+ "                                        DIMISSION_DT,"
				+ "                                        GROUP_ID,"
				+ "                                        YM,"
				+ "                                        TO_DATE(SUBSTR(BETWEEN_DATE, 0, 10),"
				+ "                                                'YYYY-MM-DD') START_DATE,"
				+ "                                        TO_DATE(SUBSTR(BETWEEN_DATE, 12, 10),"
				+ "                                                'YYYY-MM-DD') END_DATE,"
				+ "                                        BETWEEN_DATE"
				+ "                                   FROM (SELECT DEPT_ID,"
				+ "                                                EMP_CODE,"
				+ "                                                max(EM.TRANSFER_DATE) TRANSFER_DATE,"
				+ "                                                max(EM.SF_DATE) SF_DATE,"
				+ "                                                MAX(EM.DATE_FROM) DATE_FROM,"
				+ "                                                MAX(EM.EMP_POST_TYPE) EMP_POST_TYPE,"
				+ "                                                MAX(EM.DIMISSION_DT) DIMISSION_DT,"
				+ "                                                MAX(EM.LAST_ZNO) LAST_ZNO,"
				+ "                                                YM,"
				+ "                                                MAX(EMP_NAME) EMP_NAME,"
				+ "                                                MAX(WORK_TYPE) WORK_TYPE, "
				+ "                                                MAX(GROUP_ID) GROUP_ID,"
				+ "                                                MAX(CASE"
				+ "                                                      WHEN DIMISSION_DT_TEMP <= FIRST_DAY THEN"
				+ "                                                       TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||"
				+ "                                                       TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD')"
				+ "                                                      WHEN  DIMISSION_DT_TEMP > FIRST_DAY AND"
				+ "                                                           DIMISSION_DT_TEMP <= LAST_DAY THEN"
				+ "                                                       TO_CHAR(FIRST_DAY, 'YYYY-MM-DD') || '~' ||"
				+ "                                                       TO_CHAR(DIMISSION_DT_TEMP - 1,"
				+ "                                                               'YYYY-MM-DD')"
				+ "                                                      WHEN   DIMISSION_DT_TEMP > LAST_DAY THEN"
				+ "                                                       TO_CHAR(FIRST_DAY, 'YYYY-MM-DD') || '~' ||"
				+ "                                                       TO_CHAR(LAST_DAY, 'YYYY-MM-DD')"
				+ "                                                      END) BETWEEN_DATE"
				+ "                                           FROM (SELECT MON.DEPT_ID,"
				+ "                                                        MON.YM YM,"
				+ "                                                        EM.EMP_CODE,"
				+ "                                                        EM.EMP_NAME,"
				+ "                                                        EM.WORK_TYPE, "
				+ "                                                        EM.GROUP_ID,"
				+ "                                                        EM.DIMISSION_DT,"
				+ "                                                        EM.TRANSFER_DATE,"
				+ "                                                        EM.LAST_ZNO,"
				+ "                                                        EM.SF_DATE,"
				+ "                                                        EM.DATE_FROM,"
				+ "                                                        EM.EMP_POST_TYPE,"
				+ "                                                        TRUNC(TO_DATE(MON.YM || '-01',"
				+ "                                                                      'YYYY-MM-DD'),"
				+ "                                                              'MM') FIRST_DAY,"
				+ "                                                        NVL(EM.DIMISSION_DT,"
				+ "                                                            TO_DATE('2114-01-01',"
				+ "                                                                    'YYYY-MM-DD')) DIMISSION_DT_TEMP,"
				+ "                                                        LAST_DAY(TO_DATE(MON.YM || '-01',"
				+ "                                                                         'YYYY-MM-DD')) LAST_DAY"
				+ "                                                   FROM TM_OSS_EMPLOYEE EM,"
				+ "                                                        TM_PB_GROUP_INFO G,"
				+ "                                                        (SELECT YM, DEPT_ID, EMP_CODE"
				+ "                                                           FROM TT_PB_SHEDULE_BY_MONTH";
		if (StringUtils.isBlank(dto.getYm())) {
			sql = sql + " ) MON";
		} else {
			sql = sql + " WHERE YM = '" + dto.getYm() + "') MON";
		}
		sql = sql + "                                         WHERE EM.GROUP_ID = G.GROUP_ID(+)"
				+ "                                           AND EM.EMP_CODE = MON.EMP_CODE";

		if (CommonUtil.isAdmin(userId)) {
			sql = sql + " AND mon.DEPT_ID IN (SELECT DEPT_ID  FROM TM_DEPARTMENT WHERE DELETE_FLG = 0 and DEPT_ID = ?)) em ";
		} else {
			sql = sql + " AND mon.DEPT_ID IN (SELECT D.DEPT_ID FROM (SELECT DEPT_ID FROM TM_DEPARTMENT "
					+ "  WHERE DELETE_FLG = 0  and DEPT_ID = ?) D, TS_USER_DEPT UD WHERE D.DEPT_ID = UD.DEPT_ID AND UD.USER_ID =" + userId + ")) em ";
		}
		sql = sql + "                                 GROUP BY EMP_CODE, DEPT_ID, YM)) B" 
		+ "                 WHERE A.DEPT_ID = B.DEPT_ID"
				+ "                   AND A.EMP_CODE = B.EMP_CODE" 
				+ "                   AND TO_CHAR(A.SHEDULE_DT, 'YYYY-MM') = B.YM"
				+ "                   AND A.SHEDULE_DT >= START_DATE"
				+ "                   AND A.SHEDULE_DT <= END_DATE) SD,"
				+ "               TT_PB_PROCESS_BY_DAY PD" 
				+ "         WHERE C.DEPT_ID = M.DEPT_ID" 
				+ "           AND C.YM = M.YM"
				+ "           AND M.ID = SD.SHEDULE_MON_ID" 
				+ "           AND SD.DEPT_ID = PD.DEPT_ID" 
				+ "           AND SD.EMP_CODE = PD.EMP_CODE"
				+ "           AND SD.SHEDULE_DT = PD.PROCESS_DT" 
				+ "         GROUP BY PD.PROCESS_MON_ID) E" 
				+ " WHERE P.ID = E.PROCESS_MON_ID"
				+ "   AND P.DEPT_ID = D.DEPT_ID";
		if (!StringUtils.isBlank(dto.getEmpCode())) {
			sql = sql + " AND P.EMP_CODE LIKE '%" + dto.getEmpCode() + "%'";
		}
		if (!StringUtils.isBlank(dto.getEmpName())) {
			sql = sql + " AND E.EMP_NAME LIKE '%" + dto.getEmpName() + "%'";
		}
		if (dto.getTeamId() != null) {
			sql = sql + " AND E.GROUP_ID = " + dto.getTeamId();
		}
		if (dto.getStatus() != null && dto.getStatus() != 3) {
			String[] whereStr = { "", " AND E.ACTUAL_DAYS = E.NEED_DAYS", " AND E.ACTUAL_DAYS < E.NEED_DAYS", "", " and COMMIT_STATUS = 1",
					" and COMMIT_STATUS = 0" };
			sql = sql + whereStr[dto.getStatus()];
		}
		if (!StringUtils.isBlank(dto.getProcessCode())) {
			sql = sql + " AND P.ID IN (SELECT PROCESS_MON_ID FROM TT_PB_PROCESS_BY_DAY Y" + " WHERE PROCESS_CODE = '" + dto.getProcessCode()+ "')";
		}
		Object[] args = { dto.getDeptId() };
		return this.fetchPage(sql, args, pageSize, pageIndex, ProcessMgt.class);
	}

	public int getMgtCount(Long userId, ProcessDto dto) {
		String sql = "SELECT COUNT(P.ID)"
				+ "  FROM TT_PB_PROCESS_BY_MONTH P,"
				+ "       TM_DEPARTMENT D,"
				+ "       (SELECT PD.PROCESS_MON_ID,"
				+ "               MAX(SD.EMP_NAME) EMP_NAME,"
				+ "               MAX(SD.WORK_TYPE) WORK_TYPE,"
				+ "               MAX(SD.GROUP_ID) GROUP_ID,"
				+ "               MAX(NEED_DAYS) NEED_DAYS,"
				+ "               COUNT(1) ACTUAL_DAYS,"
				+ "               MAX(BETWEEN_DATE) BETWEEN_DATE"
				+ "          FROM TT_PB_SCHE_CONFIRM C,"
				+ "               TT_PB_SHEDULE_BY_MONTH M,"
				+ "               (SELECT A.DEPT_ID,"
				+ "                       A.EMP_CODE,"
				+ "                       A.SHEDULE_DT,"
				+ "                       A.SHEDULE_MON_ID,"
				+ "                       B.EMP_NAME,"
				+ "                       B.WORK_TYPE,"
				+ "                       B.GROUP_ID,"
				+ "                       (B.END_DATE - B.START_DATE + 1) NEED_DAYS,"
				+ "                       TO_CHAR(B.START_DATE, 'YYYY-MM-DD') || '~' ||"
				+ "                       TO_CHAR(B.END_DATE, 'YYYY-MM-DD') BETWEEN_DATE,"
				+ "                       B.START_DATE,"
				+ "                       B.END_DATE"
				+ "                  FROM TT_PB_SHEDULE_BY_DAY A,"
				+ "                       (SELECT DEPT_ID,"
				+ "                               EMP_CODE,"
				+ "                               EMP_NAME,"
				+ "                               WORK_TYPE,"
				+ "                               GROUP_ID,"
				+ "                               YM,"
				+ "                               TO_DATE(SUBSTR(BETWEEN_DATE, 0, 10),"
				+ "                                       'YYYY-MM-DD') START_DATE,"
				+ "                               TO_DATE(SUBSTR(BETWEEN_DATE, 12, 10),"
				+ "                                       'YYYY-MM-DD') END_DATE,"
				+ "                               BETWEEN_DATE"
				+ "                          FROM (SELECT DEPT_ID,"
				+ "                                       EMP_CODE,"
				+ "                                       YM,"
				+ "                                       MAX(EMP_NAME) EMP_NAME,"
				+ "                                       MAX(WORK_TYPE) WORK_TYPE,"
				+ "                                       MAX(GROUP_ID) GROUP_ID,"
				+ "                                       MAX(CASE"
				+ "                                             WHEN START_TM IS NULL AND"
				+ "                                                  END_TM IS NULL AND"
				+ "                                                  DIMISSION_DT_TEMP <= FIRST_DAY THEN"
				+ "                                              TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||"
				+ "                                              TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD')"
				+ "                                             WHEN START_TM IS NULL AND"
				+ "                                                  END_TM IS NULL AND"
				+ "                                                  DIMISSION_DT_TEMP > FIRST_DAY AND"
				+ "                                                  DIMISSION_DT_TEMP <= LAST_DAY THEN"
				+ "                                              TO_CHAR(FIRST_DAY, 'YYYY-MM-DD') || '~' ||"
				+ "                                              TO_CHAR(DIMISSION_DT_TEMP - 1,"
				+ "                                                      'YYYY-MM-DD')"
				+ "                                             WHEN START_TM IS NULL AND"
				+ "                                                  END_TM IS NULL AND"
				+ "                                                  DIMISSION_DT_TEMP > LAST_DAY THEN"
				+ "                                              TO_CHAR(FIRST_DAY, 'YYYY-MM-DD') || '~' ||"
				+ "                                              TO_CHAR(LAST_DAY, 'YYYY-MM-DD')"
				+ "                                             WHEN START_TM IS NOT NULL AND"
				+ "                                                  DIMISSION_DT_TEMP > FIRST_DAY AND"
				+ "                                                  END_TM > FIRST_DAY THEN"
				+ "                                              TO_CHAR(GREATEST(FIRST_DAY, START_TM),"
				+ "                                                      'YYYY-MM-DD') || '~' ||"
				+ "                                              TO_CHAR(DECODE(LEAST(LAST_DAY,"
				+ "                                                                   DIMISSION_DT_TEMP,"
				+ "                                                                   END_TM),"
				+ "                                                             LAST_DAY,"
				+ "                                                             LAST_DAY,"
				+ "                                                             LEAST(LAST_DAY,"
				+ "                                                                   DIMISSION_DT_TEMP,"
				+ "                                                                   END_TM) - 1),"
				+ "                                                      'YYYY-MM-DD')"
				+ "                                             WHEN START_TM IS NOT NULL AND"
				+ "                                                  DIMISSION_DT_TEMP > LAST_DAY AND"
				+ "                                                  END_TM > LAST_DAY THEN"
				+ "                                              TO_CHAR(GREATEST(FIRST_DAY, START_TM),"
				+ "                                                      'DD') || '~' ||"
				+ "                                              TO_CHAR(LAST_DAY, 'DD')"
				+ "                                           "
				+ "                                             WHEN START_TM IS NOT NULL AND"
				+ "                                                  END_TM IS NOT NULL AND"
				+ "                                                  END_TM <= FIRST_DAY THEN"
				+ "                                              TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||"
				+ "                                              TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD')"
				+ "                                           "
				+ "                                             WHEN START_TM IS NOT NULL AND"
				+ "                                                  END_TM IS NOT NULL AND"
				+ "                                                  DIMISSION_DT_TEMP <= FIRST_DAY THEN"
				+ "                                              TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||"
				+ "                                              TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD')"
				+ "                                           "
				+ "                                             WHEN START_TM IS NOT NULL AND"
				+ "                                                  DIMISSION_DT_TEMP <= START_TM THEN"
				+ "                                              TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||"
				+ "                                              TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD')"
				+ "                                           "
				+ "                                             WHEN START_TM IS NOT NULL AND"
				+ "                                                  LAST_DAY <= START_TM AND"
				+ "                                                  START_TM <= DIMISSION_DT_TEMP THEN"
				+ "                                              TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD') || '~' ||"
				+ "                                              TO_CHAR(FIRST_DAY - 1, 'YYYY-MM-DD')"
				+ "                                           END) BETWEEN_DATE"
				+ "                                  FROM (SELECT EM.DEPT_ID,"
				+ "                                               MON.YM YM,"
				+ "                                               EM.EMP_CODE,"
				+ "                                               EM.EMP_NAME,"
				+ "                                               EM.WORK_TYPE,"
				+ "                                               EM.GROUP_ID,"
				+ "                                               EM.DIMISSION_DT,"
				+ "                                               CHG.CHANGE_DEPT_ID,"
				+ "                                               START_TM,"
				+ "                                               END_TM,"
				+ "                                               TRUNC(TO_DATE(MON.YM || '-01',"
				+ "                                                             'YYYY-MM-DD'),"
				+ "                                                     'MM') FIRST_DAY,"
				+ "                                               NVL(EM.DIMISSION_DT,"
				+ "                                                   TO_DATE('2114-01-01',"
				+ "                                                           'YYYY-MM-DD')) DIMISSION_DT_TEMP,"
				+ "                                               LAST_DAY(TO_DATE(MON.YM || '-01',"
				+ "                                                                'YYYY-MM-DD')) LAST_DAY"
				+ "                                          FROM TM_OSS_EMPLOYEE EM,"
				+ "                                               TM_PB_GROUP_INFO G,"
				+ "                                               (SELECT CHANGE_DEPT_ID,"
				+ "                                                       EMP_CODE,"
				+ "                                                       CHANGE_DEPT_CODE,"
				+ "                                                       START_TM,"
				+ "                                                       NVL(END_TM,"
				+ "                                                           TO_DATE('2114-01-01',"
				+ "                                                                   'YYYY-MM-DD')) END_TM"
				+ "                                                  FROM (SELECT DEP.DEPT_ID CHANGE_DEPT_ID,"
				+ "                                                               CH.EMP_CODE,"
				+ "                                                               CH.DEPT_CODE CHANGE_DEPT_CODE,"
				+ "                                                               CH.CHANGE_ZONE_TM START_TM,"
				+ "                                                               LEAD(CH.CHANGE_ZONE_TM) OVER(PARTITION BY CH.EMP_CODE ORDER BY CH.CHANGE_ZONE_TM) END_TM"
				+ "                                                          FROM TI_OSS_HR_EMP_NEW_CHANGEDEPT CH,"
				+ "                                                               TM_DEPARTMENT                DEP"
				+ "                                                         WHERE CH.DEPT_CODE ="
				+ "                                                               DEP.DEPT_CODE"
				+ "                                                           AND CH.EMP_STUS = 2)) CHG,"
				+ "                                               (SELECT YM, DEPT_ID, EMP_CODE"
				+ "                                                  FROM TT_PB_SHEDULE_BY_MONTH";
		if (StringUtils.isBlank(dto.getYm())) {
			sql = sql + " ) MON";
		} else {
			sql = sql + " WHERE YM = '" + dto.getYm() + "') MON";
		}
		sql = sql + "                                         WHERE EM.GROUP_ID = G.GROUP_ID(+)"
				+ "                                           AND EM.EMP_CODE = MON.EMP_CODE"
				+ "                                           AND MON.EMP_CODE = CHG.EMP_CODE(+)"
				+ "                                           AND MON.DEPT_ID =" + "                                               CHG.CHANGE_DEPT_ID(+)"
				+ "                                           AND MON.EMP_CODE = CHG.EMP_CODE(+)";

		if (CommonUtil.isAdmin(userId)) {
			sql = sql + " AND EM.DEPT_ID IN (SELECT DEPT_ID  FROM TM_DEPARTMENT WHERE DELETE_FLG = 0 START WITH DEPT_ID = ?"
					+ " CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE))";
		} else {
			sql = sql + " AND EM.DEPT_ID IN (SELECT D.DEPT_ID FROM (SELECT DEPT_ID FROM TM_DEPARTMENT "
					+ "  WHERE DELETE_FLG = 0 CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE "
					+ "  START WITH DEPT_ID = ?) D, TS_USER_DEPT UD WHERE D.DEPT_ID = UD.DEPT_ID AND UD.USER_ID =" 
					+ userId + "))";
		}
		sql = sql + "                                 GROUP BY EMP_CODE, DEPT_ID, YM)) B" 
		+ "                 WHERE A.DEPT_ID = B.DEPT_ID"
				+ "                   AND A.EMP_CODE = B.EMP_CODE" 
				+ "                   AND TO_CHAR(A.SHEDULE_DT, 'YYYY-MM') = B.YM"
				+ "                   AND A.SHEDULE_DT >= START_DATE" 
				+ "                   AND A.SHEDULE_DT <= END_DATE) SD,"
				+ "               TT_PB_PROCESS_BY_DAY PD" 
				+ "         WHERE C.DEPT_ID = M.DEPT_ID" 
				+ "           AND C.YM = M.YM"
				+ "           AND M.ID = SD.SHEDULE_MON_ID" 
				+ "           AND SD.DEPT_ID = PD.DEPT_ID" 
				+ "           AND SD.EMP_CODE = PD.EMP_CODE"
				+ "           AND SD.SHEDULE_DT = PD.PROCESS_DT" 
				+ "         GROUP BY PD.PROCESS_MON_ID) E"
				+ " WHERE P.ID = E.PROCESS_MON_ID"
				+ "   AND P.DEPT_ID = D.DEPT_ID";
		if (!StringUtils.isBlank(dto.getEmpCode())) {
			sql = sql + " AND P.EMP_CODE LIKE '%" + dto.getEmpCode() + "%'";
		}
		if (!StringUtils.isBlank(dto.getEmpName())) {
			sql = sql + " AND E.EMP_NAME LIKE '%" + dto.getEmpName() + "%'";
		}
		if (dto.getTeamId() != null) {
			sql = sql + " AND E.GROUP_ID = " + dto.getTeamId();
		}
		if (dto.getStatus() != null && dto.getStatus() != 3) {
			if (dto.getStatus() == 1) {
				sql = sql + " AND E.ACTUAL_DAYS = E.NEED_DAYS";
			} else {
				sql = sql + " AND E.ACTUAL_DAYS < E.NEED_DAYS";
			}
		}
		if (!StringUtils.isBlank(dto.getProcessCode())) {
			sql = sql + " AND P.ID IN (SELECT PROCESS_MON_ID FROM TT_PB_PROCESS_BY_DAY Y" + " WHERE PROCESS_CODE = '" + dto.getProcessCode()
					+ "' GROUP BY PROCESS_MON_ID)";
		}
		Object[] args = { dto.getDeptId() };
		return this.getJdbcTemplate().queryForObject(sql, args, Integer.class);
	}

	// 判断该月当前网点下的所有确认后的排班有无工序
	public boolean allScheIsHasProcess(final Long deptId, final String ym) {
		return this.getJdbcTemplate().execute("CALL PKG_OSS_SCHE_PROCESS.SPT_ALL_SCHE_IS_HAS_PROCESS(?,?,?)", new CallableStatementCallback<Boolean>() {
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

	// 获取接收邮件的员工
	public List<OutEmployee> getUnFinishedEmp(final Long deptid, final String ym) {
		return this.getJdbcTemplate().execute("CALL PKG_OSS_SCHE_PROCESS.SPT_GET_PROCESS_UNFIN_EMP_LIST(?,?)",
				new CallableStatementCallback<List<OutEmployee>>() {
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

	public ProcessMgt findByCondition(Long deptid, String ym, String empcode) {
		String sql = "SELECT ID," 
				+ "       YM," 
				+ "       DEPT_ID," 
				+ "       EMP_CODE," 
				+ "       DAY1," 
				+ "       DAY2," 
				+ "       DAY3," 
				+ "       DAY4,"
				+ "       DAY5,"
				+ "       DAY6," 
				+ "       DAY7," 
				+ "       DAY8," 
				+ "       DAY9," 
				+ "       DAY10,"
				+ "       DAY11," 
				+ "       DAY12,"
				+ "       DAY13," 
				+ "       DAY14," 
				+ "       DAY15," 
				+ "       DAY16," 
				+ "       DAY17," 
				+ "       DAY18," 
				+ "       DAY19," 
				+ "       DAY20,"
				+ "       DAY21," 
				+ "       DAY22," 
				+ "       DAY23," 
				+ "       DAY24," 
				+ "       DAY25," 
				+ "       DAY26," 
				+ "       DAY27," 
				+ "       DAY28,"
				+ "       DAY29," 
				+ "       DAY30," 
				+ "       DAY31," 
				+ "       CREATE_TM," 
				+ "       MODIFIED_TM,"
				+ "       CREATE_EMP_CODE,"
				+ "       MODIFIED_EMP_CODE," 
				+ "       VERSION"
				+ "  FROM TT_PB_PROCESS_BY_MONTH T" 
				+ " WHERE T.DEPT_ID = ?" 
				+ "   AND T.YM = ?"
				+ "   AND T.EMP_CODE = ?";
		List<ProcessMgt> list = super.queryForList(sql, new Object[] { deptid, ym, empcode }, ProcessMgt.class);
		return list == null || list.isEmpty() ? null : list.get(0);
	}

	public ProcessDetail findBy(Long deptid, Date dt, String empcode) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ID, PROCESS_CODE, DEPT_ID, PROCESS_DT, EMP_CODE, PROCESS_MON_ID FROM TT_PB_PROCESS_BY_DAY").append(
				" WHERE EMP_CODE=? AND PROCESS_DT=? AND DEPT_ID=? ");
		List<ProcessDetail> list = super.queryForList(sb.toString(), new Object[] { empcode, dt, deptid }, ProcessDetail.class);
		return list == null || list.isEmpty() ? null : list.get(0);
	}

	public int getExportProcessMgtCount(ProcessDto dto) {
		String sql = "    select ("
				+  "		SELECT COUNT(1)"
				+  "          FROM (select MAX(L.ID), L.EMP_CODE, L.DEPT_ID, L.YM"
				+  "                  from TT_PB_PROCESS_BY_MONTH_LOG L";
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
				+ "              from TT_PB_PROCESS_BY_MONTH   "
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

	public IPage<ProcessMgt> importConfirmProcessMgt(ProcessDto dto, int pageSize, int pageIndex) {
		String sql = "select emp.emp_name,\n" 
				+ "       decode_emp_worktype(emp.work_type) work_Type_Str,\n" 
				+ "       dept.dept_code,\n" 
				+ "       dept.area_code,\n" 
				+ "       t.*\n"
				+ "  from tt_pb_process_by_month_log t,\n" 
				+ "       tm_oss_employee            emp,\n" 
				+ "       tm_department              dept\n"
				+ " where t.emp_code = emp.emp_code\n" 
				+ "   and t.dept_id = dept.dept_id \n"
				+ "   and t.dept_id in (SELECT DEPT_ID FROM TM_DEPARTMENT WHERE DELETE_FLG=0 \n " 
				+ " and DEPT_ID=?  ) \n"
				+ "	and id in (select t.g_id from "
				+ " (select max(g.id) g_id,g.emp_code, g.ym, g.dept_id"
				+ "      from tt_pb_process_by_month_log g"
				+ "      group by g.emp_code, g.ym, g.dept_id"
				+ "      )t) ";
		if (StringUtils.isNotEmpty(dto.getYm())) {
			sql = sql + "   and t.ym = '" + dto.getYm() + "'";
		}
		sql = sql + "    order by t.modified_tm ";
		Object[] args = { dto.getDeptId() };
		return this.fetchPage(sql.toString(), args, pageSize, pageIndex, ProcessMgt.class);
	}

	public IPage<ProcessMgt> importNoConfirmProcessMgt(ProcessDto dto, int pageSize, int pageIndex) {
		String sql = "select emp.emp_name,\n" 
				+ "       decode_emp_worktype(emp.work_type) work_Type_Str,\n" 
				+ "       dept.dept_code,\n" 
				+ "       dept.area_code,\n" 
				+ "       t.*\n"
				+ "  from tt_pb_process_by_month t,\n" 
				+ "       tm_oss_employee            emp,\n" 
				+ "       tm_department              dept\n"
				+ " where t.emp_code = emp.emp_code\n" 
				+ "   and t.dept_id = dept.dept_id \n"
				+ "   and t.dept_id in (SELECT DEPT_ID FROM TM_DEPARTMENT WHERE DELETE_FLG=0 \n " 
				+ " and DEPT_ID=?  )  and t.COMMIT_STATUS = 0 \n";
		if (StringUtils.isNotEmpty(dto.getYm())) {
			sql = sql + "   and t.ym = '" + dto.getYm() + "'";
		}
		sql = sql + "    order by t.modified_tm ";
		Object[] args = { dto.getDeptId() };
		return this.fetchPage(sql.toString(), args, pageSize, pageIndex, ProcessMgt.class);
	}

	@Override
	public List<ProcessConfirmStatus> getConfirmList() {
		String sql = "select " + " *  from tt_pb_process_confirm ";
		List<ProcessConfirmStatus> confirmStatus = super.queryForList(sql, null, ProcessConfirmStatus.class);
		return confirmStatus;
	}
}