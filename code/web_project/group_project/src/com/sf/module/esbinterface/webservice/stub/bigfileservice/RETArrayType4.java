
package com.sf.module.esbinterface.webservice.stub.bigfileservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>RETArrayType_4 complex type�� Java �ࡣ
 * <p/>
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p/>
 * <pre>
 * &lt;complexType name="RETArrayType_4">
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
@XmlType(name = "RETArrayType_4", propOrder = {
        "retcode",
        "retmsg"
})
public class RETArrayType4 {

    @XmlElement(name = "RET_CODE", namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String retcode;
    @XmlElement(name = "RET_MSG", namespace = "http://www.sf-express.com/esb/metadata", nillable = true)
    protected String retmsg;

    /**
     * ��ȡretcode���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getRETCODE() {
        return retcode;
    }

    /**
     * ����retcode���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRETCODE(String value) {
        this.retcode = value;
    }

    /**
     * ��ȡretmsg���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getRETMSG() {
        return retmsg;
    }

    /**
     * ����retmsg���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRETMSG(String value) {
        this.retmsg = value;
    }

}
