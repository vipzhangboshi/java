package com.oyc.demo.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.oyc.demo.domain.ReqHubBody;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.Map;

import static com.oyc.demo.util.Consant.*;

public class SlightUtil {
    public String getBody(Map dataMap) {
        String time = String.valueOf(DateTime.now().toDate().getTime());
        int length = time.length();
        String timeStamp = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        String seq = time.substring(length - 4, length);
        String dataStr = JSONUtil.toJsonStr(dataMap);
        String data = AesUtil.encrypt(dataStr, dataSecretIV, dataSecret);
        String sigSum = OperatorID + data + timeStamp + seq;
        String sig = HmacUtil.hmacSign(sigSum, sigSecret);
        ReqHubBody reqHubBody = new ReqHubBody(OperatorID, data, timeStamp, seq, sig);
        return JSONUtil.toJsonPrettyStr(reqHubBody).replaceAll("\\\\", "");
    }
}
