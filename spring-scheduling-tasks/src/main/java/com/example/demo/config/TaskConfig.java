package com.example.demo.config;

import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SuppressWarnings({"squid:S1118"})
@Configuration
public class TaskConfig {

    @EnableScheduling
    static class SchedulerConfig {
        @Profile({"custom"})
        @Bean
        public TaskScheduler threadPoolTaskScheduler(TaskSchedulingProperties schedulingProperties,
                                                     TaskSchedulerBuilder taskSchedulerBuilder) {
            return taskSchedulerBuilder
                    .threadNamePrefix("scheduling-")
                    .poolSize(15)
                    .build();
        }
    }

    @EnableAsync
    static class ExecutorConfig {
        @Profile({"custom"})
        @Bean
        public TaskExecutor threadPoolTaskExecutor(TaskExecutionProperties executionProperties,
                                                   TaskExecutorBuilder taskExecutorBuilder) {
            ThreadPoolTaskExecutor taskExecutor = taskExecutorBuilder
                    .corePoolSize(8)
                    .maxPoolSize(25)
                    .queueCapacity(100)
                    .threadNamePrefix("task-")
                    .build();
            taskExecutor.initialize();
            return taskExecutor;
        }
    }






}
