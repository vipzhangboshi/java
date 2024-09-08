package com.oyc.demo.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.oyc.demo.domain.QueryTokenVO;
import com.oyc.demo.domain.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.oyc.demo.util.Consant.*;


@Slf4j
public class TokenUtil {
    @Autowired
    CacheRepository cacheRepository;

    public String get_token() {
        try {
            String token = GlobalMap.getInstance().getKey();
            if (StrUtil.isNotEmpty(token)){
                return token;
            }
            Map<String, String> dataMap = new LinkedHashMap<>();
            dataMap.put("OperatorID", OperatorID);
            dataMap.put("OperatorSecret", operatorSecret);
            SlightUtil slightUtil = new SlightUtil();
            String body = slightUtil.getBody(dataMap);
            log.info("get_token request_url:{}, RequestBody:{} ", query_token, body);
            Long startTime = System.currentTimeMillis();
            String result = HttpUtil.post(query_token, body);
            Long endTime = System.currentTimeMillis();
            long cost = endTime - startTime;
            log.info(" get_token request_url:{},耗时:{}, ResponseBody:{}", query_token, cost, result);
            if (StrUtil.isEmpty(result)) {
                log.warn("get_token 异常为null");
                return "";
            }
            ServerResponse<Object> resp = JsonMapper.getInstance().fromJson(result, ServerResponse.class);
            if (null != resp && 0 != resp.getRet()) {
                log.warn("get_token resp:{}", JsonHelper.toJson(resp));
                return "";
            }
            String decryptData = AesUtil.decrypt(resp.getData().toString(), dataSecretIV, dataSecret);
            log.info("get_token-resp-decryptData-{}", decryptData);
            QueryTokenVO queryTokenVO = JsonMapper.getInstance().fromJson(decryptData, QueryTokenVO.class);
            if (ObjectUtil.isNotEmpty(queryTokenVO) && ObjectUtil.isNotEmpty(queryTokenVO.getAccessToken())) {
                GlobalMap.getInstance().add("Token", queryTokenVO.getAccessToken());
//                cacheRepository.add("Token", queryTokenVO.getAccessToken());
                log.info("get_token redis设置 :{}", queryTokenVO.getAccessToken());
                return queryTokenVO.getAccessToken();
            } else {
                return "";
            }
        } catch (Exception e) {
            log.error("get_token-获取失败:", e);
            return null;
        }
    }

    public String getRandom(int len) {
        int rs = (int) ((Math.random() * 9 + 1) * Math.pow(10, len - 1));
        return String.valueOf(rs);
    }

    public String getStartChargeSeq() {
        return OperatorID + getRandom(10) + getRandom(8);
    }

    public static void main(String[] args) {
//        String startChargeSeq = "MA25CNM38" + getRandom(10) + getRandom(8);
//        System.out.println(startChargeSeq);


    }

}
