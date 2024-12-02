package com.yaeby.spring_rabbitmq_test.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Getter
@Setter
public class AppConfig {

    @Value("${np-web-server.host}")
    private String host;

    @Value("${np-web-server.port}")
    private int port;

    @Value("${np-web-server.base-url}")
    private String baseUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
