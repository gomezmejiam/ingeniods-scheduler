package com.ingeniods.scheduler.domain.entity;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.ingeniods.scheduler.domain.type.ActionType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExcecutionRequest {
	String taskId;
	ActionType actionType;
	Map<String, String> headers;
	JsonNode body;
	String destination;
}
