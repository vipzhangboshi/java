package com.oyc.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReqHubBody {

    @JsonProperty("OperatorID")
    public String OperatorID;
    @JsonProperty("Data")
    public String Data;
    @JsonProperty("TimeStamp")
    public String TimeStamp;
    @JsonProperty("Seq")
    public String Seq;
    @JsonProperty("Sig")
    public String Sig;


}
