# logback
[logback官方地址](https://logback.qos.ch/demo.html)
## 更改包下日志级别

src/main/resources/logback-spring.xml 中可根据包名修改对应的 level

## 多种环境

src/main/resources/application.yml 中修改 active 的值更换环境

## 测试

src/test/java/com/fentiaozi/DemoTest.java 进行测试