package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

/**
 * 쿼츠 스케줄러에 의한 잡을 제어해보자.
 * @author mambo
 */
@Slf4j
@Service
public class QuartzSchedulerService {

    private final SchedulerFactoryBean schedulerFactoryBean;

    public QuartzSchedulerService(SchedulerFactoryBean schedulerFactoryBean) {
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    public boolean pause(String key) {
        return pause(TriggerKey.triggerKey(key));
    }

    public boolean pause(TriggerKey triggerKey) {
        boolean paused = false;
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.pauseTrigger(triggerKey);
            paused = true;
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return paused;
    }

    public boolean pauseAll() {
        boolean paused = false;
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.pauseAll();
            paused = true;
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return paused;
    }

    public boolean resume(String key) {
        return resume(TriggerKey.triggerKey(key));
    }

    public boolean resume(TriggerKey triggerKey) {
        boolean resumed = false;
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            Trigger trigger = scheduler.getTrigger(triggerKey);
            scheduler.resumeTrigger(triggerKey);
            resumed = true;
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return resumed;
    }

    public boolean resumeAll() {
        boolean paused = false;
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.resumeAll();
            paused = true;
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return paused;
    }
}
