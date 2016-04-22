package com.sf.module.esbinterface.domain;

import com.sf.framework.base.domain.BaseEntity;

import java.util.Date;

public class ScheduleWithErrorInfo extends BaseEntity {
    private String empCode;
    private String beginDate;
    private String endDate;
    private String beginTime;
    private String endTime;

    private String dayFlag;
    private String offDutyFlag;
    private String classSystem;
    private Date createTime;
    private String nodeKey;
    private int stateFlag;
    private String error;

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDayFlag() {
        return dayFlag;
    }

    public void setDayFlag(String dayFlag) {
        this.dayFlag = dayFlag;
    }

    public String getOffDutyFlag() {
        return offDutyFlag;
    }

    public void setOffDutyFlag(String offDutyFlag) {
        this.offDutyFlag = offDutyFlag;
    }

    public String getClassSystem() {
        return classSystem;
    }

    public void setClassSystem(String classSystem) {
        this.classSystem = classSystem;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    public int getStateFlag() {
        return stateFlag;
    }

    public void setStateFlag(int stateFlag) {
        this.stateFlag = stateFlag;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
