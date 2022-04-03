package com.ingeniods.scheduler.core;

import com.ingeniods.scheduler.domain.entity.ExcecutionRequest;
import com.ingeniods.scheduler.domain.entity.TaskExcecutionResult;
import com.ingeniods.scheduler.domain.type.PortType;

import lombok.Getter;

public abstract class TaskExcecutor {
	
	@Getter
	public final PortType portType;
	
	protected TaskExcecutor(PortType portType) {
		this.portType = portType;
	}
	
	public abstract TaskExcecutionResult excecute(ExcecutionRequest excecutionRequest, Integer maxAttemps) throws RuntimeException;

}