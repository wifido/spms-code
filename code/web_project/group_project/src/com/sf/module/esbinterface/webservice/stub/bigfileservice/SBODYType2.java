
package com.sf.module.esbinterface.webservice.stub.bigfileservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SBODYType_2 complex type�� Java �ࡣ
 * <p/>
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p/>
 * <pre>
 * &lt;complexType name="SBODYType_2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}dataType" minOccurs="0"/>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}timeStamp" minOccurs="0"/>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}isZip" minOccurs="0"/>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}filePath" minOccurs="0"/>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}fileName" minOccurs="0"/>
 *         &lt;element ref="{http://www.sf-express.com/esb/metadata}md5" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SBODYType_2", propOrder = {
        "dataType",
        "timeStamp",
        "isZip",
        "filePath",
        "fileName",
        "md5"
})
public class SBODYType2 {

    @XmlElement(namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String dataType;
    @XmlElement(namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String timeStamp;
    @XmlElement(namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String isZip;
    @XmlElement(namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String filePath;
    @XmlElement(namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String fileName;
    @XmlElement(namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String md5;

    /**
     * ��ȡdataType���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * ����dataType���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDataType(String value) {
        this.dataType = value;
    }

    /**
     * ��ȡtimeStamp���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * ����timeStamp���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTimeStamp(String value) {
        this.timeStamp = value;
    }

    /**
     * ��ȡisZip���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getIsZip() {
        return isZip;
    }

    /**
     * ����isZip���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIsZip(String value) {
        this.isZip = value;
    }

    /**
     * ��ȡfilePath���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * ����filePath���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFilePath(String value) {
        this.filePath = value;
    }

    /**
     * ��ȡfileName���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * ����fileName���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    /**
     * ��ȡmd5���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getMd5() {
        return md5;
    }

    /**
     * ����md5���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMd5(String value) {
        this.md5 = value;
    }

}
