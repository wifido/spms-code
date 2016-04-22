/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-20     zhaochangjin       创建
 **********************************************/

package com.sf.module.operation.action;

import static com.sf.module.common.domain.Constants.FIELD_FLAG;
import static com.sf.module.common.domain.Constants.FIELD_QUERY_OUT_EMPLOYEE;
import static com.sf.module.common.domain.Constants.KEY_ERROR;
import static com.sf.module.common.domain.Constants.KEY_FILE;
import static com.sf.module.common.domain.Constants.KEY_MESSAGE;
import static com.sf.module.common.domain.Constants.KEY_SUCCESS;
import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.domain.Constants.TOTAL_SIZE;

import java.io.File;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.sf.module.common.action.SpmsBaseAction;
import com.sf.module.operation.biz.IOutEmployeeBiz;
import com.sf.module.operation.domain.OutEmployee;

/**
 * 
 * 外包人员实体的Action处理类
 * 
 * @author zhaochangjin 2014-06-20
 * 
 */
public class OutEmployeeAction extends SpmsBaseAction {

	private static final String SUCESS_BATCH_MODIFY_EMPLOYEE_GROUP = "批量修改成功！";

	private static final long serialVersionUID = 1L;

	/**
	 * Ajax请求成功的响应标志
	 */
	private Boolean success = true;
	private OutEmployee emp;
	private Collection emps;
	private long totalSize;
	private int limit;
	private int start;
	private String fileName;
	private String msg;
	private String importDeptId;
	private String groupimportDeptId;

	public String getGroupimportDeptId() {
		return groupimportDeptId;
	}

	public void setGroupimportDeptId(String groupimportDeptId) {
		this.groupimportDeptId = groupimportDeptId;
	}

	public String getImportDeptId() {
		return importDeptId;
	}

	public void setImportDeptId(String importDeptId) {
		this.importDeptId = importDeptId;
	}

	/**
	 * 外包人员实体的业务接口
	 */
	private IOutEmployeeBiz outEmployeeBiz;

	/**
	 * 获取Ajax请求成功的响应标志
	 */
	public Boolean getSuccess() {
		return this.success;
	}

	/**
	 * 文件上传
	 */
	private File upload;

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * Ajax返回结果集
	 */
	private HashMap dataMap;

	public HashMap getDataMap() {
		return dataMap;
	}

	private List dataList;

	public List getDataList() {
		return dataList;
	}

	/**
	 * 设置外包人员实体的业务接口
	 */
	public void setOutEmployeeBiz(IOutEmployeeBiz outEmployeeBiz) {
		this.outEmployeeBiz = outEmployeeBiz;
	}

	public OutEmployee getEmp() {
		return emp;
	}

	public Collection getEmps() {
		return emps;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public int getLimit() {
		return limit;
	}

	public int getStart() {
		return start;
	}

	public void setEmp(OutEmployee emp) {
		this.emp = emp;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String listEmp() {
		if (emp != null) {
			Map map = outEmployeeBiz.findDriversForPage(emp.getEmpCode(), emp.getDeptId(), emp.getEmpName(), emp.getGroupId(), limit, start);
			emps = (Collection) map.get(ROOT);
			totalSize = Long.valueOf(map.get(TOTAL_SIZE).toString());
		}
		return SUCCESS;
	}

	/**
	 * 查询外包人员信息
	 * 
	 * @author 632898 李鹏
	 * @date 2014-06-21
	 * @return dataMap
	 */
	public String queryOutEmployee() {
		// 获取前端参数
		HashMap map = paramsMap();
		// 抛到前端的数据
		dataMap = new HashMap();
		try {
			map.put(FIELD_FLAG, FIELD_QUERY_OUT_EMPLOYEE);
			dataMap = outEmployeeBiz.queryOutEmployee(map);
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, e.getMessage());
		}
		return SUCCESS;
	}

	/**
	 * 查询外包人员自动生成的工号
	 * 
	 * @date 2014-06-21
	 * @author 632898 李鹏
	 * @return dataMap
	 */
	public String queryInsertEmpCode() {
		// 抛到前端的数据
		dataMap = new HashMap();
		try {
			dataMap = this.outEmployeeBiz.getInsertEmpCode();
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, e.getMessage());
		}
		return SUCCESS;
	}

	/**
	 * 通过网点取小组信息
	 * 
	 * @author 632898 李鹏
	 * @date 2014-06-22
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String queryGroupByDeptId() {
		// 获取前段参数
		HashMap map = paramsMap();
		try {
			dataList = this.outEmployeeBiz.getGoupByDeptId(map);
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
		}
		return SUCCESS;
	}

	/**
	 * 通过网点取小组信息
	 * 
	 * @author 632898 李鹏
	 * @date 2014-06-22
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String queryEmployeeGroupByDeptId() {
		// 获取前段参数
		HashMap map = paramsMap();
		try {
			dataList = this.outEmployeeBiz.queryGroupWithEmptyGroup(map);
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
		}
		return SUCCESS;
	}

	/**
	 * 保存人员基础信息
	 * 
	 * @author 632898 李鹏
	 * @date 2014-06-22
	 * @return dataMap
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String saveEmployee() {
		// 获取前段参数
		HashMap map = paramsMap();
		// 抛到前端的数据
		dataMap = new HashMap();
		try {
			// 判断是否保存成功
			if (outEmployeeBiz.saveEmployee(map)) {
				dataMap.put(KEY_SUCCESS, true);
				dataMap.put(KEY_MESSAGE, "保存成功！");
			} else {
				dataMap.put(KEY_SUCCESS, false);
				dataMap.put(KEY_MESSAGE, "保存失败！");
			}
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, e.getMessage().toString());
		}
		return SUCCESS;
	}

	/**
	 * 更新人员基础信息
	 * 
	 * @author 632898 李鹏
	 * @date 2014-06-22
	 * @return dataMap
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String updateEmployee() {
		// 获取前段参数
		HashMap map = paramsMap();
		// 抛到前端的数据
		dataMap = new HashMap();
		try {
			// 判断是否更新成功
			if (outEmployeeBiz.updateEmployee(map)) {
				dataMap.put(KEY_SUCCESS, true);
				dataMap.put(KEY_MESSAGE, "修改成功！");
			} else {
				dataMap.put(KEY_SUCCESS, false);
				dataMap.put(KEY_MESSAGE, "修改失败！");
			}
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, e.getMessage());
		}
		return SUCCESS;
	}

	/**
	 * 删除人员基础信息
	 * 
	 * @author 632898 李鹏
	 * @date 2014-06-22
	 * @return dataMap
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String deleteEmployee() {
		// 获取前段参数
		HashMap map = paramsMap();
		// 抛到前端的数据
		dataMap = new HashMap();
		try {
			// 判断是否删除成功
			if (outEmployeeBiz.deleteEmployee(map)) {
				dataMap.put(KEY_SUCCESS, true);
				dataMap.put(KEY_MESSAGE, "删除成功！");
			} else {
				dataMap.put(KEY_SUCCESS, false);
				dataMap.put(KEY_MESSAGE, "删除失败！");
			}
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, e.getMessage());
		}
		return SUCCESS;
	}

	/**
	 * 导出人员基础信息
	 * 
	 * @author 632898 李鹏
	 * @date 2014-06-22
	 * @return dataMap
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String exportEmployee() {
		// 获取前段参数
		HashMap map = paramsMap();
		// 抛到前端的数据
		dataMap = new HashMap();
		try {
			dataMap = outEmployeeBiz.exportEmployee(map);
			dataMap.put(KEY_SUCCESS, true);
			dataMap.put(KEY_MESSAGE, "");
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, "导出失败！");
		}
		return SUCCESS;
	}

	/**
	 * 导入人员基础信息
	 * 
	 * @author 632898 李鹏
	 * @date 2014-06-23
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String importEmployee() {
		// 获取前段参数
		HashMap map = paramsMap();
		// 抛到前端的数据
		dataMap = new HashMap();
		try {
			// 获取上传的文件
			map.put("file", upload);
			dataMap = outEmployeeBiz.importEmployee(map);
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, "导入模版或数据格式错误！");
		}
		return SUCCESS;
	}

	public String importEmployeeAttribute() {
		HashMap queryMap = paramsMap();
		try {
			dataMap = new HashMap();
			queryMap.put(KEY_FILE, upload);
			dataMap = outEmployeeBiz.importEmployeeAttribute(queryMap);
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, "导入模版或数据格式错误！");
		}
		return SUCCESS;
	}

	/**
	 * 查询需要同步的数据
	 * 
	 * @author 632898 李鹏
	 * @date 2014-07-10
	 * @return
	 */
	public String queryHrEmp() {
		// 获取前段参数
		HashMap map = paramsMap();
		// 抛到前端的数据
		dataMap = new HashMap();
		try {
			dataMap = outEmployeeBiz.queryHrEmp(map);
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, e.getMessage());
		}
		return SUCCESS;
	}

	/**
	 * 人员信息同步
	 * 
	 * @author 632898 李鹏
	 * @date 2014-06-22
	 * @return dataMap
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String synEmployee() {
		// 获取前段参数
		HashMap map = paramsMap();
		// 抛到前端的数据
		dataMap = new HashMap();
		boolean opera = false;
		try {
			if (map.get("empStus").equals("1")) { // 同步为新增
				opera = outEmployeeBiz.saveEmployee(map);
			} else if (map.get("empStus").equals("2")) {// 同步为更新 换网点
				map.put("isOutEmployee", false);
				map.put("isSynEmployee", true);
				opera = outEmployeeBiz.updateEmployee(map);
			}
			// 判断是否保存成功
			if (opera) {
				if (outEmployeeBiz.updateSynHrEmp(map)) {
					dataMap.put(KEY_SUCCESS, true);
					dataMap.put(KEY_MESSAGE, "同步成功！");
				} else {
					dataMap.put(KEY_SUCCESS, false);
					dataMap.put(KEY_MESSAGE, "同步失败！");
				}
			} else {
				dataMap.put(KEY_SUCCESS, false);
				dataMap.put(KEY_MESSAGE, "同步失败！");
			}
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, "同步失败！错误信息如下：" + e.getMessage().toString());
		}
		return SUCCESS;
	}

	/**
	 * 为还未对需同步的人员发邮件提醒
	 * 
	 * @author 632898 李鹏
	 * @date 2014-07-15 每天凌晨两点自动执行
	 */
	@SuppressWarnings("unused")
	public void synEmployeeEmail() {
		Boolean opera = outEmployeeBiz.synEmployeeEmail();
	}

	/**
	 * 删除用户时判断用户是否有排班
	 * 
	 * @author 632898 李鹏
	 * @date 2014-07-18
	 */
	public String isSchedulMgtbyEmpCode() {
		HttpServletRequest request = ServletActionContext.getRequest();
		dataMap = new HashMap();
		try {
			String[] empCodes = request.getParameter("empCodes").split("@@");
			dataMap = (HashMap) outEmployeeBiz.isSchedulMgtbyEmpCode(empCodes);
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, e.getMessage().toString());
		}
		return SUCCESS;
	}

	/**
	 * 批量修改人员分组信息
	 * 
	 * @return dataMap
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String updateBtnBatchModify() {
		try {
			dataMap = new HashMap();
			outEmployeeBiz.batchUpdateEmployeeGroup(paramsMap());
			dataMap.put(KEY_SUCCESS, true);
			dataMap.put(KEY_MESSAGE, SUCESS_BATCH_MODIFY_EMPLOYEE_GROUP);
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, e.getMessage());
		}
		return SUCCESS;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String groupImportEmployee() {
		// 获取前段参数
		HashMap map = paramsMap();
		// 抛到前端的数据
		dataMap = new HashMap();
		try {
			// 获取上传的文件
			map.put("file", upload);
			dataMap = outEmployeeBiz.groupImportEmployee(map);
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, "导入模版或数据格式错误！");
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
		// 在参数中添加当前用户ID
		Long userId = super.getUserId();
		map.put("userId", userId);
		return map;
	}

}