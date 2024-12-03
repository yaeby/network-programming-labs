package com.yaeby.spring_rabbitmq_test.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class RaftLeaderConfig {
    @Value("${np-web-server.protocol}")
    private String protocol;

    @Value("${np-web-server.host}")
    private String host;

    @Value("${np-web-server.port}")
    private int port;

    private String baseUrl;

    @PostConstruct
    public void init() {
        updateBaseUrl();
    }

    public void updateBaseUrl() {
        this.baseUrl = protocol + "://" + host + ":" + port;
    }
}
