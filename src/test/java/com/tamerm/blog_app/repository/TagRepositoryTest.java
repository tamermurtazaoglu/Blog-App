package com.tamerm.blog_app.repository;

import com.tamerm.blog_app.model.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    /**
     * Tests that the repository returns a tag when it exists in the database.
     */
    @Test
    void findByName_ShouldReturnTag_WhenTagExists() {
        Tag tag = new Tag();
        tag.setName("Test Tag");
        tagRepository.save(tag);

        Optional<Tag> foundTag = tagRepository.findByName("Test Tag");

        assertThat(foundTag).isPresent();
        assertThat(foundTag.get().getName()).isEqualTo("Test Tag");
    }

    /**
     * Tests that the repository returns an empty Optional when the tag does not exist.
     */
    @Test
    void findByName_ShouldReturnEmpty_WhenTagDoesNotExist() {
        Optional<Tag> foundTag = tagRepository.findByName("Nonexistent Tag");
        assertThat(foundTag).isNotPresent();
    }
}