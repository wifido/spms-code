package com.sf.module.operation.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.sf.framework.base.IPage;
import com.sf.module.common.action.SpmsBaseAction;
import com.sf.module.operation.biz.IGroupBaseInfoBiz;
import com.sf.module.operation.domain.GroupBaseInfo;

public class GroupBaseInfoAction extends SpmsBaseAction {
	private static final long serialVersionUID = -5674923043523586445L;

	private Collection<GroupBaseInfo> groupBaseInfos;
	

	private List<GroupBaseInfo> noticeList;

	private long totalSize;

	private String ids;

	private boolean success = true;

	private GroupBaseInfo groupBaseInfo;

	private String msg;
	
	private File upload;
	
	private String fileName;
	
	private Long importDeptId;

	public Long getImportDeptId() {
		return importDeptId;
	}

	public void setImportDeptId(Long importDeptId) {
		this.importDeptId = importDeptId;
	}

	/**
	 * 分页起始索引
	 */
	private int start;
	
	/**
	 * 分页每页最大记录数
	 */
	private int limit;

	// 失效日期
	private String  disableDt;
	

	// 生效日期
	private String  enableDt;

	// 错误状态信息返回
	private String status;
	/**
	 * 过期提示窗口是否显示
	 */
	private boolean noticeShow = false;

	public String getEnableDt() {
		return enableDt;
	}

	public void setEnableDt(String enableDt) {
		this.enableDt = enableDt;
	}

	@Resource
	private IGroupBaseInfoBiz groupBaseInfoBiz;

	public String findGroupBaseInfos() {
		this.groupBaseInfos = groupBaseInfoBiz.findPageByGroupBaseInfos();
		return SUCCESS;
	}

	public String findPageByGroupBaseInfo() {
		IPage<GroupBaseInfo> page = groupBaseInfoBiz
				.findPageByGroupBaseInfos(groupBaseInfo,limit,start/limit);
		this.groupBaseInfos = page.getData();
		this.totalSize = page.getTotalSize();
		return SUCCESS;
	}

	public String saveGroupBaseInfo() throws Exception {
		try {
			if (StringUtils.isNotEmpty(disableDt))
				groupBaseInfo.setDisableDt((new SimpleDateFormat("yyyy-MM-dd"))
						.parse(disableDt));
			if (StringUtils.isNotEmpty(enableDt))
				groupBaseInfo.setEnableDt((new SimpleDateFormat("yyyy-MM-dd"))
						.parse(enableDt));
			groupBaseInfoBiz.saveGroupBaseInfo(groupBaseInfo);
			success = true;
		} catch (com.sf.framework.core.exception.BizException e) {
			success = false;
			status = super.getMessageSource().getMessage(e.getMessageKey());
			log.error("error",e);
		} catch (Exception e) {
			this.success = false;
			status = super.getMessageSource().getMessage(
					"groupBaseInfo.save.error");
			log.error("error", e);
		}
		return SUCCESS;
	}

	public String deleteGroupBaseInfos() {
		try {
			groupBaseInfoBiz.delete(ids);
			success = true;
			status="ok";
		} catch (Exception e) {
			success = false;
			status=super.getMessageSource().getMessage("groupBaseInfo.using.not.del");
			log.debug("exception", e);
		}
		return SUCCESS;
	}
	
	public String noticeShow(){
		try{
			noticeShow=groupBaseInfoBiz.noticeShow();
			success = true;
			status ="ok";
			
		}catch(Exception e){
			success = false;
			status=super.getMessageSource().getMessage("groupBaseInfo.notice.error");
			log.debug("exception", e);
		}
		return SUCCESS;
	}
	
	public String noticeList(){
		try{
			noticeList=groupBaseInfoBiz.noticeShowList();
			this.totalSize = noticeList.size();
			success = true;
			status ="ok";
			
		}catch(Exception e){
			success = false;
			status=super.getMessageSource().getMessage("groupBaseInfo.notice.error");
			log.debug("exception", e);
		}
		return SUCCESS;
	}
	
	public String groupBaseaInfoUploadFile(){
		try{
			HashMap map =groupBaseInfoBiz.readUpLoadGroupBaseInfos(upload, fileName, importDeptId);
			fileName =(String)map.get("fileName");
			msg = map.get("msg").toString();
			success =true;
			status = "ok";
		}catch(Exception e){
			success = false;
			msg=super.getMessageSource().getMessage("groupBaseInfo.import.error");
			log.debug("exception", e);
		}
		return SUCCESS;
	}
	
	public String exportGroupInfos(){
		try{
			fileName =groupBaseInfoBiz.exportGroupBaseInfo(groupBaseInfo);
			success = true;
			status ="ok";
		}catch(Exception e){
			success = false;
			status = super.getMessageSource().getMessage("groupBaseInfo.export.error");
			log.debug("exception",e);
		}
		return SUCCESS;
	}

	public String test() {
		msg = "ok";
		return SUCCESS;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public GroupBaseInfo getGroupBaseInfo() {
		return groupBaseInfo;
	}

	public void setGroupBaseInfo(GroupBaseInfo groupBaseInfo) {
		this.groupBaseInfo = groupBaseInfo;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public void setGroupBaseInfoBiz(IGroupBaseInfoBiz groupBaseInfoBiz) {
		this.groupBaseInfoBiz = groupBaseInfoBiz;
	}

	public void setDisableDt(String disableDt) {
		this.disableDt = disableDt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Collection<GroupBaseInfo> getGroupBaseInfos() {
		return groupBaseInfos;
	}

	public void setGroupBaseInfos(Collection<GroupBaseInfo> groupBaseInfos) {
		this.groupBaseInfos = groupBaseInfos;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public boolean isNoticeShow() {
		return noticeShow;
	}

	public void setNoticeShow(boolean noticeShow) {
		this.noticeShow = noticeShow;
	}

	public List<GroupBaseInfo> getNoticeList() {
		return noticeList;
	}
	
}