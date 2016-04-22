
package com.sf.module.esbinterface.webservice.stub.fileResendServices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for FileResendServiceResponseBodyType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="FileResendServiceResponseBodyType">
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
 *                   &lt;element name="RET" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element ref="{http://www.sf-express.com/esb/metadata}RET_CODE" minOccurs="0"/>
 *                             &lt;element ref="{http://www.sf-express.com/esb/metadata}RET_MSG" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
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
 *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}result" minOccurs="0"/>
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
@XmlType(name = "FileResendServiceResponseBodyType", propOrder = {
        "shead",
        "sbody"
})
public class FileResendServiceResponseBodyType {

    @XmlElement(name = "SHEAD", required = true)
    protected SHEAD shead;
    @XmlElement(name = "SBODY", required = true)
    protected SBODY sbody;

    /**
     * Gets the value of the shead property.
     *
     * @return possible object is
     * {@link com.sf.module.esbinterface.webservice.stub.fileResendServices.FileResendServiceResponseBodyType.SHEAD }
     */
    public SHEAD getSHEAD() {
        return shead;
    }

    /**
     * Sets the value of the shead property.
     *
     * @param value allowed object is
     *              {@link com.sf.module.esbinterface.webservice.stub.fileResendServices.FileResendServiceResponseBodyType.SHEAD }
     */
    public void setSHEAD(SHEAD value) {
        this.shead = value;
    }

    /**
     * Gets the value of the sbody property.
     *
     * @return possible object is
     * {@link com.sf.module.esbinterface.webservice.stub.fileResendServices.FileResendServiceResponseBodyType.SBODY }
     */
    public SBODY getSBODY() {
        return sbody;
    }

    /**
     * Sets the value of the sbody property.
     *
     * @param value allowed object is
     *              {@link com.sf.module.esbinterface.webservice.stub.fileResendServices.FileResendServiceResponseBodyType.SBODY }
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
     *         &lt;element ref="{http://www.sf-express.com/esb/metadata}result" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "result"
    })
    public static class SBODY {

        @XmlElement(namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
        protected String result;

        /**
         * Gets the value of the result property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getResult() {
            return result;
        }

        /**
         * Sets the value of the result property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setResult(String value) {
            this.result = value;
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
     *         &lt;element name="RET" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}RET_CODE" minOccurs="0"/>
     *                   &lt;element ref="{http://www.sf-express.com/esb/metadata}RET_MSG" minOccurs="0"/>
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
            "pagesize",
            "ret"
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
        @XmlElement(name = "RET")
        protected List<RET> ret;

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

        /**
         * Gets the value of the ret property.
         * <p/>
         * <p/>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the ret property.
         * <p/>
         * <p/>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRET().add(newItem);
         * </pre>
         * <p/>
         * <p/>
         * <p/>
         * Objects of the following type(s) are allowed in the list
         * {@link com.sf.module.esbinterface.webservice.stub.fileResendServices.FileResendServiceResponseBodyType.SHEAD.RET }
         */
        public List<RET> getRET() {
            if (ret == null) {
                ret = new ArrayList<RET>();
            }
            return this.ret;
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
         *         &lt;element ref="{http://www.sf-express.com/esb/metadata}RET_CODE" minOccurs="0"/>
         *         &lt;element ref="{http://www.sf-express.com/esb/metadata}RET_MSG" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "retcode",
                "retmsg"
        })
        public static class RET {

            @XmlElement(name = "RET_CODE", namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
            protected String retcode;
            @XmlElement(name = "RET_MSG", namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
            protected String retmsg;

            /**
             * Gets the value of the retcode property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getRETCODE() {
                return retcode;
            }

            /**
             * Sets the value of the retcode property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setRETCODE(String value) {
                this.retcode = value;
            }

            /**
             * Gets the value of the retmsg property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getRETMSG() {
                return retmsg;
            }

            /**
             * Sets the value of the retmsg property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setRETMSG(String value) {
                this.retmsg = value;
            }

        }

    }

}
