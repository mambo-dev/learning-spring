# Spring Scheduling Tasks

본 리파지토리는 스프링 프레임워크의 태스크 수행 및 스케줄링 기능에 대한 학습을 위한 공간입니다.

## Dependencies  

- spring-boot-starter:2.5.3
- spring-boot-starter-actuator:2.5.3
- spring-boot-starter-quartz:2.5.3
  - quartz:2.3.2
- lombok:1.18.20

## [Task Execution and Scheduling](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.task-execution-and-scheduling)
스프링 프레임워크는 비동기적으로 어떠한 작업을 수행하거나 반복적으로 수행해야하는 스케줄링 기능을 지원하기 위하여 `TaskExecutor`와 `TaskScheduler` 인터페이스를 제공한다.
단, 비동기 태스크 수행과 스케줄링 기능은 기본적으로 활성화되지 않으며 `@EnableScheduling`와 `@EnableAsync`을 @Configuration이 선언된 구성 메타정보 클래스에 선언해야한다. 

@EnableScheduling이 선언되면 `ThreadPoolTaskScheduler`를 @EnableAsync는 `ThreadPoolTaskExecutor`를 자동 구성한다. 

### List of classes that are useful to know

- EnableScheduling
- TaskSchedulingProperties
- TaskSchedulerBuilder
- EnableAsync
- TaskExecutionProperties
- TaskExecutorBuilder

## [Quartz Scheduler](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.quartz)
스프링 프레임워크는 쿼츠 스케줄러 라이브러리와의 통합을 지원한다. 스프링 부트는 쿼츠 라이브러리가 클래스패스에 존재하면 `SchedulerFactoryBean`을 자동 구성한다.

쿼츠 스케줄러 라이브러리에 의한 스케줄러는 쉽게 등록할 수 있도록 쿼츠 Job 인터페이스에 대한 추상 클래스인 `QuartzJobBean`를 지원한다.

### List of classes that are useful to know
- SpringBeanJobFactory
- SchedulerFactoryBean
- QuartzJobBean
- JobBuilder
- CronTriggerFactoryBean
- SimpleTriggerFactoryBean
- CronExpression

### Troubleshooting
스프링이 지원하는 @hourly와 같은 CronExpression의 매크로를 사용할 수 없다. 
