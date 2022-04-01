package com.ingeniods.scheduler.domain.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ingeniods.scheduler.domain.entity.TaskDefinitionEntity;
import com.ingeniods.scheduler.domain.type.Status;

public interface TaskDefinitionEntityRepository extends CrudRepository<TaskDefinitionEntity, String> {

  @Query(value = "FROM TASK_DEFINITIONS td WHERE td.status IN ('IN_PROGRESS','PENDING')")
  Iterable<TaskDefinitionEntity> findActive();

  @Transactional(propagation = Propagation.REQUIRED)
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("UPDATE TASK_DEFINITIONS TD SET TD.status = :STATUS WHERE TD.id = :ID")
  int updateStatus(@Param(value = "STATUS") Status status, @Param(value = "ID") String id);
	
}
