package com.yaeby.np_lab_2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Getter
@Setter
public class UdpConfig {

    @Value("${udp.multicast.address}")
    private String MULTICAST_ADDRESS;

    @Value("${udp.multicast.port}")
    private int MULTICAST_PORT;

    @Value("${server.port}")
    private int serverPort;

    @Value("${node.id}")
    private int nodeId;
}
