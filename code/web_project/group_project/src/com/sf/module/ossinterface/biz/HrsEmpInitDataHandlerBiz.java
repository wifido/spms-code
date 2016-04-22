/***********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * 
 * HISTORY
 ********************************************************
 *  ID    DATE               PERSON             REASON
 *  1     Jun 17, 2014           文俊 (337291)       创建 
 ********************************************************/
package com.sf.module.ossinterface.biz;

import java.util.ArrayList;
import java.util.List;

import com.sf.module.ossinterface.domain.HrEmpInfo;
import com.sf.module.ossinterface.util.HRSXmlMapping;

/**
 * 初始化员工数据：当前在职员工全量+预生效员工数据
 * 
 * @author 文俊 (337291) Jun 17, 2014
 */
public class HrsEmpInitDataHandlerBiz extends HRSBigFileDataHandlerBiz<HrEmpInfo> {
	
	private IHrEmpInfoBiz hrEmpInfoBiz;
	
	/**
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014 
	 * @param to set hrEmpInfoBiz the hrEmpInfoBiz 
	 */
	public void setHrEmpInfoBiz(IHrEmpInfoBiz hrEmpInfoBiz) {
		this.hrEmpInfoBiz = hrEmpInfoBiz;
	}
	

//	public boolean handler(BigFileDataHandlerParament handlerParament) {
//		boolean handler = super.handler(handlerParament);
//		
//		/**
//		 * 调用初始化emp信息存储过程
//		 * 事物在存储过程里面控制
//		 */
//		this.hrEmpInfoBiz.initTmEmployee(handlerParament.getJournalId());
//		
//		return handler;
//	}

	/**
	 * <emps size="9999" batch_number="20140619144107" errmsg="">
	 * @author 文俊 (337291) Jun 21, 2014
	 */
	class HrEmpXmlMapping extends HRSXmlMapping<HrEmpInfo> {
		/**
		 * @author 文俊 (337291)
		 * @date Jun 24, 2014 
		 * @param journalId
		 */
		public HrEmpXmlMapping(String journalId) {
			super(journalId);
		}

		public HrEmpInfo newEntity() {
			HrEmpInfo empInfo = new HrEmpInfo();
			empInfo.setJournalId(journalId);
			empInfo.setBatchNumber(batchNumber);
			empInfo.setErrmsg(errmsg);
			empInfo.setXmlSize(size);
			return empInfo;
		}

		public List<HrEmpInfo> init() {
			return this.data = new ArrayList<HrEmpInfo>(MAX_SIZE);
		}

		public void saveBatch(List<HrEmpInfo> entities) {
			hrEmpInfoBiz.save(entities);
			this.savedSize += entities.size();
		}
		
	}

    /**
     * <pre>
     * 根据流水号(journalId)处理失败,回滚
     * </pre>
     * @author 文俊 (337291)
     * @date Jun 24, 2014 
     * @param journalId
     */

	public void tranData(String journalId) {
		System.out.println(journalId);
	}


	protected HRSXmlMapping<HrEmpInfo> getMapping(String journalId) {
		return new HrEmpXmlMapping(journalId);
	}
	
}
