package com.lybrant.homework.service;

import com.lybrant.homework.provider.IpDataClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IpDataService {
    @Autowired
    private IpDataClient ipDataClient;

    public String getCountryByIp(String remoteAddr) {
        return ipDataClient.getCountryByIp(remoteAddr);
    }
}
