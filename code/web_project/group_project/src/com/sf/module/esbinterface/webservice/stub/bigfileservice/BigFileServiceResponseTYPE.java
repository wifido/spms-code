
package com.sf.module.esbinterface.webservice.stub.bigfileservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>BigFileServiceResponse_TYPE complex type�� Java �ࡣ
 * <p/>
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p/>
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
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BigFileServiceResponse_TYPE", propOrder = {
        "shead",
        "sbody"
})
public class BigFileServiceResponseTYPE {

    @XmlElementRef(name = "SHEAD", namespace = "http://www.sf-express.com/esb/service/BigFileService", type = JAXBElement.class)
    protected JAXBElement<SHEADType3> shead;
    @XmlElementRef(name = "SBODY", namespace = "http://www.sf-express.com/esb/service/BigFileService", type = JAXBElement.class)
    protected JAXBElement<SBODYType5> sbody;

    /**
     * ��ȡshead���Ե�ֵ��
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link SHEADType3 }{@code >}
     */
    public JAXBElement<SHEADType3> getSHEAD() {
        return shead;
    }

    /**
     * ����shead���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link SHEADType3 }{@code >}
     */
    public void setSHEAD(JAXBElement<SHEADType3> value) {
        this.shead = value;
    }

    /**
     * ��ȡsbody���Ե�ֵ��
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link SBODYType5 }{@code >}
     */
    public JAXBElement<SBODYType5> getSBODY() {
        return sbody;
    }

    /**
     * ����sbody���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link SBODYType5 }{@code >}
     */
    public void setSBODY(JAXBElement<SBODYType5> value) {
        this.sbody = value;
    }

}
