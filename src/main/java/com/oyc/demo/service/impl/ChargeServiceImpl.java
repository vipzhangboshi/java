package com.oyc.demo.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.oyc.demo.domain.ReqHubBody;
import com.oyc.demo.domain.RespHubBody;
import com.oyc.demo.domain.ServerResponse;
import com.oyc.demo.service.ChargeService;
import com.oyc.demo.util.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.oyc.demo.util.Consant.*;

@Slf4j
@Service
public class ChargeServiceImpl implements ChargeService {
    @Autowired
    CacheRepository cacheRepository;

    @Override
    public String startCharge(String token, Map<String, String> dataMap) {
        JsonMapper jsonMapper = JsonMapper.getInstance();
        SlightUtil slightUtil = new SlightUtil();
        String body = slightUtil.getBody(dataMap);
        String result = HttpUtil.headerPost(query_start_charge, body, token);
        RespHubBody resp = jsonMapper.fromJson(result, RespHubBody.class);
        log.info(" query_start_charge request_url:{}, ResponseBody:{}", query_start_charge, JSONObject.toJSONString(resp));
        if (StrUtil.isEmptyIfStr(resp) || resp.getRet() != 0) {
            log.warn("get_token 异常");
            return "";
        }
        String decryptData = AesUtil.decrypt(resp.getData(), dataSecretIV, dataSecret);
        log.info(" query_start_charge request_url:{}, ResponseBody:{}", query_start_charge, decryptData);
        return decryptData;
    }

    @Override
    public String stopCharge() {
        return "";
    }

    @Override
    public String getChargingData() {
        return "";
    }
}
