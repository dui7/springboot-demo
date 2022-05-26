# admin监控
## 地址
[spring-boot-admin地址](https://github.com/codecentric/spring-boot-admin)

## 使用
### 引入依赖
```xml
            <!-- admin服务端  -->
            <dependency>
                <groupId>de.codecentric</groupId>
                <artifactId>spring-boot-admin-starter-server</artifactId>
                <version>${admin.starter.server.version}</version>
            </dependency>
            <!-- admin客户端  -->
            <dependency>
                <groupId>de.codecentric</groupId>
                <artifactId>spring-boot-admin-starter-client</artifactId>
                <version>${spring.boot.admin.client.version}</version>
            </dependency>
```

### 在启动类增加注解 `@EnableAdminServer`
```java
@EnableAdminServer
@SpringBootApplication
public class SpringBootDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }

}

```

### 修改配置文件
```yaml
# Tomcat
server:
  port: 8081

spring:
  boot:
    admin:
      client:
        url: http://localhost:8081
```

### 启动服务，访问地址
http://localhost:8081

## 监控日志
引入 actuator  
查看 `springboot-actuator` 项目  
[快速转到 springboot-actuator](../springboot-actuator/README.md)