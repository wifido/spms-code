package com.sf.module.common.domain;

import java.util.Date;

import com.sf.framework.base.domain.BaseEntity;

public class ProcedureExecutionLog extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private Long seqNo;  //异常发生时有关流水号的值
	private String procedureName;//过程名称
	private Date exceptionTm;//异常发生时间
	private String exceptionCode;//异常代码
	private String exceptionDesc;//异常描述
	private String exceptionRemk;//异常备注:BEGIN表示开始,END表示结束,ERROR表示失败
	private Long lineNo;//发生异常的位置(行号)
	private String packageName;//包名称
	private Long callSno;//调用序号
	
	public Long getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}
	public Long getLineNo() {
		return lineNo;
	}
	public void setLineNo(Long lineNo) {
		this.lineNo = lineNo;
	}
	public Long getCallSno() {
		return callSno;
	}
	public void setCallSno(Long callSno) {
		this.callSno = callSno;
	}
	public String getProcedureName() {
		return procedureName;
	}
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}
	public Date getExceptionTm() {
		return exceptionTm;
	}
	public void setExceptionTm(Date exceptionTm) {
		this.exceptionTm = exceptionTm;
	}
	public String getExceptionCode() {
		return exceptionCode;
	}
	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	public String getExceptionDesc() {
		return exceptionDesc;
	}
	public void setExceptionDesc(String exceptionDesc) {
		this.exceptionDesc = exceptionDesc;
	}
	public String getExceptionRemk() {
		return exceptionRemk;
	}
	public void setExceptionRemk(String exceptionRemk) {
		this.exceptionRemk = exceptionRemk;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
}
