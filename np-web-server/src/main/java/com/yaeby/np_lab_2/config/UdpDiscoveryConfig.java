package com.yaeby.np_lab_2.config;

import com.yaeby.np_lab_2.udp.RaftLeaderElectionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UdpDiscoveryConfig {
    private final RaftLeaderElectionService raftLeaderElectionService;

    @PostConstruct
    public void initUdpDiscovery(){
        raftLeaderElectionService.init();
    }
}