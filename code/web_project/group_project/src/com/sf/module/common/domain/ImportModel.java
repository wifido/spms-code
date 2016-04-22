package com.sf.module.common.domain;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.ArrayUtils;

public class ImportModel {
	private int rowIndex;
	private Set<String> errorInfo = new HashSet<String>();
	private boolean validPass = true;
	private String endTime;
	private String startTime;

	public boolean isValidPass() {
		return validPass;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public void appendErrorMsg(String msg) {
		errorInfo.add(msg + "\r\n");
		validPass = false;
	}

	public String getErrorMsg() {
		return errorInfo.isEmpty() ? "" : ArrayUtils.toString(errorInfo);
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
}
