package com.sf.module.common.action;

import static com.sf.module.common.util.ServletActionHelper.getHttpRequestParameter;

import java.util.HashMap;

import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.common.biz.ProcedureExecutionLogBiz;
public class ProcedureExecutionLogAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ProcedureExecutionLogBiz procedureExecutionLogBiz;
	
	private HashMap<String,Object> resultMap;

	public String query() {
		setResultMap(procedureExecutionLogBiz.query(getHttpRequestParameter()));
		return SUCCESS;
	}

	public HashMap<String,Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(HashMap<String,Object> resultMap) {
		this.resultMap = resultMap;
	}

	public ProcedureExecutionLogBiz getProcedureExecutionLogBiz() {
		return procedureExecutionLogBiz;
	}

	public void setProcedureExecutionLogBiz(ProcedureExecutionLogBiz procedureExecutionLogBiz) {
		this.procedureExecutionLogBiz = procedureExecutionLogBiz;
	}

}
