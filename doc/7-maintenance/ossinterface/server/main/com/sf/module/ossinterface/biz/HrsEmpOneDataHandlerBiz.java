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

import com.sf.module.ossinterface.domain.HrEmpInfoAlter;
import com.sf.module.ossinterface.util.HRSXmlMapping;

/**
 * 员工-不保密增量：新员工入职或者员工离职/员工的岗位调动或者部门调动
 * 
 * @author 文俊 (337291) Jun 17, 2014
 */
public class HrsEmpOneDataHandlerBiz extends HRSBigFileDataHandlerBiz<HrEmpInfoAlter> {
	
	private IHrEmpInfoAlterBiz hrEmpInfoAlterBiz;

	/**
	 * @author 文俊 (337291)
	 * @date Jun 24, 2014 
	 * @param to set hrEmpInfoAlterBiz the hrEmpInfoAlterBiz 
	 */
	public void setHrEmpInfoAlterBiz(IHrEmpInfoAlterBiz hrEmpInfoAlterBiz) {
		this.hrEmpInfoAlterBiz = hrEmpInfoAlterBiz;
	}

	
	/**
	 * <emps size="9999" batch_number="20140619144107" errmsg="">
	 * @author 文俊 (337291) Jun 21, 2014
	 */
	class HrEmpInfoAlterXmlMapping extends HRSXmlMapping<HrEmpInfoAlter> {
		
		/**
		 * @author 文俊 (337291)
		 * @date Jun 24, 2014 
		 * @param journalId
		 */
		public HrEmpInfoAlterXmlMapping(String journalId) {
			super(journalId);
		}


		
		public HrEmpInfoAlter newEntity() {
			HrEmpInfoAlter empInfo = new HrEmpInfoAlter();
			empInfo.setJournalId(journalId);
			empInfo.setBatchNumber(batchNumber);
			empInfo.setErrmsg(errmsg);
			empInfo.setXmlSize(size);
			return empInfo;
		}
		 
	
		public List<HrEmpInfoAlter> init() {
			return this.data = new ArrayList<HrEmpInfoAlter>(MAX_SIZE);
		}


	
		public void saveBatch(List<HrEmpInfoAlter> entities) {
			hrEmpInfoAlterBiz.save(entities);
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

	@Override
	protected HRSXmlMapping<HrEmpInfoAlter> getMapping(String journalId) {
		return new HrEmpInfoAlterXmlMapping(journalId);
	}
	
}
