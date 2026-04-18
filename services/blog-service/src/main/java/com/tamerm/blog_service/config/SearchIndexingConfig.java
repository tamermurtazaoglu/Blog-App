package com.tamerm.blog_service.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SearchIndexingConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void buildIndex() {
        try {
            log.info("Starting Hibernate Search mass indexer...");
            Search.session(entityManager)
                    .massIndexer(com.tamerm.blog_service.model.Post.class)
                    .startAndWait();
            log.info("Hibernate Search indexing complete.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Hibernate Search indexing interrupted", e);
        }
    }
}
