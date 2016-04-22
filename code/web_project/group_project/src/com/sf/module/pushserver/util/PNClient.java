package com.sf.module.pushserver.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

@Component
public final class PNClient {

	private static final String charset = "UTF-8";
	private static HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
	private static final int SOCKET_TIMEOUT = 10000;
	private static final int CONNECTION_TIMEOUT = 10000;

	static {
		RequestConfig defaultRequestConfig = RequestConfig
		        .custom()
		        .setSocketTimeout(SOCKET_TIMEOUT)
		        .setConnectTimeout(CONNECTION_TIMEOUT)
		        .setConnectionRequestTimeout(CONNECTION_TIMEOUT)
		        .build();
		httpClientBuilder.setDefaultRequestConfig(defaultRequestConfig);
	}

	private static HttpPost post(String url, String requestContent) throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type", "application/json;CHARSET=utf-8");
		httpPost.setEntity(new StringEntity(requestContent, Charset.forName(charset)));
		return httpPost;
	}

	private static HttpGet get(String url) throws UnsupportedEncodingException {
		return new HttpGet(url);
	}

	public static HttpResponse doPost(String api, String requestContent) throws IOException {
		return httpClientBuilder.build().execute(post(api, requestContent));
	}

	public static HttpResponse doGet(String api) throws IOException {
		return httpClientBuilder.build().execute(get(api));
	}

}
