package com.sf.module.esbinterface.domain;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.beanutils.BeanUtils;
import com.sf.framework.base.domain.BaseEntity;
import com.sf.framework.util.xml.EasyXMLStreamWriter;

public class DriverLog extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private Long logId;
	private String pernr;
	private String zausw;
	private String ldate;
	private String ltime;
	private String ldayt;
	private String origf;
	private String abwgr;
	private String pdcUsrup;
	private String zhrxgbz;
	private Long status;
	private Date syncDate;
	private String failDesc;
	private Date createDate;
	private String hostName;
	
	public String getHostName() {
    	return hostName;
    }

	public void setHostName(String hostName) {
    	this.hostName = hostName;
    }

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public String getPernr() {
		return pernr;
	}

	public void setPernr(String pernr) {
		this.pernr = pernr;
	}

	public String getZausw() {
		return zausw;
	}

	public void setZausw(String zausw) {
		this.zausw = zausw;
	}

	public String getLdate() {
		return ldate;
	}

	public void setLdate(String ldate) {
		this.ldate = ldate;
	}

	public String getLtime() {
		return ltime;
	}

	public void setLtime(String ltime) {
		this.ltime = ltime;
	}

	public String getLdayt() {
		return ldayt;
	}

	public void setLdayt(String ldayt) {
		this.ldayt = ldayt;
	}

	public String getOrigf() {
		return origf;
	}

	public void setOrigf(String origf) {
		this.origf = origf;
	}

	public String getAbwgr() {
		return abwgr;
	}

	public void setAbwgr(String abwgr) {
		this.abwgr = abwgr;
	}

	public String getPdcUsrup() {
		return pdcUsrup;
	}

	public void setPdcUsrup(String pdcUsrup) {
		this.pdcUsrup = pdcUsrup;
	}

	public String getZhrxgbz() {
		return zhrxgbz;
	}

	public void setZhrxgbz(String zhrxgbz) {
		this.zhrxgbz = zhrxgbz;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Date getSyncDate() {
		return syncDate;
	}

	public void setSyncDate(Date syncDate) {
		this.syncDate = syncDate;
	}

	public String getFailDesc() {
		return failDesc;
	}

	public void setFailDesc(String failDesc) {
		this.failDesc = failDesc;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void bulidXmlFileElement(EasyXMLStreamWriter streamWriter) throws XMLStreamException {
		streamWriter.writeStartElement("item", null);
		streamWriter.writeContentElement("PERNR", null, pernr);
		streamWriter.writeContentElement("ZAUSW", null, zausw);
		streamWriter.writeContentElement("LDATE", null, ldate);
		streamWriter.writeContentElement("LTIME", null, ltime);
		streamWriter.writeContentElement("LDAYT", null, ldayt);
		streamWriter.writeContentElement("ORIGF", null, origf);
		streamWriter.writeContentElement("ABWGR", null, abwgr);
		streamWriter.writeContentElement("PDC_USRUP", null, pdcUsrup);
		streamWriter.writeContentElement("ZHRXGBZ", null, zhrxgbz);
		streamWriter.writeContentElement("TR_DATE", null, null);
		streamWriter.writeContentElement("TR_TIME", null, null);
		streamWriter.writeEndElement();
	}

	public DriverLogHistory buildHistoryLog() throws IllegalAccessException, InvocationTargetException {
		DriverLogHistory historyLog = new DriverLogHistory();
		BeanUtils.copyProperties(historyLog, this);
		return historyLog;
	}

}
