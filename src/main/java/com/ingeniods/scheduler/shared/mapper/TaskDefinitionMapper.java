package com.ingeniods.scheduler.shared.mapper;

import com.ingeniods.scheduler.api.dto.request.TaskRequest;
import com.ingeniods.scheduler.domain.entity.TaskDefinitionEntity;

public class TaskDefinitionMapper {
	private TaskDefinitionMapper() {}
	
	public static TaskDefinitionEntity toDto(TaskRequest taskDefinition) {
		TaskDefinitionEntity entity = new TaskDefinitionEntity();
		entity.setActionType(taskDefinition.getActionType());
		entity.setCronExpression(taskDefinition.getCronExpression());
		entity.setData(taskDefinition.getData());
		entity.setDestination(taskDefinition.getDestination() );
		entity.setEvictTime(taskDefinition.getEvictTime());
		entity.setHeaders(taskDefinition.getHeaders());
		entity.setMaxAttemps(taskDefinition.getMaxAttemps());
		entity.setMaxExcecutions(taskDefinition.getMaxExcecutions());
		entity.setExcecutions(0);
		return entity;
	}
    
}