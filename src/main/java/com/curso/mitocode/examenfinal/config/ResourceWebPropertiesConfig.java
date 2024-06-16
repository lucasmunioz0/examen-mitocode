package com.curso.mitocode.examenfinal.config;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResourceWebPropertiesConfig {

    @Bean
    public WebProperties.Resources webProperties() {
        return new WebProperties.Resources();
    }
}
