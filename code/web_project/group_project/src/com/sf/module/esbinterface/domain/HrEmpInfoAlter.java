/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-17     wen.jun       创建
 **********************************************/

package com.sf.module.esbinterface.domain;

import com.sf.framework.base.domain.BaseEntity;
import com.sf.module.esbinterface.util.DeCoder;

import javax.xml.bind.annotation.XmlElement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * HR系统接口增量,新员工入职或者员工离职/员工的岗位调动或者部门调动
 *
 * @author wen.jun  2014-06-17
 */
public class HrEmpInfoAlter extends BaseEntity {
    /**
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    private static final long serialVersionUID = 1L;

    /**
     * BATCH_NUMBER	VARCHAR2(200)	Y			文件批次号<pre>
     * </pre>
     *
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     * @return
     */
    private String batchNumber;

    /**
     * 注销日期
     * HRS离职注销日期
     */
    private String cancelDate;

    /**
     * CANCEL_FLAG VARCHAR2(20) Y 离职标识N=否,Y=是
     */
    private String cancelFlag;

    /**
     * 网络网点编号
     */
    private String deptCode;

    /**
     * EFFECTIVE_DATE	DATE	Y			生效日期
     */
    private Date effectiveDate;

    /**
     * 职员代码（工号）
     */
    private String empCode;

    /**
     * 描述
     */
    private String empDesc;

    /**
     * 职员岗位
     */
    private String empDutyName;


    /**
     * 电子邮箱
     */
    private String empEmail;
    /**
     * 性别:M=男、F=女
     */
    private String empGender;

    /**
     * 手机号码
     */
    private String empMobile;
    /**
     * 姓名
     */
    private String empName;

    /**
     * 办公电话
     */
    private String empOfficephone;


    /**
     * 职员类别
     */
    private String empTypeName;

    /**
     * 转网络生效时间
     */
//	private String changeZoneTm;


    /**
     * ERRMSG	VARCHAR2(1000)	Y			错误信息<pre>
     * </pre>
     *
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     * @return
     */
    private String errmsg;

    /**
     * HRS_EMP_ID	VARCHAR2(50)	Y			hrs职员id
     */
    private String hrsEmpId;

    /**
     * HRS_PERSON_ID	VARCHAR2(50)	Y			hrs员工ID
     */
    private String hrsPersonId;

    /**
     * JOURNAL_ID	VARCHAR2(200)	Y			流水号(处理批次号)
     */
    private String journalId;

    private Date logoutDt;

    /**
     * PERSON_TYPE	VARCHAR2(50)	Y		人员类型
     */
    private String personType;

    /**
     * POSITION_NAME	VARCHAR2(200)	Y		职位名称
     */
    private String positionName;

    private Date registerDt;

    /**
     * 注册日期
     * HRS顺丰入职日期
     */
    private String sfDate;

    /**
     * XML_SIZE	NUMBER(10)	Y			文件数据量
     */
    private Long xmlSize;

    /**
     * 岗位序列
     */
    private String dutySerial;

    /**
     * @return the batchNumber
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    public String getBatchNumber() {
        return batchNumber;
    }

    /**
     * @param to set batchNumber the batchNumber
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    public void setBatchNumber(String batchNumber) {
        this.batchNumber = substring(200, batchNumber);
    }

    /**
     * @return the cancelDate
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    public String getCancelDate() {
        return cancelDate;
    }

    /**
     * @param to set cancelDate the cancelDate
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    @XmlElement(name = "CANCEL_DATE")
    public void setCancelDate(String cancelDate) throws Exception {
        this.cancelDate = substring(20, cancelDate);
        if (this.cancelDate != null && this.cancelDate.trim().length() > 0) {
            this.logoutDt = new SimpleDateFormat("yyyy-MM-dd").parse(this.cancelDate.trim());
        }
    }

    /**
     * @return the cancelFlag
     * @author 文俊 (337291)
     * @date Jun 26, 2014
     */
    public String getCancelFlag() {
        return cancelFlag;
    }

    /**
     * @param to set cancelFlag the cancelFlag
     * @author 文俊 (337291)
     * @date Jun 26, 2014
     */
    @XmlElement(name = "CANCEL_FLAG")
    public void setCancelFlag(String cancelFlag) {
        this.cancelFlag = cancelFlag;
    }

    /**
     * 获取网络网点编号
     */
    public String getDeptCode() {
        return this.deptCode;
    }

    /**
     * 设置网络网点编号
     */
    @XmlElement(name = "NET_CODE")
    public void setDeptCode(String deptCode) {
        this.deptCode = substring(30, deptCode);
    }

    /**
     * @return the effectiveDate
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * @param to set effectiveDate the effectiveDate
     * @throws Exception
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    @XmlElement(name = "EFFECTIVE_DATE")
    public void setEffectiveDate(String effectiveDate) throws Exception {
        if (effectiveDate != null && effectiveDate.trim().length() > 0) {
            this.effectiveDate = new SimpleDateFormat("yyyy-MM-dd").parse(effectiveDate.trim());
        }
    }

    /**
     * 获取职员代码（工号）
     */
    public String getEmpCode() {
        return this.empCode;
    }

    /**
     * 设置职员代码（工号）
     *
     * @throws Exception
     */
    @XmlElement(name = "EMP_NUM")
    public void setEmpCode(String empCode) throws Exception {
        this.empCode = substring(25, DeCoder.decrypt(empCode));
    }

    /**
     * 获取描述
     */
    public String getEmpDesc() {
        return this.empDesc;
    }

    /**
     * 设置描述
     */
    @XmlElement(name = "CURR_ORG_NAME")
    public void setEmpDesc(String empDesc) {
        this.empDesc = substring(66, empDesc);
    }

    /**
     * 获取职员岗位
     */
    public String getEmpDutyName() {
        return this.empDutyName;
    }

    /**
     * 设置职员岗位
     */
    @XmlElement(name = "JOB_NAME")
    public void setEmpDutyName(String empDutyName) {
        this.empDutyName = substring(16, empDutyName);
    }

    /**
     * 获取电子邮箱
     */
    public String getEmpEmail() {
        return this.empEmail;
    }

    /**
     * 设置电子邮箱
     */
    @XmlElement(name = "MAIL_ADDRESS")
    public void setEmpEmail(String empEmail) {
        this.empEmail = substring(60, empEmail);
    }

    /**
     * 获取性别:男、女
     */
    public String getEmpGender() {
        return this.empGender;
    }

    /**
     * 设置性别:男、女
     */
    @XmlElement(name = "SEX")
    public void setEmpGender(String empGender) {
        this.empGender = substring(4, empGender);
    }

    /**
     * 获取手机号码
     */
    public String getEmpMobile() {
        return this.empMobile;
    }

    /**
     * 设置手机号码
     */
    @XmlElement(name = "MOBILE_PHONE")
    public void setEmpMobile(String empMobile) {
        this.empMobile = substring(30, empMobile);
    }

    /**
     * 获取姓名
     */
    public String getEmpName() {
        return this.empName;
    }

    /**
     * 设置姓名
     * VARCHAR2(500)
     *
     * @throws Exception
     */
    @XmlElement(name = "LAST_NAME")
    public void setEmpName(String empName) throws Exception {
        this.empName = substring(166, DeCoder.decrypt(empName));
    }

    public String getDutySerial() {
        return dutySerial;
    }

    @XmlElement(name = "ATTRIBUTE10")
    public void setDutySerial(String dutySerial) {
        this.dutySerial = dutySerial;
    }

    /**
     * 获取办公电话
     */
    public String getEmpOfficephone() {
        return this.empOfficephone;
    }

    /**
     * 设置办公电话
     */
    @XmlElement(name = "OFFICE_PHONE")
    public void setEmpOfficephone(String empOfficephone) {
        this.empOfficephone = substring(32, empOfficephone);
    }

    /**
     * 获取职员类别
     */
    public String getEmpTypeName() {
        return this.empTypeName;
    }

    /**
     * 设置职员类别
     */
    @XmlElement(name = "EMP_CATEGORY")
    public void setEmpTypeName(String empTypeName) {
        this.empTypeName = substring(16, empTypeName);
    }

    /**
     * @return the errmsg
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    public String getErrmsg() {
        return errmsg;
    }

    /**
     * @param to set errmsg the errmsg
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    public void setErrmsg(String errmsg) {
        this.errmsg = substring(1000, errmsg);
    }

    /**
     * hrs职员id
     *
     * @return the hrsEmpId
     * @author 文俊 (337291)
     * @date Jun 20, 2014
     */
    public String getHrsEmpId() {
        return hrsEmpId;
    }

    /**
     * hrs职员id
     *
     * @param to set hrsEmpId the hrsEmpId
     * @author 文俊 (337291)
     * @date Jun 20, 2014
     */
    @XmlElement(name = "EMP_ID")
    public void setHrsEmpId(String hrsEmpId) {
        this.hrsEmpId = substring(50, hrsEmpId);
    }

    /**
     * @return the hrsPersonId
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    public String getHrsPersonId() {
        return hrsPersonId;
    }

    /**
     * @param to set hrsPersonId the hrsPersonId
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    @XmlElement(name = "PERSON_ID")
    public void setHrsPersonId(String hrsPersonId) {
        this.hrsPersonId = hrsPersonId;
    }

    /**
     * @return the journalId
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    public String getJournalId() {
        return journalId;
    }

    /**
     * @param to set journalId the journalId
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    public void setJournalId(String journalId) {
        this.journalId = substring(200, journalId);
    }

    /**
     * 获取注销日期
     */
    public Date getLogoutDt() {
        return this.logoutDt;
    }

    /**
     * 设置注销日期
     *
     * @param to set logoutDt the logoutDt
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    public void setLogoutDt(Date logoutDt) {
        this.logoutDt = logoutDt;
    }

    /**
     * @return the personType
     * @author 文俊 (337291)
     * @date Jun 23, 2014
     */
    public String getPersonType() {
        return personType;
    }

    /**
     * @param to set personType the personType
     * @author 文俊 (337291)
     * @date Jun 23, 2014
     */
    @XmlElement(name = "PERSON_TYPE")
    public void setPersonType(String personType) {
        this.personType = substring(16, personType);
    }

    /**
     * @return the positionName
     * @author 文俊 (337291)
     * @date Jun 23, 2014
     */
    public String getPositionName() {
        return positionName;
    }

    /**
     * @param to set positionName the positionName
     * @author 文俊 (337291)
     * @date Jun 23, 2014
     */
    @XmlElement(name = "POSITION_NAME")
    public void setPositionName(String positionName) {
        this.positionName = substring(66, positionName);
    }

    /**
     * 获取注册日期
     */
    public Date getRegisterDt() {
        return this.registerDt;
    }

    /**
     * 设置注册日期
     *
     * @param to set registerDt the registerDt
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    public void setRegisterDt(Date registerDt) {
        this.registerDt = registerDt;
    }

    /**
     * @return the sfDate
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    public String getSfDate() {
        return sfDate;
    }

    /**
     * @param to set sfDate the sfDate
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    @XmlElement(name = "SF_DATE")
    public void setSfDate(String sfDate) throws Exception {
        this.sfDate = substring(20, sfDate);
        if (this.sfDate != null && this.sfDate.trim().length() > 0) {
            this.registerDt = new SimpleDateFormat("yyyy-MM-dd").parse(this.sfDate.trim());
        }
    }

    /**
     * @return the xmlSize
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    public Long getXmlSize() {
        return xmlSize;
    }

    /**
     * @param to set xmlSize the xmlSize
     * @author 文俊 (337291)
     * @date Jun 24, 2014
     */
    public void setXmlSize(Long xmlSize) {
        this.xmlSize = xmlSize;
    }

    private String substring(int len, String str) {
        if (str != null && str.length() > len) {
            str = str.substring(0, len);
        }
        return str;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     * @author 文俊 (337291)
     * @date Jun 20, 2014
     * @return
     */
    @Override
    public String toString() {
        return "HrEmpInfo [empCode=" + empCode + ", empName=" + empName
                + ", hrsEmpId=" + hrsEmpId + "]";
    }

}