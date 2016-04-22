/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-23     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.biz;

import java.io.File;
import java.text.ParseException;
import java.util.Map;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.biz.IBiz;
import com.sf.module.vmsarrange.domain.ArrDriver;
import com.sf.module.vmsarrange.domain.PreScheduleDraft;

/**
 *
 * IPreScheduleDraftBiz处理类
 *
 */
public interface IPreScheduleDraftBiz extends IBiz {

	/**
	 * 根据id，年月日查询排班明细
	 * @param id
	 * @param yearMonth
	 * @param dayNum
	 * @return
	 */
	Map findByCondition(Long id, String yearMonth, Integer dayNum);
	
	/**
	 * 导出打印预排班信息
	 * @param deptCode
	 * @param empCode
	 * @param yearMonth
	 * @return
	 */
	public String listReport(String deptCode, String empCode,String yearMonth,Integer classType)throws Exception;
	/**
	 * 导出预排班
	 * @param deptCode
	 * @param empCode
	 * @param yearMonth
	 * @return
	 */
	public String listPreReport(String deptCode, String empCode,String yearMonth,Integer classType) throws Exception;
	/**
	 * 导出实际排班
	 * @param deptCode
	 * @param empCode
	 * @param yearMonth
	 * @return
	 */
	public String listRealReport(String deptCode, String empCode,String yearMonth,Integer classType) throws Exception;

	 /**
	 * 上传文件
	 * @param uploadFile
	 * @param fileName
	 * @throws ParseException 
	 */
	void saveFile(File uploadFile, String fileName) throws ParseException;
	/**
	 * 查找驾驶员分页数据
	 * @param deptId
	 * @param empCode
	 * @param yearMonth
	 * @param userId
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	public IPage<ArrDriver> listPageBy(Long deptId, String empCode,int pageSize,int pageIndex);

}
