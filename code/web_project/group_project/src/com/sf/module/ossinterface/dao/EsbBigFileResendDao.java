/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-21     wen.jun       创建
 **********************************************/

package com.sf.module.ossinterface.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.ossinterface.domain.EsbBigFileResend;

/**
 * 
 * ESB数据重发请求(BigFileResendData)参数配置表的Dao实现类
 * 
 * @author wen.jun 2014-06-21
 * 
 */
public class EsbBigFileResendDao extends BaseEntityDao<EsbBigFileResend>
		implements IEsbBigFileResendDao {

	private DetachedCriteria buildDc() {
		return DetachedCriteria.forClass(entityClass);
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
		DetachedCriteria dc = buildDc();

		dc.add(Restrictions.in("state", new Object[] {
				EsbBigFileResend.DOWNlOAD_FAILURE,
				EsbBigFileResend.PARSER_FAILURE,
				EsbBigFileResend.START_DOWN,
				EsbBigFileResend.START_PARSER
				}));
		dc.add(Restrictions.sqlRestriction("CEIL((TRUNC(SYSDATE,'MI') - TRUNC(CREATED_TM,'MI')) * 1440) >= INTERVAL_TIME"));
		dc.addOrder(Order.asc("createdTm"));
		
		
		List<EsbBigFileResend> list = super.findBy(dc);
		
		return list == null || list.isEmpty() ? null : list.get(0);
	}

}