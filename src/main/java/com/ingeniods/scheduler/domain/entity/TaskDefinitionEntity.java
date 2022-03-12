package com.ingeniods.scheduler.domain.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.JsonNode;
import com.ingeniods.scheduler.domain.converter.HashMapConverter;
import com.ingeniods.scheduler.domain.converter.JsonNodeConverter;
import com.ingeniods.scheduler.domain.type.ActionType;
import com.ingeniods.scheduler.domain.type.CronExpresion;
import com.ingeniods.scheduler.domain.type.Status;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Entity(name = "TASK_DEFINITIONS")
public class TaskDefinitionEntity {
	@Id
	private String id;
	@Enumerated(EnumType.STRING)
    private CronExpresion cronExpression;
    @Enumerated(EnumType.STRING)
    private ActionType actionType;
    @Convert(converter = JsonNodeConverter.class)
    @Column(name = "data", columnDefinition = "JSON")
    private JsonNode data;
    @Convert(converter = HashMapConverter.class)
    @Column(name = "headers", columnDefinition = "JSON")
    private HashMap<String,String> headers;
    private String destination;
    private LocalDateTime evictTime;
    private Integer maxAttemps;
    private Integer excecutions;
    private Integer maxExcecutions;
    @Enumerated(EnumType.STRING)
    private Status status;
    
   public boolean isFinalized() {
	   if(Objects.nonNull(getEvictTime()) && getEvictTime().isAfter(LocalDateTime.now())){
   		log.warn("La task {} se encuentra vencida", getId());
   		return true;
   	}
   	
   	if(Objects.nonNull(getMaxExcecutions()) && getMaxExcecutions() <= getExcecutions() ) {
   		log.warn("La task {} alcanzó el número máximo de ejecuciones", getId());
   		return true;
   	}
   	return false;
   } 
}