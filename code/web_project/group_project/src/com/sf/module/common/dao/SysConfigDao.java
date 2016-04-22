/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-24     380173       创建
 **********************************************/

package com.sf.module.common.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.common.domain.SysConfig;

/**
 *
 * 系统配置表(序列：SEQ_BASE)的Dao实现类
 * @author 380173  2014-07-24
 *
 */
public class SysConfigDao extends BaseEntityDao<SysConfig> implements ISysConfigDao {

	public List<SysConfig> searchByKeyName(String keyName){
		
		DetachedCriteria dc = DetachedCriteria.forClass(SysConfig.class);
		
		if (keyName != null && keyName.trim().length() > 0) {
			dc.add(Restrictions.eq("keyName", keyName));
		}
		dc.addOrder(Order.asc("createdTm"));
		return super.findBy(dc);
	}
	
	public String getConfigValue(String keyName){
		List<SysConfig> configList = searchByKeyName(keyName);
		if(configList.isEmpty()){
			return "";
		}
		return configList.get(0).getKeyValue();
	}
	
	public IPage<SysConfig> pageView(SysConfig model, int pageSize, int pageIndex, Long userId) {

		DetachedCriteria dc = DetachedCriteria.forClass(SysConfig.class);
		if(model != null) {
			
			//KEY名称
			if(model.getKeyName() != null && model.getKeyName().length() > 0) {
				dc.add(Restrictions.like("keyName", model.getKeyName(), MatchMode.ANYWHERE));
			}
			//KEY描述
			if(model.getKeyDesc() != null && model.getKeyDesc().length() > 0) {
				dc.add(Restrictions.like("keyDesc", model.getKeyDesc().toUpperCase(), MatchMode.ANYWHERE));
			}
		}
		//KEY名称
		dc.addOrder(Order.asc("keyName"));
		return super.findPageBy(dc, pageSize, pageIndex);
	}
}