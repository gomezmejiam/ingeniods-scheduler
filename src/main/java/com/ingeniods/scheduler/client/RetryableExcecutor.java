package com.ingeniods.scheduler.client;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import com.ingeniods.scheduler.domain.entity.ExcecutionRequest;
import com.ingeniods.scheduler.domain.entity.TaskExcecutionResult;
import com.ingeniods.scheduler.domain.type.ExceutionStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RetryableExcecutor {

	private static final long BACKOFF_PERIOD = 2000l;
	private static final int MAX_ATTEMPS = 3;
	private static final int MIN_ATTEMPS = 1;

	private RetryableExcecutor() {
	}
	
	public static RetryableExcecutor getInstance() {
		return new RetryableExcecutor();
	}

	public TaskExcecutionResult excecute(ExcecutionRequest excecutionRequest, Function<ExcecutionRequest, String> excecution, Integer maxAttemps)
			throws RuntimeException {
		RetryTemplate template = retryTemplate(validMaxAttemps(maxAttemps));
		return template.execute(getCallback(excecutionRequest, excecution),recoveryCallback(excecutionRequest));
	}
	
	private int validMaxAttemps(Integer maxAttemps) {
		if(Objects.isNull(maxAttemps)) {
			return MAX_ATTEMPS;
		}
		if(maxAttemps < 1) {
			return MIN_ATTEMPS;
		}
		if(maxAttemps > MAX_ATTEMPS) {
			return MAX_ATTEMPS;
		}
		return maxAttemps;
	}

	private RecoveryCallback<TaskExcecutionResult> recoveryCallback(ExcecutionRequest excecutionRequest) {
		return new RecoveryCallback<>() {
			public TaskExcecutionResult recover(RetryContext context) throws RuntimeException {
				log.warn("task[{}] recovery callback for [{}] isExhausted [{}]", excecutionRequest.getTaskId(), excecutionRequest.getDestination() ,context.isExhaustedOnly());
				return taskExcecutionError(excecutionRequest.getTaskId(),context.getLastThrowable().getLocalizedMessage(), context.getRetryCount());
			}
		};
	}

	private RetryCallback<TaskExcecutionResult, RuntimeException> getCallback(ExcecutionRequest excecutionRequest, Function<ExcecutionRequest, String> excecution) {
		return new RetryCallback<>() {
			@Override
			public TaskExcecutionResult doWithRetry(RetryContext context) throws RuntimeException {
				log.info("task[{}] requesting for[{}] have [{}] attemps", excecutionRequest.getTaskId(), excecutionRequest.getDestination() ,context.getRetryCount());
				try {
					String result =  excecution.apply(excecutionRequest);
					return taskExcecutionSuccess(excecutionRequest.getTaskId(),result, context.getRetryCount());
				} catch (Exception e) {
					log.warn("task[{}] requesting for[{}] get error [{}]", excecutionRequest.getTaskId(), excecutionRequest.getDestination() ,e.getLocalizedMessage());
					throw new RuntimeException(e);
				}
			}
		};
	}

	private RetryTemplate retryTemplate(Integer maxAttemps) {
		RetryTemplate template = new RetryTemplate();
		template.setRetryPolicy(retryPolicy(maxAttemps));
		template.setBackOffPolicy(backoffPolicy());
		return template;
	}

	private BackOffPolicy backoffPolicy() {
		FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(BACKOFF_PERIOD);
		return fixedBackOffPolicy;
	}

	private RetryPolicy retryPolicy(Integer maxAttemps) {
		SimpleRetryPolicy policy = new SimpleRetryPolicy();
		policy.setMaxAttempts(maxAttemps);
		return policy;
	}
	
	private TaskExcecutionResult taskExcecutionError(String taskId, String result, Integer attemps) {
		log.error("task[{}] excecuted with error [{}]", taskId, result);
		 return taskExcecutionResult(taskId, result, ExceutionStatus.ERROR, attemps);
	}
	
	private TaskExcecutionResult taskExcecutionSuccess(String taskId, String result, Integer attemps) {
		 return taskExcecutionResult(taskId, result, ExceutionStatus.SUCCESS, attemps);
	}
	
	private TaskExcecutionResult taskExcecutionResult(String taskId, String result, ExceutionStatus status, Integer attemps) {
		TaskExcecutionResult taskExcecutionResult = new TaskExcecutionResult();
		taskExcecutionResult.setTaskId(taskId);
		taskExcecutionResult.setExcutedAt(LocalDateTime.now());
		taskExcecutionResult.setId(UUID.randomUUID().toString());
		taskExcecutionResult.setResult(result);
		taskExcecutionResult.setStatus(status);
		taskExcecutionResult.setAttemps(attemps);
		 return taskExcecutionResult;
	}

}