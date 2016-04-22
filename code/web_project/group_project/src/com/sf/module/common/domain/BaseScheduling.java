package com.sf.module.common.domain;

import com.sf.framework.base.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public class BaseScheduling extends BaseEntity {
    private String areaCode;
    private String departmentCode;
    private String userCode;
    private String username;
    private String userType;
    private List<String> daysScheduling;

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAreaCode() {

        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public void setDaysScheduling(List<String> daysScheduling) {
        this.daysScheduling = daysScheduling;
    }

    public List<String> getDaysScheduling() {
        return daysScheduling;
    }
}
