package com.tamerm.blog_app.integration.util.mapper;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ModelMapperIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testModelMapperBean() {
        ModelMapper modelMapper = (ModelMapper) applicationContext.getBean("modelMapper");
        assertNotNull(modelMapper, "ModelMapper bean should not be null");
    }
}