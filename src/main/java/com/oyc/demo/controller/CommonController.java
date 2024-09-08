package com.oyc.demo.controller;

import com.oyc.demo.service.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName CommonController
 * @Description 公共配置类
 * @Author oyc
 * @Date 2020/12/2 9:36
 * @Version
 */
@CrossOrigin
@RestController
@RequestMapping
public class CommonController {
    @Autowired
    private ChargeService chargeService;

    /**
     * 登录界面
     *
     * @return
     */
    @RequestMapping("login")
    private String login() {
//        String s = chargeService.startCharge(token, dataMap);
        return "login";
    }

    /**
     * 欢迎界面
     *
     * @return
     */
    @GetMapping({"", "index"})
    private String index() {
        return "index";
    }

    /**
     * 欢迎界面
     *
     * @return
     */
    @GetMapping({"admin"})
    private String admin() {
        return "admin";
    }
}