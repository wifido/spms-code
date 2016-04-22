/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-18     632898 李鹏       创建
 **********************************************/

package com.sf.module.operation.action;



import static com.sf.module.common.domain.Constants.KEY_MESSAGE;
import static com.sf.module.common.domain.Constants.KEY_SUCCESS;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;

import com.sf.module.common.action.SpmsBaseAction;
import com.sf.module.operation.biz.ISchedulingBaseBiz;
import com.sf.module.operation.domain.SchedulingBase;

/**
 * 
 * 班次基础信息管理的Action处理类
 * 
 * @author 632898 李鹏 2014-06-18
 * 
 */
public class SchedulingBaseAction extends SpmsBaseAction {

	private static final long serialVersionUID = 1L;

	/**
	 * 班次基础信息管理的业务接口
	 */
	private ISchedulingBaseBiz schedulingBaseBiz;

	/**
	 * 文件上传
	 */
	private File upload;
	
	private String deptId;

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	/**
	 * Ajax返回结果集
	 */
	private HashMap dataMap;

	public HashMap getDataMap() {
		return dataMap;
	}

	/**
	 * 班次信息实体类
	 */
	private SchedulingBase schedulingBase;

	/**
	 * 设置班次基础信息管理的业务接口
	 */
	public void setSchedulingBaseBiz(ISchedulingBaseBiz schedulingBaseBiz) {
		this.schedulingBaseBiz = schedulingBaseBiz;
	}

	public SchedulingBase getSchedulingBase() {
		return schedulingBase;
	}

	/**
	 * 班次信息保存
	 * 
	 * @author 632898 李鹏
	 * @date 2014-06-18
	 * @return dataMap
	 */
	public String saveSchedule() {
		HashMap map = paramsMap();
		dataMap = new HashMap();
		try {
			if (this.schedulingBaseBiz.saveScheduling(map)) {
				dataMap.put("success", true);
				dataMap.put("msg", "保存成功！");
			} else {
				dataMap.put("success", false);
				dataMap.put("msg", "信息填写有误！");
			}
		} catch (Exception e) {
			log.error("error", e);
			dataMap.put("success", false);
			dataMap.put("msg", e.getMessage());
		}
		return SUCCESS;
	}

	/**
	 * 班次信息查询
	 * 
	 * @author 632898 李鹏
	 * @date 2014-06-18
	 * @return dataMap
	 */
	public String querySchedule() {
		HashMap map = paramsMap();
		dataMap = new HashMap();
		try {
			dataMap = this.schedulingBaseBiz.querySchedule(map);
			dataMap.put("success", true);
		} catch (Exception e) {
			log.error("error", e);
			dataMap.put("success", false);
			dataMap.put("msg", e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 班次信息删除
	 * 
	 * @author 632898 李鹏
	 * @date 2014-06-19
	 * @return dataMap
	 */
	public String deleteSchedule() {
		HashMap map = paramsMap();
		dataMap = new HashMap();
		try {
			if (this.schedulingBaseBiz.deleteSchedule(map)) {
				dataMap.put("success", true);
				dataMap.put("msg", "删除成功！");
			} else {
				dataMap.put("success", false);
				dataMap.put("msg", "删除失败！");
			}
		} catch (Exception e) {
			log.error("error", e);
			dataMap.put("msg", "删除失败！");
		}
		return SUCCESS;
	}

	/**
	 * 班次信息删除
	 * 
	 * @author 632898 李鹏
	 * @date 2014-06-19
	 * @return dataMap
	 */
	public String updateSchedule() {
		HashMap map = paramsMap();
		dataMap = new HashMap();
		try {
			if (this.schedulingBaseBiz.updateSchedule(map)) {
				dataMap.put("success", true);
				dataMap.put("msg", "保存成功！");
			} else {
				dataMap.put("success", false);
				dataMap.put("msg", "更新失败！");
			}
		} catch (Exception e) {
			log.error("error", e);
		}
		return SUCCESS;
	}
	
	/**
	 * 判断班次是否被使用
	 * 
	 * @author 632898 李鹏
	 * @param 班别Id
	 * @date 2014-07-18
	 */
	public String isScheduling(){
		HttpServletRequest request = ServletActionContext.getRequest();
		dataMap = new HashMap();
		try {
			String jsonData = request.getParameter("jsonData");
			JSONArray list = JSONArray.fromObject(jsonData);
			dataMap = (HashMap) schedulingBaseBiz.isScheduling(list);
		} catch (Exception e) {
			log.error("error", e);
			dataMap.put("success", false);
			dataMap.put("msg", e.getMessage().toString());
		}
		return SUCCESS;
	}

	/**
	 * 获取参数
	 * 
	 * @author 632898 李鹏
	 * @return 所有前端参数都以Map集合装载
	 */
	public HashMap paramsMap() {
		HashMap map = new HashMap();
		HttpServletRequest request = ServletActionContext.getRequest();
		Enumeration<?> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			Object param = params.nextElement();
			if (param instanceof String) {
				String queryField = (String) param;
				String queryValue = request.getParameter(queryField);
				map.put(queryField, queryValue);
			}
		}
		return map;
	}
	
	
	/**
	 * 班别信息导出
	 * 
	 * @author 632898 李鹏
	 * @date 2014-06-20
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String exportSchedule() {
		HashMap map = paramsMap();
		dataMap = new HashMap();
		try {
			dataMap = this.schedulingBaseBiz.exportSchedule(map);
			dataMap.put("success", true);
			dataMap.put("msg", "导出成功！");
		} catch (Exception e) {
			log.error("error", e);
			dataMap.put("success", false);
			dataMap.put("msg", "导出失败！");
		}
		return SUCCESS;
	}

	/**
	 * 班别信息导入
	 * 
	 * @author 632898 李鹏
	 * @date 2014-06-20
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String importSchedule() {
		HashMap map = paramsMap();
		dataMap = new HashMap();
		try {
			map.put("file", upload);
			dataMap = this.schedulingBaseBiz.importSchedule(map);
		} catch (Exception e) {
			log.error("error", e);
			dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, "导入模版或数据格式错误！");
		}
		return SUCCESS;
	}

}