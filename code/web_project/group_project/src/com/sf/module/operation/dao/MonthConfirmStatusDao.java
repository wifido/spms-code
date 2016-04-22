/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-02     houjingyu       创建
 **********************************************/

package com.sf.module.operation.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.operation.domain.MonthConfirmStatus;

/**
 *
 * 排班提交确认状态的Dao实现类
 * @author houjingyu  2014-07-02
 *
 */
public class MonthConfirmStatusDao extends BaseEntityDao<MonthConfirmStatus> implements IMonthConfirmStatusDao {
	public MonthConfirmStatus findBy(Long deptid,String ym){
		DetachedCriteria dc = DetachedCriteria.forClass(MonthConfirmStatus.class);
		dc.add(Restrictions.eq("deptId",deptid));
		dc.add(Restrictions.eq("ym",ym));
		List<MonthConfirmStatus> list = super.findBy(dc);
		if(list!=null&&list.size()>0)
			return list.get(0);
		return null;
	}
}