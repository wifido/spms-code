
package com.sf.module.esbinterface.webservice.server.errorinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>BigFileServiceResponse_TYPE complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="BigFileServiceResponse_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SHEAD" type="{http://www.sf-express.com/esb/service/BigFileService}SHEADType_3" minOccurs="0"/>
 *         &lt;element name="SBODY" type="{http://www.sf-express.com/esb/service/BigFileService}SBODYType_5" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BigFileServiceResponse_TYPE", propOrder = {
    "shead",
    "sbody"
})
public class BigFileServiceResponseTYPE {

    @XmlElement(name = "SHEAD", nillable = true)
    protected SHEADType3 shead;
    @XmlElement(name = "SBODY", nillable = true)
    protected SBODYType5 sbody;

    /**
     * ��ȡshead���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SHEADType3 }
     *     
     */
    public SHEADType3 getSHEAD() {
        return shead;
    }

    /**
     * ����shead���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SHEADType3 }
     *     
     */
    public void setSHEAD(SHEADType3 value) {
        this.shead = value;
    }

    /**
     * ��ȡsbody���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SBODYType5 }
     *     
     */
    public SBODYType5 getSBODY() {
        return sbody;
    }

    /**
     * ����sbody���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SBODYType5 }
     *     
     */
    public void setSBODY(SBODYType5 value) {
        this.sbody = value;
    }

}
