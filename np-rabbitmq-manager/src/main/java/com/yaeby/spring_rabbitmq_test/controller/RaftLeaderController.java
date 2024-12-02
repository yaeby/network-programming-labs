package com.yaeby.spring_rabbitmq_test.controller;

import com.yaeby.spring_rabbitmq_test.config.AppConfig;
import com.yaeby.spring_rabbitmq_test.dto.RaftLeader;
import com.yaeby.spring_rabbitmq_test.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/raft")
@RequiredArgsConstructor
public class RaftLeaderController {

    private final AppConfig appConfig;

    @PostMapping("/leader")
    public ResponseEntity<ApiResponse> updateLeader(@RequestBody RaftLeader leader) {
        appConfig.setHost(leader.getHost());
        appConfig.setPort(leader.getPort());
        return ResponseEntity.ok(new ApiResponse("New leader set successful", null));
    }
}
