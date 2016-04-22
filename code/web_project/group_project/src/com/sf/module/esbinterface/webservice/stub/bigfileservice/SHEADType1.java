
package com.sf.module.esbinterface.webservice.stub.bigfileservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SHEADType_1 complex type�� Java �ࡣ
 * <p/>
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p/>
 * <pre>
 * &lt;complexType name="SHEADType_1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}SYSTEM_ID" minOccurs="0"/>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}TRAN_MODE" minOccurs="0"/>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}TRAN_DATE" minOccurs="0"/>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}TRAN_TIMESTAMP" minOccurs="0"/>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}SERVER_ID" minOccurs="0"/>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}WS_ID" minOccurs="0"/>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}USER_LANG" minOccurs="0"/>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}USER_ID" minOccurs="0"/>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}SEQ_NO" minOccurs="0"/>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}COUNTRY" minOccurs="0"/>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}PAGE_NUM" minOccurs="0"/>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}PAGE_INDEX" minOccurs="0"/>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}PAGE_SIZE" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SHEADType_1", propOrder = {
        "systemid",
        "tranmode",
        "trandate",
        "trantimestamp",
        "serverid",
        "wsid",
        "userlang",
        "userid",
        "seqno",
        "country",
        "pagenum",
        "pageindex",
        "pagesize"
})
public class SHEADType1 {

    @XmlElement(name = "SYSTEM_ID", namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String systemid;
    @XmlElement(name = "TRAN_MODE", namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String tranmode;
    @XmlElement(name = "TRAN_DATE", namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String trandate;
    @XmlElement(name = "TRAN_TIMESTAMP", namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String trantimestamp;
    @XmlElement(name = "SERVER_ID", namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String serverid;
    @XmlElement(name = "WS_ID", namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String wsid;
    @XmlElement(name = "USER_LANG", namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String userlang;
    @XmlElement(name = "USER_ID", namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String userid;
    @XmlElement(name = "SEQ_NO", namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String seqno;
    @XmlElement(name = "COUNTRY", namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String country;
    @XmlElement(name = "PAGE_NUM", namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String pagenum;
    @XmlElement(name = "PAGE_INDEX", namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String pageindex;
    @XmlElement(name = "PAGE_SIZE", namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String pagesize;

    /**
     * ��ȡsystemid���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getSYSTEMID() {
        return systemid;
    }

    /**
     * ����systemid���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSYSTEMID(String value) {
        this.systemid = value;
    }

    /**
     * ��ȡtranmode���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getTRANMODE() {
        return tranmode;
    }

    /**
     * ����tranmode���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTRANMODE(String value) {
        this.tranmode = value;
    }

    /**
     * ��ȡtrandate���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getTRANDATE() {
        return trandate;
    }

    /**
     * ����trandate���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTRANDATE(String value) {
        this.trandate = value;
    }

    /**
     * ��ȡtrantimestamp���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getTRANTIMESTAMP() {
        return trantimestamp;
    }

    /**
     * ����trantimestamp���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTRANTIMESTAMP(String value) {
        this.trantimestamp = value;
    }

    /**
     * ��ȡserverid���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getSERVERID() {
        return serverid;
    }

    /**
     * ����serverid���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSERVERID(String value) {
        this.serverid = value;
    }

    /**
     * ��ȡwsid���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getWSID() {
        return wsid;
    }

    /**
     * ����wsid���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setWSID(String value) {
        this.wsid = value;
    }

    /**
     * ��ȡuserlang���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getUSERLANG() {
        return userlang;
    }

    /**
     * ����userlang���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUSERLANG(String value) {
        this.userlang = value;
    }

    /**
     * ��ȡuserid���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getUSERID() {
        return userid;
    }

    /**
     * ����userid���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUSERID(String value) {
        this.userid = value;
    }

    /**
     * ��ȡseqno���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getSEQNO() {
        return seqno;
    }

    /**
     * ����seqno���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSEQNO(String value) {
        this.seqno = value;
    }

    /**
     * ��ȡcountry���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getCOUNTRY() {
        return country;
    }

    /**
     * ����country���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCOUNTRY(String value) {
        this.country = value;
    }

    /**
     * ��ȡpagenum���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getPAGENUM() {
        return pagenum;
    }

    /**
     * ����pagenum���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPAGENUM(String value) {
        this.pagenum = value;
    }

    /**
     * ��ȡpageindex���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getPAGEINDEX() {
        return pageindex;
    }

    /**
     * ����pageindex���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPAGEINDEX(String value) {
        this.pageindex = value;
    }

    /**
     * ��ȡpagesize���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getPAGESIZE() {
        return pagesize;
    }

    /**
     * ����pagesize���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPAGESIZE(String value) {
        this.pagesize = value;
    }

}
