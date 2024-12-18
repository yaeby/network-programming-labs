package com.yaeby.np_lab_2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class UdpConfig {

    @Value("${udp.multicast.address}")
    private String MULTICAST_ADDRESS;

    @Value("${udp.multicast.port}")
    private int MULTICAST_PORT;

    @Value("${node.id}")
    private int nodeId;
}
