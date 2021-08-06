package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.scheduling.ScheduledTasksEndpoint;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;

import java.time.ZonedDateTime;

@Slf4j
@SpringBootApplication
public class SpringSchedulingTasksApplication implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(SpringSchedulingTasksApplication.class, args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @EventListener
    public void ready(ApplicationReadyEvent readyEvent) {
        log.info("{}", readyEvent.getApplicationContext());
        applicationContext.publishEvent(ZonedDateTime.now());

        ScheduledTasksEndpoint scheduledTasksEndpoint = applicationContext.getBean(ScheduledTasksEndpoint.class);
        ScheduledTasksEndpoint.ScheduledTasksReport scheduledTasksReport = scheduledTasksEndpoint.scheduledTasks();
        log.info("FixedRate: {}", scheduledTasksReport.getFixedRate().size());
    }
}
