package com.ingeniods.scheduler.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.ingeniods.scheduler.core.TaskSchedulingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OnReadyListener implements ApplicationListener<ApplicationReadyEvent>  {
	
	@Autowired
	private TaskSchedulingService taskSchedulingService;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		log.info("onApplicationEvent");
		taskSchedulingService.loadTasks();
	}

}
