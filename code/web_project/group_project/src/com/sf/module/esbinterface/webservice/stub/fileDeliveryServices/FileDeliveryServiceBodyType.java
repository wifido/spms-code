
package com.sf.module.esbinterface.webservice.stub.fileDeliveryServices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FileDeliveryServiceBodyType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="FileDeliveryServiceBodyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SHEAD">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}SYSTEM_ID" minOccurs="0"/>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}TRAN_MODE" minOccurs="0"/>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}TRAN_DATE" minOccurs="0"/>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}TRAN_TIMESTAMP" minOccurs="0"/>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}SERVER_ID" minOccurs="0"/>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}WS_ID" minOccurs="0"/>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}USER_LANG" minOccurs="0"/>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}USER_ID" minOccurs="0"/>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}SEQ_NO" minOccurs="0"/>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}COUNTRY" minOccurs="0"/>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}PAGE_NUM" minOccurs="0"/>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}PAGE_INDEX" minOccurs="0"/>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}PAGE_SIZE" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="SBODY">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}filePath" minOccurs="0"/>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}fileName" minOccurs="0"/>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}md5" minOccurs="0"/>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}isZip" minOccurs="0"/>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}dataType" minOccurs="0"/>
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}timestamp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FileDeliveryServiceBodyType", propOrder = {
        "shead",
        "sbody"
})
public class FileDeliveryServiceBodyType {

    @XmlElement(name = "SHEAD", required = true)
    protected SHEAD shead;
    @XmlElement(name = "SBODY", required = true)
    protected SBODY sbody;

    /**
     * Gets the value of the shead property.
     *
     * @return possible object is
     * {@link com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceBodyType.SHEAD }
     */
    public SHEAD getSHEAD() {
        return shead;
    }

    /**
     * Sets the value of the shead property.
     *
     * @param value allowed object is
     *              {@link com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceBodyType.SHEAD }
     */
    public void setSHEAD(SHEAD value) {
        this.shead = value;
    }

    /**
     * Gets the value of the sbody property.
     *
     * @return possible object is
     * {@link com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceBodyType.SBODY }
     */
    public SBODY getSBODY() {
        return sbody;
    }

    /**
     * Sets the value of the sbody property.
     *
     * @param value allowed object is
     *              {@link com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceBodyType.SBODY }
     */
    public void setSBODY(SBODY value) {
        this.sbody = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * <p/>
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p/>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{http://www.sf-express.com/esb/metadata}filePath" minOccurs="0"/>
     *         &lt;element ref="{http://www.sf-express.com/esb/metadata}fileName" minOccurs="0"/>
     *         &lt;element ref="{http://www.sf-express.com/esb/metadata}md5" minOccurs="0"/>
     *         &lt;element ref="{http://www.sf-express.com/esb/metadata}isZip" minOccurs="0"/>
     *         &lt;element ref="{http://www.sf-express.com/esb/metadata}dataType" minOccurs="0"/>
     *         &lt;element ref="{http://www.sf-express.com/esb/metadata}timestamp" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "filePath",
            "fileName",
            "md5",
            "isZip",
            "dataType",
            "timestamp"
    })
    public static class SBODY {

        @XmlElement(namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
        protected String filePath;
        @XmlElement(namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
        protected String fileName;
        @XmlElement(namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
        protected String md5;
        @XmlElement(namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
        protected String isZip;
        @XmlElement(namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
        protected String dataType;
        @XmlElement(namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
        protected String timestamp;

        /**
         * Gets the value of the filePath property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getFilePath() {
            return filePath;
        }

        /**
         * Sets the value of the filePath property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setFilePath(String value) {
            this.filePath = value;
        }

        /**
         * Gets the value of the fileName property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getFileName() {
            return fileName;
        }

        /**
         * Sets the value of the fileName property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setFileName(String value) {
            this.fileName = value;
        }

        /**
         * Gets the value of the md5 property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getMd5() {
            return md5;
        }

        /**
         * Sets the value of the md5 property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setMd5(String value) {
            this.md5 = value;
        }

        /**
         * Gets the value of the isZip property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getIsZip() {
            return isZip;
        }

        /**
         * Sets the value of the isZip property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setIsZip(String value) {
            this.isZip = value;
        }

        /**
         * Gets the value of the dataType property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getDataType() {
            return dataType;
        }

        /**
         * Sets the value of the dataType property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setDataType(String value) {
            this.dataType = value;
        }

        /**
         * Gets the value of the timestamp property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getTimestamp() {
            return timestamp;
        }

        /**
         * Sets the value of the timestamp property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setTimestamp(String value) {
            this.timestamp = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * <p/>
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p/>
     * <pre>
     * &lt;complexType>
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
    @XmlType(name = "", propOrder = {
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
    public static class SHEAD {

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
         * Gets the value of the systemid property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getSYSTEMID() {
            return systemid;
        }

        /**
         * Sets the value of the systemid property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setSYSTEMID(String value) {
            this.systemid = value;
        }

        /**
         * Gets the value of the tranmode property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getTRANMODE() {
            return tranmode;
        }

        /**
         * Sets the value of the tranmode property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setTRANMODE(String value) {
            this.tranmode = value;
        }

        /**
         * Gets the value of the trandate property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getTRANDATE() {
            return trandate;
        }

        /**
         * Sets the value of the trandate property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setTRANDATE(String value) {
            this.trandate = value;
        }

        /**
         * Gets the value of the trantimestamp property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getTRANTIMESTAMP() {
            return trantimestamp;
        }

        /**
         * Sets the value of the trantimestamp property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setTRANTIMESTAMP(String value) {
            this.trantimestamp = value;
        }

        /**
         * Gets the value of the serverid property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getSERVERID() {
            return serverid;
        }

        /**
         * Sets the value of the serverid property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setSERVERID(String value) {
            this.serverid = value;
        }

        /**
         * Gets the value of the wsid property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getWSID() {
            return wsid;
        }

        /**
         * Sets the value of the wsid property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setWSID(String value) {
            this.wsid = value;
        }

        /**
         * Gets the value of the userlang property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getUSERLANG() {
            return userlang;
        }

        /**
         * Sets the value of the userlang property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setUSERLANG(String value) {
            this.userlang = value;
        }

        /**
         * Gets the value of the userid property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getUSERID() {
            return userid;
        }

        /**
         * Sets the value of the userid property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setUSERID(String value) {
            this.userid = value;
        }

        /**
         * Gets the value of the seqno property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getSEQNO() {
            return seqno;
        }

        /**
         * Sets the value of the seqno property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setSEQNO(String value) {
            this.seqno = value;
        }

        /**
         * Gets the value of the country property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getCOUNTRY() {
            return country;
        }

        /**
         * Sets the value of the country property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setCOUNTRY(String value) {
            this.country = value;
        }

        /**
         * Gets the value of the pagenum property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getPAGENUM() {
            return pagenum;
        }

        /**
         * Sets the value of the pagenum property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setPAGENUM(String value) {
            this.pagenum = value;
        }

        /**
         * Gets the value of the pageindex property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getPAGEINDEX() {
            return pageindex;
        }

        /**
         * Sets the value of the pageindex property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setPAGEINDEX(String value) {
            this.pageindex = value;
        }

        /**
         * Gets the value of the pagesize property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getPAGESIZE() {
            return pagesize;
        }

        /**
         * Sets the value of the pagesize property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setPAGESIZE(String value) {
            this.pagesize = value;
        }

    }

}
