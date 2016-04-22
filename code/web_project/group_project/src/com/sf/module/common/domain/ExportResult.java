package com.sf.module.common.domain;

import com.sf.framework.base.domain.BaseEntity;

public class ExportResult extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private int totalCount;
	private String message;
	private boolean isSuccessed;
	private String filePath;

	public void buildErrorMessage(String message) {
		this.message = message;
	}

	public void setFilePathAfterSuccessed(String filePath) {
		this.filePath = filePath;
		this.isSuccessed = true;
	}

	// getter and setter
	public int getTotalCount() {
		return totalCount;
	}

	public String getMessage() {
		return message;
	}

	public boolean getIsSuccessed() {
		return isSuccessed;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
