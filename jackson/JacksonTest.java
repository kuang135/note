package jacnsontest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.junit.Test;

import cn.bll.mobile.bean.Bank;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JacksonTest {
	
	@Test
	public void simpleObjectTest() throws Exception {
		String json = "{\"id\":\"01\",\"name\":\"招商银行\"}";
		ObjectMapper mapper = new ObjectMapper();
		Bank bank = mapper.readValue(json, Bank.class);
		System.out.println(ReflectionToStringBuilder.toString(bank));
		System.out.println(mapper.writeValueAsString(bank));
	}

	@Test
	public void listTest() throws Exception {
		//{"result":1,"msg":null,"data":{"list":[{"id":"01","name":"招商银行"},{"id":"02","name":"中国工商银行"}]}}
		String json = "{\"result\":1,\"msg\":null,\"data\":{\"list\":[{\"id\":\"01\",\"name\":\"招商银行\"},{\"id\":\"02\",\"name\":\"中国工商银行\"}]}}";
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);
		JsonNode dataNode = jsonNode.get("data"); //{"list":[{"id":"01","name":"招商银行"},{"id":"02","name":"中国工商银行"}]}
		JsonNode listNode = dataNode.get("list"); //{"list":[{"id":"01","name":"招商银行"},{"id":"02","name":"中国工商银行"}]}
		CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Bank.class);
		List<Bank> banks = mapper.readValue(listNode.toString(), collectionType);
		System.out.println(ReflectionToStringBuilder.toString(banks));
		System.out.println(mapper.writeValueAsString(banks));
	}
	
	@Test
	public void mapTest() throws Exception {
		//{"list1":{"id":"01","name":"招商银行"},"list2":{"id":"02","name":"中国工商银行"}
		String json = "{\"list1\":{\"id\":\"01\",\"name\":\"招商银行\"},\"list2\":{\"id\":\"02\",\"name\":\"中国工商银行\"}}";
		ObjectMapper mapper = new ObjectMapper();
		MapType mapType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Bank.class);
		Map<String,Bank> map= mapper.readValue(json, mapType);
		System.out.println(ReflectionToStringBuilder.toString(map));
		System.out.println(mapper.writeValueAsString(map));
	}
	
	@Test
	public void mapListTest() throws Exception {
		//{"list1":[{"id":"01","name":"招商银行"},{"id":"02","name":"中国工商银行"}],"list2":[{"id":"03","name":"交通银行"},{"id":"04","name":"建设银行"}]}
		String json = "{\"list1\":[{\"id\":\"01\",\"name\":\"招商银行\"},{\"id\":\"02\",\"name\":\"中国工商银行\"}],"
					+ "\"list2\":[{\"id\":\"03\",\"name\":\"交通银行\"},{\"id\":\"04\",\"name\":\"建设银行\"}]}";
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		JavaType stringType = typeFactory.constructType(String.class);
		CollectionType collectionType = typeFactory.constructCollectionType(ArrayList.class, Bank.class);
		MapType mapType = typeFactory.constructMapType(HashMap.class, stringType, collectionType);
		Map<String, List<Bank>> map = mapper.readValue(json, mapType);
		System.out.println(ReflectionToStringBuilder.toString(map));
		System.out.println(mapper.writeValueAsString(map));
	}
}
