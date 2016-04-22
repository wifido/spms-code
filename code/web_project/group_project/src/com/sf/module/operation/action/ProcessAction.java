/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-30     632898 李鹏       创建
 **********************************************/

package com.sf.module.operation.action;

import static com.sf.module.common.domain.Constants.KEY_ERROR;
import static com.sf.module.common.domain.Constants.KEY_MESSAGE;
import static com.sf.module.common.domain.Constants.KEY_SUCCESS;
import static com.sf.module.operation.biz.ProcessBiz.DEPT_CODE;
import static com.sf.module.operation.biz.ProcessBiz.FIELD_DEPT_ID;
import static com.sf.module.operation.biz.ProcessBiz.FIELD_USER_ID;
import static com.sf.module.operation.biz.ProcessBiz.FILE;
import static com.sf.module.operation.biz.ProcessBiz.IS_ONLY;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.util.StringUtils;

import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.operation.biz.IProcessBiz;


/**
 * 
 * 工序管理的Action处理类
 * 
 * @author 632898 李鹏 2014-06-30
 * 
 */
public class ProcessAction extends BaseAction {

    private static final long serialVersionUID = 1L;

	/**
	 * 工序管理的业务接口
	 */
	private IProcessBiz processBiz;

	/**
	 * 设置工序管理的业务接口
	 */
	public void setProcessBiz(IProcessBiz processBiz) {
		this.processBiz = processBiz;
	}
	
	/**
	 * Ajax返回结果集
	 */
	private HashMap dataMap;

	public HashMap getDataMap() {
		return dataMap;
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
	
	/**
	 * 查询工序信息
	 * 
	 * @author 632898 李鹏
	 * @date 2014-06-30
	 * @return dataMap
	 */
	public String queryProcess(){
		// 获取前端参数
		HashMap map = paramsMap();
		// 抛到前端的数据
		dataMap = new HashMap();
		try {
			dataMap = processBiz.queryProcess(map);
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 查询总部工序信息
	 * 
	 * @author 664525 莫航
	 * @date 2014-07-23
	 * @return dataMap
	 */
	public String tbQueryProcess(){
		// 获取前端参数
		HashMap map = paramsMap();
		// 抛到前端的数据
		dataMap = new HashMap();
		try {
			dataMap = processBiz.tbQueryProcess(map);
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 查询用户所在网点工序信息
	 * 
	 * @author 632898 李鹏
	 * @date 2014-07-02
	 * @return dataMap
	 */
	public String queryUserProcess(){
		// 获取前端参数
		HashMap map = paramsMap();
		// 抛到前端的数据
		dataMap = new HashMap();
		try {
			dataMap = processBiz.queryUserProcess(map);
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 保存工序信息
	 * 
	 * @author 632898 李鹏
	 * @date 2014-06-30
	 * @return dataMap
	 */
    public String saveProcess() {
		// 获取前端参数
        HashMap map = paramsMap();
		// 抛到前端的数据
        dataMap = new HashMap();
        try {
			// 判断是否保存成功
            if (processBiz.saveProcess(map)) {
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

    public String updateProcess() {
		// 获取前端参数
        HashMap map = paramsMap();
		// 抛到前端的数据
        dataMap = new HashMap();
        try {
			// 判断是否更新成功
            if (processBiz.updateProcess(map)) {
                dataMap.put(KEY_SUCCESS, true);
				dataMap.put(KEY_MESSAGE, "更新成功！");
            } else {
                dataMap.put(KEY_SUCCESS, false);
				dataMap.put(KEY_MESSAGE, "更新失败！");
            }
        } catch (Exception e) {
            log.error(KEY_ERROR, e);
            dataMap.put(KEY_SUCCESS, false);
            dataMap.put(KEY_MESSAGE, e.getMessage().toString());
        }
        return SUCCESS;
    }

    /**
	 * 导出工序代码信息
	 * 
	 * @author 632898 李鹏
	 * @date 2014-07-01
	 * @return dataMap
	 */
	  public String exportProcess(){
		// 获取前段参数
			HashMap map = paramsMap();
		// 抛到前端的数据
			dataMap = new HashMap();
			try {
				dataMap = processBiz.exportProcess(map);
				dataMap.put(KEY_SUCCESS, true);
				dataMap.put(KEY_MESSAGE, "");
			} catch (Exception e) {
				log.error(KEY_ERROR, e);
			dataMap.put(KEY_MESSAGE, "导出失败！");
			}		  
		   return SUCCESS;
	  }

    /**
	 * 导入工序代码信息
	 * 
	 * @return dataMap
	 * @author 632898 李鹏
	 * @date 2014-07-01
	 */
    public String importProcess() {
		// 获取前端参数
        HashMap map = paramsMap();
		// 抛到前端的数据
        dataMap = new HashMap();
        try {
			// 获取上传的文件
            map.put(FILE, upload);
            dataMap = processBiz.importProcess(map);
        } catch (Exception e) {
            log.error(KEY_ERROR, e);
            dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, "导入模版或数据格式错误！");
        }
        return SUCCESS;
    }

    /**
	 * 工序确认
	 * 
	 * @author 632898 李鹏
	 * @date 2014-07-02
	 * @return dataMap
	 */
	  public String confirmProcess(){
		// 获取前端参数
			HashMap map = paramsMap();
		// 抛到前端的数据
			dataMap = new HashMap();
			try {
				dataMap = processBiz.confirmProcess(map);
			} catch (Exception e) {
				log.error(KEY_ERROR, e);
				dataMap.put(KEY_SUCCESS, false);
				dataMap.put(KEY_MESSAGE, e.getMessage());
			}			
			return SUCCESS;
	  }
	  
	  /**
	 * 判断工序的唯一性
	 * 
	 * @author 632898 李鹏
	 * @date 2014-07-03
	 * @param map
	 *            (key 属性名 ， value 值);
	 * @return
	 */
	  public String  isOnlyProcess(){
		// 获取前端参数
			HashMap map = paramsMap();
		// 抛到前端的数据
			dataMap = new HashMap();
			try {
				dataMap = processBiz.isOnlyProcess(map);
			} catch (Exception e) {
				log.error(KEY_ERROR, e);
				dataMap.put(KEY_SUCCESS, false);
				dataMap.put(IS_ONLY, false);
			}	
			return SUCCESS;
	  }
	  
	  /**
	 * 读取总部工序更新信息
	 * 
	 * @author 632898 李鹏
	 * @date 2014-07-09
	 * 
	 * @return
	 */
	   public String pushMsg(){
		// 抛到前端的数据
		   dataMap = new HashMap();
			try {
				dataMap = processBiz.pushMsg();
			} catch (Exception e) {
				log.error(KEY_ERROR, e);
				dataMap.put(KEY_SUCCESS, false);
				dataMap.put(KEY_MESSAGE,e.getMessage());
			}	
		   return SUCCESS;
	   }
	  
	  /**
	 * 读取总部工序更新信息
	 * 
	 * @author 632898 李鹏
	 * @date 2014-07-09
	 * 
	 * @return
	 */
      public String findByDeptId() {
		// 抛到前端的数据
          dataMap = new HashMap();
          HttpServletRequest request = ServletActionContext.getRequest();
          Long deptId = null;
          if (!StringUtils.isEmpty(request.getParameter(FIELD_DEPT_ID))) {
              deptId = Long.parseLong(request.getParameter(FIELD_DEPT_ID));
          } else if (!StringUtils.isEmpty(request.getParameter(DEPT_CODE))) {
              deptId = DepartmentCacheBiz.getDepartmentByCode(request.getParameter(DEPT_CODE)).getId();
          } else {
              dataMap.put(KEY_SUCCESS, false);
			dataMap.put(KEY_MESSAGE, "参数为空");
          }
          try {
              dataMap = processBiz.findByDeptId(deptId);
          } catch (Exception e) {
              log.error(KEY_ERROR, e);
              dataMap.put(KEY_SUCCESS, false);
              dataMap.put(KEY_MESSAGE, e.getMessage());
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
		map.put(FIELD_USER_ID,userId);
		return map;
	}
	
}