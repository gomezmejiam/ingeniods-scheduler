package com.ingeniods.scheduler.domain.repository;

import java.util.UUID;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ingeniods.scheduler.domain.entity.TaskDefinitionEntity;
import com.ingeniods.scheduler.domain.type.Status;

public interface TaskDefinitionEntityRepository extends CrudRepository<TaskDefinitionEntity, UUID> {

  @Query(value = "FROM TASK_DEFINITIONS td WHERE td.status IN ('IN_PROGRESS','PENDING')")
  Iterable<TaskDefinitionEntity> findActive();

  @Transactional
  @Modifying
  @Query("UPDATE TASK_DEFINITIONS TD SET TD.status = :STATUS WHERE TD.id = :ID")
  void updateStatus(@Param(value = "STATUS") Status status, @Param(value = "ID") String id);
	
}
