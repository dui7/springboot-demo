package com.fentiaozi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ruciya
 * @desc
 * @date 2022/3/28
 */
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("getValue")
    public String getValue() {
        return "get请求成功";
    }
}
