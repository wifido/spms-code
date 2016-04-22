package com.sf.module.esbinterface.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class PropertiesFileUtil {
	private static final String FILE_NAME_ESB2_PROPERTIES = "esb2.properties";
	public static String ESB2_CONFIG_PROPERTIES_FILE_PATH;

	static {
		ESB2_CONFIG_PROPERTIES_FILE_PATH = Thread.currentThread().getContextClassLoader().getResource("").getPath() + FILE_NAME_ESB2_PROPERTIES;
		File esbFile = new File(ESB2_CONFIG_PROPERTIES_FILE_PATH);
	}

	public static void createProperties(Map<String, String> params) throws IOException {
		Properties pro = new Properties();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			pro.setProperty(entry.getKey(), entry.getValue());
		}
		FileOutputStream fos = new FileOutputStream(ESB2_CONFIG_PROPERTIES_FILE_PATH);
		pro.store(fos, " ESB config information ");
		fos.close();
	}

}
