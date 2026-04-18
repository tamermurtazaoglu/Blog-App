package com.tamerm.blog_service.kafka;

import com.tamerm.blog_service.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {

    private final PostService postService;

    @KafkaListener(topics = "user-deleted", groupId = "blog-service")
    public void onUserDeleted(UserDeletedEvent event) {
        log.info("Received UserDeletedEvent for userId={}, deleting posts", event.getUserId());
        postService.deleteAllByUserId(event.getUserId());
    }
}
