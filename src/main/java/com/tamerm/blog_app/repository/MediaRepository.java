package com.tamerm.blog_app.repository;

import com.tamerm.blog_app.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {

    List<Media> findAllByGroupId(String groupId);

    Optional<Media> findByIdAndPost_Id(Long id, Long postId);
}
