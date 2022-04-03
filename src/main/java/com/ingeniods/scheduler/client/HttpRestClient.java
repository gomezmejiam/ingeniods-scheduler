package com.ingeniods.scheduler.client;

import java.net.URI;
import java.util.function.Function;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.ingeniods.scheduler.core.TaskExcecutor;
import com.ingeniods.scheduler.domain.entity.ExcecutionRequest;
import com.ingeniods.scheduler.domain.entity.TaskExcecutionResult;
import com.ingeniods.scheduler.domain.type.ActionType;
import com.ingeniods.scheduler.domain.type.PortType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HttpRestClient extends TaskExcecutor {

	public HttpRestClient() {
		super(PortType.REST);
	}
	
	private static RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Override
	public TaskExcecutionResult excecute(ExcecutionRequest excecutionRequest, Integer maxAttemps)
			throws RuntimeException {
		Function<ExcecutionRequest, String> excecution = er -> {
			 log.info("Requesting task id [{}] for [{}]", er.getTaskId(),er.getDestination());
			 return getRestTemplate().exchange(uri(er.getDestination()), httpMethod(er.getActionType()), request(er), String.class).getBody();
		};
		return RetryableExcecutor.getInstance().excecute(excecutionRequest, excecution, maxAttemps);
	}
	
	private HttpMethod httpMethod(ActionType actionType) {
		return HttpMethod.valueOf(actionType.name());
	}
	
	private URI uri(String destination) {
		return URI.create(destination);
	}

	private HttpEntity<JsonNode> request(ExcecutionRequest excecutionRequest) {
		HttpHeaders httpHeaders = headers(excecutionRequest);
		return new HttpEntity<>(excecutionRequest.getBody(), httpHeaders);
	}

	private HttpHeaders headers(ExcecutionRequest excecutionRequest) {
		HttpHeaders httpHeaders = new HttpHeaders();
		excecutionRequest.getHeaders().entrySet().stream().forEach(e -> httpHeaders.add(e.getKey(), e.getValue()));
		return httpHeaders;
	}

}