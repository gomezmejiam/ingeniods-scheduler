package com.ingeniods.scheduler.api.dto.request;

import java.time.LocalDateTime;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.ingeniods.scheduler.domain.type.ActionType;
import com.ingeniods.scheduler.domain.type.CronExpresion;

import lombok.Data;

@Data
public class TaskRequest {

    private CronExpresion cronExpression;
    private ActionType actionType;
    private String name;
    private JsonNode data;
    private HashMap<String,String> headers;
    private String destination;
    private LocalDateTime evictTime;
    private Integer maxAttemps;
    private Integer maxExcecutions;
}