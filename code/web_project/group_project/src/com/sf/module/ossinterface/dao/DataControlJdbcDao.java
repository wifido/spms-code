package com.sf.module.ossinterface.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.sf.framework.server.base.dao.BaseJdbcDao;

public class DataControlJdbcDao extends BaseJdbcDao implements
		IDataControlJdbcDao {


	public int checkReSendByDay() {
		Integer count = (Integer) super
				.getJdbcTemplate()
				.execute(
						"update ts_oss_datacontrol set starttm=sysdate,state=0 where task=2 and  trunc(sysdate) > trunc(starttm)and times<=3",
						//"update ts_oss_datacontrol set state=0,TIMES=TIMES+1 where task=2 and  trunc(sysdate) > trunc(starttm) and times<=3",
						new PreparedStatementCallback() {
							public Object doInPreparedStatement(
									PreparedStatement ps) throws SQLException,
									DataAccessException {
								int count = ps.executeUpdate();
								return count;
							}
						});
		return count.intValue();
	}


	public void writeSuccessStatus() {

		Integer count = (Integer) super
				.getJdbcTemplate()
				.execute(
						//"update ts_oss_datacontrol set endtm=sysdate,state=2 where task=2",
						"update ts_oss_datacontrol set endtm=sysdate,state=2,TIMES=0 where task=2",
						new PreparedStatementCallback() {
							public Object doInPreparedStatement(
									PreparedStatement ps) throws SQLException,
									DataAccessException {
								int count = ps.executeUpdate();
								return count;
							}
						});

	}

	private static String calculateInvockSql = "{call PKG_OSS_INTERFACE_PROCESS.STP_OSS_HR_ALL_OR_UPDATE(?,?)}";

	public void calculteOssEmp(final String dataType,final String JOURANLID) {
		super.getJdbcTemplate().execute(calculateInvockSql, new CallableStatementCallback() {

			public Object doInCallableStatement(CallableStatement stmt)
					throws SQLException, DataAccessException {
				stmt.setString(1, dataType);
				stmt.setString(2, JOURANLID);
				return stmt.execute();
			}
		});
	}


	/* (non-Javadoc)
	 * @see com.sf.module.ossinterface.dao.IDataControlJdbcDao#writeExecpeiontStatus()
	 */
	public void writeExecpeiontStatus() {
		String sql="update ts_oss_datacontrol set starttm=sysdate-1,state=0 ,TIMES=TIMES+1 where task=2 ";
		excuseSql(sql);

		Integer iTimes =0;

		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet("select t.times from ts_oss_datacontrol t where t.task=2");
		while (rs.next()){
			try{
				if(null!=rs.getObject("times"))
					iTimes=Integer.valueOf(rs.getObject("times").toString());
				break;
			}catch(Exception e){
				e.printStackTrace();
			}
		}


		if(iTimes>2){
			sql="update ts_oss_datacontrol set starttm=sysdate,endtm=sysdate,state=2,TIMES=0 where task=2";
			excuseSql(sql);
		}
	}

	/**
	 *
	 * @author 069823
	 * @date 2014-7-29
	 * @param sql void
	 */
	private void excuseSql(String sql) {
		super.getJdbcTemplate().execute(
				sql,
				new PreparedStatementCallback() {
					public Object doInPreparedStatement(
							PreparedStatement ps) throws SQLException,
							DataAccessException {
						int count = ps.executeUpdate();
						return count;
					}
				});
	}
}
