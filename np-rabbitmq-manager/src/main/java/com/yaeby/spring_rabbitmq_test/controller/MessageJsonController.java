package com.yaeby.spring_rabbitmq_test.controller;

import com.yaeby.spring_rabbitmq_test.dto.Car;
import com.yaeby.spring_rabbitmq_test.publisher.RabbitMQJsonProducer;
import com.yaeby.spring_rabbitmq_test.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/json")
@RequiredArgsConstructor
public class MessageJsonController {

    private final RabbitMQJsonProducer jsonProducer;

    @PostMapping("/publish")
    public ResponseEntity<ApiResponse> sendJsonMessage(@RequestBody Car car) {
        jsonProducer.sendJsonMessage(car);
        return ResponseEntity.ok(new ApiResponse("Success", car));
    }
}
