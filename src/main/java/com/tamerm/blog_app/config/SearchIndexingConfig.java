package com.tamerm.blog_app.config;

import com.tamerm.blog_app.model.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Triggers a full reindex of all Post entities on application startup
 * so that any data already in the database is searchable immediately.
 */
@Component
@Slf4j
public class SearchIndexingConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void indexOnStartup() {
        log.info("Starting Hibernate Search mass indexing...");
        try {
            SearchSession searchSession = Search.session(entityManager);
            searchSession.massIndexer(Post.class)
                    .threadsToLoadObjects(2)
                    .startAndWait();
            log.info("Hibernate Search mass indexing completed.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Hibernate Search mass indexing was interrupted.", e);
        }
    }
}
