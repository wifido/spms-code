/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2015-05-04     380173       创建
 **********************************************/

package com.sf.module.common.biz;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sf.framework.base.IPage;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.cache.SpmsSysConfigCache;
import com.sf.module.common.cache.SpmsSysConfigCacheProvider;
import com.sf.module.common.dao.ISysConfigDao;
import com.sf.module.common.domain.SysConfig;

/**
 *
 * 系统配置表(序列：SEQ_BASE)的业务实现类
 * @author 380173  2015-05-04
 *
 */
public class SysConfigBiz extends BaseBiz implements ISysConfigBiz {

	/**
	 * 系统配置表(序列：SEQ_BASE)的Dao接口
	 */
	private ISysConfigDao sysConfigDao;
	
	SpmsSysConfigCacheProvider spmsSysConfigCacheProvider;

	public SysConfig searchByKeyName(String keyName) {
		
		SysConfig entity = new SysConfig();
		List<SysConfig> list = sysConfigDao.searchByKeyName(keyName);
		if(list != null && list.size() > 0) {
			entity = list.get(0);
		}
		return entity;
	}
	
	public Map<String, String> createMap() {
		return SpmsSysConfigCache.getCache();
	}

	public IPage<SysConfig> pageView(SysConfig model, int pageSize, int pageIndex) {
		return sysConfigDao.pageView(model, pageSize, pageIndex, super.getUserId());
	}
	
	public void saveOrUpdate(SysConfig model) {
		
		if(model == null) {
			throw new BizException("数据不能为空");
		}
		if(model.getKeyName() == null) {
			throw new BizException("KEY名称不能为空");
		}
		if(model.getKeyName() != null && model.getKeyName().trim().length() <= 0) {
			throw new BizException("KEY名称不能为空");
		}
		if(model.getKeyValue() == null) {
			throw new BizException("KEY值不能为空");
		}
		if(model.getKeyValue() != null && model.getKeyValue().trim().length() <= 0) {
			throw new BizException("KEY值不能为空");
		}
		
		if(model != null) {
			SysConfig config = null;
			
			if(model.getId() != null) {
				
				config = sysConfigDao.load(model.getId());
				if(config == null) {
					throw new BizException("记录已经删除");
				}
				config.setKeyValue(model.getKeyValue());
				config.setKeyDesc(model.getKeyDesc());
				config.setModifiedTm(new Date());
				
				sysConfigDao.update(config);
			} else {
				
				SysConfig c = this.searchByKeyName(model.getKeyName());
				if(model.getKeyName().equals(c.getKeyName())) {
					throw new BizException("KEY名称已经存在");
				}
				
				model.setCreatedTm(new Date());
				sysConfigDao.save(model);
			}
		}
	}
	
	public void remove(long[] ids) {
			
		if(ids != null && ids.length > 0) {
			for(int i = 0; i < ids.length; i++){
				
				SysConfig entity = sysConfigDao.load(ids[i]);
				if(entity != null) {
					sysConfigDao.remove(ids[i]);
				}
			}
		}
	}
	
	/**
	 * 设置系统配置表(序列：SEQ_DATA_LOG)的Dao接口
	 */
	public void setSysConfigDao(ISysConfigDao sysConfigDao) {
		this.sysConfigDao = sysConfigDao;
	}

	public void setSfmSysConfigCacheProvider(
			SpmsSysConfigCacheProvider sfmSysConfigCacheProvider) {
		this.spmsSysConfigCacheProvider = sfmSysConfigCacheProvider;
	}

}