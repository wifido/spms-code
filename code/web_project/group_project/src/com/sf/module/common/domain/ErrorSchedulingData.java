package com.sf.module.common.domain;

import com.sf.framework.base.domain.BaseEntity;

public class ErrorSchedulingData extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ID;
	private String EMP_CODE;
	private String BEGIN_DATE;
	private String END_DATE;
	private String BEGIN_TM;
	private String END_TM;
	private String TMR_DAY_FLAG;
	private String OFF_DUTY_FLAG;
	private String CLASS_SYSTEM;
	private String THEME_NAME;
	private String ERROR_INFO;
	private String LASTUPDATE;
	private String EXTRAINFO1;
	private String EXTRAINFO2;
	private String EXTRAINFO3;
	private String DEAL_FLAG;
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getEMP_CODE() {
		return EMP_CODE;
	}
	public void setEMP_CODE(String eMP_CODE) {
		EMP_CODE = eMP_CODE;
	}
	public String getBEGIN_DATE() {
		return BEGIN_DATE;
	}
	public void setBEGIN_DATE(String bEGIN_DATE) {
		BEGIN_DATE = bEGIN_DATE;
	}
	public String getEND_DATE() {
		return END_DATE;
	}
	public void setEND_DATE(String eND_DATE) {
		END_DATE = eND_DATE;
	}
	public String getBEGIN_TM() {
		return BEGIN_TM;
	}
	public void setBEGIN_TM(String bEGIN_TM) {
		BEGIN_TM = bEGIN_TM;
	}
	public String getEND_TM() {
		return END_TM;
	}
	public void setEND_TM(String eND_TM) {
		END_TM = eND_TM;
	}
	public String getTMR_DAY_FLAG() {
		return TMR_DAY_FLAG;
	}
	public void setTMR_DAY_FLAG(String tMR_DAY_FLAG) {
		TMR_DAY_FLAG = tMR_DAY_FLAG;
	}
	public String getOFF_DUTY_FLAG() {
		return OFF_DUTY_FLAG;
	}
	public void setOFF_DUTY_FLAG(String oFF_DUTY_FLAG) {
		OFF_DUTY_FLAG = oFF_DUTY_FLAG;
	}
	public String getCLASS_SYSTEM() {
		return CLASS_SYSTEM;
	}
	public void setCLASS_SYSTEM(String cLASS_SYSTEM) {
		CLASS_SYSTEM = cLASS_SYSTEM;
	}
	public String getTHEME_NAME() {
		return THEME_NAME;
	}
	public void setTHEME_NAME(String tHEME_NAME) {
		THEME_NAME = tHEME_NAME;
	}
	public String getERROR_INFO() {
		return ERROR_INFO;
	}
	public void setERROR_INFO(String eRROR_INFO) {
		ERROR_INFO = eRROR_INFO;
	}
	public String getLASTUPDATE() {
		return LASTUPDATE;
	}
	public void setLASTUPDATE(String lASTUPDATE) {
		LASTUPDATE = lASTUPDATE;
	}
	public String getEXTRAINFO1() {
		return EXTRAINFO1;
	}
	public void setEXTRAINFO1(String eXTRAINFO1) {
		EXTRAINFO1 = eXTRAINFO1;
	}
	public String getEXTRAINFO2() {
		return EXTRAINFO2;
	}
	public void setEXTRAINFO2(String eXTRAINFO2) {
		EXTRAINFO2 = eXTRAINFO2;
	}
	public String getEXTRAINFO3() {
		return EXTRAINFO3;
	}
	public void setEXTRAINFO3(String eXTRAINFO3) {
		EXTRAINFO3 = eXTRAINFO3;
	}
	public String getDEAL_FLAG() {
		return DEAL_FLAG;
	}
	public void setDEAL_FLAG(String dEAL_FLAG) {
		DEAL_FLAG = dEAL_FLAG;
	}
	
	
}
