package com.ingeniods.scheduler.client;

import java.net.URI;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

public class HttpRestClient {
	
	private HttpRestClient() {}

	
	public void excecute(HttpMethod method, Map<String,String> headers, JsonNode body, URI url) {
    	HttpHeaders httpHeaders = new HttpHeaders();
    	headers.entrySet().stream().forEach(e -> {
    		httpHeaders.add(e.getKey(), e.getValue());
    	});
    	
    	HttpEntity<JsonNode> request = new HttpEntity<>(body,httpHeaders);
    	getRestTemplate().exchange(url,method,request, String.class);
    }
	
    private static RestTemplate getRestTemplate() {
    	return new RestTemplate();
    }

	public static HttpRestClient getInstance() {
		return new HttpRestClient();
	}
	
}