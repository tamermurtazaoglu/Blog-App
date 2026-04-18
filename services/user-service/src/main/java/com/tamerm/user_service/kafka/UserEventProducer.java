package com.tamerm.user_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventProducer {

    public static final String USER_DELETED_TOPIC = "user-deleted";

    private final KafkaTemplate<String, UserDeletedEvent> kafkaTemplate;

    public void publishUserDeleted(UserDeletedEvent event) {
        kafkaTemplate.send(USER_DELETED_TOPIC, String.valueOf(event.getUserId()), event);
        log.info("Published UserDeletedEvent for userId={}", event.getUserId());
    }
}
