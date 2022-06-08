# CAS介绍

## CAS服务端

### 安装包下载

- [cas4.0.0下载地址](https://github.com/apereo/cas/releases/tag/v4.0.0)
- [tomcat8下载地址](https://tomcat.apache.org/download-80.cgi)

### 解压和部署

1. 解压 cas-server-4.0.0-release.zip
1. 解压tomcat文件夹到D:\dev\apache-tomcat8\
1. 将cas-server-4.0.0\modules目录下的cas-server-webapp-4.0.0.war 复制到Tomcat 的 D:\dev\apache-tomcat8\webapps\下 ，修改tomcat下cas-server-webapp-4.0.0.war改名为cas.war
1. 启动tomcat，D:\dev\apache-tomcat\bin\startup.bat

访问CAS登录页面:
[http://localhost:8080/cas/login](http://localhost:8080/cas/login)
账号：casuser
密码：Mellon

### 降低安全基本，取消https

> HTTPS验证相关，true为采用HTTPS验证，false为不采用https验证。

1. 编辑D:\\apache-tomcat8\webapps\cas\WEB-INF\deployerConfigContext.xml，增加`p:requireSecure="false"`

```xml
<bean class="org.jasig.cas.authentication.handler.support.HttpBasedServiceCredentialsAuthenticationHandler"
p:httpClient-ref="httpClient" p:requireSecure="false"/>
```

2. 编辑修改D:\Program Files\apache-tomcat8\webapps\cas\WEB-INF\spring-configuration\ticketGrantingTicketCookieGenerator.xml，把p:cookieSecure="true"修改为`p:cookieSecure="false"`

```xml
<bean id="ticketGrantingTicketCookieGenerator" 
    class="org.jasig.cas.web.support.CookieRetrievingCookieGenerator"      
p:cookieSecure="false"      
p:cookieMaxAge="-1"      
p:cookieName="CASTGC"      
p:cookiePath="/cas" />
```

> 参数p:cookieMaxAge="-1"，简单说是COOKIE的最大生命周期，-1为无生命周期，即只在当前打开的IE窗口有效，IE关闭或重新打开其它窗口，仍会要求验证。可以根据需要修改为大于0的数字，比如3600等，意思是在3600秒内，打开任意IE窗口，都不需要验证。

3. 修改D:\Program Files\apache-tomcat8\webapps\cas\WEB-INF\spring-configuration\warnCookieGenerator.xml,把p:cookieSecure="true"修改为`p:cookieSecure="false"`

```xml
<bean id="warnCookieGenerator" 
class="org.jasig.cas.web.support.CookieRetrievingCookieGenerator"
p:cookieSecure="false"
p:cookieMaxAge="-1"
p:cookieName="CASPRIVACY"
p:cookiePath="/cas" />
```

4. 修改登录页，不显示安全的提示信息(Non-secure Connection)

配置完http方式访问之后页面上的 提示还是存在的，如果我们之后会对登录界面样式完全改版，所以可以不用管它。如果还是需要把它去掉的话，cas统一认证的登陆页面位于： D:\dev\apache-tomcat8\webapps\cas\WEB-INF\view\jsp\default \ui\casLoginView.jsp为登陆页面。
我们找到如下这段代码删掉即可。

```html
<c:if test="${not pageContext.request.secure}">
  <div id="msg" class="errors">
    <h2>Non-secure Connection</h2>
    <p>You are currently accessing CAS over a non-secure connection.  Single Sign On WILL NOT WORK.  In order to have single sign on work, you MUST log in over HTTPS.</p>
  </div>
</c:if>
```

### 配置cas,从数据库读取账号和密码

1. 创建数据库

```shell
# 登录myql
mysql -uroot -proot
# 创建数据库实例
create database cas;
# 使用cas
use cas;
# 创建一张表
create table t_user(id int, username varchar(20),password varchar(20));
# 插入一条记录
insert into t_user(id,username,password) values(1,'admin','admin');
```

2. 修改D:\dev\apache-tomcat8\webapps\cas\WEB-INF\deployerConfigContext.xml

```xml
<!-- 注释掉原本固定登录用户 -->
<!--     <bean id="primaryAuthenticationHandler" -->
<!--           class="org.jasig.cas.authentication.AcceptUsersAuthenticationHandler"> -->
<!--         <property name="users"> -->
<!--             <map> -->
<!--                 <entry key="casuser" value="Mellon"/> -->
<!--             </map> -->
<!--         </property> -->
<!--     </bean> -->
<!-- 变更为JDBC验证方式 -->
<bean id="primaryAuthenticationHandler" class="org.jasig.cas.adaptors.jdbc.QueryDatabaseAuthenticationHandler">
  <property name="dataSource" ref="dataSource"></property>
  <property name="sql" value="select password from t_user where username=?"></property>
</bean>
<!-- 数据源配置 -->
<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
  <property name="driverClassName">
    <value>com.mysql.jdbc.Driver</value></property>
  <property name="url">
    <value>jdbc:mysql://localhost:3306/cas?characterEncoding=utf8</value></property>
  <property name="username"><value>root</value></property>
  <property name="password"><value>root</value></property> 
</bean> 
```

3. 拷贝cas-server-support-jdbc-4.0.0.jar

拷贝 cas-server-4.0.0\modules\cas-server-support-jdbc-4.0.0.jar 到 D:\dev\apache-tomcat8\webapps\cas\WEB-INF\lib

4. 拷贝mysql-connector-java-5.1.26-bin.jar

拷贝 mysql-connector-java-5.1.26-bin.jar 到D:\Program Files\apache-tomcat8\webapps\cas\WEB-INF\lib

5. 重启tomcat，账号admin,密码admin

## CAS客户端

### springboot集成CAS客户端

cas-client依赖

```xml
<dependency>
  <groupId>net.unicon.cas</groupId>
  <artifactId>cas-client-autoconfig-support</artifactId>
  <version>1.4.0-GA</version>
</dependency>
```

```yaml
#单点登录配置
cas:
  server-url-prefix: http://127.0.0.1:8080/cas
  server-login-url: http://127.0.0.1:8080/cas/login
  client-host-url: http://127.0.0.1:8081
  use-session: true
  validation-type: cas3
```

```java
package io.visi.common.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CASAutoConfig {
    @Value("${cas.server-url-prefix}")
    private String casServerUrlPrefix;
    @Value("${cas.server-login-url}")
    private String casServerLoginUrl;
    @Value("${cas.client-host-url}")
    private String casClientHostUrl;
    
    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listener = new ServletListenerRegistrationBean<SingleSignOutHttpSessionListener>();
        listener.setEnabled(true);
        listener.setListener(new SingleSignOutHttpSessionListener());
        listener.setOrder(1);
        return listener;
    }
    
    @Bean
    public FilterRegistrationBean<SingleSignOutFilter> logOutFilter() {
        FilterRegistrationBean<SingleSignOutFilter> authenticationFilter = new FilterRegistrationBean();
        authenticationFilter.setFilter(new SingleSignOutFilter());
        authenticationFilter.setEnabled(true);
        
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("casServerUrlPrefix", casServerUrlPrefix);
        initParameters.put("casServerLoginUrl", casServerLoginUrl);
        initParameters.put("serverName", casClientHostUrl);
        //忽略的url，"|"分隔多个url
        initParameters.put("ignorePattern", "/admin/*");
        authenticationFilter.setInitParameters(initParameters);
        authenticationFilter.addUrlPatterns("/*");
        authenticationFilter.setOrder(1);
        return authenticationFilter;
    }   
}
```

启动程序

```java
package io.visi;

import net.unicon.cas.client.configuration.EnableCasClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCasClient //增加注解，开启单点登录
public class AdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}
    
}
```
启动后访问接口  
http://127.0.0.1:8081/test/getValue