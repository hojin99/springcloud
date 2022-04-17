# Spring Cloud

## 환경

* rabbitmq (run with docker container in wsl)  
포트 : 5672(qmqp) , 15672(web)  

* eureka-server - service 정보를 중앙에서 관리하며, client들에게 제공하는 역할  
포트 : 8787

* config-server - configuration 정보를 다른 서버들에게 제공하는 역할 (중앙 관리)  
포트 : 8888

* gateway-server - api gateway 역할을 하면서, 모든 요청을 로깅하고, 인증을 담당 (config-client, eureka-client, hystrix, feign-client)  
포트 : 8030

* ec-user-service - 사용자 정보 서비스  
포트 : random  

* ec-order-service - 제품 주문 서비스  
포트 : random  

* ec-catalog-service - 제품 카타로그 서비스  
포트 : random  


## 실행

* 실행 순서
    - eureka-server : 의존성 없음
    - rabbitmq : 의존성 없음
    - config-server : rabbitmq 필요
    - 나머지는 순서 없음 
    
* eureka-server  
    - spring boot app 실행 방법으로 실행 (아래)  
    - 유레카 대시보드 UI 
    - http://localhost:8787/  

* rabbitmq (docker container in wsl)
    - `docker-compose up -d` 
    - rabbitmq 관리 UI   
    - http://localhost:15672/  

* config-server  
    - spring boot app 실행 방법으로 실행 (아래)  
    - config-server endpoints
    - http://localhost:8888/actuator/** (예: http://localhost:8888/actuator/env)  

* 각 서비스 별 spring boot application 실행 방법으로 실행   
    - Application 소스에서 실행(Intellij)  
    - Toolbar에서 실행(Intellij)  
    - mvn spring-boot:run  
    - java -jar jar파일  


## 참조
eureka server/client : https://docs.spring.io/spring-cloud-netflix/docs/current/reference/html/  
spring cloud gateway : https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/  
spring cloud config : https://docs.spring.io/spring-cloud-config/docs/current/reference/html/  
spring cloud openfeign : https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/  
spring cloud bus : https://docs.spring.io/spring-cloud-bus/docs/current/reference/html/  
spring for apache kafka : https://docs.spring.io/spring-kafka/docs/current/reference/html/  

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
    - GlobalFilter 구현  
    - Eureka에 등록된 서비스이름을 이용해서 로드밸런싱   
```
spring:
  application:
    name: gateway-service
  cloud:
    gateway:
      default-filters: # Global Filter 사용
        - name: GlobalFilter
          args:
            baseMessage: Spring Cloud Gateway GlobalFilter
            preLogger: true
            postLogger: true
      routes:
        - id: user-service # eureka server에 등록된 spring.application.name
          uri: lb://USER-SERVICE # 포워딩할 uri : user-service를 load balancing (eureak 서버 내 이름 기반)
          predicates:
            - Path=/user/**  # 해당 패턴에 대해서
          filters:
            - RewritePath=/user/?(?<segment>.*), /$\{segment} # URL을 변경
            - CustomAuthFilter # 해당 필터 클래스로 필터 실행
        - id: statdata-service
          uri: lb://STATDATA-SERVICE
          predicates:
            - Path=/statdata/**
          filters:
            - AddRequestHeader=sc_req,statdata # 기본 필터로 필터 실행
            - AddResponseHeader=sc_res,statdata
            - RewritePath=/statdata/?(?<segment>.*), /$\{segment}
            - CustomAuthFilter
        - id: mapdata-service
          uri: lb://MAPDATA-SERVICE
          predicates:
            - Path=/mapdata/**
          filters:
            - AddRequestHeader=sc_req,mapdata
            - AddResponseHeader=sc_res,mapdata
            - RewritePath=/mapdata/?(?<segment>.*), /$\{segment}
            - name: CustomAuthFilter
            - name: LoggingFilter # 커스텀 필터에 args를 사용 시 name 필요
              args:
                baseMessage: Spring Cloud Gateway LoggingFilter
                preLogger: true
                postLogger: true
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
