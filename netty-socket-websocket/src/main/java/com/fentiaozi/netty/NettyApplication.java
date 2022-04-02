/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * http://www.visight.cn
 * <p>
 * 版权所有，侵权必究！
 */

package com.fentiaozi.netty;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


/**
 * @author visi
 */
@SpringBootApplication
public class NettyApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(NettyApplication.class, args);
    }

}