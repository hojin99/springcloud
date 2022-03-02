# Spring Cloud Test

### config-server 
* configuration 정보를 다른 서버들에게 제공하는 역할 (중앙 관리)
* 포트 : 8040

### eureka-server
* service 정보를 중앙에서 관리하며, client들에게 제공하는 역할
* 포트 : 8787

### gateway-server
* api gateway 역할을 하면서, 모든 요청을 로깅하고, 인증을 담당 (config-client, eureka-client, hystrix, feign-client)
* 포트 : 8030

### user-service 
* 사용자 정보 서비스 (api-service, config-client, eureka-client, hystrix, feign-client)
* 포트 : 8000

### statdata-service
* 통계 정보 서비스 (api-service, config-client, eureka-client, hystrix, feign-client)
* 포트 : 8020, 8021

### mapdata-service
* map 정보 서비스 (api-service, config-client, eureka-client, hystrix, feign-client)
* 포트 : 8010, 8011