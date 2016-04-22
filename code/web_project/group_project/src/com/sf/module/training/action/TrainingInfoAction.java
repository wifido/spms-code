package com.sf.module.training.action;

import java.io.File;
import java.util.HashMap;

import com.sf.module.common.action.SpmsBaseAction;
import com.sf.module.common.util.ServletActionHelper;
import com.sf.module.training.biz.TrainingInfoBiz;

public class TrainingInfoAction extends SpmsBaseAction {

	private static final long serialVersionUID = 1L;

	private TrainingInfoBiz trainingInfoBiz;

	private HashMap<String, Object> dataMap;

	private File uploadFile;

	private boolean success = true;
	
	private String fileName;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public HashMap<String, Object> getDataMap() {
		return dataMap;
	}

	public TrainingInfoBiz getTrainingInfoBiz() {
		return trainingInfoBiz;
	}

	public void setTrainingInfoBiz(TrainingInfoBiz trainingInfoBiz) {
		this.trainingInfoBiz = trainingInfoBiz;
	}

	public String query() {
		dataMap = trainingInfoBiz.query(getHttpRequestParameter());
		return SUCCESS;
	}

	public String delete() {
		dataMap = new HashMap<String, Object>();
		if (trainingInfoBiz.delete(getHttpRequestParameter())) {
			dataMap.put("success", true);
			dataMap.put("msg", "删除成功！");
		} else {
			dataMap.put("success", false);
			dataMap.put("msg", "删除失败！");
		}
		return SUCCESS;
	}

	public String importTraining() {
		dataMap = trainingInfoBiz.importTraining(uploadFile, getHttpRequestParameter());
		return SUCCESS;
	}

	public String export() {
		fileName = trainingInfoBiz.exporTraining(getHttpRequestParameter());
		success = true;
		return SUCCESS;
	}

	public HashMap<String, String> getHttpRequestParameter() {
		return ServletActionHelper.getHttpRequestParameter();
	}
}
