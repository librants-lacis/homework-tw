package com.lybrant.homework.provider.client;

import com.lybrant.homework.model.IpData;
import com.lybrant.homework.provider.IpDataClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@ConfigurationProperties(prefix = "my-ip-data-client")
public class MyIpDataClient implements IpDataClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${default-country}")
    private String defaultCountry;

    @Override
    public String getCountryByIp(String remoteAddr) {
        try {
            IpData result = restTemplate.getForObject(remoteAddr, IpData.class);
            return result.getCountry_code();
        } catch (Exception e) {
            return defaultCountry;
        }
    }
}
