package com.oyc.demo.controller;

import com.oyc.demo.service.ChargeService;
import com.oyc.demo.util.JsonMapper;
import com.oyc.demo.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/charge")
public class ChargeController {
    @Autowired
    private ChargeService chargeService;

    @RequestMapping("/startCharge")
    public String startCharge() {
        TokenUtil tokenUtil = new TokenUtil();
        String token = tokenUtil.get_token();

        String pileNo = "32010629640853";
        String gunNo = "1";


        Map<String, String> dataMap = new LinkedHashMap<>();
        dataMap.put("chargingAmt", "50");
        dataMap.put("StartChargeSeq", tokenUtil.getStartChargeSeq());
        dataMap.put("QRCode", pileNo + "_" + gunNo);
        dataMap.put("ConnectorID", pileNo + "_" + gunNo);
        dataMap.put("pileNo", pileNo + "_" + gunNo);
        dataMap.put("gunNo", pileNo + "_" + gunNo);
        return chargeService.startCharge(token, dataMap);
    }

    @RequestMapping("/stopCharge")
    public String stopCharge() {
        //{"StartChargeSeq":"MA25CNM38240807100201807002","ConnectorID":"20240624050046_1"}
        return chargeService.stopCharge();
    }



    @RequestMapping("/getChargingData")
    public String getChargingData() {

        return chargeService.getChargingData();
    }

}
