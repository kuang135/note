package com.fccs.memcache.demo;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

import org.junit.Before;
import org.junit.Test;


public class Demo {

	private static final long DEFAULT_TIMEOUT = 5;
	private static final TimeUnit DEFAULT_TIMENUINT = TimeUnit.SECONDS;
	
	
	MemcachedClient client = null;
	
	@Before
	public void getConnection() throws IOException {
		client = new MemcachedClient(AddrUtil.getAddresses("127.0.0.1:11211"));
	}
	
	
	// Set：添加一个新条目到memcached，或是用新的数据替换替换掉已存在的条目 
	@Test
	public void setTest() throws IOException, InterruptedException, ExecutionException, TimeoutException {
		OperationFuture<Boolean> operate = client.set("key1", 30, "value1");
		// 注意下面的代码：set操作，没有下面的代码，操作失败；add，replace，delete操作，没有下面的代码，操作成功
		Boolean stored = operate.get(DEFAULT_TIMEOUT, DEFAULT_TIMENUINT);
		System.out.println(stored);
		String value = (String) client.get("key1");
		System.out.println(value);
		
		Boolean stored_new = client.set("key1", 30, "value1_new").get(DEFAULT_TIMEOUT, DEFAULT_TIMENUINT);
		System.out.println(stored_new);
		String value_new = (String) client.get("key1");
		System.out.println(value_new);
	}
	
	// Add：当KEY不存在的情况下，它向memcached存数据，否则，返回NOT_STORED响应 
	@Test
	public void addTest() throws IOException, InterruptedException, TimeoutException, ExecutionException {
		Boolean stored = client.add("key2", 30, "value2").get(DEFAULT_TIMEOUT, DEFAULT_TIMENUINT);
		System.out.println(stored);
		String value = (String) client.get("key2");
		System.out.println(value);
		
		Boolean stored_new = client.add("key2", 30, "value2_new").get(DEFAULT_TIMEOUT, DEFAULT_TIMENUINT);
		System.out.println(stored_new);
		String value_new = (String) client.get("key2");
		System.out.println(value_new);
	}
	
	// Replace：当KEY存在的情况下，它才会向memcached存数据，否则返回NOT_STORED响应 
	@Test
	public void replaceTest() throws InterruptedException, TimeoutException, ExecutionException {
			Boolean stored = client.replace("key3", 30, "value3").get(DEFAULT_TIMEOUT, DEFAULT_TIMENUINT);
			System.out.println(stored);
			String value = (String) client.get("key3");
			System.out.println(value);
			
			client.set("key3", 30, "value3").get(DEFAULT_TIMEOUT, DEFAULT_TIMENUINT);
			
			Boolean stored_new = client.replace("key3", 30, "value3_new").get(DEFAULT_TIMEOUT, DEFAULT_TIMENUINT);
			System.out.println(stored_new);
			String value_new = (String) client.get("key3");
			System.out.println(value_new);
	}

	@Test
	public void mlutiGetTest() throws InterruptedException, TimeoutException, ExecutionException {
		client.set("key4", 30, "value4").get(DEFAULT_TIMEOUT, DEFAULT_TIMENUINT);
		client.set("key5", 30, "value5").get(DEFAULT_TIMEOUT, DEFAULT_TIMENUINT);
		Map<String, Object> bulk = client.getBulk("key4", "key5");
		for (Entry<String, Object> entry : bulk.entrySet()) {
			System.out.println("key: " + entry.getKey() + " -- value: " +entry.getValue());
		}
	}
	
	@Test
	public void deleteTest() throws InterruptedException, TimeoutException, ExecutionException {
		client.set("key5", 30, "value5").get(DEFAULT_TIMEOUT, DEFAULT_TIMENUINT);
		System.out.println(client.get("key5"));
		
		client.delete("key5").get(DEFAULT_TIMEOUT, DEFAULT_TIMENUINT);
		System.out.println(client.get("key5"));
	}
	
	/*
	 * Cas: 改变一个存在的KEY值 ，但它还带了检查的功能 
	 * Append: 在这个值后面插入新值 
	 * Prepend: 在这个值前面插入新值
	 * */
	
	
}
