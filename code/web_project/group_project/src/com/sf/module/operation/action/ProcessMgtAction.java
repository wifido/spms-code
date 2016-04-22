/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-08     houjingyu       创建
 **********************************************/

package com.sf.module.operation.action;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.sf.framework.base.IPage;
import com.sf.framework.core.exception.BizException;
import com.sf.module.common.action.SpmsBaseAction;
import com.sf.module.operation.action.dto.ProcessDto;
import com.sf.module.operation.biz.IProcessDetailBiz;
import com.sf.module.operation.biz.IProcessMgtBiz;
import com.sf.module.operation.domain.ProcessMgt;

/**
 * 
 * 月度工序实体的Action处理类
 * 
 * @author houjingyu 2014-07-08
 * 
 */
public class ProcessMgtAction extends SpmsBaseAction {

	private static final long serialVersionUID = 1L;
	private Boolean success = true;
	private ProcessDto dto;
	private Collection processMgts;
	private int limit;
	private int start;
	private long totalSize;
	private String msg;
	private String fileName;
	private InputStream downStream;
	private File uploadFile;
	private Map<String, Object> mp;
	private IProcessMgtBiz processMgtBiz;
	private IProcessDetailBiz processDetailBiz;
	private String tips;
	private boolean confirmFlag = false;
	private String processMgtIds;
	
	public String getProcessMgtIds() {
		return processMgtIds;
	}

	public void setProcessMgtIds(String processMgtIds) {
		this.processMgtIds = processMgtIds;
	}

	public Boolean getSuccess() {
		return this.success;
	}

	public ProcessDto getDto() {
		return dto;
	}

	public Collection getProcessMgts() {
		return processMgts;
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

	public String getFileName() {
		return fileName;
	}

	public InputStream getDownStream() {
		return downStream;
	}

	public void setDto(ProcessDto dto) {
		this.dto = dto;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getTips() {
		return tips;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public Map<String, Object> getMp() {
		return mp;
	}

	public boolean getConfirmFlag() {
		return confirmFlag;
	}

	public void setConfirmFlag(boolean confirmFlag) {
		this.confirmFlag = confirmFlag;
	}

	public void setProcessMgtBiz(IProcessMgtBiz processMgtBiz) {
		this.processMgtBiz = processMgtBiz;
	}

	public void setProcessDetailBiz(IProcessDetailBiz processDetailBiz) {
		this.processDetailBiz = processDetailBiz;
	}

	public String searchProcess() {
		processMgts = processDetailBiz.findByDeptId(dto.getDeptId());
		return SUCCESS;
	}

	// 查询
	public String search() {
		if (dto != null && dto.getDeptId() != null) {
			IPage<ProcessMgt> data = this.processMgtBiz.findPage(dto, limit,
					start);
			processMgts = data.getData();
			totalSize = data.getTotalSize();
		}
		return SUCCESS;
	}

	// 导出
	public String export() {
		try {
			fileName = processMgtBiz.getExcel(dto);
		} catch (BizException e) {
			msg = e.getMessageKey();
		}
		return SUCCESS;
	}

	// 导入
	public String importRec() {
		try {
			mp = processMgtBiz.saveImport(uploadFile, dto);
			fileName = (String) mp.get("errExcelPath");
			tips = (String) mp.get("tips");
		} catch (BizException e) {
			msg = e.getMessageKey();
		} catch (org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException e) {
			this.msg = "该记录已被修改，请刷新后重试";
		} catch (Exception e) {
			this.msg = "导入模版或数据格式错误！";
			if (msg != null && msg.indexOf("UK_TT_PB_PROCESS_BY_MONTH") != -1) {
				this.msg = "导入的数据有部分已存在，请稍后重试!";
			}
		}
		return SUCCESS;
	}

	// 修改
	public String update() {
		if (dto != null) {
			try {
				processDetailBiz.updateMgtAndDetail(dto);
			} catch (BizException e) {
				this.msg = e.getMessage();
			} catch (org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException e) {
				this.msg = "该记录已被修改，请刷新后重试";
			}
		}
		dto = null;
		return SUCCESS;
	}

	public String checkCanConfirm() {
		try {
			confirmFlag = processMgtBiz.getCanConfirm(dto.getDeptId(),dto.getYm());
		} catch (BizException e) {
			this.msg = e.getMessage();
		}
		return SUCCESS;
	}

	// 确认提交
	public String commitConfirm() {
		try {
			processMgtBiz.saveConfirmStatus(dto.getDeptId(),dto.getYm());
		} catch (Exception ee) {
			this.msg = ee.getMessage();
		}
		return SUCCESS;
	}
	
	
	public String checkIsCanDelete(){
		try{
			ProcessDto pro=new ProcessDto();
			pro.setDeptId(dto.getDeptId());
			IPage<ProcessMgt> data = this.processMgtBiz.findPage(pro, 2, 0);
			if(data==null||data.isEmpty()){
				confirmFlag=false;
				this.msg = "网点无工序安排数据!";
				return SUCCESS;
			}
			confirmFlag = processMgtBiz.processMgtIsConfirm(dto.getDeptId(), processMgtIds);
		}catch (BizException e) {
			this.msg = e.getMessage();
		}
		return SUCCESS;
	}

	// 删除
	public String delete() {
		try {
			processMgtBiz.deleteProcessMgt(dto.getDeptId(),
					processMgtIds);
		} catch (BizException e) {
			this.msg = e.getMessage();
		} catch (Exception ee) {
			this.msg = ee.getMessage();
		}
		return SUCCESS;
	}

	// 通知
	public String searchNoticesCount() {
		HashMap mp = new HashMap();
		mp.put("flag", processMgtBiz.searchNoticesCount());
		processMgts = new ArrayList();
		processMgts.add(mp);
		return SUCCESS;
	}

	// 保存
	public String saveDetail() {
		if (dto != null) {
			try {
				processDetailBiz.saveMgtAndDetail(dto);
			} catch (BizException e) {
				this.msg = e.getMessage();
			} catch (Exception ee) {
				String m = ee.getMessage();
				if (m != null && m.indexOf("UK_TT_PB_PROCESS_BY_MONTH") != -1) {
					this.msg = "数据已存在，不能重复新增!";
				}
			}
		}
		dto = null;
		return SUCCESS;
	}
	
	public String confirmExport() {
		try {
			fileName = processMgtBiz.getConfirmExport();
		} catch (BizException e) {
			msg = e.getMessageKey();
		}
		return SUCCESS;
	}

}