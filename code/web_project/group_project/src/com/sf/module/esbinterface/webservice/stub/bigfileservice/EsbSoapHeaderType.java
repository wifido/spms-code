
package com.sf.module.esbinterface.webservice.stub.bigfileservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>esbSoapHeaderType complex type�� Java �ࡣ
 * <p/>
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p/>
 * <pre>
 * &lt;complexType name="esbSoapHeaderType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MAC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MACOrg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Source" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MessageID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="From" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReplyTo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="To" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Action" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Subscriber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "esbSoapHeaderType", propOrder = {
        "mac",
        "macOrg",
        "source",
        "messageID",
        "from",
        "replyTo",
        "to",
        "action",
        "subscriber"
})
public class EsbSoapHeaderType {

    @XmlElementRef(name = "MAC", namespace = "http://www.sf-express.com/esb/service/BigFileService", type = JAXBElement.class)
    protected JAXBElement<String> mac;
    @XmlElementRef(name = "MACOrg", namespace = "http://www.sf-express.com/esb/service/BigFileService", type = JAXBElement.class)
    protected JAXBElement<String> macOrg;
    @XmlElementRef(name = "Source", namespace = "http://www.sf-express.com/esb/service/BigFileService", type = JAXBElement.class)
    protected JAXBElement<String> source;
    @XmlElementRef(name = "MessageID", namespace = "http://www.sf-express.com/esb/service/BigFileService", type = JAXBElement.class)
    protected JAXBElement<String> messageID;
    @XmlElementRef(name = "From", namespace = "http://www.sf-express.com/esb/service/BigFileService", type = JAXBElement.class)
    protected JAXBElement<String> from;
    @XmlElementRef(name = "ReplyTo", namespace = "http://www.sf-express.com/esb/service/BigFileService", type = JAXBElement.class)
    protected JAXBElement<String> replyTo;
    @XmlElementRef(name = "To", namespace = "http://www.sf-express.com/esb/service/BigFileService", type = JAXBElement.class)
    protected JAXBElement<String> to;
    @XmlElementRef(name = "Action", namespace = "http://www.sf-express.com/esb/service/BigFileService", type = JAXBElement.class)
    protected JAXBElement<String> action;
    @XmlElementRef(name = "Subscriber", namespace = "http://www.sf-express.com/esb/service/BigFileService", type = JAXBElement.class)
    protected JAXBElement<String> subscriber;

    /**
     * ��ȡmac���Ե�ֵ��
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getMAC() {
        return mac;
    }

    /**
     * ����mac���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setMAC(JAXBElement<String> value) {
        this.mac = value;
    }

    /**
     * ��ȡmacOrg���Ե�ֵ��
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getMACOrg() {
        return macOrg;
    }

    /**
     * ����macOrg���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setMACOrg(JAXBElement<String> value) {
        this.macOrg = value;
    }

    /**
     * ��ȡsource���Ե�ֵ��
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getSource() {
        return source;
    }

    /**
     * ����source���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setSource(JAXBElement<String> value) {
        this.source = value;
    }

    /**
     * ��ȡmessageID���Ե�ֵ��
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getMessageID() {
        return messageID;
    }

    /**
     * ����messageID���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setMessageID(JAXBElement<String> value) {
        this.messageID = value;
    }

    /**
     * ��ȡfrom���Ե�ֵ��
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getFrom() {
        return from;
    }

    /**
     * ����from���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setFrom(JAXBElement<String> value) {
        this.from = value;
    }

    /**
     * ��ȡreplyTo���Ե�ֵ��
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getReplyTo() {
        return replyTo;
    }

    /**
     * ����replyTo���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setReplyTo(JAXBElement<String> value) {
        this.replyTo = value;
    }

    /**
     * ��ȡto���Ե�ֵ��
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getTo() {
        return to;
    }

    /**
     * ����to���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setTo(JAXBElement<String> value) {
        this.to = value;
    }

    /**
     * ��ȡaction���Ե�ֵ��
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getAction() {
        return action;
    }

    /**
     * ����action���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setAction(JAXBElement<String> value) {
        this.action = value;
    }

    /**
     * ��ȡsubscriber���Ե�ֵ��
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getSubscriber() {
        return subscriber;
    }

    /**
     * ����subscriber���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setSubscriber(JAXBElement<String> value) {
        this.subscriber = value;
    }

}
