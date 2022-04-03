package com.ingeniods.scheduler.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.ingeniods.scheduler.domain.type.ExceutionStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "TASK_EXCECUTION_RESULTS")
public class TaskExcecutionResult {
	@Id
	private String id;
	private String taskId;
    private String result;
    private LocalDateTime excutedAt;
    @Enumerated(EnumType.STRING)
    private ExceutionStatus status;
    private Integer attemps;
}