package com.sf.module.ossinterface.dao;

public interface IDataControlJdbcDao {
	/*
	 * 验证是否需要进行数据重发，重发请求保证智能通过一台机器发送出去
	 */
	int checkReSendByDay();
	/*
	 * 实现完成标记
	 */
	void writeSuccessStatus();
	/**
	 * 进行本地数据更新
	 * @param dataType
	 * @param JOURANLID
	 */
	void calculteOssEmp(final String dataType,final String JOURANLID) ;

	/**
	 * 写异常状态
	 *
	 * @author 069823
	 * @date 2014-7-29 void
	 */
	void writeExecpeiontStatus();
}
