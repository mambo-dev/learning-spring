package com.example.demo.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

/**
 * 스프링 자체 스케줄링 기능 예시
 *  NOTE: @EnableScheduling이 선언되어야 @Schedules 또는 @Scheduled이 동작한다.
 * @author mambo
 */
@Slf4j
@Component
public class SimpleScheduler implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Scheduled(fixedRate = 1000 * 60)
    public void repeat() {
        log.info("{}", ZonedDateTime.now());
    }

    @Schedules({
        @Scheduled(fixedRate = 1000 * 60),
        @Scheduled(cron = "@hourly")
    })
    public void publish() {
        applicationContext.publishEvent(ZonedDateTime.now());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
