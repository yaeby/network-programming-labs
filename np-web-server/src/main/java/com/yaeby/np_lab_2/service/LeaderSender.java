package com.yaeby.np_lab_2.service;

import com.yaeby.np_lab_2.config.UdpConfig;
import com.yaeby.np_lab_2.model.RaftLeader;
import com.yaeby.np_lab_2.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LeaderSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeaderSender.class);
    private final RestTemplate restTemplate;
    private final UdpConfig udpConfig;

    public void sendNewLeader() {
        String url = "http://localhost:9090/raft/leader";
        try {
            RaftLeader leaderUpdate = new RaftLeader(
                    "localhost",
                    udpConfig.getServerPort()
            );
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<RaftLeader> requestEntity = new HttpEntity<>(leaderUpdate, headers);
            ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                    url,
                    requestEntity,
                    ApiResponse.class
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                LOGGER.info("POST request sent successfully: {}", response.getBody());
            } else {
                LOGGER.error("Failed to send POST request: {}", response.getBody());
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("HTTP error sending car to endpoint: {}", e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error sending car to endpoint", e);
        }
    }
}