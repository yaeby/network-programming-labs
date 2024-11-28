package com.yaeby.spring_rabbitmq_test.publisher;

import com.yaeby.spring_rabbitmq_test.dto.Car;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQJsonProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingJsonKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQJsonProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public void sendJsonMessage(Car car) {
        LOGGER.info("Sending Json Message: {}", car.toString());
        rabbitTemplate.convertAndSend(exchange, routingJsonKey, car);
    }
}
