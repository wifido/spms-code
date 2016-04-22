/***********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * 
 * HISTORY
 ********************************************************
 *  ID    DATE               PERSON             REASON
 *  1     Jun 19, 2014           文俊 (337291)       创建 
 ********************************************************/
package com.sf.module.ossinterface.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author 文俊 (337291) Jun 19, 2014
 */

public class XmlHandler<T> extends DefaultHandler {

	private XmlHandler() {
	}

	private static final Logger LOG = LoggerFactory.getLogger(XmlHandler.class);
	
	private IXmlMapping<T> xmlMapping;
	private List<T> data;
	private T entity;
	
	private boolean isPattern;
	private String rootElementPattern = "";
//	private Map<String, Attributes> attrMap = new HashMap<String, Attributes>();

	private String tagName;

	@Override
	public void startDocument() throws SAXException {
		LOG.info(" XmlHandler.init()  start");
		this.data = this.xmlMapping.init();
		LOG.info(" XmlHandler.init()  end");
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		LOG.info(" XmlHandler.startElement()  start");
		if (qName == null) {
			return;
		}
		String tagName = qName.trim().toUpperCase();
		this.xmlMapping.attributes(tagName, attributes);
		
		if (!isPattern && rootElementPattern.equals(tagName)) {
			isPattern = true;
			this.entity = this.xmlMapping.newEntity();
		}
		if (!rootElementPattern.equals(tagName)) {
			this.tagName = tagName;
		}
		LOG.info(" XmlHandler.startElement()  end");
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		LOG.info(" XmlHandler.endElement()  start");
		String tagName = "";
		if (qName != null) {
			tagName = qName.trim().toUpperCase();
		}
		
		if (tagName.equals(this.tagName)) {
			this.xmlMapping.mapping(this.entity, this.tagName, sb.toString().trim());
			sb = new StringBuilder();
		}
		
		if (isPattern && rootElementPattern.equals(tagName)) {
			isPattern = false;
			if (this.data.size() >= IXmlMapping.MAX_SIZE) {
				LOG.info(" data = " + this.data);
				this.xmlMapping.saveBatch(data);
				LOG.info("  saveBatch hrempinfo ");
				this.startDocument();
			}
			this.data.add(this.entity);
			this.entity = null;
		}
		LOG.info(" XmlHandler.endElement()  end");
		this.tagName = null;
	}
	
	@Override
	public void endDocument() throws SAXException {
		LOG.info(" XmlHandler.endDocument()  start");
		if (data != null && !data.isEmpty()) {
			LOG.info("  saveBatch hrempinfo ");
			this.xmlMapping.saveBatch(data);
		}
		LOG.info(" XmlHandler.endDocument()  end");
	}
	
	private StringBuilder sb = new StringBuilder();

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (isPattern) {
			//String textValue = new String(ch, start, length);
			//this.xmlMapping.mapping(this.entity, this.tagName, textValue);
			sb.append(ch, start, length);
		}
	}
	 
	private static <T> DefaultHandler newInstance(IXmlMapping<T> xmlMapping,
			String rootElementPattern) {
		XmlHandler<T> handler = new XmlHandler<T>();
		if (rootElementPattern != null) {
			handler.rootElementPattern = rootElementPattern.toUpperCase();
		}
		handler.xmlMapping = xmlMapping;
		return handler;
	}

	public static <T> void parser(File xmlFile, IXmlMapping<T> xmlMapping,
			String rootElementPattern) throws Exception {

		if (xmlFile == null) {
			throw new IllegalArgumentException(
					"parameter 'xmlFile' must not null !");
		}
		if (xmlMapping == null) {
			throw new IllegalArgumentException(
			"parameter 'xmlMapping' must not null !");
		}
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		// 禁止DTD解析，避免因找不到DTD文件导致解析失败。
		parser.getXMLReader()
				.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-external-dtd",
						false);
		DefaultHandler handler = null;
		InputStream input = null;
		try {
			handler = newInstance(xmlMapping, rootElementPattern);
			input = new FileInputStream(xmlFile);
			parser.parse(input, handler);
			
		} catch (Exception e) {
			System.err.print("Cann't parse ");
			System.err.println(xmlFile.getAbsolutePath());
			LOG.error("XmlHandler.parser: ", e);
			throw e;
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}

}
