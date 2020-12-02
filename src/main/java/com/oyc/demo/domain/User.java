package com.oyc.demo.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author oyc
 * @Date 2020/12/2 12:23 上午
 */
@Data
public class User implements Serializable {
    private int id;
    private String name;
    private String account;
    private String password;

}
