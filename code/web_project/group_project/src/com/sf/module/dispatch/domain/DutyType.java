package com.sf.module.dispatch.domain;

public enum DutyType {
    // 用工类型(1-非全日制工、2-基地见习生、3-劳务派遣、4-全日制员工、5-实习生、6-外包
    // 、7-勤工助学、8-代理、9-个人承包经营者、10-业务外包)
    UNKNOWN(""),
    HOURLY_WORKER("非全日制工"),
    BASE_TRAINEE("基地见习生"),
    LABOR_DISPATCH("劳务派遣"),
    FULL_TIME("全日制员工"),
    TRAINEE("实习生"),
    OUTSOURCING("外包"),
    THEPARTTIMEJOB("勤工助学"),
    AGENT("代理"),
    INDIVIDUAL_CONTRACTORS("个人承包经营者"),
    UTSOURCING("业务外包"),
    NOEMPLOYEE("非员工");

    public final String name;

    DutyType(String name) {
        this.name = name;
    }
}