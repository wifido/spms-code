
package com.sf.module.esbinterface.webservice.stub.bigfileservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SBODYType_5 complex type�� Java �ࡣ
 * <p/>
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p/>
 * <pre>
 * &lt;complexType name="SBODYType_5">
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
@XmlType(name = "SBODYType_5", propOrder = {
        "result"
})
public class SBODYType5 {

    @XmlElement(namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String result;

    /**
     * ��ȡresult���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getResult() {
        return result;
    }

    /**
     * ����result���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setResult(String value) {
        this.result = value;
    }

}
