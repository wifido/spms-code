package com.sf.module.operation.biz;

import com.sf.framework.base.domain.IEntity;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.operation.util.CommonUtil;

public class SysdateBiz extends BaseEntityDao<IEntity> implements
		ISysdateBiz {

	public String getSysTime() {
		return CommonUtil.getYmdStr(CommonUtil.currentDt());
	}
}
