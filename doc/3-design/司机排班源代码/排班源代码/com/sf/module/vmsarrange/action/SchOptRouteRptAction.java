/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-16     方芳                        创建
 **********************************************/
package com.sf.module.vmsarrange.action;

import java.io.InputStream;
import java.util.Collection;
import java.util.Date;

import com.sf.framework.base.IPage;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.vmsarrange.biz.ISchOptRouteRptBiz;
import com.sf.module.vmsarrange.domain.SchOptRouteRpt;

/**
 *
 * 路径优化分析报表Action处理类
 *
 */
public class SchOptRouteRptAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ISchOptRouteRptBiz schOptRouteRptBiz;
	private boolean success = true;  // Ajax响应标志
	private String retMsg; //Ajax请求响应提示消息
	private int start;
	private int limit;
	private long total;
	private Collection<SchOptRouteRpt> page;
	private String fileName;
	private InputStream stream;
	private Date yearMonth;
	private Long deptId;
	
	/**
	 * 查找分页数据
	 * @return
	 */
	public String listPage(){
		if(limit > 0){
			IPage<SchOptRouteRpt> data = schOptRouteRptBiz.listPage(deptId, yearMonth, limit, start/limit);
			if(null != data){
				this.page = data.getData();
				this.total = data.getTotalSize();
			}
		}
		return SUCCESS;
	}
	/**
	 * 生成报表
	 * @return
	 */
	public String listReport(){
		try{
			this.fileName = schOptRouteRptBiz.listReport(deptId, yearMonth);
		}catch(BizException e){
			this.retMsg = e.getMessageKey();
		}
		return SUCCESS;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public boolean isSuccess() {
		return success;
	}
	public String getRetMsg() {
		return retMsg;
	}
	public long getTotal() {
		return total;
	}
	public Collection<SchOptRouteRpt> getPage() {
		return page;
	}
	public InputStream getStream() {
		return stream;
	}
	public void setSchOptRouteRptBiz(ISchOptRouteRptBiz schOptRouteRptBiz) {
		this.schOptRouteRptBiz = schOptRouteRptBiz;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public void setYearMonth(Date yearMonth) {
		this.yearMonth = yearMonth;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
}
