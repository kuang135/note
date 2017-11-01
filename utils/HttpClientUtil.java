package com.demo.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class HttpClientUtil {
	
	
	private HttpClientUtil() {
		
	}
	
	/*
	 * 	GET 请求
	 */
	public static String doGet(String url) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();  
		HttpGet httpGet = new HttpGet(url);  
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);  
			 if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				 HttpEntity entity = response.getEntity();  
                return EntityUtils.toString(entity, "UTF-8");
            }
		} finally { 
			if (response != null) {
				response.close();  
			}
		}
		return null;
	}
	
	
	/*
	 * 	POST 请求
	 */
	public String doPost(String url, Map<String, String> params) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault(); 
        HttpPost httpPost = new HttpPost(url);
        if (null != params) {
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, Consts.UTF_8);
            httpPost.setEntity(formEntity);
        }
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }
	
	/*
	 * 	POST 请求，后台用 @RequestBody 接收
	 */
	public String doPostJson(String url, String json) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault(); 
        HttpPost httpPost = new HttpPost(url);
        if(null != json){
            StringEntity stringEntity = new StringEntity(json, "UTF-8");
            httpPost.setEntity(stringEntity);
        }
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }
	
	/*
	 * 	POST 请求，上传文件，后台用 @RequestParam("files") MultipartFile[] multipartFiles
	 */
	public static String doPostFiles(String url, File[] files) throws Exception{
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = builder.build();
		HttpPost httpPost = new HttpPost(url);
		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		for (File file : files) {
			entityBuilder.addPart("files", new FileBody(file));
		}
		HttpEntity httpEntity = entityBuilder.build();
		httpPost.setEntity(httpEntity);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == HttpStatus.SC_OK){
				HttpEntity entity = response.getEntity();  
                return EntityUtils.toString(entity, "UTF-8");
			}
		} finally {
			if (response != null) {
				response.close();  
			}
			httpPost.releaseConnection();
		}
		return null;
	}
		
	public static void main(String[] args) throws Exception {  
		File dir = new File("e:\\ceair\\index");
		File[] files = dir.listFiles();
		String result = HttpClientUtil.doPostFiles("http://localhost/mumop/resource/lucene/sync.do",  files);
		System.out.println(result);
		
//		String url = "http://localhost/mumop/index.html";
//		String back = HttpClientUtil.doGet(url);
//		System.out.println(back);
	}

}
