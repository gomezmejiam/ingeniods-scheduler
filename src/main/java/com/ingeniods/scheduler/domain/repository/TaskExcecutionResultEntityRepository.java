package com.ingeniods.scheduler.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.ingeniods.scheduler.domain.entity.TaskExcecutionResult;

public interface TaskExcecutionResultEntityRepository extends CrudRepository<TaskExcecutionResult, String> {

	
}
