package com.lybrant.homework.provider.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
@ConfigurationProperties(prefix = "my-ip-data-client")
public class MyIpDataRestTemplateConfig {

    @Value("${url}")
    private String uri;

    @Bean
    public RestTemplate myIpDataRestTemplate() {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(uri));

        return restTemplate;
    }
}
