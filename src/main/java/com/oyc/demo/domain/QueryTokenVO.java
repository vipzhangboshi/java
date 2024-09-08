package com.oyc.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QueryTokenVO {
    @JsonProperty("OperatorID")
    private String userOperatorCode;

    @JsonProperty("SuccStat")
    private Integer succStat;

    @JsonProperty("AccessToken")
    private String accessToken;

    @JsonProperty("TokenAvailableTime")
    private Integer tokenAvailableTime;

    @JsonProperty("FailReason")
    private Integer failReason;
}
