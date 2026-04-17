package com.tamerm.blog_app.util.mapper;

import com.tamerm.blog_app.model.Post;
import com.tamerm.blog_app.request.UpdatePostRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for ModelMapper.
 */
@Configuration
public class ModelMapperConfig {

    @Bean
    @Qualifier("modelMapper")
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(UpdatePostRequest.class, Post.class)
                .addMappings(mapper -> mapper.skip(Post::setTags));
        return modelMapper;
    }
}
