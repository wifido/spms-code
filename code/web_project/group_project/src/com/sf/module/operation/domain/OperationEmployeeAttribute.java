package com.sf.module.operation.domain;

import java.util.Date;

public class OperationEmployeeAttribute {
    private Long id;
    private String groupId;
    private String empCode;
    private String siteCode;
    private Date createTm;
    private Date modifiedTm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTm() {
        return createTm;
    }

    public void setCreateTm(Date createTm) {
        this.createTm = createTm;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Date getModifiedTm() {
        return modifiedTm;
    }

    public void setModifiedTm(Date modifiedTm) {
        this.modifiedTm = modifiedTm;
    }
}
