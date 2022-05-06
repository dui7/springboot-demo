package com.fentiaozi.hikvision.nvr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan(basePackages="com.fentiaozi.hikvision.nvr")
public class HikvisionApplication {
    public static void main(String[] args) {
        SpringApplication.run(HikvisionApplication.class, args);
    }
}
