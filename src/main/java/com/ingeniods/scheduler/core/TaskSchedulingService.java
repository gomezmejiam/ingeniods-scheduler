package com.ingeniods.scheduler.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.ingeniods.scheduler.domain.entity.ExcecutionRequest;
import com.ingeniods.scheduler.domain.entity.TaskDefinitionEntity;
import com.ingeniods.scheduler.domain.entity.TaskExcecutionResult;
import com.ingeniods.scheduler.domain.repository.TaskDefinitionEntityRepository;
import com.ingeniods.scheduler.domain.repository.TaskExcecutionResultEntityRepository;
import com.ingeniods.scheduler.domain.type.ActionType;
import com.ingeniods.scheduler.domain.type.Status;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TaskSchedulingService {

	@Autowired
	private TaskScheduler taskScheduler;

	@Autowired
	private TaskDefinitionEntityRepository taskDefinitions;
	
	@Autowired
	private TaskExcecutionResultEntityRepository taskResults;
	
	@Autowired
	List<TaskExcecutor> taskExcecutors; 

	Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();

	public TaskDefinitionEntity scheduleATask(TaskDefinitionEntity taskDefinition) {
		TaskDefinitionEntity entity = saveTask(taskDefinition);
		loadScheduledFuture(entity);
		return entity;
	}

	public void loadTasks() {
		log.info("Loading all pending tasks");
		taskDefinitions.findActive().forEach(this::loadScheduledFuture);
	}

	private ScheduledFuture<?> loadScheduledFuture(TaskDefinitionEntity entity) {
		Runnable runnableTask = getRunnableTask(entity);
		ScheduledFuture<?> scheduledTask = taskScheduler.schedule(runnableTask, getCronTrigger(entity));
		jobsMap.put(entity.getId(), scheduledTask);
		return scheduledTask;
	}

	private Runnable getRunnableTask(TaskDefinitionEntity entity) {
		log.info("generation runnable task [{}] for [{}:{}] ", entity.getId(), entity.getActionType(),
				entity.getDestination());
		return () -> {
			log.info("Runing task [{}] for [{}:{}] ", entity.getId(), entity.getActionType(), entity.getDestination());

			if (entity.isFinalized()) {
				removeScheduledTask(entity.getId(), Status.FINALIZED);
				return;
			}
			excecuteTask(entity);
			registrarExcecution(entity);
		};
	}

	private void excecuteTask(TaskDefinitionEntity entity) {
		log.info("Excecuting task [{}] for [{}:{}] ", entity.getId(), entity.getActionType(), entity.getDestination());
		entity.setStatus(Status.IN_PROGRESS);

		ExcecutionRequest excecutionRequest = ExcecutionRequest.builder().body(entity.getData())
				.headers(entity.getHeaders()).actionType(entity.getActionType()).taskId(entity.getId())
				.destination(entity.getDestination()).build();
		
		TaskExcecutor excecutor = getTaskExcecutor(entity.getActionType());
		TaskExcecutionResult result = excecutor.excecute(excecutionRequest, entity.getMaxAttemps());
		taskResults.save(result);
		log.info("Excecuted task [{}] for [{}:{}] status [{}]", entity.getId(), entity.getActionType(),
				entity.getDestination(), entity.getStatus());
	}

	private TaskExcecutor getTaskExcecutor(ActionType actionType) {
		return taskExcecutors.stream()
		.filter(x -> x.getPortType().equals(actionType.getPort()))
		.findFirst()
		.orElseThrow(()->new RuntimeException("TaskExcecutor for " + actionType.name() + " can't be found"));	
	}

	private void registrarExcecution(TaskDefinitionEntity entity) {
		entity.setExcecutions(entity.getExcecutions() + 1);
		if (entity.isFinalized()) {
			removeScheduledTask(entity.getId(), Status.FINALIZED);
			entity.setStatus(Status.FINALIZED);
		}
		log.info("Register excecution task [{}] for [{}:{}] status [{}] excecutions [{}]", entity.getId(),
				entity.getActionType(), entity.getDestination(), entity.getStatus(), entity.getExcecutions());

		taskDefinitions.save(entity);
	}

	private TaskDefinitionEntity saveTask(TaskDefinitionEntity entity) {
		entity.setId(UUID.randomUUID().toString());
		entity.setStatus(Status.PENDING);
		return taskDefinitions.save(entity);
	}

	private CronTrigger getCronTrigger(TaskDefinitionEntity runnableTask) {
		return new CronTrigger(runnableTask.getCronExpression().getExpresion(),
				TimeZone.getTimeZone(TimeZone.getDefault().getID()));
	}

	public void removeScheduledTask(String jobId, Status status) {
		log.info("Removing task [{}] with status[{}] ", jobId, status);

		if (!jobsMap.containsKey(jobId)) {
			log.info("Task [{}] not found in job maps", jobId);
			// TODO: throw a no content exception
			return;
		}

		ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
		if (Objects.isNull(scheduledTask)) {
			log.info("Task [{}] not found in job maps", jobId);
			// TODO: throw a no content exception
			return;
		}

		taskDefinitions.updateStatus(status, jobId);
		log.info("Task [{}] updated to [{}]", jobId, status);
		scheduledTask.cancel(true);
		jobsMap.remove(jobId);
		log.info("Task [{}] removed from triggers", jobId);
	}
}