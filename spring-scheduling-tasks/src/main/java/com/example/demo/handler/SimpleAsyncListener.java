package com.example.demo.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

/**
 * 비동기 수행 및 리스너 예시
 * @author mambo
 */
@Slf4j
@Component
public class SimpleAsyncListener {
    @Async
    @EventListener
    public void handleDateTime(ZonedDateTime dateTime) {
        // NOTE: @EnableAsync를 선언할 때는 TaskExecutor에 의해 처리된다.
        log.info("handle => {}", dateTime);
    }
}
