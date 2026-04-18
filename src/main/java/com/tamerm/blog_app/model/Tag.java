package com.tamerm.blog_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

/**
 * Entity representing a tag.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @KeywordField
    @Column(nullable = false, unique = true)
    private String name;

    public Tag(String tagName) {
        this.name = tagName;
    }
}
