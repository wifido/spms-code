package com.sf.module.common.domain;

public class ImportResult {
	private int totalCount;
	private int successCount;
	private int failCount;
	private String msg;
	private String errorDataDownloadPath;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getErrorDataDownloadPath() {
		return errorDataDownloadPath;
	}

	public void setErrorDataDownloadPath(String errorDataDownloadPath) {
		this.errorDataDownloadPath = errorDataDownloadPath;
	}

}
