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

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.operation.domain.ProcessDetail;

/**
 *
 * 工序每日明细的Dao实现类
 * @author houjingyu  2014-07-08
 *
 */
public class ProcessDetailDao extends BaseEntityDao<ProcessDetail> implements IProcessDetailDao {

	public ProcessDetail findByCondition(Long deptid, Date dt, String empcode) {
		DetachedCriteria dc = DetachedCriteria.forClass(ProcessDetail.class);
		dc.add(Restrictions.eq("empCode", empcode));
		dc.add(Restrictions.eq("processDt", dt));
		dc.add(Restrictions.eq("deptId", deptid));
		List<ProcessDetail> list = this.findBy(dc);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}