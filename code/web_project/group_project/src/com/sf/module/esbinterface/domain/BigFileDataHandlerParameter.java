package com.sf.module.esbinterface.domain;

import java.io.File;
import java.util.List;

public class BigFileDataHandlerParameter {

    private final String timeStamp;
    private final String journalId;
    /**
     * MD5值
     */
    protected String md5Code;
    /**
     * 远程文件路径
     */
    protected String remotePath;
    /**
     * 远程文件名称
     */
    protected String remoteFileName;
    /**
     * 是否压缩
     */
    protected String isZip;
    private String systemId;
    private String selfSystemId;
    private String dataType;
    private List<File> fileList;

    private boolean isSuccess;

    public BigFileDataHandlerParameter(boolean isSuccess, String systemId, String dataType, String selfSystemId, String timeStamp, String journalId, List<File> fileList, String md5Code, String remotePath, String remoteFileName, String isZip) {
        this.isSuccess = isSuccess;
        this.systemId = systemId;
        this.dataType = dataType;
        this.selfSystemId = selfSystemId;
        this.timeStamp = timeStamp;
        this.journalId = journalId;
        this.fileList = fileList;
        this.md5Code = md5Code;
        this.remotePath = remotePath;
        this.remoteFileName = remoteFileName;
        this.isZip = isZip;
    }

    public String getSelfSystemId() {
        return selfSystemId;
    }

    public void setSelfSystemId(String selfSystemId) {
        this.selfSystemId = selfSystemId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getJournalId() {
        return journalId;
    }

    public String getMd5Code() {
        return md5Code;
    }

    public void setMd5Code(String md5Code) {
        this.md5Code = md5Code;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getRemoteFileName() {
        return remoteFileName;
    }

    public void setRemoteFileName(String remoteFileName) {
        this.remoteFileName = remoteFileName;
    }

    public String getIsZip() {
        return isZip;
    }

    public void setIsZip(String isZip) {
        this.isZip = isZip;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
