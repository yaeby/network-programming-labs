package com.yaeby.spring_rabbitmq_test.consumer;

import com.yaeby.spring_rabbitmq_test.dto.Car;
import com.yaeby.spring_rabbitmq_test.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RabbitMQJsonConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQJsonConsumer.class);
    private final RestTemplate restTemplate;

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(Car car) {
        LOGGER.info(String.format("Received message: %s", car.toString()));

        String url = "http://localhost:8080/interauto/car/add";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            HttpEntity<Car> requestEntity = new HttpEntity<>(car, headers);

            ResponseEntity<ApiResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    ApiResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                LOGGER.info("POST request send successfully: " + response.getBody());
            } else {
                LOGGER.error("Failed to send POST request: " + response.getBody());
            }
        } catch (Exception e) {
            LOGGER.error("Error sending car to endpoint", e);
        }
    }

}
