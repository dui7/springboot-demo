# actuator
## [springboot actuator](https://docs.spring.io/spring-boot/docs/2.0.5.RELEASE/reference/htmlsingle/#production-ready)
## 使用
### 引入依赖
```xml
<!-- actuator监控 -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
### 修改配置
```yaml
management:
  server:
    port: 9001
  endpoints:
    web:
      base-path: /fentiaozi  #actuator的访问路径
      exposure:
        include: "*"  # “*”号代表启用所有的监控端点，可以单独启用，例如，`health`，`info`，`metrics`等
logging:
  file:
    name: ./logs/actuator.log
```

### 启动项目后，访问地址  
http://localhost:9001/fentiaozi/logfile