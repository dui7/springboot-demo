package com.fentiaozi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ruciya
 * @desc
 * @date 2022/3/28
 */
@RestController
public class TestController {

    @GetMapping("hello")
    public String hello() {
        return "hello";
    }
}
