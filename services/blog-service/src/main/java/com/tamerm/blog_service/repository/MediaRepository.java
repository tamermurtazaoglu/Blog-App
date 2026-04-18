package com.tamerm.blog_service.repository;

import com.tamerm.blog_service.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findAllByGroupId(String groupId);
}
