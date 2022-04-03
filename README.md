# API Gateway

## 의존성

```
spring-cloud-starter-gateway
```

## 설정

```yaml
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://localhost:8761/eureka

spring:
  cloud:
    gateway:
      routes:
        - id: first-service
          uri: lb://MY-FIRST-SERVICE  # 어디로 forward 될것인가
          predicates: # 조건정보, Path 에 해당하는 요청을 uri 로 전달함. 
            - Path=/first-service/**  # 요청이 http://localhost:8081/first-service/** 로 넘어가므로 /first-service 주의
```
- routes: uri => lb://{MICRO-SERVICE-NAME}

## Filter

### java Bean

- FilterConfig.java 확인

### application.yml

- CustomFilter.java
- GlobalFilter.java
- application.yml
    ```yaml
    spring:
      cloud:
        gateway:
          routes:
            - id: first-service
              uri: lb://MY-FIRST-SERVICE
              predicates: # 조건정보
                - Path=/first-service/**  # 요청이 http://localhost:8081/first-service/** 로 넘어가므로 /first-service 주의
              filters:
                - CustomFilter
          default-filters: # 모든 서비스에 적용됨
            - name: GlobalFilter
              args:
                baseMessage: Spring Cloud Gateway Global Filter
                preLogger: true # GlobalConfig.java > config.isPreLogger()
                postLogger: true # GlobalConfig.java > config.isPostLogger()
    ```
    - filters 를 통해 필터 등록
    - default-filters = 글로벌 필터

## Netty

- 실행 시 Netty 서버로 작동 (비동기 서버)