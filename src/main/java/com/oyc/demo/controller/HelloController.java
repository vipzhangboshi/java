package com.oyc.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: Hello测试控制器
 * @Author oyc
 * @Date 2020/12/1 11:26 下午
 */
@RestController
@RequestMapping("hello")
public class HelloController {

    @GetMapping
    public String hello(){
        return "Hello World";
    }
}
