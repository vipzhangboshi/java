package com.oyc.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName CommonController
 * @Description 公共配置类
 * @Author oyc
 * @Date 2020/12/2 9:36
 * @Version
 */
@Controller
@RequestMapping
public class CommonController {
    /**
     * 登录界面
     * @return
     */
    @GetMapping("login")
    private String login() {
        return "login";
    }
    /**
     * 欢迎界面
     * @return
     */
    @GetMapping({"","index"})
    private String index() {
        return "index";
    }
    /**
     * 欢迎界面
     * @return
     */
    @GetMapping({"admin"})
    private String admin() {
        return "admin";
    }
}