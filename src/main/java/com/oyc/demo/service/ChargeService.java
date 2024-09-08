package com.oyc.demo.service;

import java.util.Map;

public interface ChargeService {
    public String startCharge(String token, Map<String, String> dataMap);

    public String stopCharge();

    public String getChargingData();
}
