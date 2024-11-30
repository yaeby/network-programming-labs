package com.yaeby.spring_rabbitmq_test.consumer;

import com.yaeby.spring_rabbitmq_test.dto.Car;
import com.yaeby.spring_rabbitmq_test.publisher.Sender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQJsonConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQJsonConsumer.class);
    private final Sender sender;

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(Car car) {
        LOGGER.info(String.format("Received message: %s", car.toString()));

        sender.sendAddCarPostRequest(car);
    }
}
