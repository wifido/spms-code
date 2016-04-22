/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-21     wen.jun       创建
 **********************************************/

package com.sf.module.ossinterface.biz;

import java.util.Date;

import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.ossinterface.dao.IDataControlJdbcDao;
import com.sf.module.ossinterface.dao.IEsbBigFileResendDao;
import com.sf.module.ossinterface.domain.EsbBigFileResend;

/**
 *
 * ESB数据重发请求(BigFileResendData)参数配置表的业务实现类
 * @author wen.jun  2014-06-21
 *
 */
public class EsbBigFileResendBiz extends BaseBiz implements IEsbBigFileResendBiz {

	/**
	 * ESB数据重发请求(BigFileResendData)参数配置表的Dao接口
	 */
	private IEsbBigFileResendDao esbBigFileResendDao;
	/**
	 * ESB数据重发次数(一天一次)控制Dao
	 */
	private IDataControlJdbcDao dataControlJdbcDao;

	/**
	 * 设置ESB数据重发请求(BigFileResendData)参数配置表的Dao接口
	 */
	public void setEsbBigFileResendDao(IEsbBigFileResendDao esbBigFileResendDao) {
		this.esbBigFileResendDao = esbBigFileResendDao;
	}



	public void setDataControlJdbcDao(IDataControlJdbcDao dataControlJdbcDao) {
		this.dataControlJdbcDao = dataControlJdbcDao;
	}



	/**
	 * <pre>
	 * ESB数据重发请求(BigFileResendData)参数配置表
	 * </pre>
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014
	 * @param entity
	 * @return
	 */

	public Long save(EsbBigFileResend entity) {
		entity.setCreatedTm(new Date());
		return esbBigFileResendDao.save(entity);
	}


	/**
	 * <pre>
	 * 更新参数配置表状态
	 * </pre>
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014
	 * @param id
	 * @param state
	 */

	public void update(Long id, Integer state) {
		EsbBigFileResend load = esbBigFileResendDao.load(id);
		load.setState(state);
		load.setModifiedTm(new Date());
		esbBigFileResendDao.update(load);
	}

	/**
	 * <pre>
	 * 更加createdTm获取间隔最早失败的任务
	 * </pre>
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014
	 * @return
	 */

	public EsbBigFileResend getByCreatedTmOrder() {
		return esbBigFileResendDao.getByCreatedTmOrder();
	}



	public int checkDataResend() {
		// TODO Auto-generated method stub
		return dataControlJdbcDao.checkReSendByDay();
	}



	public void writeSuccessStatus() {
		dataControlJdbcDao.writeSuccessStatus();

	}



	/* (non-Javadoc)
	 * @see com.sf.module.ossinterface.biz.IEsbBigFileResendBiz#writeExecpeiontStatus()
	 */
	public void writeExecpeiontStatus() {
		dataControlJdbcDao.writeExecpeiontStatus();
	}




}