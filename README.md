# Spring Cloud

## 환경

* eureka-server - service 정보를 중앙에서 관리하며, client들에게 제공하는 역할  
포트 : 8787

* config-server - configuration 정보를 다른 서버들에게 제공하는 역할 (중앙 관리)  
포트 : 8040

* gateway-server - api gateway 역할을 하면서, 모든 요청을 로깅하고, 인증을 담당 (config-client, eureka-client, hystrix, feign-client)  
포트 : 8030

* user-service - 사용자 정보 서비스 (api-service, config-client, eureka-client, hystrix, feign-client)  
포트 : 8000

* statdata-service - 통계 정보 서비스 (api-service, config-client, eureka-client, hystrix, feign-client)  
포트 : 8020, 8021

* mapdata-service - map 정보 서비스 (api-service, config-client, eureka-client, hystrix, feign-client)  
포트 : 8010, 8011

## 실행
* 각 서비스 별 spring boot application 실행 방법으로 실행 (단, Config Server, Eureka Server 먼저 실행)   
    - Application 소스에서 실행(Intellij)  
    - Toolbar에서 실행(Intellij)  
    - mvn spring-boot:run  
    - java -jar jar파일  
    
* eureka-server  
    - 유레카 대시보드 UI 
    - http://localhost:8787/  


## 참조

* eureka-server
    - @EnableEurekaServer 추가  
    - application.yml에 아래와 같이 설정  
```
server:
  port: 8787

spring:
  application:
    name: discover-service # eureka에서 사용하는 이름

eureka:
  client:
    fetch-registry: false # 유레카 서비스에 주기적으로 자신을 등록하지 않음 (즉, 클라이어언트로 사용 안함)
    register-with-eureka: false # 유레카 서비스가 시작할 때 레지스트리 정보를 로컬에 저장하지 않음 (즉, 클라이어언트로 사용 안함)
```

* eureka-client
    - @EnableDiscoveryClient 추가  
    - application.yml에 아래와 같이 설정  
    
```
spring:
  application:
    name: mapdata-service

eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    serviceUrl:
      defaultZone: ${EUREKA_URL:http://127.0.0.1:8787/eureka}
```

* spring cloud gateway
    - application.yml을 아래와 같이 route 설정  
    - CustomAuthFilter 구현  
```
server:
  port: 8030

spring:
  application:
    name: gateway-service
  cloud:
    gateway:
      routes:
        - id: user-service # eureka server에 등록된 spring.application.name
          uri: lb://USER-SERVICE # user-service를 load balancing
          predicates:
            - Path=/user/**  # 해당 패턴에 대해서
          filters:
            - RewritePath=/user/?(?<segment>.*), /$\{segment}  # URL을 변경
            - CustomAuthFilter # 해당 필터 클래스로 필터 실행

        - id: statdata-service
          uri: lb://STATDATA-SERVICE
          predicates:
            - Path=/statdata/**
          filters:
            - RewritePath=/statdata/?(?<segment>.*), /$\{segment}
            - CustomAuthFilter

        - id: mapdata-service
          uri: lb://MAPDATA-SERVICE
          predicates:
            - Path=/mapdata/**
          filters:
            - RewritePath=/mapdata/?(?<segment>.*), /$\{segment}
            - CustomAuthFilter
```

* Feign Client  
    - @EnableFeignClients 추가  
    - 아래와 같이 @FeignClient 어노테이션을 가진 인터페이스를 생성 후
    - Controller에서 호출해서 사용

```
@FeignClient("mapdata-service")
public interface MapdataServiceClient {

    @GetMapping(value="/api/hello")
    String hello();
}

```
```    
@Autowired
private MapdataServiceClient mapClient;

@GetMapping("/api/hello-map")
public String helloMapdata() {
   return mapClient.hello();
}
```
