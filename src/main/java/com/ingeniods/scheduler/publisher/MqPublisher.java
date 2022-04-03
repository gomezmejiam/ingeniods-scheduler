package com.ingeniods.scheduler.publisher;

import javax.el.MethodNotFoundException;

import org.springframework.stereotype.Service;

import com.ingeniods.scheduler.core.TaskExcecutor;
import com.ingeniods.scheduler.domain.entity.ExcecutionRequest;
import com.ingeniods.scheduler.domain.entity.TaskExcecutionResult;
import com.ingeniods.scheduler.domain.type.PortType;

@Service
public class MqPublisher extends TaskExcecutor{

	public MqPublisher() {
		super(PortType.MESSAGE_QUEUE);
	}
	
	@Override
	public TaskExcecutionResult excecute(ExcecutionRequest excecutionRequest, Integer maxAttemps)
			throws RuntimeException {
		throw new MethodNotFoundException();
	}

}