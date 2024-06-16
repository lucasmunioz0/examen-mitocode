package com.curso.mitocode.examenfinal.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
public class ReactiveMongoConfig implements InitializingBean {

    @Lazy
    private final MappingMongoConverter converter;

    public ReactiveMongoConfig(MappingMongoConverter converter) {
        this.converter = converter;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }
}
