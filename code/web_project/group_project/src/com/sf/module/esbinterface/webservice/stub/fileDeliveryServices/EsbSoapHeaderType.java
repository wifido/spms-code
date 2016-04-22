
package com.sf.module.esbinterface.webservice.stub.fileDeliveryServices;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for esbSoapHeaderType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
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
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "esbSoapHeaderType", namespace = "http://www.sf-express.com/esb/metadata", propOrder = {
        "mac",
        "macOrg",
        "source",
        "messageID",
        "from",
        "replyTo",
        "to",
        "action"
})
public class EsbSoapHeaderType {

    @XmlElementRef(name = "MAC", namespace = "http://www.sf-express.com/esb/metadata", type = JAXBElement.class)
    protected JAXBElement<String> mac;
    @XmlElementRef(name = "MACOrg", namespace = "http://www.sf-express.com/esb/metadata", type = JAXBElement.class)
    protected JAXBElement<String> macOrg;
    @XmlElementRef(name = "Source", namespace = "http://www.sf-express.com/esb/metadata", type = JAXBElement.class)
    protected JAXBElement<String> source;
    @XmlElementRef(name = "MessageID", namespace = "http://www.sf-express.com/esb/metadata", type = JAXBElement.class)
    protected JAXBElement<String> messageID;
    @XmlElementRef(name = "From", namespace = "http://www.sf-express.com/esb/metadata", type = JAXBElement.class)
    protected JAXBElement<String> from;
    @XmlElementRef(name = "ReplyTo", namespace = "http://www.sf-express.com/esb/metadata", type = JAXBElement.class)
    protected JAXBElement<String> replyTo;
    @XmlElementRef(name = "To", namespace = "http://www.sf-express.com/esb/metadata", type = JAXBElement.class)
    protected JAXBElement<String> to;
    @XmlElementRef(name = "Action", namespace = "http://www.sf-express.com/esb/metadata", type = JAXBElement.class)
    protected JAXBElement<String> action;

    /**
     * Gets the value of the mac property.
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getMAC() {
        return mac;
    }

    /**
     * Sets the value of the mac property.
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setMAC(JAXBElement<String> value) {
        this.mac = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the macOrg property.
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getMACOrg() {
        return macOrg;
    }

    /**
     * Sets the value of the macOrg property.
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setMACOrg(JAXBElement<String> value) {
        this.macOrg = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the source property.
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setSource(JAXBElement<String> value) {
        this.source = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the messageID property.
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getMessageID() {
        return messageID;
    }

    /**
     * Sets the value of the messageID property.
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setMessageID(JAXBElement<String> value) {
        this.messageID = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the from property.
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getFrom() {
        return from;
    }

    /**
     * Sets the value of the from property.
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setFrom(JAXBElement<String> value) {
        this.from = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the replyTo property.
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getReplyTo() {
        return replyTo;
    }

    /**
     * Sets the value of the replyTo property.
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setReplyTo(JAXBElement<String> value) {
        this.replyTo = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the to property.
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getTo() {
        return to;
    }

    /**
     * Sets the value of the to property.
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setTo(JAXBElement<String> value) {
        this.to = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the action property.
     *
     * @return possible object is
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getAction() {
        return action;
    }

    /**
     * Sets the value of the action property.
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setAction(JAXBElement<String> value) {
        this.action = ((JAXBElement<String>) value);
    }

}
