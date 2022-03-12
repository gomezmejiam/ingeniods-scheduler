package com.ingeniods.scheduler.api;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ingeniods.scheduler.api.dto.request.TaskRequest;
import com.ingeniods.scheduler.api.dto.response.TaskResponse;
import com.ingeniods.scheduler.core.TaskSchedulingService;
import com.ingeniods.scheduler.domain.entity.TaskDefinitionEntity;
import com.ingeniods.scheduler.domain.type.Status;
import com.ingeniods.scheduler.shared.mapper.TaskDefinitionMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/schedule")
public class JobSchedulingController {

    @Autowired
    private TaskSchedulingService taskSchedulingService;

    @PostMapping(path="/", consumes = "application/json", produces="application/json")
    public TaskResponse scheduleATask(@RequestBody TaskRequest taskDefinition) {
    	log.info("Creating task for destination [{}:{}]",taskDefinition.getActionType(),taskDefinition.getDestination());
    	TaskDefinitionEntity task = taskSchedulingService.scheduleATask(TaskDefinitionMapper.toDto(taskDefinition));
        return new TaskResponse(task.getId(),task.getStatus());
    }

    @DeleteMapping(path="/{jobid}")
    public void removeJob(@PathVariable UUID jobid) {
    	log.info("Deleting task {}",jobid);
        taskSchedulingService.removeScheduledTask(jobid.toString(), Status.DELETED);
    }
}