
package com.sf.module.esbinterface.webservice.server.errorinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>BigFileService_TYPE complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="BigFileService_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SHEAD" type="{http://www.sf-express.com/esb/service/BigFileService}SHEADType_1" minOccurs="0"/>
 *         &lt;element name="SBODY" type="{http://www.sf-express.com/esb/service/BigFileService}SBODYType_2" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BigFileService_TYPE", propOrder = {
    "shead",
    "sbody"
})
public class BigFileServiceTYPE {

    @XmlElement(name = "SHEAD", nillable = true)
    protected SHEADType1 shead;
    @XmlElement(name = "SBODY", nillable = true)
    protected SBODYType2 sbody;

    /**
     * ��ȡshead���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SHEADType1 }
     *     
     */
    public SHEADType1 getSHEAD() {
        return shead;
    }

    /**
     * ����shead���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SHEADType1 }
     *     
     */
    public void setSHEAD(SHEADType1 value) {
        this.shead = value;
    }

    /**
     * ��ȡsbody���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SBODYType2 }
     *     
     */
    public SBODYType2 getSBODY() {
        return sbody;
    }

    /**
     * ����sbody���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SBODYType2 }
     *     
     */
    public void setSBODY(SBODYType2 value) {
        this.sbody = value;
    }

}
