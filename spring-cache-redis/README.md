# Spring Cache Redis

이 리파지토리는 스프링 캐시 추상화를 이해하기 위한 학습 공간입니다.

- Java 11
- Spring Boot 2.4.3
- Redis localhost:6379

## Cache Abstraction
스프링의 캐시 추상화는 스프링 [3.1](https://docs.spring.io/spring-framework/docs/3.2.9.RELEASE/spring-framework-reference/htmlsingle/#cache) 부터 지원된 기능으로 쉽고 일관된 방식으로 캐시 기능을 적용할 수 있다. 추가적으로 스프링 4.1에서부터 좀 더 확장된 옵션을 제공한다. 

자바 어노테이션과 함께 [SpEL](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#cache-spel-context) 을 사용하여 효율적으로 키를 작성할 수 있다. 

## Run Redis
```sh
docker-compose up -d

# 스프링 캐시 조회
redis-cli keys spring:cache*

# 모든 데이터 삭제
redis-cli flushall async
```

## Troubleshooting

### java.io.NotSerializableException: com.example.demo.domain.User
레디스에 저장될 도메인 클래스를 직렬화할 수 있도록 Serializable 인터페이스 구현.
```java
public class User implements Serializable {
    private static final long serialVersionUID = 1384295212965556694L;
}
```

### java.io.InvalidClassException: com.example.demo.domain.User
java.io.InvalidClassException: com.example.demo.domain.User; local class incompatible: stream classdesc serialVersionUID = 2647231258959551334, local class serialVersionUID = 1384295212965556694

직렬화 버전이 다르므로 캐시된 데이터 삭제
```sh
redis-cli flushall async
```
