/***********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * 
 * HISTORY
 ********************************************************
 *  ID    DATE              PERSON             	REASON
 *  1     2015-5-4          杜志星 (380173)      创建 
 ********************************************************/
package com.sf.module.common.action;

import java.util.Collection;

import com.sf.framework.base.IPage;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.common.biz.ISysConfigBiz;
import com.sf.module.common.domain.SysConfig;

/**
 * 系统配置表(序列：SEQ_DATA_LOG)ACTION
 * @author 杜志星 (380173) 2015-5-4 
 */
public class SysConfigAction extends BaseAction{

	private static final long serialVersionUID = 1L;

	private Boolean success = true;
	
	private int start = 0;
	private int limit = 50;
	private long total;
	private String msg;
	private long[] ids;
	
	private SysConfig sysConfig;
	private Collection<SysConfig> results;
	
	private ISysConfigBiz sysConfigBiz;
	
	/**
	 * 系统配置表:分页查询
	 * @author 杜志星 (380173)
	 * @date 2015-5-4 
	 * @return
	 */
	public String pageView() {
		
		IPage<SysConfig> pageView = sysConfigBiz.pageView(sysConfig, limit, start/limit);
		results = pageView.getData();
		total = pageView.getTotalSize();
		
		return SUCCESS;
	}
	
	/**
	 * 新增或者编辑
	 * @author 杜志星 (380173)
	 * @date 2015-5-4 
	 * @return
	 */
	public String saveOrUpdate() {
		
		try {
			sysConfigBiz.saveOrUpdate(sysConfig);
		} catch (BizException e) {
			msg = e.getMessage();
		}
		sysConfig = null;
		return SUCCESS;
	}
	
	/**
	 * 删除
	 * @author 杜志星 (380173)
	 * @date 2015-5-4 
	 * @return
	 */
	public String remove() {
		
		try{
			sysConfigBiz.remove(ids);
		} catch (BizException e) {
			msg = e.getMessage();
		}
		return SUCCESS;
	}
	
	/**
	 * 查询
	 * @author 杜志星 (380173)
	 * @date 2015-5-4 
	 * @return
	 */
	public String search() {
		
		try{
			sysConfig = sysConfigBiz.searchByKeyName(sysConfig.getKeyName());
		} catch (BizException e) {
			msg = e.getMessage();
		}
		return SUCCESS;
	}
	
	public SysConfig getSysConfig() {
		return sysConfig;
	}
	public void setSysConfig(SysConfig sysConfig) {
		this.sysConfig = sysConfig;
	}
	public Boolean getSuccess() {
		return success;
	}
	public long getTotal() {
		return total;
	}
	public String getMsg() {
		return msg;
	}
	public Collection<SysConfig> getResults() {
		return results;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public void setIds(long[] ids) {
		this.ids = ids;
	}

	public void setSysConfigBiz(ISysConfigBiz sysConfigBiz) {
		this.sysConfigBiz = sysConfigBiz;
	}
}
