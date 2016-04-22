/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-20     houjingyu       创建
 **********************************************/

package com.sf.module.operation.action;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sf.framework.base.IPage;
import com.sf.framework.core.exception.BizException;
import com.sf.module.common.action.SpmsBaseAction;
import com.sf.module.common.util.StringUtil;
import com.sf.module.operation.action.dto.ScheduleDto;
import com.sf.module.operation.biz.IMonthConfirmStatusBiz;
import com.sf.module.operation.biz.ISchedulMgtBiz;
import com.sf.module.operation.biz.ISchedulingBaseBiz;
import com.sf.module.operation.biz.ISchedulingBiz;
import com.sf.module.operation.domain.OutEmployee;
import com.sf.module.operation.domain.SchedulMgt;
import com.sf.module.operation.domain.SchedulingBase;

/**
 * 
 * 排班管理的Action处理类
 * 
 * @author houjingyu 2014-06-20
 * 
 */
public class SchedulMgtAction extends SpmsBaseAction {

	private static final long serialVersionUID = 1L;

	/**
	 * Ajax请求成功的响应标志
	 */
	private Boolean success = true;
	private ScheduleDto dto;
	private Collection schedulMgts;
	private int limit;
	private int start;
	private long totalSize;
	private String msg;
	private String fileName;
	private InputStream downStream;
	private File uploadFile;
	private String dataStr;
	private Map<String, Object> mp;
	private String tips;
	private boolean confirmFlag = false;
	private List<SchedulingBase> scheBases;
	private Collection<OutEmployee> emps = null;
	private String schedulingIds;
	public String filterAreaCodeType;
	/**
	 * 排班管理的业务接口
	 */
	private ISchedulMgtBiz schedulMgtBiz;
	private IMonthConfirmStatusBiz monthConfirmStatusBiz;
	private ISchedulingBiz schedulingBiz;
	private ISchedulingBaseBiz schedulingBaseBiz;

	/**
	 * 获取Ajax请求成功的响应标志
	 */
	public Boolean getSuccess() {
		return this.success;
	}

	public String getSchedulingIds() {
		return schedulingIds;
	}

	public void setSchedulingIds(String schedulingIds) {
		this.schedulingIds = schedulingIds;
	}

	public Collection getSchedulMgts() {
		return schedulMgts;
	}

	public Collection<OutEmployee> getEmps() {
		return emps;
	}

	public int getLimit() {
		return limit;
	}

	public int getStart() {
		return start;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public String getMsg() {
		return msg;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setDto(ScheduleDto dto) {
		this.dto = dto;
	}

	public ScheduleDto getDto() {
		return dto;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public InputStream getDownStream() {
		return downStream;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getDataStr() {
		return dataStr;
	}

	public void setDataStr(String dataStr) {
		this.dataStr = dataStr;
	}

	public Map<String, Object> getMp() {
		return mp;
	}

	public String getTips() {
		return tips;
	}

	public boolean getConfirmFlag() {
		return confirmFlag;
	}

	public void setConfirmFlag(boolean confirmFlag) {
		this.confirmFlag = confirmFlag;
	}

	public List<SchedulingBase> getScheBases() {
		return scheBases;
	}

	/**
	 * 设置排班管理的业务接口
	 */
	public void setSchedulMgtBiz(ISchedulMgtBiz schedulMgtBiz) {
		this.schedulMgtBiz = schedulMgtBiz;
	}

	public void setMonthConfirmStatusBiz(IMonthConfirmStatusBiz monthConfirmStatusBiz) {
		this.monthConfirmStatusBiz = monthConfirmStatusBiz;
	}

	public void setSchedulingBiz(ISchedulingBiz schedulingBiz) {
		this.schedulingBiz = schedulingBiz;
	}

	public void setSchedulingBaseBiz(ISchedulingBaseBiz schedulingBaseBiz) {
		this.schedulingBaseBiz = schedulingBaseBiz;
	}
	
	public String forwardOperationIndex() {
			
			filterDeptCodeType = sysConfigBiz.searchByKeyName(StringUtil.OPERATION_DEPTCODE_FILTER).getKeyValue()+",";
			filterAreaCodeType =sysConfigBiz.searchByKeyName(StringUtil.OPERATION_DEPTCODE_FILTER).getKeyValue()+"," +
					sysConfigBiz.searchByKeyName(StringUtil.QB_DEPTCODE_FILTER).getKeyValue()+",";
			return SUCCESS;
	}

	// 查询
	public String search() {
		if (dto != null && dto.getDeptId() != null) {
			IPage<SchedulMgt> data = schedulMgtBiz.findPage(dto, limit, start);
			schedulMgts = data.getData();
			totalSize = data.getTotalSize();
		}
		return SUCCESS;
	}

	// 导出
	public String export() {
		try {
			fileName = schedulMgtBiz.getExcel(dto);
		} catch (BizException e) {
			msg = e.getMessageKey();
		}
		return SUCCESS;
	}

	// 导入
	@SuppressWarnings("unchecked")
	public String importRec() {
		try {
			mp = schedulMgtBiz.saveImport(uploadFile, dto);
			fileName = (String) mp.get("errExcelPath");
			schedulMgts = (Collection) mp.get("confrimList");
			tips = (String) mp.get("tips");
		} catch (BizException e) {
			msg = e.getMessageKey();

		} catch (org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException e) {
			this.msg = "该记录已被修改，请刷新后重试";
		} catch (Exception e) {
			this.msg = "导入模版或数据格式错误！";
			if (msg != null && msg.indexOf("UK_TT_PB_SHEDULE_BY_MONTH") != -1) {
				this.msg = "导入的数据有部分已存在，请稍后重试!";
			}
		}
		return SUCCESS;
	}

	// 导入数据确认
	public String saveImportConfirmData() {
		try {
			schedulMgtBiz.saveImportConfirmData(dataStr);
		} catch (BizException e) {
			this.msg = e.getMessage();
		} catch (org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException e) {
			this.msg = "该记录已被修改，请刷新后重试";
		} catch (Exception e) {
			this.msg = e.getMessage();
			if (msg != null && msg.indexOf("UK_TT_PB_SHEDULE_BY_MONTH") != -1) {
				this.msg = "需确认的数据有部分已存在，请稍后重试!";
			}
		}
		return SUCCESS;
	}

	// 修改
	public String update() {
		if (dto != null) {
			try {
				schedulingBiz.updateMgtAndDetail(dto);
			} catch (BizException e) {
				this.msg = e.getMessage();
			} catch (org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException e) {
				this.msg = "该记录已被修改，请刷新后重试";
			}
		}
		dto = null;
		return SUCCESS;
	}

	// 确认提交
	public String commitConfirm() {
		try {
			schedulMgtBiz.commitConfirmScheduling(dto.getDeptId(), dto.getYm());
		} catch (BizException e) {
			this.msg = e.getMessage();
		} catch (Exception e) {
			this.msg = e.getMessage();
			if (msg != null && msg.indexOf("UK_TT_PB_SCHE_CONFIRM") != -1) {
				this.msg = "不能重复进行确认提交操作!";
			}
		}
		return SUCCESS;
	}

	// 删除排班数据
	public String delete() {
		try {
			schedulMgtBiz.delete(dto, schedulingIds);
		} catch (Exception e) {
			this.msg = e.getMessage();
		}
		return SUCCESS;
	}

	// 通知
	public String searchNoticesCount() {
		HashMap mp = new HashMap();
		mp.put("flag", schedulMgtBiz.searchNoticesCount());
		schedulMgts = new ArrayList();
		schedulMgts.add(mp);
		return SUCCESS;
	}

	public String checkCanConfirm() {
		try {
			confirmFlag = schedulMgtBiz.getCanConfirm(dto.getDeptId(), dto.getYm());
		} catch (BizException e) {
			this.msg = e.getMessage();
		}
		return SUCCESS;
	}

	public String toConfirmThe() {
		try {
			confirmFlag = schedulMgtBiz.getToConfirmThe(dto.getDeptId(), schedulingIds);
		} catch (BizException e) {
			this.msg = e.getMessage();
		}
		return SUCCESS;
	}

	// 保存
	public String saveDetail() {
		if (dto != null) {
			try {
				schedulingBiz.saveMgtAndDetail(dto);
			} catch (BizException e) {
				this.msg = e.getMessage();
			} catch (Exception ee) {
				String m = ee.getMessage();
				if (m != null && m.indexOf("UK_TT_PB_SHEDULE_BY_MONTH") != -1) {
					this.msg = "数据已存在，不能重复新增!";
				}
			}
		}
		dto = null;
		return SUCCESS;
	}

	// 检查数据有效性
	public String checkSaveValid() {
		if (dto != null) {
			try {
				mp = schedulingBiz.getSaveValid(dto);
				mp.put("success", success);
			} catch (Exception e) {
				this.msg = e.getMessage();
			}
		}
		dto = null;
		return SUCCESS;
	}

	// 获取月份内有效的班别
	public String searchScheList() {
		if (dto != null) {
			try {
				scheBases = schedulingBaseBiz.getScheBaseList(dto.getDeptId(), dto.getYm());
			} catch (Exception e) {
				this.msg = e.getMessage();
			}
		}
		dto = null;
		return SUCCESS;
	}

	public String listEmp() {
		if (dto != null && dto.getDeptId() != null) {
			IPage<OutEmployee> data = schedulMgtBiz.findEmpPage(dto, limit, start);
			emps = data.getData();
			totalSize = data.getTotalSize();
		}
		dto = null;
		return SUCCESS;
	}

	// 导出已提交确认信息
	public String btnConfirmExport() {
		try {
			fileName = schedulMgtBiz.getConfirmExport();
		} catch (BizException e) {
			msg = e.getMessageKey();
		}
		return SUCCESS;
	}
	
	// 导出未排班人员信息
	public String exportNotScheduling() {
		try {
			fileName = schedulMgtBiz.exportNotScheduling(dto.getDeptId(), dto.getYm());
		} catch (BizException e) {
			msg = e.getMessageKey();
		}
		return SUCCESS;
	}
}