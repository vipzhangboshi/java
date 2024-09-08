package com.oyc.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * 通用服务器返回对象
 *
 * @param <T>
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL) //加上此注解，当序列化json时，如果对象是null，key也不显示
public class ServerResponse<T> implements Serializable {

    private static final long serialVersionUID = -2734608999559701579L;
    @JsonProperty("Ret")
    private int ret;

    @JsonProperty("Msg")
    private String msg;

    @JsonProperty("Data")
    private T data;

    @JsonProperty("Sig")
    private String sign;

    public ServerResponse() {
        super();
    }

    private ServerResponse(int ret) {
        this.ret = ret;
    }

    private ServerResponse(int ret, T data) {
        this.ret = ret;
        this.data = data;
    }

    public ServerResponse(int ret, String msg) {
        this.ret = ret;
        this.msg = msg;
    }

    public ServerResponse(int ret, String msg, T data) {
        this.ret = ret;
        this.msg = msg;
        this.data = data;
    }

    public ServerResponse(int ret, String msg, T data, String sign) {
        this.ret = ret;
        this.msg = msg;
        this.data = data;
        this.sign = sign;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.ret == 0;
    }

    public int getRet() {
        return ret;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}

