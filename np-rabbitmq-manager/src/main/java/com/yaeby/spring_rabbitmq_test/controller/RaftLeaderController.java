package com.yaeby.spring_rabbitmq_test.controller;

import com.yaeby.spring_rabbitmq_test.config.RaftLeaderConfig;
import com.yaeby.spring_rabbitmq_test.dto.RaftLeader;
import com.yaeby.spring_rabbitmq_test.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/raft")
@RequiredArgsConstructor
public class RaftLeaderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RaftLeaderController.class);
    private final RaftLeaderConfig raftLeaderConfig;

    @PostMapping("/leader")
    public ResponseEntity<ApiResponse> updateLeader(@RequestBody RaftLeader leader) {
        raftLeaderConfig.setPort(leader.getPort());
        raftLeaderConfig.setNode(leader.getNode());
        raftLeaderConfig.updateBaseUrl();
        LOGGER.info("New leader updated. Node: {} Port: {}", raftLeaderConfig.getNode(), raftLeaderConfig.getPort());
        return ResponseEntity.ok(new ApiResponse("New leader acknowledged", raftLeaderConfig.getNode()));
    }
}
