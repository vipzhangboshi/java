package com.oyc.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RespHubBody {
    @JsonProperty("Ret")
    public Integer ret;
    @JsonProperty("Msg")
    public String msg;
    @JsonProperty("Data")
    public String data;
    @JsonProperty("Sig")
    public String sig;
    @JsonProperty("TimeStamp")
    public String timeStamp;
    @JsonProperty("OperatorID")
    public String operatorID;
}
