package com.example.demo.handler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * 쿼츠 잡 스케줄 등록 예시
 * @author mambo
 */
@Slf4j
@Component
@DisallowConcurrentExecution
public class SimpleQuartzJob extends QuartzJobBean {
    private static final String JOB_NAME = "SimpleQuartzJob";
    private static final String JOB_IDENTITY = JOB_NAME;
    private static final String JOB_TRIGGER_NAME = JOB_NAME + "Trigger";

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("{}", context);
    }

    @Bean(name = JOB_NAME)
    public JobDetail jobDetail() {
        return JobBuilder.newJob()
                .ofType(this.getClass())
                .withIdentity(JOB_IDENTITY)
                .storeDurably(true)
                .build();
    }

    @Bean(name = JOB_TRIGGER_NAME)
    public CronTriggerFactoryBean trigger(@Qualifier(JOB_NAME) JobDetail jobDetail) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setCronExpression("0 * * * * ?"); // 스프링이 지원하는 @hourly와 같은 CronExpression의 매크로를 사용할 수 없다.
        return factoryBean;
    }

}
